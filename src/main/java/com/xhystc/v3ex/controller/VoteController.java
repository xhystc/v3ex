package com.xhystc.v3ex.controller;

import com.xhystc.v3ex.commons.FormUtils;
import com.xhystc.v3ex.model.User;
import com.xhystc.v3ex.model.Vote;
import com.xhystc.v3ex.model.vo.json.GeneralResultBean;
import com.xhystc.v3ex.service.VoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class VoteController
{

	private VoteService voteService;

	@Autowired
	public VoteController(VoteService voteService)
	{
		this.voteService = voteService;
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
		User user = FormUtils.getCurrentUser();
		if(user==null)
		{
			return new GeneralResultBean(0,"请先登录");
		}
		Vote vote = new Vote(user.getId(),type,id);
		if(voteService.isVote(user.getId(),type,id)){
			voteService.disVote(user.getId(),type,id);
			return new GeneralResultBean(-1,""+voteService.voteInform(type,id).getVoteCount());
		}else {
			voteService.doVote(user.getId(),type,id);
			return new GeneralResultBean(1,""+voteService.voteInform(type,id).getVoteCount());
		}

	}

}





