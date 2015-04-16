package com.common.log;

import com.common.exception.ShipSuiteRuntimeException;
import com.common.util.string.IMessageHelper;
import com.common.util.string.StringUtil;

import org.apache.log4j.Logger;

public final class SystemLoggerImpl implements ISystemLogger {
    private String className;
    Logger logger = null;
    private IMessageHelper logHelper;

    public SystemLoggerImpl(String componentName, String className) {
	this(componentName, className, null);
    }

    public SystemLoggerImpl(String componentName, String className,
	    IMessageHelper logHelper) {
	this.className = className;
	this.logHelper = logHelper;
	if ((StringUtil.isNullOrEmpty(componentName))
		&& (StringUtil.isNotNullAndNotEmpty(className))) {
	    this.logger = Logger.getLogger(className);
	} else if (StringUtil.isNotNullAndNotEmpty(componentName)) {
	    this.logger = Logger.getLogger(componentName);
	} else {
	    this.logger = Logger.getLogger("System");
	}
	if (this.logger == null) {
	    throw new ShipSuiteRuntimeException(
		    "Can't get looger, please check the log4j.propertites.");
	}
    }

    public void debug(String logMessageKey) {
	if (this.logger.isDebugEnabled()) {
	    StringBuffer sb = new StringBuffer();
	    sb.append(this.className);
	    sb.append(": ");
	    sb.append(" D ");
	    sb.append(getLogString(logMessageKey));
	    this.logger.debug(sb.toString());
	}
    }

    public void debug(String logMessageKey, String logText) {
	if (this.logger.isDebugEnabled()) {
	    StringBuffer sb = new StringBuffer();
	    sb.append(this.className);
	    sb.append(": ");
	    sb.append(" D ");
	    sb.append(getLogString(logMessageKey));
	    sb.append("[");
	    sb.append(logText);
	    sb.append("]");
	    this.logger.debug(sb.toString());
	}
    }

    public void error(String logMessageKey) {
	StringBuffer sb = new StringBuffer();
	sb.append(this.className);
	sb.append(": ");
	sb.append(" E ");
	sb.append(getLogString(logMessageKey));
	this.logger.error(sb.toString());
    }

    public void error(String logMessageKey, String logText) {
	StringBuffer sb = new StringBuffer();
	sb.append(this.className);
	sb.append(": ");
	sb.append(" E ");
	sb.append(getLogString(logMessageKey));
	sb.append("[");
	sb.append(logText);
	sb.append("]");

	this.logger.error(sb.toString());
    }

    public void error(String logMessageKey, String logText, Throwable ex) {
	StringBuffer sb = new StringBuffer();
	sb.append(logText);
	sb.append(StringUtil.throwableToString(ex));
	error(logMessageKey, sb.toString());
    }

    public void error(String logMessageKey, String[] logMessageArgs) {
	StringBuffer sb = new StringBuffer();
	sb.append(this.className);
	sb.append(": ");
	sb.append(" E ");
	sb.append(getLogString(logMessageKey, logMessageArgs));

	this.logger.error(sb.toString());
    }

    public void error(String logMessageKey, Throwable exception) {
	error(logMessageKey, null, exception);
    }

    public void error(Throwable ex) {
	error(null, StringUtil.throwableToString(ex));
    }

    public void fatal(String logMessageKey) {
	StringBuffer sb = new StringBuffer();
	sb.append(this.className);
	sb.append(": ");
	sb.append(" F ");
	sb.append(getLogString(logMessageKey));
	this.logger.fatal(sb.toString());
    }

    public void fatal(String logMessageKey, String logText) {
	StringBuffer sb = new StringBuffer();
	sb.append(this.className);
	sb.append(": ");
	sb.append(" F ");
	sb.append(getLogString(logMessageKey));
	sb.append("[");
	sb.append(logText);
	sb.append("]");
	this.logger.fatal(sb.toString());
    }

    public void fatal(String logMessageKey, String logText, Throwable ex) {
	StringBuffer sb = new StringBuffer();
	sb.append(logText);
	sb.append(StringUtil.throwableToString(ex));
	fatal(logMessageKey, sb.toString());
    }

    public void fatal(String logMessageKey, Throwable exception) {
	fatal(logMessageKey, null, exception);
    }

    public void fatal(Throwable error) {
	fatal(null, StringUtil.throwableToString(error));
    }

    public IMessageHelper getLogHelper() {
	return this.logHelper;
    }

    String getLogString(String messageKey) {
	String msg = this.logHelper.getMessageString(messageKey, new String[0]);
	return msg == null ? "" : msg;
    }

    String getLogString(String messageKey, String[] messageArgs) {
	String msg = this.logHelper.getMessageString(messageKey, messageArgs);
	return msg == null ? "" : msg;
    }

    public void info(String logMessageKey) {
	if (this.logger.isInfoEnabled()) {
	    StringBuffer sb = new StringBuffer();
	    sb.append(this.className);
	    sb.append(": ");
	    sb.append(" I ");
	    sb.append(getLogString(logMessageKey));

	    this.logger.info(sb.toString());
	}
    }

    public void info(String logMessageKey, String logText) {
	if (this.logger.isInfoEnabled()) {
	    StringBuffer sb = new StringBuffer();
	    sb.append(this.className);
	    sb.append(": ");
	    sb.append(" I ");
	    sb.append(getLogString(logMessageKey));
	    sb.append("[");
	    sb.append(logText);
	    sb.append("]");
	    this.logger.info(sb.toString());
	}
    }

    public boolean isDebugEnabled() {
	return this.logger.isDebugEnabled();
    }

    public boolean isInfoEnabled() {
	return this.logger.isInfoEnabled();
    }

    public void setLogHelper(IMessageHelper logHelper) {
	this.logHelper = logHelper;
    }

    public void trace(String logMessageKey) {
	StringBuffer sb = new StringBuffer();
	sb.append(this.className);
	sb.append(": ");
	sb.append(" T ");
	sb.append(getLogString(logMessageKey));
	this.logger.trace(sb.toString());
    }

    public void trace(String logMessageKey, String logText) {
	StringBuffer sb = new StringBuffer();
	sb.append(this.className);
	sb.append(": ");
	sb.append(" T ");
	sb.append(getLogString(logMessageKey));
	sb.append("[");
	sb.append(logText);
	sb.append("]");

	this.logger.trace(sb.toString());
    }

    public void trace(String logMessageKey, String[] logMessageArgs) {
	StringBuffer sb = new StringBuffer();
	sb.append(this.className);
	sb.append(": ");
	sb.append(" T ");
	sb.append(getLogString(logMessageKey, logMessageArgs));

	this.logger.trace(sb.toString());
    }

    public void warn(String logMessageKey) {
	StringBuffer sb = new StringBuffer();
	sb.append(this.className);
	sb.append(": ");
	sb.append(" W ");
	sb.append(getLogString(logMessageKey));
	this.logger.info(sb.toString());
    }

    public void warn(String logMessageKey, String logText) {
	StringBuffer sb = new StringBuffer();
	sb.append(this.className);
	sb.append(": ");
	sb.append(" W ");
	sb.append(getLogString(logMessageKey));
	sb.append("[");
	sb.append(logText);
	sb.append("]");

	this.logger.warn(sb.toString());
    }

    public void warn(String logMessageKey, String[] logMessageArgs) {
	StringBuffer sb = new StringBuffer();
	sb.append(this.className);
	sb.append(": ");
	sb.append(" W ");
	sb.append(getLogString(logMessageKey, logMessageArgs));

	this.logger.warn(sb.toString());
    }
}
