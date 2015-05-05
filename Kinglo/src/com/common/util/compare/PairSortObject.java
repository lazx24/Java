package com.common.util.compare;

import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import com.common.util.date.DateUtil;

public final class PairSortObject<K, V> implements
	Comparator<PairSortObject<K, V>>, Comparable {
    private K sortKey;
    private V value;
    private boolean nullsAreHigh = false;

    public PairSortObject(K k, V v) {
	this.sortKey = k;
	this.value = v;
    }

    public PairSortObject(K k, V v, boolean nullsAreHigh) {
	this.sortKey = k;
	this.value = v;
	this.nullsAreHigh = nullsAreHigh;
    }

    public int compare(PairSortObject<K, V> o1, PairSortObject<K, V> o2) {
	return PinyinComparator.getInstance(this.nullsAreHigh).compare(
		o1.sortKey, o2.sortKey);
    }

    public int compareTo(Object o) {
	if ((this == null) && (o == null)) {
	    return 0;
	}
	if ((this != null) && (o == null)) {
	    if (this.nullsAreHigh) {
		return -1;
	    }
	    return 1;
	}
	if ((this == null) && (o != null)) {
	    if (this.nullsAreHigh) {
		return 1;
	    }
	    return -1;
	}
	return PinyinComparator.getInstance(this.nullsAreHigh).compare(
		this.sortKey, ((PairSortObject) o).getSortKey());
    }

    public String getKeySortString() {
	return getSortString(this.sortKey);
    }

    public K getSortKey() {
	return this.sortKey;
    }

    private String getSortString(Object obj) {
	if ((obj instanceof Date)) {
	    return DateUtil.dateToString((Date) obj);
	}
	if ((obj instanceof Calendar)) {
	    return DateUtil.calendarToString((Calendar) obj);
	}
	if (obj == null) {
	    return "";
	}
	return obj.toString();
    }

    public V getValue() {
	return this.value;
    }

    public String getValueSortString() {
	return getSortString(this.value);
    }

    public void setSortKey(K key) {
	this.sortKey = key;
    }

    public void setValue(V value) {
	this.value = value;
    }

    public String toString() {
	return getKeySortString();
    }
}
