package com.common.util.excel;

import java.math.BigDecimal;

import com.common.enums.ExcelCellType;

/**
 * 
 * 类的描述:Excel数据格式
 * 创建人:邹建华
 * 创建时间:2015-4-16
 */
public class ExcelDataFormat {
    
    private Object value;
    
    private String cellType;
    
    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public String getCellType() {
        return cellType;
    }

    public void setCellType(String cellType) {
        this.cellType = cellType;
    }

    public ExcelDataFormat(Object value){
	if(value == null){
	    this.value="";
	    this.cellType=ExcelCellType.CELL_TYPE_STRING.getValue();
	    return;
	}
	
	this.value=value;
	if(isNumber(value)){
	    this.cellType=ExcelCellType.CELL_TYPE_NUMBER.getValue();
	}else if(value instanceof Boolean){
	    this.cellType=ExcelCellType.CELL_TYPE_BOOLEAN.getValue();
	}else if(value instanceof String){
	    this.cellType=ExcelCellType.CELL_TYPE_STRING.getValue();
	}else{
	    this.cellType=ExcelCellType.CELL_TYPE_STRING.getValue();
	}
    }
    
    private boolean isNumber(Object value){
	if(value instanceof Integer || value instanceof Byte 
		|| value instanceof Double || value instanceof Float
		|| value instanceof Long || value instanceof BigDecimal){
	    return true;
	}
	return false;
    }
}
