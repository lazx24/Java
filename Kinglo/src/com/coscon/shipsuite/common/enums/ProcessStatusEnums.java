package com.coscon.shipsuite.common.enums;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

public enum ProcessStatusEnums implements Serializable {
    SUCCESS("SUCCESS"), FAILED("FAILED");

    private String status;

    private ProcessStatusEnums(String status) {
	this.status = status;
    }

    public String getStatus() {
	return this.status;
    }

    public static List<ProcessStatusEnums> getAll() {
	List<ProcessStatusEnums> list = new ArrayList<ProcessStatusEnums>();
	list.addAll(EnumSet.allOf(ProcessStatusEnums.class));
	return list;
    }
}
