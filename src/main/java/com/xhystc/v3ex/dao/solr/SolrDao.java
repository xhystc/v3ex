package com.xhystc.v3ex.dao.solr;

import com.xhystc.v3ex.model.Question;
import com.xhystc.v3ex.model.vo.SolrHighLightInform;
import com.xhystc.v3ex.model.vo.SolrSearchResult;

import java.util.List;

public interface SolrDao
{
	SolrSearchResult queryQuestion(String query, String tag, int start, int rows);
	boolean addQuestion(Question question,double score);
	boolean deleteQuestion(Long id);
	boolean updateQuestion(Long id,String field,String value);
}



