package com.common.util.generic;

import com.common.enums.SortEnums;
import com.common.exception.ShipSuiteRuntimeException;
import com.common.util.bean.BeanUtil;
import com.common.util.compare.PairSortObject;
import com.common.util.compare.PinyinComparator;
import com.common.util.compare.SimilarityUtil;
import com.common.util.string.StringUtil;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.collections.comparators.NullComparator;

public final class CollectionUtil {

    /**
     * 数组转换为LinkedHashMap
     * 
     * @param <T>
     * @param itemsArray
     * @return
     */
    public static <T> LinkedHashMap<String, T> arraryToLinkedHashMap(
	    T[] itemsArray) {
	LinkedHashMap<String, T> map = new LinkedHashMap<String, T>();
	T[] arrayOfObject = itemsArray;
	int j = itemsArray.length;
	for (int i = 0; i < j; i++) {
	    T s = arrayOfObject[i];
	    map.put(s.toString(), s);
	}
	return map;
    }

    /**
     * 数组转换为TreeMap
     * 
     * @param <T>
     * @param itemsArray
     * @return
     */
    public static <T> TreeMap<String, T> arraryToTreeMap(T[] itemsArray) {
	TreeMap<String, T> map = new TreeMap<String, T>();
	T[] arrayOfObject = itemsArray;
	int j = itemsArray.length;
	for (int i = 0; i < j; i++) {
	    T s = arrayOfObject[i];
	    map.put(s.toString(), s);
	}
	return map;
    }

    /**
     * 数组转换为List
     * 
     * @param <E>
     * @param list
     * @param newItems
     * @return 在原来的List的基础上累加数组对象
     */
    public static <E> List<E> arrayAddToList(List<E> list, E[] newItems) {
	if (list == null) {
	    list = new ArrayList<E>();
	}
	for (E object : newItems) {
	    list.add(object);
	}
	return list;
    }

    /**
     * 判断数组是否为空
     * 
     * @param arr
     * @return
     */
    public static boolean arrayIsNullOrEmpty(Object[] arr) {
	return (arr == null) || (arr.length == 0);
    }

    /**
     * 数组转换为List
     * 
     * @param <E>
     * @param newItems
     * @return
     */
    public static <E> List<E> arrayToList(E[] newItems) {
	List<E> list = new ArrayList<E>();

	E[] arrayOfObject = newItems;
	int j = newItems.length;
	for (int i = 0; i < j; i++) {
	    E object = arrayOfObject[i];
	    list.add(object);
	}
	return list;
    }

    /**
     * 返回Value在列表的位置
     * 
     * @param collection
     *            列表
     * @param value
     *            值
     * @return
     */
    public static int binarySearchIndex(Collection<?> collection, Object value) {
	if ((value instanceof Comparable)) {
	    List valueList;
	    if ((collection instanceof List)) {
		valueList = (List) collection;
	    } else {
		valueList = new ArrayList();
		valueList.addAll(collection);
	    }
	    return Collections.binarySearch(valueList, value);
	}
	return searchIndex(collection, value);
    }

    /**
     * 比较Map中的内容是否相等
     * 
     * @param <K>
     * @param <V>
     * @param map1
     * @param map2
     * @return
     */
    public static <K, V> boolean compareContents(Map<K, V> map1, Map<K, V> map2) {
	NullComparator nullComparator = new NullComparator();
	if ((map1 == null) && (map2 == null)) {
	    return true;
	}
	if ((map1 != null) && (map1.isEmpty()) && (map2 == null)) {
	    return true;
	}
	if ((map1 == null) && (map2 != null) && (map2.isEmpty())) {
	    return true;
	}
	if ((map1.isEmpty()) && (map2.isEmpty())) {
	    return true;
	}
	if (map1.size() != map2.size()) {
	    return false;
	}
	Set<Map.Entry<K, V>> entrySet = map1.entrySet();
	for (Map.Entry<K, V> entry : entrySet) {
	    if (!map2.containsKey(entry.getKey())) {
		return false;
	    }
	    V value1 = entry.getValue();
	    V value2 = map2.get(entry.getKey());
	    if ((value1 != null) && (value1.getClass().isArray())) {
		Object[] objects1 = (Object[]) value1;
		Object[] objects2 = (Object[]) value2;
		if ((objects2 == null) || (objects1.length != objects2.length)) {
		    return false;
		}
		for (int i = 0; i < objects1.length; i++) {
		    if (nullComparator.compare(objects1[i], objects2[i]) != 0) {
			return false;
		    }
		}
	    } else if (nullComparator.compare(value1, value2) != 0) {
		return false;
	    }
	}
	return true;
    }

