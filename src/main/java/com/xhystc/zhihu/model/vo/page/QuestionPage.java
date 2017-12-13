package com.xhystc.zhihu.model.vo.page;

import com.xhystc.zhihu.model.Question;
import com.xhystc.zhihu.model.Tag;

import java.util.List;

public class QuestionPage
{
	private List<Question> questions;
	private Long currentTag;
	private List<Tag> tags;

	public List<Question> getQuestions()
	{
		return questions;
	}

	public void setQuestions(List<Question> questions)
	{
		this.questions = questions;
	}

	public Long getCurrentTag()
	{
		return currentTag;
	}

	public void setCurrentTag(Long currentTag)
	{
		this.currentTag = currentTag;
	}

	public List<Tag> getTags()
	{
		return tags;
	}

	public void setTags(List<Tag> tags)
	{
		this.tags = tags;
	}
}
