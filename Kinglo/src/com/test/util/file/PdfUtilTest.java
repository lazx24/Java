package com.test.util.file;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import javax.imageio.ImageIO;

import org.junit.Test;

import com.common.util.file.FileUtil;
import com.common.util.file.PDFUtil;

public class PdfUtilTest {
    
    @Test
    public void mergePDF() throws IOException{
	InputStream in=new FileInputStream(new File("D:\\poc.pdf"));
	InputStream in2=new FileInputStream(new File("D:\\poc2.pdf"));
	InputStream[] inArray = new InputStream[]{in,in2};
//	byte[] bytes=PDFUtil.mergePDF(inArray);
//	OutputStream out=new FileOutputStream(new File("D:\\poc3.pdf"));
//	out.write(bytes);
//	out.flush();
//	out.close();
	
	OutputStream destinationOutputStream = new FileOutputStream("D:\\poc4.pdf");
	
	PDFUtil.mergePDF(destinationOutputStream, inArray);
    }
    
    @Test
    public void convertAutoPrintPdf(){
	PDFUtil.convertAutoPrintPdf("D:\\poc.pdf");
    }
    
    @Test
    public void convertImage() throws IOException {
//	List<File> imageList = PDFUtil
//		.convertImageFiles("D:\\poc.pdf");
//	for (int i = 0; i < imageList.size(); i++) {
//	    File file=imageList.get(i);
//	    BufferedImage image=ImageIO.read(file);
//	    image.flush();
//	}
	
	List<File> imageList=PDFUtil.convertImageFiles("D:\\poc.pdf", "D:\\photo", 1, 2);
	for (int i = 0; i < imageList.size(); i++) {
	    File file=imageList.get(i);
	    BufferedImage image=ImageIO.read(file);
	    image.flush();
	}
	
    }
    
    @Test
    public void mergePdfFiles() throws IOException{
	
	OutputStream destinationOutputStream =new FileOutputStream("D:\\poc10.pdf");
	PDFUtil.mergePdfFiles(destinationOutputStream, "D:\\poc11.pdf", new String[]{"D:\\poc.pdf","D:\\poc2.pdf"});
	destinationOutputStream.flush();
	destinationOutputStream.close();
    }

    @Test
    public void encrypFile() {
	String pdfFile = "D:\\poc2.pdf";
	PDFUtil.encryp(pdfFile, "12345", "qqqq", 2068);
    }

    @Test
    public void encrypBytes() {
	String pdfFile = "c:/temp/Spring_FrameWork3.0开发教程.pdf";
	byte[] bytes = FileUtil.readBytesFromFile(pdfFile);
	InputStream inputStream = new ByteArrayInputStream(bytes);
	byte[] encrptBytes = PDFUtil.encryp(inputStream, "12345", null, 2068);

	FileUtil.writeBytesToFile(encrptBytes,
		"c:/temp/Spring_FrameWork3.0开发教程_ec.pdf");
    }

    @Test
    public void encrypInputStream() {
	try {
	    InputStream inputStream = new FileInputStream(
		    "c:/temp/2012年度全国职称外语等级考试.pdf");

	    byte[] bytes = PDFUtil.encryp(inputStream, null, "12345", 2068);
	    FileUtil.writeBytesToFile(bytes, "c:/temp/2012年度全国职称外语等级考试_加密2.pdf");
	} catch (FileNotFoundException e) {
	    e.printStackTrace();
	}
    }
}
