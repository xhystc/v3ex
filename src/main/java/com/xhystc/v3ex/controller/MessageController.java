package com.xhystc.v3ex.controller;

import com.xhystc.v3ex.commons.CommonUtils;
import com.xhystc.v3ex.model.Conversation;
import com.xhystc.v3ex.model.Message;
import com.xhystc.v3ex.model.User;
import com.xhystc.v3ex.model.vo.form.MessageForm;
import com.xhystc.v3ex.model.vo.Problem;
import com.xhystc.v3ex.service.MessageService;
import com.xhystc.v3ex.service.UserService;
import org.apache.log4j.Logger;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

	@RequiresRoles("user")
	@RequestMapping("/message")
	public String message(Model model){
		List<Conversation> conversations = messageService.getConversations(CommonUtils.getCurrentUser().getId());
		model.addAttribute("conversations",conversations);
		return "message";
	}

	@RequiresRoles("user")
	@RequestMapping(value = "/message",headers = {"x-requested-with=XMLHttpRequest","Accept=application/json"})
	public @ResponseBody Map<String,Object> messageService(){
		List<Conversation> conversations = messageService.getConversations(CommonUtils.getCurrentUser().getId());
		Map<String,Object> ret = new HashMap<>(2);
		ret.put("result",0);
		ret.put("conversations",conversations);
		return ret;
	}

	@RequiresRoles("user")
	@RequestMapping(value = "/conversation",params = {"conversationId"})
	public String conversation(Model model,String conversationId){
		User user = CommonUtils.getCurrentUser();
		List<Message> messages = messageService.getMessages(user.getId(),conversationId);
		model.addAttribute("messages",messages);
		String[] ids = Conversation.divIds(conversationId);
		if (ids == null){
			logger.info("conversation id error");
			throw new IllegalArgumentException("conversation id error");
		}

		model.addAttribute("with",user.getId().toString().equals(ids[0])?ids[1]:ids[0]);
		return "conversation";
	}
	@RequestMapping(value = "/conversation",params = {"conversationId"},headers = {"x-requested-with=XMLHttpRequest","Accept=application/json"})
	public @ResponseBody Map<String,Object> conversationService(String conversationId){
		User user = CommonUtils.getCurrentUser();
		List<Message> messages = messageService.getMessages(user.getId(),conversationId);
		Map<String,Object> ret = new HashMap<>(3);
		ret.put("result",0);
		ret.put("messages",messages);
		String[] ids = Conversation.divIds(conversationId);
		if (ids == null){
			logger.info("conversation id error");
			throw new IllegalArgumentException("conversation id error");
		}
		ret.put("with",user.getId().toString().equals(ids[0])?ids[1]:ids[0]);
		return ret;
	}


	@RequiresRoles("user")
	@RequestMapping(value = "/publish_message",method = RequestMethod.POST)
	public String publishMessage(@Valid MessageForm form,Model model ,Errors errors){
		if(CommonUtils.handleErrors(model.asMap(),errors)){
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
		User from = CommonUtils.getCurrentUser();
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
		CommonUtils.escapeFormModle(form);
		form.setContent(CommonUtils.AtEscape(form.getContent(),userService));
		messageService.publishMessage(from,to,form.getContent());
		return "redirect:/conversation?conversationId="+Conversation.conversationKey(to.getId(),from.getId());
	}

	@RequiresRoles("user")
	@RequestMapping(value = "/publish_message",method = RequestMethod.POST,headers = {"x-requested-with=XMLHttpRequest","Accept=application/json"})
	public @ResponseBody Map<String,Object> publishMessageService(@Valid MessageForm form,Errors errors){
		Map<String,Object> ret = new HashMap<>(3);
		if(CommonUtils.handleErrors(ret,errors)){
			ret.put("result",-1);
			return ret;
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
		User from = CommonUtils.getCurrentUser();
		if(to==null){
			problems.add(new Problem(null,null,"用户不存在"));
		}
		if(from == null){
			problems.add(new Problem(null,null,"请先登录"));
		}
		if(problems.size()>0){
			ret.put("problems",problems);
			ret.put("result",-1);
			return ret;
		}
		CommonUtils.escapeFormModle(form);
		form.setContent(CommonUtils.AtEscape(form.getContent(),userService));
		problems = messageService.publishMessage(from,to,form.getContent());
		if(problems.size()>0){
			ret.put("result",-1);
			ret.put("problems",problems);
			return ret;
		}
		ret.put("result",0);
		return ret;
	}

}







