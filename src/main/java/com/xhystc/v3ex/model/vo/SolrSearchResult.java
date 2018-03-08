package com.xhystc.v3ex.model.vo;

import com.xhystc.v3ex.model.Question;

import java.util.List;

public class SolrSearchResult
{
	private long total;
	private List<SolrHighLightInform> highLightInforms;
	private List<Question> questions ;

	public long getTotal()
	{
		return total;
	}

	public void setTotal(long total)
	{
		this.total = total;
	}

	public List<SolrHighLightInform> getHighLightInforms()
	{
		return highLightInforms;
	}

	public void setHighLightInforms(List<SolrHighLightInform> highLightInforms)
	{
		this.highLightInforms = highLightInforms;
	}

	public List<Question> getQuestions()
	{
		return questions;
	}

	public void setQuestions(List<Question> questions)
	{
		this.questions = questions;
	}
}



