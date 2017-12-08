package com.xhystc.zhihu.model.vo.query;

import java.util.Date;
import java.util.Set;

public class MessageQueryCondition
{
	private Set<Long> ids;
	private Long fromId;
	private Long toId;
	private Boolean isRead;
	private Date sendDate;

	public Boolean getIsRead()
	{
		return isRead;
	}

	public void setIsRead(Boolean read)
	{
		isRead = read;
	}

	public Date getSendDate()
	{
		return sendDate;
	}

	public void setSendDate(Date sendDate)
	{
		this.sendDate = sendDate;
	}

	public Long getFromId()
	{
		return fromId;
	}

	public void setFromId(Long fromId)
	{
		this.fromId = fromId;
	}

	public Long getToId()
	{
		return toId;
	}

	public void setToId(Long toId)
	{
		this.toId = toId;
	}

	public Set<Long> getIds()
	{
		return ids;
	}

	public void setIds(Set<Long> ids)
	{
		this.ids = ids;
	}
}
