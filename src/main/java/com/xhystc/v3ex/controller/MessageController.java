package com.xhystc.v3ex.controller;

import com.xhystc.v3ex.commons.FormUtils;
import com.xhystc.v3ex.model.Conversation;
import com.xhystc.v3ex.model.Message;
import com.xhystc.v3ex.model.User;
import com.xhystc.v3ex.model.vo.form.MessageForm;
import com.xhystc.v3ex.model.vo.json.Problem;
import com.xhystc.v3ex.service.MessageService;
import com.xhystc.v3ex.service.UserService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@Controller
public class MessageController
{
	private static Logger logger = Logger.getLogger(MessageController.class);

	private MessageService messageService;
	private UserService userService;

	@Autowired
	public MessageController(MessageService messageService, UserService userService)
	{
		this.messageService = messageService;
		this.userService = userService;
	}

	@RequestMapping("/message")
	public String message(Model model){
		List<Conversation> conversations = messageService.getConversations(FormUtils.getCurrentUser().getId());
		model.addAttribute("conversations",conversations);
		return "message";
	}
	@RequestMapping(value = "/conversation",params = {"conversationId"})
	public String conversation(Model model,String conversationId){
		User user = FormUtils.getCurrentUser();
		List<Message> messages = messageService.getMessages(user.getId(),conversationId);
		model.addAttribute("messages",messages);
		String[] ids = conversationId.split("_");
		if (ids.length!=2){
			logger.info("conversation id error");
			throw new IllegalArgumentException("conversation id error");
		}
		model.addAttribute("with",user.getId().toString().equals(ids[0])?ids[1]:ids[0]);
		return "conversation";
	}


	@RequestMapping(value = "/publish_message",method = RequestMethod.POST)
	public String publishMessage(@Valid MessageForm form,Model model ,Errors errors){
		if(FormUtils.handleErrors(model,errors)){
			return message(model);
		}
		List<Problem> problems = new ArrayList<>(3);
		User to = null;
		Long toId = null;
		try
		{
			 toId = Long.parseLong(form.getTo());
			 to = userService.getUserById(toId);
		}catch (NumberFormatException ignored){

		}
		if(to == null){
			to = userService.getUserByName(form.getTo());
		}
		User from = FormUtils.getCurrentUser();
		if(to==null){
			problems.add(new Problem(null,null,"用户不存在"));
		}
		if(from == null){
			problems.add(new Problem(null,null,"请先登录"));
		}
		if(problems.size()>0){
			model.addAttribute("problems",problems);
			return message(model);
		}
		messageService.publishMessage(from,to,form.getContent());
		return "redirect:/conversation?conversationId="+Conversation.conversationKey(to.getId(),from.getId());
	}

}







