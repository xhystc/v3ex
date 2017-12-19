package com.xhystc.v3ex.model;

import java.io.Serializable;
import java.util.Date;

public class Message implements Serializable
{
	private Long id;
	private User from;
	private User to;
	private String content;
	private Date sendDate;
	private Boolean isRead;
	private String conversationId;

	public Long getId()
	{
		return id;
	}

	public void setId(Long id)
	{
		this.id = id;
	}

	public User getFrom()
	{
		return from;
	}

	public void setFrom(User from)
	{
		this.from = from;
	}


	public User getTo()

	{
		return to;
	}

	public void setTo(User to)
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

	public Date getSendDate()
	{
		return sendDate;
	}

	public void setSendDate(Date sendDate)
	{
		this.sendDate = sendDate;
	}

	public Boolean getIsRead()
	{
		return isRead;
	}

	public void setIsRead(Boolean read)
	{
		isRead = read;
	}

	public String getConversationId()
	{
		return conversationId;
	}

	public void setConversationId(String conversationId)
	{
		this.conversationId = conversationId;
	}
}