    /**
     * List转换为Map
     * 
     * @param <V>
     * @param list
     *            List列表
     * @param key
     *            List列表中对象的属性名
     * @return
     */
    public static <V> Map<String, V> convertListToMap(List<V> list, String key) {
	Map<String, V> map = new HashMap<String, V>();
	if (isNullOrEmpty(list)) {
	    return map;
	}
	try {
	    for (V object : list) {
		String name = BeanUtils.getProperty(object, key);
		map.put(name, object);
	    }
	} catch (IllegalAccessException e) {
	    e.printStackTrace();
	} catch (InvocationTargetException e) {
	    e.printStackTrace();
	} catch (NoSuchMethodException e) {
	    e.printStackTrace();
	}
	return map;
    }

    /**
     * 根据keys数组获取keys在Map中的位置
     * 
     * @param <K>
     * @param <V>
     * @param map
     *            Map
     * @param keys
     *            Object数组
     * @return
     */
    public static <K, V> List<Integer> getIndexListByKeys(Map<K, V> map,
	    Object[] keys) {
	if ((keys == null) || (keys.length == 0)) {
	    return null;
	}
	List<Integer> indexList = new ArrayList<Integer>(keys.length);
	Integer i = Integer.valueOf(0);
	for (K k : map.keySet()) {
	    for (Object key : keys) {
		if ((k == key) || (k.equals(key))) {
		    indexList.add(i);
		}
	    }
	    i = Integer.valueOf(i.intValue() + 1);
	}
	return indexList;
    }

    /**
     * 获取键在Map的位置
     * 
     * @param <K>
     * @param <V>
     * @param map
     * @param key
     * @return
     */
    public static <K, V> int getIndexByKey(Map<K, V> map, K key) {
	List<Integer> indexList = getIndexListByKeys(map, new Object[] { key });
	if ((indexList != null) && (indexList.size() > 0)) {
	    return ((Integer) indexList.get(0)).intValue();
	}
	return -1;
    }

    /**
     * 获取Value在Map的位置
     * 
     * @param <K>
     * @param <V>
     * @param map
     * @param value
     * @return
     */
    public static <K, V> int getIndexByValue(Map<K, V> map, V value) {
	List<Integer> indexList = getIndexListByValues(map,
		new Object[] { value });
	if ((indexList != null) && (indexList.size() > 0)) {
	    return ((Integer) indexList.get(0)).intValue();
	}
	return -1;
    }

    /**
     * 根据Value数组获取Map中的位置
     * @param <K>
     * @param <V>
     * @param map
     * @param values
     * @return
     */
    public static <K, V> List<Integer> getIndexListByValues(Map<K, V> map,
	    Object[] values) {
	if ((values == null) || (values.length == 0)) {
	    return null;
	}
	List<Integer> indexList = new ArrayList<Integer>(map.size());
	Integer i = Integer.valueOf(0);
	for (V v : map.values()) {
	    for (Object value : values) {
		if ((v == value) || (v.equals(value))) {
		    indexList.add(i);
		}
	    }
	    i = Integer.valueOf(i.intValue() + 1);
	}
	return indexList;
    }
    
