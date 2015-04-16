package com.common.enums;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

/**
 * SQL基本函数
 * @author zou
 * 2015-1-29
 */
public enum AggregateEnums implements Serializable {
    MIN("min"), MAX("max"), COUNT("count"), AVG("avg"), SUM("sum");

    private String type;

    public static List<AggregateEnums> getAll() {
	List<AggregateEnums> list = new ArrayList<AggregateEnums>();
	list.addAll(EnumSet.allOf(AggregateEnums.class));
	return list;
    }

    private AggregateEnums(String type) {
	this.type = type;
    }

    public String getType() {
	return this.type;
    }
}
