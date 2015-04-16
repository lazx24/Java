package com.common.util.cache;

import com.common.context.CommonConstant;
import com.common.util.timetask.GenericTimeTask;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock.ReadLock;
import java.util.concurrent.locks.ReentrantReadWriteLock.WriteLock;

@SuppressWarnings("all")
public final class CacheManager<T> implements ICacheManager<T> {
    private static final ReentrantReadWriteLock locker = new ReentrantReadWriteLock();
    private static Map<Object, CacheManager> instanceMap = new WeakHashMap();
    private Map<String, CacheObject<T>> cachePool = new ConcurrentHashMap();
    private static int DEFAULT_RECYCLE_DURATION_SECONDS = 60;

    public CacheManager() {
    }

    static final class CacheRecycler extends GenericTimeTask {
	public CacheRecycler(ICacheManager cacheManager,
		int recycleDurationSeconds) {
	    super(cacheManager,recycleDurationSeconds);
	}
    }

    private static class CacheManagerHolder {
	static final CacheManager instance = new CacheManager();

	static {
	    new CacheManager.CacheRecycler(instance,
		    CacheManager.DEFAULT_RECYCLE_DURATION_SECONDS);
	}
    }

    public CacheManager(int recycleDurationSeconds) {
	setDurationMills(recycleDurationSeconds * 1000);
    }

    public static CacheManager getInstance() {
	return CacheManagerHolder.instance;
    }

    public static <T> CacheManager<T> getInstance(Class<T> cls) {
	locker.readLock().lock();
	CacheManager<T> cm = (CacheManager<T>) instanceMap.get(cls);
	if (cm == null) {
	    locker.readLock().unlock();
	    locker.writeLock().lock();
	    try {
		cm = (CacheManager<T>) instanceMap.get(cls);
		if (cm == null) {
		    cm = new CacheManager<T>();

		    new CacheRecycler(cm, DEFAULT_RECYCLE_DURATION_SECONDS);

		    instanceMap.put(cls, cm);
		}
	    } finally {
		locker.writeLock().unlock();
	    }
	} else {
	    locker.readLock().unlock();
	}
	return cm;
    }

    public CacheObject<T> getCacheObject(String key) {
	if (key == null) {
	    return null;
	}
	key = key.trim();

	CacheObject<T> ObjInPool = (CacheObject<T>) this.cachePool.get(key);
	if (ObjInPool == null) {
	    return null;
	}
	return ObjInPool;
    }

    public T getValueAnyway(String key) {
	CacheObject<T> theCacheObject = getCacheObject(key);
	if (theCacheObject != null) {
	    return theCacheObject.getObjectItemAnyway();
	}
	return null;
    }

    public T getValueWithinDuration(String key) {
	CacheObject<T> theCacheObject = getCacheObject(key);
	if (theCacheObject != null) {
	    return theCacheObject.getObjectWithinDuration();
	}
	return null;
    }

    public Set<String> getOverTimeKeySet() {
	Set<String> overTimeKeySet = new HashSet<String>();
	Set<String> keySet = this.cachePool.keySet();
	for (String key : keySet) {
	    CacheObject<T> item = (CacheObject<T>) this.cachePool.get(key);
	    if (!item.isNotOverTime()) {
		overTimeKeySet.add(key);
	    }
	}
	return overTimeKeySet;
    }

    public boolean isNotOverTime(String key) {
	if (key == null) {
	    return false;
	}
	key = key.trim();

	CacheObject<T> ObjInPool = (CacheObject<T>) this.cachePool.get(key);
	if (ObjInPool == null) {
	    return false;
	}
	CacheObject<T> theCacheObject = ObjInPool;

	boolean isNotOverTime = theCacheObject.isNotOverTime();
	if (!isNotOverTime) {
	    theCacheObject = null;
	}
	return isNotOverTime;
    }

    public Set<String> keySet() {
	return this.cachePool.keySet();
    }

    public boolean refreshCacheObjectUpdatetTime(String key) {
	CacheObject<T> obj = getCacheObject(key);
	if (obj == null) {
	    return false;
	}
	return obj.isNotOverTime();
    }

    public void removeCacheObject(String key) {
	if (key == null) {
	    return;
	}
	key = key.trim();

	CacheObject<T> obj = (CacheObject<T>) this.cachePool.get(key);
	if (obj != null) {
	    this.cachePool.remove(key);
	    obj = null;
	}
    }

    public void removeOverTimeObject() {
	Set<String> keySet = new HashSet<String>();
	keySet.addAll(getOverTimeKeySet());
	int i = 0;
	for (String key : keySet) {
	    if (!getCacheObject(key).isNotOverTime()) {
		removeCacheObject(key);
		i++;
	    }
	}
    }

    public void saveCacheObjectForever(String key, T value) {
	saveCacheObject(key, value, 0L);
    }

    public void saveCacheObject(String key, T value) {
	saveCacheObject(key, value, CommonConstant.CACHE_DURATION_MINUTES * 60);
    }

    public void saveCacheObject(String key, T value, long durationSeconds) {
	saveCacheObjectMills(key, value, durationSeconds * 1000L);
    }

    public void saveCacheObjectMills(String key, T value, long durationMills) {
	if (key == null) {
	    return;
	}
	key = key.trim();

	CacheObject<T> theObj = new CacheObject<T>(value, durationMills);

	this.cachePool.put(key, theObj);
    }

    public void setDurationMills(long durationMills) {
	Set<String> keySet = this.cachePool.keySet();
	for (String key : keySet) {
	    CacheObject<T> item = (CacheObject<T>) this.cachePool.get(key);
	    item.setDuration(durationMills);
	}
    }

    public int size() {
	return this.cachePool.size();
    }

    public List<T> values() {
	Collection<CacheObject<T>> cacheValueCollection = this.cachePool
		.values();
	List<T> valuesList = new ArrayList<T>(cacheValueCollection.size());
	for (CacheObject<T> cacheObj : cacheValueCollection) {
	    valuesList.add(cacheObj.getObjectItemAnyway());
	}
	return valuesList;
    }

    public List<T> valuesWithinDuration() {
	Collection<CacheObject<T>> cacheValueCollection = this.cachePool
		.values();
	List<T> valuesList = new ArrayList<T>(cacheValueCollection.size());
	for (CacheObject<T> cacheObj : cacheValueCollection) {
	    valuesList.add(cacheObj.getObjectWithinDuration());
	}
	return valuesList;
    }

    public void clear() {
	this.cachePool.clear();
    }

    public void processTask() {
	removeOverTimeObject();
    }
}
