package com.xhystc.zhihu.service;

import com.xhystc.zhihu.model.Votable;
import com.xhystc.zhihu.model.Vote;

import java.util.List;

public interface VoteService
{
	boolean doVote(Vote vote);
	boolean disVote(Vote vote);
	void fetchUserVotes(Long userId,Votable votable);
	void fetchUserVotes(Long userId,List<? extends Votable> votables,String type);
	int getVotes(String type,Long id);
	boolean isVoted(Long userId,String type,Long id);
}


