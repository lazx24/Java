package com.coscon.shipsuite.common.util.cache;

public final class CacheManagerFactory {
    @SuppressWarnings("unchecked")
    public static <T> ICacheManager<T> getCacheManger(Class<T> cls) {
	if (MemcachedManager.getInstance().hasClient()) {
	    return MemcachedManager.getInstance();
	}
	return CacheManager.getInstance(cls);
    }
}
