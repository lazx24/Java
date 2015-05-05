package com.common.util.file;

import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.imageio.ImageIO;

import org.apache.pdfbox.util.PDFMergerUtility;

import com.common.exception.ShipSuiteRuntimeException;
import com.common.util.date.DateUtil;
import com.common.util.html.JavaScriptUtil;
import com.itextpdf.text.pdf.PdfCopy;
import com.itextpdf.text.pdf.PdfImportedPage;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;

public final class PDFUtil {
    
    /**
     * 合并PDF文件
     * @param toMergeInputStreams PDF文件输入流数组
     * @return	字节数组数据
     */
    public static byte[] mergePDF(InputStream[] toMergeInputStreams) {
	ByteArrayOutputStream destinationOutputStream = new ByteArrayOutputStream(
		1024);
	try {
	    toMergePDF(destinationOutputStream, null, null, toMergeInputStreams);
	    return destinationOutputStream.toByteArray();
	} finally {
	    try {
		if (destinationOutputStream != null) {
		    destinationOutputStream.close();
		}
	    } catch (IOException localIOException1) {
	    }
	}
    }
    
    /**
     * 合并PDF文件
     * @param destinationOutputStream 	输出流
     * @param toMergeInputStreams	PDF文件输入流数组
     */
    public static void mergePDF(OutputStream destinationOutputStream,
	    InputStream[] toMergeInputStreams) {
	toMergePDF(destinationOutputStream, null, null, toMergeInputStreams);
    }
    
    /**
     * 合并PDF文件
     * @param destinationOutputStream	输出流
     * @param toMergeFiles		字符串数组
     */
    public static void mergePDF(OutputStream destinationOutputStream,
	    String[] toMergeFiles) {
	toMergePDF(destinationOutputStream, null, toMergeFiles, null);
    }
    
    /**
     * 合并PDF文件
     * @param destinationFileName	文件名
     * @param toMergeInputStreams	PDF文件输入流数组
     */
    public static void mergePDF(String destinationFileName,
	    InputStream[] toMergeInputStreams) {
	toMergePDF(null, destinationFileName, null, toMergeInputStreams);
    }
    
    /**
     * 合并PDF文件
     * @param destinationFileName	文件名
     * @param toMergeFiles		字符串数组
     */
    public static void mergePDF(String destinationFileName,
	    String[] toMergeFiles) {
	toMergePDF(null, destinationFileName, toMergeFiles, null);
    }

    private static void toMergePDF(OutputStream destinationOutputStream,
	    String destinationFileName, String[] toMergeFiles,
	    InputStream[] toMergeInputStreams) {
	if (((destinationOutputStream == null) && (destinationFileName == null))
		|| (((toMergeFiles == null) || (toMergeFiles.length == 0)) && ((toMergeInputStreams == null) || (toMergeInputStreams.length == 0)))) {
	    return;
	}
	PDFMergerUtility util = new PDFMergerUtility();
	if (destinationFileName != null) {
	    util.setDestinationFileName(destinationFileName);
	}
	if (destinationOutputStream != null) {
	    util.setDestinationStream(destinationOutputStream);
	}
	if (toMergeFiles != null) {
	    for (String toMergeFile : toMergeFiles) {
		if (toMergeFile != null) {
		    util.addSource(toMergeFile);
		}
	    }
	}
	if (toMergeInputStreams != null) {
	    for (InputStream stream : toMergeInputStreams) {
		if (stream != null) {
		    util.addSource(stream);
		}
	    }
	}
	try {
	    util.mergeDocuments();
	} catch (Exception e) {
	    throw new ShipSuiteRuntimeException(e);
	}
    }
    
    /**
     * 打印PDF
     * @param pdfFilename
     */
    public static void convertAutoPrintPdf(String pdfFilename) {
	convertAutoPrintPdf(pdfFilename, pdfFilename);
    }
    
    /**
     * 打印PDF
     * @param pdfFilename
     * @param newPdfFilename
     */
    public static void convertAutoPrintPdf(String pdfFilename,
	    String newPdfFilename) {
	byte[] data = FileUtil.readBytesFromFile(pdfFilename);
	if ((data != null) && (data.length > 0)) {
	    ServiceFile serviceFile = createAutoPrintPdf(data, pdfFilename);
	    FileUtil.writeBytesToFile(serviceFile.getData(), newPdfFilename);
	} else {
	    throw new ShipSuiteRuntimeException("File not exists or invalid.");
	}
    }

