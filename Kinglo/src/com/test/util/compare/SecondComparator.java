package com.test.util.compare;

import java.util.Comparator;

import com.test.Student;

public class SecondComparator implements Comparator<Student>{

	@Override
	public int compare(Student s1, Student s2) {
		if(s1.getAge()>s2.getAge()){
			return 1;
		}else if(s1.getAge()<s2.getAge()){
			return -1;
		}
		return 0;
	}
}
