package com.example.arithmetic;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;
/**
 * AES加密解密辅助类
 * @author zoujianhua
 * 下午08:13:50
 * @version 1.0
 */
public class AESUtil {
	
	private static String key="0123456789abcdef";//必须为16字节
	
	private static String parameter="0123456789abcdef";//必须为16字节
	
	/**
	 * AES加密
	 * @param sSrc
	 * @return String
	 * @throws Exception
	 */
	public static String encrypt(String sSrc) throws Exception {
		Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
		byte[] raw = key.getBytes();
		SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
		IvParameterSpec iv = new IvParameterSpec(parameter.getBytes());//使用CBC模式，需要一个向量iv，可增加加密算法的强度
		cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);
		byte[] encrypted = cipher.doFinal(sSrc.getBytes("utf-8"));
		return new BASE64Encoder().encode(encrypted);//此处使用BASE64做转码。
	}

	/**
	 * AES解密
	 * @param sSrc
	 * @return String
	 * @throws Exception
	 */
	public static String decrypt(String sSrc) throws Exception{
		byte[] raw = key.getBytes("ASCII");
		SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
		Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
		IvParameterSpec iv = new IvParameterSpec(parameter.getBytes());
		cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);
		byte[] encrypted1 = new BASE64Decoder().decodeBuffer(sSrc);//先用base64解密
		byte[] original = cipher.doFinal(encrypted1);
		String originalString = new String(original,"utf-8");
		return originalString;
	}
}
