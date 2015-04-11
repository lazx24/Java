package com.coscon.shipsuite.common.log;

import com.coscon.shipsuite.common.util.cache.CacheManager;
import com.coscon.shipsuite.common.util.string.MessageHelper;
import com.coscon.shipsuite.common.util.string.StringUtil;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

public final class LoggerFactory {
    private static final ReentrantReadWriteLock locker = new ReentrantReadWriteLock();
    private static final CacheManager<ISystemLogger> loggerCacheManager = new CacheManager<ISystemLogger>();

    public static ISystemLogger getSystemLogger(Class cls) {
	String clsName = cls.getName();
	return getSystemLogger(null,
		clsName.substring(clsName.lastIndexOf(".") + 1));
    }

    public static ISystemLogger getSystemLogger(String componentName) {
	return getSystemLogger(componentName, null);
    }

    public static ISystemLogger getSystemLogger(String componentName,
	    String className) {
	if ((!StringUtil.isNullOrEmpty(componentName))
		&& (!StringUtil.isNullOrEmpty(className))) {
	    return null;
	}
	StringBuilder sb = new StringBuilder();
	if (componentName != null) {
	    sb.append(componentName);
	}
	if (className != null) {
	    if (componentName != null) {
		sb.append("-");
	    }
	    sb.append(className);
	}
	String id = sb.toString();
	ISystemLogger logger;
	try {
	    locker.readLock().lock();
	    logger = (ISystemLogger) loggerCacheManager.getValueAnyway(id);
	} finally {
	    locker.readLock().unlock();
	}
	if (logger == null) {
	    try {
		locker.writeLock().lock();
		logger = new SystemLoggerImpl(componentName, className,
			MessageHelper.getInstance());
		loggerCacheManager.saveCacheObject(id, logger, 0L);
	    } finally {
		locker.writeLock().unlock();
	    }
	}
	return logger;
    }

    public static void setRootLevel(Level level) {
	Logger.getRootLogger().setLevel(level);
    }
}