    public static ServiceFile createAutoPrintPdf(String pdfFilename) {
	byte[] data = FileUtil.readBytesFromFile(pdfFilename);
	if ((data != null) && (data.length > 0)) {
	    return createAutoPrintPdf(data, pdfFilename);
	}
	return null;
    }

    public static ServiceFile createAutoPrintPdf(byte[] pdfStream,
	    String printFilename) {
	PdfReader reader = null;
	ByteArrayOutputStream bos = null;
	PdfStamper stamp = null;
	try {
	    reader = new PdfReader(pdfStream);
	    StringBuffer script = new StringBuffer();
	    script.append(JavaScriptUtil.getScriptCloseMe());
	    script.append(
		    "\r\nthis.print({bUI: true,bSilent: false,bShrinkToFit: false});")
		    .append("\r\nthis.closeDoc();");
	    bos = new ByteArrayOutputStream(pdfStream.length);

	    stamp = new PdfStamper(reader, bos);
	    stamp.setViewerPreferences(28672);

	    stamp.addJavaScript(script.toString());
	    stamp.close();
	    stamp = null;
	    return new ServiceFile(printFilename, bos.toByteArray());
	} catch (Exception e) {
	    throw new ShipSuiteRuntimeException(e);
	} finally {
	    if (reader != null) {
		reader.close();
	    }
	    if (bos != null) {
		try {
		    bos.close();
		} catch (IOException localIOException1) {
		}
	    }
	    if (stamp != null) {
		try {
		    stamp.close();
		} catch (Exception localException2) {
		}
	    }
	}
    }
    
    /**
     * 转换PDF文件为图片文件
     * @param pdfFilePath PDF文件路径
     * @return
     */
    public static List<File> convertImageFiles(String pdfFilePath) {
	return convertImageFiles(pdfFilePath, null, null, null);
    }
    
    /**
     * 转换PDF文件为图片文件
     * @param pdfFile		PDF文件路径
     * @param imageDirectory	图片保存路径
     * @param pageFrom		从多少页开始
     * @param pageTo		从多少页结束
     * @return
     */
    public static List<File> convertImageFiles(String pdfFile,
	    String imageDirectory, Integer pageFrom, Integer pageTo) {
	org.icepdf.core.pobjects.Document document = new org.icepdf.core.pobjects.Document();
	try {
	    document.setFile(pdfFile);
	} catch (Exception localException) {
	}
	float scale = 1.0F;

	float rotation = 0.0F;
	if (imageDirectory == null) {
	    imageDirectory = FileUtil.getPathOfFile(pdfFile);
	}
	List<File> imageFileList = new ArrayList<File>();
	for (int i = 0; i < document.getNumberOfPages(); i++) {
	    if ((pageFrom == null) || (i >= pageFrom.intValue())) {
		if ((pageTo != null) && (i > pageTo.intValue())) {
		    break;
		}
		BufferedImage image = (BufferedImage) document.getPageImage(i,
			1, 2, rotation, scale);
		try {
		    RenderedImage rendImage = image;
		    File file = new File(String.format(
			    "%s/%s-%d.png",
			    new Object[] { imageDirectory,
				    FileUtil.getPureFilename(pdfFile),
				    Integer.valueOf(i) }));

		    ImageIO.write(rendImage, "png", file);
		    imageFileList.add(file);
		} catch (IOException e) {
		    e.printStackTrace();
		}
		image.flush();
	    }
	}
	document.dispose();

	return imageFileList;
    }
    
