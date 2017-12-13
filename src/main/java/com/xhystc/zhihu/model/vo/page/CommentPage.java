package com.xhystc.zhihu.model.vo.page;

import com.xhystc.zhihu.model.Comment;
import com.xhystc.zhihu.model.Question;
import com.xhystc.zhihu.model.Tag;

import java.util.List;

public class CommentPage
{
	private List<Comment> comments;
	private Question question;
	private int currentPage;
	private int lastPage;
	private List<Integer> pageButtons;
	private boolean isVoted;
	private List<Tag> tags;

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

	public int getCurrentPage()
	{
		return currentPage;
	}

	public void setCurrentPage(int currentPage)
	{
		this.currentPage = currentPage;
	}

	public int getLastPage()
	{
		return lastPage;
	}

	public void setLastPage(int lastPage)
	{
		this.lastPage = lastPage;
	}

	public List<Integer> getPageButtons()
	{
		return pageButtons;
	}

	public void setPageButtons(List<Integer> pageButtons)
	{
		this.pageButtons = pageButtons;
	}


	public boolean getIsVoted()
	{
		return isVoted;
	}

	public void setIsVoted(boolean voted)
	{
		isVoted = voted;
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





