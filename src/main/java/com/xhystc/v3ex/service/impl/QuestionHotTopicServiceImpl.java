package com.xhystc.v3ex.service.impl;

import com.xhystc.v3ex.dao.QuestionDao;
import com.xhystc.v3ex.model.vo.query.QuestionQueryCondition;
import com.xhystc.v3ex.service.HotTopicService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.Pipeline;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Component
public class QuestionHotTopicServiceImpl implements HotTopicService,InitializingBean,Runnable
{
	static private final String NOW_KEY = "hotTopic-now";
	static private final String NEXT_KEY = "hotTopic-next";
	static private final Logger logger = Logger.getLogger(QuestionHotTopicServiceImpl.class);

	private QuestionDao questionDao;
	private JedisPool jedisPool;

	@Value("#{configProperties['hotTopic.size']}")
	private int topicSize = 1000;

	@Value("#{configProperties['hotTopic.threshold']}")
	private int threshold = 1;

	@Value("#{configProperties['hotTopic.timeout']}")
	private long timeout = 1000*3*60;

	@Autowired
	public QuestionHotTopicServiceImpl(QuestionDao questionDao,JedisPool jedisPool)
	{
		this.questionDao = questionDao;
		this.jedisPool = jedisPool;
	}

	@Override
	public void incScore(Long id, double score)
	{
		try(Jedis redis = jedisPool.getResource())
		{
			redis.zincrby(NEXT_KEY,score,id.toString());
		}
	}

	@Override
	public List getHotTopics()
	{
		try(Jedis redis = jedisPool.getResource())
		{
			Set<String> idSet = redis.smembers(NOW_KEY);
			if (idSet.size()==0)
			{
				return Collections.EMPTY_LIST;
			}
			QuestionQueryCondition condition = new QuestionQueryCondition();
			condition.setInclude(new HashSet<>(idSet.size()));
			for(String s : idSet){
				condition.getInclude().add(Long.parseLong(s));
			}

			return questionDao.selectQuestions(condition);
		}
	}

	@Override
	public void run()
	{
		try(Jedis redis = jedisPool.getResource())
		{
			redis.del(NOW_KEY);
			Set<String> idSet = redis.zrangeByScore(NEXT_KEY,threshold,Integer.MAX_VALUE);
			Pipeline pipeline = redis.pipelined();
			for(String s : idSet){
				pipeline.zrem(NEXT_KEY,s);
				pipeline.sadd(NOW_KEY,s);
			}
			pipeline.zremrangeByScore(NEXT_KEY,Integer.MIN_VALUE,0);
			pipeline.sync();
		}
	}

	@Override
	public void afterPropertiesSet() throws Exception
	{
		logger.debug("hottopic service do init");
		ScheduledExecutorService service = Executors.newScheduledThreadPool(1, r -> {
			Thread t = Executors.defaultThreadFactory().newThread(r);
			t.setName("hottopic-service-flush");
			t.setDaemon(true);
			return t;
		});
		service.scheduleWithFixedDelay(this,timeout,timeout, TimeUnit.MILLISECONDS);
	}
}




