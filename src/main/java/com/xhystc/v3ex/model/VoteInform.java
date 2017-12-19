package com.xhystc.v3ex.model;

import java.util.Date;

public class VoteInform
{
	private String type;
	private Long vid;
	private Date lastVoteTime;
	private Long lastVoteUser;
	private int voteCount;

	public static String voteInformId(String type,Long id){
		return type+"_"+id;
	}
	public static String voteInformId(Votable votable){
		return votable.type()+"_"+votable.id();
	}

	public String getType()
	{
		return type;
	}

	public void setType(String type)
	{
		this.type = type;
	}

	public Long getVid()
	{
		return vid;
	}

	public void setVid(Long vid)
	{
		this.vid = vid;
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




