package com.coscon.shipsuite.common.util.file;

import java.util.Properties;

public final class PropertyLoader extends AbstractPropertyLoader {
    protected static volatile String defaultPropertyFilename = "";

    static {
	defaultPropertyDir = "";
    }

    public static String getDefaultProperty(String key) {
	if (key == null) {
	    throw new RuntimeException("Can not load NULL key!");
	}
	Properties myResource = loadProperties(getDefaultPropertyFilename());
	if (myResource != null) {
	    return myResource.getProperty(key);
	}
	return null;
    }

    public static String getDefaultProperty(String key, String defaultVal) {
	if (key == null) {
	    throw new RuntimeException("Can not load NULL key!");
	}
	Properties myResource = loadProperties(getDefaultPropertyFilename());
	if (myResource != null) {
	    return myResource.getProperty(key, defaultVal);
	}
	return defaultVal;
    }

    public static String getDefaultPropertyDir() {
	return defaultPropertyDir;
    }

    public static String getDefaultPropertyFilename() {
	return defaultPropertyFilename;
    }

    public static void setDefaultPropertyDir(String propertyDir) {
	defaultPropertyDir = propertyDir;
    }

    public static void setDefaultPropertyFilename(String property) {
	defaultPropertyFilename = property;
    }
}
