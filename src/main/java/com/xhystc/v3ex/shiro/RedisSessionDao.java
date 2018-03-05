package com.xhystc.v3ex.shiro;

import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.ValidatingSession;
import org.apache.shiro.session.mgt.eis.CachingSessionDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.concurrent.TimeUnit;

@Component("RedisSessionDAO")
public class RedisSessionDao extends CachingSessionDAO
{
	@Qualifier("stringRedisTemplate")
	@Autowired
	RedisTemplate<String,Object> jedis;

	@Value("#{configProperties['shiro.session.maxAge']}")
	private long exp = 1800000;
	final static String prefix = "shiro_session_dao:";

	@Override
	protected void doUpdate(Session session)
	{
		if(session instanceof ValidatingSession && !((ValidatingSession)session).isValid())
		{
			return;
		}
		jedis.opsForValue().set(getSessionKey(session),session,exp, TimeUnit.SECONDS);
	}

	@Override
	protected void doDelete(Session session)
	{
		jedis.delete(getSessionKey(session));
	}

	@Override
	protected Serializable doCreate(Session session)
	{
		Serializable sessionId = generateSessionId(session);
		assignSessionId(session, sessionId);
		jedis.opsForValue().set(getSessionKey(session),session,exp,TimeUnit.SECONDS);
		return sessionId;
	}

	@Override
	protected Session doReadSession(Serializable serializable)
	{
		return (Session) jedis.opsForValue().get(getSessionKey(serializable));
	}

	private String getSessionKey(Session session){
		return prefix+session.getId().toString();
	}
	private String getSessionKey(Serializable serializable){
		return prefix+serializable.toString();
	}


	public RedisTemplate<String, Object> getJedis()
	{
		return jedis;
	}

	public void setJedis(RedisTemplate<String, Object> jedis)
	{
		this.jedis = jedis;
	}

	public long getExp()
	{
		return exp;
	}

	public void setExp(long exp)
	{
		this.exp = exp;
	}
}
