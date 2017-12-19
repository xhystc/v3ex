package com.xhystc.v3ex.model.vo.query;

import java.util.Date;
import java.util.Set;

public class QuestionQueryCondition extends QueryCondition
{
	private Long userId;
	private Date createDate;

	private Long tagId;
	private Set<Long> include;


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


}



