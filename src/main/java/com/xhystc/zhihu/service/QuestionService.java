package com.xhystc.zhihu.service;

import com.xhystc.zhihu.model.Question;
import com.xhystc.zhihu.model.User;
import com.xhystc.zhihu.model.vo.form.QuestionForm;

import java.util.List;

public interface QuestionService
{
	Question getQuestion(Long id);
	List<Question> getQuestions(Long tagId,int page,int pageSize);
	Long publishQuetion(User user, QuestionForm form);
	boolean upQuestion(Long questionId);
}





