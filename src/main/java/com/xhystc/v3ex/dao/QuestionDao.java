package com.xhystc.v3ex.dao;

import com.xhystc.v3ex.model.Question;
import com.xhystc.v3ex.model.vo.query.QuestionQueryCondition;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Component
public interface QuestionDao
{
	Question getQuestionById(Long id);
	int insertQuestion(Question question);
	int deleteQuestionById(Long id);
	int updateQuestion(Question question);
	List<Question> selectQuestions(QuestionQueryCondition condition);
	int total(Long tag);
}
