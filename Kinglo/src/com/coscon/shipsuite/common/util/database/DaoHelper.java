package com.coscon.shipsuite.common.util.database;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.coscon.shipsuite.common.util.generic.ArrayUtil;
import com.coscon.shipsuite.common.util.validator.ValidUtil;


/**
 * 你是否为获取某个表所有字段的精确值而烦恼？<br>
 * 这里的代码可以为你精确提供那些字段名称的值<br>
 * 它可以帮里生成常用的SQL<br>
 * 它还可以帮里生成bean 里的所有字段
 * 
 * @author huangfei
 * @version 1.0.0
 * @2009-5-4 下午04:02:08
 */
public class DaoHelper {

    private static ConnectionPool pool;

    private String databaseType;// 数据库类型

    private String databaseName;// 数据库名称

    public DaoHelper() {
	try {
	    DBConfig config = DBConfig.getInstance();
	    String url = config.getUrl();
	    String username = config.getUsername();
	    String password = config.getPassword();
	    String driverClass = config.getDriverClass();
	    int initPoolSize = config.getInitPoolSize();
	    int incrementSize = config.getIncrementSize();
	    int maxPoolSize = config.getMaxPoolSize();
	    pool = new ConnectionPool(driverClass, url, username, password,
		    initPoolSize, incrementSize, maxPoolSize);
	    pool.createPool();

	    databaseType = DBUtil.getDatabaseType(driverClass);
	    databaseName = DBUtil.getDatabaseName(url, driverClass);
	} catch (Exception e) {
	    e.printStackTrace();
	}
    }

    public Connection getConnection() {
	try {
	    return pool.getConnection();
	} catch (SQLException e) {
	    System.out.println("获取数据库连接失败!");
	    e.printStackTrace();
	}
	return null;
    }

    /**
     * 返回到连接池
     * 
     * @param conn
     */
    public void close(Connection conn) {
	pool.returnConnection(conn);
    }

    /**
     * 得到所有的表的信息
     * 
     * @return
     */
    public List<Table> getAllTable() {
	List<String> tableNameList = getAllTableName();
	List<Table> tableList = new ArrayList<Table>();
	for (int i = 0; i < tableNameList.size(); i++) {
	    String tableName = tableNameList.get(i);
	    Table table = getAllColumn(tableName);
	    tableList.add(table);
	}
	return tableList;
    }

    /**
     * 获取表的所有的主键
     * 
     * @param tableName
     * @return
     */
    public String[] getAllPrimaryKey(String tableName) {
	Connection conn = getConnection();
	ResultSet rs = null;
	StringBuffer buffer = new StringBuffer();
	try {
	    DatabaseMetaData metaData = conn.getMetaData();
	    rs = metaData.getPrimaryKeys(null, null, tableName);
	    while (rs.next()) {
		buffer.append(rs.getString("COLUMN_NAME")).append(",");
	    }
	    if (buffer.toString().length() > 0) {
		String[] primaryKey = buffer.substring(0,
			buffer.lastIndexOf(",")).split(",");
		return primaryKey;
	    } else {
		System.out.println("表:" + tableName + "中没有主键!");
	    }
	} catch (SQLException e) {
	    System.out.println("Exception buffer is:" + buffer.toString());
	    e.printStackTrace();
	}
	return null;
    }

    /**
     * 根据TableName得到表的所有列
     * 
     * @param tableName
     * @return
     */
    public Table getAllColumn(String tableName) {
	if (ValidUtil.isEmpty(tableName)) {
	    return null;
	}
	Connection conn = getConnection();
	Table table = new Table();
	try {
	    table.setName(tableName);
	    table.setClassName(DBUtil.getClassName(tableName));
	    String sql = "SELECT * FROM " + tableName;
	    PreparedStatement ps = conn.prepareStatement(sql);
	    ResultSet rs = ps.executeQuery();
	    ResultSetMetaData metaData = rs.getMetaData();
	    int columnCount = metaData.getColumnCount();
	    List<Column> columnList = new ArrayList<Column>();
	    for (int i = 1; i <= columnCount; i++) {
		String columnName = metaData.getColumnName(i);
		int columnType = metaData.getColumnType(i);
		String columnTypeName = metaData.getColumnTypeName(i);
		int precision = metaData.getPrecision(i);
		int scale = metaData.getScale(i);
		String scaleStr = "";
		if (scale != 0) {
		    scaleStr = "," + scale;
		}
		String jdbcType = JdbcTypeUtil.getJdbcType(columnType);
		Column column = new Column();
		column.setColumnName(columnName);
		column.setColumnType(columnTypeName);
		column.setFieldName(DBUtil.getFieldName(columnName));// 属性名
		column.setJdbcType(jdbcType);// jdbcType
		column.setLength(precision + scaleStr);// 数据库字段长度
		column.setRemark("");// 备注
		columnList.add(column);
	    }
	    table.setColumnList(columnList);
	} catch (SQLException e) {
	    System.out.println("读取表信息失败!");
	    e.printStackTrace();
	} finally {
	    close(conn);
	}
	return table;
    }

