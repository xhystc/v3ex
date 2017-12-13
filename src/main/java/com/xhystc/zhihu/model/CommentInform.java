package com.xhystc.zhihu.model;

import java.util.Date;

public class CommentInform
{
	private String id;
	private User lastCommentUser;
	private Date lastCommentTime;
	private int commentCount;

	public String getId()
	{
		return id;
	}

	public void setId(String id)
	{
		this.id = id;
	}

	public User getLastCommentUser()
	{
		return lastCommentUser;
	}

	public void setLastCommentUser(User lastCommentUser)
	{
		this.lastCommentUser = lastCommentUser;
	}

	public Date getLastCommentTime()
	{
		return lastCommentTime;
	}

	public void setLastCommentTime(Date lastCommentTime)
	{
		this.lastCommentTime = lastCommentTime;
	}

	public int getCommentCount()
	{
		return commentCount;
	}

	public void setCommentCount(int commentCount)
	{
		this.commentCount = commentCount;
	}
}


