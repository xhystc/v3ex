package com.xhystc.v3ex.model;

import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.Date;

public class Conversation
{
	private String id;
	private Date lastActiveTime;
	private Message lastMessage;

	public static String conversationKey(Long id1,Long id2){
		return Math.min(id1,id2)+"_"+Math.max(id1,id2);
	}
	public static String[] divIds(String id){
		if(id == null){
			return null;
		}
		String[] res = id.split("_");
		if(res.length != 2 || !StringUtils.isNumeric(res[0]) || !StringUtils.isNumeric(res[1])){
			return null;
		}
		return res;
	}

	public String getId()
	{
		return id;
	}

	public void setId(String id)
	{
		this.id = id;
	}

	public Date getLastActiveTime()
	{
		return lastActiveTime;
	}

	public void setLastActiveTime(Date lastActiveTime)
	{
		this.lastActiveTime = lastActiveTime;
	}

	public Message getLastMessage()
	{
		return lastMessage;
	}

	public void setLastMessage(Message lastMessage)
	{
		this.lastMessage = lastMessage;
	}
}



