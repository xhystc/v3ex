package com.xhystc.v3ex.service.impl;

import com.xhystc.v3ex.dao.CommentDao;
import com.xhystc.v3ex.dao.CommentInformDao;
import com.xhystc.v3ex.model.*;
import com.xhystc.v3ex.model.vo.query.CommentInformQueryCondition;
import com.xhystc.v3ex.model.vo.query.CommentQueryCondition;
import com.xhystc.v3ex.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

@Component
@Transactional(rollbackFor = RuntimeException.class)
public class CommentServiceImpl implements CommentService
{
	private CommentDao commentDao;
	private CommentInformDao commentInformDao;



	@Autowired
	public CommentServiceImpl(CommentDao commentDao, CommentInformDao commentInformDao)
	{
		this.commentDao = commentDao;
		this.commentInformDao = commentInformDao;
	}



	@Override
	public void doComment(Comment comment)
	{
		commentDao.insertComment(comment);
		System.out.println(comment.getId());
		String commentInformId = CommentInform.commentInformId("question",comment.getQuestion().getId());
		CommentInform commentInform = new CommentInform();
		commentInform.setId(commentInformId);
		commentInform.setLastCommentTime(new Date());
		commentInform.setLastCommentUser(comment.getUser());
		commentInform.setCommentCount(1);
		if(commentInformDao.increaseCommentInform(commentInform)<=0){
			commentInformDao.insertCommentInform(commentInform);
		}

	}

	@Override
	public List<Comment> getQuestionComments(Long id, int page, int pageSize)
	{
		CommentQueryCondition commentQueryCondition = new CommentQueryCondition();
		commentQueryCondition.setOffset((page-1)*pageSize);
		commentQueryCondition.setRows(pageSize);
		commentQueryCondition.setParentId(id);

		List<Comment> comments = commentDao.selectComments(commentQueryCondition);



		return comments;
	}

	@Override
	public CommentInform commentInform(String type, Long id)
	{
		CommentInform commentInform = commentInformDao.getCommentInformById(CommentInform.commentInformId(type,id));
		if (commentInform==null){
			commentInform = new CommentInform();
			commentInform.setId(CommentInform.commentInformId(type,id));
		}
		return commentInform;
	}

	@Override
	public void fetchComment(Commentable commentable)
	{
		CommentInform commentInform  = commentInformDao.getCommentInformById(CommentInform.commentInformId(commentable.type(),commentable.id()));
		if (commentInform == null){
			commentInform = new CommentInform();
			commentInform.setId(CommentInform.commentInformId(commentable.type(),commentable.id()));
		}
		commentable.setCommentInform(commentInform);
	}

	@Override
	public void fetchComments(List<? extends Commentable> commentables)
	{
		CommentInformQueryCondition condition = new CommentInformQueryCondition();
		condition.setInclude(new HashSet<>());
		for(Commentable commentable : commentables){
			condition.getInclude().add(CommentInform.commentInformId(commentable.type(),commentable.id()));
		}
		List<CommentInform> commentInforms = commentInformDao.selectCommentInform(condition);
		for(Commentable commentable : commentables){
			if(commentable.getCommentInform()==null)
			{
				boolean find = false;
				Iterator<CommentInform> iterator = commentInforms.iterator();
				while (iterator.hasNext()){
					CommentInform commentInform = iterator.next();
					String id = VoteInform.voteInformId(commentable.type(),commentable.id());
					if(commentInform.getId().equals(id)){
						commentable.setCommentInform(commentInform);
						iterator.remove();
						find = true;
						break;
					}
				}
				if (!find){
					CommentInform commentInform = new CommentInform();
					commentInform.setId(CommentInform.commentInformId(commentable.type(),commentable.id()));
					commentable.setCommentInform(commentInform);
				}
			}
		}
	}

}







