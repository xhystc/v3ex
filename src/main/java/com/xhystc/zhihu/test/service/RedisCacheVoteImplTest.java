package com.xhystc.zhihu.test.service;

import com.xhystc.zhihu.commons.RedisUtils;
import com.xhystc.zhihu.model.Vote;
import com.xhystc.zhihu.service.impl.RedisCacheVoteImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import redis.clients.jedis.JedisPool;


@RunWith(SpringJUnit4ClassRunner.class)  //使用junit4进行测试
@ContextConfiguration(locations = {"classpath:conf/applicationContext-service.xml",
		"classpath:conf/applicationContext.xml", "classpath:conf/applicationContext-mybatis.xml","classpath:conf/applicationContext-redis.xml"})
public class RedisCacheVoteImplTest
{
	@Autowired
	RedisCacheVoteImpl service;

	@Autowired
	JedisPool pool;

	@Transactional(rollbackFor = Exception.class)
	@Rollback(false)
	@Test
	public void doVote() throws Exception
	{
		Vote vote = new Vote();
		vote.setParentType("question");
		vote.setUserId(33L);
		vote.setParentId(37L);
		System.out.println(service.doVote(vote));


	}

	@Transactional(rollbackFor = Exception.class)
	@Rollback(false)
	@Test
	public void disVote() throws Exception
	{
		Vote vote = new Vote();
		vote.setParentType("question");
		vote.setUserId(33L);
		vote.setParentId(33L);

		System.out.println(service.disVote(vote));
	}

	@Transactional(rollbackFor = Exception.class)
	@Rollback(false)
	@Test
	public void isVoted() throws Exception
	{
	}

	@Transactional(rollbackFor = Exception.class)
	@Rollback(false)
	@Test
	public void activeTest(){
		System.out.println(RedisUtils.getActive(pool.getResource(),0,System.currentTimeMillis(),"question"));
		System.out.println(RedisUtils.getActive(pool.getResource(),0,System.currentTimeMillis(),"user"));
	}

	@Transactional(rollbackFor = Exception.class)
	@Rollback(false)
	@Test
	public void timeoutTest(){

		service.onTimeout(pool.getResource(),"user",34L);
	}

}









