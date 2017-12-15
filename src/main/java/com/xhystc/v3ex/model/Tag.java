package com.xhystc.v3ex.model;

public class Tag
{
	private Long id;
	private String name;
	private int questionCount;
	private String description;
	private String icon;

	public Long getId()
	{
		return id;
	}

	public void setId(Long id)
	{
		this.id = id;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public int getQuestionCount()
	{
		return questionCount;
	}

	public void setQuestionCount(int questionCount)
	{
		this.questionCount = questionCount;
	}

	public String getDescription()
	{
		return description;
	}

	public void setDescription(String description)
	{
		this.description = description;
	}

	public String getIcon()
	{
		return icon;
	}

	public void setIcon(String icon)
	{
		this.icon = icon;
	}
}
