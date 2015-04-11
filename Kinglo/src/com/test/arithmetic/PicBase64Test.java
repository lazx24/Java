package com.test.arithmetic;

import org.junit.Test;

import com.example.arithmetic.PicBase64;

public class PicBase64Test {

	@Test
	public void testGetPicBASE64() {
		String path="D:\\meinv.jpg";//图片路径
		System.out.println("本地图片路径为:"+path);
		String result=PicBase64.getPicBASE64(path);
		System.out.println("图片Base64加密后字符串:"+result);
	}

	@Test
	public void testGetPicFromBASE64() {
		String path="D:\\meinv.jpg";//图片路径
		System.out.println("本地图片路径为:"+path);
		String result=PicBase64.getPicBASE64(path);
		System.out.println("图片Base64加密后字符串:"+result);
		
		System.out.println("根据"+result+"解密生成图片");
		String outPathString="D:\\meinv2.jpg";
		System.out.println("生成图片的路径为："+outPathString);
		PicBase64.getPicFromBASE64(result, outPathString);
		System.out.println("生成图片成功!");
	}

}
