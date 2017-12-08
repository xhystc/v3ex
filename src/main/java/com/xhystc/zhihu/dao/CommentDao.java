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
	int increaseAgree(Long id,int i);
	int setAgree(Long id,int i);
	List<Comment> selectComments(CommentQueryCondition condition);
	int increaseComment(Long id);}



