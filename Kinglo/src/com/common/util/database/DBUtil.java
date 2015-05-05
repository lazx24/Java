package com.common.util.database;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;

import org.apache.commons.lang.NullArgumentException;

import com.common.enums.DatabaseDriverType;
import com.common.util.string.StringUtil;
import com.common.util.validator.ValidUtil;

public class DBUtil {
    
    /**
     * 根据DB类型获取驱动类
     * @param dbType  	数据库类型
     * @return	      	驱动类
     */
    public static String getDriverClass(String dbType){
	if(StringUtil.isNullOrEmpty(dbType)){
	    throw new NullArgumentException("dbType is null!");
	}
	List<DatabaseDriverType> databaseDriverTypeList = DatabaseDriverType.getAll();
	for(DatabaseDriverType type:databaseDriverTypeList){
	    if(type.getKey().equals(dbType.toLowerCase())){
		return type.getValue();
	    }
	}
	return null;
    }
    
    /************************************关闭连接***********************************/
    public static void close(Object...params){
	if(null!=params && params.length > 0){
	    for (int i = 0; i < params.length; i++) {
		try {
		    if(params[i] instanceof ResultSet){
			ResultSet rs = (ResultSet)params[i];
			rs.close();
		    }
		    if(params[i] instanceof Statement){
			Statement statement = (Statement)params[i];
			statement.close();
		    }
		    if(params[i] instanceof Connection){
			Connection conn = (Connection)params[i];
			conn.close();
		    }
		} catch (Exception e) {
		    e.printStackTrace();
		    continue;
		}
	    }
	}
    }
    /************************************关闭连接***********************************/
    
    /************************************关闭连接***********************************/
    
    
    
    /************************************关闭连接***********************************/

    /**
     * 主用用于数据库列名转属性名
     * 
     * @param columnName
     * @return
     */
    public static String getFieldName(String columnName) {
	StringBuffer buffer = new StringBuffer();
	if (!StringUtil.isNullOrEmpty(columnName)) {
	    String[] columnNames = columnName.split("_");
	    for (int i = 0; i < columnNames.length; i++) {
		if (i == 0) {
		    buffer.append(firstLetterLower(columnNames[i]));
		} else {
		    buffer.append(firstLetterUpper(columnNames[i]));
		}
	    }
	}
	return buffer.toString();
    }

    /**
     * 主用用于数据库属性名转列名
     * 
     * @param fieldName
     * @return
     */
    public static String getColumnName(String fieldName) {
	StringBuffer buffer = new StringBuffer();
	if (!StringUtil.isNullOrEmpty(fieldName)) {
	    for (int i = 0; i < fieldName.length(); i++) {
		Character c = fieldName.charAt(i);
		if (ValidUtil.isUpperCase(c)) {
		    buffer.append("_").append(c);
		} else {
		    buffer.append(Character.toUpperCase(c));
		}
	    }
	}
	return buffer.toString();
    }

    /**
     * 根据表名得到类的名称
     * 
     * @param tableName
     * @return
     */
    public static String getClassName(String tableName) {
	StringBuffer buffer = new StringBuffer();
	if (!StringUtil.isNullOrEmpty(tableName)) {
	    String[] tableNames = tableName.split("_");
	    for (int i = 0; i < tableNames.length; i++) {
		buffer.append(firstLetterUpper(tableNames[i]));
	    }
	}
	return buffer.toString();
    }

    /**
     * 整个字符串小写
     * 
     * @param str
     * @return
     */
    public static String firstLetterLower(String str) {
	if (!StringUtil.isNullOrEmpty(str)) {
	    return str.toLowerCase();
	}
	return "";
    }

    /**
     * 首字母大写 后面字符全部小写
     * 
     * @param str
     * @return
     */
    public static String firstLetterUpper(String str) {
	if (!StringUtil.isNullOrEmpty(str)) {
	    String lowerStr = str.toLowerCase();
	    return lowerStr.substring(0, 1).toUpperCase()
		    + lowerStr.substring(1);
	}
	return "";
    }
}
