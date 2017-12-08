package com.xhystc.zhihu.rediscache;

import redis.clients.jedis.Jedis;

public interface TimeoutHandle
{
	void onTimeout(Jedis redis,String type, Long id);
}
