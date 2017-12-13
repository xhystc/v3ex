package com.xhystc.zhihu.controller;

import com.xhystc.zhihu.commons.FormUtils;
import com.xhystc.zhihu.model.Question;
import com.xhystc.zhihu.model.User;
import com.xhystc.zhihu.model.vo.form.QuestionForm;
import com.xhystc.zhihu.model.vo.json.Problem;
import com.xhystc.zhihu.service.*;
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
	@Value("#{configProperties['page.pageSize']}")
	int pageSize;
	private static Logger logger = Logger.getLogger(QuestionController.class);


	private VoteService voteService;
	private TagService tagService;
	private QuestionService questionService;
	private CommentService commentService;



	@Autowired
	public QuestionController(VoteService voteService, TagService tagService, QuestionService questionService, CommentService commentService)
	{
		this.voteService = voteService;
		this.tagService = tagService;
		this.questionService = questionService;
		this.commentService = commentService;
	}



	@RequestMapping(value = {"/","/index"})
	public String index(Model model,@RequestParam(defaultValue = "1") int page,Long tagId){
		List<Question> questions = doQuestions(page,tagId);
		model.addAttribute("questions", questions);
		model.addAttribute("tags",tagService.getAllTag());
		model.addAttribute("currentTag",tagId);
		return "v2ex";
	}

	@RequestMapping(value = "/questions")
	public String questions(@RequestParam(defaultValue = "2") int page,Long tagId,Model model){
		List<Question> questions = doQuestions(page,tagId);
		model.addAttribute("questions", questions);
		return "item";
	}

	@RequestMapping("/publish")
	public String publish(@Valid QuestionForm form, Errors errors, Model m){

		if(FormUtils.handleErrors(m,errors)){
			return editQuestion(m);
		}

		FormUtils.escapeFormModle(form);
		User user = FormUtils.getCurrentUser();

		Long questionId = questionService.publishQuetion(user,form);
		commentService.initCommentInform("question",questionId);
		return "redirect:/index";
	}

	@RequestMapping("/edit_question")
	public String editQuestion(Model model){
		model.addAttribute("tags",tagService.getAllTag());
		return "question_edit";
	}

	private List<Question> doQuestions(int page,Long tagId){

		List<Question> questions = questionService.getQuestions(tagId,page,pageSize);
		Long userId = FormUtils.getCurrentUser()==null? null : FormUtils.getCurrentUser().getId();
		voteService.fetchUserVotes(userId,questions);
		return questions;
	}
}






