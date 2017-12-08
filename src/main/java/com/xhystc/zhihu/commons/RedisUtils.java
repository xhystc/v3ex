package com.xhystc.zhihu.commons;

import redis.clients.jedis.Jedis;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class RedisUtils
{
	static private final String PREFIX = "active";

	public static void active(Jedis redis, String type, Long id){
		redis.zadd(PREFIX +"-"+type,System.currentTimeMillis(),id.toString());
	}

	public static void delActive(Jedis redis,String type,Long id){
		redis.zrem(PREFIX +"-"+type,id.toString());
	}
	public static List<Long> getActive(Jedis redis,double min, double max,String type){
		Set<String> set = redis.zrangeByScore(PREFIX +"-"+type,min,max);

		List<Long> ret = new ArrayList<>(set.size());
		for(String s : set){
			ret.add(Long.parseLong(s));
		}
		return ret;
	}

	public static String getVoteKey(String type){
		String prefix = "vote-"+type;
		return prefix;
	}
	public static String getUserVoteKey(String type,Long id){
		String prefix = "vote-user-"+type;
		return prefix+"-"+id;
	}



}







