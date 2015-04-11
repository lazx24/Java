package com.test.arithmetic;

import org.junit.Test;

import com.example.arithmetic.DESUtil;

public class DESUtilTest {

	@Test
	public void testEncryptStringString() {
		String key="0123456789***";
    	String data="abcddddklsddddddddddddddddd";
    	System.out.println("密匙为"+key);
    	System.out.println("需要加密的数据:"+data);
		String result=DESUtil.encrypt(data, key);
		System.out.println("加密后的结果为:"+result);
	}

	@Test
	public void testEncryptStringStringString() {
		String key="0123456789***";
		String secondKey="0123456789";
    	String data="abcddddklsddddddddddddddddd";
    	System.out.println("第一个密匙为"+key);
    	System.out.println("第二个密匙为:"+secondKey);
    	System.out.println("需要加密的数据:"+data);
		String result=DESUtil.encrypt(data, key,secondKey);
		System.out.println("加密后的结果为:"+result);
	}

	@Test
	public void testEncryptStringStringStringString() {
		String key="0123456789***";
		String secondKey="0123456789";
		String thirdKey="uuuuuuu";
    	String data="abcddddklsddddddddddddddddd";
    	System.out.println("第一个密匙为"+key);
    	System.out.println("第二个密匙为:"+secondKey);
    	System.out.println("第三个密匙为:"+thirdKey);
    	System.out.println("需要加密的数据:"+data);
		String result=DESUtil.encrypt(data, key,secondKey,thirdKey);
		System.out.println("加密后的结果为:"+result);
	}

	@Test
	public void testDecryptStringString() {
		String key="0123456789***";
    	String data="abcddddklsddddddddddddddddd";
    	System.out.println("密匙为"+key);
    	System.out.println("需要加密的数据:"+data);
		String result=DESUtil.encrypt(data, key);
		System.out.println("加密后的结果为:"+result);
		
		System.out.println("对加密后的结果"+result+"进行解密!");
		data=DESUtil.decrypt(result, key);
		System.out.println("解密后的原始数据为:"+data);
	}

	@Test
	public void testDecryptStringStringString() {
		String key="0123456789***";
		String secondKey="0123456789";
    	String data="3333";
    	System.out.println("第一个密匙为"+key);
    	System.out.println("第二个密匙为:"+secondKey);
    	System.out.println("需要加密的数据:"+data);
		String result=DESUtil.encrypt(data, key,secondKey);
		System.out.println("加密后的结果为:"+result);
		
		System.out.println("对加密后的结果"+result+"进行解密!");
		data=DESUtil.decrypt(result, key, secondKey);
		System.out.println("解密后的原始数据为:"+data);
	}

	@Test
	public void testDecryptStringStringStringString() {
		String key="0123456789***";
		String secondKey="0123456789";
		String thirdKey="uuuuuuu";
    	String data="abcddddklsddddddddddddddddd";
    	System.out.println("第一个密匙为"+key);
    	System.out.println("第二个密匙为:"+secondKey);
    	System.out.println("第三个密匙为:"+thirdKey);
    	System.out.println("需要加密的数据:"+data);
		String result=DESUtil.encrypt(data, key,secondKey,thirdKey);
		System.out.println("加密后的结果为:"+result);
		
		System.out.println("对加密后的结果"+result+"进行解密!");
		data=DESUtil.decrypt(result, key, secondKey,thirdKey);
		System.out.println("解密后的原始数据为:"+data);
	}

	@Test
	public void testGetKeyBytes() {
		
	}

}
