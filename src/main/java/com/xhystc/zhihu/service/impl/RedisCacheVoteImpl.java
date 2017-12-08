package com.xhystc.zhihu.service.impl;

import com.xhystc.zhihu.commons.RedisUtils;
import com.xhystc.zhihu.dao.CommentDao;
import com.xhystc.zhihu.dao.QuestionDao;
import com.xhystc.zhihu.dao.VoteDao;
import com.xhystc.zhihu.model.*;
import com.xhystc.zhihu.rediscache.TimeoutHandle;
import com.xhystc.zhihu.service.VoteService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;


@Component
public class RedisCacheVoteImpl implements VoteService,TimeoutHandle
{

	private VoteDao voteDao;
	private QuestionDao questionDao;
	private CommentDao commentDao;
	private JedisPool jedisPool;


	@Autowired
	public RedisCacheVoteImpl(VoteDao voteDao, QuestionDao questionDao, CommentDao commentDao, JedisPool jedisPool)
	{
		this.voteDao = voteDao;
		this.questionDao = questionDao;
		this.commentDao = commentDao;
		this.jedisPool = jedisPool;
	}

	static final private Logger logger  = Logger.getLogger(RedisCacheVoteImpl.class);

	@Override
	public boolean doVote(Vote vote)
	{
		try(Jedis redis = jedisPool.getResource())
		{
			RedisUtils.active(redis, "user", vote.getUserId());
			RedisUtils.active(redis, vote.getParentType(), vote.getParentId());
			String userKey = RedisUtils.getUserVoteKey(vote.getParentType(),vote.getUserId());

			redis.sadd(userKey,vote.getParentId().toString());
			incVote(redis,vote.getParentType(),vote.getParentId(),1,needCache(vote));
			return true;
		}

	}

	@Override
	public boolean disVote(Vote vote)
	{
		try(Jedis redis = jedisPool.getResource())
		{
			RedisUtils.active(redis, "user", vote.getUserId());
			RedisUtils.active(redis, vote.getParentType(), vote.getParentId());
			String userKey = RedisUtils.getUserVoteKey(vote.getParentType(),vote.getUserId());
			Long l = redis.srem(userKey,vote.getParentId().toString());

			if(l==null || l<=0){
				voteDao.deleteVote(vote);
			}
			incVote(redis,vote.getParentType(),vote.getParentId(),-1,needCache(vote));

		}
		return true;
	}


	@Override
	public void fetchUserVotes(Long userId,Votable votable)
	{
		try(Jedis redis = jedisPool.getResource())
		{
			String s = redis.hget(RedisUtils.getVoteKey(votable.type()),votable.id().toString());
			if(s!=null){
				int score = Integer.parseInt(s);
				votable.votes(score);
			}

			if (userId != null && userId>0){
				votable.isVoted(doIsVoted(redis, new Vote(userId,votable.type(), votable.id())));
			}
		}
	}

	@Override
	public void fetchUserVotes(Long userId,List<? extends Votable> votables,String type)
	{
		logger.debug("userId:"+userId+" type:"+type+" vo size:"+votables.size());
		try(Jedis redis = jedisPool.getResource())
		{
			Set<Long> voteIds = null;
			if(userId !=null && userId>0){
				Vote vote = new Vote(userId,type,null);
				List<Vote> votes = voteDao.selectVotes(vote);
				voteIds = new HashSet<>(votes.size());
				for(Vote v : votes){
					voteIds.add(v.getParentId());
				}
				fetchRedisUserVotesSet(redis,voteIds,userId,type);
			}
			Map<String,String> scoreMap = redis.hgetAll(RedisUtils.getVoteKey(type));
			for(Votable votable : votables){
				if(voteIds !=null ){
					if(voteIds.contains(votable.id())){
						votable.isVoted(true);
					}else {
						votable.isVoted(false);
					}
				}
				String l = scoreMap.get(votable.id().toString());
				if(l!=null){
					votable.votes(Integer.parseInt(l));
				}
			}
		}
	}

