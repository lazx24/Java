package com.coscon.shipsuite.common.log;

import com.coscon.shipsuite.common.util.file.FileUtil;
import com.coscon.shipsuite.common.util.string.StringUtil;
import java.util.Properties;
import org.apache.log4j.PropertyConfigurator;

public class DynamicLog4j {
    private static final int DEFAULT_MAX_BACKUP_INDEX = 7;
    private static final String DEFAULT_DATE_PATTERN = "'.'yyyy-MM-dd";
    private static final String DEFAULT_CONVERSION_PATTERN = "%d - %c -%-4r [%t] %-5p %x - %m%n";

    public static void newLog4j(String logPath, String userId, String env,
	    boolean enableDebug) {
	newLog4j(logPath, userId, env, enableDebug, 7, "'.'yyyy-MM-dd");
    }

    public static void newLog4j(String logPath, String userId, String env,
	    boolean enableDebug, int maxBackupIndex, String rollingDatePattern) {
	if (StringUtil.isNullOrEmpty(rollingDatePattern)) {
	    rollingDatePattern = "'.'yyyy-MM-dd";
	}
	Properties pro = new Properties();
	pro.put("log4j.rootLogger", "C,I,E");
	pro.put("log4j.appender.C", "org.apache.log4j.ConsoleAppender");
	pro.put("log4j.appender.C.Threshold", "DEBUG");
	pro.put("log4j.appender.C.ImmediateFlush", "true");
	pro.put("log4j.appender.C.layout", "org.apache.log4j.PatternLayout");
	pro.put("log4j.appender.C.layout.ConversionPattern",
		"%d - %c -%-4r [%t] %-5p %x - %m%n");

	pro.put("log4j.appender.I",
		DynamicExtendDailyRollingFileAppender.class.getName());
	String fileMainName = FileUtil.getPathOfFile(logPath) + "/"
		+ FileUtil.getFilenameMain(logPath);

	String fileExtendName = FileUtil.getFilenameExtend(logPath);
	if (StringUtil.isNullOrEmpty(fileExtendName)) {
	    fileExtendName = "log";
	}
	StringBuffer filenameSb = new StringBuffer(fileMainName);
	if (StringUtil.isNotNullAndNotEmpty(userId)) {
	    filenameSb.append(".").append(userId);
	}
	if (StringUtil.isNotNullAndNotEmpty(env)) {
	    filenameSb.append(".").append(env);
	}
	filenameSb.append(".").append(fileExtendName);
	pro.put("log4j.appender.I.File", filenameSb.toString());
	pro.put("log4j.appender.I.Threshold", enableDebug ? "DEBUG" : "INFO");
	pro.put("log4j.appender.I.MaxBackupIndex",
		Integer.valueOf(maxBackupIndex));
	pro.put("log4j.appender.I.DatePattern", rollingDatePattern);
	pro.put("log4j.appender.I.layout", "org.apache.log4j.PatternLayout");
	pro.put("log4j.appender.I.layout.ConversionPattern",
		"%d - %c -%-4r [%t] %-5p %x - %m%n");

	pro.put("log4j.appender.E",
		DynamicExtendDailyRollingFileAppender.class.getName());

	filenameSb.setLength(0);
	filenameSb.append(fileMainName).append("_Error");
	if (StringUtil.isNotNullAndNotEmpty(userId)) {
	    filenameSb.append(".").append(userId);
	}
	if (StringUtil.isNotNullAndNotEmpty(env)) {
	    filenameSb.append(".").append(env);
	}
	filenameSb.append(".").append(fileExtendName);

	pro.put("log4j.appender.E.File", filenameSb.toString());
	pro.put("log4j.appender.E.Threshold", "ERROR");
	pro.put("log4j.appender.E.MaxBackupIndex",
		Integer.valueOf(maxBackupIndex));
	pro.put("log4j.appender.E.DatePattern", rollingDatePattern);
	pro.put("log4j.appender.E.layout", "org.apache.log4j.PatternLayout");
	pro.put("log4j.appender.E.layout.ConversionPattern",
		"%d - %c -%-4r [%t] %-5p %x - %m%n");

	PropertyConfigurator.configure(pro);
    }
}
