package com.coscon.shipsuite.common.util.cache;

import org.junit.Test;

public class CacheUtilTest {
    @Test
    public void hash() {
	int cacheSize = 3;
	System.out.println(String.format("id=%s\t hash=%d", new Object[] {
		"duyj", Integer.valueOf(CacheUtil.hash("duyj", cacheSize)) }));
	System.out.println(String.format("id=%s\t hash=%d", new Object[] {
		"test", Integer.valueOf(CacheUtil.hash("test", cacheSize)) }));
	System.out
		.println(String.format(
			"id=%s\t hash=%d",
			new Object[] {
				"system",
				Integer.valueOf(CacheUtil.hash("system",
					cacheSize)) }));
	System.out
		.println(String.format(
			"id=%s\t hash=%d",
			new Object[] {
				"test1",
				Integer.valueOf(CacheUtil.hash("test1",
					cacheSize)) }));
	System.out.println(String.format("id=%s\t hash=%d", new Object[] {
		"test", Integer.valueOf(CacheUtil.hash("test", cacheSize)) }));
	System.out
		.println(String.format(
			"id=%s\t hash=%d",
			new Object[] {
				"system",
				Integer.valueOf(CacheUtil.hash("system",
					cacheSize)) }));
	System.out.println(String.format("id=%s\t hash=%d", new Object[] {
		"duyj", Integer.valueOf(CacheUtil.hash("duyj", cacheSize)) }));
	System.out.println("---------------------------------------------");

	int size0 = 0;
	int size1 = 0;
	int size2 = 0;
	for (int i = 0; i <= 20000; i++) {
	    String id = String.format("test%d",
		    new Object[] { Integer.valueOf(i) });
	    int hashValue = CacheUtil.hash(id, cacheSize);
	    if (hashValue == 0) {
		size0++;
	    } else if (hashValue == 1) {
		size1++;
	    } else if (hashValue == 2) {
		size2++;
	    }
	    System.out.println(String.format("id=%s\t hash=%d", new Object[] {
		    id, Integer.valueOf(hashValue) }));
	}
	System.out.println("---------------------------------------------");

	System.out.println(String.format("list0.size=%d",
		new Object[] { Integer.valueOf(size0) }));
	System.out.println(String.format("list1.size=%d",
		new Object[] { Integer.valueOf(size1) }));
	System.out.println(String.format("list2.size=%d",
		new Object[] { Integer.valueOf(size2) }));
    }
}
