package com.test.proxy;

import static org.junit.Assert.fail;

import org.junit.Test;

import com.common.proxy.HashCglibProxy;
import com.test.Student;

public class HashCglibProxyTest {

    @Test
    public void testGetInstanceObjectBoolean() {
	Student s=new Student();
	s.setName("2222");
	HashCglibProxy<Student> proxy=new HashCglibProxy<Student>();
	Student ss=(Student)proxy.getInstance(s);
	System.out.println(ss);
    }

    @Test
    public void testGetInstanceObject() {
	fail("Not yet implemented");
    }
}
