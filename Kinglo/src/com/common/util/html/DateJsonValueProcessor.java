package com.common.util.html;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import net.sf.json.JsonConfig;
import net.sf.json.processors.JsonValueProcessor;

public class DateJsonValueProcessor implements JsonValueProcessor {
    public static final String DEFAULT_DATE_PATTERN = "yyyy-MM-dd HH:mm:sss.SSS";
    private DateFormat dateFormat;

    public DateJsonValueProcessor() {
	this("yyyy-MM-dd HH:mm:sss.SSS");
    }

    public DateJsonValueProcessor(String datePattern) {
	try {
	    this.dateFormat = new SimpleDateFormat(datePattern);
	} catch (Exception ex) {
	    this.dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:sss.SSS");
	}
    }

    public Object processArrayValue(Object value, JsonConfig jsonConfig) {
	return process(value);
    }

    public Object processObjectValue(String key, Object value,
	    JsonConfig jsonConfig) {
	return process(value);
    }

    private Object process(Object value) {
	if ((value instanceof Timestamp)) {
	    return this.dateFormat.format((Timestamp) value);
	}
	if ((value instanceof Date)) {
	    return this.dateFormat.format((Date) value);
	}
	if (value == null) {
	    return "";
	}
	return value.toString();
    }
}
