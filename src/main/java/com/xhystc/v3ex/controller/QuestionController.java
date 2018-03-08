package com.xhystc.v3ex.controller;

import com.xhystc.v3ex.async.Event;
import com.xhystc.v3ex.async.EventManager;
import com.xhystc.v3ex.async.EventType;
import com.xhystc.v3ex.commons.CommonUtils;
import com.xhystc.v3ex.model.EntityType;
import com.xhystc.v3ex.model.Question;
import com.xhystc.v3ex.model.User;
import com.xhystc.v3ex.model.vo.form.QuestionForm;
import com.xhystc.v3ex.service.*;
import org.apache.log4j.Logger;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SessionAttributes(value = {"unread"})
@Controller
public class QuestionController
{
	@Value("#{configProperties['page.pageSize']}")
	int pageSize;
	private static Logger logger = Logger.getLogger(QuestionController.class);


	private VoteService voteService;
	private TagService tagService;
	private QuestionService questionService;
	private CommentService commentService;
	private MessageService messageService;
	private HotTopicService hotTopicService;
	private UserService userService;
	private EventManager eventManager;


	@Autowired
	public QuestionController(VoteService voteService,
	                          TagService tagService,
	                          QuestionService questionService,
	                          CommentService commentService,
	                          MessageService messageService,
	                          HotTopicService hotTopicService,
	                          UserService userService,
	                          EventManager eventManager)
	{
		this.voteService = voteService;
		this.tagService = tagService;
		this.questionService = questionService;
		this.commentService = commentService;
		this.messageService = messageService;
		this.hotTopicService = hotTopicService;
		this.userService = userService;
		this.eventManager = eventManager;
	}



	@RequestMapping(value = {"/","/index"})
	public String index(Model model,@RequestParam(defaultValue = "1") int page,Long tagId){
		List<Question> questions = getQuestions(page,tagId);
		commentService.fetchComments(questions);
		model.addAttribute("questions", questions);
		model.addAttribute("tags",tagService.getAllTag());
		model.addAttribute("currentParam","tagId="+tagId);
		int total = questionService.total(tagId);
		int lastPage =total/pageSize+(total%pageSize>0?1:0);
		model.addAttribute("pageButtons", CommonUtils.pageButtons(page,lastPage));
		model.addAttribute("currentPage",page);
		model.addAttribute("lastPage",lastPage);
		model.addAttribute("hotQuestions",hotTopicService.getHotTopics());
		User currentUser = CommonUtils.getCurrentUser();
		if(currentUser!=null){
			model.addAttribute("unread",messageService.unread(currentUser.getId()));
		}

		return "v2ex";
	}

	@RequestMapping(value = "/questions",headers = {"x-requested-with=XMLHttpRequest","Accept=application/json"})
	public @ResponseBody Map<String,Object> questionsService(@RequestParam(defaultValue = "1") int page, Long tagId){
		List<Question> questions = getQuestions(page,tagId);
		Map<String,Object> ret = new HashMap<>(2);
		ret.put("questions", questions);
		return ret;
	}

	@RequiresRoles("user")
	@RequestMapping("/publish_question")
	public String publish(@Valid QuestionForm form, Errors errors, Model m){

		if(CommonUtils.handleErrors(m.asMap(),errors)){
			return editQuestion(m);
		}

		form.setContent(CommonUtils.AtEscape(form.getContent(),userService));
		CommonUtils.escapeFormModle(form);

		User user = CommonUtils.getCurrentUser();

		Long id = questionService.publishQuetion(user,form);
		if(id!=null && id>0){
			Event event = new Event(EventType.PUBLISH_QUESTION,user.getId(), EntityType.QUESTION,id);
			eventManager.publishEvent(event);
		}


		return "redirect:/index";
	}

	@RequiresRoles("user")
	@RequestMapping(value = "/publish_question",headers = {"x-requested-with=XMLHttpRequest","Accept=application/json"})
	public @ResponseBody Map<String,Object> publishService(@Valid QuestionForm form, Errors errors){
		Map<String,Object> ret = new HashMap<>(3);
		if(CommonUtils.handleErrors(ret,errors)){
			ret.put("result",-1);
			return ret;
		}

		form.setContent(CommonUtils.AtEscape(form.getContent(),userService));
		CommonUtils.escapeFormModle(form);

		User user = CommonUtils.getCurrentUser();

		Long id = questionService.publishQuetion(user,form);
		if(id!=null && id>0){
			Event event = new Event(EventType.PUBLISH_QUESTION,user.getId(), EntityType.QUESTION,id);
			eventManager.publishEvent(event);
		}
		ret.put("result",0);

		return ret;
	}

	@RequiresRoles("user")
	@RequestMapping("/edit_question")
	public String editQuestion(Model model){
		model.addAttribute("tags",tagService.getAllTag());
		return "question_edit";
	}

	private List<Question> getQuestions(int page, Long tagId){
		List<Question> questions = questionService.getQuestions(tagId,page,pageSize);
		Long userId = CommonUtils.getCurrentUser()==null? null : CommonUtils.getCurrentUser().getId();
		voteService.fetchUserVotes(userId,questions);
		commentService.fetchComments(questions);

		return questions;
	}
}






