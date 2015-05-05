package com.common.util.date;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.common.enums.RepeatEnums;

public class DateUtil {
    
    public static final String MONTH_DATE_FORMAT = "yyyy-MM";
    
    public static final String DAY_DATE_FORMAT = "yyyy-MM-dd";
    
    public static final String HOUR_DATE_FORMAT ="yyyy-MM-dd HH";
    
    public static final String MINUTE_DATE_FORMAT = "yyyy-MM-dd HH:mm";
    
    public static final String DEFAULT_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
    
    public static final String MILLIONS_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss:SSS";
    
    public static final int YEAR = Calendar.YEAR;//年份

    public static final int MONTH = Calendar.MONTH;//月份 从0开始

    public static final int DAY = Calendar.DATE;//天

    public static final int HOUR = Calendar.HOUR;//小时

    public static final int MINUTE = Calendar.MINUTE;//分钟

    public static final int DAY_OF_MONTH = Calendar.DAY_OF_MONTH;// 得到某月中的当前日期

    public static final int HOUR_OF_DAY = Calendar.HOUR_OF_DAY;// 得到某天的当前小时

    public static final int SECOND = Calendar.SECOND;// 得到秒钟
    
    public static final int MILLISECOND=Calendar.MILLISECOND;//毫秒
    
    public static final int DATE = Calendar.DATE;//获取日期的天

    public static final int DAY_OF_WEEK = Calendar.DAY_OF_WEEK;//获取星期中的第几天 1表示星期天 5表示星期六 以此类推
    
    public static final int WEEK_OF_YEAR = Calendar.WEEK_OF_YEAR;//获取一年的第几周
    
    public static final int WEEK_OF_MONTH = Calendar.WEEK_OF_MONTH;//获取一月中的第几周
    
    public static final int DAY_OF_WEEK_IN_MONTH = Calendar.DAY_OF_WEEK_IN_MONTH; 
    
    public static final int ZONE_OFFSET = Calendar.ZONE_OFFSET;//GMT时区偏移量
    
    /**
     * 根据Calendar参数获取日期相关部分
     * @param date	日期
     * @param field	属性
     * @return		日期相关部分
     */
    public static int getCutDate(Date date, int field){
	if(null == date){
	    throw new NullPointerException("date is null!");
	}
	Calendar calendar = Calendar.getInstance();
	calendar.setTime(date);
	return calendar.get(field);
    }
    
    /**
     * 获取日期的月份
     * @param date	日期
     * @return		月份
     */
    public static int getMonth(Date date){
	return getCutDate(date,MONTH)+1;
    }
    
    /**
     * 获取日期是星期几的数字描述
     * @param date	日期
     * @return		星期几
     */
    public static int getDayOfWeek(Date date){
	 int day = getCutDate(date,DAY_OF_WEEK);
	 if(day == 1){
	     return 7;
	 }
	 return day--;
    }
    
    /**
     * 获取日期是星期几的中文描述
     * @param date
     * @return
     */
    public static String getChinesDayOfWeek(Date date){
	String[] weeks = new String[]{"星期一","星期二","星期三","星期四","星期五","星期六","星期七"};
	return weeks[getDayOfWeek(date)-1];
    }
    
    /**
     * 构建日期  日期格式为：yyyy-MM-dd HH:mm:ss
     * @param year	年份
     * @param month	月份
     * @param day	天
     * @return		日期
     */
    public static Date setDate(int year,int month,int day){
	return setDate(year,month,day,0,0,0);
    }
    
    /**
     * 构建日期  日期格式为:yyyy-MM-dd HH:mm:ss
     * @param year	年份
     * @param month	月份
     * @param day	天
     * @param hour	小时
     * @param minute	分钟
     * @return		日期
     */
    public static Date setDate(int year,int month,int day,int hour,int minute){
	return setDate(year,month,day,hour,minute,0);
    }
    
    /**
     * 构建日期  日期格式为:yyyy-MM-dd HH:mm:ss
     * @param year	年份
     * @param month	月份
     * @param day	天
     * @param hour	小时
     * @param minute	分钟
     * @param second	秒
     * @return		日期
     */
    public static Date setDate(int year,int month,int day,int hour,int minute,int second){
	Calendar calendar = Calendar.getInstance();
	calendar.set(year, month, day, hour, minute, second);
	return calendar.getTime();
    }
    
