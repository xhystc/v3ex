package com.xhystc.zhihu.rediscache;

import com.xhystc.zhihu.commons.RedisUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class TimeoutEventManager extends TimerTask
{
	private List<TimeoutHandle> handles = new ArrayList<>();
	static final private Logger logger = Logger.getLogger(TimeoutEventManager.class);
	private long timeout = 1000*60*3;
	private JedisPool pool;
	private String type = "";


	public TimeoutEventManager(List<TimeoutHandle> handles, long timeout, JedisPool pool, String type)
	{
		this.handles = handles;
		this.timeout = timeout;
		this.pool = pool;
		this.type = type;
	}

	@Override
	public void run()
	{
		logger.debug("active timer out type:"+type);
		try(Jedis redis = pool.getResource())
		{
			doTimeout(redis,type);
		}
	}

	private void doTimeout(Jedis redis,String type){
		List<Long> ids = RedisUtils.getActive(redis,0,System.currentTimeMillis()-timeout,type);
		for(Long id : ids){
			logger.debug("active-"+type+"time out id:"+id);
			for(TimeoutHandle handle : handles){
				handle.onTimeout(redis,type,id);
			}
			RedisUtils.delActive(redis,type,id);
		}
	}

	public void initTimer(){
		Timer timer = new Timer("redis active timer type:"+type,true);
		timer.schedule(this,timeout,timeout);
	}


	public List<TimeoutHandle> getHandles()
	{
		return handles;
	}

	public void setHandles(List<TimeoutHandle> handles)
	{
		this.handles = handles;
	}

	public long getTimeout()
	{
		return timeout;
	}

	public void setTimeout(long timeout)
	{
		this.timeout = timeout;
	}

	public JedisPool getPool()
	{
		return pool;
	}

	public void setPool(JedisPool pool)
	{
		this.pool = pool;
	}

	public String getType()
	{
		return type;
	}

	public void setType(String type)
	{
		this.type = type;
	}
}
