package com.test.arithmetic;

import org.junit.Test;

import com.example.arithmetic.AESUtil;

public class AESUtilTest {

	@Test
	public void testEncrypt() {
		try {
			String str="100000/zoujianhua/12345678";
			System.out.println("加密的字符串是:"+str);
			String enString = AESUtil.encrypt(str);
			System.out.println("加密后的字串是：" + enString);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testDecrypt() {
		String enString="vEpKFCIfX05VI72hVg7PAINcMzrPNNcAhFOIt28STzo=";
		try {
			System.out.println("解密字符串是:"+enString);
			String DeString = AESUtil.decrypt(enString);
			System.out.println("解密后的字串是：" + DeString);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
