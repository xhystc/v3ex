package com.xhystc.v3ex.commons;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtils
{
	public static String dateFrom(Date date)
	{
		Calendar curr = Calendar.getInstance();
		Calendar from  = Calendar.getInstance();

		from.setTimeInMillis(date.getTime());
		int yearCha = curr.get(Calendar.YEAR) - from.get(Calendar.YEAR);
		int monthCha = curr.get(Calendar.MONTH) - from.get(Calendar.MONTH);
		int dayCha = curr.get(Calendar.DAY_OF_MONTH) - from.get(Calendar.DAY_OF_MONTH);
		int hourCha = curr.get(Calendar.HOUR_OF_DAY) - from.get(Calendar.HOUR_OF_DAY);
		int minCha = curr.get(Calendar.MINUTE) - from.get(Calendar.MINUTE);

		String ret;
		if(yearCha>0){
			DateFormat format = new SimpleDateFormat("yyyy年MM月dd日");
			ret = format.format(date);
		}else if(monthCha>0){
			DateFormat format = new SimpleDateFormat("MM月dd日");
			ret = format.format(date);
		}else if(dayCha>0){
			ret = dayCha+"天前";
		}else if(hourCha>0){
			ret = hourCha+"小时前";
		}else if(minCha>0){
			ret = minCha+"分钟前";
		}else {
			ret = "刚刚";
		}
		return ret;
	}

}
