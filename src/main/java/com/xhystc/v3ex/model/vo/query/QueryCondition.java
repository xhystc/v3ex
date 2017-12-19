package com.xhystc.v3ex.model.vo.query;

public class QueryCondition
{
	private String order;
	private String orderBy;
	private int offset;
	private int rows;
	private boolean needOrder = true;
	private boolean needLimit = true;

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
