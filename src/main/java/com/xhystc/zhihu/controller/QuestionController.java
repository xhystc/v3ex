package com.xhystc.zhihu.controller;

import com.xhystc.zhihu.commons.FormUtils;
import com.xhystc.zhihu.model.Question;
import com.xhystc.zhihu.model.User;
import com.xhystc.zhihu.model.vo.form.QuestionForm;
import com.xhystc.zhihu.model.vo.json.Problem;
import com.xhystc.zhihu.service.QuestionCommentService;
import com.xhystc.zhihu.service.VoteService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.List;
import java.util.Set;

@Controller
public class QuestionController
{

	private QuestionCommentService questionCommentService;
	private VoteService voteService;

	@Autowired
	public QuestionController(QuestionCommentService questionCommentService, VoteService voteService)
	{
		this.questionCommentService = questionCommentService;
		this.voteService = voteService;
	}

	@Value("#{configProperties['page.pageSize']}")
	int pageSize;
	private static Logger logger = Logger.getLogger(QuestionController.class);

	@RequestMapping(value = {"/","/index"})
	public String index(Model model){
		List<Question> questions = doQuestions(1);
		model.addAttribute("questions", questions);
		return "v2ex";
	}

	@RequestMapping(value = "/questions")
	public String questions(@RequestParam(defaultValue = "2") int page,Model model){
		List<Question> questions = doQuestions(page);
		model.addAttribute("questions", questions);
		return "item";
	}

	@RequestMapping("/publish")
	public String publish(@Valid QuestionForm form, Errors errors, Model m, HttpSession session){



		FormUtils.escapeHTML(form);

		User user = FormUtils.getCurrentUser();
		Set<Problem> ret = questionCommentService.publishQuestion(user,form);
		if(ret.size()>0){
			m.addAttribute("problems",ret);
			return "edit_question";
		}
		return "redirect:/index";
	}

	@RequestMapping("/edit_question")
	public String editQuestion(){
		return "edit_question";
	}

	private List<Question> doQuestions(int page){
		List<Question> questionBeans = questionCommentService.getQuestionItems(page,pageSize);
		logger.debug("get questions:"+ questionBeans.size());
		User user = FormUtils.getCurrentUser();
		Long userId = user==null?-1:user.getId();
		voteService.fetchUserVotes(userId,questionBeans,"question");
		return questionBeans;
	}
}



