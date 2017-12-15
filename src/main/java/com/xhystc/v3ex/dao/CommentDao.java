package com.xhystc.v3ex.dao;

import com.xhystc.v3ex.model.Comment;
import com.xhystc.v3ex.model.vo.query.CommentQueryCondition;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface CommentDao
{
	Comment getCommentById(Long id);

	int deleteCommentById(Long id);

	int updateComment(Comment comment);

	int insertComment(Comment comment);

	List<Comment> selectComments(CommentQueryCondition condition);
}



