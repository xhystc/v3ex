package com.xhystc.v3ex.controller;

import com.xhystc.v3ex.async.Event;
import com.xhystc.v3ex.async.EventManager;
import com.xhystc.v3ex.async.EventType;
import com.xhystc.v3ex.commons.CommonUtils;
import com.xhystc.v3ex.model.*;
import com.xhystc.v3ex.model.vo.form.QuestionCommentForm;
import com.xhystc.v3ex.service.*;
import org.apache.log4j.Logger;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;


import javax.validation.Valid;
import java.util.*;

@Controller
public class CommentController
{
	private static final Logger logger = Logger.getLogger(CommentController.class);

	@Value("#{configProperties['page.pageSize']}")
	int pageSize;

	private CommentService commentService;
	private QuestionService questionService;
	private VoteService voteService;
	private HotTopicService hotTopicService;
	private UserService userService;
	private EventManager eventManager;

	@Autowired
	public CommentController(CommentService commentService, QuestionService questionService,
	                         HotTopicService hotTopicService,
	                         VoteService voteService,
	                         UserService userService,
	                         EventManager eventManager)
	{
		this.commentService = commentService;
		this.questionService = questionService;
		this.voteService = voteService;
		this.hotTopicService = hotTopicService;
		this.userService = userService;
		this.eventManager = eventManager;
	}

	@RequestMapping("/q/{questionId}")
	public String getQuestionComment(@PathVariable("questionId") Long questionId, Model model,
	                                 @RequestParam(defaultValue = "1") int page){

		Map<String,Object> commentPage = new HashMap<>(20);
		Question question = questionService.getQuestionById(questionId);
		if (question==null){
			return "redirect:/index";
		}
		commentPage.put("question",question);

		List<Comment> comments= commentService.getComments(EntityType.QUESTION,questionId,page,pageSize);

		commentPage.put("comments",comments);
		commentPage.put("tage",question.getTag());

		CommentInform commentInform = commentService.commentInform(question.type(),questionId);
		int lastPage = commentInform.getCommentCount()/pageSize+(commentInform.getCommentCount()%pageSize>0?1:0);

		User user = CommonUtils.getCurrentUser();
		Long userId = user==null?null:user.getId();

		voteService.fetchUserVote(userId, (Votable) commentPage.get("question"));
		voteService.fetchUserVotes(userId,comments);

		commentService.fetchComments(comments);
		commentService.fetchComment(question);
		question.setCommentInform(commentInform);

		model.addAttribute("commentPage",commentPage);
		model.addAttribute("pageButtons", CommonUtils.pageButtons(page,lastPage));
		model.addAttribute("currentPage",page);
		model.addAttribute("lastPage",lastPage);
		model.addAttribute("hotQuestion",hotTopicService.getHotTopics());

		return "question_comment";
	}

	@RequestMapping(value = "/q/{questionId}",headers = {"x-requested-with=XMLHttpRequest","Accept=application/json"})
	public @ResponseBody
	Map<String,Object> getQuestionCommentService(@PathVariable("questionId") Long questionId,
	                                             @RequestParam(defaultValue = "1") int page){
		Map<String,Object> ret = new HashMap<>(4);
		Question question = questionService.getQuestionById(questionId);
		if(question==null){
			ret.put("result",-1);
			return ret;
		}
		List<Comment> comments= commentService.getComments(EntityType.QUESTION,questionId,page,pageSize);


		ret.put("result",0);
		ret.put("question",question);
		ret.put("comments",comments);
		ret.put("rows",comments.size());
		return ret;
	}

	@RequiresRoles("user")
	@RequestMapping(value = "/publish_comment",params = {"questionId"})
	public String publishQuestionComment(@Valid QuestionCommentForm form, Model model, Errors errors){
		logger.debug("get do_comment request");

		if(CommonUtils.handleErrors(model.asMap(),errors))
		{
			return "/q/"+form.getQuestionId();
		}
		Question question = questionService.getQuestionById(form.getQuestionId());
		if(question==null){
			return "redirect:/index";
		}
		form.setContent(CommonUtils.AtEscape(form.getContent(),userService));
		CommonUtils.escapeFormModle(form);

		Comment comment = new Comment();
		comment.setContent(form.getContent());
		comment.setParentId(question.getId());
		comment.setParentType(EntityType.QUESTION);
		comment.setUser(CommonUtils.getCurrentUser());
		comment.setSendDate(new Date());

		commentService.doComment(comment);
		Event event = new Event(EventType.QUESTION_RANK_UPDATE,comment.getUser().getId(), EntityType.QUESTION,question.getId());

		eventManager.publishEvent(event);

		return "redirect:/q/"+form.getQuestionId();
	}

	@RequiresRoles("user")
	@RequestMapping(value = "/publish_comment",params = {"questionId"},headers = {"x-requested-with=XMLHttpRequest","Accept=application/json"})
	public @ResponseBody Map<String,Object>
	publishQuestionCommentService(@Valid QuestionCommentForm form, Errors errors){
		logger.debug("get do_comment request");
		Map<String,Object> ret = new HashMap<>(4);
		if(CommonUtils.handleErrors(ret,errors))
		{
			ret.put("result",-1);
			return ret;
		}
		Question question = questionService.getQuestionById(form.getQuestionId());
		if(question==null){
			ret.put("result",-1);
			return ret;
		}
		form.setContent(CommonUtils.AtEscape(form.getContent(),userService));
		CommonUtils.escapeFormModle(form);

		Comment comment = new Comment();
		comment.setContent(form.getContent());
		comment.setParentType(EntityType.QUESTION);
		comment.setParentId(question.getId());
		comment.setUser(CommonUtils.getCurrentUser());
		comment.setSendDate(new Date());

		commentService.doComment(comment);
		if(comment.getId()==null || comment.getId()<=0){
			ret.put("result",-1);
			return ret;
		}
		Event event = new Event(EventType.QUESTION_RANK_UPDATE,comment.getUser().getId(), EntityType.QUESTION,question.getId());

		eventManager.publishEvent(event);
		ret.put("result",0);

		return ret;
	}

	@RequestMapping(value = "/comment_inform",headers = {"x-requested-with=XMLHttpRequest"})
	public @ResponseBody Map<String,Object> getCommentInform(String type,Long[] id){
		List<StaticCommentable> comments = new ArrayList<>(id.length);
		for(Long l : id){
			StaticCommentable commentable = new StaticCommentable(EntityType.valueOf(type),l);
			comments.add(commentable);
		}
		Map<String,Object> ret = new HashMap<>(2);
		commentService.fetchComments(comments);
		ret.put("result",0);
		ret.put("commentInform",comments);
		System.out.println("ok?");
		return ret;
	}
}







