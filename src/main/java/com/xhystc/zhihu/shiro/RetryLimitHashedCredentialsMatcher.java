package com.xhystc.zhihu.shiro;

import com.xhystc.zhihu.commons.FormUtils;
import com.xhystc.zhihu.service.UserService;
import org.apache.log4j.Logger;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.ExcessiveAttemptsException;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class RetryLimitHashedCredentialsMatcher  extends HashedCredentialsMatcher
{
	Logger logger = Logger.getLogger(RetryLimitHashedCredentialsMatcher.class);

	@Qualifier("stringRedisTemplate")
	@Autowired
	RedisTemplate<String,AtomicInteger> redis;

	@Autowired
	UserService userService;

	private int retryTimes = 5;
	private String prefix = "passwordRetryCount";
	private int interval = 30;


	@Override
	public boolean doCredentialsMatch(AuthenticationToken token, AuthenticationInfo info) {
		String username = (String)token.getPrincipal();
		String usernameKey = prefix+":"+username;

		AtomicInteger retryCount = redis.opsForValue().get(usernameKey);
		logger.debug("retryCount:"+retryCount);

		if(retryCount == null) {
			retryCount = new AtomicInteger(1);
			logger.debug("retryCount == null");
		} else if(retryCount.incrementAndGet() > retryTimes) {
			logger.debug("retryCount > retryTimes");
			throw new ExcessiveAttemptsException();
		}

		boolean matches = super.doCredentialsMatch(token, info);
		if(matches) {
			redis.delete(usernameKey);
			FormUtils.setCurrentUser(userService.getUserByName(username));
		}else {
			redis.opsForValue().set(usernameKey,retryCount,interval, TimeUnit.SECONDS);
		}
		return matches;
	}

	public int getRetryTimes()
	{
		return retryTimes;
	}

	public void setRetryTimes(int retryTimes)
	{
		this.retryTimes = retryTimes;
	}

	public String getPrefix()
	{
		return prefix;
	}

	public void setPrefix(String prefix)
	{
		this.prefix = prefix;
	}

	public RedisTemplate<String, AtomicInteger> getRedis()
	{
		return redis;
	}

	public void setRedis(RedisTemplate<String, AtomicInteger> redis)
	{
		this.redis = redis;
	}

	public int getInterval()
	{
		return interval;
	}

	public void setInterval(int interval)
	{
		this.interval = interval;
	}
}
