package com.example.arithmetic;

import java.io.FileInputStream;
import java.io.FileOutputStream;

/**
 * 图片BASE64加密解密
 * @author zoujianhua
 * 2014-12-8
 * @version 1.0
 */
public class PicBase64 {
	/**
	 * 图片BASE64 编码
	 * 
	 * @author zhangym
	 * @create 2009-7-20 下午02:28:47
	 * @since
	 * @param picPath
	 * @return
	 */
	public static String getPicBASE64(String picPath) {
		String content = null;
		try {
			FileInputStream fis = new FileInputStream(picPath);
			byte[] bytes = new byte[fis.available()];
			fis.read(bytes);
			content = new sun.misc.BASE64Encoder().encode(bytes); // 具体的编码方法
			fis.close();
			System.out.println(content.length());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return content;
	}

	/**
	 * 图片BASE64 编码
	 * 
	 * @author zhangym
	 * @create 2009-7-20 下午02:28:54
	 * @since
	 * @param base64str
	 * @param outPicPath
	 */
	public static void getPicFromBASE64(String base64str, String outPicPath) {
		try {
			byte[] result = new sun.misc.BASE64Decoder().decodeBuffer(base64str
					.trim());
			FileOutputStream fos = new FileOutputStream(outPicPath); // r,rw,rws,rwd
			fos.write(result);
			fos.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
