package com.xhystc.zhihu.service;

import com.xhystc.zhihu.model.Tag;

import java.util.List;

public interface TagService
{
	List<Tag> getAllTag();
	List<Tag> getByName(String name);
}
