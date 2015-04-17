package com.common.util.generic;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import org.apache.commons.lang.time.DateUtils;
import org.apache.commons.lang.time.FastDateFormat;

import com.common.enums.RepeatEnums;
import com.common.util.string.StringUtil;

/**
 * 
 * 类的描述:日期辅助类
 * 创建时间:2015-4-17
 * 创建人:邹建华
 */
public class DateUtil {
    
    /**
     * YYYY-MM-DD
     */
    public static final String SIMPLE_DATE_FORMAT = "yyyy-MM-dd";
    
    /**
     * YYYY-MM-DD HH:MM:SS
     */
    public static final String DEFAULT_DATE_FORMAT ="yyyy-MM-dd HH:mm:ss";
    
//    public static final String 
    
    
    public static final String DAY_HOURS_FORMAT = "D H";
    private static final FastDateFormat simpleDateFormatter = FastDateFormat
	    .getInstance("yyyy-MM-dd");
    
    private static final FastDateFormat timeStampFormatter = FastDateFormat
	    .getInstance("yyyyMMddHHmmssSSS");

    private static final String[] DEFAULT_DATE_PATTERNS = {
	    "yyyy-MM-dd'T'HH:mm:ss.SSS", "yyyy-MM-dd'T'HH:mm:ss",
	    "yyyy-MM-dd H:mm:ss", "yyyy-M-d H:mm:ss", "yyyy-M-d H:mm",
	    "yyyy-MM-dd'T'H:mm:ss.SSS", "yyyy-MM-dd H:mm:ss.SSS", "yyyy-M-d H",
	    "yyyy M d H", "yyyy-M-d", "y-M-d", "yyyy M d", "yyyyMMddHHmmss",
	    "yyyyMMddHHmm", "yyyyMMddHH", "yyyyMMdd", "yyyyMMdd HH",
	    "yyyy-MM-dd HH", "yyyy-MM-dd HH:mm" };

    private static final int[] MAX_DAY_OF_MONTH = new int[] { 31, 28, 31, 30,
	    31, 30, 31, 31, 30, 31, 30, 31 };

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

    public static void getCalendarInfo(Date date){
	Calendar calendar = Calendar.getInstance();
	calendar.setTime(date);
	System.out.println("YEAR:"+calendar.get(YEAR));
	System.out.println("MONTH:"+calendar.get(MONTH));
	System.out.println("DAY:"+calendar.get(DAY));
	System.out.println("HOUR:"+calendar.get(HOUR));
	System.out.println("MINUTE:"+calendar.get(MINUTE));
	System.out.println("DAY_OF_MONTH:"+calendar.get(DAY_OF_MONTH));
	System.out.println("HOUR_OF_DAY:"+calendar.get(HOUR_OF_DAY));
	System.out.println("SECOND:"+calendar.get(SECOND));
	System.out.println("MILLISECOND:"+calendar.get(MILLISECOND));
	System.out.println("DAY_OF_WEEK:"+calendar.get(DAY_OF_WEEK));
	System.out.println("WEEK_OF_YEAR:"+calendar.get(WEEK_OF_YEAR));
	System.out.println("WEEK_OF_MONTH:"+calendar.get(WEEK_OF_MONTH));
	System.out.println("DATE:"+calendar.get(DATE));
	System.out.println("DAY_OF_WEEK_IN_MONTH:"+calendar.get(DAY_OF_WEEK_IN_MONTH));
	System.out.println("ZONE_OFFSET:"+calendar.get(ZONE_OFFSET));
    }
    

    /**
     * 比较两日期对象中的小时和分钟部分的大小
     * 如果输入时间为空 会启用与当前时间比较
     * @param date		时间
     * @param anotherDate	时间
     * @return			2:表示参数为空 1表示第一个日期大  0表示相等 -1表示第二个日期大
     */
    public static int compareHourAndMinute(Date date, Date anotherDate) {
	if(null == date || null == anotherDate){
	    return 2;
	}
	
	Calendar time = Calendar.getInstance();
	time.setTime(date);
	int timeHour = time.get(HOUR);
	int timeMinute = time.get(MINUTE);
	
	Calendar anotherTime = Calendar.getInstance();
	anotherTime.setTime(anotherDate);
	int anotherHour = anotherTime.get(HOUR);
	int anotherMinute = anotherTime.get(MINUTE);
	
	if(timeHour > anotherHour){
	    return 1;
	}else if(timeHour == anotherHour){
	    if(timeMinute == anotherMinute){
		return 0;
	    }else if(timeMinute > anotherMinute){
		return 1;
	    }
	}
	return -1;
	
    }

    /**
     * 比较两日期对象的大小,忽略秒,只精确到分钟
     * @param date		时间
     * @param anotherDate	时间	
     * @return			2:表示参数为空 1表示第一个日期大  0表示相等 -1表示第二个日期大
     */
    public static int compareIgnoreSecond(Date date, Date anotherDate) {
	if(null == date || null == anotherDate){
	    return 2;
	}

	Calendar calendar = Calendar.getInstance();
	calendar.setTime(date);
	calendar.set(SECOND, 0);
	calendar.set(MILLISECOND, 0);
	date = calendar.getTime();

	calendar.setTime(anotherDate);
	calendar.set(SECOND, 0);
	calendar.set(MILLISECOND, 0);
	anotherDate = calendar.getTime();

	return date.compareTo(anotherDate);
    }

