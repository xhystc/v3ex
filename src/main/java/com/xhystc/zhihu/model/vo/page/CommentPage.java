package com.xhystc.zhihu.model.vo.page;

import com.xhystc.zhihu.model.Comment;
import com.xhystc.zhihu.model.Question;

import java.util.List;

public class CommentPage
{
	List<Comment> comments;
	Question question;
	int currentPage;
	int lastPage;
	List<Integer> pageButtons;
	boolean questionVote;
	boolean isVoted;

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

	public boolean getQuestionVote()
	{
		return questionVote;
	}

	public void setQuestionVote(boolean questionVote)
	{
		this.questionVote = questionVote;
	}

	public boolean getIsVoted()
	{
		return isVoted;
	}

	public void setIsVoted(boolean voted)
	{
		isVoted = voted;
	}
}





