package com.xhystc.v3ex.dao;

import com.xhystc.v3ex.model.Vote;
import com.xhystc.v3ex.model.vo.query.VoteQueryCondition;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface VoteDao
{
	List<Vote> selectVotes(VoteQueryCondition condition);
	int deleteVote(Vote vote);
	int insertVote(Vote vote);
	List<Vote> getUserVotes(Long userId);
}



