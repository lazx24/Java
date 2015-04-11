package com.coscon.shipsuite.common.util.generic;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Collection辅助类
 * 
 * @author zoujianhua
 * @time 2014-10-20
 * @version 1.0
 */
public class ListUtil {

    /**
     * 数组转换为List
     * 
     * @param array
     * @return
     */
    public static <T> List<T> asList(T[] array) {
	if (array == null || array.length == 0) {
	    return null;
	}
	return Arrays.asList(array);
    }

    /**
     * 数组转换为Set
     * 
     * @param array
     * @return
     */
    public static <T> Set<T> asSet(T[] array) {
	if (array == null || array.length == 0) {
	    return null;
	}
	return new HashSet<T>(Arrays.asList(array));
    }

    /**
     * Set转换为List
     * 
     * @param set
     * @return
     */
    public static <T> List<T> asList(Set<T> set) {
	if (set == null || set.size() == 0) {
	    return null;
	}
	return new ArrayList<T>(set);
    }

    /**
     * 复制List
     * 
     * @param dest
     * @param src
     */
    public static <T> void copy(List<T> dest, List<T> src) {
	Collections.copy(dest, src);
    }

    /**
     * 在List中将所有oldVal替换成newVal
     * 
     * @param list
     * @param oldVal
     * @param newVal
     */
    public static void replaceAll(List<Object> list, Object oldVal,
	    Object newVal) {
	Collections.replaceAll(list, oldVal, newVal);
    }

    /**
     * 反转List
     * 
     * @param list
     */
    public static void reverse(List<Object> list) {
	Collections.reverse(list);
    }

    /**
     * 将List的所有对象都替换为同一个对象
     * 
     * @param list
     * @param value
     */
    public static void fill(List<Object> list, Object value) {
	Collections.fill(list, value);
    }

    /**
     * 两个List取并集
     * 
     * @param dest
     * @param src
     */
    public static List<Object> union(List<Object> dest, List<Object> src) {
	if (dest == null || src == null) {
	    return null;
	}

	List<Object> newList = new ArrayList<Object>(dest);
	for (int i = 0; i < src.size(); i++) {
	    Object object = src.get(i);
	    if (!dest.contains(object)) {
		newList.add(object);
	    }
	}
	return newList;
    }

    /**
     * 两个List取交集
     * 
     * @param dest
     * @param src
     * @return
     */
    public static List<Object> intersect(List<Object> dest, List<Object> src) {
	if (dest == null || src == null) {
	    return null;
	}
	List<Object> newList = new ArrayList<Object>();
	for (int i = 0; i < src.size(); i++) {
	    Object object = src.get(i);
	    if (dest.contains(object)) {
		newList.add(object);
	    }
	}
	return newList;
    }

    /**
     * 重新复制一个List
     * 
     * @param src
     * @return
     */
    public static List<Object> copy(List<Object> src) {
	if (src == null || src.size() == 0) {
	    return null;
	}
	List<Object> result = new ArrayList<Object>(src);
	return result;
    }
}
