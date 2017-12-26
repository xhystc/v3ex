package com.xhystc.v3ex.service;

import java.util.List;

public interface HotTopicService
{
	void incScore(Long id,double score);
	List getHotTopics();
}
