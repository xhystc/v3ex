package com.xhystc.v3ex.dao;

import com.xhystc.v3ex.model.CommentInform;
import java.util.List;
import java.util.Map;

public interface CommentInformDao
{
	int insertCommentInform(CommentInform commentInform);
	int updateCommentInform(CommentInform commentInform);
	CommentInform getCommentInformById(String id);
	List<CommentInform> selectCommentInform(Map<String,Object> condition);
	int increaseCommentInform(CommentInform commentInform);
}
