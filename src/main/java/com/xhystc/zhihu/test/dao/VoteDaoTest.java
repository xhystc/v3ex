package com.xhystc.zhihu.test.dao;

import com.xhystc.zhihu.dao.VoteDao;
import com.xhystc.zhihu.model.Vote;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)  //使用junit4进行测试
@ContextConfiguration(locations = {"classpath:applicationContext.xml", "classpath:applicationContext-mybatis.xml"})

public class VoteDaoTest
{
	@Autowired
	VoteDao voteDao;

	@Transactional(rollbackFor = Exception.class)
	@Rollback(false)
	@Test
	public void selectVote() throws Exception
	{
		Vote vote = new Vote();
		vote.setParentId(1L);
		vote.setUserId(4L);
		vote.setParentType("question");

		List<Vote> v = voteDao.selectVotes(vote);
		System.out.println(v.get(0).getUserId());
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


	@Transactional(rollbackFor = Exception.class)
	@Rollback(false)
	@Test
	public void getQuestionUsers(){
		List<Long> users = voteDao.getUsers(1L);
		for(Long vote : users){
			System.out.println(vote);
		}
	}

}