    /**
     * 得到所有当前数据库用户的所有的表名
     * 
     * @return
     */
    public List<String> getAllTableName() {
	Connection conn = getConnection();
	ResultSet rs = null;
	PreparedStatement ps = null;
	List<String> tableNameList = new ArrayList<String>();
	String sql = "";
	if (databaseType.equals("mysql")) {
	    sql = "SELECT TABLE_NAME FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_SCHEMA='"
		    + databaseName + "'";
	} else if (databaseType.equals("oracle")) {
	    // select table_name from all_tables where owner='SCOTT';
	    // select object_name from sys.all_objects where owner='SCOTT' and
	    // OBJECT_TYPE
	    // ='TABLE';
	    sql = "SELECT TABLE_NAME FROM USER_TABLES";
	} else if (databaseType.equals("sqlserver")) {
	    // SELECT name FROM sys.sysobjects WHERE TYPE='U'
	    // SELECT name  FROM sys.tables WHERE TYPE='U'
	    // SELECT name  FROM sys.objects WHERE TYPE='U'
	    sql = "SELECT name  FROM sys.tables WHERE TYPE='U'";
	}
	try {
	    ps = conn.prepareStatement(sql);
	    rs = ps.executeQuery();
	    while (rs.next()) {
		String tableName = rs.getString(1);
		System.out.println(tableName);
		tableNameList.add(tableName);
	    }
	} catch (SQLException e) {
	    e.printStackTrace();
	} finally {
	    try {
		rs.close();
		ps.close();
		close(conn);
	    } catch (SQLException e) {
		e.printStackTrace();
	    }
	}
	return tableNameList;
    }

    /**
     * 根据TableName生成insert语句
     * 
     * @param tableName
     * @return
     */
    public static String getInsertSql(String tableName) {
	return null;
    }

    /**
     * 根据TableName生成update语句
     * 
     * @param tableName
     * @return
     */
    public static String getUpdateSql(String tableName) {
	return null;
    }

    /**
     * 根据TableName生成select语句
     * 
     * @param tableName
     * @return
     */
    public static String getSelectSql(String tableName) {
	return null;
    }

    /**
     * 根据实体类对象得到创建表的SQL
     * 
     * @param object
     * @return
     */
    public static String executeCreateTabelSql(Class classes) {
	return executeCreateTableSql(classes, false);
    }

    public static String executeCreateTableSql(Class classes, boolean isExecute) {
	if (!ValidUtil.isEmpty(classes)) {

	}
	return "";
    }

    /**
     * 
     * @param sql
     * @return
     */
    public static String generatorSqlFile(String sql) {
	return null;
    }

    public static <T> List<T> getData(String sql, Class<T> classes) {

	return null;
    }

    /**
     * 根据TableName查询insert update selectSQL语句
     * 
     * @param tableName
     */
    public void autoCreatSql(String tableName) {
	Connection connection = getConnection();
	String sql = "select * from " + tableName + " limit 1";
	try {
	    Statement stmt = connection.createStatement();
	    ResultSet rs = stmt.executeQuery(sql);
	    ResultSetMetaData data = rs.getMetaData();

	    String selectSql = "select ";
	    String insertSql = "insert into " + tableName + " ( ";
	    String updateSql = "update " + tableName + " set ";
	    String beanFileds = "\r\n";
	    String getFileds = "\r\n";

	    for (int i = 1; i <= data.getColumnCount(); i++) {
		// 获得指定列的列名
		String columnName = data.getColumnName(i);
		// 获得指定列的数据类型名
		String columnTypeName = data.getColumnTypeName(i);
		selectSql += (i == (data.getColumnCount()) ? columnName
			: columnName + ", ");
		insertSql += (i == (data.getColumnCount()) ? columnName + ")"
			: columnName + ", ");
		updateSql += (i == (data.getColumnCount()) ? columnName + "=?"
			: columnName + "=?, ");
		beanFileds += "private String " + columnName + ";   //"
			+ columnTypeName + "\r\n";
		getFileds += tableName + "." + "get"
			+ columnName.substring(0, 1).toUpperCase()
			+ columnName.substring(1) + "()" + "\r\n";
		;
	    }

	    insertSql += "values(";
	    for (int j = 1; j <= data.getColumnCount(); j++) {
		insertSql += (j == (data.getColumnCount()) ? "?)" : "?, ");
	    }

	    selectSql += " from " + tableName + " where 1=1 ";
	    updateSql += " where 1=1 ";
	    System.out.println(selectSql + "\r\n");
	    System.out.println(insertSql + "\r\n");
	    System.out.println(updateSql + "\r\n");
	    System.out.println(getFileds);
	    System.out.println(beanFileds);

	    close(connection);
	} catch (SQLException e) {
	    e.printStackTrace();
	}
    }

    /**
     * 根据查询条件
     * 
     * @param sql
     * @return
     */
    // public static String createSqlText(String sql){
    //
    // }
    //
    // public static List<Object> getObjectBySql(String sql){
    //
    // }

    public static void main(String[] args) throws SQLException {
	DaoHelper helper = new DaoHelper();
	String[] str = helper.getAllPrimaryKey("adj");
	System.out.println(ArrayUtil.toString(str));
    }
}
