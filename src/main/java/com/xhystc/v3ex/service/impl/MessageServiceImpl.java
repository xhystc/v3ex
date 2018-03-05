package com.xhystc.v3ex.service.impl;

import com.xhystc.v3ex.dao.ConversationDao;
import com.xhystc.v3ex.dao.MessageDao;
import com.xhystc.v3ex.model.Conversation;
import com.xhystc.v3ex.model.Message;
import com.xhystc.v3ex.model.User;
import com.xhystc.v3ex.model.vo.Problem;
import com.xhystc.v3ex.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.*;

@Component
public class MessageServiceImpl implements MessageService
{
	private static final String UNREADPREFIX = "message-unread-";
	private ConversationDao conversationDao;
	private MessageDao messageDao;
	private JedisPool jedisPool;

	@Autowired
	public MessageServiceImpl(ConversationDao conversationDao, MessageDao messageDao,JedisPool jedisPool)
	{
		this.conversationDao = conversationDao;
		this.messageDao = messageDao;
		this.jedisPool = jedisPool;
	}

	@Transactional(rollbackFor = RuntimeException.class,isolation = Isolation.READ_COMMITTED)
	@Override
	public List<Problem> publishMessage(User from, User to, String content)
	{
		Message message = new Message();
		message.setFrom(from);
		message.setTo(to);
		message.setContent(content);
		message.setIsRead(false);
		message.setSendDate(new Date());
		message.setConversationId(Conversation.conversationKey(from.getId(),to.getId()));

		messageDao.insertMessage(message);
		Conversation conversation = new Conversation();
		conversation.setLastActiveTime(message.getSendDate());
		conversation.setLastMessage(message);
		conversation.setId(Conversation.conversationKey(from.getId(),to.getId()));
		if(conversationDao.updateConversation(conversation)<=0){
			conversationDao.insertConversation(conversation);
			conversationDao.createUserConversation(from.getId(),to.getId(),conversation.getId());
		}
		try(Jedis redis = jedisPool.getResource())
		{
			redis.incr(unreadKey(to.getId()));
		}

		return Collections.emptyList();
	}

	@Override
	public List<Conversation> getConversations(Long userId)
	{
		try(Jedis redis = jedisPool.getResource())
		{
			redis.del(unreadKey(userId));
		}
		return conversationDao.selectUserConversation(userId);
	}

	@Override
	public List<Message> getMessages(Long userId,String conversationId)
	{
		Map<String,Object> condition = new HashMap<>(2);
		condition.put("conversation",conversationId);
		List<Message> messages = messageDao.selectMessages(condition);
		messageDao.readAll(userId,conversationId);
		return messages;
	}

	@Override
	public int unread(Long userId)
	{
		if (userId==null)
		{
			return 0;
		}
		try(Jedis redis = jedisPool.getResource())
		{
			String s = redis.get(unreadKey(userId));
			return s==null?0:Integer.parseInt(s);
		}
	}

	private static String unreadKey(Long userId){
		return UNREADPREFIX+userId.toString();
	}
}







