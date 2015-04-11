package com.coscon.shipsuite.common.log;

public final class DynamicExtendDailyRollingFileAppender extends
	ExtendDailyRollingFileAppender {
    protected String getEnv() {
	return "";
    }
}
