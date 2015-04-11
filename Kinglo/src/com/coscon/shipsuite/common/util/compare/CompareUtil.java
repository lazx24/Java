package com.coscon.shipsuite.common.util.compare;

import com.coscon.shipsuite.common.exception.ShipSuiteRuntimeException;
import com.coscon.shipsuite.common.util.bean.BeanUtil;
import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import org.hibernate.LazyInitializationException;

public final class CompareUtil {
    /**
     * 比较两个对象内容是否相等
     * @param obj1 第一个对象
     * @param obj2 第二个对象
     * @return
     */
    public static boolean equals(Object obj1, Object obj2) {
	if (obj1 == obj2) {
	    return true;
	}
	if ((obj1 == null) && (obj2 != null)) {
	    return false;
	}
	if ((obj1 != null) && (obj2 == null)) {
	    return false;
	}
	if (!obj1.getClass().equals(obj2.getClass())) {
	    return false;
	}
	if (obj1.getClass().isPrimitive()) {
	    return obj1 == obj2;
	}
	if (obj1.getClass().isArray()) {
	    int arrayLen = Array.getLength(obj1);
	    if (arrayLen != Array.getLength(obj2)) {
		return false;
	    }
	    for (int i = 0; i < arrayLen; i++) {
		Object o1 = Array.get(obj1, i);
		Object o2 = Array.get(obj2, i);
		if (!equals(o1, o2)) {
		    return false;
		}
	    }
	    return true;
	}
	if ((obj1 instanceof Collection)) {
	    int listSize1 = 0;
	    try {
		listSize1 = ((Collection<?>) obj1).size();
	    } catch (Exception e) {
		if ((e instanceof LazyInitializationException)) {
		    listSize1 = 0;
		} else {
		    throw new ShipSuiteRuntimeException(e);
		}
	    }
	    int listSize2 = 0;
	    try {
		listSize2 = ((Collection<?>) obj2).size();
	    } catch (Exception e) {
		if ((e instanceof LazyInitializationException)) {
		    listSize2 = 0;
		} else {
		    throw new ShipSuiteRuntimeException(e);
		}
	    }
	    if (listSize1 != listSize2) {
		return false;
	    }
	    if (listSize1 == 0) {
		return true;
	    }
	    Iterator<?> iterator1 = ((Collection<?>) obj1).iterator();
	    Iterator<?> iterator2 = ((Collection<?>) obj2).iterator();
	    do {
		if (!equals(iterator1.next(), iterator2.next())) {
		    return false;
		}
	    } while (iterator1.hasNext());
	    return true;
	}
	if ((obj1 instanceof Map)) {
	    int mapSize = ((Map) obj1).size();
	    if (mapSize != ((Map) obj2).size()) {
		return false;
	    }
	    Set<Map.Entry> entrySet1 = ((Map) obj1).entrySet();
	    Set<Map.Entry> entrySet2 = ((Map) obj2).entrySet();

	    Iterator<?> iterator1 = entrySet1.iterator();
	    Iterator<?> iterator2 = entrySet2.iterator();
	    do {
		if (!equals(iterator1.next(), iterator2.next())) {
		    return false;
		}
	    } while (iterator1.hasNext());
	    return true;
	}
	if ((obj1.getClass().getName().startsWith("java."))
		|| (obj1.getClass().getName().startsWith("javax."))
		|| (obj1.getClass().getName().startsWith("com.sun."))) {
	    return obj1.equals(obj2);
	}
	return equals(BeanUtil.getProperties(obj1),
		BeanUtil.getProperties(obj2));
    }
    
    /**
     * 比较两个对象的属性是否相等
     * @param obj1	第一个对象
     * @param obj2	第二个对象
     * @param compareProperties	比较的属性
     * @return
     */
    public static boolean equals(Object obj1, Object obj2,
	    String... compareProperties) {
	if (obj1 == obj2) {
	    return true;
	}
	if ((obj1 == null) || (obj2 == null)) {
	    return false;
	}
	for (String property : compareProperties) {
	    Object value1 = null;
	    Object value2 = null;
	    try {
		value1 = BeanUtil.getProperty(obj1, property);
	    } catch (Exception localException) {
	    }
	    try {
		value2 = BeanUtil.getProperty(obj2, property);
	    } catch (Exception localException1) {
	    }
	    if (!equals(value1, value2)) {
		return false;
	    }
	}
	return true;
    }
}
