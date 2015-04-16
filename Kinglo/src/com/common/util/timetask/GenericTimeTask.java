package com.common.util.timetask;

import com.common.log.LoggerFactory;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
/**
 * 
 * 类的描述:定时任务类
 * 创建人:邹建华
 * 创建时间:2015-4-11
 */
public class GenericTimeTask extends TimerTask {
    protected int processDurationSeconds = 60;
    protected Date firstTime = new Date();
    protected ITimeTaskHandler taskHandler;

    public GenericTimeTask() {
	scheduleTask();
    }

    public GenericTimeTask(ITimeTaskHandler taskHandler) {
	this.taskHandler = taskHandler;
	scheduleTask();
    }

    public GenericTimeTask(ITimeTaskHandler taskHandler,
	    int processDurationSeconds) {
	this.taskHandler = taskHandler;
	this.processDurationSeconds = processDurationSeconds;

	scheduleTask();
    }

    public GenericTimeTask(ITimeTaskHandler taskHandler, Date firstTime,
	    int processDurationSeconds) {
	this.firstTime = firstTime;
	this.taskHandler = taskHandler;
	this.processDurationSeconds = processDurationSeconds;

	scheduleTask();
    }

    public void run() {
	try {
	    if (this.taskHandler != null) {
		this.taskHandler.processTask();
	    } else {
		cancel();
	    }
	} catch (Exception ex) {
	    LoggerFactory.getSystemLogger(GenericTimeTask.class).error(ex);
	}
    }

    private void scheduleTask() {
	Timer timer = new Timer();

	timer.schedule(this, this.firstTime, this.processDurationSeconds * 1000);
    }
}
