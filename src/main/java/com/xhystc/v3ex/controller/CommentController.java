package com.xhystc.v3ex.controller;

import com.xhystc.v3ex.commons.FormUtils;
import com.xhystc.v3ex.model.Comment;
import com.xhystc.v3ex.model.CommentInform;
import com.xhystc.v3ex.model.Question;
import com.xhystc.v3ex.model.User;
import com.xhystc.v3ex.model.vo.form.QuestionCommentForm;
import com.xhystc.v3ex.model.vo.page.CommentPage;
import com.xhystc.v3ex.service.*;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
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

@Controller
public class CommentController
{
	private static final Logger logger = Logger.getLogger(CommentController.class);

	@Value("#{configProperties['page.pageSize']}")
	int pageSize;

	private CommentService commentService;
	private QuestionService questionService;
	private VoteService voteService;
	private TagService tagService;

	@Autowired
	public CommentController(CommentService commentService, @Qualifier("redisCacheQuestionServiceImpl") QuestionService questionService
			, VoteService voteService,TagService tagService)
	{
		this.commentService = commentService;
		this.questionService = questionService;
		this.voteService = voteService;
		this.tagService = tagService;
	}

	@RequestMapping("/q/{questionId}")
	public String questionComment(@PathVariable("questionId") Long questionId,Model model,
	                              @RequestParam(defaultValue = "1") int page){

		CommentPage commentPage = new CommentPage();
		Question question = questionService.getQuestion(questionId);
		commentPage.setQuestion(question);

		List<Comment> comments= commentService.getQuestionComments(question.type(),questionId,page,pageSize);

		commentPage.setComments(comments);
		commentPage.setTags(tagService.getQuestionTags(questionId));

		CommentInform commentInform = commentService.commentInform(question.type(),questionId);
		int lastPage = commentInform.getCommentCount()/pageSize+(commentInform.getCommentCount()%pageSize>0?1:0);

		User user = FormUtils.getCurrentUser();
		Long userId = user==null?null:user.getId();

		voteService.fetchUserVote(userId,commentPage.getQuestion());
		voteService.fetchUserVotes(userId,comments);

		commentService.fetchComments(comments);
		question.setCommentInform(commentInform);

		model.addAttribute("commentPage",commentPage);
		model.addAttribute("pageButtons",FormUtils.pageButtons(page,lastPage));
		model.addAttribute("currentPage",page);
		model.addAttribute("lastPage",lastPage);

		return "question_comment";
	}

	@RequestMapping(value = "/publish_comment",params = {"questionId"})
	public String publishQuestionComment(@Valid QuestionCommentForm form, Model model, HttpSession session, Errors errors){
		logger.debug("get do_comment request");

		if(FormUtils.handleErrors(model,errors))
		{
			return "/q/"+form.getQuestionId();
		}
		FormUtils.escapeFormModle(form);


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







