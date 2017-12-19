package com.xhystc.v3ex.dao;

import com.xhystc.v3ex.model.Conversation;

import java.util.List;

public interface ConversationDao
{
	int insertConversation(Conversation conversation);
	int createUserConversation(Long id1,Long id2,String conversationId);
	int updateConversation(Conversation conversation);
	List<Conversation> selectUserConversation(Long userId);
}







