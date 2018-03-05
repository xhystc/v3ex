package com.xhystc.v3ex.dao;

import com.xhystc.v3ex.model.Question;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.Map;

@Component
public interface QuestionDao
{
	Question getQuestionById(Long id);
	int insertQuestion(Question question);
	int deleteQuestionById(Long id);
	int updateQuestion(Question question);
	List<Question> selectQuestions(Map<String,Object> condition);
	int total(Long tag);
}
