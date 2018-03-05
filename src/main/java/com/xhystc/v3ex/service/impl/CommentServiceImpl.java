package com.xhystc.v3ex.service.impl;

import com.xhystc.v3ex.dao.CommentDao;
import com.xhystc.v3ex.dao.CommentInformDao;
import com.xhystc.v3ex.model.*;
import com.xhystc.v3ex.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Component
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


	@Transactional(rollbackFor = RuntimeException.class,isolation = Isolation.READ_COMMITTED)
	@Override
	public void doComment(Comment comment)
	{
		commentDao.insertComment(comment);
		CommentInform commentInform = new CommentInform();
		commentInform.setId(CommentInform.commentInformId(comment.getParentType(),comment.getParentId()));
		commentInform.setLastCommentUser(comment.getUser());
		commentInform.setCommentCount(1);
		commentInform.setLastCommentTime(new Date());
		if(commentInformDao.increaseCommentInform(commentInform)<1){
			commentInformDao.insertCommentInform(commentInform);
		}
	}

	@Override
	public List<Comment> getComments(EntityType type,Long id, int page, int pageSize)
	{
		Map<String,Object> commentQueryCondition = new HashMap<>(5);
		commentQueryCondition.put("parentType",type);
		if(pageSize>0){
			commentQueryCondition.put("offset",(page-1)*pageSize);
			commentQueryCondition.put("rows",pageSize);
		}
		commentQueryCondition.put("parentId",id);

		List<Comment> comments = commentDao.selectComments(commentQueryCondition);



		return comments;
	}

	@Override
	public CommentInform commentInform(EntityType type, Long id)
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
		Map<String,Object> condition = new HashMap<>(5);
		condition.put("include",new HashSet<String>());
		for(Commentable commentable : commentables){
			Collection<String> include = (Collection<String>) condition.get("include");
			include.add(CommentInform.commentInformId(commentable.type(),commentable.id()));
		}
		List<CommentInform> commentInforms = commentInformDao.selectCommentInform(condition);
		for(Commentable commentable : commentables){
			if(commentable.getCommentInform()==null)
			{
				boolean find = false;
				Iterator<CommentInform> iterator = commentInforms.iterator();
				while (iterator.hasNext()){
					CommentInform commentInform = iterator.next();
					String id = CommentInform.commentInformId(commentable.type(),commentable.id());
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

	@Override
	public Comment getCommentById(Long id)
	{
		return id==null?null:commentDao.getCommentById(id);
	}

}







