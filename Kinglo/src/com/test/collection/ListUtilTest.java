package com.test.collection;

import static org.junit.Assert.fail;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.junit.Test;

import com.common.util.generic.ArrayUtil;
import com.common.util.generic.ListUtil;
import com.test.Student;

public class ListUtilTest {

	@Test
	public void testAsListObjectArray() {
		System.out.println("数组转换为List");
		String[] str={"1","2","eee"};
		System.out.println("初始化数组内容："+ArrayUtil.toString(str));
		List<String> list=ListUtil.asList(str);
		System.out.println("结果为:"+list);
	}

	@Test
	public void testAsSet() {
		System.out.println("数组转换为Set");
		Student[] stu=new Student[2];
		Student s1=new Student();
		s1.setId(1);
		s1.setName("zoujianhua");
		s1.setAge(12);
		stu[0]=s1;
		
		Student s2=new Student();
		s2.setId(1);
		s2.setName("zoujianhua");
		s2.setAge(12);
		stu[1]=s2;
		
		Set<Student> set=ListUtil.asSet(stu);
		System.out.println("set length："+set.size());
		Iterator<Student> iter=set.iterator();
		while(iter.hasNext()){
			Student s=iter.next();
			System.out.println(s.toString());
		}
	}

	@Test
	public void testAsListSetOfObject() {
		
	}

	@Test
	public void testCopyListOfObjectListOfObject() {
		fail("Not yet implemented");
	}

	@Test
	public void testReplaceAll() {
		fail("Not yet implemented");
	}

	@Test
	public void testReverse() {
		fail("Not yet implemented");
	}

	@Test
	public void testFill() {
		fail("Not yet implemented");
	}

	@Test
	public void testUnion() {
		fail("Not yet implemented");
	}

	@Test
	public void testIntersect() {
		fail("Not yet implemented");
	}

	@Test
	public void testCopyListOfObject() {
		fail("Not yet implemented");
	}

}
