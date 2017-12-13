package com.xhystc.zhihu.dao;

import com.xhystc.zhihu.model.Tag;

import java.util.List;

public interface QuestionTagDao
{
	List<Tag> getQuestionTags(Long quetionId);
	int insertQuestionTag(Long questionId,Long tags);
}


