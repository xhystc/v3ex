package com.xhystc.zhihu.model.vo.query;

import java.util.Set;

public class VoteQueryCondition
{
	private Long userId;
	private String parentType;
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

	public String getParentType()
	{
		return parentType;
	}

	public void setParentType(String parentType)
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
