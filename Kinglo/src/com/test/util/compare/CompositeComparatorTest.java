package com.test.util.compare;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.junit.Test;
import com.coscon.shipsuite.common.util.compare.CompositeComparator;
import com.test.Student;

public class CompositeComparatorTest {

	@Test
	public void testCompare() {
		//比较器集合(主要用于数组或者list排序)
		CompositeComparator<Student> comparator=new CompositeComparator<Student>();
		comparator.addComparator(new FirstComparator());
		comparator.addComparator(new SecondComparator());
		
		Student s1=new Student();
		s1.setId(2);
		s1.setAge(12);
		
		Student s2=new Student();
		s2.setId(1);
		s2.setAge(13);
		
		List<Student> list=new ArrayList<Student>();
		list.add(s1);
		list.add(s2);
		
		Collections.sort(list, comparator);
		for (int i = 0; i < list.size(); i++) {
			Student s3=list.get(i);
			System.out.println(s3.getId()+"-"+s3.getAge());
		}
	}

}
