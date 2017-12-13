package com.xhystc.zhihu.model;

public interface Votable
{
	String type();
	Long id();
	VoteInform getVoteInform();
	void setVoteInform(VoteInform voteInform);
	boolean getIsVoted();
	void setIsVoted(boolean is);
}
