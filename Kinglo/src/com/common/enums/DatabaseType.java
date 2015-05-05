package com.common.enums;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

import com.common.util.generic.ListUtil;

/**
 * 
 * 类的描述:数据库类型枚举
 * 创建时间:2015-5-5
 * 创建人:邹建华
 */
public enum DatabaseType {
    ORACLE("oracle"),SQLSERVER("sqlserver"),MYSQL("mysql");
    
    private String value;
    
    public String getValue() {
        return value;
    }

    private DatabaseType(String value){
	this.value=value;
    }
    
    public static List<DatabaseType> getAll(){
	EnumSet<DatabaseType> databaseTypeSet = EnumSet.allOf(DatabaseType.class);
	List<DatabaseType> databaseTypeList = new ArrayList<DatabaseType>();
	databaseTypeList = ListUtil.asList(databaseTypeSet);
	return databaseTypeList;
    }
    
    public static void main(String[] args) {
	List<DatabaseType> databaseTypeList = getAll();
	for(DatabaseType type:databaseTypeList){
	    System.out.println(type);
	}
    }
}
