package com.xhystc.v3ex.dao;

import com.xhystc.v3ex.model.EntityType;
import com.xhystc.v3ex.model.Vote;
import com.xhystc.v3ex.model.VoteInform;
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
	int updateVoteCount(String type,Long id,int count);
	int incVoteCount(EntityType type, Long id, int count);
	int getVoteCount(EntityType type,Long id);
	Vote getVote(Long userId,EntityType type,Long id);
}



