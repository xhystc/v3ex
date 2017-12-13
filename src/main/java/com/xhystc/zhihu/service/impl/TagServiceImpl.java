package com.xhystc.zhihu.service.impl;

import com.xhystc.zhihu.dao.TagDao;
import com.xhystc.zhihu.model.Tag;
import com.xhystc.zhihu.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TagServiceImpl implements TagService
{
	@Autowired
	TagDao tagDao;

	@Override
	public List<Tag> getAllTag()
	{
		return tagDao.selectAll();
	}

	@Override
	public List<Tag> getByName(String name)
	{
		return tagDao.selectTagsByName(name);
	}
}
