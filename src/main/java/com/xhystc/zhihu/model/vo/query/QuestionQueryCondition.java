package com.xhystc.zhihu.model.vo.query;

import java.util.Date;
import java.util.Set;

public class QuestionQueryCondition
{
	private Set<Long> ids;
	private Long userId;
	private Date createDate;
	private String order;
	private String orderBy;
	private int offset;
	private int rows;
	private Set<Long> ruleOut;

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

	public void setRuleOut(Set<Long> ruleOut)
	{
		this.ruleOut = ruleOut;
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



