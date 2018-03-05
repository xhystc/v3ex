package com.xhystc.v3ex.service;

import com.xhystc.v3ex.model.Question;

import java.util.List;

public interface QuestionSearchService
{
	List<Question> searchQuestions(String queryString,String tag,int start,int rows);
	boolean addQuestion(Long questionId,double score);
	boolean deleteQuestion(Long questionId);
	boolean updateQuestion(Long questionId,String field,String value);
}



