package com.xhystc.v3ex.model.vo.query;

import com.xhystc.v3ex.model.EntityType;

import java.util.Set;

public class VoteQueryCondition extends QueryCondition
{
	private Long userId;
	private EntityType parentType;
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

	public EntityType getParentType()
	{
		return parentType;
	}

	public void setParentType(EntityType parentType)
	{
		this.parentType = parentType;
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
