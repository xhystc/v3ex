package com.xhystc.zhihu.dao;

import com.xhystc.zhihu.model.Message;
import com.xhystc.zhihu.model.vo.query.MessageQueryCondition;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface MessageDao
{
	Message getMessageById(Long id);
	int insertMessage(Message message);
	int deleteMessageById(Long id);
	int updateMessage(Message message);
	List<Message> selectMessages(MessageQueryCondition condition);
	int countUnreaded(Long userId);
	int readAll(Long userId);
}
