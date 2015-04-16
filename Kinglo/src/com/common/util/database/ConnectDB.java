package com.common.util.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * 功能描述：连接数据库
 * @author zou
 * 2015-2-3
 */
public class ConnectDB {

    private static final String DEFAULT_MYSQL_DRIVER = "com.mysql.jdbc.Driver";

    private static final String DEFAULT_ORACLE_DRIVER = "oracle.jdbc.driver.OracleDriver";

    private static final String DEFAULT_SQLSERVER_DRIVER = "com.microsoft.jdbc.sqlserver.SQLServerDriver";

    private static final String DEFAULT_SYBASE_DRIVER = "com.sybase.jdbc3.jdbc.SybDriver";

    private static final String DEFAULT_POSTGRESQL_DRIVER = "org.postgresql.Driver";

    private ConnectDB() {
    }

    public static Connection getConnection(String DBType, String driverClass,
	    String url, String user, String password) throws SQLException {
	if ("mysql".equalsIgnoreCase(DBType))
	    return getMySqlConn(driverClass, url, user, password);
	if ("oracle".equalsIgnoreCase(DBType))
	    return getOracleConn(driverClass, url, user, password);
	if ("sqlserver".equalsIgnoreCase(DBType))
	    return getSqlServerConn(driverClass, url, user, password);
	if ("sybase".equalsIgnoreCase(DBType))
	    return getSybaseConn(driverClass, url, user, password);
	if ("postgresql".equalsIgnoreCase(DBType))
	    return getPostgreSQLConn(driverClass, url, user, password);
	if ("db2".equalsIgnoreCase(DBType))
	    return getDB2Conn(driverClass, url, user, password);

	return null;
    }

    public static void closeConn(Connection conn) {
	if (conn != null) {
	    try {
		conn.close();
	    } catch (SQLException e) {
		e.printStackTrace();
	    }
	}
    }

    private static Connection getMySqlConn(String driverClass, String url,
	    String user, String password) throws SQLException {
	Connection conn = null;
	try {
	    if (driverClass == null || driverClass.equals("")) {
		Class.forName(DEFAULT_MYSQL_DRIVER);// 加载驱动
	    } else {
		Class.forName(driverClass);// 加载驱动
	    }
	} catch (ClassNotFoundException e) {
	    e.printStackTrace();
	}
	conn = DriverManager.getConnection(url, user, password);

	return conn;
    }

    private static Connection getDB2Conn(String driverClass, String url,
	    String user, String password) throws SQLException {
	Connection conn = null;
	try {
	    Class.forName(driverClass);// 加载驱动
	} catch (ClassNotFoundException e) {
	    e.printStackTrace();
	}
	conn = DriverManager.getConnection(url, user, password);

	return conn;
    }

    private static Connection getSybaseConn(String driverClass, String url,
	    String user, String password) throws SQLException {
	Connection conn = null;
	try {
	    if (driverClass == null || driverClass.equals("")) {
		Class.forName(DEFAULT_SYBASE_DRIVER);// 加载驱动
	    } else {
		Class.forName(driverClass);// 加载驱动
	    }
	} catch (ClassNotFoundException e) {
	    e.printStackTrace();
	}
	conn = DriverManager.getConnection(url, user, password);

	return conn;
    }

    private static Connection getPostgreSQLConn(String driverClass, String url,
	    String user, String password) throws SQLException {
	Connection conn = null;
	try {
	    if (driverClass == null || driverClass.equals("")) {
		Class.forName(DEFAULT_POSTGRESQL_DRIVER);// 加载驱动
	    } else {
		Class.forName(driverClass);// 加载驱动
	    }
	} catch (ClassNotFoundException e) {
	    e.printStackTrace();
	}
	conn = DriverManager.getConnection(url, user, password);

	return conn;
    }

    private static Connection getOracleConn(String driverClass, String url,
	    String user, String password) throws SQLException {
	Connection conn = null;
	try {
	    if (driverClass == null || driverClass.equals("")) {
		Class.forName(DEFAULT_ORACLE_DRIVER);// 加载驱动
	    } else {
		Class.forName(driverClass);// 加载驱动
	    }
	} catch (ClassNotFoundException e) {
	    e.printStackTrace();
	}
	conn = DriverManager.getConnection(url, user, password);

	return conn;
    }

    private static Connection getSqlServerConn(String driverClass, String url,
	    String user, String password) throws SQLException {
	Connection conn = null;
	try {
	    if (driverClass == null || driverClass.equals("")) {
		Class.forName(DEFAULT_SQLSERVER_DRIVER);// 加载驱动
	    } else {
		Class.forName(driverClass);// 加载驱动
	    }
	} catch (ClassNotFoundException e) {
	    e.printStackTrace();
	}
	conn = DriverManager.getConnection(url, user, password);

	return conn;
    }

    public static void main(String[] args) {
	try {
	    Connection conn = getConnection("", "MySQL", "127.0.0.1", "root",
		    "root");
	    if (conn == null) {
		System.out.println("Connection the database is failled !");
	    } else {
		System.out.println(conn.toString());
	    }
	} catch (SQLException e) {
	    e.printStackTrace();
	}

    }

}
