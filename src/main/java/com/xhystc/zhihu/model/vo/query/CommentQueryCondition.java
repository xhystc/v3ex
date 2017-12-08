package com.xhystc.zhihu.model.vo.query;

import java.util.Set;

public class CommentQueryCondition
{
	private Set<Long> ids;
	private Long userId;
	private String parentType;
	private Long parentId;
	private int offset;
	private int rows;
	private String order;
	private String orderBy;

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
	public int getOffset()
	{
		return offset;
	}

	public void setOffset(int offset)
	{
		this.offset = offset;
	}

	public int getRows()
	{
		return rows;
	}

	public void setRows(int rows)
	{
		this.rows = rows;
	}

	public String getOrder()
	{
		return order;
	}

	public void setOrder(String order)
	{
		this.order = order;
	}

	public String getOrderBy()
	{
		return orderBy;
	}

	public void setOrderBy(String orderBy)
	{
		this.orderBy = orderBy;
	}

	public Set<Long> getIds()
	{
		return ids;
	}

	public void setIds(Set<Long> ids)
	{
		this.ids = ids;
	}
}





