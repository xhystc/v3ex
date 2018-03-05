package com.xhystc.v3ex.controller;

import com.xhystc.v3ex.async.EventManager;
import com.xhystc.v3ex.commons.CommonUtils;
import com.xhystc.v3ex.model.Question;
import com.xhystc.v3ex.model.User;
import com.xhystc.v3ex.service.*;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class SearchController
{
	@Autowired
	QuestionSearchService questionSearchService;
	@Autowired
	private CommentService commentService;
	@Autowired
	private MessageService messageService;
	@Autowired
	private HotTopicService hotTopicService;
	@Autowired
	private VoteService voteService;


	@RequestMapping("/searchService")
	public @ResponseBody Map<String,Object> searchService(@NotBlank String q,
	                                                      @RequestParam(defaultValue = "1") int page,String tag){
		Map<String,Object > ret = new HashMap<>(3);
		List<Question> questions = questionSearchService.searchQuestions(q,tag,(page-1)*10,10);
		if(questions !=null){
			ret.put("result",0);
			ret.put("rows",questions.size());
			ret.put("questions",questions);
			return ret;
		}
		ret.put("result",-1);
		return ret;
	}

	@RequestMapping("/search")
	public String search(@NotBlank String q,@RequestParam(defaultValue = "1") int page,String tag,Model model){
		Map<String,Object > ret = new HashMap<>(3);
		List<Question> questions = questionSearchService.searchQuestions(q,tag,(page-1)*10,10);
		commentService.fetchComments(questions);
		model.addAttribute("questions", questions);

		model.addAttribute("hotQuestions",hotTopicService.getHotTopics());
		if(CommonUtils.getCurrentUser()!=null){
			voteService.fetchUserVotes(CommonUtils.getCurrentUser().getId(),questions);
		}
		commentService.fetchComments(questions);
		User currentUser = CommonUtils.getCurrentUser();
		if(currentUser!=null){
			model.addAttribute("unread",messageService.unread(currentUser.getId()));
		}
		return "search_result";
	}


	public QuestionSearchService getQuestionSearchService()
	{
		return questionSearchService;
	}

	public void setQuestionSearchService(QuestionSearchService questionSearchService)
	{
		this.questionSearchService = questionSearchService;
	}
}





