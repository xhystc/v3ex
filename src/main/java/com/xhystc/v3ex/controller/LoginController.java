package com.xhystc.v3ex.controller;


import com.xhystc.v3ex.commons.CommonUtils;
import com.xhystc.v3ex.model.vo.form.LoginForm;
import com.xhystc.v3ex.model.vo.form.UserRegistForm;
import com.xhystc.v3ex.model.vo.json.GeneralResultBean;
import com.xhystc.v3ex.model.vo.json.Problem;
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

import javax.servlet.http.HttpSession;
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
	public  String dologin(LoginForm form,Model model, HttpSession session)
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
		return login(session,model);
	}

	@RequestMapping("/logout")
	public String logout(){
		Subject subject  = SecurityUtils.getSubject();
		subject.logout();
		return "redirect:index";
	}

	@RequestMapping("/do_regist")
	public String doRegist(@Valid UserRegistForm form, Errors errors, Model mv,HttpSession session){

		if(CommonUtils.handleErrors(mv,errors))
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

	@RequestMapping("/doregist/username_check")
	public @ResponseBody GeneralResultBean usernameCheck(String username){
		if(userService.usernameExist(StringEscapeUtils.escapeHtml4(username))){
			return new GeneralResultBean(1,"exist");
		}
		return new GeneralResultBean(0,"not exist");
	}
	@RequestMapping("/doregist/email_check")
	public @ResponseBody GeneralResultBean emailCheck(String email){
		if(userService.emailExist(StringEscapeUtils.escapeHtml4(email))){
			return new GeneralResultBean(1,"exist");
		}
		return new GeneralResultBean(0,"not exist");
	}



	@RequestMapping("/login")
	public String login(HttpSession session, Model model){

		return "login";
	}

	@RequestMapping("/regist")
	public String regist(){

		return "regist";
	}

}