    /**
     * 取得今天的最后一个时刻
     * @return 当前日期 23:59:59
     */
    public static Date currentEndDate() {
	return getEndDate(new Date());
    }

    /**
     * 取得今天的第一个时刻
     * @return 当前日期 00:00:00
     */
    public static Date currentStartDate() {
	return getStartDate(new Date());
    }

    /**
     * 获取某天的起始时间 精确到毫秒 00:00:00:000
     * @param date	时间
     * @return		当前日期 00:00:00:000
     */
    public static Date getStartDate(Date date) {
	if (date == null) {
	    return null;
	}

	Calendar cal = Calendar.getInstance();
	cal.setTime(date);

	cal.set(Calendar.HOUR_OF_DAY, 0);
	cal.set(Calendar.MINUTE, 0);
	cal.set(Calendar.SECOND, 0);
	cal.set(Calendar.MILLISECOND, 0);

	return cal.getTime();
    }

    /**
     *  根据指定的年,月,日等参数获取日期对象
     * @param year	年份
     * @param month	月份
     * @param date	天
     * @return		日期
     */
    public static Date getDate(int year, int month, int date) {
	return getDate(year, month, date, 0, 0);
    }

    /**
     * 根据指定的年,月,日,时,分等参数获取日期对象
     * @param year	年份
     * @param month	月份
     * @param date	天
     * @param hourOfDay	时
     * @param minute	分
     * @return		日期
     */
    public static Date getDate(int year, int month, int date, int hourOfDay,
	    int minute) {
	return getDate(year, month, date, hourOfDay, minute, 0);
    }

    /**
     * 根据指定的年,月,日,时,分,秒等参数获取日期对象
     * @param year	年份
     * @param month	月份
     * @param date	天
     * @param hourOfDay	时
     * @param minute	分
     * @parm  second    秒
     * @return		日期
     */
    public static Date getDate(int year, int month, int date, int hourOfDay,
	    int minute, int second) {
	Calendar cal = Calendar.getInstance();
	cal.set(year, month - 1, date, hourOfDay, minute, second);
	cal.set(Calendar.MILLISECOND, 0);

	return cal.getTime();
    }

    /**
     *  获取某天的结束时间 2013-12-12 23:59:59:999 精确到毫秒
     * @param date	时间
     * @return		日期
     */
    public static Date getEndDate(Date date) {

	if (date == null) {
	    return null;
	}

	Calendar cal = Calendar.getInstance();
	cal.setTime(date);

	cal.set(Calendar.HOUR_OF_DAY, 23);
	cal.set(Calendar.MINUTE, 59);
	cal.set(Calendar.SECOND, 59);
	cal.set(Calendar.MILLISECOND, 999);

	return cal.getTime();
    }

    /**
     * 获取一个月最后一天的日期
     * @param year	年份
     * @param month	月份  0表示1月
     * @return		日期
     */
    public static int getMaxDayOfMonth(int year, int month) {
	if (month == 1 && isLeapYear(year)) {
	    return 29;
	}
	return MAX_DAY_OF_MONTH[month];
    }

    /**
     * 判断是否是闰年
     * @param year	年份
     * @return		结果
     */
    

    /**
     * 取得一年中的第几周
     * @param date	日期
     * @return		周
     */
    public static int getWeekOfYear(Date date) {
	Calendar cal = Calendar.getInstance();
	cal.setTime(date);
	return cal.get(WEEK_OF_YEAR);
    }

    /**
     * 获取上周的指定星期的日期
     * @param dayOfWeek	1-7之间的数字
     * @return		日期
     */
    public static Date getDateOfPreviousWeek(int dayOfWeek) {
	if (dayOfWeek > 7 || dayOfWeek < 1) {
	    throw new IllegalArgumentException("参数必须是1-7之间的数字");
	}

	return getDateOfRange(dayOfWeek, -7);
    }

    /**
     * 获取本周的指定星期的日期
     * @param dayOfWeek 1-7之间的数字
     * @return		日期
     */
    public static Date getDateOfCurrentWeek(int dayOfWeek) {
	if (dayOfWeek > 7 || dayOfWeek < 1) {
	    throw new IllegalArgumentException("参数必须是1-7之间的数字");
	}

	return getDateOfRange(dayOfWeek, 0);
    }

    /**
     * 获取下周的指定星期的日期
     * @param dayOfWeek	1-7之间的数字
     * @return		日期
     */
    public static Date getDateOfNextWeek(int dayOfWeek) {
	if (dayOfWeek > 7 || dayOfWeek < 1) {
	    throw new IllegalArgumentException("参数必须是1-7之间的数字");
	}

	return getDateOfRange(dayOfWeek, 7);
    }
    
    private static Date getDateOfRange(int dayOfWeek, int dayOfRange) {
	Calendar calendar = Calendar.getInstance();
	calendar.set(DAY_OF_WEEK, dayOfWeek);
	calendar.set(DATE, calendar.get(DATE) + dayOfRange + 1);
	calendar.set(HOUR_OF_DAY, 0);
	calendar.set(MINUTE, 0);
	calendar.set(SECOND, 0);
	calendar.set(MILLISECOND, 0);
	return calendar.getTime();
    }

