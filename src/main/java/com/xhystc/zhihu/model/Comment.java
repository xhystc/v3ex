package com.xhystc.zhihu.model;

import com.xhystc.zhihu.commons.DateUtils;
import com.xhystc.zhihu.commons.Markdown;

import java.io.Serializable;
import java.util.Date;

public class Comment implements Serializable,Votable
{
	private Long id;
	private User user;
	private String parentType;
	private Long parentId;
	private String content;
	private int commentCount;
	private Date sendDate;
	private int agree;
	private boolean isVoted;


	public String getSendDateShowString(){
		return DateUtils.dateFrom(this.getSendDate());
	}


	public Long getId()
	{
		return id;
	}

	public void setId(Long id)
	{
		this.id = id;
	}

	public User getUser()
	{
		return user;
	}

	public void setUser(User user)
	{
		this.user = user;
	}

	public String getParentType()
	{
		return parentType;
	}

	public void setParentType(String parentType)
	{
		this.parentType = parentType;
	}

	public Long getParentId()
	{
		return parentId;
	}

	public void setParentId(Long parentId)
	{
		this.parentId = parentId;
	}

	public String getContent()
	{
		return content;
	}

	public void setContent(String content)
	{
		this.content = content;
	}

	public int getCommentCount()
	{
		return commentCount;
	}

	public void setCommentCount(int commentCount)
	{
		this.commentCount = commentCount;
	}

	public Date getSendDate()
	{
		return sendDate;
	}

	public void setSendDate(Date sendDate)
	{
		this.sendDate = sendDate;
	}

	public int getAgree()
	{
		return agree;
	}

	public void setAgree(int agree)
	{
		this.agree = agree;
	}

	public boolean getIsVoted()
	{
		return isVoted;
	}

	public void setIsVoted(boolean voted)
	{
		isVoted = voted;
	}

	@Override
	public String type()
	{
		return "comment";
	}

	@Override
	public Long id()
	{
		return id;
	}

	@Override
	public int votes()
	{
		return agree;
	}

	@Override
	public void votes(int vote)
	{
		agree = vote;
	}

	@Override
	public boolean isVoted()
	{
		return isVoted;
	}

	@Override
	public void isVoted(boolean is)
	{
		isVoted = is;
	}

}
