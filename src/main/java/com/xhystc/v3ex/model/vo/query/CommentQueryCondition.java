package com.xhystc.v3ex.model.vo.query;

import java.util.Set;

public class CommentQueryCondition  extends QueryCondition
{
	private Long userId;
	private Long parentId;
	private Set<Long> include;

	public Long getUserId()
	{
		return userId;
	}

	public void setUserId(Long userId)
	{
		this.userId = userId;
	}

	public Long getParentId()
	{
		return parentId;
	}

	public void setParentId(Long parentId)
	{
		this.parentId = parentId;
	}

	public Set<Long> getInclude()
	{
		return include;
	}

	public void setInclude(Set<Long> include)
	{
		this.include = include;
	}
}