    /**
     * 返回年份
     * @param date 日期
     * @return	        年份
     */
    public static int getYear(Date date) {
	Calendar calendar = Calendar.getInstance();
	calendar.setTime(date);
	return calendar.get(YEAR);
    }

    /**
     * 返回月份
     * @param date	日期
     * @return		月份
     */
    public static int getMonth(Date date) {
	Calendar calendar = Calendar.getInstance();
	calendar.setTime(date);
	return calendar.get(MONTH) + 1;
    }

    /**
     * 返回日
     * @param date	日期
     * @return		日
     */
    public static int getDay(Date date) {
	Calendar calendar = Calendar.getInstance();
	calendar.setTime(date);
	return calendar.get(DAY_OF_MONTH);
    }

    /**
     * 返回小时
     * @param date	日期
     * @return		小时
     */
    public static int getHour(Date date) {
	Calendar calendar = Calendar.getInstance();
	calendar.setTime(date);
	return calendar.get(HOUR_OF_DAY);
    }

    /**
     * 返回分钟
     * @param date	日期
     * @return		分钟
     */
    public static int getMinute(Date date) {
	Calendar calendar = Calendar.getInstance();
	calendar.setTime(date);
	return calendar.get(MINUTE);
    }

    /**
     * 返回秒钟
     * @param date	日期
     * @return		秒钟
     */
    public static int getSecond(Date date) {
	Calendar calendar = Calendar.getInstance();
	calendar.setTime(date);
	return calendar.get(SECOND);
    }

    /**
     * 返回毫秒
     * @param date	日期
     * @return		毫秒
     */
    public static long getMillis(Date date) {
	Calendar calendar = Calendar.getInstance();
	calendar.setTime(date);
	return calendar.getTimeInMillis();
    }

    /**
     * 添加天数返回日期
     * @param date 日期
     * @param day  天数
     * @return	        日期
     */
    public static Date addDate(Date date, int day) {
	Calendar calendar = Calendar.getInstance();
	long millis = getMillis(date) + ((long) day) * 24 * 3600 * 1000;
	calendar.setTimeInMillis(millis);
	return calendar.getTime();
    }

    /**
     * 日期相减返回天数
     * @param date	日期
     * @param date1	日期
     * @return		天数
     */
    

    /**
     * 得到参数时间的这个星期的第一天的日期
     * @param date	日期
     * @return		日期
     */
    public static Date getFirstDateByWeek(Date date) {
	Calendar now = Calendar.getInstance();
	now.setTime(date);
	int today = now.get(DAY_OF_WEEK);
	int first_day_of_week = now.get(DAY) + 2 - today;
	now.set(DAY, first_day_of_week);
	return now.getTime();
    }

    /**
     * 得到参数时间的这个星期的最后一天的日期
     * @param date	日期
     * @return		日期
     */
    public static Date getLastDateByWeek(Date date) {

	Calendar now = Calendar.getInstance();
	now.setTime(date);
	int today = now.get(DAY_OF_WEEK);
	int first_day_of_week = now.get(DAY) + 2 - today;
	int last_day_of_week = first_day_of_week + 6;
	now.set(now.DATE, last_day_of_week);
	return now.getTime();
    }

    /**
     * 得到当前日期(24小时制)
     * @return
     */
    public static String getCurrentTime() {
	DateFormat format = new SimpleDateFormat(DEFAULT_DATE_FORMAT);
	return format.format(new Date());
    }

    /**
     * 转换日期类型到String类型(时间格式为formatLayout)
     * @param date 	日期
     * @return		日期字符串
     */
    public static String dateToString(Date date) {
	return dateToString(date, DEFAULT_DATE_FORMAT);
    }

    /**
     * 转换日期类型到String类型 自定义时间格式
     * @param date		日期
     * @param formatLayout	日期格式
     * @return			日期字符串
     */
    public static String dateToString(Date date, String formatLayout) {
	if (null == date) {
	    return "";
	} else {
	    DateFormat format = new SimpleDateFormat(formatLayout);
	    return format.format(date);
	}
    }

    /**
     * 转换String类型到日期类型(时间格式为formatLayout)
     * @param dateString	日期字符串
     * @return			日期
     * @throws ParseException
     */
    public static Date stringToDate(String dateStr) {
	return stringToDate(dateStr, DEFAULT_DATE_FORMAT);
    }

    /**
     * 转换String类型到日期类型 自定义时间格式
     * @param dateStr		日期字符串
     * @param formatLayout	日期格式
     * @return			日期
     * @throws ParseException
     */
    public static Date stringToDate(String dateStr, String formatLayout) {
	Date date = null;
	try {
	    if (!StringUtil.isNullOrEmpty(dateStr)) {
		DateFormat format = new SimpleDateFormat(formatLayout);
		date = format.parse(dateStr);
	    }
	} catch (Exception e) {
	    e.printStackTrace();
	}
	return date;
    }

    /**
     * 在给定时间上添加或减去天数 月数 和年数
     * @param day	天数
     * @param dateType	日期类型
     * @param date	日期
     * @return		日期
     */
    public static Date addDate(int day, int dateType, Date date) {
	Calendar calendar = Calendar.getInstance();
	if (null != date) {
	    calendar.setTime(date);
	}
	calendar.add(dateType, day);
	return calendar.getTime();
    }

    /**
     * Date 转换格式
     * @param date	日期
     * @return		日期
     */
    public static Date dateToDate(Date date) {
	return dateToDate(date, DEFAULT_DATE_FORMAT);
    }

