package com.common.log;

public final class DynamicExtendDailyRollingFileAppender extends
	ExtendDailyRollingFileAppender {
    protected String getEnv() {
	return "";
    }
}
