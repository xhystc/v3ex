package com.xhystc.v3ex.service;

import com.xhystc.v3ex.model.Question;
import com.xhystc.v3ex.model.User;
import com.xhystc.v3ex.model.vo.form.QuestionForm;

import java.util.List;

public interface QuestionService
{
	Question getQuestion(Long id);
	List<Question> getQuestions(Long tagId,int page,int pageSize);
	Long publishQuetion(User user, QuestionForm form);
	void upQuestion(Question question);
	int total(Long tag);
	void storeQuestions();
}





