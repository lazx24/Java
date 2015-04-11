package com.coscon.shipsuite.common.util.generic;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public final class CronGenerator {
    public static String getDailyCronSetting(int hour, int min) {
	StringBuffer cronSetting = new StringBuffer();
	cronSetting.append("0 ");
	cronSetting.append(min);
	cronSetting.append(" ");
	cronSetting.append(hour);
	cronSetting.append(" * * ?");

	return cronSetting.toString();
    }

    public static List<String> getFebSchedulerCronSetting(
	    String preCronSetting, int year, int futureYears, int day,
	    int hour, int min) {
	List<String> monthlyCronSetting = new ArrayList<String>();

	StringBuffer sb = new StringBuffer();

	Calendar cal = Calendar.getInstance();
	cal.set(year, 2, day, hour, min);

	boolean thisYearTooLate = false;
	if (cal.compareTo(Calendar.getInstance()) <= 0) {
	    thisYearTooLate = true;
	}
	for (int i = 0; i < futureYears; i++) {
	    sb.setLength(0);
	    sb.append(preCronSetting);
	    if (DateUtil.isLeapYear(year + i)) {
		sb.append("29 2 ? ");
	    } else {
		sb.append("28 2 ? ");
	    }
	    if ((i != 0) || (!thisYearTooLate)) {
		sb.append(year + i);
		monthlyCronSetting.add(sb.toString());
	    }
	}
	return monthlyCronSetting;
    }

    public static String getHourlyCronSetting(int min) {
	StringBuffer cronSetting = new StringBuffer();
	cronSetting.append("0 ");
	cronSetting.append(min);
	cronSetting.append(" * * * ?");
	return cronSetting.toString();
    }

    public static String getMinutelyCronSetting() {
	return "0 * * * * ?";
    }

    public static List<String> getMonthlyCronSetting(int day, int hour,
	    int min, int futureYears) {
	List<String> cronSetting = new ArrayList<String>();

	StringBuffer sb = new StringBuffer();
	sb.append("0 ");
	sb.append(min);
	sb.append(" ");
	sb.append(hour);
	sb.append(" ");
	String preSetting = sb.toString();

	Calendar cDay = Calendar.getInstance();

	int year = cDay.get(1);
	switch (day) {
	case 29:
	    sb.setLength(0);
	    sb.append(preSetting);
	    List<String> febCronSetting = getFebSchedulerCronSetting(
		    sb.toString(), year, futureYears, day, hour, min);
	    cronSetting.addAll(febCronSetting);

	    sb.setLength(0);
	    sb.append(preSetting);
	    sb.append("29 1,3-12 ?");
	    cronSetting.add(sb.toString());
	    break;
	case 30:
	    sb.setLength(0);
	    sb.append(preSetting);
	    febCronSetting = getFebSchedulerCronSetting(
		    sb.toString(), year, futureYears, day, hour, min);
	    cronSetting.addAll(febCronSetting);

	    sb.setLength(0);
	    sb.append(preSetting);
	    sb.append("30 1,3-12 ?");
	    cronSetting.add(sb.toString());
	    break;
	case 31:
	    sb.setLength(0);
	    sb.append(preSetting);
	    febCronSetting = getFebSchedulerCronSetting(
		    sb.toString(), year, futureYears, day, hour, min);
	    cronSetting.addAll(febCronSetting);

	    sb.setLength(0);
	    sb.append(preSetting);
	    sb.append("30 4,6,9,11 ?");
	    cronSetting.add(sb.toString());

	    sb.setLength(0);
	    sb.append(preSetting);
	    sb.append("31 1,3,5,7,8,10,12 ?");
	    cronSetting.add(sb.toString());
	    break;
	default:
	    sb.setLength(0);
	    sb.append(preSetting);
	    sb.append(day);
	    sb.append(" * ?");
	    cronSetting.add(sb.toString());
	}
	return cronSetting;
    }

    public static String getWeeklyCronSetting(int weekDay, int hour, int min) {
	StringBuffer cronSetting = new StringBuffer();
	cronSetting.append("0 ");
	cronSetting.append(min);
	cronSetting.append(" ");
	cronSetting.append(hour);
	cronSetting.append(" ? * ");
	cronSetting.append(weekDay);
	return cronSetting.toString();
    }
}