    /**
     * 根据index获取Map中的键
     * @param <K>
     * @param <V>
     * @param map
     * @param index
     * @return
     */
    public static <K, V> K getKeyByIndex(Map<K, V> map, int index) {
	if (index <= -1) {
	    return null;
	}
	List<K> keyList = getKeyListByIndexes(map, new int[] { index });
	if ((keyList != null) && (keyList.size() > 0)) {
	    return keyList.get(0);
	}
	return null;
    }
    
    /**
     * 根据Value获取Map中的键
     * @param <K>
     * @param <V>
     * @param map
     * @param value
     * @return
     */
    public static <K, V> K getKeyByValue(Map<K, V> map, V value) {
	Set<Map.Entry<K, V>> entrySet = map.entrySet();
	for (Map.Entry<K, V> entry : entrySet) {
	    if ((entry.getValue() == value)
		    || ((entry.getValue() != null) && (entry.getValue()
			    .equals(value)))) {
		return entry.getKey();
	    }
	}
	return null;
    }
    
    /**
     * 根据index数组获取Map中的键的List
     * @param <K>
     * @param <V>
     * @param map
     * @param indexes
     * @return
     */
    public static <K, V> List<K> getKeyListByIndexes(Map<K, V> map,
	    int[] indexes) {
	if ((indexes == null) || (indexes.length == 0)) {
	    return null;
	}
	List<K> keyList = new ArrayList<K>(indexes.length);

	int i = 0;
	for (K key : map.keySet()) {
	    for (int index : indexes) {
		if (i == index) {
		    keyList.add(key);
		}
	    }
	    i++;
	}
	return keyList;
    }
    
    /**
     * 根据value数组获取Map的Key的List
     * @param <K>
     * @param <V>
     * @param map
     * @param values
     * @return
     */
    public static <K, V> List<K> getKeyListByValues(Map<K, V> map, V[] values) {
	if ((values == null) || (values.length == 0)) {
	    return null;
	}
	List<K> keyList = new ArrayList<K>(map.size());
	Set<Map.Entry<K, V>> entrySet = map.entrySet();
	for (Iterator<?> localIterator = entrySet.iterator(); localIterator
		.hasNext();) {
	    Map.Entry<K, V> entry = (Map.Entry) localIterator.next();

	    for (int i = 0; i < values.length; i++) {
		Object value = values[i];
		if ((entry.getValue() == null)
			|| ((entry.getValue() != null) && (entry.getValue()
				.equals(value)))) {
		    keyList.add(entry.getKey());
		}
	    }
	}
	return keyList;
    }
    
    private static <E> PairSortObject<Object, E> getPairSortObject(E obj,
	    String... properties) {
	if ((properties == null) || (properties.length == 0)) {
	    return new PairSortObject<Object, E>(obj, obj);
	}
	Object[] propertyValues = new Object[properties.length];
	int index = 0;
	Method method;
	for (String property : properties) {
	    try {
		propertyValues[index] = PropertyUtils
			.getProperty(obj, property);
	    } catch (Exception e) {
		try {
		    method = obj.getClass().getDeclaredMethod(property,
			    new Class[0]);
		    if (method != null) {
			propertyValues[index] = method.invoke(obj,
				new Object[0]);
		    }
		} catch (Exception localException1) {
		}
	    }
	    index++;
	}
	PairSortObject<Object, E> sortObject;
	if (properties.length == 1) {
	    sortObject = new PairSortObject<Object, E>(propertyValues[0], obj);
	} else {
	    StringBuilder keyBuilder = new StringBuilder();
	    int i = propertyValues.length;
	    for (int j = 0; j < i; j++) {
		Object propertyObjct = propertyValues[j];
		if (propertyObjct != null) {
		    keyBuilder.append(propertyObjct);
		}
	    }
	    sortObject = new PairSortObject<Object, E>(keyBuilder.toString(),
		    obj);
	}
	return sortObject;
    }

    private static String getSortProperty(String sortBy) {
	if (StringUtil.isNullOrEmpty(sortBy)) {
	    return "";
	}
	sortBy = sortBy.trim();
	if (sortBy.contains(" ")) {
	    return sortBy.substring(0, sortBy.indexOf(" "));
	}
	return sortBy;
    }

