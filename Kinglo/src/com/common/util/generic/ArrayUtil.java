package com.common.util.generic;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.common.util.validator.ValidUtil;

/**
 * 
 * 类的描述:数组辅助类
 * 创建时间:2015-5-5
 * 创建人:邹建华
 */
public class ArrayUtil {

    public static String[] toArray(List<String> list) {
	if (list == null || list.size() == 0) {
	    return new String[0];
	}
	return list.toArray(new String[list.size()]);
    }

    /**
     * 取数组的并集
     * 
     * @param dest
     * @param src
     * @return
     */
    public static Object[] union(Object[] dest, Object[] src) {
	if (dest == null || src == null) {
	    return new Object[0];
	}
	List<Object> newDest = Arrays.asList(dest);
	List<Object> newSrc = Arrays.asList(src);
	List<Object> result = ListUtil.union(newDest, newSrc);
	return result.toArray();
    }

    /**
     * 取数组的交集
     * 
     * @param dest
     * @param src
     * @return
     */
    public static Object[] intersect(Object[] dest, Object[] src) {
	if (dest == null || src == null) {
	    return new Object[0];
	}
	List<Object> newDest = Arrays.asList(dest);
	List<Object> newSrc = Arrays.asList(src);
	List<Object> result = ListUtil.intersect(newDest, newSrc);
	return result.toArray();
    }

    /**
     * 显示字符串数组的内容，用,分隔
     * @param args	字符串数组
     * @return 		字符串数组的内容
     */
    public static String toString(String[] args) {
	return toString(args, ",");
    }

    /**
     * 显示字符串数组的内容
     * @param args	字符串数组
     * @param separator	分隔符
     * @return 		字符串数组的内容
     */
    public static String toString(String[] args, String separator) {
	if (ValidUtil.isEmpty(args)) {
	    return null;
	}
	StringBuilder buffer = new StringBuilder();
	for (int i = 0; i < args.length; i++) {
	    if (i > 0) {
		buffer.append(separator);
	    }
	    buffer.append(args[i]);
	}
	return buffer.toString();
    }

    /**
     * 取得数组的第一个元素
     * @param array	数组
     * @return 		数组的第一个元素
     */
    public static <T> T getFirst(T[] array) {
	if (array == null || array.length == 0) {
	    return null;
	}
	return array[0];
    }

    /**
     * 判断数组是否包含指定的对象
     * @param array	数组
     * @param str	指定的对象
     * @return 		包含true，否则false
     */
    public static <T> boolean contains(T[] array, T str) {

	if (ValidUtil.isEmpty(array)) {
	    return false;
	}

	for (int i = 0; i < array.length; i++) {
	    if (array[i] == str) {
		return true;
	    }
	}
	return false;
    }

    /**
     * 把数组转换成 列表，如果数组为 null，则会返回一个空列表。
     * @param array 	数组
     * @return 		列表对象
     */
    public static <T> List<T> toList(T[] array) {
	ArrayList<T> list = new ArrayList<T>();
	if (array == null) {
	    return list;
	}

	for (int i = 0; i < array.length; i++) {
	    list.add(array[i]);
	}
	return list;
    }

    /**
     * 联合两个数组
     * @param first 	第一个数组
     * @param last 	另一个数组
     * @return 		内容合并后的数组
     */
    public static Object[] combine(Object[] first, Object[] last) {
	if (first.length == 0 && last.length == 0) {
	    return null;
	}
	Object[] result = new Object[first.length + last.length];
	System.arraycopy(first, 0, result, 0, first.length);
	System.arraycopy(last, 0, result, first.length, last.length);
	return result;
    }
}
