package com.xhystc.zhihu.service.impl;

import com.xhystc.zhihu.commons.RedisUtils;

import com.xhystc.zhihu.dao.VoteDao;
import com.xhystc.zhihu.dao.VoteInformDao;
import com.xhystc.zhihu.model.*;
import com.xhystc.zhihu.model.vo.query.VoteInformQueryCondition;
import com.xhystc.zhihu.model.vo.query.VoteQueryCondition;
import com.xhystc.zhihu.service.VoteService;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.Pipeline;
import redis.clients.jedis.Response;

import javax.annotation.PostConstruct;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;


@Component
public class RedisCacheVoteServiceImpl extends TimerTask implements VoteService,Runnable,InitializingBean
{
	static final private Logger logger = Logger.getLogger(RedisCacheVoteServiceImpl.class);
	static final private String ACTIVE_PREFIX = "vote-active-";
	static final private String INFORM_PREFIX = "vote-inform-";
	static final private String USER_PREFIX = "vote-userVotes-";


	private String[] votableList = {"question","comment"};
	private VoteDao voteDao;
	private JedisPool jedisPool;
	private VoteInformDao voteInformDao;
	private long timeout = 1000*60*3;


	@Autowired
	public RedisCacheVoteServiceImpl(VoteDao voteDao,  VoteInformDao voteInformDao, JedisPool jedisPool)
	{
		this.voteDao = voteDao;
		this.jedisPool = jedisPool;
		this.voteInformDao = voteInformDao;
	}


	@Transactional(rollbackFor = RuntimeException.class)
	@Override
	public boolean doVote(Long userId, String type, Long id)
	{
		try (Jedis redis = jedisPool.getResource())
		{
			return addUserVotes(redis, userId, type, id) && incVote(redis, userId, type, id, 1);
		}

	}

	@Transactional(rollbackFor = RuntimeException.class)
	@Override
	public boolean disVote(Long userId, String type, Long id)
	{
		try (Jedis redis = jedisPool.getResource())
		{
			return delUserVotes(redis, userId, type, id) && incVote(redis, userId, type, id, -1);
		}
	}


	@Override
	public void fetchUserVote(Long userId, Votable votable)
	{
		try (Jedis redis = jedisPool.getResource())
		{
			votable.setVoteInform(getVoteInform(redis, votable.type(),votable.id()));
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
			fetchVoteInforms(redis,votables);
			if(userId != null && userId>0){
				fetchIsVotes(redis,userId,votables);
			}
		}
	}

	@Override
	public boolean isVote(Long userId,String type,Long id){
		try (Jedis redis = jedisPool.getResource())
		{
			return getIsVote(redis, userId,type,id);
		}
	}

	@Override
	public VoteInform voteInform(String type,Long id){
		try (Jedis redis = jedisPool.getResource())
		{
			return getVoteInform(redis,type,id);
		}
	}

	@Override
	public void run()
	{
		logger.debug("vote time out");
		try(Jedis redis = jedisPool.getResource())
		{
			Set<Long> timeoutUser = getTimeoutIds(redis,"user",timeout);
			for (Long id : timeoutUser){
				logger.debug("time out user:"+id);
			}
			for(String type : votableList){
				dealUserTimeout(redis,timeoutUser,type);
			}
			for(String type : votableList){
				Set<Long> ids = getTimeoutIds(redis,type,timeout);
				for (Long id : timeoutUser){
					logger.debug("time out votable:"+id+" type:"+type);
				}
				dealVotableTimeout(redis,ids,type);
			}


		}
	}

	@Override
	public void afterPropertiesSet() throws Exception
	{
		logger.debug("vote service do init");
		Timer timer = new Timer(true);
		timer.schedule(this,timeout,timeout);
	}

	private Set<Long> getTimeoutIds(Jedis redis,String type,long timeout){
		String activeKey;
		activeKey = activeKey(type);
		long curr = System.currentTimeMillis();
		Set<String> idSet = RedisUtils.getActive(redis,curr-timeout,activeKey);
		RedisUtils.delActive(redis,activeKey,idSet);

		Set<Long> ids = new HashSet<>(idSet.size());

		for(String s : idSet){
			ids.add(Long.parseLong(s));
		}
		return ids;
	}


