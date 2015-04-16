package com.common.util.cache;

import com.common.util.timetask.ITimeTaskHandler;

import java.util.List;
import java.util.Set;

public abstract interface ICacheManager<T> extends ITimeTaskHandler {
    public abstract CacheObject<T> getCacheObject(String paramString);

    public abstract T getValueAnyway(String paramString);

    public abstract T getValueWithinDuration(String paramString);

    public abstract Set<String> getOverTimeKeySet();

    public abstract boolean isNotOverTime(String paramString);

    public abstract Set<String> keySet();

    public abstract boolean refreshCacheObjectUpdatetTime(String paramString);

    public abstract void removeCacheObject(String paramString);

    public abstract void removeOverTimeObject();

    public abstract void saveCacheObjectForever(String paramString, T paramT);

    public abstract void saveCacheObject(String paramString, T paramT);

    public abstract void saveCacheObject(String paramString, T paramT,
	    long paramLong);

    public abstract void saveCacheObjectMills(String paramString, T paramT,
	    long paramLong);

    public abstract void setDurationMills(long paramLong);

    public abstract int size();

    public abstract List<T> values();

    public abstract List<T> valuesWithinDuration();
}
