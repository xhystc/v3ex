package com.xhystc.v3ex.test.service;

import com.xhystc.v3ex.commons.RedisUtils;
import com.xhystc.v3ex.dao.QuestionDao;
import com.xhystc.v3ex.model.EntityType;
import com.xhystc.v3ex.model.Question;
import com.xhystc.v3ex.service.VoteService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.*;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)  //使用junit4进行测试
@ContextConfiguration(locations = {"classpath:conf/applicationContext.xml", "classpath:conf/applicationContext-mybatis.xml","classpath:conf/applicationContext-service.xml"})
public class RedisCacheVoteImplTest
{
	@Autowired
	VoteService voteService;

	@Autowired
	QuestionDao questionDao;
	@Autowired
	JedisPool jedisPool;

	@Test
	public void doVote()
	{
		System.out.println(voteService.doVote(7L, EntityType.QUESTION,38L));
		System.out.println(voteService.doVote(7L,EntityType.QUESTION,31L));
	}

	@Test
	public void disVote()
	{
		System.out.println(voteService.disVote(34L,EntityType.QUESTION,1L));
		System.out.println(voteService.disVote(35L,EntityType.QUESTION,1L));
	}

	@Test
	public void fetchUserVote()
	{
		Question q = new Question();
		q.setId(2L);
		voteService.fetchUserVote(35L,q);
		System.out.println(q.getIsVoted());
	}

	@Test
	public void fetchUserVotes()
	{
		Map<String,Object> condition = new HashMap<>();
		condition.put("offset",0);
		condition.put("rows",1000);
		List<Question> questions = questionDao.selectQuestions(condition);
		voteService.fetchUserVotes(38L,questions);
		for(Question question :questions){
			System.out.println(question.getId()+":"+question.getIsVoted()+":"+question.getVoteCount());
		}
	}

	@Test
	public void isVote()
	{
		System.out.println(voteService.isVote(35L,EntityType.QUESTION,2L));
		System.out.println(voteService.isVote(35L,EntityType.QUESTION,33L));
	}



	@Test
	public void runTest()
	{
	//	voteService.run();
		try(Jedis redis = jedisPool.getResource())
		{
			Set<String> ids = new HashSet<>();
			ids.add("31");
			ids.add("38");
			RedisUtils.removeFromSet(redis,"vote-userVotes-question-7",ids);
		}
	}
}














