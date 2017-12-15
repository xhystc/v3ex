package com.xhystc.v3ex.model.vo.query;

import java.util.Date;
import java.util.Set;

public class QuestionQueryCondition
{
	private Long userId;
	private Date createDate;
	private String order;
	private String orderBy;
	private int offset;
	private int rows;
	private Long tagId;
	private Set<Long> include;
	private boolean needOrder = true;
	private boolean needLimit = true;

	public Long getUserId()
	{
		return userId;
	}

	public void setUserId(Long userId)
	{
		this.userId = userId;
	}

	public void setCreateDate(Date createDate)
	{
		this.createDate = createDate;
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

	public Date getCreateDate()
	{
		return createDate;
	}


	public Set<Long> getInclude()
	{
		return include;
	}

	public void setInclude(Set<Long> include)
	{
		this.include = include;
	}

	public Long getTagId()
	{
		return tagId;
	}

	public void setTagId(Long tagId)
	{
		this.tagId = tagId;
	}

	public boolean getNeedOrder()
	{
		return needOrder;
	}

	public void setNeedOrder(boolean needOrder)
	{
		this.needOrder = needOrder;
	}

	public boolean getNeedLimit()
	{
		return needLimit;
	}

	public void setNeedLimit(boolean needLimit)
	{
		this.needLimit = needLimit;
	}
}



