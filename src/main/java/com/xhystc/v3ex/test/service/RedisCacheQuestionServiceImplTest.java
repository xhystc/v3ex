package com.xhystc.v3ex.test.service;

import com.xhystc.v3ex.commons.CommonUtils;
import com.xhystc.v3ex.model.Question;
import com.xhystc.v3ex.service.QuestionService;
import com.xhystc.v3ex.service.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)  //使用junit4进行测试
@ContextConfiguration(locations = {"classpath:conf/applicationContext.xml", "classpath:conf/applicationContext-mybatis.xml","classpath:conf/applicationContext-service.xml"})
public class RedisCacheQuestionServiceImplTest
{
	@Autowired
	QuestionService questionService;

	@Autowired
	UserService userService;

	@Test
	public void getQuestion()
	{
		long start = System.currentTimeMillis();
		Question question = questionService.getQuestionById(48L);
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
	@Test
	@Transactional(rollbackFor = Exception.class)
	@Rollback(false)
	public void atTest(){
		//System.out.println(CommonUtils.AtEscape("@靠脸吃饭 哈哈",userService,"<a href=\'/user/%d\'>@%s</a>",'@'));
		System.out.println(CommonUtils.AtEscape("@靠脸吃饭 @撒大事 @叉叉是我儿是的是的 @果冻战魂",userService));
	}

}