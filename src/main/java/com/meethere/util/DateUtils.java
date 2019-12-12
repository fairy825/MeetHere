package com.meethere.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @description:
 * @author: ivy034
 * @create: 2019-12-12 21:01
 **/
public class DateUtils {
	public static Date formatDate(int date) throws ParseException {
		Date bookingDate = new Date();
		Calendar cal = Calendar.getInstance();
		cal.setTime(bookingDate);//设置起时间
		cal.add(Calendar.DATE, date);//增加一天  
		Date d = cal.getTime();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		String dateString = formatter.format(d);
		return formatter.parse(dateString);
	}
}
