package com.xhystc.v3ex.model;

public class Vote
{
	private Long userId;
	private String parentType;
	private Long parentId;

	public Vote(){}

	public Vote(Long userId,String parentType,Long parentId){
		this.userId = userId;
		this.parentType = parentType;
		this.parentId = parentId;
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

	public Long getUserId()
	{
		return userId;
	}

	public void setUserId(Long userId)
	{
		this.userId = userId;
	}

	@Override
	public boolean equals(Object o)
	{
		if (this == o) return true;
		if (!(o instanceof Vote)) return false;

		Vote vote = (Vote) o;

		if (!getUserId().equals(vote.getUserId())) return false;
		if (!getParentType().equals(vote.getParentType())) return false;
		return getParentId().equals(vote.getParentId());
	}

	@Override
	public int hashCode()
	{
		int result = getUserId().hashCode();
		result = 31 * result + getParentType().hashCode();
		result = 31 * result + getParentId().hashCode();
		return result;
	}
}







