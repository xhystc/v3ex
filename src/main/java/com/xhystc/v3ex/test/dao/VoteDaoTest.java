package com.xhystc.v3ex.test.dao;

import com.xhystc.v3ex.dao.VoteDao;
import com.xhystc.v3ex.model.Vote;
import com.xhystc.v3ex.model.vo.query.VoteQueryCondition;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)  //使用junit4进行测试
@ContextConfiguration(locations = {"classpath:conf/applicationContext.xml", "classpath:conf/applicationContext-mybatis.xml"})

public class VoteDaoTest
{
	@Autowired
	VoteDao voteDao;

	@Transactional(rollbackFor = Exception.class)
	@Rollback(false)
	@Test
	public void selectVote() throws Exception
	{
		VoteQueryCondition condition = new VoteQueryCondition();
		condition.setInclude(new HashSet<>());
		/*condition.getInclude().add(37L);
		condition.getInclude().add(39L);
		condition.getInclude().add(38L);
		condition.getInclude().add(50L);
		condition.setUserId(38L);*/
		condition.setParentType("question");

		List<Vote> vs = voteDao.selectVotes(condition);
		for(Vote v : vs){
			System.out.println(v.getParentType()+":"+v.getParentId());
		}
	}

	@Transactional(rollbackFor = Exception.class)
	@Rollback(false)
	@Test
	public void deleteVote() throws Exception
	{
		Vote vote = new Vote();
		vote.setParentId(1L);
		vote.setUserId(4L);
		vote.setParentType("question");

		voteDao.deleteVote(vote);
	}

	@Transactional(rollbackFor = Exception.class)
	@Rollback(false)
	@Test
	public void insertVote() throws Exception
	{
		Vote vote = new Vote();
		vote.setParentId(1L);
		vote.setUserId(4L);
		vote.setParentType("question");

		voteDao.insertVote(vote);
	}

	@Transactional(rollbackFor = Exception.class)
	@Rollback(false)
	@Test
	public void getUserVotes(){
		List<Vote> votes = voteDao.getUserVotes(4L);
		for(Vote vote : votes){
			System.out.println(vote.getParentType()+":"+vote.getUserId());
		}
	}


	/*@Transactional(rollbackFor = Exception.class)
	@Rollback(false)
	@Test
	public void getQuestionUsers(){
		List<Long> users = voteDao.getUsers(1L);
		for(Long vote : users){
			System.out.println(vote);
		}
	}*/

}















