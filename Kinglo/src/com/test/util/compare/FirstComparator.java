package com.test.util.compare;

import java.util.Comparator;

import com.test.Student;

@SuppressWarnings("hiding")
public class FirstComparator implements Comparator<Student>{

	@Override
	public int compare(Student s1, Student s2) {
		if(s1.getId()>s2.getId()){
			return 1;
		}else if(s1.getId()<s2.getId()){
			return -1;
		}
		return 0;
	}

	

}
