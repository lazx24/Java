package com.coscon.shipsuite.common.util.bean;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public abstract interface IObjectEditContainer<T> {
    public abstract boolean putObject(T paramT,
	    ObjectEditStatusEnums paramObjectEditStatusEnums);

    public abstract boolean add(T paramT);

    public abstract boolean addAll(Collection<? extends T> paramCollection);

    public abstract void clear();

    public abstract boolean contains(Object paramObject);

    public abstract boolean containsAll(Collection<?> paramCollection);

    public abstract boolean containsChanged(T paramT);

    public abstract boolean containsDeleted(T paramT);

    public abstract boolean containsInserted(T paramT);

    public abstract boolean containsNoChanged(T paramT);

    public abstract List<T> getAll();

    public abstract List<T> getAllExcludeDeleted();

    public abstract List<T> getList(
	    ObjectEditStatusEnums paramObjectEditStatusEnums);

    public abstract List<T> getList(
	    ObjectEditStatusEnums[] paramArrayOfObjectEditStatusEnums);

    public abstract List<T> getChanged();

    public abstract List<T> getDeleted();

    public abstract List<T> getInserted();

    public abstract List<T> getNoChanged();

    public abstract ObjectEditStatusEnums getStatus(T paramT);

    public abstract boolean hasChanged();

    public abstract boolean hasDeleted();

    public abstract boolean hasInserted();

    public abstract boolean hasNoChanged();

    public abstract boolean isEmpty();

    public abstract Iterator<T> iterator();

    public abstract void putAll(Collection<? extends T> paramCollection);

    public abstract void putCleanAll(Collection<? extends T> paramCollection);

    public abstract boolean putChanged(Collection<? extends T> paramCollection);

    public abstract boolean putChanged(T paramT);

    public abstract boolean putDeleted(Collection<? extends T> paramCollection);

    public abstract boolean putDeleted(T paramT);

    public abstract boolean putInserted(Collection<? extends T> paramCollection);

    public abstract boolean putInserted(T paramT);

    public abstract boolean putNoChanged(Collection<? extends T> paramCollection);

    public abstract boolean putNoChanged(T paramT);

    public abstract boolean remove(Object paramObject);

    public abstract boolean remove(Object paramObject,
	    ObjectEditStatusEnums... paramVarArgs);

    public abstract boolean removeAll(Collection paramCollection,
	    ObjectEditStatusEnums... paramVarArgs);

    public abstract void resetAllNoChanged();

    public abstract boolean retainAll(Collection<?> paramCollection);

    public abstract int size();

    public abstract Object[] toArray();

    public abstract <T> T[] toArray(T[] paramArrayOfT);
}
