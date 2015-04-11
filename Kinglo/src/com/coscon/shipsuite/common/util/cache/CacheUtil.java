package com.coscon.shipsuite.common.util.cache;

public final class CacheUtil {
    public static int hash(String cacheName, int cacheSize) {
	int hashCode = 0;
	int i = 0;
	int len = cacheName.length();
	for (; i < len; i++) {
	    hashCode = hashCode * 33 + cacheName.codePointAt(i) & 0x7FFFFFFF;
	}
	return hashCode % cacheSize;
    }
}
