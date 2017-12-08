package com.xhystc.zhihu.service.impl;

import com.xhystc.zhihu.commons.FormUtils;
import com.xhystc.zhihu.dao.CommentDao;
import com.xhystc.zhihu.dao.QuestionDao;
import com.xhystc.zhihu.model.Comment;
import com.xhystc.zhihu.model.Question;
import com.xhystc.zhihu.model.User;
import com.xhystc.zhihu.model.vo.form.QuestionCommentForm;
import com.xhystc.zhihu.model.vo.form.QuestionForm;
import com.xhystc.zhihu.model.vo.json.Problem;
import com.xhystc.zhihu.model.vo.page.CommentPage;
import com.xhystc.zhihu.model.vo.query.CommentQueryCondition;
import com.xhystc.zhihu.model.vo.query.QuestionQueryCondition;
import com.xhystc.zhihu.service.QuestionCommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Component
@Transactional(rollbackFor = RuntimeException.class)
public class QuestionCommentServiceImpl implements QuestionCommentService
{

	private QuestionDao questionDao;
	private CommentDao commentDao;

	@Autowired
	public QuestionCommentServiceImpl(QuestionDao questionDao, CommentDao commentDao)
	{
		this.questionDao = questionDao;
		this.commentDao = commentDao;
	}

	@Override
	public List<Question> getQuestionItems(int page,int pageSize)
	{
		QuestionQueryCondition condition = new QuestionQueryCondition();
		condition.setOffset((page-1)*pageSize);
		condition.setRows(pageSize);
		List<Question> questions = questionDao.selectQuestions(condition);
		return questions;
	}

	@Override
	public Set<Problem> publishQuestion(User user, QuestionForm form)
	{
		Set<Problem> ret = new HashSet<>(2);
		Question question = new Question();
		question.setUser(user);
		question.setTitle(form.getTitle());
		question.setContent(form.getContent());
		question.setCreateDate(new Date());

		questionDao.insertQuestion(question);
		return ret;
	}

	@Override
	public List<Comment> getCommentItems(CommentQueryCondition condition)
	{
		List<Comment> comments = commentDao.selectComments(condition);
		return comments;
	}

	@Override
	public CommentPage commentPage(Long questionId,int page,int pageSize)
	{
		CommentQueryCondition condition = new CommentQueryCondition();
		condition.setOffset((page-1)*pageSize);
		condition.setRows(pageSize);
		condition.setParentType("question");
		condition.setParentId(questionId);

		Question questionBean = questionDao.getQuestionById(questionId);
		List<Comment> comments = commentDao.selectComments(condition);
		CommentPage commentPage = new CommentPage();
		commentPage.setComments(comments);
		commentPage.setLastPage(questionBean.getCommentCount()/pageSize+(questionBean.getCommentCount()%pageSize>0?1:0));
		commentPage.setPageButtons(FormUtils.pageButtons(page,commentPage.getLastPage()));
		commentPage.setQuestion(questionBean);
		return commentPage;
	}

	@Override
	public int totalQuestions(QuestionQueryCondition condition)
	{
		return questionDao.total(condition);
	}

	@Override
	public Question getQuestionById(Long id)
	{
		Question question = questionDao.getQuestionById(id);
		return question;
	}

	@Override
	public Set<Problem> publishQuestionComment(User user, QuestionCommentForm form)
	{
		Set<Problem> problems = new HashSet<>(3);

		Comment comment = new Comment();
		comment.setParentType("question");
		comment.setParentId(form.getQuestionId());
		comment.setUser(user);
		comment.setContent(form.getContent());
		comment.setSendDate(new Date());

		commentDao.insertComment(comment);
		questionDao.increaseComment(form.getQuestionId());

		return problems;
	}


}










