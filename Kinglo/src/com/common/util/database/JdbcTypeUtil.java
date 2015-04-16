package com.common.util.database;

public class JdbcTypeUtil {

    public static String getJdbcType(int type) {
	String returnType = "";
	switch (type) {
	case -1:
	    returnType = "String";
	    break;
	case 1:
	    returnType = "char";
	    break;
	case 4:
	    returnType = "int";
	    break;
	case 6:
	    returnType = "float";
	    break;
	case 8:
	    returnType = "double";
	    break;
	case 12:
	    returnType = "String";
	    break;
	case 16:
	    returnType = "boolean";
	    break;
	case 91:
	    returnType = "Date";
	    break;
	case 93:
	    returnType = "Date";
	    break;
	}
	return returnType;
    }
}
