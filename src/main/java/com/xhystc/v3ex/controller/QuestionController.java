package com.xhystc.v3ex.controller;

import com.xhystc.v3ex.commons.FormUtils;
import com.xhystc.v3ex.model.Question;
import com.xhystc.v3ex.model.User;
import com.xhystc.v3ex.model.vo.form.QuestionForm;
import com.xhystc.v3ex.service.*;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.bind.annotation.SessionAttributes;

import javax.validation.Valid;
import java.util.List;

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


	@Autowired
	public QuestionController(VoteService voteService,
	                          TagService tagService,
	                          @Qualifier("redisCacheQuestionServiceImpl") QuestionService questionService,
	                          CommentService commentService,
	                          MessageService messageService)
	{
		this.voteService = voteService;
		this.tagService = tagService;
		this.questionService = questionService;
		this.commentService = commentService;
		this.messageService = messageService;
	}



	@RequestMapping(value = {"/","/index"})
	public String index(Model model,@RequestParam(defaultValue = "1") int page,Long tagId){
		List<Question> questions = doQuestions(page,tagId,model);
		model.addAttribute("questions", questions);
		model.addAttribute("tags",tagService.getAllTag());
		model.addAttribute("currentTag",tagId);
		int total = questionService.total(tagId);
		int lastPage =total/pageSize+(total%pageSize>0?1:0);
		model.addAttribute("pageButtons",FormUtils.pageButtons(page,lastPage));
		model.addAttribute("currentPage",page);
		model.addAttribute("lastPage",lastPage);
		User currentUser = FormUtils.getCurrentUser();
		if(currentUser!=null){
			model.addAttribute("unread",messageService.unread(currentUser.getId()));
		}
		return "v2ex";
	}

	@RequestMapping(value = "/questions")
	public String questions(@RequestParam(defaultValue = "2") int page,Long tagId,Model model){
		List<Question> questions = doQuestions(page,tagId,model);
		model.addAttribute("questions", questions);
		return "item";
	}

	@RequestMapping("/publish_question")
	public String publish(@Valid QuestionForm form, Errors errors, Model m){

		if(FormUtils.handleErrors(m,errors)){
			return editQuestion(m);
		}

		FormUtils.escapeFormModle(form);
		User user = FormUtils.getCurrentUser();

		Long questionId = questionService.publishQuetion(user,form);
		return "redirect:/index";
	}

	@RequestMapping("/edit_question")
	public String editQuestion(Model model){
		model.addAttribute("tags",tagService.getAllTag());
		return "question_edit";
	}

	private List<Question> doQuestions(int page,Long tagId,Model model){
		List<Question> questions = questionService.getQuestions(tagId,page,pageSize);
		Long userId = FormUtils.getCurrentUser()==null? null : FormUtils.getCurrentUser().getId();
		voteService.fetchUserVotes(userId,questions);
		commentService.fetchComments(questions);

		return questions;
	}
}






