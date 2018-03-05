package com.xhystc.v3ex.dao;

import com.xhystc.v3ex.model.Comment;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public interface CommentDao
{
	Comment getCommentById(Long id);

	int deleteCommentById(Long id);

	int updateComment(Comment comment);

	int insertComment(Comment comment);

	List<Comment> selectComments(Map<String,Object> condition);
}