	@Override
	public int getVotes(String type, Long id)
	{
		try(Jedis redis = jedisPool.getResource())
		{
			String l = redis.hget(RedisUtils.getVoteKey(type),id.toString());
			if(l!=null){
				return Integer.parseInt(l);
			}
		}

		return getDBVotes(type,id);
	}

	@Override
	public boolean isVoted(Long userId, String type, Long id)
	{
		try(Jedis redis = jedisPool.getResource())
		{
			Vote vote = new Vote(userId,type,id);
			return doIsVoted(redis,vote);
		}
	}

	@Override
	public void onTimeout(Jedis redis,String type, Long id)
	{
		if("user".equals(type)){
			Set<Long> set = delUserVoteSet(redis,RedisUtils.getUserVoteKey("question",id));
			for(Long l : set){
				doInsertVote("question",l,id);
			}
			set = delUserVoteSet(redis,RedisUtils.getUserVoteKey("comment",id));
			for(Long l : set){
				doInsertVote("comment",l,id);
			}

		}else if("question".equals(type)){
			int score = delVoteMap(redis,RedisUtils.getVoteKey(type),id);
			if(score>0){
				questionDao.setAgree(id,score);
			}
		}else if("comment".equals(type)){
			int score = delVoteMap(redis,RedisUtils.getVoteKey(type),id);
			if(score>0){
				commentDao.setAgree(id,score);
			}
		}else {
			throw new IllegalArgumentException("unknow type");
		}
	}

	private int getDBVotes(String type, Long id){
		int ret;
		if("question".equals(type)){
			Question question = questionDao.getQuestionById(id);
			ret = question.getAgree();
		}else if("comment".equals(type)){
			Comment comment = commentDao.getCommentById(id);
			ret = comment.getAgree();
		}else {
			throw new IllegalArgumentException("unknow type");
		}
		return ret;
	}

	private Set<Long> delUserVoteSet(Jedis redis,String key){
		Set<String> set = redis.smembers(key);
		redis.del(key);
		Set<Long> ret = new HashSet<>(set.size());
		for(String s : set){
			ret.add(Long.parseLong(s));
		}
		return ret;
	}

	private int delVoteMap(Jedis redis,String key,Long id){
		int score = (int) Long.parseLong(redis.hget(key,id.toString()));
		redis.hdel(key,id.toString());
		return score;
	}

	private boolean doIsVoted(Jedis redis, Vote vote){
		String key = RedisUtils.getUserVoteKey(vote.getParentType(),vote.getUserId());
		boolean ret = redis.sismember(key,vote.getParentId().toString());
		if (ret)
		{
			return true;
		}
		List<Vote> votes = voteDao.selectVotes(vote);
		return votes!=null && votes.size()>0;
	}


	private void fetchRedisUserVotesSet(Jedis redis,Set<Long> ids,Long userId,String type){
		String key = RedisUtils.getUserVoteKey(type,userId);
		Set<String> set = redis.smembers(key);
		for(String s : set){
			ids.add(Long.parseLong(s));
		}
	}

	private  void incVote(Jedis redis,String type,Long id,int i,boolean cache){
		String voteKey = RedisUtils.getVoteKey(type);
		if(cache){
			if(redis.hget(voteKey,id.toString())!=null){
				redis.hincrBy(voteKey,id.toString(),i);
			}else {
				int votes = getDBVotes(type,id)+i;
				Long res = redis.hsetnx(voteKey,id.toString(),votes+"");
				if(res==null || res==0 && votes!=0){
					redis.hincrBy(voteKey,id.toString(),i);
				}
			}
		}else {
			incDBVote(type,id,i);
		}
	}

	private void incDBVote(String type, Long id, int i){
		if("question".equals(type)){
			questionDao.increaseAgree(id,i);
		}else if("comment".equals(type)){
			commentDao.increaseAgree(id,i);
		}else {
			throw new IllegalArgumentException("unknow type");
		}
	}
	private void doInsertVote(String type,Long id,Long userId){
		Vote vote = new Vote();
		vote.setParentType(type);
		vote.setParentId(id);
		vote.setUserId(userId);
		voteDao.insertVote(vote);
	}



	private boolean needCache(Vote vote){
		return "question".equals(vote.getParentType());
	}
}









