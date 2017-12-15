package com.xhystc.v3ex.controller;


import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class PageController
{
	private static Logger logger = Logger.getLogger(PageController.class);


	@RequestMapping("/question_comment")
	public String questionComment(){
		return "question_comment";
	}
	@RequestMapping("/scroll_test")
	public String scrollTest(){
		return "scroll_test";
	}


}

