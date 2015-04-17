package com.common.util.date;

import java.text.DateFormat;
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
import java.util.Map.Entry;

public class DateUtil {
    
    private static final String MONTH_DATE_FORMAT = "yyyy-MM";
    
    private static final String DAY_DATE_FORMAT = "yyyy-MM-dd";
    
    private static final String HOUR_DATE_FORMAT ="yyyy-MM-dd HH";
    
    private static final String MINUTE_DATE_FORMAT = "yyyy-MM-dd HH:mm";
    
    private static final String DEFAULT_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
    
    private static final String MILLIONS_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss:SSS";
    
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
    
    public static boolean isNoramlDateFormat(String dateStr){
	
	return true;
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
