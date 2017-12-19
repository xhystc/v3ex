package com.xhystc.v3ex.model.vo.form;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

public class MessageForm
{
	@NotBlank(message = "对方不能为空")
	private String to;

	@Length(max = 200,message = "正文长度过长")
	@NotBlank(message = "正文不能为空")
	private String content;

	public String getTo()
	{
		return to;
	}

	public void setTo(String to)
	{
		this.to = to;
	}

	public String getContent()
	{
		return content;
	}

	public void setContent(String content)
	{
		this.content = content;
	}
}




