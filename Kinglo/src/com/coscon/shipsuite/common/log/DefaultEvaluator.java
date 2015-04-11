package com.coscon.shipsuite.common.log;

import org.apache.log4j.Level;
import org.apache.log4j.spi.LoggingEvent;
import org.apache.log4j.spi.TriggeringEventEvaluator;

class DefaultEvaluator implements TriggeringEventEvaluator {
    public boolean isTriggeringEvent(LoggingEvent event) {
	return event.getLevel().isGreaterOrEqual(Level.ERROR);
    }
}
