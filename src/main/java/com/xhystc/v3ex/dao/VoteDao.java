package com.xhystc.v3ex.dao;

import com.xhystc.v3ex.model.EntityType;
import com.xhystc.v3ex.model.Vote;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public interface VoteDao
{
	List<Vote> selectVotes(Map<String,Object> condition);
	int deleteVote(Vote vote);
	int insertVote(Vote vote);
	List<Vote> getUserVotes(Long userId);
	int updateVoteCount(String type,Long id,int count);
	int incVoteCount(EntityType type, Long id, int count);
	int getVoteCount(EntityType type,Long id);
	Vote getVote(Long userId,EntityType type,Long id);
}



