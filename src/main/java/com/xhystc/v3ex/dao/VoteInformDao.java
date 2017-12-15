package com.xhystc.v3ex.dao;

import com.xhystc.v3ex.model.VoteInform;
import com.xhystc.v3ex.model.vo.query.VoteInformQueryCondition;

import java.util.Date;
import java.util.List;

public interface VoteInformDao
{
	int insertVoteInform(VoteInform voteInform);
	int updateVoteInform(VoteInform voteInform);
	List<VoteInform> selectVoteInform(VoteInformQueryCondition condition);
	int increaseVoteInform(VoteInform voteInform);
	VoteInform getVoteInformById(String id);
}
