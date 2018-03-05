package com.xhystc.v3ex.model.vo;

public class Problem
{
	private String key;
	private String value;
	private String reason;

	public Problem(){}
	public Problem(String key,String value,String reason){
		this.key = key;
		this.value = value;
		this.reason = reason;
	}

	public String getValue()
	{
		return value;
	}

	public void setValue(String value)
	{
		this.value = value;
	}

	public String getReason()
	{
		return reason;
	}

	public void setReason(String reason)
	{
		this.reason = reason;
	}

	public String getKey()
	{
		return key;
	}

	public void setKey(String key)
	{
		this.key = key;
	}

	@Override
	public boolean equals(Object o)
	{
		if (this == o)
		{
			return true;
		}
		if (o.getClass()!=this.getClass())
		{
			return false;
		}

		Problem problem = (Problem) o;

		return getKey().equals(problem.getKey());
	}

	@Override
	public int hashCode()
	{
		return getKey().hashCode();
	}
}
