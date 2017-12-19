package com.xhystc.v3ex.test.dao;

import com.xhystc.v3ex.dao.ConversationDao;
import com.xhystc.v3ex.dao.MessageDao;
import com.xhystc.v3ex.dao.UserDao;
import com.xhystc.v3ex.model.Conversation;
import com.xhystc.v3ex.model.Message;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)  //使用junit4进行测试
@ContextConfiguration(locations = {"classpath:conf/applicationContext.xml", "classpath:conf/applicationContext-mybatis.xml"})
public class ConversationDaoTest
{
	@Autowired
	ConversationDao conversationDao;
	@Autowired
	MessageDao messageDao;
	@Autowired
	UserDao userDao;

	@Transactional(rollbackFor = Exception.class)
	@Rollback(false)
	@Test
	public void insertAndCreate(){
		Conversation conversation = new Conversation();
		conversation.setId(Conversation.conversationKey(4L,5L));
		conversation.setLastActiveTime(new Date());
		conversation.setLastMessage(messageDao.getMessageById(5L));
		conversationDao.insertConversation(conversation);
		conversationDao.createUserConversation(4L,5L,conversation.getId());
	}

	@Transactional(rollbackFor = Exception.class)
	@Rollback(false)
	@Test
	public void updateConversation() throws ParseException
	{
		Conversation conversation = new Conversation();
		conversation.setId(Conversation.conversationKey(4L,5L));
		conversation.setLastMessage(messageDao.getMessageById(5L));
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		conversation.setLastActiveTime(format.parse("2017-10-15 17:06:36"));
		conversationDao.updateConversation(conversation);
	}
	@Transactional(rollbackFor = Exception.class)
	@Rollback(false)
	@Test
	public void selectUserConversation(){
		List<Conversation> conversations = conversationDao.selectUserConversation(4L);
		for(Conversation conversation : conversations){
			System.out.println(conversation.getLastMessage().getContent());
		}
	}
}











