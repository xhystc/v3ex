package com.xhystc.v3ex.dao;

import com.xhystc.v3ex.model.Tag;

import java.util.List;
import java.util.Set;

public interface TagDao
{
	int insertTag(Tag tag);
	int deleteTag(Tag tag);
	List<Tag> selectAll();
	List<Tag> getTagsByName(String name);
	Tag getTagById(Long id);
	int increaseCount(List<Long> ids);

}




