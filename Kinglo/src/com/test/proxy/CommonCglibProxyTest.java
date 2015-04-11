package com.test.proxy;

import org.junit.Test;

import com.coscon.shipsuite.common.proxy.CommonCglibProxy;
import com.test.Student;

public class CommonCglibProxyTest {

    @Test
    public void testGetInstanceObjectBoolean() {
	Student s =new Student();
	s.setId(1);
	s.setAge(23);
	s.setName("zoujianhua");
	CommonCglibProxy<Student> proxy=new CommonCglibProxy<Student>();
	Object object=proxy.getInstance(s, false);
	System.out.println(object);
	System.out.println(((Student)object).getName());
    }

    @Test
    public void testGetInstanceObject() {
	Student s =new Student();
	s.setId(1);
	s.setAge(23);
	s.setName("zoujianhua");
	CommonCglibProxy<Student> proxy=new CommonCglibProxy<Student>();
	Object object=proxy.getInstance(s);
	System.out.println(object);
	System.out.println(((Student)object).getName());
    }

    @Test
    public void testIntercept() {
	
    }

}