    /**
     * Date 转换格式
     * @param date		日期
     * @param formatLayout	日期格式
     * @return			日期
     */
    public static Date dateToDate(Date date, String formatLayout) {
	try {
	    DateFormat format = new SimpleDateFormat(formatLayout);
	    if (null != date) {
		date = format.parse(format.format(date));
	    }
	} catch (Exception e) {
	    e.printStackTrace();
	}
	return date;
    }

    /**
     * 比较输入日期是否在当前日期的后面
     * 
     * @param date 日期
     * @return	        结果
     */
    public static boolean afterDate(Date date) {
	return new Date().after(date);
    }

    /**
     * 比较输入日期是否在当前日期的前面
     * 
     * @param date	日期
     * @return		结果
     */
    public static boolean beforeDate(Date date) {
	return new Date().before(date);
    }

   /**
    * 比较第一个日期是否比第二个日期大
    * @param date	日期
    * @param anotherDate日期
    * @return		结果
    */
    public static boolean whoBig(Date date, Date anotherDate) {
	boolean result = false;
	if (null != date && null == anotherDate) {
	    result = true;
	} else if (null != date && null != anotherDate) {
	    if (date.getTime() > anotherDate.getTime()) {
		return true;
	    }
	}
	return result;
    }

    /**
     * 获取明天的日期
     * @return 日期字符串
     */
    public static String nextDay() {
	return nextDay(new Date(), DEFAULT_DATE_FORMAT);
    }

    /**
     * 得到明天的日期
     * @param dateStr 		日期字符串
     * @param formatLayout	日期格式
     * @return			日期字符串
     */
    public static String nextDay(String dateStr, String formatLayout) {
	if (StringUtil.isNullOrEmpty(dateStr)) {
	    return null;
	}
	return nextDay(stringToDate(dateStr, formatLayout), formatLayout);
    }

    /**
     * 得到明天的日期
     * @param date		日期
     * @param formatLayout	日期格式
     * @return			日期字符串
     */
    public static String nextDay(Date date, String formatLayout) {
	if (date == null) {
	    return "";
	}
	Date nextDay = new Date(date.getTime() + 1000 * 60 * 60 * 24);
	return dateToString(nextDay, formatLayout);
    }

    /**
     * 获取两个日期之间的相隔天数  
     * @param date		日期
     * @param anotherDate	日期
     * @return			天数  -1表示参数为NULL
     */
    

    /**
     * 得到两个字符串之间的相隔天数
     * @param dateStr		日期字符串
     * @param anoterDateStr	日期字符串
     * @return			天数 
     */
    public static int compareToDay(String dateStr, String anoterDateStr) {
	Date firstDate = stringToDate(dateStr, "yyyy-MM-dd");
	Date secondDate = stringToDate(anoterDateStr, "yyyy-MM-dd");
	return compareToDay(firstDate, secondDate);
    }

   /**
    * 返回两个日期之间隔了多少分钟
    * @param firstStr
    * @param secondStr
    * @return
    */
    public static long compareToMinute(String firstStr, String secondStr) {
	try {
	    Date firstDate = stringToDate(firstStr);
	    Date secondDate = stringToDate(secondStr);
	    return Math.abs((firstDate.getTime() - secondDate.getTime())
		    / (60 * 1000));
	} catch (Exception e) {
	    return 0;
	}
    }

    public static String stringToString(String dateStr, String formatLayout) {
	Date date = stringToDate(dateStr, formatLayout);
	return dateToString(date);
    }

    public static String stringToString(String dateStr) {
	return stringToString(dateStr, DEFAULT_FORMAT);
    }

    /**
     * 只计算工作日
     * 
     * @param firstStr
     * @param secondStr
     * @param isOnlyWorkDay
     * @return
     */
    public static int compareToDay(String firstStr, String secondStr,
	    boolean isOnlyWorkDay) {
	int days = 0;
	String currentDay = "";
	if (!isOnlyWorkDay) {
	    return compareToDay(firstStr, secondStr);
	} else {
	    String formatLayout = "yyyy-MM-dd";
	    secondStr = stringToString(secondStr, formatLayout);
	    secondStr = secondStr.substring(0, secondStr.indexOf(" "));
	    currentDay = stringToString(firstStr, formatLayout);
	    while (!secondStr.equals(currentDay)) {
		currentDay = nextDay(currentDay, formatLayout);
		if (isWorkDay(stringToDate(currentDay, formatLayout))) {
		    days++;
		}
	    }
	}
	return days;
    }

    /**
     * 是否是工作日
     * 
     * @param date
     * @return
     */
    public static boolean isWorkDay(Date date) {
	Calendar calendar = Calendar.getInstance();
	calendar.setTime(date);
	int week = calendar.get(Calendar.DAY_OF_WEEK);
	if (week == Calendar.SUNDAY || week == Calendar.SATURDAY) {
	    return false;
	} else {
	    return true;
	}
    }

    /**
     * 毫秒数转换成天数
     */
    public static int millonToDay(long millions) {
	long day = millions / (1000 * 60 * 60 * 24);
	return new Double(Math.ceil(day)).intValue();
    }

