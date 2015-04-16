package com.common.util.html;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import net.sf.json.JsonConfig;
import net.sf.json.processors.JsonValueProcessor;

public class JsonConfigDate {
	
	public static JsonConfig getJsonConfig(){
		JsonConfig config=new JsonConfig();
		config.registerJsonValueProcessor(Date.class, new JsonValueProcessor() {
			
			private DateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			
			public Object processObjectValue(String key, Object value, JsonConfig arg2) {
				if("".equals(value)){
					return "";
				}
				if(value instanceof Date){
					return format.format((Date)value);
				}
				return value.toString();
			}
			
			public Object processArrayValue(Object arg0, JsonConfig arg1) {
				return null;
			}
		});
		return config;
	}
}
