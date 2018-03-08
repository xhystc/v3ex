package com.xhystc.v3ex.test;

import com.alibaba.fastjson.JSON;
import com.xhystc.v3ex.async.Event;
import com.xhystc.v3ex.async.EventType;
import com.youbenzi.mdtool.tool.MDTool;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

public class MDToolsTest
{
	public static void main(String[] args){
		/*MarkdownProcessor processor = new MarkdownProcessor();
		//## 测试
// ### &lt;script&gt;alert('xixi')&lt;/script&gt;
		System.out.println(MDTool.markdown2Html("hha+sds"));*/
		/*Event event = new Event(EventType.USER_LOGIN_EVENT,1L,"dsd",2L);
		System.out.println(JSON.toJSONString(event));*/
		JedisPool jedisPool = new JedisPool("localhost");
		try(Jedis redis = jedisPool.getResource())
		{
			System.out.println(redis.blpop(0,"xixi"));
		}
	}

}
