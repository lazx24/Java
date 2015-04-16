package com.common.util.generic;

import com.common.enums.RepeatEnums;

import java.io.PrintStream;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import org.junit.Before;
import org.junit.Test;

public class DateUtilTest
{
  @Before
  public void setUp()
    throws Exception
  {}
  
  @Test
  public void getDayHourString()
  {
    System.out.println(DateUtil.getDayHourString(new BigDecimal("0")));
    System.out.println(DateUtil.getDayHourString(new BigDecimal("1")));
    System.out.println(DateUtil.getDayHourString(new BigDecimal("11.5")));
    System.out.println(DateUtil.getDayHourString(new BigDecimal("23.5")));
    System.out.println(DateUtil.getDayHourString(new BigDecimal("24")));
    System.out.println(DateUtil.getDayHourString(new BigDecimal("28")));
    System.out.println(DateUtil.getDayHourString(new BigDecimal("48")));
    System.out.println(DateUtil.getDayHourString(new BigDecimal("48.567")));
  }
  
  @Test
  public void parseStringDate()
  {
    System.out.println(DateUtil.parseStringDate(
      "2011-11-01 07:38:17.668000", "yyyy-MM-dd hh:mm:ss.SSS"));
    
    System.out.println(DateUtil.parseStringDate("14 Aug 2013", 
      "dd MMMM yyyy"));
    
    System.out.println(DateUtil.parseStringDate("2013 Aug 14 07:38:17.668", 
      "yyyy MMM dd hh:mm:ss.SSS"));
    
    System.out.println(
      DateUtil.parseStringDate("14 八月 2013", "dd MMM yyyy"));
  }
  
  @Test
  public void parseSimpleStringDate()
  {
    System.out.println(DateUtil.parseSimpleStringDate("2011-2-29"));
    System.out.println(DateUtil.parseSimpleStringDate("2012-2-30"));
    System.out.println(DateUtil.parseSimpleStringDate("2011-3-32"));
  }
  
  @Test
  public void isDate()
  {
    System.out.println(DateUtil.isDate("2011-2-28"));
    System.out.println(DateUtil.isDate("2012-2-29"));
    System.out.println(DateUtil.isDate("2011-3-32"));
  }
  
  @Test
  public void parseComplexStringDate()
  {
    System.out.println(
      DateUtil.parseComplexStringDate("2011-10-10 21:10:09"));
  }
  
  @Test
  public void getDateString()
  {
    System.out.println(DateUtil.getDateString(new Date(), 
      "MM/dd/yyyy HH:mm a", Locale.US));
    System.out.println(DateUtil.getDateString(new Date(), 
      "yyyy-MM-dd HH:mm:ss"));
    System.out.println(DateUtil.getDateString(new Date(), 
      "yyyy-MM-dd HH:mm"));
    System.out.println(DateUtil.getDateString(new Date(), 
      "yyyy-MM-dd HH:mm", Locale.US));
    System.out.println(DateUtil.getDateString(new Date(), 
      "yyyy-MM-dd HH:00"));
    
    System.out.println(DateUtil.getDateString(new Date(), "E HH:mm", 
      Locale.SIMPLIFIED_CHINESE));
    
    System.out.println(DateUtil.getDateString(new Date(), 
      "MMM dd, yyyy HH:mm", Locale.US));
    
    System.out.println(DateUtil.getDateString(Calendar.getInstance(), 
      "yyyyMMddHHmmssSSS"));
    System.out.println(DateUtil.getDateString(new Date(), 
      "dd MMM yyyy hh:mm:ss.SSS"));
    System.out.println(DateUtil.getDateString(new Date(), 
      "dd MMM yyyy hh:mm:ss.SSS", Locale.US));
  }
  
  @Test
  public void getNextWorkingDay()
  {
    System.out.println(DateUtil.getNextWorkingDay(new Date(), 7));
    System.out.println(DateUtil.getNextWorkingDay(new Date(), -2));
    System.out.println(DateUtil.getNextWorkingDay(new Date(), -7));
  }
  
  @Test
  public void addDay()
  {
    System.out.println(DateUtil.addDay(new Date(), -10));
  }
  
  @Test
  public void addHour()
  {
    System.out.println(DateUtil.addHour(new Date(), -1));
    System.out.println(DateUtil.addHour(new Date(), -2));
    System.out.println(DateUtil.addHour(new Date(), 1));
  }
  
  @Test
  public void addMonth()
  {
    System.out.println(DateUtil.addMonth(new Date(), -1));
  }
  
  @Test
  public void addWeek()
  {
    System.out.println(DateUtil.addWeek(new Date(), -1));
    System.out.println(DateUtil.addWeek(new Date(), -4));
  }
  
  @Test
  public void addYear()
  {
    System.out.println(DateUtil.addYear(new Date(), -1));
  }
  
  @Test
  public void getNextScheduleDate()
  {
    System.out.println(DateUtil.getComplexDateString(
      DateUtil.getNextScheduleDate(
      DateUtil.parseComplexStringDate("2013-01-01 10:12:00"), 
      RepeatEnums.DAILY)));
    
    System.out.println(DateUtil.getComplexDateString(
      DateUtil.getNextScheduleDate(
      DateUtil.parseComplexStringDate("2013-05-30 14:12:00"), 
      RepeatEnums.DAILY)));
    
    System.out.println(DateUtil.getComplexDateString(
      DateUtil.getNextScheduleDate(
      DateUtil.parseComplexStringDate("2013-05-30 15:12:00"), 
      RepeatEnums.DAILY)));
    
    System.out.println(DateUtil.getComplexDateString(
      DateUtil.getNextScheduleDate(
      DateUtil.parseComplexStringDate("2013-11-01 10:12:00"), 
      RepeatEnums.DAILY)));
  }
  
  @Test
  public void getMiddleNightDate()
  {
    Date day = DateUtil.addDay(new Date(), -1);
    System.out.println(DateUtil.getComplexDateString(DateUtil.getMiddleNightDate(day)));
  }
}
