package com.xhystc.v3ex.test.dao;

import com.xhystc.v3ex.dao.QuestionDao;
import com.xhystc.v3ex.dao.TagDao;
import com.xhystc.v3ex.dao.UserDao;
import com.xhystc.v3ex.model.EntityType;
import com.xhystc.v3ex.model.Question;
import com.xhystc.v3ex.model.Tag;
import com.xhystc.v3ex.model.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

@RunWith(SpringJUnit4ClassRunner.class)  //使用junit4进行测试
@ContextConfiguration(locations = {"classpath:conf/applicationContext.xml", "classpath:conf/applicationContext-mybatis.xml"})
public class QuestionDaoTest
{
	@Autowired
	QuestionDao questionDao;
	@Autowired
	UserDao userDao;

	@Autowired
	TagDao tagDao;


	@Test
	@Transactional(rollbackFor = Exception.class)
	@Rollback(false)
	public void getQuestionById() throws Exception
	{
		System.out.println(EntityType.valueOf("question"));
		Question question = questionDao.getQuestionById(47L);
		System.out.println(question.getCommentCount());
	}

	@Test
	@Transactional(rollbackFor = Exception.class)
	@Rollback(false)
	public void insertQuestion() throws Exception
	{
		Question question =new Question();
		User user = userDao.getUserById((long) 4);
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = format.parse("2017-10-30 16:31:51");
		Tag tag = tagDao.getTagById(3L);
		for(int i = 0;i<300000;i++){
			date.setTime(date.getTime()+1000);
			question.setContent("作者：徐子陵\n");
			question.setTitle("你认识的有钱人是怎样生活的？");
			question.setUser(user);
			question.setCreateDate(date);
			question.setTag(tag);
			questionDao.insertQuestion(question);
		}
	}

	@Test
	@Transactional(rollbackFor = Exception.class)
	@Rollback(false)
	public void deleteQuestionById() throws Exception
	{
		questionDao.deleteQuestionById((long) 5);
	}

	@Test
	@Transactional(rollbackFor = Exception.class)
	@Rollback(false)
	public void updateQuestion() throws Exception
	{
		Question question = questionDao.getQuestionById((long) 1);

		questionDao.updateQuestion(question);
	}


	@Test
	@Transactional(rollbackFor = Exception.class)
	@Rollback(false)
	public void selectQuestion(){
			Map<String,Object> condition = new HashMap<>(5);
			condition.put("rows",20);
			condition.put("offset",0);

			List<Question> questions = questionDao.selectQuestions(condition);

			for(Question question : questions){
				System.out.println(question.getTitle());
			}
	}


	@Test
	@Transactional(rollbackFor = Exception.class)
	@Rollback(false)
	public void total(){
		System.out.println(questionDao.total(null));
	}
}