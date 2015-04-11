package com.test.collection;

import java.util.List;

import org.junit.Test;

import com.coscon.shipsuite.common.util.generic.ArrayUtil;
import com.test.Student;

public class ArrayUtilTest {
	
	@Test
	public void testUnion() {
		System.out.println("取两个数组的并集");
		String[] dest={"1","2","3"};
		System.out.println("第一个数组内容:"+ArrayUtil.toString(dest));
		String[] src={"1","4"};
		System.out.println("第二个数组内容:"+ArrayUtil.toString(src));
		Object[] str=ArrayUtil.union(dest, src);
		for (int i = 0; i < str.length; i++) {
			System.out.println("结果为:"+str[i]);
		}
	}

	@Test
	public void testIntersect() {
		System.out.println("取两个数组的交集");
		String[] dest={"1","2","3"};
		System.out.println("第一个数组内容:"+ArrayUtil.toString(dest));
		String[] src={"1","4"};
		System.out.println("第二个数组内容:"+ArrayUtil.toString(src));
		Object[] str=ArrayUtil.intersect(dest, src);
		for (int i = 0; i < str.length; i++) {
			System.out.println("结果为:"+str[i]);
		}
	}

	@Test
	public void testToStringStringArray() {
		System.out.println("数组内容打印输出，以，分隔");
		String[] str={"1","2"};
		System.out.println("数组内容为:"+ArrayUtil.toString(str));
	}

	@Test
	public void testToStringStringArrayString() {
		System.out.println("数组内容打印输出，以。分隔");
		String[] str={"1","2"};
		System.out.println("数组内容为:"+ArrayUtil.toString(str,"."));
	}

	@Test
	public void testGetFirstStringArray() {
		System.out.println("取得数组的第一个元素");
		String[] array={"3","4"};
		System.out.println("数组初始化内容:"+ArrayUtil.toString(array));
		String str=ArrayUtil.getFirst(array);
		System.out.println("第一个元素为:"+str);
	}

	@Test
	public void testContains() {
		System.out.println("判断数组里是否包含该对象");
		Student[] str=new Student[2];
		Student s1=new Student();
		s1.setId(1);
		s1.setName("zoujianhua");
		s1.setAge(12);
		str[0]=s1;
		Student s2=new Student();
		s2.setId(2);
		s2.setName("zoujianhua2");
		s2.setAge(22);
		str[1]=s2;
		
		Student s3=new Student();
		s3.setId(1);
		s3.setName("zoujianhua");
		s3.setAge(12);
		boolean flag=ArrayUtil.contains(str, s3);
		System.out.println("判断数组里是否包含该对象(判断引用不判断内容):"+flag);
	}

	@Test
	public void testToList() {
		System.out.println("数组转换为List");
		String[] str={"1","2","3"};
		System.out.println("初始化数组内容:"+ArrayUtil.toString(str));
		List<String> list=ArrayUtil.toList(str);
		System.out.println("结果内容:"+list);
	}

	@Test
	public void testCombine() {
		System.out.println("联合两个数组");
		String[] str1={"1","2","3"};
		System.out.println("第一个数组内容:"+ArrayUtil.toString(str1));
		String[] str2={"1","4","5"};
		System.out.println("第二个数组内容:"+ArrayUtil.toString(str2));
		Object[] object=ArrayUtil.combine(str1, str2);
		System.out.println("结果长度为:"+object.length);
	}
}
