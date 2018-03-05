package com.xhystc.v3ex.async.handler;

import com.xhystc.v3ex.async.Event;
import com.xhystc.v3ex.async.EventHandler;
import com.xhystc.v3ex.async.EventType;
import com.xhystc.v3ex.commons.RedisUtils;
import com.xhystc.v3ex.model.EntityType;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

@Component
public class SolrRankScoreUpdateHandle implements EventHandler
{
	private static final Logger logger = Logger.getLogger(SolrRankScoreUpdateHandle.class);
	private final JedisPool jedisPool;

	@Autowired
	public SolrRankScoreUpdateHandle(JedisPool jedisPool)
	{
		this.jedisPool = jedisPool;
	}

	@Override
	public void handle(Event event)
	{
		if(EventType.QUESTION_RANK_UPDATE.equals(event.getEventType())|| EventType.PUBLISH_QUESTION.equals(event.getEventType())){
			logger.debug("handle update event");
			try(Jedis redis = jedisPool.getResource())
			{
				assert event.getEntityType().equals(EntityType.QUESTION);
				redis.sadd(RedisUtils.questionScoreModityKey(),String.valueOf(event.getEntityId()));
			}
		}
	}
}






