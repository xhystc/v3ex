package com.xhystc.v3ex.service;

import com.xhystc.v3ex.model.Conversation;
import com.xhystc.v3ex.model.Message;
import com.xhystc.v3ex.model.User;
import com.xhystc.v3ex.model.vo.json.Problem;

import java.util.List;

public interface MessageService
{
	List<Problem> publishMessage(User from,User to,String content);
	List<Conversation> getConversations(Long userId);
	List<Message> getMessages(Long userId,String conversationId);
	int unread(Long userId);
}



