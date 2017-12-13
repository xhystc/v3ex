package com.xhystc.zhihu.test.dao;

import com.xhystc.zhihu.dao.*;
import com.xhystc.zhihu.model.*;
import com.xhystc.zhihu.model.vo.query.CommentQueryCondition;
import com.xhystc.zhihu.model.vo.query.QuestionQueryCondition;
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
public class VoteInformDaoTest
{

	@Autowired
	VoteInformDao voteInformDao;
	@Autowired
	UserDao userDao;
	@Autowired
	QuestionDao questionDao;
	@Autowired
	CommentDao commentDao;
	@Autowired
	VoteDao voteDao;

	@Transactional(rollbackFor = Exception.class)
	@Rollback(false)
	@Test
	public void insertVoteInform()
	{
		User user = userDao.getUserByName("叉叉是我儿");
		/*QuestionQueryCondition questionQueryCondition = new QuestionQueryCondition();
		questionQueryCondition.setOffset(0);
		questionQueryCondition.setRows(100000);

		List<Question> questions = questionDao.selectQuestions(questionQueryCondition);
		for(Question question : questions){
			VoteInform voteInform = new VoteInform();
			voteInform.setId("question_"+question.getId());
			voteInform.setLastVoteTime(new Date());
			voteInform.setVoteCount(question.getAgree());
			voteInform.setLastVoteUser(user);

			voteInformDao.insertVoteInform(voteInform);
		}*/

		CommentQueryCondition commentQueryCondition = new CommentQueryCondition();
		commentQueryCondition.setOffset(20);
		commentQueryCondition.setRows(100000);
		List<Comment> comments = commentDao.selectComments(commentQueryCondition);
		for(Comment comment : comments){
			VoteInform voteInform = new VoteInform();
			voteInform.setId("comment_"+comment.getId());
			voteInform.setLastVoteTime(new Date());
			/*voteInform.setVoteCount(c);
			voteInform.setLastVoteUser(user);*/

			voteInformDao.insertVoteInform(voteInform);
		}
	}

	@Transactional(rollbackFor = Exception.class)
	@Rollback(false)
	@Test
	public void updateVoteInform()
	{
		VoteInform voteInform = new VoteInform();
		voteInform.setId("comment_1");
		voteInform.setLastVoteUser(userDao.getUserByName("果冻战魂").getId());

		voteInformDao.updateVoteInform(voteInform);
	}

	@Transactional(rollbackFor = Exception.class)
	@Rollback(false)
	@Test
	public void selectVoteInform()
	{

	}

	@Transactional(rollbackFor = Exception.class)
	@Rollback(false)
	@Test
	public void increaseVoteInform()
	{
		//voteInformDao.increaseVoteInform("comment_1",34L,new Date(),1);
	}
}









