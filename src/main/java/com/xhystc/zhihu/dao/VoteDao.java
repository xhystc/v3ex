package com.xhystc.zhihu.dao;

import com.xhystc.zhihu.model.Vote;
import com.xhystc.zhihu.model.vo.query.VoteQueryCondition;
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



