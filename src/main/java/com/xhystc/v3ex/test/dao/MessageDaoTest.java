package com.xhystc.v3ex.test.dao;

import com.xhystc.v3ex.dao.MessageDao;
import com.xhystc.v3ex.dao.UserDao;
import com.xhystc.v3ex.model.Message;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RunWith(SpringJUnit4ClassRunner.class)  //使用junit4进行测试
@ContextConfiguration(locations = {"classpath:conf/applicationContext.xml", "classpath:conf/applicationContext-mybatis.xml"})
public class MessageDaoTest
{
	@Autowired
	MessageDao messageDao;

	@Autowired
	UserDao userDao;

	@Test
	@Transactional(rollbackFor = Exception.class)
	@Rollback(false)
	public void getMessageById() throws Exception
	{
		Message message = messageDao.getMessageById(1L);
		System.out.println(message.getContent());
		System.out.println(message.getFrom().getName()+"->" + message.getTo().getName());
	}

	@Test
	public void insertMessage() throws Exception
	{
		Message message = new Message();
		message.setContent("巴拉巴拉");
		message.setSendDate(new Date());
		message.setTo(userDao.getUserByName("test"));
		message.setFrom(userDao.getUserById(5L));
		messageDao.insertMessage(message);
	}

	@Test
	@Transactional(rollbackFor = Exception.class)
	@Rollback(false)
	public void deleteMessageById() throws Exception
	{

	}

	@Test
	@Transactional(rollbackFor = Exception.class)
	@Rollback(false)
	public void updateMessage() throws Exception
	{
		Message message = messageDao.getMessageById(2L);
		message.setIsRead(true);
		messageDao.updateMessage(message);
	}

	@Test
	@Transactional(rollbackFor = Exception.class)
	@Rollback(false)
	public void selectMessage() throws Exception
	{
		Map<String,Object> condition = new HashMap<>(2);
		condition.put("onversation","4_5");
		List<Message> messages = messageDao.selectMessages(condition);
		for(Message message : messages){
			System.out.println(message.getFrom().getName()+"->"+message.getTo().getName()+" "+message.getContent()+" "+message.getSendDate());
		}
	}

	@Test
	@Transactional(rollbackFor = Exception.class)
	@Rollback(false)
	public void countUnreaded(){
		System.out.println(messageDao.countUnreaded(4L));
	}

	@Test
	@Transactional(rollbackFor = Exception.class)
	@Rollback(false)
	public void tranTest(){
		Message message = new Message();
		message.setContent("???");
		message.setTo(userDao.getUserById(4L));
		message.setFrom(userDao.getUserById(5L));
		messageDao.insertMessage(message);

		message.setContent("xxxx");
		message.setTo(userDao.getUserById(5L));
		message.setFrom(userDao.getUserById(4L));
		messageDao.insertMessage(message);
	}
}






