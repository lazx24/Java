package com.coscon.shipsuite.common.enums;

import java.io.Serializable;

public enum WeekDayEnums implements Serializable {
	SUNDAY("Sun"), MONDAY("Mon"), TUESDAY("Tue"), WEDNESDAY("Wed"), THURSDAY(
			"Thur"), FRIDAY("Fri"), SATURDAY("Sat");

	private String weekday;

	private WeekDayEnums(String weekday) {
		this.weekday = weekday;
	}

	public String getValue() {
		return this.weekday;
	}
}
