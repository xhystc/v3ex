package com.xhystc.v3ex.controller;

import com.xhystc.v3ex.commons.CommonUtils;
import com.xhystc.v3ex.model.Question;
import com.xhystc.v3ex.model.User;
import com.xhystc.v3ex.model.vo.SolrSearchResult;
import com.xhystc.v3ex.service.*;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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

	@Value("#{configProperties['page.pageSize']}")
	int pageSize;


	@RequestMapping("/searchService")
	public @ResponseBody Map<String,Object> searchService(@NotBlank String q,
	                                                      @RequestParam(defaultValue = "1") int page,String tag){
		Map<String,Object > ret = new HashMap<>(3);
		SolrSearchResult questions = questionSearchService.searchQuestions(q,tag,(page-1)*10,10);
		if(questions !=null){
			ret.put("result",0);
			ret.put("total",questions.getTotal());
			ret.put("questions",questions.getQuestions());
			return ret;
		}
		ret.put("result",-1);
		return ret;
	}

	@RequestMapping("/search")
	public String search(@NotBlank String q,@RequestParam(defaultValue = "1") int page,String tag,Model model){
		Map<String,Object > ret = new HashMap<>(3);
		SolrSearchResult res = questionSearchService.searchQuestions(q,tag,(page-1)*pageSize,pageSize);
		List<Question> questions = res.getQuestions();


		int total = Integer.parseInt(String.valueOf(res.getTotal()));
		int lastPage =total/pageSize+(total%pageSize>0?1:0);
		model.addAttribute("currentParam","q="+q);
		model.addAttribute("questions", questions);
		model.addAttribute("pageButtons", CommonUtils.pageButtons(page,lastPage));
		model.addAttribute("currentPage",page);
		model.addAttribute("lastPage",lastPage);
		model.addAttribute("hotQuestions",hotTopicService.getHotTopics());
		if(questions==null || questions.size()<=0){
			return "search_result";
		}
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





