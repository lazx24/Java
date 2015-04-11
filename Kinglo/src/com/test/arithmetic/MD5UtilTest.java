package com.test.arithmetic;

import org.junit.Test;

import com.example.arithmetic.MD5Util;

public class MD5UtilTest {

	@Test
	public void testMD5Encode() {
		String string="abcddddddddddd";
		System.out.println("初始化字符串:"+string);
		String result=MD5Util.MD5Encode(string);
		System.out.println("MD5加密后:"+result);
	}

	@Test
	public void testGetStringMD5() {
		String string="abcddddddddddd";
		System.out.println("初始化字符串:"+string);
		String result=MD5Util.getStringMD5(string);
		System.out.println("MD5加密后:"+result);
	}

	@Test
	public void testGetByteArrayMD5() {
		String string="abcddddddddddd";
		System.out.println("初始化字符串:"+string);
		String result=MD5Util.getByteArrayMD5(string.getBytes());
		System.out.println("MD5加密后:"+result);
	}

	@Test
	public void testGetPictureMD5() {
		
	}

	@Test
	public void testGetMenuMD5() {
		
	}
}
