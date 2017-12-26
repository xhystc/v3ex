package com.xhystc.v3ex.service.impl;

import com.xhystc.v3ex.commons.RedisUtils;

import com.xhystc.v3ex.dao.VoteDao;
import com.xhystc.v3ex.model.*;
import com.xhystc.v3ex.model.vo.query.VoteQueryCondition;
import com.xhystc.v3ex.service.VoteService;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.Pipeline;
import redis.clients.jedis.Response;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.*;

@Component
public class RedisCacheVoteServiceImpl extends TimerTask implements VoteService,Runnable,InitializingBean
{
	static final private Logger logger = Logger.getLogger(RedisCacheVoteServiceImpl.class);
	static final private String ACTIVE_PREFIX = "vote-active-";
	static final private String INFORM_PREFIX = "vote-inform-";

	private EntityType[] votableList = {EntityType.question,EntityType.comment};
	private VoteDao voteDao;
	private JedisPool jedisPool;

	@Value("#{configProperties['vote.active.timeout']}")
	private long timeout = 1000*60*3;


	@Autowired
	public RedisCacheVoteServiceImpl(VoteDao voteDao, JedisPool jedisPool)
	{
		this.voteDao = voteDao;
		this.jedisPool = jedisPool;
	}


	@Transactional(rollbackFor = RuntimeException.class)
	@Override
	public boolean doVote(Long userId, EntityType type, Long id)
	{
		try (Jedis redis = jedisPool.getResource())
		{
			RedisUtils.active(redis, activeKey(type), id.toString());
			String informKey = informKey(type, id);
			return redis.sadd(informKey,userId.toString())!=null;
		}

	}

	@Transactional(rollbackFor = RuntimeException.class)
	@Override
	public boolean disVote(Long userId, EntityType type, Long id)
	{
		try (Jedis redis = jedisPool.getResource())
		{
			RedisUtils.active(redis, activeKey(type), id.toString());
			String informKey = informKey(type, id);
			Long res = redis.srem(informKey, userId.toString());
			if(res !=null && res >0){
				return true;
			}else {
				if(voteDao.deleteVote(new Vote(userId, type, id))>0){
					voteDao.incVoteCount(type,id,-1);
					return true;
				}
				return false;
			}
		}
	}


	@Override
	public void fetchUserVote(Long userId, Votable votable)
	{
		try (Jedis redis = jedisPool.getResource())
		{
			Long count = redis.scard(informKey(votable.type(),votable.id()));
			votable.setVoteCount(votable.getVoteCount()+(count==null?0:count.intValue()));
			if (userId != null && userId > 0)
			{
				votable.setIsVoted(getIsVote(redis,userId,votable.type(),votable.id()));
			}
		}
	}

	@Override
	public void fetchUserVotes(Long userId, List<? extends Votable> votables)
	{
		try (Jedis redis = jedisPool.getResource())
		{
			fetchVoteCount(redis,votables);
			if(userId != null && userId>0){
				fetchIsVotes(redis,userId,votables);
			}
		}
	}

	@Override
	public boolean isVote(Long userId,EntityType type,Long id){
		try (Jedis redis = jedisPool.getResource())
		{
			return getIsVote(redis, userId,type,id);
		}
	}

	@Override
	public int voteCount(EntityType type,Long id){
		try (Jedis redis = jedisPool.getResource())
		{
			String key  = informKey(type,id);
			Long count = redis.scard(key);
			return voteDao.getVoteCount(type,id)+(count==null?0:count.intValue());
		}
	}

	@Transactional(rollbackFor = RuntimeException.class)
	@Override
	public void run()
	{
		logger.debug("vote time out");
		try(Jedis redis = jedisPool.getResource())
		{
			for(EntityType type : votableList){
				Set<Long> ids = getTimeoutIds(redis,type,timeout);
				dealVotableTimeout(redis,ids,type);
			}
		}
	}

	@Override
	public void afterPropertiesSet()
	{
		logger.debug("vote service do init");
		ScheduledExecutorService service = Executors.newScheduledThreadPool(1, r -> {
			Thread t = Executors.defaultThreadFactory().newThread(r);
			t.setDaemon(true);
			t.setName("vote-service-flush");
			return t;
		});
		service.scheduleWithFixedDelay(this,timeout,timeout, TimeUnit.MILLISECONDS);
	}

