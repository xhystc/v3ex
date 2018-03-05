package com.xhystc.v3ex.controller;

import com.xhystc.v3ex.async.Event;
import com.xhystc.v3ex.async.EventManager;
import com.xhystc.v3ex.async.EventType;
import com.xhystc.v3ex.commons.CommonUtils;
import com.xhystc.v3ex.model.*;
import com.xhystc.v3ex.service.HotTopicService;
import com.xhystc.v3ex.service.VoteService;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

@Controller
public class VoteController
{

	private VoteService voteService;
	private EventManager eventManager;

	@Autowired
	public VoteController(VoteService voteService,EventManager eventManager)
	{
		this.voteService = voteService;
		this.eventManager = eventManager;
	}

	@RequiresRoles("user")
	@RequestMapping(value = "/do_vote",headers = {"x-requested-with=XMLHttpRequest","Accept=application/json"},params = {"questionId"})
	public @ResponseBody  Map<String,Object>  doQuestionVote(Long questionId){
		return doVote("question",questionId);
	}

	@RequiresRoles("user")
	@RequestMapping(value = "/do_vote",headers = {"x-requested-with=XMLHttpRequest","Accept=application/json"},params = {"commentId"})
	public @ResponseBody Map<String,Object> doCommentVote(Long commentId){
		return doVote("comment",commentId);
	}



	private Map<String,Object> doVote(String type,Long id){
		User user = CommonUtils.getCurrentUser();
		System.out.println("user:"+user);
		Map<String,Object> ret = new HashMap<>(3);
		if(voteService.isVote(user.getId(), EntityType.valueOf(type.toUpperCase()),id)){
			voteService.disVote(user.getId(),EntityType.valueOf(type.toUpperCase()),id);
			ret.put("result",0);
			ret.put("code",-1);
		}else {
			voteService.doVote(user.getId(),EntityType.valueOf(type.toUpperCase()),id);
			Event event = new Event(EventType.VOTE_EVENT,user.getId(),EntityType.valueOf(type.toUpperCase()),id);
			eventManager.publishEvent(event);
			event = new Event(EventType.QUESTION_RANK_UPDATE,user.getId(),EntityType.QUESTION,id);
			eventManager.publishEvent(event);

			ret.put("result",0);
			ret.put("code",1);
		}
		ret.put("hint",voteService.voteCount(EntityType.valueOf(type.toUpperCase()),id));
		return ret;
	}

}





