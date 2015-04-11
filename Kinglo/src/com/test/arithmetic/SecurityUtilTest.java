package com.test.arithmetic;

import org.junit.Test;

import com.example.arithmetic.SecurityUtil;


public class SecurityUtilTest {

	@Test
	public void testEncodeBySelf() {
		System.out.println("提示只能加密字节长度小于30的字符串");
//		String string="中国中国中中国中国中中国中国中中国中国中中国中国中中国中国中中国中国中";
		String string="abcded";
		System.out.println("需要加密的字符串:"+string);
		String result=SecurityUtil.encodeBySelf(string);
		System.out.println("加密后的结果为:"+result);
	}

	@Test
	public void testDecodeBySelf() {
		System.out.println("提示只能加密字节长度小于30的字符串");
//		String string="中国中国中中国中国中中国中国中中国中国中中国中国中中国中国中中国中国中";
		String string="abcded";
		System.out.println("需要加密的字符串:"+string);
		String result=SecurityUtil.encodeBySelf(string);
		System.out.println("加密后的结果为:"+result);
		
		System.out.println("对加密后的结果进行解密");
		result=SecurityUtil.decodeBySelf(result);
		System.out.println("解密结果为:"+result);
	}

	@Test
	public void testEncodeBySHA1() {
		System.out.println("使用SHA加密");
		String string="中国中国中中国中国中中国中国中中国中国中中国中国中中国中国中中国中国中";
//		String string="abcded";
		System.out.println("需要加密的字符串:"+string);
		String result=SecurityUtil.encodeBySHA1(string);
		System.out.println("加密后的结果为:"+result);
		
	}

	@Test
	public void testEncodeByMD5String() {
		System.out.println("使用MD5加密");
		String string="中国中国中中国中国中中国中国中中国中国中中国中国中中国中国中中国中国中";
//		String string="abcded";
		System.out.println("需要加密的字符串:"+string);
		String result=SecurityUtil.encodeByMD5(string);
		System.out.println("加密后的结果为:"+result);
	}

	@Test
	public void testEncodeByMD5ByteArray() {
		System.out.println("使用MD5对字节数组加密");
		String string="中国中国中中国中国中中国中国中中国中国中中国中国中中国中国中中国中国中";
//		String string="abcded";
		System.out.println("需要加密的字符串:"+string);
		String result=SecurityUtil.encodeByMD5(string.getBytes());
		System.out.println("加密后的结果为:"+result);
	}

	@Test
	public void testDecodeBy36Radix() {
		System.out.println("使用36进制加密");
		String string="dddddddddddddd";
		System.out.println("需要加密的字符串:"+string);
		String result=SecurityUtil.encodeBy36Radix(string);
		System.out.println("加密后的结果为:"+result);
		
		System.out.println("使用36进制解密");
		String result1=SecurityUtil.decodeBy36Radix(result);
		System.out.println("解密结果为:"+result1);
	}

	@Test
	public void testEncodeBy36Radix() {
		System.out.println("使用36进制加密");
		String string="dddddddddddddd";
		System.out.println("需要加密的字符串:"+string);
		String result=SecurityUtil.encodeBy36Radix(string);
		System.out.println("加密后的结果为:"+result);
	}

}
