package com.xhystc.v3ex.async.handler;

import com.xhystc.v3ex.async.Event;
import com.xhystc.v3ex.async.EventHandler;
import com.xhystc.v3ex.async.EventType;
import com.xhystc.v3ex.commons.QuestionRankUtils;
import com.xhystc.v3ex.commons.RedisUtils;
import com.xhystc.v3ex.model.Comment;
import com.xhystc.v3ex.model.EntityType;
import com.xhystc.v3ex.model.Question;
import com.xhystc.v3ex.service.CommentService;
import com.xhystc.v3ex.service.QuestionService;
import com.xhystc.v3ex.service.VoteService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.Pipeline;

import java.util.List;

@Component
public class QuestionRankUpdateHandler implements EventHandler
{

	private static final Logger logger = Logger.getLogger(QuestionRankUpdateHandler.class);

	private final VoteService voteService;
	private final QuestionService questionService;
	private final CommentService commentService;
	private final JedisPool jedisPool;

	@Autowired
	public QuestionRankUpdateHandler(VoteService voteService, QuestionService questionService, CommentService commentService, JedisPool jedisPool)
	{
		this.voteService = voteService;
		this.questionService = questionService;
		this.commentService = commentService;
		this.jedisPool = jedisPool;
	}

	@Override
	public void handle(Event event)
	{
		if(EventType.QUESTION_RANK_UPDATE.equals(event.getEventType()) || EventType.PUBLISH_QUESTION.equals(event.getEventType())){
			logger.debug("handle update event");
			assert event.getEntityType().equals(EntityType.QUESTION);
			Question question;
			question = questionService.getQuestionById(event.getEntityId());
			if(question==null){
				return;
			}
			doUpdate(question);
		}
	}
	public void doUpdate(Question question){

		try(Jedis redis = jedisPool.getResource())
		{
			Double score;
			score = QuestionRankUtils.questionRankScore(question,commentService,voteService);
			long time = question.getCreateDate().getTime()/1000;
			time/=3600;
			time/=24;
			score = time+score;
			Pipeline pipeline = redis.pipelined();
			pipeline.zadd(RedisUtils.questionActiveKey(question.getTag().getId()),score,question.getId().toString());
			pipeline.zadd(RedisUtils.questionActiveKey(),score,question.getId().toString());
			pipeline.sync();
		}
	}

}
