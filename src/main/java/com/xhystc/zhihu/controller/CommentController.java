package com.xhystc.zhihu.controller;

import com.xhystc.zhihu.commons.FormUtils;
import com.xhystc.zhihu.model.User;
import com.xhystc.zhihu.model.vo.form.QuestionCommentForm;
import com.xhystc.zhihu.model.vo.json.Problem;
import com.xhystc.zhihu.model.vo.page.CommentPage;
import com.xhystc.zhihu.service.QuestionCommentService;
import com.xhystc.zhihu.service.VoteService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.Set;

@Controller
public class CommentController
{

	private QuestionCommentService questionCommentService;
	private VoteService voteService;

	@Autowired
	public CommentController(QuestionCommentService questionCommentService, VoteService voteService)
	{
		this.questionCommentService = questionCommentService;
		this.voteService = voteService;
	}

	private static Logger logger = Logger.getLogger(CommentController.class);

	@Value("#{configProperties['page.pageSize']}")
	int pageSize;

	@RequestMapping("/q/{questionId}")
	public String questionComment(@PathVariable("questionId") Long questionId,Model model,
	                              @RequestParam(defaultValue = "1") int page){
		CommentPage commentPage = questionCommentService.commentPage(questionId,page,pageSize);

		User user = FormUtils.getCurrentUser();
		Long userId = user==null?-1:user.getId();
		voteService.fetchUserVotes(userId,commentPage.getQuestion());
		voteService.fetchUserVotes(userId,commentPage.getComments(),"comment");

		model.addAttribute("commentPage",commentPage);

		return "question_comment";
	}

	@RequestMapping(value = "/do_comment",params = {"questionId"})
	public String doQuestionComment(@Valid QuestionCommentForm form, Model model, HttpSession session, Errors errors){
		logger.debug("get do_comment request");
		String error = FormUtils.handleErrors(model,errors);
		if(error!=null)
		{
			return error;
		}
		FormUtils.escapeHTML(form);

		Set<Problem> problems = questionCommentService.publishQuestionComment(FormUtils.getCurrentUser(),form);
		if(problems!=null && problems.size()>0){
			model.addAttribute("problems",problems);
			return questionComment(form.getQuestionId(),model,1);
		}

		return "redirect:/q/"+form.getQuestionId();
	}
}







