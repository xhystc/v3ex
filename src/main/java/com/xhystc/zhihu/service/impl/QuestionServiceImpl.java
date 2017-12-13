package com.xhystc.zhihu.service.impl;

import com.xhystc.zhihu.commons.FormUtils;
import com.xhystc.zhihu.dao.QuestionDao;
import com.xhystc.zhihu.dao.QuestionTagDao;
import com.xhystc.zhihu.dao.TagDao;
import com.xhystc.zhihu.model.Comment;
import com.xhystc.zhihu.model.Question;
import com.xhystc.zhihu.model.User;
import com.xhystc.zhihu.model.vo.form.QuestionForm;
import com.xhystc.zhihu.model.vo.query.QuestionQueryCondition;
import com.xhystc.zhihu.service.CommentService;
import com.xhystc.zhihu.service.QuestionService;
import com.xhystc.zhihu.service.VoteService;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
			List<Long> tagIds = new ArrayList<>(form.getTags().length);
			for(Long tagId : form.getTags()){
				tagIds.add(tagId);
				questionTagDao.insertQuestionTag(question.getId(),tagId);
			}
			tagDao.increaseCount(tagIds);
		}

		return question.getId();
	}

	@Override
	public boolean upQuestion(Long questionId)
	{
		Question question = new Question();
		question.setId(questionId);
		question.setActiveTime(new Date());


		return questionDao.updateQuestion(question) >0 ;
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





