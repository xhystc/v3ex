package com.xhystc.v3ex.test.dao;

import com.xhystc.v3ex.dao.CommentDao;
import com.xhystc.v3ex.dao.CommentInformDao;
import com.xhystc.v3ex.dao.QuestionDao;
import com.xhystc.v3ex.dao.UserDao;
import com.xhystc.v3ex.model.CommentInform;
import com.xhystc.v3ex.model.Question;
import com.xhystc.v3ex.model.vo.query.QuestionQueryCondition;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;

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
	public void insertCommentInform()
	{
		QuestionQueryCondition condition = new QuestionQueryCondition();
		condition.setOffset(0);
		condition.setRows(100000);
		List<Question>  questions = questionDao.selectQuestions(condition);
		for(Question question : questions){
			CommentInform commentInform = new CommentInform();
			commentInform.setId("question_"+question.getId());
			commentInform.setLastCommentUser(userDao.getUserById(34L));
			commentInform.setLastCommentTime(question.getActiveTime());
			commentInform.setCommentCount(question.getCommentCount());

			commentInformDao.insertCommentInform(commentInform);

		}
	}

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
		/*CommentInform commentInform = commentInformDao.selectCommentInform("question_26");
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










