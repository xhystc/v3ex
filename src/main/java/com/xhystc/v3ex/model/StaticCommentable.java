package com.xhystc.v3ex.model;

public class StaticCommentable implements Commentable
{
	private EntityType type;
	private Long id;
	private CommentInform commentInform;

	public StaticCommentable(EntityType type, Long id){
		this.type = type;
		this.id = id;
	}

	@Override
	public EntityType type()
	{
		return type;
	}

	@Override
	public Long id()
	{
		return id;
	}

	@Override
	public CommentInform getCommentInform()
	{
		return commentInform;
	}

	@Override
	public void setCommentInform(CommentInform commentInform)
	{
		this.commentInform = commentInform;
	}

	public EntityType getType()
	{
		return type;
	}

	public void setType(EntityType type)
	{
		this.type = type;
	}

	public Long getId()
	{
		return id;
	}

	public void setId(Long id)
	{
		this.id = id;
	}
}
