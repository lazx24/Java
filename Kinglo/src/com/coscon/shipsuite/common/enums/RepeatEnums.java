package com.coscon.shipsuite.common.enums;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

public enum RepeatEnums implements Serializable {
    ONCE, HOURLY, DAILY, WEEKLY, MONTHLY, YEARLY;

    public static List<RepeatEnums> getAll() {
	List<RepeatEnums> list = new ArrayList<RepeatEnums>();
	list.addAll(EnumSet.allOf(RepeatEnums.class));
	return list;
    }
}
