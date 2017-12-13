package com.xhystc.zhihu.commons;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class TokenFilter implements Filter
{
	private List<String> tokenPath = new ArrayList<>();
	private List<String> excludedPrefix = new ArrayList<>();;
	private String redirect="/index";
	private String tokenParam = "formToken";
	private static final String FORM_TOKEN_KEY = "xhystc-form-token-key";

	@Override
	public void init(FilterConfig filterConfig) throws ServletException
	{

	}

	@Override
	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException
	{
		HttpServletRequest request = (HttpServletRequest) servletRequest;
		HttpServletResponse response = (HttpServletResponse) servletResponse;
		HttpSession session = ((HttpServletRequest) servletRequest).getSession();

		String path = request.getServletPath();


		for(String s : excludedPrefix){
			if (path.startsWith(s) || path.contains("service")){
				filterChain.doFilter(servletRequest,servletResponse);
				return;
			}
		}

		if(tokenPath.contains(path)){
			if(!validateToken(request.getParameter(tokenParam),session)){
				response.sendRedirect(redirect);
				return;
			}
		}

		request.setAttribute(tokenParam,createToken(session));
		filterChain.doFilter(servletRequest,servletResponse);
	}

	private String createToken(HttpSession session){
		String token = UUID.randomUUID().toString().substring(0,6);
		session.setAttribute(FORM_TOKEN_KEY,token);
		return token;
	}

	private boolean validateToken(String token, HttpSession session){
		if(token==null || !token.equals(session.getAttribute(FORM_TOKEN_KEY))){
			return false;
		}
		session.setAttribute(FORM_TOKEN_KEY,null);
		return true;
	}

	@Override
	public void destroy()
	{

	}

	public String getTokenParam()
	{
		return tokenParam;
	}

	public void setTokenParam(String tokenParam)
	{
		this.tokenParam = tokenParam;
	}

	public List<String> getTokenPath()
	{
		return tokenPath;
	}

	public void setTokenPath(List<String> tokenPath)
	{
		this.tokenPath = tokenPath;
	}

	public String getRedirect()
	{
		return redirect;
	}

	public void setRedirect(String redirect)
	{
		this.redirect = redirect;
	}

	public List<String> getExcludedPrefix()
	{
		return excludedPrefix;
	}

	public void setExcludedPrefix(List<String> excludedPrefix)
	{
		this.excludedPrefix = excludedPrefix;
	}
}
