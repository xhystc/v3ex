package com.xhystc.v3ex.model;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

public class Question implements Serializable,Votable,Commentable
{
	private Long id;
	private User user;
	private Tag tag;
	private String title;
	private String content;
	private Date createDate;
	private int commentCount;
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

	public String getTitle()
	{
		return title;
	}

	public void setTitle(String title)
	{
		this.title = title;
	}

	public String getContent()
	{
		return content;
	}

	public void setContent(String content)
	{
		this.content = content;
	}

	public Date getCreateDate()
	{
		return createDate;
	}

	public void setCreateDate(Date createDate)
	{
		this.createDate = createDate;
	}

	public int getCommentCount()
	{
		return commentCount;
	}

	public void setCommentCount(int commentCount)
	{
		this.commentCount = commentCount;
	}


	public Tag getTag()
	{
		return tag;
	}

	public void setTag(Tag tag)
	{
		this.tag = tag;
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
	public void setIsVoted(boolean is)
	{
		isVoted = is;
	}

	@Override
	public EntityType type()
	{
		return EntityType.QUESTION;
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

	@Override
	public boolean equals(Object o)
	{
		if (this == o)
		{
			return true;
		}
		if (!(o instanceof Question))
		{
			return false;
		}
		Question question = (Question) o;
		if(this.getId()!=null && ((Question) o).getId()!=null){
			return this.getId().equals(((Question) o).getId());
		}
		return Objects.equals(getUser(), question.getUser()) &&
				Objects.equals(getTag(), question.getTag()) &&
				Objects.equals(getTitle(), question.getTitle()) &&
				Objects.equals(getContent(),((Question) o).getContent()) &&
				Objects.equals(getCreateDate(), question.getCreateDate());
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(getUser(), getTag(), getTitle(), getCreateDate());
	}
}














