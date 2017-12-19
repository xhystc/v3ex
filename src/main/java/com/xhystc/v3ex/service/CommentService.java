package com.xhystc.v3ex.service;

import com.xhystc.v3ex.model.Comment;
import com.xhystc.v3ex.model.CommentInform;
import com.xhystc.v3ex.model.Commentable;
import com.xhystc.v3ex.model.Votable;
import com.xhystc.v3ex.model.vo.page.CommentPage;

import java.util.List;

public interface CommentService
{
	void doComment(Comment comment);
	List<Comment> getQuestionComments(Long id, int page, int pageSize);
	CommentInform commentInform(String type, Long id);
	void fetchComment(Commentable commentable);
	void fetchComments( List<? extends Commentable> commentables);

}







