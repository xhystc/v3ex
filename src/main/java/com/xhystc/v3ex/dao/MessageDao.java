package com.xhystc.v3ex.dao;

import com.xhystc.v3ex.model.Message;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public interface MessageDao
{
	Message getMessageById(Long id);
	int insertMessage(Message message);
	int deleteMessageById(Long id);
	int updateMessage(Message message);
	List<Message> selectMessages(Map<String,Object> condition);
	int countUnreaded(Long userId);
	int readAll(Long userId,String conversationId);
}
