package com.test.arithmetic;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import org.junit.Test;

import com.example.arithmetic.Base64;
import com.example.file.CreateFileUtil;
import com.example.file.WriteToFile;
import com.test.Student;

public class Base64Test {

	@Test
	public void testEncodeByteArray() {
		String s = "sadfihowiqjDLAFKJASDLFKLASD阿拉卡积分拉丝机v3u5925u";
		System.out.println("初始化字符串是:"+s);
		String s1 = Base64.encode(s.getBytes());
		System.out.println("将字节数组Base64加密:"+s1);
	}

	@Test
	public void testDecode() {
		String s = "sadfihowiqjDLAFKJASDLFKLASD阿拉卡积分拉丝机v3u5925u";
		System.out.println("初始化字符串是:"+s);
		String s1 = Base64.encode(s.getBytes());
		System.out.println("将字节数组Base64加密:"+s1);
		
		String s2 = new String(Base64.decode(s1));
		System.out.println("将"+s1+"字符串Base64解密："+s2);
	}

	@Test
	public void testEncodeInputStream() {
		try {
			String path="D:\\a.txt";
			String text="测试423424242432424324242";
			CreateFileUtil.createFile(path);
			WriteToFile.writeFile(path,text,"utf-8");
			InputStream in=new FileInputStream(new File(path));
			System.out.println("文件输入流 文件路径为:"+path);
			System.out.println("对文件输入流进行Base64加密");
			String str=Base64.encode(in);
			System.out.println("对文件输入流Base64加密后的字符串:"+str);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testEncodeToString() {
		String s = "sadfihowiqjDLAFKJASDLFKLASD阿拉卡积分拉丝机v3u5925u";
		System.out.println("初始化字符串是:"+s);
		String s1 = Base64.encodeToString(s);
		System.out.println("将字符串"+s+"Base64加密:"+s1);
	}

	@Test
	public void testDecodeToString() {
		String s = "sadfihowiqjDLAFKJASDLFKLASD阿拉卡积分拉丝机v3u5925u";
		System.out.println("初始化字符串是:"+s);
		String s1 = Base64.encodeToString(s);
		System.out.println("将字符串"+s+"Base64加密:"+s1);
		System.out.println("初始化解密字符串:"+s1);
		s=Base64.decodeToString(s1);
		System.out.println("解密后的字符串为:"+s);
	}

	@Test
	public void testEncodeObject() {
		Student s=new Student();
		s.setId(1);
		s.setName("zoujianhua");
		System.out.println("将Student对象进行Base64编码");
		System.out.println("Student id:"+s.getId());
		System.out.println("Student Name:"+s.getName());
		String str=Base64.encodeObject(s);
		System.out.println("Base64编码后:"+str);
	}

	@Test
	public void testDecodeObject() {
		Student s=new Student();
		s.setId(1);
		s.setName("zoujianhua");
		System.out.println("将Student对象进行Base64编码");
		System.out.println("Student id:"+s.getId());
		System.out.println("Student Name:"+s.getName());
		String str=Base64.encodeObject(s);
		System.out.println("Base64编码后:"+str);
		
		System.out.println("根据"+str+"进行Base64解码");
		Student stu=(Student)Base64.decodeObject(str);
		System.out.println("解码后Student id:"+stu.getId());
		System.out.println("解码后Student Name:"+stu.getName());
	}

}
