package com.common.util.database;

import com.common.util.string.StringUtil;
import com.common.util.validator.ValidUtil;

public class DBUtil {
    /**
     * 根据driverClass获取数据库类型
     * 
     * @param driverClass
     * @return
     */
    public static String getDatabaseType(String driverClass) {
	if (!StringUtil.isNullOrEmpty(driverClass)) {
	    if (driverClass.toLowerCase().contains("mysql")) {
		return "mysql";
	    } else if (driverClass.toLowerCase().contains("oracle")) {
		return "oracle";
	    } else if (driverClass.toLowerCase().contains("sqlserver")) {
		return "sqlserver";
	    }
	}
	return "";
    }

    /**
     * 根据url获取数据库名称
     * 
     * @param url
     * @param driverClass
     * @return
     */
    public static String getDatabaseName(String url, String driverClass) {
	String databaseName = "";
	if (!StringUtil.isNullOrEmpty(url)) {
	    String databaseType = getDatabaseType(driverClass);
	    if (databaseType.equals("mysql")) {
		databaseName = url.substring(url.lastIndexOf("/") + 1);
	    } else if (databaseType.equals("sqlserver")) {
		databaseName = url.substring(url.lastIndexOf("=") + 1);
	    } else if (databaseType.equals("oracle")) {
		databaseName = url.substring(url.lastIndexOf(":") + 1);
	    }
	}
	return databaseName;
    }

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
