package com.xhystc.zhihu.controller;

import com.xhystc.zhihu.commons.FormUtils;
import com.xhystc.zhihu.model.Comment;
import com.xhystc.zhihu.model.CommentInform;
import com.xhystc.zhihu.model.User;
import com.xhystc.zhihu.model.vo.form.QuestionCommentForm;
import com.xhystc.zhihu.model.vo.json.Problem;
import com.xhystc.zhihu.model.vo.page.CommentPage;
import com.xhystc.zhihu.service.*;
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
import java.util.Date;
import java.util.List;
import java.util.Set;

@Controller
public class CommentController
{
	private static final Logger logger = Logger.getLogger(CommentController.class);

	@Value("#{configProperties['page.pageSize']}")
	int pageSize;

	private CommentService commentService;
	private QuestionService questionService;
	private VoteService voteService;

	@Autowired
	public CommentController(CommentService commentService, QuestionService questionService,VoteService voteService)
	{
		this.commentService = commentService;
		this.questionService = questionService;
		this.voteService = voteService;
	}

	@RequestMapping("/q/{questionId}")
	public String questionComment(@PathVariable("questionId") Long questionId,Model model,
	                              @RequestParam(defaultValue = "1") int page){
		/*CommentPage commentPage = questionCommentService.commentPage(questionId,page,pageSize);*/

		CommentPage commentPage = commentService.getQuestionCommentPage("question",questionId,page,pageSize);
		Long userId = FormUtils.getCurrentUser()==null? null : FormUtils.getCurrentUser().getId();
		voteService.fetchUserVote(userId,commentPage.getQuestion());
		voteService.fetchUserVotes(userId,commentPage.getComments());
		model.addAttribute("commentPage",commentPage);

		return "question_comment";
	}

	@RequestMapping(value = "/do_comment",params = {"questionId"})
	public String doQuestionComment(@Valid QuestionCommentForm form, Model model, HttpSession session, Errors errors){
		logger.debug("get do_comment request");

		if(FormUtils.handleErrors(model,errors))
		{
			return "/q/"+form.getQuestionId();
		}
		FormUtils.escapeFormModle(form);

		/*Set<Problem> problems = questionCommentService.publishQuestionComment(FormUtils.getCurrentUser(),form);
		if(problems!=null && problems.size()>0){
			model.addAttribute("problems",problems);
			return questionComment(form.getQuestionId(),model,1);
		}*/
		Comment comment = new Comment();
		comment.setContent(form.getContent());
		comment.setParentId(form.getQuestionId());
		comment.setUser(FormUtils.getCurrentUser());
		comment.setSendDate(new Date());
		comment.setParentType("question");

		commentService.doComment(comment);
		questionService.upQuestion(form.getQuestionId());

		return "redirect:/q/"+form.getQuestionId();
	}
}







