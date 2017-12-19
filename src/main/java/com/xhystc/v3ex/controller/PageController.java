package com.xhystc.v3ex.controller;


import com.xhystc.v3ex.commons.FormUtils;
import com.xhystc.v3ex.dao.ConversationDao;
import com.xhystc.v3ex.dao.MessageDao;
import com.xhystc.v3ex.model.Conversation;
import com.xhystc.v3ex.model.Message;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
public class PageController
{
	private static Logger logger = Logger.getLogger(PageController.class);
	@Autowired
	private MessageDao messageDao;
	@Autowired
	private ConversationDao conversationDao;

	@RequestMapping("/question_comment")
	public String questionComment(){
		return "question_comment";
	}
	@RequestMapping("/scroll_test")
	public String scrollTest(){
		return "scroll_test";
	}


	public static Logger getLogger()
	{
		return logger;
	}

	public static void setLogger(Logger logger)
	{
		PageController.logger = logger;
	}

	public MessageDao getMessageDao()
	{
		return messageDao;
	}

	public void setMessageDao(MessageDao messageDao)
	{
		this.messageDao = messageDao;
	}
}