    /**
     * 合并PDF文件
     * @param destinationOutputStream 	输出流
     * @param destinationFileName	文件名
     * @param files			字符串数组
     */
    public static void mergePdfFiles(OutputStream destinationOutputStream,
	    String destinationFileName, String[] files) {
	try {
	    com.itextpdf.text.Document document = new com.itextpdf.text.Document(
		    new PdfReader(files[0]).getPageSize(1));

	    OutputStream dos = null;
	    if (destinationFileName != null) {
		dos = new FileOutputStream(destinationFileName);
	    }
	    if (destinationOutputStream != null) {
		dos = destinationOutputStream;
	    }
	    PdfCopy copy = new PdfCopy(document, dos);

	    document.open();
	    for (int i = 0; i < files.length; i++) {
		PdfReader reader = new PdfReader(files[i]);

		int n = reader.getNumberOfPages();
		for (int j = 1; j <= n; j++) {
		    document.newPage();
		    PdfImportedPage page = copy.getImportedPage(reader, j);
		    copy.addPage(page);
		}
	    }
	    document.close();
	} catch (Exception e) {
	    e.printStackTrace();
	}
    }
    
    /**
     * PDF文件加密
     * @param pdfFile		PDF文件路径
     * @param userPassWord	用户密码
     */
    public static void encryp(String pdfFile, String userPassWord) {
	encryp(pdfFile, userPassWord, null);
    }
    
    /**
     * PDF文件加密
     * @param pdfFile		PDF文件路径
     * @param userPassWord	用户密码
     * @param ownerPassWord	貌似没用
     */
    public static void encryp(String pdfFile, String userPassWord,
	    String ownerPassWord) {
	encryp(pdfFile, userPassWord, ownerPassWord, 2068);
    }
    
    /**
     * PDF文件加密
     * @param pdfFile		PDF文件路径
     * @param userPassWord	用户密码
     * @param ownerPassWord	貌似没用
     * @param permission	用户权限
     */
    public static void encryp(String pdfFile, String userPassWord,
	    String ownerPassWord, int permission) {
	String tempFile = FileUtil.getSystemTempPath()
		+ DateUtil.dateFormatToString(new Date(),DateUtil.MILLIONS_DATE_FORMAT)
		+ FileUtil.getPureFilename(pdfFile);
	PdfStamper stamper = null;
	try {
	    FileUtil.copyFile(pdfFile, tempFile);

	    PdfReader reader = new PdfReader(tempFile);
	    stamper = new PdfStamper(reader, new FileOutputStream(pdfFile));

	    stamper.setEncryption(
		    userPassWord == null ? null : userPassWord.getBytes(),
		    ownerPassWord == null ? null : ownerPassWord.getBytes(),
		    permission, true);
	    stamper.close();
	    stamper = null;
	} catch (Exception e) {
	    throw new ShipSuiteRuntimeException(e);
	} finally {
	    if (stamper != null) {
		try {
		    stamper.close();
		} catch (Exception localException1) {
		}
	    }
	}
    }
    
    /**
     * PDF文件加密
     * @param pdfFile		PDF文件输入流
     * @param userPassWord	用户密码
     * @return
     */
    public static byte[] encryp(InputStream pdfFile, String userPassWord) {
	return encryp(pdfFile, userPassWord, null);
    }
    
    /**
     * PDF文件加密
     * @param pdfFile		PDF文件输入流
     * @param userPassWord	用户密码
     * @param ownerPassWord	貌似没用
     * @return
     */
    public static byte[] encryp(InputStream pdfFile, String userPassWord,
	    String ownerPassWord) {
	return encryp(pdfFile, userPassWord, ownerPassWord, 2068);
    }
    
    /**
     * PDF文件加密
     * @param pdfFile		PDF文件输入流
     * @param userPassWord	用户密码
     * @param ownerPassWord	貌似没用
     * @param permission	用户权限
     * @return
     */
    public static byte[] encryp(InputStream pdfFile, String userPassWord,
	    String ownerPassWord, int permission) {
	PdfStamper stamper = null;
	try {
	    PdfReader reader = new PdfReader(pdfFile);
	    ByteArrayOutputStream baos = new ByteArrayOutputStream();
	    stamper = new PdfStamper(reader, baos);

	    stamper.setEncryption(
		    userPassWord == null ? null : userPassWord.getBytes(),
		    ownerPassWord == null ? null : ownerPassWord.getBytes(),
		    permission, true);
	    stamper.close();
	    stamper = null;
	    return baos.toByteArray();
	} catch (Exception e) {
	    throw new ShipSuiteRuntimeException(e);
	} finally {
	    if (stamper == null) {
	    }
	}
    }
}
