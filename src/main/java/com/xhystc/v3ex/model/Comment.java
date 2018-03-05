package com.xhystc.v3ex.model;

import com.alibaba.fastjson.annotation.JSONField;

import java.io.Serializable;
import java.util.Date;

public class Comment implements Serializable,Votable,Commentable
{
	private Long id;
	private User user;
	private Long parentId;
	private EntityType parentType;
	private String content;
	private int commentCount;
	private Date sendDate;
	private int voteCount;
	private CommentInform commentInform;
	private boolean isVoted;


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

	public Long getParentId()
	{
		return parentId;
	}

	public void setParentId(Long parentId)
	{
		this.parentId = parentId;
	}

	public EntityType getParentType()
	{
		return parentType;
	}

	public void setParentType(EntityType parentType)
	{
		this.parentType = parentType;
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

	@Override
	public int getVoteCount()
	{
		return voteCount;
	}

	@Override
	public void setVoteCount(int voteCount)
	{
		this.voteCount = voteCount;
	}

	@Override
	public boolean getIsVoted()
	{
		return isVoted;
	}

	@Override
	public void setIsVoted(boolean voted)
	{
		isVoted = voted;
	}

	@Override
	public EntityType type()
	{
		return EntityType.COMMENT;
	}

	@Override
	public Long id()
	{
		return id;
	}

	@Override
	public CommentInform getCommentInform()
	{
		return commentInform;
	}

	@Override
	public void setCommentInform(CommentInform commentInform)
	{
		this.commentInform = commentInform;
	}


}
