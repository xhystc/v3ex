package com.xhystc.zhihu.model;

import com.xhystc.zhihu.commons.DateUtils;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Question implements Serializable,Votable
{
	private Long id;
	private User user;
	private String title;
	private String content;
	private Date createDate;
	private int commentCount;
	private Date activeTime;
	private int agree;
	private boolean isVoted;

	public String getCreateDateShowString(){
		return DateUtils.dateFrom(this.getCreateDate());
	}

	public String getActiveTimeShowString(){
		DateFormat format = new SimpleDateFormat("yyyy年MM月dd日 HH时mm分ss秒");
		return format.format(this.getActiveTime());
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

	public Date getActiveTime()
	{
		return activeTime;
	}

	public void setActiveTime(Date activeTime)
	{
		this.activeTime = activeTime;
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
	public boolean equals(Object o)
	{
		if (this == o) return true;
		if (!(o instanceof Question)) return false;

		Question question = (Question) o;

		if (getCommentCount() != question.getCommentCount()) return false;
		if (agree != question.agree) return false;
		if (!getUser().equals(question.getUser())) return false;
		if (!getTitle().equals(question.getTitle())) return false;
		return getContent().equals(question.getContent());
	}

	@Override
	public int hashCode()
	{
		int result = getUser().hashCode();
		result = 31 * result + getTitle().hashCode();
		result = 31 * result + getContent().hashCode();
		result = 31 * result + getCommentCount();
		result = 31 * result + agree;
		return result;
	}

	@Override
	public String type()
	{
		return "question";
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














