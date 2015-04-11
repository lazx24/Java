package com.coscon.shipsuite.common.enums;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

public enum TimeZoneEnums implements Serializable {
    GMT_12("GMT-12"), GMT_11("GMT-11"), GMT_10("GMT-10"), GMT_9("GMT-9"), GMT_8(
	    "GMT-8"), GMT_7("GMT-7"), GMT_6("GMT-6"), GMT_5("GMT-5"), GMT_4(
	    "GMT-4"), GMT_3("GMT-3"), GMT_2("GMT-2"), GMT_1("GMT-1"), GMT("GMT"), GMT1(
	    "GMT+1"), GMT2("GMT+2"), GMT3("GMT+3"), GMT4("GMT+4"), GMT5("GMT+5"), GMT6(
	    "GMT+6"), GMT7("GMT+7"), GMT8("GMT+8"), GMT9("GMT+9"), GMT10(
	    "GMT+10"), GMT11("GMT+11"), GMT12("GMT+12");

    private String code;

    public static List<TimeZoneEnums> getAll() {
	EnumSet<TimeZoneEnums> timeZoneSet = EnumSet.allOf(TimeZoneEnums.class);
	List<TimeZoneEnums> list = new ArrayList<TimeZoneEnums>(timeZoneSet.size());
	for (int i = 0; i < timeZoneSet.size(); i++) {
	    for (TimeZoneEnums timeZone : timeZoneSet) {
		if (timeZone.ordinal() == i) {
		    list.add(timeZone);
		    break;
		}
	    }
	}
	return list;
    }

    private TimeZoneEnums(String code) {
	this.code = code;
    }

    public String getCode() {
	return this.code;
    }
}
