package com.coscon.shipsuite.common.util.database;

import java.util.ArrayList;
import java.util.List;

public class Table {

    private String name;// 数据库表名

    private String className;// 实体类名称

    public String getClassName() {
	return className;
    }

    public void setClassName(String className) {
	this.className = className;
    }

    private List<Column> columnList = new ArrayList<Column>();

    public String getName() {
	return name;
    }

    public void setName(String name) {
	this.name = name;
    }

    public List<Column> getColumnList() {
	return columnList;
    }

    public void setColumnList(List<Column> columnList) {
	this.columnList = columnList;
    }

}
