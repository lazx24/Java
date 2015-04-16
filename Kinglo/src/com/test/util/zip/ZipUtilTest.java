package com.test.util.zip;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;

import org.junit.Test;

import com.common.util.file.FileUtil;
import com.common.util.generic.DateUtil;
import com.common.util.string.StringUtil;
import com.common.util.zip.ZipUtil;

public class ZipUtilTest {
    @Test
    public void compress() throws FileNotFoundException{
	OutputStream os =new FileOutputStream(new File("D:\\a.txt"));
	ZipUtil.compress(new String("中国人").getBytes(), os);
	
	InputStream in=new FileInputStream(new File("D:\\a.txt"));
	byte[] bytes=ZipUtil.decompress(in);
	System.out.println(new String(bytes));
    }
    
    @Test
    public void compressFile() {
	String[] filenameArray = {
		"InboundFreighttList20120810101607601[1].pdf",
		"InboundFreighttList20120925161225048[1].pdf", "J1844101.pdf",
		"merged.pdf", "merged.pdf",
		"ObCorrectAdvice20120917134213629[1].pdf",
		"ObCorrectAdvice20120917134219651[1].pdf" };
	for (String filename : filenameArray) {
	    String filePath = "d:\\temp\\" + filename;
	    String zipFilename = filePath + ".zip";
	    byte[] bytes = FileUtil.readBytesFromFile(filePath);

	    System.out.println(String.format(
		    "Start compress %s at %s.",
		    new Object[] { filename,
			    DateUtil.getComplexDateString(new Date()) }));
	    byte[] zipBytes = ZipUtil.compress(bytes);

	    System.out.println(String.format(
		    "End compress %s at %s.",
		    new Object[] { filename,
			    DateUtil.getComplexDateString(new Date()) }));

	    FileUtil.writeBytesToFile(zipBytes, zipFilename);
	}
    }

    @Test
    public void compressString() {
	String html = FileUtil.readFile(
		"D:\\ww.txt", "GBK");
	System.out.println(html.length());
	System.out.println(StringUtil.bytesToString(StringUtil
		.stringToBytes(html)));
	String zippedString = ZipUtil.compressString(html);
	System.out.println("zip after:"+zippedString);
	System.out.println("---------------------------------------------");
	String htmlString = ZipUtil.decompressString(zippedString);
	System.out.println(htmlString);
	System.out.println(zippedString.length() / html.length());
    }

    @Test
    public void compressString2Byte() {
	String html = FileUtil.readFile(
		"D:\\ww.txt", "GBK");

	byte[] zippedBytes = ZipUtil.compressString2Byte(html);
	for (byte b : zippedBytes) {
	    System.out.print(b);
	}
	System.out.println("---------------------------------------------");
	String htmlString = ZipUtil.decompressStringByBytes(zippedBytes);
	System.out.println(htmlString);
	System.out.println(zippedBytes.length / html.length());
    }
}
