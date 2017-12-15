package com.xhystc.v3ex.dao;

import com.xhystc.v3ex.model.CommentInform;
import com.xhystc.v3ex.model.vo.query.CommentInformQueryCondition;

import java.util.Date;
import java.util.List;

public interface CommentInformDao
{
	int insertCommentInform(CommentInform commentInform);
	int updateCommentInform(CommentInform commentInform);
	CommentInform getCommentInformById(String id);
	List<CommentInform> selectCommentInform(CommentInformQueryCondition condition);
	int increaseCommentInform(CommentInform commentInform);
}
