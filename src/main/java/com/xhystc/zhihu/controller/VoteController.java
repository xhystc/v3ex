package com.xhystc.zhihu.controller;

import com.xhystc.zhihu.commons.FormUtils;
import com.xhystc.zhihu.model.User;
import com.xhystc.zhihu.model.Vote;
import com.xhystc.zhihu.model.vo.json.GeneralResultBean;
import com.xhystc.zhihu.service.VoteService;
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

	private GeneralResultBean doVote(String type,Long id){
		User user = FormUtils.getCurrentUser();
		if(user==null)
			return new GeneralResultBean(0,"请先登录");
		Vote vote = new Vote(user.getId(),type,id);
		if(voteService.isVoted(user.getId(),type,id)){
			voteService.disVote(vote);
			return new GeneralResultBean(-1,""+voteService.getVotes(type,id));
		}else {
			voteService.doVote(vote);
			return new GeneralResultBean(1,""+voteService.getVotes(type,id));
		}

	}

}





