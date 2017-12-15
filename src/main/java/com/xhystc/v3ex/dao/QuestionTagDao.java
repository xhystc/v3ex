package com.xhystc.v3ex.dao;

import com.xhystc.v3ex.model.Tag;

import java.util.List;

public interface QuestionTagDao
{
	List<Tag> getQuestionTags(Long quetionId);
	int insertQuestionTag(Long questionId,Long tag);
	int tagCount(Long questionId);
	List<Long> getQuestionTagIds(Long qId);
}