    private static SortEnums getSortType(String sortBy) {
	if ((StringUtil.isNullOrEmpty(sortBy))
		|| (!sortBy.toLowerCase().trim().contains(" desc"))) {
	    return SortEnums.ASC;
	}
	return SortEnums.DESC;
    }
    
    /**
     * 根据index获取LinedHashMap中的value
     * @param <K>
     * @param <V>
     * @param map
     * @param index
     * @return
     */
    public static <K, V> V getValueByIndex(LinkedHashMap<K, V> map, int index) {
	if (index <= -1) {
	    return null;
	}
	List<V> valueList = getValueListByIndexes(map, new int[] { index });
	if ((valueList != null) && (valueList.size() > 0)) {
	    return valueList.get(0);
	}
	return null;
    }
    
    /**
     * 根据index数组获取Map中值的List
     * @param <K>
     * @param <V>
     * @param map
     * @param indexes
     * @return
     */
    public static <K, V> List<V> getValueListByIndexes(Map<K, V> map,
	    int[] indexes) {
	if ((indexes == null) || (indexes.length == 0)) {
	    return null;
	}
	List<V> valueList = new ArrayList<V>(indexes.length);
	int i = 0;
	for (V value : map.values()) {
	    for (int index : indexes) {
		if (i == index) {
		    valueList.add(value);
		}
	    }
	    i++;
	}
	return valueList;
    }
    
    /**
     * 获取index位置之前的LinkedHashMap
     * @param <K>
     * @param <V>
     * @param map
     * @param endIndex
     * @return
     */
    public static <K, V> LinkedHashMap<K, V> headMap(LinkedHashMap<K, V> map,
	    int endIndex) {
	return headMap(map, endIndex, false);
    }
    
    /**
     * 如果inclusive为true  获取index+1位置之前的LinkedHashMap
     * @param <K>
     * @param <V>
     * @param map
     * @param endIndex
     * @param inclusive 是否在endIndex上加1
     * @return
     */
    public static <K, V> LinkedHashMap<K, V> headMap(LinkedHashMap<K, V> map,
	    int endIndex, boolean inclusive) {
	int index = endIndex;
	if (inclusive) {
	    index++;
	}
	if ((map == null) || (map.size() == 0) || (map.size() < index)
		|| (endIndex < 0)) {
	    return new LinkedHashMap<K, V>(0);
	}
	LinkedHashMap<K, V> subMap = new LinkedHashMap<K, V>(index + 1);
	int i = 0;
	for (Map.Entry<K, V> entry : map.entrySet()) {
	    if (i < index) {
		subMap.put(entry.getKey(), entry.getValue());
	    }
	    i++;
	}
	return subMap;
    }
    
    /**
     * 判断Collection是否为空
     * @param collection
     * @return
     */
    public static boolean isNullOrEmpty(Collection<?> collection) {
	return (collection == null) || (collection.isEmpty());
    }
    
    /**
     * List中插入子列表
     * @param <E>
     * @param list
     * @param subList
     * @param beginIndex
     * @param endIndex
     */
    public static <E> void replaceSubList(List<E> list, List<E> subList,
	    int beginIndex, int endIndex) {
	if ((endIndex < beginIndex) || (list == null) || (list.size() == 0)
		|| (subList == null) || (subList.size() == 0)) {
	    return;
	}
	if (endIndex - beginIndex > subList.size()) {
	    endIndex = beginIndex + subList.size();
	}
	List<E> newList = new ArrayList<E>(list);
	list.clear();

	list.addAll(newList.subList(0, beginIndex));

	list.addAll(subList);
	if (endIndex >= newList.size()) {
	    return;
	}
	list.addAll(newList.subList(endIndex, newList.size()));
    }
    
