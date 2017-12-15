package com.xhystc.v3ex.service;

import com.xhystc.v3ex.model.Votable;
import com.xhystc.v3ex.model.Vote;
import com.xhystc.v3ex.model.VoteInform;

import java.util.List;

public interface VoteService
{
	boolean doVote(Long userId, String type, Long id);
	boolean disVote(Long userId, String type, Long id);
	void fetchUserVote(Long userId,Votable votable);
	void fetchUserVotes(Long userId,List<? extends Votable> votables);
	VoteInform voteInform(String type, Long id);
	boolean isVote(Long userId,String type,Long id);
}


