package com.common.enums;

/**
 * 
 * 类的描述:Excel单元格类型 主要用于Excel导出
 * 创建时间:2015-5-5
 * 创建人:邹建华
 */
public enum ExcelCellType {
    CELL_TYPE_NUMBER("cellTypeNumber"), CELL_TYPE_BOOLEAN("cellTypeBoolean"), CELL_TYPE_STRING(
	    "cellTypeString");

    private String value;

    public String getValue() {
	return value;
    }

    private ExcelCellType(String value) {
	this.value = value;
    }
}
