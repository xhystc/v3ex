package com.xhystc.v3ex.controller;


import com.xhystc.v3ex.commons.CommonUtils;
import com.xhystc.v3ex.model.vo.form.LoginForm;
import com.xhystc.v3ex.model.vo.form.UserRegistForm;
import com.xhystc.v3ex.model.vo.Problem;
import com.xhystc.v3ex.service.UserService;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.log4j.Logger;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.Valid;
import java.util.*;


@Controller
public class LoginController
{

	private UserService userService;

	@Autowired
	public LoginController(UserService userService)
	{
		this.userService = userService;
	}

	private static Logger logger = Logger.getLogger(LoginController.class);

	@Value("#{configProperties['shiro.retryTimes']}")
	private int retryTimes;

	@Value("#{configProperties['shiro.interval']}")
	private int interval;

	@RequestMapping(value = "/do_login")
	public  String dologin(LoginForm form,Model model)
	{
		logger.info("user:"+form.getUsername()+" login");

		CommonUtils.escapeFormModle(form);
		UsernamePasswordToken usernamePasswordToken = new UsernamePasswordToken(form.getUsername(),form.getPassword());
		Subject subject = SecurityUtils.getSubject();
		List<Problem> problems = new ArrayList<>(3);
		try
		{
			if(StringUtils.hasText(form.getRemeberMe())){
				usernamePasswordToken.setRememberMe(true);
			}
			subject.login(usernamePasswordToken);
			if(!subject.isAuthenticated())
			{
				logger.info("user:"+form.getUsername()+" login failed:"+"!subject.isAuthenticated()");
				problems.add(new Problem("未知错误",null,"请与管理员联系"));
			}

		}catch (ExcessiveAttemptsException eae){
			logger.info("user:"+form.getUsername()+" login failed:"+"user interval");
			problems.add(new Problem("登录错误","尝试次数过多","请于"+interval+"秒之后重试"));
		} catch (LockedAccountException lae){
			logger.info("user:"+form.getUsername()+" login failed:"+"user locked");
			problems.add(new Problem("登录错误","","用户已被锁定"));
		}catch (UnknownAccountException uae){
			logger.info("user:"+form.getUsername()+" login failed:"+"user not found");
			problems.add(new Problem("登录错误", "账号或密码错误", "最多可以尝试"+retryTimes+"次"));
		}catch (IncorrectCredentialsException ice){
			logger.info("user:"+form.getUsername()+" login failed:"+"password wrong");
			problems.add(new Problem("登录错误", "账号或密码错误", "最多可以尝试"+retryTimes+"次"));
		} catch (AuthenticationException ae){
			logger.info("user:"+form.getUsername()+" login failed:"+"AuthenticationException:"+ae.getMessage());
			ae.printStackTrace();
			problems.add(new Problem("登录错误", "权限不足", "您无权访问此页面"));
		}
		if(problems.size()==0){
			return "redirect:/index";
		}
		model.addAttribute("problems",problems);
		return login();
	}

	@RequestMapping("/logout")
	public String logout(){
		Subject subject  = SecurityUtils.getSubject();
		subject.logout();
		return "redirect:index";
	}

	@RequestMapping("/do_regist")
	public String doRegist(@Valid UserRegistForm form, Errors errors, Model mv){

		if(CommonUtils.handleErrors(mv.asMap(),errors))
		{
			return regist();
		}

		CommonUtils.escapeFormModle(form);
		Set<Problem> problems = userService.userRegist(form);
		if(problems==null || problems.size() == 0){
			return "redirect:/index";
		}
		logger.info("username or email exist error");
		mv.addAttribute("problems",problems);
		return regist();
	}

	@RequestMapping(value = "/username_check",headers = {"x-requested-with=XMLHttpRequest","Accept=application/json"})
	public @ResponseBody Map<String,Object> usernameCheck(String username){
		Map<String,Object> ret = new HashMap<>(2);
		ret.put("result",0);
		if(userService.getUserByName(StringEscapeUtils.escapeHtml4(username))!=null){
			ret.put("exit",1);
		}else {
			ret.put("exit",0);
		}
		return ret;
	}
	@RequestMapping(value = "/email_check",headers = {"x-requested-with=XMLHttpRequest","Accept=application/json"})
	public @ResponseBody Map<String,Object> emailCheck(String email){
		Map<String,Object> ret = new HashMap<>(2);
		ret.put("result",0);
		if(userService.getUserByEmail(StringEscapeUtils.escapeHtml4(email))!=null){
			ret.put("exit",1);
		}else {
			ret.put("exit",0);
		}
		return ret;
	}

	@RequestMapping(value = "/hint_username",headers =  {"x-requested-with=XMLHttpRequest","Accept=application/json"})
	public @ResponseBody Map<String,Object> hintUsername(String name){
		Map<String,Object> ret = new HashMap<>(2);
		ret.put("result",0);
		ret.put("users",userService.getUserByUsersnamePrefix(name));
		return ret;
	}

	@RequestMapping("/login")
	public String login(){
		return "login";
	}

	@RequestMapping("/regist")
	public String regist(){

		return "regist";
	}

}












