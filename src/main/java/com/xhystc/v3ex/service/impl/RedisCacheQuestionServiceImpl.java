package com.xhystc.v3ex.service.impl;

import com.xhystc.v3ex.dao.QuestionDao;
import com.xhystc.v3ex.dao.TagDao;
import com.xhystc.v3ex.model.Question;
import com.xhystc.v3ex.model.Tag;
import com.xhystc.v3ex.model.User;
import com.xhystc.v3ex.model.vo.form.QuestionForm;
import com.xhystc.v3ex.model.vo.query.QuestionQueryCondition;
import com.xhystc.v3ex.service.QuestionService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import redis.clients.jedis.*;

import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Transactional(rollbackFor = RuntimeException.class)
@Component("redisCacheQuestionServiceImpl")
public class RedisCacheQuestionServiceImpl implements QuestionService,InitializingBean,Runnable
{

	static final private Logger logger = Logger.getLogger(RedisCacheQuestionServiceImpl.class);
	static final private String ACTIVE_PREFIX = "question-active";
	static final private int FETCH_SIZE = 5000;

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
			questionIds = redis.zrevrange(activeKey(tagId),offset,offset+pageSize-1);
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

		QuestionQueryCondition condition = new QuestionQueryCondition();
		condition.setInclude(ids);
		condition.setNeedOrder(false);
		condition.setNeedLimit(false);
		List<Question> questions = questionDao.selectQuestions(condition);
		Question[] questionArray = new Question[questions.size()];

		for(Question question : questions){
			questionArray[indexMap.get(question.getId())] = question;
		}

		return Arrays.asList(questionArray);
	}

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
		upQuestion(question);
		return question.getId();
	}

	@Override
	public void upQuestion(Question question)
	{
		try(Jedis redis = jedisPool.getResource())
		{
			Long tagId = question.getTag().getId();

			Pipeline pipeline = redis.pipelined();
			pipeline.zadd(activeKey(tagId),System.currentTimeMillis(),question.getId().toString());
			pipeline.zadd(activeKey(),System.currentTimeMillis(),question.getId().toString());
			pipeline.sync();
		}
	}

	@Override
	public int total(Long tagId)
	{
		try(Jedis redis = jedisPool.getResource())
		{
			Long s = redis.zcard(activeKey(tagId));
			return s.intValue();
		}
	}
	@Override
	public void storeQuestions(){
		logger.info("redis cached question service start");
		Long start = System.currentTimeMillis();

		try(Jedis redis = jedisPool.getResource())
		{
			for(int i=0;;i++){
				List<Question> questions = getDBQuestions(i*FETCH_SIZE,FETCH_SIZE);
				if (questions.size()==0)
				{
					break;
				}
				storeActive(redis,questions);
			}
		}
		System.out.println("store over:"+((System.currentTimeMillis()-start)/1000)+"s");
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
				responseMap.put(tag.getId(),pipeline.zcard(activeKey(tag.getId())));
			}
			pipeline.sync();

			pipeline = redis.pipelined();
			for(Tag tag : tags){
				int size = responseMap.get(tag.getId()).get().intValue();
				if(size<=activeSize){
					continue;
				}
				pipeline.zremrangeByRank(activeKey(tag.getId()),0,size - activeSize-1);
			}
			pipeline.sync();
			int size = redis.zcard(activeKey()).intValue();
			if(size>activeSize){
				redis.zremrangeByRank(activeKey(),0,size - activeSize-1);
			}

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



	private List<Question> getDBQuestions(int offset,int rows){
		QuestionQueryCondition condition = new QuestionQueryCondition();
		condition.setOffset(offset);
		condition.setRows(rows);
		condition.setNeedOrder(false);
		return questionDao.selectQuestions(condition);
	}
	private void storeActive(Jedis redis, List<Question> questions){
		Pipeline pipeline = redis.pipelined();
		for (Question question : questions){
			pipeline.zadd(activeKey(question.getTag().getId()),question.getCreateDate().getTime(),question.getId().toString());
			pipeline.zadd(activeKey(),question.getCreateDate().getTime(),question.getId().toString());
		}
		pipeline.sync();
	}

	private static String activeKey(){
		return ACTIVE_PREFIX;
	}
	private static String activeKey(Long tagId){
		return tagId==null?ACTIVE_PREFIX:ACTIVE_PREFIX+"-"+tagId;
	}

	public long getTimeout()
	{
		return timeout;
	}

	public void setTimeout(long timeout)
	{
		this.timeout = timeout;
	}

	public int getActiveSize()
	{
		return activeSize;
	}

	public void setActiveSize(int activeSize)
	{
		this.activeSize = activeSize;
	}
}






