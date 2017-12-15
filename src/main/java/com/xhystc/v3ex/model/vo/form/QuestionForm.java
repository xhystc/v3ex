package com.xhystc.v3ex.model.vo.form;

import com.xhystc.v3ex.commons.Markdown;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Size;

public class QuestionForm
{
	@Length(min = 1,max = 120,message = "标题长度应大于1小于120")
	@NotBlank(message = "标题不能为空")
	private String title;

	@Markdown
	@Length(max = 20000,message = "标题长度应1小于20000")
	private String content;

	@Size(max = 5,message = "标签最多5个")
	private Long[] tags;

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

	public Long[] getTags()
	{
		return tags;
	}

	public void setTags(Long[] tags)
	{
		this.tags = tags;
	}
}
