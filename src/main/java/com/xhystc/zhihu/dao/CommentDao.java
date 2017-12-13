package com.xhystc.zhihu.dao;

import com.xhystc.zhihu.model.Comment;
import com.xhystc.zhihu.model.vo.query.CommentQueryCondition;
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



