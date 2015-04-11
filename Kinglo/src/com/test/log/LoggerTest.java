package com.test.log;

import org.apache.log4j.Level;

import com.coscon.shipsuite.common.log.DynamicLog4j;
import com.coscon.shipsuite.common.log.ISystemLogger;
import com.coscon.shipsuite.common.log.LoggerFactory;
import com.coscon.shipsuite.common.util.file.PropertyLoader;

public class LoggerTest {

    public static ISystemLogger logger = LoggerFactory
	    .getSystemLogger(LoggerTest.class);

    public static void main(String[] args) {
	// 初始化日志组件
	/******************************************************/
	boolean log4jProfileEnabled = Boolean.parseBoolean(PropertyLoader
		.getDefaultProperty("log4j.profile.enabled", "true"));
	if (log4jProfileEnabled) {
	    String log4jRootPath = PropertyLoader.getDefaultProperty(
		    "log4j.root.path", "c:\\log\\System.log");

	    boolean enableDebug = Boolean.valueOf(PropertyLoader
		    .getDefaultProperty("log4j.profile.debug", "false"));
	    DynamicLog4j.newLog4j(log4jRootPath, "userId",
		    "", enableDebug);

	}
	LoggerFactory.setRootLevel(Level.DEBUG);
	logger.debug("OS name: "
		+ System.getProperties().getProperty("os.name")
			.replaceAll(" ", ""));

	 /******************************************************/
	 
	 try {
	    int i=9/0;
	} catch (Exception e) {
	    logger.error(e);
	}

    }
}
