package com.xhystc.v3ex.model.vo.form;

import org.hibernate.validator.constraints.NotBlank;

public class LoginForm
{
	@NotBlank(message = "用户名不能为空")
	private String username;

	@NotBlank(message = "密码不能为空")
	private String password;
	private String remeberMe;

	public String getUsername()
	{
		return username;
	}

	public void setUsername(String username)
	{
		this.username = username;
	}

	public String getPassword()
	{
		return password;
	}

	public void setPassword(String password)
	{
		this.password = password;
	}

	public String getRemeberMe()
	{
		return remeberMe;
	}

	public void setRemeberMe(String remeberMe)
	{
		this.remeberMe = remeberMe;
	}
}


