package com.xhystc.zhihu.model.vo.query;

import java.util.Set;

public class UserQueryCondition
{
	private Set<Long> ids;
	private String username;
	private String email;

	public UserQueryCondition(){}
	public UserQueryCondition(String username, String email)
	{
		this.username = username;
		this.email = email;
	}

	public String getUsername()
	{
		return username;
	}

	public void setUsername(String username)
	{
		this.username = username;
	}

	public String getEmail()
	{
		return email;
	}

	public void setEmail(String email)
	{
		this.email = email;
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
