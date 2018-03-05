package com.xhystc.v3ex.test.dao;

import com.xhystc.v3ex.dao.CommentDao;
import com.xhystc.v3ex.dao.CommentInformDao;
import com.xhystc.v3ex.dao.QuestionDao;
import com.xhystc.v3ex.dao.UserDao;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;


@RunWith(SpringJUnit4ClassRunner.class)  //使用junit4进行测试
@ContextConfiguration(locations = {"classpath:conf/applicationContext.xml", "classpath:conf/applicationContext-mybatis.xml"})
public class CommentInformDaoTest
{

	@Autowired
	CommentDao commentDao;
	@Autowired
	UserDao userDao;
	@Autowired
	QuestionDao questionDao;
	@Autowired
	CommentInformDao commentInformDao;


	@Transactional(rollbackFor = Exception.class)
	@Rollback(false)
	@Test
	public void updateCommentInform()
	{
	}

	@Transactional(rollbackFor = Exception.class)
	@Rollback(false)
	@Test
	public void selectCommentInform()
	{
		/*Map<String,Object> condition = new HashMap<>(20);
		condition.put("id","question_26");
		CommentInform commentInform = commentInformDao.selectCommentInform(condition);
		System.out.println(commentInform.getCommentCount());*/
	}

	@Transactional(rollbackFor = Exception.class)
	@Rollback(false)
	@Test
	public void increaseCommentInform()
	{
	//	commentInformDao.increaseCommentInform("question_11",34L,new Date(),1);
	}
}










