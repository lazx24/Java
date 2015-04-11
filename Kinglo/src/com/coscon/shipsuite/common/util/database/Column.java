package com.coscon.shipsuite.common.util.database;

public class Column {

    private String columnName;// 列名

    private String columnType;// 列类型

    private String length;// 长度

    public String getLength() {
	return length;
    }

    public void setLength(String length) {
	this.length = length;
    }

    public String getRemark() {
	return remark;
    }

    public void setRemark(String remark) {
	this.remark = remark;
    }

    public String getFieldName() {
	return fieldName;
    }

    public void setFieldName(String fieldName) {
	this.fieldName = fieldName;
    }

    public String getJdbcType() {
	return jdbcType;
    }

    public void setJdbcType(String jdbcType) {
	this.jdbcType = jdbcType;
    }

    private String remark;// 备注

    private String fieldName;// 属性名

    private String jdbcType;// 属性类型

    public String getColumnName() {
	return columnName;
    }

    public void setColumnName(String columnName) {
	this.columnName = columnName;
    }

    public String getColumnType() {
	return columnType;
    }

    public void setColumnType(String columnType) {
	this.columnType = columnType;
    }
}
