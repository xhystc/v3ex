package com.xhystc.v3ex.controller;

import com.alibaba.fastjson.JSON;
import org.apache.log4j.Logger;
import org.apache.shiro.authz.AuthorizationException;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class BaseControllerAdvice
{
	private static final Logger logger = Logger.getLogger(BaseControllerAdvice.class);
	@ExceptionHandler(AuthorizationException.class)
	public void handleAuthorizationException(HttpServletRequest request, HttpServletResponse response) throws IOException
	{
		if("XMLHttpRequest".equalsIgnoreCase(request.getHeader("x-requested-with"))
				|| request.getServletPath().contains("service")){
			logger.info("ajax auth exception");
			Map<String,Object> ret = new HashMap<>(3);
			ret.put("result",-1);
			ret.put("hint","请先登录");
			response.setHeader("Content-Type","application/json;charset=UTF-8");
			response.getOutputStream().write(JSON.toJSONString(ret).getBytes("utf-8"));

		}else {
			logger.info("auth exception");
			response.sendRedirect(request.getContextPath()+"/login");
		}
	}
}
