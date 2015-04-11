package com.coscon.shipsuite.common.util.compare;

import java.util.Collection;
import java.util.Comparator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import net.sourceforge.pinyin4j.PinyinHelper;
import org.apache.commons.collections.comparators.NullComparator;

public final class PinyinComparator implements Comparator {
    private boolean nullsHigh = false;
    private boolean ignoreCase = false;
    private static NullComparator nullComparator = new NullComparator(false);
    private static NullComparator nullsAreHighComparator = new NullComparator(
	    true);
    private static Map<String, PinyinComparator> instanceMap = new ConcurrentHashMap<String, PinyinComparator>();

    public static PinyinComparator getInstance() {
	return getInstance(false);
    }

    public static PinyinComparator getInstance(boolean nullsAreHigh) {
	return getInstance(nullsAreHigh, false);
    }

    public static synchronized PinyinComparator getInstance(
	    boolean nullsAreHigh, boolean ignoreCase) {
	String instanceKey = String.valueOf(nullsAreHigh) + "|"
		+ String.valueOf(ignoreCase);
	PinyinComparator instance = (PinyinComparator) instanceMap
		.get(instanceKey);
	if (instance == null) {
	    instance = new PinyinComparator(nullsAreHigh, ignoreCase);
	    instanceMap.put(instanceKey, instance);
	}
	return instance;
    }

    private PinyinComparator(boolean nullsAreHigh, boolean ignoreCase) {
	this.nullsHigh = nullsAreHigh;
	this.ignoreCase = ignoreCase;
    }

    public int compare(Object o1, Object o2) {
	if (((o1 instanceof Collection)) || ((o2 instanceof Collection))) {
	    return 0;
	}
	if ((o1 == null) || (o2 == null)) {
	    if (this.nullsHigh) {
		return nullsAreHighComparator.compare(o1, o2);
	    }
	    return nullComparator.compare(o1, o2);
	}
	String key1 = o1.toString();
	String key2 = o2.toString();
	if (this.ignoreCase) {
	    key1 = key1.toUpperCase();
	    key2 = key2.toUpperCase();
	}
	Pattern pattern = Pattern.compile(".+[一-龥]+(.*)");

	Matcher matcher1 = pattern.matcher(key1);
	Matcher matcher2 = pattern.matcher(key2);
	if ((!matcher1.matches()) && (!matcher2.matches())) {
	    boolean areString = false;
	    if (((o1 instanceof String)) && ((o2 instanceof String))) {
		areString = true;
	    }
	    if (this.nullsHigh) {
		if ((this.ignoreCase) && (areString)) {
		    return nullsAreHighComparator.compare(o1.toString()
			    .toUpperCase(), o2.toString().toUpperCase());
		}
		return nullsAreHighComparator.compare(o1, o2);
	    }
	    if ((this.ignoreCase) && (areString)) {
		return nullComparator.compare(o1.toString().toUpperCase(), o2
			.toString().toUpperCase());
	    }
	    return nullComparator.compare(o1, o2);
	}
	for (int i = 0; (i < key1.length()) && (i < key2.length()); i++) {
	    int codePoint1 = key1.charAt(i);
	    int codePoint2 = key2.charAt(i);
	    if ((Character.isSupplementaryCodePoint(codePoint1))
		    || (Character.isSupplementaryCodePoint(codePoint2))) {
		i++;
	    }
	    if (codePoint1 != codePoint2) {
		if ((Character.isSupplementaryCodePoint(codePoint1))
			|| (Character.isSupplementaryCodePoint(codePoint2))) {
		    return codePoint1 - codePoint2;
		}
		String pinyin1 = pinyin((char) codePoint1);
		String pinyin2 = pinyin((char) codePoint2);
		if ((pinyin1 != null) && (pinyin2 != null)) {
		    if (!pinyin1.equals(pinyin2)) {
			return pinyin1.compareTo(pinyin2);
		    }
		} else {
		    return codePoint1 - codePoint2;
		}
	    }
	}
	return key1.length() - key2.length();
    }

    private String pinyin(char c) {
	String[] pinyins = PinyinHelper.toHanyuPinyinStringArray(c);
	if (pinyins == null) {
	    return null;
	}
	return pinyins[0];
    }
}
