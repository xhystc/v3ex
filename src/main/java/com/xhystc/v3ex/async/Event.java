package com.xhystc.v3ex.async;

import java.util.HashMap;
import java.util.Map;

public class Event
{
	private EventType eventType;
	private Long sourceId;
	private String entityType;
	private Long entityId;

	private Map<String,String> ext = new HashMap<>();

	public Event()
	{
	}

	public Event(EventType eventType, Long sourceId, String entityType, Long entityId)
	{
		this.eventType = eventType;
		this.sourceId = sourceId;
		this.entityType = entityType;
		this.entityId = entityId;
	}

	public void addExt(String key,String value){
		ext.put(key,value);
	}
	public String getExt(String key){
		return ext.get(key);
	}

	public EventType getEventType()
	{
		return eventType;
	}

	public void setEventType(EventType eventType)
	{
		this.eventType = eventType;
	}

	public Long getSourceId()
	{
		return sourceId;
	}

	public void setSourceId(Long sourceId)
	{
		this.sourceId = sourceId;
	}

	public String getEntityType()
	{
		return entityType;
	}

	public void setEntityType(String entityType)
	{
		this.entityType = entityType;
	}

	public Long getEntityId()
	{
		return entityId;
	}

	public void setEntityId(Long entityId)
	{
		this.entityId = entityId;
	}
}