    /**
     * 日期字符串转换为日期
     * @param dateStr	日期字符串
     * @return		日期
     */
    public static Date stringToDate(String dateStr){
	return stringFormatToDate(dateStr, DEFAULT_DATE_FORMAT);
    }
    
    /**
     * 日期转换为日期字符串
     * @param date	日期
     * @return		日期字符串
     */
    public static String dateToString(Date date){
	return dateFormatToString(date, DEFAULT_DATE_FORMAT);
    }
    
    /**
     * 日期字符串转换为日期 自定义格式
     * @param dateStr	日期字符串
     * @param dateFormat日期格式
     * @return		日期
     */
    public static Date stringFormatToDate(String dateStr,String dateFormat) {
	try {
	    SimpleDateFormat format = new SimpleDateFormat(dateFormat);
	    return format.parse(dateStr);
	} catch (ParseException e) {
	    e.printStackTrace();
	}
	return null;
    }
    
    /**
     * 日期转换为日期字符串 自定义格式
     * @param date	日期
     * @param dateFormat日期格式
     * @return		日期字符串
     */
    public static String dateFormatToString(Date date,String dateFormat){
	if(null == date || null == dateFormat || dateFormat.equals("")){
	    return null;
	}
	SimpleDateFormat format = new SimpleDateFormat(dateFormat);
	return format.format(date);
    }
    
    /**
     * 获取某个月的第一天  日期格式为:yyyy-MM-dd 00:00:00
     * @param date	日期对象
     * @return		日期
     */
    public static Date getFirstDayOfMonth(Date date){
	Calendar calendar = Calendar.getInstance();
	calendar.setTime(date);
	calendar.set(DAY_OF_MONTH, 1);
	calendar.set(HOUR, 0);
	calendar.set(MINUTE,0);
	calendar.set(SECOND, 0);
	return calendar.getTime();
    }
    
    /**
     * 获取某个月的最后一天  日期格式为:yyyy-MM-dd 00:00:00
     * @param date	日期
     * @return		日期
     */
    public static Date getLastDayOfMonth(Date date){
	date = add(date,MONTH,1);
	Calendar calendar = Calendar.getInstance();
	calendar.setTime(date);
	calendar.set(DAY_OF_MONTH,1);
	calendar.set(HOUR, 0);
	calendar.set(MINUTE,0);
	calendar.set(SECOND, 0);
	return add(calendar.getTime(),DAY_OF_MONTH,-1);
    }
    
    /**
     * 计算日期
     * @param date	日期
     * @param field	日期属性
     * @param value	日期值
     * @return		日期
     */
    public static Date add(Date date,int field,int value){
	Calendar calendar = Calendar.getInstance();
	calendar.setTime(date);
	calendar.add(field, value);
	return calendar.getTime();
    }
    
    /**
     * 判断第二个日期是否在第一个日期之后
     * @param date	日期
     * @param anotherDate日期
     * @return		结果
     */
    public static boolean after(Date date,Date anotherDate){
	return date.after(anotherDate);
    }
    
    /**
     * 判断第二个日期是否在第一个日期之前
     * @param date	日期
     * @param anotherDate日期
     * @return		结果
     */
    public static boolean before(Date date,Date anotherDate){
	return date.before(anotherDate);
    }
    
    /**
     * 是否是同一个星期
     * @param date	日期
     * @param anotherDate日期
     * @return		结果
     */
    public static boolean isSameWeek(Date date,Date anotherDate){
	int year=getCutDate(date,YEAR);
	int secondYear=getCutDate(anotherDate,YEAR);
	int week = getCutDate(date,WEEK_OF_YEAR);
	int secondWeek = getCutDate(date,WEEK_OF_YEAR);
	return week == secondWeek && year==secondYear;
    }
    
    /**
     * 是否是润年
     * @param year	年份
     * @return		结果
     */
    public static boolean isLeapYear(int year) {
	Calendar calendar = Calendar.getInstance();
	return ((GregorianCalendar) calendar).isLeapYear(year);
    }   
    
    /**
     * 是否是工作日
     * @param date	日期
     * @return		结果
     */
    public static boolean isWorkDay(Date date){
	Calendar calendar = Calendar.getInstance();
	calendar.setTime(date);
	if(calendar.get(DAY_OF_WEEK) == calendar.SUNDAY 
		||calendar.get(DAY_OF_WEEK) ==calendar.SATURDAY){
	    return true;
	}
	return false;
    }
    