    /**
     * 查询对象在Collection中的位置
     * @param collection
     * @param value
     * @return
     */
    public static int searchIndex(Collection<? extends Object> collection,
	    Object value) {
	List<? extends Object> valueList = new ArrayList<Object>(collection);

	int i = 0;
	for (Object o : valueList) {
	    if ((o == value) || ((o != null) && (o.equals(value)))) {
		return i;
	    }
	    i++;
	}
	return -1;
    }
    
    /**
     * 根据实体类的Id的annotation注解来查询索引位置
     * @param <T>
     * @param collection
     * @param entity
     * @return
     */
    public static <T> int searchIndexByUuid(Collection<T> collection, T entity) {
	if (entity == null) {
	    return -1;
	}
	String entityUuid = BeanUtil.getEntityUuidPropertyValue(entity);
	if (StringUtil.isNullOrEmpty(entityUuid)) {
	    entityUuid = entityUuid.toString();
	}
	int index = 0;
	for (T obj : collection) {
	    if (entityUuid.equals(BeanUtil.getEntityUuidPropertyValue(obj))) {
		return index;
	    }
	    index++;
	}
	return -1;
    }
    
    /**
     * 根据实体类的Id的annotation注解来查询索引位置
     * @param <T>
     * @param array
     * @param entity
     * @return
     */
    public static <T> int searchIndexByUuid(T[] array, T entity) {
	if (entity == null) {
	    return -1;
	}
	String entityUuid = BeanUtil.getEntityUuidPropertyValue(entity);
	if (StringUtil.isNullOrEmpty(entityUuid)) {
	    entityUuid = entityUuid.toString();
	}
	int index = 0;
	T[] arrayOfObject = array;
	int j = array.length;
	for (int i = 0; i < j; i++) {
	    T obj = arrayOfObject[i];
	    if (entityUuid.equals(BeanUtil.getEntityUuidPropertyValue(obj))) {
		return index;
	    }
	    index++;
	}
	return -1;
    }
    
    /**
     * 根据属性名来排序
     * @param map	 Map
     * @param properties 属性名
     * @return
     */
    public static LinkedHashMap sort(Map map, String... properties) {
	return sort(map, false, properties);
    }

    public static LinkedHashMap sort(Map map, boolean ignoreCase,
	    String... properties) {
	if ((map == null) || (map.isEmpty())) {
	    return new LinkedHashMap(0);
	}
	List keyList = new ArrayList(map.keySet());

	LinkedHashMap linkedHashMap = new LinkedHashMap(map.size());
	sort(keyList, false, ignoreCase, properties);
	for (Object key : keyList) {
	    linkedHashMap.put(key, map.get(key));
	}
	return linkedHashMap;
    }

    public static <E> void sort(List<E> list, boolean nullsAreHigh,
	    String... properties) {
	sort(list, nullsAreHigh, false, properties);
    }

