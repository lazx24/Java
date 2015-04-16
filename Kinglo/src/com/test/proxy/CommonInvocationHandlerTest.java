package com.test.proxy;

import static org.junit.Assert.fail;

import org.junit.Test;

import com.common.proxy.CommonInvocationHandler;
import com.test.Student;

public class CommonInvocationHandlerTest {

    @Test
    public void testBind() {
	Student s=new Student();
	s.setId(1);
	s.setName("zoujinahua");
	CommonInvocationHandler<Student> common=new CommonInvocationHandler<Student>();
	common.bind(s);
	Student ss=common.getTarget();
	System.out.println(ss.getName());
    }

    @Test
    public void testInvokeMethodObjectArray() {
	fail("Not yet implemented");
    }

    @Test
    public void testInvokeObjectMethodObjectArray() {
	fail("Not yet implemented");
    }

    @Test
    public void testGetTarget() {
	fail("Not yet implemented");
    }

}
