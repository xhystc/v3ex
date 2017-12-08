package com.xhystc.zhihu.dao;

import com.xhystc.zhihu.model.Question;
import com.xhystc.zhihu.model.vo.query.QuestionQueryCondition;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface QuestionDao
{
	Question getQuestionById(Long id);
	int insertQuestion(Question question);
	int deleteQuestionById(Long id);
	int updateQuestion(Question question);
	int increaseComment(Long id);
	int increaseAgree(Long id,int i);
	int setAgree(Long id,int i);
	List<Question> selectQuestions(QuestionQueryCondition condition);
	int total(QuestionQueryCondition condition);
}