    /**
     * 返回输入日期是星期几
     * 
     * @param args
     */
    public static String getWeek(Date date) {
	String[] weeks = { "星期一", "星期二", "星期三", "星期四", "星期五", "星期六", "星期天" };
	Calendar calendar = Calendar.getInstance();
	calendar.setTime(date);
	int week = calendar.get(Calendar.DAY_OF_WEEK) - 1;
	if (week < 0) {
	    week = 0;
	}
	return weeks[week - 1];
    }

    /**
     * 获取明天00:00:00
     * 
     * @param args
     */
    public static String nextDayToPoint() {
	return nextDayToPoint(new Date());
    }

    /**
     * 得到明天
     * 
     * @param date
     * @return
     */
    public static Date nextDayToDate(Date date, String formatLayout) {
	String nextDay = nextDay(date, formatLayout);
	return stringToDate(nextDay, formatLayout);
    }

    /**
     * 得到明天的零点时刻
     * 
     * @param date
     * @return
     */
    public static String nextDayToPoint(Date date) {
	date = nextDayToDate(date, DEFAULT_FORMAT);
	DateFormat format = new SimpleDateFormat("yyyy-MM-dd 00:00:00");
	return format.format(date);
    }

    /**
     * 得到某一天是星期几
     * 
     * @param strDate
     *            日期字符串
     * @return int 星期几（-1异常）
     */
    public static String stringToWeek(String strDate) {
	DateFormat df = DateFormat.getDateInstance();
	try {
	    String[] week = { "星期一", "星期二", "星期三", "星期四", "星期五", "星期六", "星期天" };
	    df.parse(strDate);
	    java.util.Calendar c = df.getCalendar();
	    int day = c.get(Calendar.DAY_OF_WEEK) - Calendar.SUNDAY;
	    return week[day - 1];
	} catch (ParseException e) {
	    e.printStackTrace();
	}
	return "";
    }

    /**
     * 得到日期的交集范围
     * 
     * @param firstStartDate
     * @param firstEndtDate
     * @param secondStartDate
     * @param secondEndDate
     * @return
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
     * 得到日期段
     * 
     * @param d1
     * @param d2
     * @return
     */
    

    /**
     * 获取某月所有日期
     * 
     * @param date
     * @return
     * @throws ParseException
     */
    

    public static Calendar getCalendarWithoutHourMinSec(Date date) {
	Calendar cal = Calendar.getInstance();
	cal.setTime(date);
	cal.set(11, 0);
	cal.set(12, 0);
	cal.set(13, 0);
	cal.set(14, 0);
	return cal;
    }

    public static String getComplexDateString(Calendar calendar) {
	return complexDateFormatter.format(calendar.getTime());
    }

    public static String getComplexDateString(Date date) {
	return complexDateFormatter.format(date);
    }

    public static String getComplexDateString(Date date, Locale localeType) {
	if (date == null)
	    return "";
	FastDateFormat dateTimeFormat = FastDateFormat.getDateTimeInstance(2,
		2, localeType);
	return dateTimeFormat.format(date);
    }

    public static String getDateString(Calendar calendar, String dateFormat) {
	String format = dateFormat;
	if (StringUtil.isNullOrEmpty(dateFormat)) {
	    format = "yyyy-MM-dd";
	}
	FastDateFormat dateFormatter = FastDateFormat.getInstance(format);

	return dateFormatter.format(calendar.getTime());
    }

    public static String getDateString(Calendar calendar, String dateFormat,
	    Locale locale) {
	String format = dateFormat;
	if (StringUtil.isNullOrEmpty(dateFormat)) {
	    format = "yyyy-MM-dd";
	}
	FastDateFormat dateFormatter = FastDateFormat.getInstance(format,
		locale);

	return dateFormatter.format(calendar.getTime());
    }

    public static String getDateString(Date date, String dateFormat) {
	String format = dateFormat;
	if (StringUtil.isNullOrEmpty(dateFormat)) {
	    format = "yyyy-MM-dd";
	}
	FastDateFormat dateFormatter = FastDateFormat.getInstance(format);

	return dateFormatter.format(date);
    }

    public static String getDateString(Date date, String dateFormat,
	    Locale locale) {
	String format = dateFormat;
	if (StringUtil.isNullOrEmpty(dateFormat)) {
	    format = "yyyy-MM-dd";
	}
	FastDateFormat dateFormatter = FastDateFormat.getInstance(format,
		locale);

	return dateFormatter.format(date);
    }

    public static String getDayHourString(BigDecimal hours) {
	int DAY_HOURS = 24;
	StringBuilder sb = new StringBuilder();
	if (hours.setScale(0, 3).intValue() < 24) {
	    sb.append(hours);
	    sb.append("H");
	} else {
	    int days = hours.setScale(0, 3).intValue() / 24;
	    BigDecimal leftHours = hours.subtract(new BigDecimal(24 * days));
	    sb.append(days);
	    sb.append("D ");
	    sb.append(leftHours);
	    sb.append("H");
	}
	return sb.toString();
    }

    public static Date getMiddleNightDate(Date dt) {
	Calendar calendar = transDateToCalendar(dt);

	calendar.set(11, 0);
	calendar.set(12, 0);
	calendar.set(13, 0);
	calendar.set(14, 0);
	return calendar.getTime();
    }

    public static Date getFirstDayOfMonth(Date dt) {
	Calendar calendar = transDateToCalendar(dt);
	calendar.set(5, calendar.getActualMinimum(5));
	calendar.set(11, 0);
	calendar.set(12, 0);
	calendar.set(13, 0);
	calendar.set(14, 0);
	return calendar.getTime();
    }

