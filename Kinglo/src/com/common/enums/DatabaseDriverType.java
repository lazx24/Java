package com.common.enums;

import java.util.EnumSet;
import java.util.List;

import com.common.util.generic.ListUtil;

/**
 * 
 * 类的描述:数据库驱动类型
 * 创建时间:2015-5-5
 * 创建人:邹建华
 */
public enum DatabaseDriverType {
    ORACLE("oracle","oracle.jdbc.driver.OracleDriver"),
    SQLSERVER("sqlserver","com.microsoft.jdbc.sqlserver.SQLServerDriver"),
    MYSQL("mysql","com.mysql.jdbc.Driver");
    
    private String value;
    
    private String key;
    
    public String getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }

    private DatabaseDriverType(String key,String value){
	this.key = key;
	this.value= value;
    }
    
    public static List<DatabaseDriverType> getAll(){
	EnumSet<DatabaseDriverType> databaseDriverTypeSet = EnumSet.allOf(DatabaseDriverType.class);
	List<DatabaseDriverType> databaseDriverTypeList = ListUtil.asList(databaseDriverTypeSet);
	return databaseDriverTypeList;
    }
}
