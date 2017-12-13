package com.xhystc.zhihu.commons;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.Pipeline;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class RedisUtils
{
	static private final String PREFIX = "active";

	public static void active(Jedis redis, String key, String value){
		redis.zadd(key,System.currentTimeMillis(),value);
	}


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
	public static Set<String> getActive(Jedis redis, double limit,String key){
		Set<String> set = redis.zrangeByScore(key,0,limit);
		return set;
	}

	public static String getVoteKey(String type){
		return "vote-"+type;
	}
	public static String getUserVoteKey(String type,Long id){
		String prefix = "vote-user-"+type;
		return prefix+"-"+id;
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



}







