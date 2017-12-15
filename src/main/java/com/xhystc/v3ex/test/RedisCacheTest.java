/*
package com.xhystc.zhihu.test;


import com.xhystc.zhihu.dao.QuestionDao;
import com.xhystc.zhihu.model.vo.query.QuestionQueryCondition;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.io.UnsupportedEncodingException;
import java.util.Set;

@RunWith(SpringJUnit4ClassRunner.class)  //使用junit4进行测试
@ContextConfiguration(locations = {"classpath:conf/applicationContext.xml", "classpath:conf/applicationContext-redis.xml","classpath:conf/applicationContext-mybatis.xml"})
public class RedisCacheTest
{

	@Autowired
	JedisPool pool;

	@Autowired
	QuestionDao questionDao;

	@Test
	public void test() throws UnsupportedEncodingException
	{
		QuestionQueryCondition condition = new QuestionQueryCondition();
		condition.setOffset(0);
		condition.setRows(20);

		*/
/*List<Question> questions = questionDao.selectQuestion(condition);
		for(Question question : questions){
			redis.opsForZSet().add("questionList",question,question.getActiveTime().getTime());
		}
*//*

	//	Set<Object> set = redis.opsForZSet().reverseRange("questionList",0,10);

		Jedis jedis = pool.getResource();
		jedis.sadd("test2","123");

		Set<String> set = jedis.smembers("test2");
		System.out.println(set);
	}

}









*/
