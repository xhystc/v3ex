package com.xhystc.v3ex.service.impl;

import com.xhystc.v3ex.commons.FormUtils;
import com.xhystc.v3ex.dao.QuestionDao;
import com.xhystc.v3ex.dao.QuestionTagDao;
import com.xhystc.v3ex.dao.TagDao;
import com.xhystc.v3ex.model.Comment;
import com.xhystc.v3ex.model.Question;
import com.xhystc.v3ex.model.User;
import com.xhystc.v3ex.model.vo.form.QuestionForm;
import com.xhystc.v3ex.model.vo.query.QuestionQueryCondition;
import com.xhystc.v3ex.service.CommentService;
import com.xhystc.v3ex.service.QuestionService;
import com.xhystc.v3ex.service.VoteService;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Transactional(rollbackFor = RuntimeException.class)
@Component
public class QuestionServiceImpl implements QuestionService
{
	private static final Logger logger = Logger.getLogger(QuestionServiceImpl.class);

	private TagDao tagDao;
	private QuestionDao questionDao;
	private QuestionTagDao questionTagDao;


	public QuestionServiceImpl(TagDao tagDao, QuestionDao questionDao, QuestionTagDao questionTagDao)
	{
		this.tagDao = tagDao;
		this.questionDao = questionDao;
		this.questionTagDao = questionTagDao;
	}

	@Override
	public Question getQuestion(Long id)
	{
		return questionDao.getQuestionById(id);
	}

	@Override
	public List<Question> getQuestions(Long tagId, int page, int pageSize)
	{
		QuestionQueryCondition condition = new QuestionQueryCondition();
		condition.setRows(pageSize);
		condition.setOffset((page-1)*pageSize);
		condition.setTagId(tagId);

		List<Question> questions = questionDao.selectQuestions(condition);

		User user = FormUtils.getCurrentUser();
		Long userId = user==null?-1:user.getId();

		return questions;
	}


	@Override
	public Long publishQuetion(User user, QuestionForm form)
	{
		Question question = new Question();
		question.setUser(user);
		question.setTitle(form.getTitle());
		question.setContent(form.getContent());
		question.setCreateDate(new Date());

		if (questionDao.insertQuestion(question)<0){
			throw new RuntimeException("insert question error");
		}

		if(form.getTags()!=null){
			List<Long> tagIds = Arrays.asList(form.getTags());
			for(Long tagId : form.getTags()){
				questionTagDao.insertQuestionTag(question.getId(),tagId);
			}
			tagDao.increaseCount(tagIds);
		}

		return question.getId();
	}

	@Override
	public void upQuestion(Long questionId)
	{
		Question question = new Question();
		question.setId(questionId);
		question.setActiveTime(new Date());
		questionDao.updateQuestion(question);

	}

	@Override
	public int total(Long tag)
	{
		return questionDao.total(tag);
	}

	public QuestionDao getQuestionDao()
	{
		return questionDao;
	}

	public void setQuestionDao(QuestionDao questionDao)
	{
		this.questionDao = questionDao;
	}


}





