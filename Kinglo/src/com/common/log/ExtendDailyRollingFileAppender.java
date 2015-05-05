package com.common.log;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.Hashtable;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.apache.log4j.DailyRollingFileAppender;
import org.apache.log4j.spi.LoggingEvent;

import com.common.util.date.DateUtil;
import com.common.util.file.FileUtil;
import com.common.util.file.PropertyLoader;
import com.common.util.string.StringUtil;

public class ExtendDailyRollingFileAppender extends DailyRollingFileAppender {
    private static final ReentrantReadWriteLock locker = new ReentrantReadWriteLock();
    protected int maxBackupIndex = 30;
    private Map<String, Boolean> dateLogMap = new Hashtable<String, Boolean>();
    private volatile String appEnv = "";
    private volatile String userId = "";
    private volatile String cachedEnv = null;

    protected String getEnv() {
	locker.readLock().lock();
	if (StringUtil.isNullOrEmpty(this.cachedEnv)) {
	    locker.readLock().unlock();
	    try {
		locker.writeLock().lock();
		this.cachedEnv = PropertyLoader.getProperty(
			"properties/deploy.properties", "deploy.env", "");
	    } catch (Exception localException) {
	    } finally {
		if (this.cachedEnv == null) {
		    this.cachedEnv = "";
		}
		locker.writeLock().unlock();
	    }
	} else {
	    locker.readLock().unlock();
	}
	return this.cachedEnv;
    }

    protected String getUserId() {
	return this.userId;
    }

    public synchronized void setFile(String fileName, boolean append,
	    boolean bufferedIO, int bufferSize) throws IOException {
	String fileMainName = FileUtil.getPathOfFile(fileName) + "/"
		+ FileUtil.getFilenameMain(fileName);

	String fileExtendName = FileUtil.getFilenameExtend(fileName);
	if (StringUtil.isNullOrEmpty(fileExtendName)) {
	    fileExtendName = "log";
	}
	StringBuffer fixedName = new StringBuffer();
	String userIdCurrent = getUserId();
	if (StringUtil.isNotNullAndNotEmpty(userIdCurrent)) {
	    fixedName.append(".").append(userIdCurrent);
	}
	String envCurrent = getEnv();
	if (StringUtil.isNotNullAndNotEmpty(envCurrent)) {
	    fixedName.append(".").append(envCurrent);
	}
	String newFileName;
	if (fileName.endsWith(String.format("%s.%s",
		new Object[] { fixedName.toString(), fileExtendName }))) {
	    newFileName = fileName;
	} else {
	    newFileName = String.format("%s%s.%s", new Object[] { fileMainName,
		    fixedName.toString(), fileExtendName });
	}
	super.setFile(newFileName, append, bufferedIO, bufferSize);
    }

    private void removeHistoryLog(String dateString) {
	if (((Boolean) this.dateLogMap.get(dateString)).booleanValue()) {
	    return;
	}
	Set<File> logSet = FileUtil.findMatchingFileSystemResources(
		FileUtil.getPathOfFile(this.fileName),
		FileUtil.getPureFilename(this.fileName) + "*");

	Calendar deadlineCalendar = Calendar.getInstance();
	deadlineCalendar.add(5, -this.maxBackupIndex - 1);
	Calendar calendarFile = Calendar.getInstance();
	for (File f : logSet) {
	    calendarFile.setTime(new Date(f.lastModified()));
	    if (deadlineCalendar.after(calendarFile)) {
		try {
		    f.delete();
		} catch (Exception localException) {
		}
	    }
	}
	this.dateLogMap.put(dateString, Boolean.valueOf(true));
    }

    public void setMaxBackupIndex(int maxBackups) {
	this.maxBackupIndex = maxBackups;
    }

    protected void subAppend(LoggingEvent event) {
	boolean changed = false;
	String appEnvNow = getEnv();
	if (appEnvNow == null) {
	    appEnvNow = "";
	}
	if (!StringUtil.isEqual(this.appEnv, appEnvNow)) {
	    this.appEnv = appEnvNow;
	    changed = true;
	}
	String userIdNow = getUserId();
	if (userIdNow == null) {
	    userIdNow = "";
	}
	if (!StringUtil.isEqual(this.userId, userIdNow)) {
	    this.userId = userIdNow;
	    changed = true;
	}
	if (changed) {
	    activateOptions();
	}
	super.subAppend(event);
	locker.readLock().lock();
	if ((this.maxBackupIndex > 0)
		&& (!this.dateLogMap.keySet().contains(
			DateUtil.dateToString(new Date())))) {
	    locker.readLock().unlock();
	    locker.writeLock().lock();
	    try {
		String dateString = DateUtil.dateToString(new Date());
		this.dateLogMap.put(dateString, Boolean.valueOf(false));
		removeHistoryLog(dateString);
	    } finally {
		locker.writeLock().unlock();
	    }
	} else {
	    locker.readLock().unlock();
	}
    }
}
