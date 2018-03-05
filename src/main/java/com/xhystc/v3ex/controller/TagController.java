package com.xhystc.v3ex.controller;

import com.xhystc.v3ex.model.Tag;
import com.xhystc.v3ex.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class TagController
{
	@Autowired
	private TagService tagService;


	@RequestMapping("/tags")
	public @ResponseBody Map<String,Object> tags(){
		List<Tag> tags = tagService.getAllTag();
		Map<String,Object> ret = new HashMap<>(2);
		ret.put("result",0);
		ret.put("tags",tags);
		return ret;
	}

	@RequestMapping("/hint_tag")
	public @ResponseBody Map<String,Object> hint(String name){
		List<Tag> tags = tagService.getTagsByName(name);
		Map<String,Object> ret = new HashMap<>(2);
		ret.put("result",0);
		ret.put("tags",tags);
		return ret;
	}
}






