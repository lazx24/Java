package com.common.enums;

public enum ExcelCellType {
	CELL_TYPE_NUMBER("cellTypeNumber"),CELL_TYPE_BOOLEAN("cellTypeBoolean"),CELL_TYPE_STRING("cellTypeString");
	
	private String value;
	
	public String getValue() {
		return value;
	}

	private ExcelCellType(String value){
		this.value=value;
	}
}
