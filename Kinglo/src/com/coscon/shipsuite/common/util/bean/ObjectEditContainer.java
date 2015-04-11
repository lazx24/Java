package com.coscon.shipsuite.common.util.bean;

import com.coscon.shipsuite.common.util.generic.CollectionUtil;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public final class ObjectEditContainer<T> implements IObjectEditContainer<T>,
	Serializable {
    private static final long serialVersionUID = -8292504438352486699L;
    private final Map<T, ObjectEditStatusEnums> objectMap = new LinkedHashMap<T, ObjectEditStatusEnums>();

    public boolean add(T e) {
	return putNoChanged(e);
    }

    public boolean addAll(Collection<? extends T> c) {
	return putNoChanged(c);
    }

    public void clear() {
	this.objectMap.clear();
    }

    public boolean contains(Object obj) {
	return this.objectMap.containsKey(obj);
    }

    public boolean containsAll(Collection<?> c) {
	return this.objectMap.keySet().containsAll(c);
    }

    public boolean containsChanged(T obj) {
	ObjectEditStatusEnums status = (ObjectEditStatusEnums) this.objectMap
		.get(obj);
	return status == ObjectEditStatusEnums.CHANGED;
    }

    public boolean containsDeleted(T obj) {
	ObjectEditStatusEnums status = (ObjectEditStatusEnums) this.objectMap
		.get(obj);
	return status == ObjectEditStatusEnums.DELETED;
    }

    public boolean containsInserted(T obj) {
	ObjectEditStatusEnums status = (ObjectEditStatusEnums) this.objectMap
		.get(obj);
	return status == ObjectEditStatusEnums.INSERTED;
    }

    public boolean containsNoChanged(T obj) {
	ObjectEditStatusEnums status = (ObjectEditStatusEnums) this.objectMap
		.get(obj);
	return status == ObjectEditStatusEnums.NOCHANGED;
    }

    public List<T> getAll() {
	return new ArrayList<T>(this.objectMap.keySet());
    }

    public List<T> getList(ObjectEditStatusEnums status) {
	return CollectionUtil.getKeyListByValues(this.objectMap,
		new ObjectEditStatusEnums[] { status });
    }

    public List<T> getList(ObjectEditStatusEnums[] statusArray) {
	return CollectionUtil.getKeyListByValues(this.objectMap, statusArray);
    }

    public List<T> getAllExcludeDeleted() {
	return getList(new ObjectEditStatusEnums[] {
		ObjectEditStatusEnums.NOCHANGED,
		ObjectEditStatusEnums.INSERTED, ObjectEditStatusEnums.CHANGED });
    }

    public List<T> getChanged() {
	return getList(ObjectEditStatusEnums.CHANGED);
    }

    public List<T> getDeleted() {
	return getList(ObjectEditStatusEnums.DELETED);
    }

    public List<T> getInserted() {
	return getList(ObjectEditStatusEnums.INSERTED);
    }

    public List<T> getNoChanged() {
	return getList(ObjectEditStatusEnums.NOCHANGED);
    }

    public ObjectEditStatusEnums getStatus(T obj) {
	ObjectEditStatusEnums status = (ObjectEditStatusEnums) this.objectMap
		.get(obj);
	if (status == null) {
	    return ObjectEditStatusEnums.NOT_EXISTS;
	}
	return status;
    }

    public boolean hasChanged() {
	return this.objectMap.containsValue(ObjectEditStatusEnums.CHANGED);
    }

    public boolean hasDeleted() {
	return this.objectMap.containsValue(ObjectEditStatusEnums.DELETED);
    }

    public boolean hasInserted() {
	return this.objectMap.containsValue(ObjectEditStatusEnums.INSERTED);
    }

    public boolean hasNoChanged() {
	return this.objectMap.containsValue(ObjectEditStatusEnums.NOCHANGED);
    }

    public boolean isEmpty() {
	return this.objectMap.isEmpty();
    }

    public Iterator<T> iterator() {
	return this.objectMap.keySet().iterator();
    }

    public void putCleanAll(Collection<? extends T> objCollection) {
	this.objectMap.clear();
	putAll(objCollection);
    }

    public void putAll(Collection<? extends T> objCollection) {
	putNoChanged(objCollection);
    }

    public boolean putChanged(Collection<? extends T> objCollection) {
	boolean hasChanged = false;
	for (T obj : objCollection) {
	    if (putChanged(obj)) {
		hasChanged = true;
	    }
	}
	return hasChanged;
    }

    public boolean putChanged(T obj) {
	return putObject(obj, ObjectEditStatusEnums.CHANGED);
    }

    public boolean putDeleted(Collection<? extends T> objCollection) {
	boolean hasChanged = false;
	for (T obj : objCollection) {
	    if (putDeleted(obj)) {
		hasChanged = true;
	    }
	}
	return hasChanged;
    }

    public boolean putDeleted(T obj) {
	return putObject(obj, ObjectEditStatusEnums.DELETED);
    }

    public boolean putInserted(Collection<? extends T> objCollection) {
	boolean hasChanged = false;
	for (T obj : objCollection) {
	    if (putInserted(obj)) {
		hasChanged = true;
	    }
	}
	return hasChanged;
    }

    public boolean putInserted(T obj) {
	return putObject(obj, ObjectEditStatusEnums.INSERTED);
    }

    public boolean putNoChanged(Collection<? extends T> objCollection) {
	boolean hasChanged = false;
	for (T obj : objCollection) {
	    if (putNoChanged(obj)) {
		hasChanged = true;
	    }
	}
	return hasChanged;
    }

    public boolean putNoChanged(T obj) {
	return putObject(obj, ObjectEditStatusEnums.NOCHANGED);
    }

    public boolean putObject(T obj, ObjectEditStatusEnums objStatus) {
	ObjectEditStatusEnums status = getStatus(obj);
	if (status == objStatus) {
	    return false;
	}
	if (status == ObjectEditStatusEnums.INSERTED) {
	    if (objStatus == ObjectEditStatusEnums.DELETED) {
		remove(obj);
		return true;
	    }
	    return false;
	}
	this.objectMap.put(obj, objStatus);
	return true;
    }

    public boolean remove(Object o) {
	ObjectEditStatusEnums status = (ObjectEditStatusEnums) this.objectMap
		.remove(o);
	if (status != null) {
	    return true;
	}
	return false;
    }

    public boolean remove(Object obj, ObjectEditStatusEnums... statusToRemove) {
	boolean hasChanged = false;
	ObjectEditStatusEnums status = (ObjectEditStatusEnums) this.objectMap
		.remove(obj);
	if (((status != null) && (statusToRemove == null))
		|| (statusToRemove.length == 0)) {
	    hasChanged = true;
	} else if (status != null) {
	    for (ObjectEditStatusEnums s : statusToRemove) {
		if (s == status) {
		    hasChanged = true;
		    break;
		}
	    }
	}
	return hasChanged;
    }

    public boolean removeAll(Collection objCollection,
	    ObjectEditStatusEnums... statusToRemove) {
	boolean hasChanged = false;
	for (Object obj : objCollection) {
	    if (remove(obj, statusToRemove)) {
		hasChanged = true;
	    }
	}
	return hasChanged;
    }

    public void resetAllNoChanged() {
	Set<T> keySet = new HashSet<T>(this.objectMap.keySet());
	for (T obj : keySet) {
	    this.objectMap.put(obj, ObjectEditStatusEnums.NOCHANGED);
	}
    }

    public boolean retainAll(Collection<?> c) {
	boolean retained = false;
	Map<T, ObjectEditStatusEnums> newMap = new LinkedHashMap<T, ObjectEditStatusEnums>(c.size());
	for (Object obj : c) {
	    ObjectEditStatusEnums status = (ObjectEditStatusEnums) this.objectMap
		    .get(obj);
	    if (status != null) {
		newMap.put((T)obj, status);
		retained = true;
	    }
	}
	this.objectMap.clear();
	this.objectMap.putAll(newMap);
	return retained;
    }

    public int size() {
	return this.objectMap.size();
    }

    public Object[] toArray() {
	return this.objectMap.keySet().toArray();
    }

    public <T> T[] toArray(T[] a) {
	return this.objectMap.keySet().toArray(a);
    }
}
