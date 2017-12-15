package com.xhystc.v3ex.model.vo.form;

import com.xhystc.v3ex.commons.Markdown;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;

public class QuestionCommentForm
{

	@Length(min = 1,max = 20000,message = "正文长度应大于1小于2000")
	@NotBlank(message = "正文不能为空")
	@Markdown
	private String content;

	@NotNull
	private Long questionId;

	public String getContent()
	{
		return content;
	}

	public void setContent(String content)
	{
		this.content = content;
	}

	public Long getQuestionId()
	{
		return questionId;
	}

	public void setQuestionId(Long questionId)
	{
		this.questionId = questionId;
	}
}
