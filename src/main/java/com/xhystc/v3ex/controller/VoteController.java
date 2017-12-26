package com.xhystc.v3ex.controller;

import com.xhystc.v3ex.async.Event;
import com.xhystc.v3ex.async.EventManager;
import com.xhystc.v3ex.async.EventType;
import com.xhystc.v3ex.commons.CommonUtils;
import com.xhystc.v3ex.model.*;
import com.xhystc.v3ex.model.vo.json.GeneralResultBean;
import com.xhystc.v3ex.service.HotTopicService;
import com.xhystc.v3ex.service.VoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class VoteController
{

	private VoteService voteService;
	private HotTopicService hotTopicService;
	private EventManager eventManager;

	@Autowired
	public VoteController(VoteService voteService,HotTopicService hotTopicService,EventManager eventManager)
	{
		this.voteService = voteService;
		this.hotTopicService = hotTopicService;
		this.eventManager = eventManager;
	}

	@RequestMapping(value = "/vote/service/do_vote",headers = {"Accept=application/json"},params = {"questionId"})
	public @ResponseBody GeneralResultBean doQuestionVote(Long questionId){
		return doVote("question",questionId);
	}

	@RequestMapping(value = "/vote/service/do_vote",headers = {"Accept=application/json"},params = {"commentId"})
	public @ResponseBody GeneralResultBean doCommentVote(Long commentId){
		return doVote("comment",commentId);
	}

	private GeneralResultBean doVote(String type,Long id){
		User user = CommonUtils.getCurrentUser();
		if(user==null)
		{
			return new GeneralResultBean(0,"请先登录");
		}
		if(voteService.isVote(user.getId(), EntityType.valueOf(type),id)){
			voteService.disVote(user.getId(),EntityType.valueOf(type),id);
			hotTopicService.incScore(id,-1);
			return new GeneralResultBean(-1,""+voteService.voteCount(EntityType.valueOf(type),id));
		}else {
			voteService.doVote(user.getId(),EntityType.valueOf(type),id);
			Event event = new Event(EventType.VOTE_EVENT,user.getId(),type,id);
			event.addExt("id",id.toString());
			eventManager.publishEvent(event);
			return new GeneralResultBean(1,""+voteService.voteCount(EntityType.valueOf(type),id));
		}

	}

}