    public static <E> void sort(List<E> list, boolean nullsAreHigh,
	    boolean ignoreCase, String... properties) {
	if ((list == null) || (list.size() <= 1)) {
	    return;
	}
	Comparator comparator = PinyinComparator.getInstance(nullsAreHigh,
		ignoreCase);
	if ((properties == null) || (properties.length == 0)) {
	    Collections.sort(list, comparator);
	} else {
	    List<List<PairSortObject<Object, E>>> propertyListList = new ArrayList(
		    properties.length);
	    String preProperty = null;
	    for (int index = 0; index < properties.length; index++) {
		List<PairSortObject<Object, E>> propertyList = new ArrayList(
			list.size());

		List<String> propertyNameList = new ArrayList();
		for (int i = 0; i <= index; i++) {
		    propertyNameList.add(getSortProperty(properties[i]));
		}
		String[] propertyArray = (String[]) propertyNameList
			.toArray(new String[propertyNameList.size()]);
		SortEnums sortType = getSortType(properties[index]);
		if (index == 0) {
		    for (E element : list) {
			propertyList.add(getPairSortObject(element,
				propertyArray));
		    }
		    Collections.sort(propertyList, comparator);
		    if (sortType.equals(SortEnums.DESC)) {
			Collections.reverse(propertyList);
		    }
		    propertyListList.add(propertyList);

		    list.clear();
		    for (PairSortObject<Object, E> sortObject : propertyList) {
			list.add(sortObject.getValue());
		    }
		} else {
		    List<PairSortObject<Object, E>> prePropertyList = (List) propertyListList
			    .get(index - 1);

		    Object subList = new ArrayList();

		    PairSortObject<Object, E> firstSortObject = null;
		    if (prePropertyList.size() > 0) {
			firstSortObject = (PairSortObject) prePropertyList
				.get(0);

			((List) subList).add(firstSortObject.getValue());
		    }
		    int beginIndex = 0;
		    PairSortObject<Object, E> sortObject;
		    for (int n = 1; n < prePropertyList.size(); n++) {
			sortObject = (PairSortObject) prePropertyList.get(n);
			if ((sortObject.getSortKey() == firstSortObject
				.getSortKey())
				|| ((firstSortObject.getSortKey() != null) && (firstSortObject
					.getSortKey().equals(sortObject
					.getSortKey())))) {
			    ((List) subList).add(sortObject.getValue());
			} else {
			    if (((List) subList).size() > 1) {
				sort((List) subList, nullsAreHigh,
					propertyArray);

				replaceSubList(list, (List) subList,
					beginIndex, beginIndex
						+ ((List) subList).size());
			    }
			    for (E e : (List<E>) subList) {
				propertyList.add(getPairSortObject(e,
					propertyArray));
			    }
			    firstSortObject = sortObject;
			    ((List) subList).clear();
			    ((List) subList).add(sortObject.getValue());

			    beginIndex = n;
			}
		    }
		    if ((beginIndex < prePropertyList.size() - 1)
			    && (((List) subList).size() > 1)) {
			sort((List) subList, nullsAreHigh,
				new String[] { properties[index] });

			replaceSubList(list, (List) subList, beginIndex,
				beginIndex + ((List) subList).size());
			for (E e : (List<E>) subList) {
			    propertyList
				    .add(getPairSortObject(e, propertyArray));
			}
		    }
		    propertyListList.add(propertyList);
		}
	    }
	}
    }

    public static <E> void sort(List<E> list, String... properties) {
	sort(list, false, properties);
    }

    public static <K, V> LinkedHashMap<K, V> tailMap(LinkedHashMap<K, V> map,
	    int beginIndex) {
	return tailMap(map, beginIndex, true);
    }

    public static <K, V> LinkedHashMap<K, V> tailMap(LinkedHashMap<K, V> map,
	    int beginIndex, boolean inclusive) {
	int index = beginIndex;
	if (!inclusive) {
	    index++;
	}
	if ((map == null) || (map.size() == 0) || (map.size() <= index)) {
	    return new LinkedHashMap<K, V>(0);
	}
	LinkedHashMap<K, V> subMap = new LinkedHashMap<K, V>(map.size() - index);
	int i = 0;
	for (Map.Entry<K, V> entry : map.entrySet()) {
	    if (i >= index) {
		subMap.put(entry.getKey(), entry.getValue());
	    }
	    i++;
	}
	return subMap;
    }

    public static <T> T[] toArray(List<T> list) {
	if (list == null) {
	    list = new ArrayList<T>();
	}
	Object[] obs = new Object[list.size()];
	int index = 0;
	for (T o : list) {
	    if (o != null) {
		obs[(index++)] = o;
	    }
	}
	return (T[]) obs;
    }

    private static final double similarProperty(String keyword,
	    String propertyValue) {
	String[] keys = keyword.trim().split(" ");
	List<String> keyList = new ArrayList<String>(keys.length);
	for (String key : keys) {
	    if (StringUtil.isNotNullAndNotEmptyWithTrim(key)) {
		keyList.add(key);
	    }
	}
	double similar = 0.0D;
	for (Iterator<String> iterator = keyList.iterator(); iterator.hasNext();) {
	    String key = (String) (iterator.next());
	    similar += SimilarityUtil.similar(propertyValue, key);
	    if (propertyValue.contains(key)) {
		similar += 1.0D;
	    }
	}
	return similar;
    }

