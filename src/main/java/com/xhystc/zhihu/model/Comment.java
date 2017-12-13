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
	private VoteInform voteInform;
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

	public VoteInform getVoteInform()
	{
		return voteInform;
	}

	public void setVoteInform(VoteInform voteInform)
	{
		this.voteInform = voteInform;
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


}
