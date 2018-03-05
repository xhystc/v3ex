package com.xhystc.v3ex.commons;

import com.xhystc.v3ex.model.EntityType;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Pipeline;
import redis.clients.jedis.Tuple;

import java.util.Set;

public class RedisUtils
{
	static private final String PREFIX = "active";

	public static void active(Jedis redis, String key, String value){
		redis.zadd(key,System.currentTimeMillis(),value);
	}
	static final private String VOTE_ACTIVE_PREFIX = "vote-active-";
	static final private String VOTE_INFORM_PREFIX = "vote-inform-";
	static final private String QUESTION_ACTIVE_PREFIX = "question-rank";
	static final private String RANK_INFORM_KEY = "rank-inform-key";
	static final private String QUESTION_SCORE_MODIFY = "question-score-modify";

	public static void delActive(Jedis redis,String key,String value){
		redis.zrem(key,value);
	}
	public static void delActive(Jedis redis,String key,Set<String> values){
		removeFromZSet(redis,key,values);
	}
	public static boolean isActive(Jedis redis,String key,String value){
		Long rank = redis.zrank(key,value);
		return rank!=null && rank>=0;
	}
	public static Set<String> getActiveByScore(Jedis redis, String key, double limit){
		Set<String> set = redis.zrangeByScore(key,0,limit);
		return set;
	}

	public static Set<String> getActiveByScore(Jedis redis, String key, double start, double end){
		Set<String> set = redis.zrangeByScore(key,start,end);
		return set;
	}

	public static Set<Tuple> getActiveByRange(Jedis redis,String key, long start, long end){
		Set<Tuple> set = redis.zrevrangeWithScores(key,start,end);
		return set;
	}

	public static void removeFromSet(Jedis redis,String key,Set<String> set){
		Pipeline pipeline = redis.pipelined();
		for(String value : set){
			pipeline.srem(key,value);
		}
		pipeline.sync();
	}
	public static void removeFromZSet(Jedis redis,String key,Set<String> set){
		Pipeline pipeline = redis.pipelined();
		for(String value : set){
			pipeline.zrem(key,value);
		}
		pipeline.sync();
	}

	public static String voteActiveKey(EntityType type)
	{
		return VOTE_ACTIVE_PREFIX + type.toString();
	}

	public static String voteInformKey(EntityType type, Long id)
	{
		return VOTE_INFORM_PREFIX + type + "-" + id.toString();
	}
	public static String questionActiveKey(){
		return QUESTION_ACTIVE_PREFIX;
	}
	public static String questionActiveKey(Long tagId){
		return tagId==null?QUESTION_ACTIVE_PREFIX:QUESTION_ACTIVE_PREFIX+"-"+tagId;
	}


	public static String questionScoreModityKey(){
		return QUESTION_SCORE_MODIFY;
	}


}