	private Set<Long> getTimeoutIds(Jedis redis,EntityType type,long timeout){
		String activeKey;
		activeKey = activeKey(type);
		long curr = System.currentTimeMillis();
		Set<String> idSet = RedisUtils.getActiveByScore(redis, activeKey, curr-timeout);
		RedisUtils.delActive(redis,activeKey,idSet);

		Set<Long> ids = new HashSet<>(idSet.size());

		for(String s : idSet){
			ids.add(Long.parseLong(s));
		}
		return ids;
	}


	private void dealVotableTimeout(Jedis redis,Set<Long> ids,EntityType type){
		Map<Long,Response<Set<String>>> responseMap = new HashMap<>(ids.size());
		Pipeline pipeline = redis.pipelined();
		for(Long id : ids){
			responseMap.put(id,pipeline.smembers(informKey(type,id)));
		}
		pipeline.sync();

		for(Long id : ids){
			Set<String> userSet = responseMap.get(id).get();
			voteDao.incVoteCount(type,id,userSet.size());


			for(String s : userSet){
				pipeline.srem(informKey(type,id),s);
				Long userId = Long.parseLong(s);
				voteDao.insertVote(new Vote(userId,type,id));
			}
			pipeline.sync();
			if(!RedisUtils.isActive(redis,activeKey(type),id.toString())){
				redis.del(informKey(type,id));
			}
		}

	}


	private void fetchRedisUserVotesSet(Jedis redis, Set<Long> ids, Long userId, EntityType type,Set<Long> include)
	{
		Map<Long,Response<Boolean>> responseMap = new HashMap<>(include.size());
		Pipeline pipeline = redis.pipelined();
		for(Long id : include){
			String informKey = informKey(type,id);
			responseMap.put(id,pipeline.sismember(informKey,userId.toString()));
		}
		pipeline.sync();
		for(Map.Entry<Long,Response<Boolean>> en : responseMap.entrySet()){
			if(en.getValue().get()){
				ids.add(en.getKey());
			}
		}
	}

	private boolean getIsVote(Jedis redis, Long userId, EntityType type, Long id)
	{
		String key = informKey(type, id);
		return redis.sismember(key, userId.toString()) || voteDao.getVote(userId, type, id) != null;
	}

	private void fetchIsVotes(Jedis redis, Long userId, List<? extends Votable> votables)
	{
		if (votables.size() == 0)
		{
			return;
		}
		EntityType type = votables.get(0).type();
		Set<Long> voteIds = null;
		if (userId != null && userId > 0)
		{
			VoteQueryCondition condition = new VoteQueryCondition();

			condition.setParentType(type);
			condition.setUserId(userId);
			condition.setInclude(new HashSet<>(votables.size()));
			for(Votable votable : votables){
				condition.getInclude().add(votable.id());
			}
			List<Vote> votes = voteDao.selectVotes(condition);

			voteIds = new HashSet<>(votes.size());
			for (Vote v : votes)
			{
				voteIds.add(v.getParentId());
			}
			fetchRedisUserVotesSet(redis, voteIds, userId, type,condition.getInclude());

			for (Votable votable : votables)
			{
				votable.setIsVoted(voteIds.contains(votable.id()));
			}
		}
	}


	private void fetchVoteCount(Jedis redis, List<? extends Votable> votables)
	{
		List<Response<Long>> responses = new ArrayList<>(votables.size());

		Pipeline pipeline = redis.pipelined();
		for (Votable votable : votables)
		{
			responses.add(pipeline.scard(informKey(votable.type(),votable.id())));
		}
		pipeline.sync();

		for (int i = 0; i < responses.size(); i++)
		{
			Votable votable = votables.get(i);
			Long count = responses.get(i).get();
			votable.setVoteCount(votable.getVoteCount()+(count==null?0:count.intValue()));
		}

	}


	private static String activeKey(EntityType type)
	{
		return ACTIVE_PREFIX + type.toString();
	}

	private static String informKey(EntityType type, Long id)
	{
		return INFORM_PREFIX + type + "-" + id.toString();
	}


	private boolean needCache(String type,Long id){
		return true;
	}

	private boolean needCache(Long userId){
		return true;
	}

	public long getTimeout()
	{
		return timeout;
	}

	public void setTimeout(long timeout)
	{
		this.timeout = timeout;
	}

	public EntityType[] getVotableList()
	{
		return votableList;
	}

	public void setVotableList(EntityType[] votableList)
	{
		this.votableList = votableList;
	}

}









