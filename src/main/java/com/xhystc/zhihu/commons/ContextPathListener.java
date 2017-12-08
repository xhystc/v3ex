package com.xhystc.zhihu.commons;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class ContextPathListener implements ServletContextListener
{

	@Override
	public void contextInitialized(ServletContextEvent servletContextEvent)
	{
		servletContextEvent.getServletContext().setAttribute("path",servletContextEvent.getServletContext().getContextPath());
	}

	@Override
	public void contextDestroyed(ServletContextEvent servletContextEvent)
	{

	}
}
