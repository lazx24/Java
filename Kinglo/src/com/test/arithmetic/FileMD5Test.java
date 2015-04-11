package com.test.arithmetic;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.junit.Test;

import com.example.arithmetic.FileMD5;
import com.example.file.CreateFileUtil;
import com.example.file.WriteToFile;

public class FileMD5Test {

	@Test
	public void testFileMD5String() {
		String content="abcdedf";
		String content2="abcded";
		try {
			System.out.println("比较文本文件中的内容字符串是否相同");
			System.out.println("初始化字符串1为:"+content);
			System.out.println("初始化字符串2为："+content2);
			String md5 = FileMD5.fileMD5(content);
			String md52=FileMD5.fileMD5(content2);
			System.out.println("文件内容字符串是否相同:"+md5.equals(md52));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testFileMD5InputStream() {
		try {
			System.out.println("比较文件的输入流是否相同(或者说比较两个文件是否相同!)");
			
			String filePath="D:\\a.txt";
			CreateFileUtil.createFile(filePath);
			WriteToFile.writeFile(filePath, "abc", "utf-8");
			InputStream input=new FileInputStream(filePath);
			String md53 = FileMD5.fileMD5(input);
			String filePath2="D:\\b.txt";
			CreateFileUtil.createFile(filePath2);
			WriteToFile.writeFile(filePath2, "abcd", "utf-8");
			InputStream input2=new FileInputStream(filePath2);
			String md54=FileMD5.fileMD5(input2);
			System.out.println("两个文件是否相同:"+md53.equals(md54));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
