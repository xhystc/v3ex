package com.xhystc.v3ex.model;

import com.alibaba.fastjson.annotation.JSONField;

import java.io.Serializable;
import java.util.Date;

public class User implements Serializable
{

	private Long id;
	private String name="";
	@JSONField(serialize=false)
	private String password="";
	private Date registDate;
	@JSONField(serialize=false)
	private String email="";
	private String iconUrl="";
	@JSONField(serialize=false)
	private String salt="";
	@JSONField(serialize=false)
	private Boolean isLocked = false;

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	@JSONField(serialize=false)
	public String getCredentialsSalt(){
		return name+salt;
	}

	public Long getId()
	{
		return id;
	}

	public void setId(Long id)
	{
		this.id = id;
	}

	public String getPassword()
	{
		return password;
	}

	public void setPassword(String password)
	{
		this.password = password;
	}

	public Date getRegistDate()
	{
		return registDate;
	}

	public void setRegistDate(Date registDate)
	{
		this.registDate = registDate;
	}

	public String getEmail()
	{
		return email;
	}

	public void setEmail(String email)
	{
		this.email = email;
	}

	public String getSalt()
	{
		return salt;
	}

	public void setSalt(String salt)
	{
		this.salt = salt;
	}

	public Boolean getIsLocked()
	{
		return isLocked;
	}


	public void setIsLocked(Boolean isLocked)
	{
		this.isLocked = isLocked;
	}

	public String getIconUrl()
	{
		return iconUrl;
	}

	public void setIconUrl(String iconUrl)
	{
		this.iconUrl = iconUrl;
	}

	@Override
	public boolean equals(Object o)
	{
		if (this == o) return true;
		if (!(o instanceof User)) return false;

		User user = (User) o;

		if (getName() != null ? !getName().equals(user.getName()) : user.getName() != null) return false;
		if (getPassword() != null ? !getPassword().equals(user.getPassword()) : user.getPassword() != null)
			return false;
		return getEmail() != null ? getEmail().equals(user.getEmail()) : user.getEmail() == null;
	}

	@Override
	public int hashCode()
	{
		int result = getName() != null ? getName().hashCode() : 0;
		result = 31 * result + (getPassword() != null ? getPassword().hashCode() : 0);
		result = 31 * result + (getEmail() != null ? getEmail().hashCode() : 0);
		return result;
	}
}
