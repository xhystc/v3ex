package com.xhystc.v3ex.commons;

import com.xhystc.v3ex.async.handler.QuestionRankUpdateHandler;
import com.xhystc.v3ex.dao.QuestionDao;
import com.xhystc.v3ex.dao.solr.SolrDao;
import com.xhystc.v3ex.model.Comment;
import com.xhystc.v3ex.model.EntityType;
import com.xhystc.v3ex.model.Question;
import com.xhystc.v3ex.service.CommentService;
import com.xhystc.v3ex.service.VoteService;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Pipeline;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class QuestionRankUtils
{

	public static void storeRedisQuestionRank(QuestionDao questionDao, QuestionRankUpdateHandler handler){
		Map<String,Object> condition = new HashMap<>(5);
		for(int i=0;;i++){
			condition.put("offset",i*5000);
			condition.put("rows",5000);
			List<Question> questions = questionDao.selectQuestions(condition);
			if(questions.size()==0){
				break;
			}
			for(Question question : questions){
				handler.doUpdate(question);
			}
		}
	}

	public static void storeSolrQuestions(QuestionDao questionDao, SolrDao solrDao, CommentService commentService, VoteService voteService){
		Map<String,Object> condition = new HashMap<>(5);
		for(int i=0;;i++){
			condition.put("offset",i*5000);
			condition.put("rows",5000);
			List<Question> questions = questionDao.selectQuestions(condition);
			if(questions.size()==0){
				break;
			}
			for(Question question : questions){
				long time = question.getCreateDate().getTime()/1000;
				time/=3600;
				time/=24;

				solrDao.addQuestion(question,questionRankScore(question,commentService,voteService)+time);
				System.out.println("store:"+question.getId()+" title:"+question.getTitle());
			}
		}
	}

	public static double questionRankScore(Question question, CommentService commentService, VoteService voteService){
		double sum = 1;
		sum+=question.getVoteCount();
		List<Comment> comments = commentService.getComments(EntityType.QUESTION,question.getId(),0,-1);
		commentService.fetchComment(question);
		sum+=question.getCommentInform().getCommentCount();
		voteService.fetchUserVotes(null,comments);
		voteService.fetchUserVote(null,question);
		for(Comment comment : comments){
			sum+=comment.getVoteCount();
		}
		sum+=question.getVoteCount();
		return sum;
	}
	public static double currentQuestionRankScore(Long questionId,Jedis redis){
		return redis.zscore(RedisUtils.questionActiveKey(),String.valueOf(questionId));
	}
}
