package com.xhystc.v3ex.model.vo.form;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

public class UserRegistForm
{
	@Length(min = 3,max = 28,message = "用户名长度应大于3小于28")
	@NotBlank(message = "用户名不能为空")
	String username;

	@Length(min = 6,max = 28,message = "密码长度应大于6小于28")
	@NotBlank(message = "密码不能为空")
	String password;

	@Email(message = "邮箱格式错误")
	@NotBlank(message = "邮箱不能为空")
	String email;

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

	public String getEmail()
	{
		return email;
	}

	public void setEmail(String email)
	{
		this.email = email;
	}
}
