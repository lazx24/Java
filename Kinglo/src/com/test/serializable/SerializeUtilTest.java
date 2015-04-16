package com.test.serializable;

import org.junit.Test;

import com.common.util.serializable.SerializeUtil;
import com.test.Student;

public class SerializeUtilTest {

	@Test
	public void testObjectToByte() {
		System.out.println("对象序列化");
		Student s=new Student();
		s.setId(1);
		s.setName("chenhuan");
		s.setAge(12);
		
		System.out.println("初始化对象信息:"+s);
		try {
			byte[] bytes=SerializeUtil.objectToByte(s);
			System.out.println("Student转换为字节数组");
			System.out.println("字节数组转换为Student对象");
			Student s2=(Student)SerializeUtil.byteToObject(bytes);
			System.out.println("结果为:"+s2);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
