package com.test.arithmetic;

import org.junit.Test;

import com.example.arithmetic.MD5;


public class MD5Test {

	@Test
	public void testGetMD5ofStr() {
		String string="4444";
		System.out.println("需要MD5加密的字符串:"+string);
		MD5 md5=new MD5();
		String result=md5.getMD5ofStr(string);
		System.out.println("MD5后的结果:"+result);
	}

	@Test
	public void testB2iu() {
		System.out.println("本方法可能没什么用");
		byte string=15;
		System.out.println("需要MD5加密的字节:"+string);
		long result=MD5.b2iu(string);
		System.out.println("结果:"+result);
	}

	@Test
	public void testByteHEX() {
		byte string=15;
		System.out.println("需要MD5加密的字节:"+string);
		String result=MD5.byteHEX(string);
		System.out.println("结果:"+result);
	}
}
