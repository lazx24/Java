package com.test.reflect;

import org.junit.Test;

import com.common.util.bean.CreateObject;
import com.test.Student;

public class CreateObjectTest {

	@Test
	public void testCreateObjectString() {
		try {
			Student s=(Student)CreateObject.createObject("com.test.Student");
			System.out.println("Object："+s.getAge());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testCreateObjectClass() {
		try {
			Student s=(Student)CreateObject.createObject(Student.class);
			System.out.println("Object："+s.getAge());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testCreateObjectStringObjectArray() {
		Student s=null;
		try {
			s = (Student)CreateObject.createObject(Student.class,new Object[]{3});
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("Object："+s.getAge());
	}

	@Test
	public void testCreateObjectClassObjectArray() {
		Student s=null;
		try {
			s = (Student)CreateObject.createObject("com.test.Student",new Object[]{3});
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("Object："+s.getAge());
	}

}
