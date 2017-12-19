package com.xhystc.v3ex.model.vo.page;

import com.xhystc.v3ex.model.Comment;
import com.xhystc.v3ex.model.Question;
import com.xhystc.v3ex.model.Tag;

import java.util.List;

public class CommentPage
{
	private List<Comment> comments;
	private Question question;
	private boolean isVoted;
	private Tag tag;

	public List<Comment> getComments()
	{
		return comments;
	}

	public void setComments(List<Comment> comments)
	{
		this.comments = comments;
	}

	public Question getQuestion()
	{
		return question;
	}

	public void setQuestion(Question question)
	{
		this.question = question;
	}

	public boolean getIsVoted()
	{
		return isVoted;
	}

	public void setIsVoted(boolean voted)
	{
		isVoted = voted;
	}

	public Tag getTag()
	{
		return tag;
	}

	public void setTag(Tag tag)
	{
		this.tag = tag;
	}
}