    /**
     * 获取日期段
     * @param startDate	开始日期
     * @param endDate	结束日期
     * @return
     */
    public static List<HashMap<String, Object>> betweenDate(String startDate, String endDate) {
	ArrayList<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();
	try {
	    Calendar calendar = Calendar.getInstance();
	    Calendar anotherCalendar = Calendar.getInstance();
	    calendar.setTime(stringFormatToDate(startDate, DAY_DATE_FORMAT));
	    anotherCalendar.setTime(stringFormatToDate(endDate, DAY_DATE_FORMAT));

	    int monthnum = (anotherCalendar.get(Calendar.YEAR) - calendar.get(Calendar.YEAR))
		    * 12 + anotherCalendar.get(Calendar.MONTH) - calendar.get(Calendar.MONTH);
	    for (int i = 0; i < monthnum; i++) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("begindate", dateFormatToString(calendar.getTime(),DAY_DATE_FORMAT));
		calendar.add(Calendar.DATE, calendar.getActualMaximum(Calendar.DATE)
			- calendar.get(Calendar.DATE));
		map.put("enddate", dateFormatToString(calendar.getTime(),DAY_DATE_FORMAT));
		list.add(map);
		calendar.add(Calendar.MONTH, 1);
		calendar.add(Calendar.DATE, 1 - calendar.get(Calendar.DATE));
	    }
	    HashMap<String, Object> map = new HashMap<String, Object>();
	    map.put("begindate", dateFormatToString(calendar.getTime(),DAY_DATE_FORMAT));
	    map.put("enddate", endDate);
	    list.add(map);
	} catch (Exception e) {
	    return list;
	}
	return list;
    }
    
    /**
     * 获取某月所有日期
     * @param date	日期
     * @return		日期字符串列表
     */
    public static List<String> getMonthAllDate(Date date){
	String month = dateFormatToString(date, MONTH_DATE_FORMAT);
	List<String> dateList = new ArrayList<String>();
	Calendar calendar = Calendar.getInstance();
	calendar.setTime(date);
	int lastDay = calendar.getActualMaximum(DAY_OF_MONTH);
	for (int i = 1; i <= lastDay; i++) {
	    String time = month +"-"+i;
	    dateList.add(time);
	}
	return dateList;
    }
    
    /**
     * 毫秒数转换成天数
     */
    public static int millonToDay(long millions) {
	long day = millions / (1000 * 60 * 60 * 24);
	return new Double(Math.ceil(day)).intValue();
    }
    
    /**
     * 得到日期的交集范围
     * @param firstStartDate 	第一个开始的日期
     * @param firstEndtDate	第一个结束的日期
     * @param secondStartDate	第二个开始的日期
     * @param secondEndDate	第二个结束的日期
     * @return			交集范围
     */
    public static String[] getRangeDate(String firstStartDate,
	    String firstEndtDate, String secondStartDate, String secondEndDate) {
	String[] date = new String[2];
	String begindate = "";
	String enddate = "";
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	try {
	    Calendar begin1 = Calendar.getInstance();
	    Calendar end1 = Calendar.getInstance();
	    Calendar begin2 = Calendar.getInstance();
	    Calendar end2 = Calendar.getInstance();
	    begin1.setTime(sdf.parse(firstStartDate));
	    end1.setTime(sdf.parse(firstEndtDate));
	    begin2.setTime(sdf.parse(secondStartDate));
	    end2.setTime(sdf.parse(secondEndDate));
	    if ((begin2.getTime().getTime() >= end1.getTime().getTime() && end2
		    .getTime().getTime() >= end1.getTime().getTime())
		    || (begin2.getTime().getTime() <= begin1.getTime()
			    .getTime() && end2.getTime().getTime() <= begin1
			    .getTime().getTime())) {
		date[0] = "";
		return date;
	    }

	    if (begin2.getTime().getTime() >= begin1.getTime().getTime()) {
		begindate = sdf.format(begin2.getTime());
	    } else {
		begindate = sdf.format(begin1.getTime());
	    }
	    if (end2.getTime().getTime() >= end1.getTime().getTime()) {
		enddate = sdf.format(end1.getTime());
	    } else {
		enddate = sdf.format(end2.getTime());
	    }

	    if (!begindate.equals("") && !enddate.equals("")) {
		date[0] = begindate;
		date[1] = enddate;
	    }
	} catch (Exception e) {

	}
	return date;
    }
    
    /**
     * 分钟转换为小时数
     * @param min	分钟
     * @return		小时数
     */
    public static double miniute2Hour(long min) {
	return min / 60L + min % 60L / 60.0D;
    }
    
    /**
     * 转换日期为 yyyy-MM-dd 00:00:00:00
     * @param date
     * @return
     */
    public static Date transQueryFromDate(Date date) {
	Calendar calendar = GregorianCalendar.getInstance();
	calendar.setTime(date);
	calendar.set(11, 0);
	calendar.set(12, 0);
	calendar.set(13, 0);
	calendar.set(14, 0);
	return calendar.getTime();
    }
    
    /**
     * 转换日期为yyyy-MM-dd 23:59:59:500
     * @param date
     * @return
     */
    public static Date transQueryToDate(Date date) {
	Calendar calendar = GregorianCalendar.getInstance();
	calendar.setTime(date);
	calendar.set(11, 23);
	calendar.set(12, 59);
	calendar.set(13, 59);
	calendar.set(14, 500);
	return calendar.getTime();
    }
    
    /**
     * 转换日期为Calendar
     * @param date
     * @return
     */
    public static Calendar transDateToCalendar(Date date) {
	Calendar calendar = GregorianCalendar.getInstance();
	calendar.setTime(date);
	return calendar;
    }
    
    /**
     * 获取下一个调度日期
     * @param currentScheduleDate	开始调度日期
     * @param repeatType		枚举类型
     * @return				日期
     */
    public static Date getNextScheduleDate(Date currentScheduleDate,
	    RepeatEnums repeatType) {
	Date now = new Date();
	Calendar nextScheduleCalendar = Calendar.getInstance();
	if (currentScheduleDate != null)
	    nextScheduleCalendar.setTime(currentScheduleDate);
	Date nextScheduleDate = nextScheduleCalendar.getTime();

	if (nextScheduleDate.before(now)) {
	    while (nextScheduleDate.before(now)) {
		if ((repeatType == null) || (repeatType == RepeatEnums.ONCE))
		    return now;
		if (repeatType == RepeatEnums.HOURLY) {
		    Date lastDate = addMinute(now, -60);
		    if (nextScheduleDate.before(lastDate)) {
			return now;
		    }
		    nextScheduleDate = addMinute(nextScheduleDate, 60);
		} else if (repeatType == RepeatEnums.DAILY) {
		    Date lastDate = addDay(now, -1);
		    if (nextScheduleDate.before(lastDate)) {
			return now;
		    }
		    nextScheduleDate = addDay(nextScheduleDate, 1);
		} else if (repeatType == RepeatEnums.WEEKLY) {
		    Date lastDate = addWeek(now, -1);
		    if (nextScheduleDate.before(lastDate)) {
			return now;
		    }
		    nextScheduleDate = addWeek(nextScheduleDate, 1);
		} else if (repeatType == RepeatEnums.MONTHLY) {
		    Date lastDate = addMonth(now, -1);
		    if (nextScheduleDate.before(lastDate)) {
			return now;
		    }
		    nextScheduleDate = addMonth(nextScheduleDate, 1);
		} else if (repeatType == RepeatEnums.YEARLY) {
		    Date lastDate = addYear(now, -1);
		    if (nextScheduleDate.before(lastDate)) {
			return now;
		    }
		    nextScheduleDate = addYear(nextScheduleDate, 1);
		}
	    }
	}
	return nextScheduleDate;
    }
    
    /**
     * 获取下一个工作日
     * @param dt	日期
     * @param amount	星期几
     * @return
     */
    public static Date getNextWorkingDay(Date dt, int amount) {
	if (amount == 0)
	    return dt;
	Calendar calendar = transDateToCalendar(dt);
	calendar = transDateToCalendar(dt);
	int inteval;
	if (amount > 0)
	    inteval = 1;
	else {
	    inteval = -1;
	}
	for (int i = 0; i < Math.abs(amount);) {
	    calendar.add(7, inteval);
	    int weekDay = calendar.get(7);
	    if ((weekDay != 7) && (weekDay != 1))
		i++;
	}
	return calendar.getTime();
    }
    
    /**
     * 增加天数
     * @param date
     * @param days
     * @return
     */
    public static Date addDay(Date date, int days) {
	Calendar gc = Calendar.getInstance();
	gc.setTime(date);
	gc.add(5, days);
	return gc.getTime();
    }
    
    /**
     * 添加月数
     * @param date
     * @param months
     * @return
     */
    public static Date addMonth(Date date, int months) {
	Calendar gc = Calendar.getInstance();
	gc.setTime(date);
	gc.add(2, months);
	return gc.getTime();
    }
    
    /**
     * 添加星期
     * @param date
     * @param weeks
     * @return
     */
    public static Date addWeek(Date date, int weeks) {
	Calendar gc = Calendar.getInstance();
	gc.setTime(date);
	gc.add(3, weeks);
	return gc.getTime();
    }
    
    /**
     * 添加年数
     * @param date
     * @param years
     * @return
     */
    public static Date addYear(Date date, int years) {
	Calendar gc = Calendar.getInstance();
	gc.setTime(date);
	gc.add(1, years);
	return gc.getTime();
    }
    
    /**
     * 添加分钟
     * @param date
     * @param minutes
     * @return
     */
    public static Date addMinute(Date date, int minutes) {
	Calendar gc = Calendar.getInstance();
	gc.setTime(date);
	gc.add(12, minutes);
	return gc.getTime();
    }
    
    /**
     * 添加小时
     * @param date
     * @param hours
     * @return
     */
    public static Date addHour(Date date, int hours) {
	Calendar gc = Calendar.getInstance();
	gc.setTime(date);
	gc.add(10, hours);
	return gc.getTime();
    }
    
    /**
     * 是否是日期
     * @param dateString 日期字符串
     * @return		  结果
     */
    public static boolean isDate(String dateString) {
	if (stringToDate(dateString) != null) {
	    try {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		dateFormat.setLenient(false);

		return dateFormat.parse(dateString) != null;
	    } catch (Exception e) {
		return false;
	    }
	}

	return false;
    }
    
    /**
     * 是否是日期
     * @param dateTimeString	日期字符串
     * @return			结果
     */
    public static boolean isDateTime(String dateTimeString) {
	if (stringToDate(dateTimeString) != null) {
	    try {
		SimpleDateFormat dateFormat = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss");
		dateFormat.setLenient(false);

		return dateFormat.parse(dateTimeString) != null;
	    } catch (Exception e) {
		return false;
	    }
	}

	return false;
    }
    
    /**
     * 获取最大的日期
     * @param dateArray	日期数组
     * @return		最大的日期
     */
    public static Date max(Date[] dateArray) {
	if ((dateArray == null) || (dateArray.length == 0)) {
	    return null;
	}
	Date maxDate = stringToDate("1900-01-01");

	for (int i = 0; i < dateArray.length; i++) {
	    if ((dateArray[i] != null) && (maxDate.before(dateArray[i]))) {
		maxDate = dateArray[i];
	    }
	}
	return maxDate;
    }
    
    /**
     * 获取最小的日期
     * @param dateArray	日期数组
     * @return		最小的日期
     */
    public static Date min(Date[] dateArray) {
	if ((dateArray == null) || (dateArray.length == 0)) {
	    return null;
	}
	Date minDate = dateArray[0];
	for (int i = 0; i < dateArray.length; i++) {
	    if ((dateArray[i] != null) && (minDate.after(dateArray[i]))) {
		minDate = dateArray[i];
	    }
	}
	return minDate;
    }
    
    public static String calendarToString(Calendar calendar){
	return dateToString(calendar.getTime());
    }
    
    public static void main(String[] args) {
	Date now = new Date();
	System.out.println(getDayOfWeek(new Date()));
	System.out.println(setDate(2014,5,3));
	System.out.println(getCutDate(now,DAY_OF_MONTH));
	System.out.println(getFirstDayOfMonth(now));
	System.out.println(dateToString(add(now,MONTH,-1)));
	System.out.println(dateToString(getLastDayOfMonth(now)));
	System.out.println(isSameWeek(now,add(now,YEAR,-1)));
	System.out.println(isWorkDay(now));
	
	List<HashMap<String,Object>> list = betweenDate("2015-01-01","2015-04-17");
	for (int i = 0; i < list.size(); i++) {
	    HashMap<String,Object> hashMap = list.get(i);
	    Set<Map.Entry<String, Object>> set = hashMap.entrySet();
	    Iterator<Map.Entry<String, Object>> iter = set.iterator();
	    while(iter.hasNext()){
		Map.Entry<String, Object> map=iter.next();
		System.out.println(map.getKey());
		System.out.println(map.getValue());
	    }
	}
	
	System.out.println("某月所有日期开始....");
	List<String> dateList = getMonthAllDate(new Date());
	for(String date:dateList){
	    System.out.println(date);
	}
	
    }
    
}
