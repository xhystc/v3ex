package com.xhystc.v3ex.model;

import java.util.Date;

public class VoteInform
{
	private String id;
	private Date lastVoteTime;
	private Long lastVoteUser;
	private int voteCount;

	public static String voteInformId(String type,Long id){
		return type+"_"+id;
	}
	public static String voteInformId(Votable votable){
		return votable.type()+"_"+votable.id();
	}

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

}




