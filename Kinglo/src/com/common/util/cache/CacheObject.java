package com.common.util.cache;

import java.io.Serializable;

public final class CacheObject<T> implements Serializable {
    
    private static final long serialVersionUID = 7281102000107432046L;
    private T cacheObject;
    private long updateTime = 0L;
    private long durationMills = 0L;

    public CacheObject(T theObject) {
	this(theObject, 0L);
    }

    public CacheObject(T theObject, long theDurationMills) {
	this.cacheObject = theObject;
	this.updateTime = System.currentTimeMillis();
	this.durationMills = theDurationMills;
    }

    public long getDuration() {
	return this.durationMills;
    }

    public T getObjectItemAnyway() {
	return this.cacheObject;
    }

    public T getObjectWithinDuration() {
	if (isNotOverTime()) {
	    return this.cacheObject;
	}
	return null;
    }

    public boolean isNotOverTime() {
	if (this.durationMills > 0L) {
	    return System.currentTimeMillis() - this.updateTime < this.durationMills;
	}
	return true;
    }

    public void refresh() {
	this.updateTime = System.currentTimeMillis();
    }

    public void setDuration(long theDurationMills) {
	this.durationMills = theDurationMills;
    }
}
