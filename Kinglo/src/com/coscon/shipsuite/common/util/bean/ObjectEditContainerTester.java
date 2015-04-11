package com.coscon.shipsuite.common.util.bean;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.junit.Test;

public class ObjectEditContainerTester {
    @Test
    public void putAll() {
	List<Integer> list = new ArrayList<Integer>();
	for (int i = 0; i < 100; i++) {
	    list.add(Integer.valueOf(i));
	}
	ObjectEditContainer<Integer> container = new ObjectEditContainer<Integer>();
	container.clear();
	container.putNoChanged(list);

	List<Integer> listInserted = new ArrayList<Integer>();

	listInserted.add(Integer.valueOf(101));
	listInserted.add(Integer.valueOf(102));
	listInserted.add(Integer.valueOf(103));
	listInserted.add(Integer.valueOf(104));
	listInserted.add(Integer.valueOf(105));

	container.putInserted(listInserted);

	container.putInserted(Integer.valueOf(105));
	container.putInserted(Integer.valueOf(106));
	container.putChanged(Integer.valueOf(100));
	container.putChanged(Integer.valueOf(101));
	container.putInserted(Integer.valueOf(105));
	container.putInserted(Integer.valueOf(106));
	container.putDeleted(Integer.valueOf(99));
	container.putDeleted(Integer.valueOf(102));

	StringBuffer sb = new StringBuffer();

	sb.append("NoChanged: \t\t");
	for (Iterator localIterator = container.getNoChanged().iterator(); localIterator
		.hasNext();) {
	    int i = ((Integer) localIterator.next()).intValue();
	    sb.append(i).append(",");
	}
	System.out.println(sb.toString());

	sb.setLength(0);
	sb.append("Inserted: \t\t");
	for (Iterator localIterator = container.getInserted().iterator(); localIterator
		.hasNext();) {
	    int i = ((Integer) localIterator.next()).intValue();
	    sb.append(i).append(",");
	}
	System.out.println(sb.toString());

	sb.setLength(0);
	sb.append("Changed: \t\t");
	for (Iterator localIterator = container.getChanged().iterator(); localIterator
		.hasNext();) {
	    int i = ((Integer) localIterator.next()).intValue();
	    sb.append(i).append(",");
	}
	System.out.println(sb.toString());

	sb.setLength(0);
	sb.append("Deleted: \t\t");
	for (Iterator localIterator = container.getDeleted().iterator(); localIterator
		.hasNext();) {
	    int i = ((Integer) localIterator.next()).intValue();
	    sb.append(i).append(",");
	}
	System.out.println(sb.toString());

	sb.setLength(0);
	sb.append("AllExcludeDeleted: \t");
	for (Iterator localIterator = container.getAllExcludeDeleted().iterator(); localIterator
		.hasNext();) {
	    int i = ((Integer) localIterator.next()).intValue();
	    sb.append(i).append(",");
	}
	System.out.println(sb.toString());

	sb.setLength(0);
	sb.append("All: \t\t\t");
	for (Iterator localIterator = container.getAll().iterator(); localIterator
		.hasNext();) {
	    int i = ((Integer) localIterator.next()).intValue();
	    sb.append(i).append(",");
	}
	System.out.println(sb.toString());

	container.resetAllNoChanged();
	sb.setLength(0);
	sb.append("NoChanged: \t\t");
	for (Iterator localIterator = container.getNoChanged().iterator(); localIterator
		.hasNext();) {
	    int i = ((Integer) localIterator.next()).intValue();
	    sb.append(i).append(",");
	}
	System.out.println(sb.toString());

	list.clear();
	list.add(Integer.valueOf(9));
	list.add(Integer.valueOf(19));
	container.retainAll(list);
	sb.setLength(0);
	sb.append("All: \t\t\t");
	for (Iterator localIterator = container.getAll().iterator(); localIterator
		.hasNext();) {
	    int i = ((Integer) localIterator.next()).intValue();
	    sb.append(i).append(",");
	}
	System.out.println(sb.toString());
	for (int i = 300; i < 800; i++) {
	    list.add(Integer.valueOf(i));
	}
	container.addAll(list);
	sb.setLength(0);
	sb.append("All: \t\t\t");
	for (Iterator localIterator = container.getAll().iterator(); localIterator
		.hasNext();) {
	    int i = ((Integer) localIterator.next()).intValue();
	    sb.append(i).append(",");
	}
	System.out.println(sb.toString());

	container.clear();
    }
}
