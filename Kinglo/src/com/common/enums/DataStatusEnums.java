package com.common.enums;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

public enum DataStatusEnums implements Serializable {
    INSERTED("INSERTED"), UPDATED("UPDATED"), DELETED("DELETED");

    private String status;

    private DataStatusEnums(String status) {
	this.status = status;
    }

    public String getStatus() {
	return this.status;
    }

    public static List<DataStatusEnums> getAll() {
	List<DataStatusEnums> list = new ArrayList<DataStatusEnums>();
	list.addAll(EnumSet.allOf(DataStatusEnums.class));
	return list;
    }
}
