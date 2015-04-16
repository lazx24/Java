package com.test.proxy;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.common.proxy.HibernateProxyUtil;
import com.test.Student;

public class HibernateProxyUtilTest {

    @Test
    public void testInitializeAndUnproxy() {
	Student s= new Student();
	s.setId(1);
	s.setAge(23);
	s.setName("aaa");
	
//	List<String> list=new ArrayList<String>();
//	list.add("aaa");
//	list.add("bbbb");
	Student ss=HibernateProxyUtil.initializeAndUnproxy(s);
	System.out.println(ss);
    }

}
