package com.xhystc.v3ex.model;

public interface Commentable
{
	EntityType type();
	Long id();
	CommentInform getCommentInform();
	void setCommentInform(CommentInform commentInform);
}
