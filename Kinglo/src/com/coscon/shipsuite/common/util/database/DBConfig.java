package com.coscon.shipsuite.common.util.database;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;
import com.coscon.shipsuite.common.util.string.StringUtil;

/**
 * 数据库配置文件类
 * 
 * @author zoujianhua
 * @time 2014-10-26
 * @version 1.0
 */
public class DBConfig {

    private static final String DEFAULT_FILE_NAME = "DB.properties";// 默认DB文件名

    private String fileName = DEFAULT_FILE_NAME;

    public String getFileName() {
	return fileName;
    }

    public void setFileName(String fileName) {
	this.fileName = fileName;
    }

    private static DBConfig config;

    private DBConfig() {
	initDBConfig();
    }

    public static DBConfig getInstance() {
	if (config == null) {
	    synchronized (DBConfig.class) {
		if (config == null) {
		    config = new DBConfig();
		}
	    }
	}
	return config;
    }

    private String url;

    private String username;

    private String password;

    private String driverClass;

    private int initPoolSize;

    private int incrementSize;

    public String getUrl() {
	return url;
    }

    public void setUrl(String url) {
	this.url = url;
    }

    public String getUsername() {
	return username;
    }

    public void setUsername(String username) {
	this.username = username;
    }

    public String getPassword() {
	return password;
    }

    public void setPassword(String password) {
	this.password = password;
    }

    public String getDriverClass() {
	return driverClass;
    }

    public void setDriverClass(String driverClass) {
	this.driverClass = driverClass;
    }

    public int getInitPoolSize() {
	return initPoolSize;
    }

    public void setInitPoolSize(int initPoolSize) {
	this.initPoolSize = initPoolSize;
    }

    public int getIncrementSize() {
	return incrementSize;
    }

    public void setIncrementSize(int incrementSize) {
	this.incrementSize = incrementSize;
    }

    public int getMaxPoolSize() {
	return maxPoolSize;
    }

    public void setMaxPoolSize(int maxPoolSize) {
	this.maxPoolSize = maxPoolSize;
    }

    private int maxPoolSize;

    private void initDBConfig() {
	String path = this.getClass().getClassLoader().getResource(fileName)
		.getPath();
	Properties pps = new Properties();
	try {
	    pps.load(new FileInputStream(path));
	    url = pps.getProperty("DB.url");
	    username = pps.getProperty("DB.username");
	    password = pps.getProperty("DB.password");
	    driverClass = pps.getProperty("DB.driverClass");
	    initPoolSize = StringUtil.stringToInt(pps
		    .getProperty("DB.initPoolSize"));
	    incrementSize = StringUtil.stringToInt(pps
		    .getProperty("DB.incrementSize"));
	    maxPoolSize = StringUtil.stringToInt(pps
		    .getProperty("DB.maxPoolSize"));
	} catch (FileNotFoundException e) {
	    e.printStackTrace();
	} catch (IOException e) {
	    e.printStackTrace();
	}
    }

    public static void main(String[] args) {
	config.getInstance();
	System.out.println(config.url);
	System.out.println(config.username);
	System.out.println(config.password);
    }
}
