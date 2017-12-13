package com.xhystc.zhihu.dao;

import com.xhystc.zhihu.model.VoteInform;
import com.xhystc.zhihu.model.vo.query.VoteInformQueryCondition;

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
