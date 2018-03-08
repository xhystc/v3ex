package com.xhystc.v3ex.service.impl;

import com.xhystc.v3ex.async.handler.QuestionRankUpdateHandler;
import com.xhystc.v3ex.commons.QuestionRankUtils;
import com.xhystc.v3ex.commons.RedisUtils;
import com.xhystc.v3ex.dao.QuestionDao;
import com.xhystc.v3ex.dao.TagDao;
import com.xhystc.v3ex.dao.solr.SolrDao;
import com.xhystc.v3ex.model.Question;
import com.xhystc.v3ex.model.vo.SolrHighLightInform;
import com.xhystc.v3ex.model.vo.SolrSearchResult;
import com.xhystc.v3ex.service.QuestionSearchService;
import org.apache.log4j.Logger;
import org.apache.solr.client.solrj.util.ClientUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;


import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Component
public class QuestionSearchServiceImpl implements QuestionSearchService, InitializingBean, Runnable
{
	static private final Logger logger = Logger.getLogger(QuestionSearchServiceImpl.class);

	private SolrDao solrDao;
	private QuestionDao questionDao;
	private long timeout = 60;
	private JedisPool jedisPool;
	private TagDao tagDao;
	@Autowired
	QuestionRankUpdateHandler handler;

	@Autowired
	public QuestionSearchServiceImpl(SolrDao solrDao, QuestionDao questionDao, TagDao tagDao, JedisPool jedisPool)
	{
		this.solrDao = solrDao;
		this.questionDao = questionDao;
		this.jedisPool = jedisPool;
		this.tagDao = tagDao;
	}

	@Override
	public SolrSearchResult searchQuestions(String queryString, String tag, int start, int rows)
	{
		queryString = ClientUtils.escapeQueryChars(queryString);
		SolrSearchResult result = solrDao.queryQuestion(queryString, tag, start, rows);
		if (result.getTotal() <= 0)
		{
			return result;
		}

		Map<String, Object> condition = new HashMap<>(1);
		Set<Long> ids = new HashSet<>((int) result.getTotal());
		for (SolrHighLightInform res : result.getHighLightInforms())
		{
			ids.add(res.getId());
		}
		condition.put("include", ids);
		List<Question> qs = questionDao.selectQuestions(condition);
		List<Question> questions = new ArrayList<>(qs.size());

		for (SolrHighLightInform res : result.getHighLightInforms())
		{
			for (Question question : qs)
			{
				if (question.getId().equals(res.getId()))
				{
					if (res.getContentHighlight() != null)
					{
						question.setContent(res.getContentHighlight());
					}
					if (res.getTitleHighlight() != null)
					{
						question.setTitle(res.getTitleHighlight());
					}
					questions.add(question);
					qs.remove(question);
					break;
				}
			}
		}
		result.setQuestions(questions);
		return result;
	}

	@Override
	public boolean addQuestion(Long questionId, double score)
	{
		Question question = questionDao.getQuestionById(questionId);
		return question != null && solrDao.addQuestion(question, score);
	}

	@Override
	public boolean deleteQuestion(Long questionId)
	{
		return solrDao.deleteQuestion(questionId);
	}

	@Override
	public boolean updateQuestion(Long questionId, String field, String value)
	{
		return solrDao.updateQuestion(questionId, field, value);
	}


	@Override
	public void run()
	{
		logger.debug("solr time out");
		try (Jedis redis = jedisPool.getResource())
		{
			Set<String> ids = redis.smembers(RedisUtils.questionScoreModityKey());
			redis.del(RedisUtils.questionScoreModityKey());
			for (String s : ids)
			{
				Long id = Long.decode(s);
				double score = QuestionRankUtils.currentQuestionRankScore(id, redis);
				if (score <= 0)
				{
					return;
				}
				Question question = questionDao.getQuestionById(id);
				if (question == null)
				{
					return;
				}
				logger.debug("score:" + score);
				solrDao.addQuestion(question, score);
			}

		} catch (Exception e)
		{
			logger.info(e.getMessage());
		}
	}

	@Override
	public void afterPropertiesSet()
	{
		logger.debug("question service do init");
		ScheduledExecutorService service = Executors.newScheduledThreadPool(1, r -> {
			Thread t = Executors.defaultThreadFactory().newThread(r);
			t.setName("solrSearch-service-flush");
			t.setDaemon(true);
			return t;
		});
		service.scheduleWithFixedDelay(this, timeout, timeout, TimeUnit.SECONDS);
	}

	public SolrDao getSolrDao()
	{
		return solrDao;
	}

	public void setSolrDao(SolrDao solrDao)
	{
		this.solrDao = solrDao;
	}

	public long getTimeout()
	{
		return timeout;
	}

	public void setTimeout(long timeout)
	{
		this.timeout = timeout;
	}
}



