package com.xhystc.v3ex.test.dao;

import com.xhystc.v3ex.dao.CommentDao;
import com.xhystc.v3ex.dao.QuestionDao;
import com.xhystc.v3ex.dao.UserDao;
import com.xhystc.v3ex.model.Comment;
import com.xhystc.v3ex.model.Question;
import com.xhystc.v3ex.model.User;
import com.xhystc.v3ex.model.vo.query.CommentQueryCondition;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)  //使用junit4进行测试
@ContextConfiguration(locations = {"classpath:conf/applicationContext.xml", "classpath:conf/applicationContext-mybatis.xml"})
public class CommentDaoTest
{
	@Autowired
	CommentDao commentDao;
	@Autowired
	UserDao userDao;
	@Autowired
	QuestionDao questionDao;

	@Transactional(rollbackFor = Exception.class)
	@Rollback(false)
	@Test
	public void getCommentById() throws Exception
	{
		Comment comment = commentDao.getCommentById(1L);
		System.out.println(comment.getUser().getName()+":"+comment.getContent());
	}

	@Transactional(rollbackFor = Exception.class)
	@Rollback(false)
	@Test
	public void deleteCommentById() throws Exception
	{

	}

	@Transactional(rollbackFor = Exception.class)
	@Rollback(false)
	@Test
	public void updateComment() throws Exception
	{
		Comment comment = commentDao.getCommentById(1L);
		comment.setContent("撒独立空间撒旦");
		commentDao.updateComment(comment);
	}

	@Transactional(rollbackFor = Exception.class)
	@Rollback(false)
	@Test
	public void insertComment() throws Exception
	{
		Comment comment = new Comment();
		User user =userDao.getUserById(4L);
		for(int i=0;i<100000;i++){
			comment.setContent("十二月份枪毙名单排满了");
			comment.setUser(user);
			Question question = new Question();
			question.setId(36L);
			comment.setQuestion(question);
			commentDao.insertComment(comment);
		}
	}



	@Transactional(rollbackFor = Exception.class)
	@Rollback(false)
	@Test
	public void selectComment() throws Exception
	{
		CommentQueryCondition condition = new CommentQueryCondition();
		condition.setOffset(0);
		condition.setRows(100);
		condition.setParentId(51L);

		List<Comment> comments = commentDao.selectComments(condition);
		for(Comment c : comments){
			System.out.println(c.getQuestion().getTag().getName());
		}

	}

}