    public static <T extends Comparable<T>> void sort(T[] array, boolean ascend) {
	sort(array, 0, array.length - 1, ascend);
    }

    private static <T extends Comparable<T>> void sort(T[] array, int lo,
	    int hi, boolean ascend) {
	if (lo >= hi) {
	    return;
	}
	T toFinal = array[lo];
	int leftIdx = lo;
	int rightIdx = hi;

	int i = lo + 1;
	while (i <= rightIdx) {
	    int compare = array[i].compareTo(toFinal);
	    if (compare == 0) {
		i++;
	    } else if (compare < 0 == ascend) {
		exchange(array, leftIdx++, i++);
	    } else {
		exchange(array, rightIdx--, i);
	    }
	}
	sort(array, lo, leftIdx - 1, ascend);
	sort(array, rightIdx + 1, hi, ascend);
    }

    private static void exchange(Object[] array, int p, int q) {
	if (p == q) {
	    return;
	}
	Object temp = array[p];
	array[p] = array[q];
	array[q] = temp;
    }

    public static <E> E max(Collection<E> list, String propertyName) {
	if ((list == null) || (list.isEmpty())) {
	    return null;
	}
	List<E> sortList = new ArrayList<E>(list);

	sort(sortList, new String[] { propertyName });

	return sortList.get(sortList.size() - 1);
    }

    public static <E> E min(Collection<E> list, String propertyName) {
	if ((list == null) || (list.isEmpty())) {
	    return null;
	}
	List<E> sortList = new ArrayList<E>(list);

	sort(sortList, new String[] { propertyName });

	return sortList.get(0);
    }

    public static <E> BigDecimal sum(Collection<E> list, String propertyName) {
	BigDecimal totalResult = new BigDecimal(0);
	if ((list == null) || (list.isEmpty())) {
	    return totalResult;
	}
	for (E obj : list) {
	    Object v = null;
	    try {
		v = BeanUtil.getProperty(obj, propertyName);
	    } catch (Exception localException) {
	    }
	    if (v == null) {
		v = Integer.valueOf(0);
	    }
	    totalResult = totalResult.add(new BigDecimal(v.toString()));
	}
	return totalResult;
    }

    public static <E> BigDecimal avg(Collection<E> list, String propertyName) {
	BigDecimal avgResult = new BigDecimal(0);
	if ((list == null) || (list.isEmpty())) {
	    return avgResult;
	}
	for (E obj : list) {
	    Object v = null;
	    try {
		v = BeanUtil.getProperty(obj, propertyName);
	    } catch (Exception localException) {
	    }
	    if (v == null) {
		v = Integer.valueOf(0);
	    }
	    avgResult = avgResult.add(new BigDecimal(v.toString()));
	}
	return avgResult.divide(new BigDecimal(list.size()));
    }

    public static <T extends Comparable<T>> void sort(List<T> list,
	    String propertyName, Class<Comparable<T>> propertyClass,
	    boolean ascend) throws IllegalAccessException,
	    InvocationTargetException, NoSuchMethodException {
	if ((list == null) || (list.isEmpty()) || (list.size() == 1)) {
	    return;
	}
	sort(list, propertyName, propertyClass, 0, list.size() - 1, ascend);
    }

    private static <T extends Comparable<T>> void sort(List<T> list,
	    String propertyName, Class<Comparable<T>> propertyClass, int lo,
	    int hi, boolean ascend) throws IllegalAccessException,
	    InvocationTargetException, NoSuchMethodException {
	if (lo >= hi) {
	    return;
	}
	T toFinal = (T) BeanUtil.getProperty(list.get(lo), propertyName);
	int leftIdx = lo;
	int rightIdx = hi;

	int i = lo + 1;
	while (i <= rightIdx) {
	    int compare = ((Comparable) BeanUtil.getProperty(list.get(i),
		    propertyName)).compareTo(toFinal);
	    if (compare == 0) {
		i++;
	    } else if (compare < 0 == ascend) {
		exchange(list, leftIdx++, i++);
	    } else {
		exchange(list, rightIdx--, i);
	    }
	}
	sort(list, propertyName, propertyClass, lo, leftIdx - 1, ascend);
	sort(list, propertyName, propertyClass, rightIdx + 1, hi, ascend);
    }

