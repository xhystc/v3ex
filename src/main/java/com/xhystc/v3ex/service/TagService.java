package com.xhystc.v3ex.service;

import com.xhystc.v3ex.model.Tag;

import java.util.List;

public interface TagService
{
	List<Tag> getAllTag();
	List<Tag> getTagsByName(String name);
}
