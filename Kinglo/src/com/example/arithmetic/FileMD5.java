package com.example.arithmetic;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
/**
 * 根据MD5算法来比较文件内容是否相同
 * @author Administrator
 *
 */
public class FileMD5 {
	
	/**
	 * 比较文本文件中的字符串是否相同
	 * @param content
	 * @return
	 * @throws IOException
	 */
	public static String fileMD5(String content) throws IOException {  
	      int bufferSize = 256 * 1024;  
	      DigestInputStream digestInputStream = null;  
	      ByteArrayInputStream in=null;
	      try {  
	         MessageDigest messageDigest =MessageDigest.getInstance("MD5");  
	         in=new ByteArrayInputStream(content.getBytes());
	         digestInputStream = new DigestInputStream(in,messageDigest);  
	         byte[] buffer =new byte[bufferSize];  
	         while (digestInputStream.read(buffer) > 0);  
	         messageDigest= digestInputStream.getMessageDigest();  
	         byte[] resultByteArray = messageDigest.digest();  
	         return byteArrayToHex(resultByteArray);  
	      } catch (NoSuchAlgorithmException e) {  
	         return null;  
	      } finally{  
	         try {  
	            digestInputStream.close();  
	         } catch (Exception e) {  
	        	 e.printStackTrace();
	         }  
	         try {  
	            in.close();  
	         } catch (Exception e) {  
	        	 e.printStackTrace();
	         }  
	      }  
	  
	   }  
	
	/**
	 * 根据输入流来比较文件内容是否相同
	 * @param in
	 * @return
	 * @throws IOException
	 */
	public static String fileMD5(InputStream in) throws IOException {  
	      int bufferSize = 256 * 1024;  
	      DigestInputStream digestInputStream = null;  
	      try {  
	         MessageDigest messageDigest =MessageDigest.getInstance("MD5");  
	         digestInputStream = new DigestInputStream(in,messageDigest);  
	         byte[] buffer =new byte[bufferSize];  
	         while (digestInputStream.read(buffer) > 0);  
	         messageDigest= digestInputStream.getMessageDigest();  
	         byte[] resultByteArray = messageDigest.digest();  
	         return byteArrayToHex(resultByteArray);  
	      } catch (NoSuchAlgorithmException e) {  
	         return null;  
	      } finally{  
	         try {  
	            digestInputStream.close();  
	         } catch (Exception e) {  
	        	 e.printStackTrace();
	         }  
	         try {  
	            in.close();  
	         } catch (Exception e) {  
	        	 e.printStackTrace();
	         }  
	      }  
	  
	   }  
	
	private static String byteArrayToHex(byte[] byteArray) {  
		   char[] hexDigits = {'0','1','2','3','4','5','6','7','8','9', 'A','B','C','D','E','F' };  
		   char[] resultCharArray =new char[byteArray.length * 2];  
		   int index = 0;  
		   for (byte b : byteArray) {  
		      resultCharArray[index++] = hexDigits[b>>> 4 & 0xf];  
		      resultCharArray[index++] = hexDigits[b& 0xf];  
		   }  
		   return new String(resultCharArray);  
	}
}
