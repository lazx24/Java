package com.coscon.shipsuite.common.util.cache;

import com.coscon.shipsuite.common.context.CommonConstant;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import net.rubyeye.xmemcached.KeyIterator;
import net.rubyeye.xmemcached.MemcachedClient;
import net.rubyeye.xmemcached.MemcachedClientBuilder;
import net.rubyeye.xmemcached.XMemcachedClientBuilder;
import net.rubyeye.xmemcached.exception.MemcachedException;
import net.rubyeye.xmemcached.utils.AddrUtil;

public final class MemcachedManager<T> implements ICacheManager<T> {
    private static final int EXP = 2592000;
    private static final long TIMEOUT = 30000L;
    private static final ReentrantReadWriteLock locker = new ReentrantReadWriteLock();
    private MemcachedClient client = null;

    private static class InstanceHolder {
	private static int DEFAULT_RECYCLE_DURATION_MINUTES = 1;
	static final MemcachedManager instance = new MemcachedManager();

	static {
	    new CacheManager.CacheRecycler(instance,
		    DEFAULT_RECYCLE_DURATION_MINUTES);
	}
    }

    private static String MEMCACHED_SERVERS = readFile("memcached.config", null);
    private static final String MEMCACHED_CONFIG = "memcached.config";

    public static InputStream getInputStream(String filename) {
	InputStream fis = null;
	try {
	    fis = new FileInputStream(new File(filename));
	} catch (FileNotFoundException ex) {
	    try {
		fis = new FileInputStream(new File(filename));
	    } catch (FileNotFoundException ex1) {
		fis = Thread.currentThread().getContextClassLoader()
			.getResourceAsStream(filename);
		if (fis == null) {
		    throw new RuntimeException(new FileNotFoundException(
			    filename));
		}
	    }
	}
	return fis;
    }

    public static String readFile(String filePath, String charsetName) {
	InputStreamReader inputStreamReader = null;
	BufferedReader bufferReader = null;
	try {
	    if (charsetName != null) {
		inputStreamReader = new InputStreamReader(
			getInputStream(filePath), charsetName);
	    } else {
		inputStreamReader = new InputStreamReader(
			getInputStream(filePath));
	    }
	    if (inputStreamReader == null) {
	    }
	    bufferReader = new BufferedReader(inputStreamReader);

	    StringBuffer sb = new StringBuffer();
	    String line;
	    while ((line = bufferReader.readLine()) != null) {
		sb.append(line);
	    }
	    return sb.toString();
	} catch (Exception ex) {
	    throw new RuntimeException(ex);
	} finally {
	    if (bufferReader != null) {
		try {
		    bufferReader.close();
		    bufferReader = null;
		} catch (IOException localIOException2) {
		}
	    }
	    if (inputStreamReader != null) {
		try {
		    inputStreamReader.close();
		    inputStreamReader = null;
		} catch (IOException localIOException3) {
		}
	    }
	}
    }

    private MemcachedClient getClient() {
	if (this.client == null) {
	    try {
		locker.writeLock().lock();
		try {
		    if (this.client == null) {
			MemcachedClientBuilder builder = new XMemcachedClientBuilder(
				AddrUtil.getAddresses(MEMCACHED_SERVERS));
			this.client = builder.build();
			this.client.flushAll();
		    }
		} catch (Exception e) {
		    e.printStackTrace();
		    throw new RuntimeException(e);
		}
	    } finally {
		locker.writeLock().unlock();
	    }
	    locker.writeLock().unlock();
	}
	return this.client;
    }

    public boolean hasClient() {
	try {
	    return getClient() != null;
	} catch (Exception e) {
	    e.printStackTrace();
	}
	return false;
    }

    public static MemcachedManager getInstance() {
	return InstanceHolder.instance;
    }

    public CacheObject<T> getCacheObject(String key) {
	if (key == null) {
	    return null;
	}
	key = key.trim();

	CacheObject<T> ObjInPool = null;
	try {
	    ObjInPool = (CacheObject<T>) getClient().get(key);
	} catch (Exception e) {
	    e.printStackTrace();
	    return null;
	}
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
	for (String key : keySet()) {
	    CacheObject<T> item = getCacheObject(key);
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

	CacheObject<T> ObjInPool = getCacheObject(key);
	if (ObjInPool == null) {
	    return false;
	}
	CacheObject<T> theCacheObject = ObjInPool;

	return theCacheObject.isNotOverTime();
    }

    public Set<String> keySet() {
	Set<String> keySet = new HashSet<String>();

	String[] serverArray = MEMCACHED_SERVERS.split(" ");
	for (String server : serverArray) {
	    try {
		KeyIterator it = this.client.getKeyIterator(AddrUtil
			.getOneAddress(server));
		while (it.hasNext()) {
		    keySet.add(it.next());
		}
	    } catch (MemcachedException e) {
		e.printStackTrace();
	    } catch (Exception localException) {
	    }
	}
	return keySet;
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
	try {
	    getClient().delete(key);
	} catch (Exception e) {
	    e.printStackTrace();
	    throw new RuntimeException(e);
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

    public void saveCacheObject(String key, T value) {
	saveCacheObject(key, value, CommonConstant.CACHE_DURATION_MINUTES * 60);
    }

    public void saveCacheObject(String key, T value, long durationSeconds) {
	saveCacheObjectMills(key, value, durationSeconds * 1000L);
    }

    public void setDurationMills(long duration) {
	for (String key : keySet()) {
	    CacheObject<T> item = getCacheObject(key);
	    item.setDuration(duration);
	}
    }

    public int size() {
	return keySet().size();
    }

    private List<CacheObject<T>> getCacheObjectList() {
	List<CacheObject<T>> cacheValueList = new ArrayList<CacheObject<T>>();
	for (String key : keySet()) {
	    cacheValueList.add(getCacheObject(key));
	}
	return cacheValueList;
    }

    public List<T> values() {
	List<CacheObject<T>> cacheValueList = getCacheObjectList();
	List<T> valuesList = new ArrayList<T>(cacheValueList.size());
	for (CacheObject<T> cacheObj : cacheValueList) {
	    if (cacheObj != null) {
		valuesList.add(cacheObj.getObjectItemAnyway());
	    }
	}
	return valuesList;
    }

    public List<T> valuesWithinDuration() {
	Collection<CacheObject<T>> cacheValueCollection = getCacheObjectList();
	List<T> valuesList = new ArrayList<T>(cacheValueCollection.size());
	for (CacheObject<T> cacheObj : cacheValueCollection) {
	    valuesList.add(cacheObj.getObjectWithinDuration());
	}
	return valuesList;
    }

    public void saveCacheObjectForever(String key, T value) {
	saveCacheObject(key, value, 0L);
    }

    public void saveCacheObjectMills(String key, T value, long durationMills) {
	if (key == null) {
	    return;
	}
	key = key.trim();

	CacheObject<T> theObj = new CacheObject<T>(value, durationMills);
	try {
	    getClient().set(key, 2592000, theObj, 30000L);
	} catch (Exception e) {
	    e.printStackTrace();
	    throw new RuntimeException(e);
	}
    }

    public void processTask() {
	removeOverTimeObject();
    }
}
