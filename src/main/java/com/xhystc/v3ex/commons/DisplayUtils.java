package com.xhystc.v3ex.commons;

import com.xhystc.v3ex.model.Conversation;
import com.xhystc.v3ex.model.Message;
import com.xhystc.v3ex.model.User;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DisplayUtils
{
	public static String dateFrom(Date date)
	{
		String ret;
		if(date == null){
			return "";
		}
		long cha = (System.currentTimeMillis()-date.getTime())/1000;
		if(cha < 60){
			ret = "刚刚";
		}else if(cha <3600){
			ret = cha/60+"分钟前";
		}else if(cha < 86400){
			ret = cha/3600+"小时前";
		}else if(cha < 2592000){
			ret = cha/86400+"天前";
		}else {
			DateFormat format = new SimpleDateFormat("yyyy年MM月dd日 HH时mm分ss秒");
			ret = format.format(date);
		}
		return ret;
	}
	public static String showConversation(String path,User currentUser, Message message){
		User from = message.getFrom();
		User to = message.getTo();
		if(from.getId().equals(currentUser.getId())){
			return "你 对 <a href="+path+"/user/"+to.getId()+">"+"<strong>"+to.getName()+"</strong></a>说";
		}else {
			return "<a href="+path+"/user/"+from.getId()+">"+"<strong>"+from.getName()+"</strong></a> 对 你说";
		}

	}
}
