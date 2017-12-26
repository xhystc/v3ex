package com.xhystc.v3ex.service;

import com.xhystc.v3ex.model.*;
import com.xhystc.v3ex.model.vo.page.CommentPage;

import java.util.List;

public interface CommentService
{
	void doComment(Comment comment);
	List<Comment> getQuestionComments(Long id, int page, int pageSize);
	CommentInform commentInform(EntityType type, Long id);
	void fetchComment(Commentable commentable);
	void fetchComments( List<? extends Commentable> commentables);
	Comment getCommentById(Long id);
}







