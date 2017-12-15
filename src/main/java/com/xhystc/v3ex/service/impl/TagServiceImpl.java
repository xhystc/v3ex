package com.xhystc.v3ex.service.impl;

import com.xhystc.v3ex.dao.QuestionTagDao;
import com.xhystc.v3ex.dao.TagDao;
import com.xhystc.v3ex.model.Tag;
import com.xhystc.v3ex.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TagServiceImpl implements TagService
{
	@Autowired
	TagDao tagDao;
	@Autowired
	QuestionTagDao questionTagDao;

	@Override
	public List<Tag> getAllTag()
	{
		return tagDao.selectAll();
	}

	@Override
	public List<Tag> getTagsByName(String name)
	{
		return tagDao.getTagsByName(name);
	}

	@Override
	public List<Tag> getQuestionTags(Long quetionId)
	{
		return questionTagDao.getQuestionTags(quetionId);
	}


}
