package com.xhystc.v3ex.test.service;

import com.xhystc.v3ex.model.Question;
import com.xhystc.v3ex.service.QuestionService;
import com.xhystc.v3ex.service.impl.RedisCacheQuestionServiceImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)  //使用junit4进行测试
@ContextConfiguration(locations = {"classpath:conf/applicationContext.xml", "classpath:conf/applicationContext-mybatis.xml","classpath:conf/applicationContext-service.xml"})
public class RedisCacheQuestionServiceImplTest
{
	@Qualifier("redisCacheQuestionServiceImpl")
	@Autowired
	QuestionService questionService;

	@Test
	public void getQuestion()
	{
		long start = System.currentTimeMillis();
		Question question = questionService.getQuestion(48L);
		System.out.println(question.getTitle()+"cost:"+(System.currentTimeMillis()-start));
	}

	@Test
	public void getQuestions()
	{
		long start = System.currentTimeMillis();
		List<Question> question = questionService.getQuestions(1L,1,20);
		System.out.println(question.size()+"cost:"+(System.currentTimeMillis()-start));
	}

	@Test
	public void publishQuetion()
	{
	}

	@Test
	public void upQuestion()
	{
	}

	@Test
	public void total()
	{
		System.out.println("total:"+questionService.total(1L));
	}

	@Test
	public void afterPropertiesSet()
	{
		questionService.storeQuestions();
	}


}