    public static Date getLastDayOfMonth(Date dt) {
	Calendar calendar = transDateToCalendar(dt);
	calendar.set(5, calendar.getActualMaximum(5));
	calendar.set(11, 23);
	calendar.set(12, 59);
	calendar.set(13, 59);
	calendar.set(14, 999);
	return calendar.getTime();
    }

    public static Date getOverDateTime(Date date, int days) {
	return addDay(date, days);
    }

    public static Calendar getNewCalendar(Calendar calendar) {
	Calendar cal = Calendar.getInstance(calendar.getTimeZone());
	cal.setTime(calendar.getTime());
	return cal;
    }

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

    public static Date addDay(Date date, int days) {
	Calendar gc = Calendar.getInstance();
	gc.setTime(date);
	gc.add(5, days);
	return gc.getTime();
    }

    public static Date addMonth(Date date, int months) {
	Calendar gc = Calendar.getInstance();
	gc.setTime(date);
	gc.add(2, months);
	return gc.getTime();
    }

    public static Date addWeek(Date date, int weeks) {
	Calendar gc = Calendar.getInstance();
	gc.setTime(date);
	gc.add(3, weeks);
	return gc.getTime();
    }

    public static Date addYear(Date date, int years) {
	Calendar gc = Calendar.getInstance();
	gc.setTime(date);
	gc.add(1, years);
	return gc.getTime();
    }

    public static Date addMinute(Date date, int minutes) {
	Calendar gc = Calendar.getInstance();
	gc.setTime(date);
	gc.add(12, minutes);
	return gc.getTime();
    }

    public static Date addHour(Date date, int hours) {
	Calendar gc = Calendar.getInstance();
	gc.setTime(date);
	gc.add(10, hours);
	return gc.getTime();
    }

    public static String getSimpleDateString(Calendar calendar) {
	return simpleDateFormatter.format(calendar.getTime());
    }

    public static String getSimpleDateString(Date date) {
	return simpleDateFormatter.format(date);
    }

    public static String getSimpletDateString(Date date, Locale locale) {
	if (date == null)
	    return "";
	FastDateFormat dateFormat = FastDateFormat.getDateInstance(2, locale);
	return dateFormat.format(date);
    }

    public static String getTimeStampDateString() {
	return timeStampFormatter.format(new Date());
    }

    public static String getTimeStampDateString(Date date) {
	return timeStampFormatter.format(date);
    }

    public static int intervalBetweenDates(Date startDate, Date endDate) {
	if ((startDate == null) || (endDate == null)) {
	    throw new RuntimeException("There is null date passed in.");
	}
	Calendar calStartDate = Calendar.getInstance();
	calStartDate.setTime(startDate);
	calStartDate.set(11, 0);
	calStartDate.set(12, 0);
	calStartDate.set(13, 0);
	calStartDate.set(14, 0);

	Calendar calEndDate = Calendar.getInstance();
	calEndDate.setTime(endDate);
	calEndDate.set(11, 0);
	calEndDate.set(12, 0);
	calEndDate.set(13, 0);
	calEndDate.set(14, 0);

	long intervalMillis = calEndDate.getTimeInMillis()
		- calStartDate.getTimeInMillis();
	int intervalDays = (int) Math.floor(intervalMillis / 86400000L);
	return intervalDays;
    }

    public static int intervalHoursBetweenDates(Date startDate, Date endDate) {
	if ((startDate == null) || (endDate == null)) {
	    throw new RuntimeException("There is null date passed in.");
	}
	Calendar calStartDate = Calendar.getInstance();
	calStartDate.setTime(startDate);
	calStartDate.set(12, 0);
	calStartDate.set(13, 0);
	calStartDate.set(14, 0);

	Calendar calEndDate = Calendar.getInstance();
	calEndDate.setTime(endDate);
	calEndDate.set(12, 0);
	calEndDate.set(13, 0);
	calEndDate.set(14, 0);

	long intervalMillis = calEndDate.getTimeInMillis()
		- calStartDate.getTimeInMillis();
	int intervalHours = (int) Math.floor(intervalMillis / 3600000L);
	return intervalHours;
    }