	private void dealVotableTimeout(Jedis redis,Set<Long> ids,String type){
		Map<Long,Response<Map<String,String>>> resposeMap = new HashMap<>(ids.size());
		Pipeline pipeline = redis.pipelined();
		for(Long id : ids){
			resposeMap.put(id,pipeline.hgetAll(informKey(type,id)));
		}
		pipeline.sync();

		for(Long id : ids){
			Map<String,String> informMap = resposeMap.get(id).get();
			VoteInform voteInform = mapToInform(informMap,type,id);
			voteInformDao.updateVoteInform(voteInform);
			if(!RedisUtils.isActive(redis,activeKey(type),id.toString())){
				redis.del(informKey(type,id));
			}
		}

	}

	private void dealUserTimeout(Jedis redis,Set<Long> ids,String type){
		Map<Long,Response<Set<String>>> resposeMap = new HashMap<>(ids.size());
		Pipeline pipeline = redis.pipelined();
		for(Long id : ids){
			resposeMap.put(id,pipeline.smembers(userkey(id,type)));
		}
		pipeline.sync();
		for(Long id : ids){
			Set<String> targetIds = resposeMap.get(id).get();
			for(String s : targetIds){
				try
				{
					Vote vote = new Vote(id,type,Long.parseLong(s));
					voteDao.insertVote(vote);
				}catch (Exception e){
					logger.info(e.getMessage());
				}
			}
			RedisUtils.removeFromSet(redis,userkey(id,type),targetIds);
		}
	}


	private void fetchRedisUserVotesSet(Jedis redis, Set<Long> ids, Long userId, String type,Set<Long> include)
	{
		String key = userkey(userId, type);
		Set<String> set = redis.smembers(key);
		if (set == null)
		{
			return;
		}
		for (String s : set)
		{
			Long id = Long.parseLong(s);
			if(include.contains(id))
			{
				ids.add(id);
			}
		}
	}

	private boolean getIsVote(Jedis redis, Long userId, String type, Long id)
	{

		String key = userkey(userId, type);
		boolean ret = redis.sismember(key,id.toString());
		if (ret)
		{
			return true;
		}
		VoteQueryCondition condition = new VoteQueryCondition();
		condition.setParentId(id);
		condition.setParentType(type);
		condition.setUserId(userId);
		List<Vote> votes = voteDao.selectVotes(condition);

		return votes != null && votes.size() > 0;
	}

