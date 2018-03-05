package com.xhystc.v3ex.service.impl;

import com.xhystc.v3ex.commons.RedisUtils;
import com.xhystc.v3ex.dao.QuestionDao;
import com.xhystc.v3ex.dao.TagDao;
import com.xhystc.v3ex.model.Question;
import com.xhystc.v3ex.model.Tag;
import com.xhystc.v3ex.model.User;
import com.xhystc.v3ex.model.vo.form.QuestionForm;
import com.xhystc.v3ex.service.QuestionService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import redis.clients.jedis.*;

import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


@Component("redisCacheQuestionServiceImpl")
public class RedisCacheQuestionServiceImpl implements QuestionService,InitializingBean,Runnable
{
	static final private Logger logger = Logger.getLogger(RedisCacheQuestionServiceImpl.class);

	@Value("#{configProperties['question.active.timeout']}")
	private long timeout = 60*1000*3;

	@Value("#{configProperties['question.active.size']}")
	private int activeSize = 100000;

	private JedisPool jedisPool;
	private QuestionDao questionDao;
	private TagDao tagDao;


	@Autowired
	public RedisCacheQuestionServiceImpl(JedisPool jedisPool, QuestionDao questionDao,TagDao tagDao)
	{
		this.jedisPool = jedisPool;
		this.questionDao = questionDao;
		this.jedisPool = jedisPool;
		this.tagDao = tagDao;
	}


	@Override
	public Question getQuestionById(Long id)
	{
		return questionDao.getQuestionById(id);
	}

	@Override
	public List<Question> getQuestions(Long tagId, int page, int pageSize)
	{
		Set<String> questionIds;
		try(Jedis redis = jedisPool.getResource())
		{
			int offset = (page-1)*pageSize;
			questionIds = redis.zrevrange(RedisUtils.questionActiveKey(tagId),offset,offset+pageSize-1);
		}
		if(questionIds.size()==0)
		{
			return Collections.emptyList();
		}
		Map<Long,Integer> indexMap = new HashMap<>(questionIds.size());
		Set<Long> ids = new HashSet<>(questionIds.size());

		int i=0;
		for(String idStr : questionIds){
			Long questionId = Long.parseLong(idStr);
			indexMap.put(questionId,i++);
			ids.add(questionId);
		}

		Map<String,Object> condition = new HashMap<>(5);
		condition.put("include",ids);
		List<Question> questions = questionDao.selectQuestions(condition);
		Question[] questionArray = new Question[questions.size()];

		for(Question question : questions){
			questionArray[indexMap.get(question.getId())] = question;
		}

		return Arrays.asList(questionArray);
	}

	@Transactional(rollbackFor = RuntimeException.class,isolation = Isolation.READ_COMMITTED)
	@Override
	public Long publishQuetion(User user, QuestionForm form)
	{
		Question question = new Question();
		question.setContent(form.getContent());
		question.setCreateDate(new Date());
		question.setTitle(form.getTitle());
		question.setUser(user);
		Tag tag = new Tag();
		tag.setId(form.getTag());
		question.setTag(tag);
		questionDao.insertQuestion(question);

		tagDao.increaseCount(form.getTag());

		return question.getId();
	}



	@Override
	public int total(Long tagId)
	{
		try(Jedis redis = jedisPool.getResource())
		{
			Long s = redis.zcard(RedisUtils.questionActiveKey(tagId));
			return s.intValue();
		}
	}


	@Override
	public void run()
	{
		logger.debug("question time out");
		try(Jedis redis = jedisPool.getResource())
		{
			List<Tag> tags = tagDao.selectAll();
			Map<Long,Response<Long>> responseMap = new HashMap<>(tags.size());
			Pipeline pipeline = redis.pipelined();
			for(Tag tag : tags){
				responseMap.put(tag.getId(),pipeline.zcard(RedisUtils.questionActiveKey(tag.getId())));
			}
			pipeline.sync();

			pipeline = redis.pipelined();
			for(Tag tag : tags){
				int size = responseMap.get(tag.getId()).get().intValue();
				if(size<=activeSize){
					continue;
				}
				pipeline.zremrangeByRank(RedisUtils.questionActiveKey(tag.getId()),0,size - activeSize-1);
			}
			pipeline.sync();
			int size = redis.zcard(RedisUtils.questionActiveKey()).intValue();
			if(size>activeSize){
				redis.zremrangeByRank(RedisUtils.questionActiveKey(),0,size - activeSize-1);
			}

		}catch (Exception e){
			logger.info(e.getMessage());
		}
	}

	@Override
	public void afterPropertiesSet() throws Exception
	{
		logger.debug("question service do init");
		ScheduledExecutorService service = Executors.newScheduledThreadPool(1, r -> {
			Thread t = Executors.defaultThreadFactory().newThread(r);
			t.setName("question-service-flush");
			t.setDaemon(true);
			return t;
		});
		service.scheduleWithFixedDelay(this,timeout,timeout, TimeUnit.MILLISECONDS);
	}

}






