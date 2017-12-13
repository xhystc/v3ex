package com.xhystc.zhihu.service;

import com.xhystc.zhihu.model.Comment;
import com.xhystc.zhihu.model.CommentInform;
import com.xhystc.zhihu.model.vo.page.CommentPage;

import java.util.List;

public interface CommentService
{
	void initCommentInform(String type,Long id);
	CommentInform doComment(Comment comment);
	CommentPage getQuestionCommentPage(String type, Long id,int page,int pageSize);
	CommentInform getCommentInform(String type, Long id);
}







