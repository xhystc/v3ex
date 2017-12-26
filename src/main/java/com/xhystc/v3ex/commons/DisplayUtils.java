package com.xhystc.v3ex.commons;

import com.xhystc.v3ex.model.Message;
import com.xhystc.v3ex.model.User;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
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
			return "你 对 <a href="+path+"/user/"+to.getId()+">"+"<strong>"+to.getName()+"</strong></a> 说";
		}else {
			return "<a href="+path+"/user/"+from.getId()+">"+"<strong>"+from.getName()+"</strong></a> 对 你 说";
		}

	}

	public static String linkConvert(String path,String content){
		StringBuilder sb = new StringBuilder();
		int ptr = 0;
		while (ptr < content.length()){
			int index = content.indexOf(CommonUtils.LINK_BEGIN,ptr);

			if(index<0){
				sb.append(content.substring(ptr));
				break;
			}
			sb.append(content.substring(ptr,index));
			int nextIndex = CommonUtils.nextIndex(content,index+CommonUtils.LINK_BEGIN.length(),CommonUtils.LINK_END);
			String substring = content.substring(index+CommonUtils.LINK_BEGIN.length(),nextIndex);
			if(substring.contains(CommonUtils.LINK_DIVIDE)){
				String[] t = substring.split(CommonUtils.LINK_DIVIDE);
				String s = CommonUtils.wrapLinkHtml(path+t[0],t[1]);
				System.out.println(s);
				sb.append(s);
				ptr = nextIndex+1;
			}else {
				sb.append(content.substring(index,nextIndex));
				ptr = nextIndex;
			}

		}
		return sb.toString();
	}
}






