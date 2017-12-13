package com.xhystc.zhihu.dao;

import com.xhystc.zhihu.model.Tag;

import java.util.List;
import java.util.Set;

public interface TagDao
{
	int insertTag(Tag tag);
	int deleteTag(Tag tag);
	List<Tag> selectAll();
	List<Tag> selectTagsByName(String name);
	int increaseCount(List<Long> ids);
}




