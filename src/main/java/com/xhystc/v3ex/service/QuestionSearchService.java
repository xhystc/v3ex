package com.xhystc.v3ex.service;

import com.xhystc.v3ex.model.Question;
import com.xhystc.v3ex.model.vo.SolrSearchResult;

import java.util.List;
import java.util.Map;

public interface QuestionSearchService
{
	SolrSearchResult searchQuestions(String queryString, String tag, int start, int rows);
	boolean addQuestion(Long questionId,double score);
	boolean deleteQuestion(Long questionId);
	boolean updateQuestion(Long questionId,String field,String value);
}



