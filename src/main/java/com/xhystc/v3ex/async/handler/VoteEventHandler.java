package com.xhystc.v3ex.async.handler;

import com.xhystc.v3ex.async.Event;
import com.xhystc.v3ex.async.EventHandler;
import com.xhystc.v3ex.async.EventType;
import com.xhystc.v3ex.commons.CommonUtils;
import com.xhystc.v3ex.commons.QuestionRankUtils;
import com.xhystc.v3ex.model.Comment;
import com.xhystc.v3ex.model.EntityType;
import com.xhystc.v3ex.model.Question;
import com.xhystc.v3ex.model.User;
import com.xhystc.v3ex.service.*;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

@Component("voteEventHandler")
public class VoteEventHandler implements EventHandler
{
	private static final Logger logger = Logger.getLogger(VoteEventHandler.class);

	private UserService userService;
	private CommentService commentService;
	private QuestionService questionService;
	private MessageService messageService;
	private HotTopicService hotTopicService;

	@Autowired
	public VoteEventHandler(UserService userService, CommentService commentService,
	                        QuestionService questionService,MessageService messageService,
	                        HotTopicService hotTopicService,JedisPool jedisPool)
	{
		this.userService = userService;
		this.commentService = commentService;
		this.questionService = questionService;
		this.messageService = messageService;
		this.hotTopicService = hotTopicService;
	}

	@Override
	public void handle(Event event)
	{
		if(event.getEventType().equals(EventType.VOTE_EVENT)){
			logger.debug("handle event:"+event.getEventType()+" "+event.getEntityType()+":"+event.getEntityId());
			User source = userService.getUserById(event.getSourceId());
			User system = userService.getUserById(4L);

			if(EntityType.QUESTION.equals(event.getEntityType())){

				Question question = questionService.getQuestionById(event.getEntityId());
				String userLink = String.format(CommonUtils.LINK_TEMPLATE,"/user/"+source.getId(),source.getName());
				String questionLink = String.format(CommonUtils.LINK_TEMPLATE,"/q/"+question.getId(),question.getTitle());
				String content = userLink+" 给你的提问"+questionLink+" 点了赞";
				messageService.publishMessage(system,question.getUser(),content);
				hotTopicService.incScore(question.getId(),1);


			}else if(EntityType.COMMENT.equals(event.getEntityType())){

				Comment comment = commentService.getCommentById(event.getEntityId());
				String userLink = String.format(CommonUtils.LINK_TEMPLATE,"/user/"+source.getId(),source.getName());
				String questionLink = String.format(CommonUtils.LINK_TEMPLATE,"/q/"+comment.getParentId(),comment.getContent());
				String content = userLink+" 给你的回复："+questionLink+"点了赞";
				messageService.publishMessage(system,comment.getUser(),content);


			}else {
				logger.debug("??????????");
			}

		}
	}
}







