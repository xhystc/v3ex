package com.xhystc.zhihu.model.vo.form;

import com.xhystc.zhihu.commons.Markdown;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

public class QuestionForm
{
	@Length(min = 1,max = 120,message = "标题长度应大于1小于120")
	@NotBlank(message = "标题不能为空")
	private String title;

	@Markdown
	@Length(max = 20000,message = "标题长度应1小于20000")
	private String content;

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
}
