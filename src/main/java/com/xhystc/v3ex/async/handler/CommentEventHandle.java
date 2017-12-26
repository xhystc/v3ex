package com.xhystc.v3ex.async.handler;

import com.xhystc.v3ex.async.Event;
import com.xhystc.v3ex.async.EventHandler;
import com.xhystc.v3ex.async.EventType;
import com.xhystc.v3ex.service.QuestionService;

public class CommentEventHandle implements EventHandler
{
	private QuestionService questionService;


	@Override
	public void handle(Event event)
	{
		if(event.getEventType().equals(EventType.PUBLISH_COMMENT_EVENT)){
		/*	questionService.upQuestion();*/
		}
	}
}
