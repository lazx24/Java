package com.common.util.database;

import java.sql.Connection;
import java.sql.DriverManager;

public final class DBConn {
    
    private static DBConfig config = DBConfig.getInstance();
    
    public static Connection getConnection(){
	try {
	    String driverClass = DBUtil.getDriverClass(config.getDbType());
	    Class.forName(driverClass);
	    return DriverManager.getConnection(config.getUrl(), config.getUsername(), config.getPassword());
	} catch (Exception e) {
	    e.printStackTrace();
	}
	return null;
    }
}