    private static void exchange(List list, int p, int q) {
	if (p == q) {
	    return;
	}
	Object temp = list.get(p);
	list.set(p, list.get(q));
	list.set(q, temp);
    }

    public static void sortBySimilar(String keyword, Map<String, Object> map) {
	if ((map == null) || (map.isEmpty())
		|| (StringUtil.isNullOrEmptyWithTrim(keyword))) {
	    return;
	}
	int size = map.size();

	Map<String, Double> similarMap = new LinkedHashMap<String, Double>(size);
	try {
	    List<String> list = new ArrayList<String>(map.keySet());

	    List<String> sortedList = new ArrayList<String>(size);
	    Object e;
	    for (int i = 0; i < size; i++) {
		String key = (String) list.get(i);
		e = map.get(key);

		Double similar = Double.valueOf(similarProperty(keyword, key));
		similarMap.put(key, similar);
		sortedList.add(key);
		if (i > 0) {
		    for (int j = 0; j < i; j++) {
			Double preSimilar = (Double) similarMap.get(sortedList
				.get(j));
			if (preSimilar.doubleValue() < similar.doubleValue()) {
			    String temp = (String) sortedList.get(j);
			    sortedList.set(j, (String) sortedList.get(i));
			    sortedList.set(i, temp);
			}
		    }
		}
	    }
	    LinkedHashMap<String, Object> sortedMap = new LinkedHashMap<String, Object>(
		    size);
	    for (String k : sortedList) {
		sortedMap.put(k, map.get(k));
	    }
	    map.clear();
	    map.putAll(sortedMap);
	} catch (Exception e1) {
	    throw new ShipSuiteRuntimeException();
	}
    }
    
    /**
     * 
     * @param <E>
     * @param keyword
     * @param list
     * @param propertyName
     */
    public static <E> void sortBySimilar(String keyword, List<E> list,
	    String propertyName) {
	if ((list == null) || (list.isEmpty())
		|| (StringUtil.isNullOrEmptyWithTrim(keyword))) {
	    return;
	}
	List<E> sortedList = new ArrayList<E>(list.size());
	Map<E, Double> similarMap = new LinkedHashMap<E, Double>(list.size());
	try {
	    int size = list.size();
	    for (int i = 0; i < size; i++) {
		E e = list.get(i);
		String propertyValue = String.valueOf(BeanUtil.getProperty(e,
			propertyName));

		Double similar = Double.valueOf(similarProperty(keyword,
			propertyValue));
		similarMap.put(e, similar);
		sortedList.add(e);
		if (i > 0) {
		    for (int j = 0; j < i; j++) {
			Double preSimilar = (Double) similarMap.get(sortedList
				.get(j));
			if (preSimilar.doubleValue() < similar.doubleValue()) {
			    E temp = sortedList.get(j);
			    sortedList.set(j, sortedList.get(i));
			    sortedList.set(i, temp);
			}
		    }
		}
	    }
	} catch (Exception e1) {
	    throw new ShipSuiteRuntimeException();
	}
	list.clear();
	list.addAll(sortedList);
    }
    
    /**
     * Map键值反转  键做为Map的值 值做为Map的键
     * @param <K>
     * @param <V>
     * @param map
     * @return
     */
    public static <K, V> Map<V, K> reverseKeyValue(Map<K, V> map) {
	if (map == null) {
	    return null;
	}
	Map<V, K> newMap = new HashMap<V, K>();
	for (Map.Entry<K, V> entry : map.entrySet()) {
	    newMap.put(entry.getValue(), entry.getKey());
	}
	return newMap;
    }
}
