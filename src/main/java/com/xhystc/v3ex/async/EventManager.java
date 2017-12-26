package com.xhystc.v3ex.async;

import com.alibaba.fastjson.JSON;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

public class EventManager implements Runnable,InitializingBean
{
	static private final String EVENT_QUEUE_KEY = "v3ex_event_queue";
	static private final Logger logger = Logger.getLogger(EventManager.class);
	private List<EventHandler> handlers = new LinkedList<>();
	@Autowired
	private JedisPool jedisPool;


	public void publishEvent(Event event){
		try(Jedis redis = jedisPool.getResource())
		{
			redis.rpush(EVENT_QUEUE_KEY, JSON.toJSONString(event));
		}
	}

	public Event pollEvent(){
		try(Jedis redis = jedisPool.getResource())
		{
			return JSON.parseObject(redis.blpop(0,EVENT_QUEUE_KEY).get(1),Event.class);
		}

	}

	@Override
	public void run()
	{
		logger.debug("event manager thread running");
		while (true){
			Event event = pollEvent();
			if (event!=null){
				logger.debug("manager get event:"+event.getEntityType());
				for(EventHandler handler : handlers){
					handler.handle(event);
				}
			}else {
				return;
			}
		}
	}

	@Override
	public void afterPropertiesSet()
	{
		ExecutorService service = Executors.newFixedThreadPool(1, new ThreadFactory()
		{
			@Override
			public Thread newThread(Runnable r)
			{
				Thread thread = new Thread(r);
				thread.setName("event handle thread");
				thread.setDaemon(true);
				return thread;
			}
		});
		service.submit(this);

	}

	public List<EventHandler> getHandlers()
	{
		return handlers;
	}

	public void setHandlers(List<EventHandler> handlers)
	{
		this.handlers = handlers;
	}
}




