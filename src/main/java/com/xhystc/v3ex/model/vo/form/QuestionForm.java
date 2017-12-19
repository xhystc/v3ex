package com.xhystc.v3ex.model.vo.form;

import com.xhystc.v3ex.commons.Markdown;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class QuestionForm
{
	@Length(min = 1,max = 120,message = "标题长度应大于1小于120")
	@NotBlank(message = "标题不能为空")
	private String title;

	@Markdown
	@Length(max = 20000,message = "标题长度应1小于20000")
	private String content;

	@NotNull(message = "话题不能为空")
	private Long tag;

	public String getTitle()
	{
		return title;
	}

	public void setTitle(String title)
	{
		this.title = title;
	}

	public String getContent()
	{
		return content;
	}

	public void setContent(String content)
	{
		this.content = content;
	}

	public Long getTag()
	{
		return tag;
	}

	public void setTag(Long tag)
	{
		this.tag = tag;
	}
}
