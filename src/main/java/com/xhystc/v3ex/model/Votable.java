package com.xhystc.v3ex.model;

public interface Votable
{
	EntityType type();
	Long id();
	int getVoteCount();
	void setVoteCount(int vote);
	boolean getIsVoted();
	void setIsVoted(boolean is);
}
