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
	private VoteInform voteInform;
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


	@Override
	public VoteInform getVoteInform()
	{
		return voteInform;
	}

	@Override
	public void setVoteInform(VoteInform voteInform)
	{
		this.voteInform = voteInform;
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
	public String type()
	{
		return "question";
	}

	@Override
	public Long id()
	{
		return id;
	}

}














