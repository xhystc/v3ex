package com.xhystc.v3ex.model;

public interface Commentable
{
	String type();
	Long id();
	CommentInform getCommentInform();
	void setCommentInform(CommentInform commentInform);
}
