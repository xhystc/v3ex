package com.xhystc.zhihu.model.vo.query;

import java.util.Date;
import java.util.Set;

public class VoteInformQueryCondition
{
	private String id;
	private Date lastVoteTime;
	private Long lastVoteUser;
	private int voteCount;
	private Set<String> include;

	public String getId()
	{
		return id;
	}

	public void setId(String id)
	{
		this.id = id;
	}

	public Date getLastVoteTime()
	{
		return lastVoteTime;
	}

	public void setLastVoteTime(Date lastVoteTime)
	{
		this.lastVoteTime = lastVoteTime;
	}

	public Long getLastVoteUser()
	{
		return lastVoteUser;
	}

	public void setLastVoteUser(Long lastVoteUser)
	{
		this.lastVoteUser = lastVoteUser;
	}

	public int getVoteCount()
	{
		return voteCount;
	}

	public void setVoteCount(int voteCount)
	{
		this.voteCount = voteCount;
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