	private void fetchIsVotes(Jedis redis, Long userId, List<? extends Votable> votables)
	{
		if (votables.size() == 0)
		{
			return;
		}
		String type = votables.get(0).type();
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

	private VoteInform getVoteInform(Jedis redis, String type,Long id)
	{
		String informKey = informKey(type, id);
		Map<String, String> informMap = redis.hgetAll(informKey);
		VoteInform voteInform;
		if (informMap != null && informMap.size()>0)
		{
			voteInform = mapToInform(informMap,type,id);
		} else
		{
			voteInform = voteInformDao.getVoteInformById(VoteInform.voteInformId(type,id));
		}
		return voteInform;
	}

	private void fetchVoteInforms(Jedis redis, List<? extends Votable> votables)
	{
		List<Response<Map<String, String>>> responses = new ArrayList<>(votables.size());

		Pipeline pipeline = redis.pipelined();
		for (Votable votable : votables)
		{
			responses.add(pipeline.hgetAll(informKey(votable.type(), votable.id())));
		}
		pipeline.sync();

		VoteInformQueryCondition condition = new VoteInformQueryCondition();
		condition.setInclude(new HashSet<>(votables.size()));
		for (int i = 0; i < responses.size(); i++)
		{
			Map<String, String> informMap = responses.get(i).get();
			Votable votable = votables.get(i);
			if (informMap == null || informMap.size() == 0)
			{
				condition.getInclude().add(VoteInform.voteInformId(votable.type(),votable.id()));
				votable.setVoteInform(null);
			} else
			{
				votable.setVoteInform(mapToInform(informMap,votable.type(),votable.id()));
			}
		}

		if (condition.getInclude().size()>0){
			List<VoteInform> voteInforms = voteInformDao.selectVoteInform(condition);
			for(Votable votable : votables){
				if(votable.getVoteInform()==null)
				{
					for(VoteInform voteInform : voteInforms){
						if(voteInform.getId().equals(VoteInform.voteInformId(votable.type(),votable.id()))){
							votable.setVoteInform(voteInform);
						}
					}
				}
			}
		}

	}

	private boolean incVote(Jedis redis, Long userId, String type, Long id, int i)
	{
		if(needCache(type,id) || redis.exists(informKey(type,id))){
			return storeVoteInform(redis,userId,type,id,i);
		}else {
			return incDBVote(userId,type,id,i);
		}

	}


	private boolean storeVoteInform(Jedis redis, Long userId,String type,Long id, int i)
	{

		RedisUtils.active(redis, activeKey(type), id.toString());
		String informKey = informKey(type, id);
		if (!redis.exists(informKey))
		{
			if(!incDBVote(userId,type,id,i)){
				return false;
			}
			VoteInform voteInform = voteInformDao.getVoteInformById(VoteInform.voteInformId(type, id));
			return redis.hmset(informKey, informToMap(voteInform)) != null;

		} else
		{
			if (i > 0)
			{
				Pipeline pipeline = redis.pipelined();
				DateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
				pipeline.hset(informKey, "lastVoteUser", userId.toString());
				pipeline.hset(informKey, "lastVoteDate", format.format(new Date()));
				pipeline.hincrBy(informKey, "voteCount", i);
				pipeline.sync();
				return true;
			}else {
				return redis.hincrBy(informKey, "voteCount", i)!=null;
			}

		}

	}

	private boolean addUserVotes(Jedis redis, Long userId, String type, Long id)
	{
		if(needCache(userId)){
			RedisUtils.active(redis, activeKey("user"), userId.toString());
			return redis.sadd(userkey(userId, type), id.toString())>0;
		}else {
			Vote vote = new Vote(userId,type,id);
			return voteDao.insertVote(vote)>0;
		}
	}

	private boolean delUserVotes(Jedis redis, Long userId, String type, Long id)
	{
		Long l = redis.srem(userkey(userId, type), id.toString());
		if (l == null || l <= 0)
		{
			Vote vote = new Vote(userId, type, id);
			return voteDao.deleteVote(vote)>0;
		}
		return true;
	}

	private boolean incDBVote( Long userId,String type,Long id, int i){
		VoteInform voteInform = new VoteInform();
		voteInform.setVoteCount(i);
		voteInform.setId(VoteInform.voteInformId(type,id));
		if(i>0){
			voteInform.setLastVoteUser(userId);
			voteInform.setLastVoteTime(new Date());
		}

		return voteInformDao.increaseVoteInform(voteInform)>0;
	}



	private static String activeKey(String type)
	{
		return ACTIVE_PREFIX + type;
	}

	private static String informKey(String type, Long id)
	{
		return INFORM_PREFIX + type + "-" + id.toString();
	}

	private static String userkey(Long userId, String type)
	{
		return USER_PREFIX + type + "-" + userId.toString();
	}

	private static VoteInform mapToInform(Map<String, String> informMap,String type,Long id)
	{
		VoteInform voteInform = new VoteInform();
		String userIdStr =  informMap.get("lastVoteUser");
		Long userId = StringUtils.isBlank(userIdStr)?0:Long.parseLong(informMap.get("lastVoteUser"));
		int votes = Integer.parseInt(informMap.get("voteCount"));
		String s = informMap.get("lastVoteTime");
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = null;
		if( !StringUtils.isBlank(s)){
			try
			{
				date = format.parse(s);
			} catch (ParseException e)
			{
				date = null;
			}
		}
		voteInform.setLastVoteUser(userId);
		voteInform.setVoteCount(votes);
		voteInform.setLastVoteTime(date);
		voteInform.setId(VoteInform.voteInformId(type,id));

		return voteInform;
	}

	private static Map<String, String> informToMap(VoteInform voteInform)
	{
		Map<String, String> informMap = new HashMap<>(3);

		DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		informMap.put("voteCount", voteInform.getVoteCount() + "");
		informMap.put("lastVoteUser", voteInform.getLastVoteUser()==null?"":voteInform.getLastVoteUser().toString());
		informMap.put("lastVoteDate", voteInform.getLastVoteTime()==null?"":format.format(voteInform.getLastVoteTime()));
		return informMap;
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

	public String[] getVotableList()
	{
		return votableList;
	}

	public void setVotableList(String[] votableList)
	{
		this.votableList = votableList;
	}

}









