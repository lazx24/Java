package com.example.arithmetic;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

/**
 * 对base64编码的封装,为了提高维护性,避免处处类用标注的api
 * @author zoujianhua
 * 2014-12-8
 * @version 1.0
 */
public class Base64 {

	/**
	 * 将byte类型的数据经过base64编码转为string.
	 * 
	 * @param fileData
	 *            byte类型的数据 .
	 * @return 转码后的数据,发生异常或者filedate为null时返回null.
	 */
	public static String encode(byte[] fileData) {

		if (fileData == null) {
			return null;
		}
		BASE64Encoder encoder = new BASE64Encoder();
		return encoder.encode(fileData).replaceAll("\\s*","");

	}

	/**
	 * 将string类型的数据转码为byte类型.
	 * 
	 * @param fileData
	 *            String 类型的数据.
	 * @return 转码后的数据byte类型,发生异常或者filedate为null时返回null.
	 */
	public static byte[] decode(String fileData) {
		if (fileData == null) {
			return null;
		}
		BASE64Decoder decoder = new BASE64Decoder();
		try {
			return decoder.decodeBuffer(fileData);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 将InputStream类型的数据转码为String.
	 * 
	 * @param fileData
	 *            InputStream类型的数据.
	 * @return 转码后的数据String类型,发生异常或者filedate为null时返回null.
	 */
	public static String encode(InputStream fileData) {
		if (fileData == null) {
			return null;
		}
		BASE64Encoder encoder = new BASE64Encoder();
		byte[] _fileData;
		try {
			_fileData = new byte[fileData.available()];
			fileData.read(_fileData);
			fileData.close();
			return encoder.encode(_fileData).replaceAll("\\s*","");
		} catch (IOException e) {
			e.printStackTrace();
			//logger.info(e.toString());
		}
		return null;
	}
	
	/**对String编码.
	 * @param string
	 * @return
	 */
	public static String encodeToString(String string){
		if(string==null){
			return null;
		}else{
			return encode(string.getBytes());
		}
	}
	public static String decodeToString(String string){
		if(string==null){
			return null;
		}else{
			byte[] _byte=decode(string);
			return new String(_byte);
		}
	}
	
	/**
	 * 将对象编码为base64的String.
	 * 
	 * @param object
	 *            要进行编码的对象.
	 * @return 编码后的对象对应的bease64String.
	 */
	public static String encodeObject(Object object) {
		String objectString = null;
		ByteArrayOutputStream arrayOutputStream = new ByteArrayOutputStream();
		try {
			ObjectOutputStream objectOutputStream = new ObjectOutputStream(
					arrayOutputStream);
			objectOutputStream.writeObject(object);
			objectString = encode(arrayOutputStream
					.toByteArray());
		} catch (IOException e) {
			//logger.info(e.toString());
			e.printStackTrace();
		}
		return objectString;
	}

	/**
	 * 将bease64的String解编码为对象;
	 * 
	 * @param objectString
	 *            可以解编码为Object的Base64String,如果不能解编码为Object抛出异常.
	 * @return 解编码成功后的对象.
	 */
	public static Object decodeObject(String objectString) {
		Object object = null;
		ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(
				decode(objectString));
		try {
			ObjectInputStream objectInputStream = new ObjectInputStream(
					byteArrayInputStream);
			object = objectInputStream.readObject();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return object;
	}
}

