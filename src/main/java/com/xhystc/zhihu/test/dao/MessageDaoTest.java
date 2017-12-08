package com.xhystc.zhihu.test.dao;

import com.xhystc.zhihu.dao.MessageDao;
import com.xhystc.zhihu.dao.UserDao;
import com.xhystc.zhihu.model.Message;
import com.xhystc.zhihu.model.vo.query.MessageQueryCondition;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)  //使用junit4进行测试
@ContextConfiguration(locations = {"classpath:applicationContext.xml", "classpath:applicationContext-mybatis.xml"})
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
		MessageQueryCondition condition = new MessageQueryCondition();
		condition.setFromId(5L);
		condition.setIsRead(true);
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

}





