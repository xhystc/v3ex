package com.xhystc.v3ex.model.vo.query;

import java.util.Date;
import java.util.Set;

public class CommentInformQueryCondition extends QueryCondition
{
	private String id;
	private Date lastCommentTime;
	private Long lastCommenteUser;
	private int commentCount;
	private Set<String> include;

	public String getId()
	{
		return id;
	}

	public void setId(String id)
	{
		this.id = id;
	}

	public Date getLastCommentTime()
	{
		return lastCommentTime;
	}

	public void setLastCommentTime(Date lastCommentTime)
	{
		this.lastCommentTime = lastCommentTime;
	}

	public Long getLastCommenteUser()
	{
		return lastCommenteUser;
	}

	public void setLastCommenteUser(Long lastCommenteUser)
	{
		this.lastCommenteUser = lastCommenteUser;
	}

	public int getCommentCount()
	{
		return commentCount;
	}

	public void setCommentCount(int commentCount)
	{
		this.commentCount = commentCount;
	}

	public Set<String> getInclude()
	{
		return include;
	}

	public void setInclude(Set<String> include)
	{
		this.include = include;
	}
}
