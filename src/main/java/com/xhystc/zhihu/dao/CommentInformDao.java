package com.xhystc.zhihu.dao;

import com.xhystc.zhihu.model.CommentInform;

import java.util.Date;

public interface CommentInformDao
{
	int insertCommentInform(CommentInform commentInform);
	int updateCommentInform(CommentInform commentInform);
	CommentInform selectCommentInform(String id);
	int increaseCommentInform(String id,Long lastCommentUser,Date lastCommentDate,int count);
}