    public static boolean isDate(String dateString) {
	if (parseStringDate(dateString) != null) {
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

    public static boolean isDateTime(String dateTimeString) {
	if (parseStringDateTime(dateTimeString) != null) {
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

    public static Date max(Date[] dateArray) {
	if ((dateArray == null) || (dateArray.length == 0)) {
	    return null;
	}
	Date maxDate = parseStringDate("1900-01-01");

	for (int i = 0; i < dateArray.length; i++) {
	    if ((dateArray[i] != null) && (maxDate.before(dateArray[i]))) {
		maxDate = dateArray[i];
	    }
	}
	return maxDate;
    }

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

    public static Date parseComplexStringDate(String datestring) {
	try {
	    datestring = replaceChineseUnit(datestring);
	    return DateUtils.parseDate(datestring,
		    new String[] { "yyyy-MM-dd HH:mm:ss" });
	} catch (Exception ex) {
	}
	return null;
    }

    public static Date parseSimpleStringDate(String datestring) {
	try {
	    datestring = replaceChineseUnit(datestring);
	    return DateUtils.parseDate(datestring,
		    new String[] { "yyyy-MM-dd" });
	} catch (Exception ex) {
	}
	return null;
    }

    private static String replaceChineseUnit(String dateString) {
	if (dateString.contains("年"))
	    dateString = dateString.replaceAll("年", "-");
	if (dateString.contains("月"))
	    dateString = dateString.replaceAll("月", "-");
	if (dateString.contains("日"))
	    dateString = dateString.replaceAll("日", " ");
	if (dateString.contains("时"))
	    dateString = dateString.replaceAll("时", ":");
	if (dateString.contains("分"))
	    dateString = dateString.replaceAll("分", ":");
	if (dateString.contains("秒")) {
	    dateString = dateString.replaceAll("秒", "");
	}
	return dateString.trim();
    }

    private static String replaceChineseMonth(String dateString) {
	dateString = dateString.replaceAll("十一月", "Nov")
		.replaceAll("十二月", "Dec").replaceAll("一月", "Jan")
		.replaceAll("二月", "Feb").replaceAll("三月", "Mar")
		.replaceAll("四月", "Apr").replaceAll("五月", "May")
		.replaceAll("六月", "Jun").replaceAll("七月", "Jul")
		.replaceAll("八月", "Aug").replaceAll("九月", "Sep")
		.replaceAll("十月", "Oct");
	return dateString.trim();
    }

    public static Date parseStringDate(String datestring) {
	try {
	    datestring = replaceChineseMonth(datestring);
	    datestring = replaceChineseUnit(datestring);

	    Date date = DateUtils.parseDate(datestring, DEFAULT_DATE_PATTERNS);
	    if (date.getYear() > 2099) {
		return null;
	    }
	    return date;
	} catch (Exception ex) {
	}
	return null;
    }

    public static Date parseStringDate(String datestring, String dateFormat) {
	try {
	    datestring = replaceChineseMonth(datestring);
	    datestring = replaceChineseUnit(datestring);
	    dateFormat = dateFormat.replace(":00", "").replace(":00", "");
	    if (datestring.length() > dateFormat.length())
		datestring = datestring.substring(0, dateFormat.length());
	    return DateUtils.parseDate(datestring, new String[] { dateFormat });
	} catch (ParseException e) {
	    SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
		    dateFormat, Locale.US);
	    try {
		return simpleDateFormat.parse(datestring);
	    } catch (ParseException e1) {
	    }
	}
	return null;
    }

    public static Date parseStringDateTime(String timeString) {
	try {
	    timeString = replaceChineseUnit(timeString);
	    Calendar cal = Calendar.getInstance();
	    StringBuffer sb = new StringBuffer();
	    sb.append(cal.get(1));
	    sb.append("-");
	    sb.append(cal.get(2) + 1);
	    sb.append("-");
	    sb.append(cal.get(5));
	    sb.append(" ");
	    sb.append(timeString);

	    return DateUtils.parseDate(sb.toString(), DEFAULT_DATE_PATTERNS);
	} catch (Exception ex) {
	}
	return null;
    }

    public static Date parseStringDateTime(String timeString,
	    String datetimeFormmat) {
	try {
	    timeString = replaceChineseMonth(timeString);
	    timeString = replaceChineseUnit(timeString);

	    return DateUtils.parseDate(timeString,
		    new String[] { datetimeFormmat });
	} catch (Exception ex) {
	    SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
		    datetimeFormmat);
	    try {
		return simpleDateFormat.parse(datetimeFormmat);
	    } catch (ParseException e1) {
	    }
	}
	return null;
    }

    public static Date parseTimestampStringDate(String datestring) {
	try {
	    return DateUtils.parseDate(datestring, DEFAULT_DATE_PATTERNS);
	} catch (Exception ex) {
	}
	return null;
    }

    public static Calendar transDateToCalendar(Date date) {
	Calendar calendar = GregorianCalendar.getInstance();
	calendar.setTime(date);
	return calendar;
    }

    public static Date transQueryFromDate(Date date) {
	Calendar calendar = GregorianCalendar.getInstance();
	calendar.setTime(date);
	calendar.set(11, 0);
	calendar.set(12, 0);
	calendar.set(13, 0);
	calendar.set(14, 0);
	return calendar.getTime();
    }

    public static Date transQueryToDate(Date date) {
	Calendar calendar = GregorianCalendar.getInstance();
	calendar.setTime(date);
	calendar.set(11, 23);
	calendar.set(12, 59);
	calendar.set(13, 59);
	calendar.set(14, 500);
	return calendar.getTime();
    }

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

    public static Date getTwoThousandYear() {
	return parseSimpleStringDate("2000-01-01");
    }
   /**
    * 
    * @param min
    * @return
    */
    public static double miniute2Hour(long min) {
	return min / 60L + min % 60L / 60.0D;
    }

    public static void main(String[] args) throws ParseException {
	Date date = new Date();
	System.out.println("当前日期:" + date);
	System.out.println("得到当前日期的年份:" + getYear(date));
	System.out.println("得到当前的月份:" + getMonth(date));
	System.out.println("得到当前的日期:" + getDay(date));
	System.out.println("得到当前的点:" + getHour(date));
	System.out.println("得到当前的分钟:" + getMinute(date));
	System.out.println("得到当前的秒:" + getSecond(date));
	System.out.println("得到当前的毫秒:" + getMillis(date));
	System.out.println("日期相加:" + addDate(date, 3));
	System.out.println("日期相减:" + diffDate(date, date));
	System.out.println("得到这个星期星期一的日期:" + getFirstDateByWeek(date));
	System.out.println("得到这个星期星期天的日期：" + getLastDateByWeek(date));
	System.out.println("得到这个月第一天的日期:" + getFirstDateByMonth(date));
	System.out.println("得到这个月最后一天的日期:" + getLastDateByMonth(date));
	System.out.println("得到24小时制的当前时间:" + getCurrentTime());
	System.out.println("得到12小时制的当前时间:" + getCurrentTimeBySept());
	System.out.println("Date转字符串:" + dateToString(new Date()));
	System.out
		.println("Date转字符串:" + dateToString(new Date(), "yyyy/MM/dd"));
	System.out.println("当前时间上增加两年：" + addDate(2, YEAR, new Date()));
	System.out.println("当前时间上增加2个月:" + addDate(2, MONTH, new Date()));
	System.out.println("当前时间上增加两天:" + addDate(-2, DAY, new Date()));
	System.out.println("当前时间上减去2分钟:" + addDate(-2, MINUTE, new Date()));
	System.out.println("Date转Date：" + dateToDate(new Date()));
	System.out.println("日期是否在当前日期之后:" + afterDate(new Date()));
	System.out.println("日期是否在当前日期之前:" + beforeDate(new Date()));
	System.out.println("是否第一个日期比第二个日期大:" + whoBig(new Date(), new Date()));
	System.out.println("得到明天日期：" + nextDay());
	System.out.println("得到当前时间 精确到毫秒：" + getCurrentTimeMillions());
	System.out.println("得到两个日期之间相隔的天数:"
		+ compareToDay2(new Date(), new Date()));
	System.out.println("毫秒转换成天数:" + millonToDay(2112121212));
	System.out.println("得到当前是星期几:" + getWeek(new Date()));
	System.out.println("得到明天的零点:" + nextDayToPoint());
	System.out.println("得到明天:" + nextDayToDate(new Date(), "yyyy-MM-dd"));
	System.out.println("得到某一天是星期几:"
		+ stringToWeek(dateToString(new Date())));
	System.out.println("得到是否是工作日:" + isWorkDay(new Date()));
	System.out.println("日期交集范围开始------------------------");
	String[] rangeDate = getRangeDate("2012-10-17", "2013-10-17",
		"2013-10-01", "2014-10-17");
	for (int i = 0; i < rangeDate.length; i++) {
	    System.out.println("范围日期区间为:" + rangeDate[i]);
	}
	System.out.println("日期交集范围结束-------------------------");

	System.out.println("日期段开始-----------------------------");
	List<HashMap<String, Object>> list = betweenDate("2012-10-19",
		"2013-10-19");
	for (int i = 0; i < list.size(); i++) {
	    HashMap<String, Object> map = list.get(i);
	    System.out.println(map.get("begindate"));
	    System.out.println(map.get("enddate"));
	}
	System.out.println("日期段结束------------------------------");

	System.out.println("某个月的所有日期开始------------------------");
	List<String> dayList = dayList("2014-10");
	for (int i = 0; i < dayList.size(); i++) {
	    String string = dayList.get(i);
	    System.out.println(string);
	}
	System.out.println("某个月的所有日期结束----------------------");

	System.out.println();
	System.out.println("字符串转Date:" + stringToDate("2012-10-22 00:00:00"));
	System.out.println("字符串转Date:"
		+ stringToDate("2012/10/22 00:00:00", "yyyy/MM/dd"));
	System.out
		.println("两个日期之间相隔多少分钟:"
			+ compareToMinute("2012-10-22 00:00:00",
				"2012-10-22 01:00:00"));
	System.out.println(nextDayToDate(new Date(), "yyyy-MM-dd"));
	System.out.println("得到相隔天数只计算工作日:"
		+ compareToDay("2014-10-17", "2014-10-22", true));
	System.out.println("比较两个日期对象小时和分钟的大小:"
		+ compareHourAndMinute(new Date(), new Date()));
	System.out.println("比较两个日期对象 精确到分钟:"
		+ compareIgnoreSecond(new Date(), new Date()));
	System.out.println("取得今天的最后一个时刻:" + currentEndDate());
	System.out.println("取得今天第一个时刻:" + currentStartDate());
	System.out.println("取得某一天的起始时间:" + getStartDate(new Date()));
	System.out.println("根据英文名称得到中文几:" + getChineseWeekNumber("monday"));
	System.out.println("构建日期对象:" + getDate(2013, 12, 12));
	System.out.println("构建日期对象:" + getDate(2013, 12, 12, 12, 11));
	System.out.println("构建日期对象:" + getDate(2013, 12, 12, 12, 12, 12));
	System.out.println("获取某一天的结束时间:" + getEndDate(new Date()));
	System.out.println("获取这个月最大的天数:" + getMaxDayOfMonth(2014, 9));
	System.out.println("是否是闰年:" + isLeapYear(2014));
	System.out.println("一年中的第几周:" + getWeekOfYear(new Date()));
	System.out.println("获取上周星期几的日期:" + getDateOfPreviousWeek(1));
	System.out.println("获取本周星期几的日期:" + getDateOfCurrentWeek(4));
	System.out.println("获取下周星期几的日期:" + getDateOfNextWeek(1));

    }

}
