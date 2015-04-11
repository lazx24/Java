package com.coscon.shipsuite.common.enums;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

public enum FullTextQueryTypeEnums implements Serializable {
    LIKE("like"), ABOVE("above"), BELOW("below");

    private String type;

    public static List<FullTextQueryTypeEnums> getAll() {
	List<FullTextQueryTypeEnums> list = new ArrayList<FullTextQueryTypeEnums>();
	list.addAll(EnumSet.allOf(FullTextQueryTypeEnums.class));
	return list;
    }

    private FullTextQueryTypeEnums(String type) {
	this.type = type;
    }

    public String getType() {
	return this.type;
    }
}
