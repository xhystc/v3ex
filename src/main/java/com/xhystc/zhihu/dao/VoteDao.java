package com.xhystc.zhihu.dao;

import com.xhystc.zhihu.model.Vote;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface VoteDao
{
	List<Vote> selectVotes(Vote vote);
	int deleteVote(Vote vote);
	int insertVote(Vote vote);
	List<Vote> getUserVotes(Long userId);
	List<Long> getUsers(Long questionId);
}



