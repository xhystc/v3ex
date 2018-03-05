package com.xhystc.v3ex.service;

import com.xhystc.v3ex.model.*;

import java.util.List;

public interface CommentService
{
	void doComment(Comment comment);
	List<Comment> getComments(EntityType type,Long id, int page, int pageSize);
	CommentInform commentInform(EntityType type, Long id);
	void fetchComment(Commentable commentable);
	void fetchComments( List<? extends Commentable> commentables);
	Comment getCommentById(Long id);
}







