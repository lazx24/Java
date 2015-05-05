package com.common.util.zip;

import java.io.File;
import java.util.Date;
import java.util.Locale;

import org.apache.tools.ant.types.selectors.DateSelector;

import com.common.util.date.DateUtil;
/**
 * 压缩类
 * 类的描述: 此压缩文件类压缩成功后会删除被压缩的文件
 * 创建人:邹建华
 * 创建时间:2015-4-11
 */
public final class ArchiveUtil {
    /**
     * 压缩文件
     * @param archiveDescPath 压缩保存的目录
     * @param sourceFile      待压缩的文件
     * @return File对象
     */
    public static File archive(String archiveDescPath, File sourceFile) {
	String destZipFilename = generateArchiveFilename(archiveDescPath);
	Zipper.zip(sourceFile, destZipFilename, true);
	return new File(destZipFilename);
    }
    
    /**
     * 压缩文件
     * @param archiveDescPath	压缩保存的目录
     * @param sourceFileArray	待压缩的文件
     * @return File对象
     */
    public static File archive(String archiveDescPath, File[] sourceFileArray) {
	String destZipFilename = generateArchiveFilename(archiveDescPath);
	Zipper.zip(sourceFileArray, destZipFilename, true);
	return new File(destZipFilename);
    }
    
    /**
     * 压缩文件
     * @param archiveDescPath 	压缩保存的目录
     * @param fileArray		文件数组
     * @param endDate		文件的最后修改时间在此之前的文件才会被压缩
     * @return File对象
     */
    public static File archive(String archiveDescPath, File[] fileArray,
	    Date endDate) {
	String destZipFilename = generateArchiveFilename(archiveDescPath);

	Zipper.zip(fileArray, destZipFilename, null, endDate, true);
	return new File(destZipFilename);
    }
    
    /**
     * 压缩文件
     * @param archiveDescPath	压缩保存的目录
     * @param sourceFileArray	文件数组
     * @param destZipFilename	压缩后的文件名(包含扩展名)
     * @return File对象
     */
    public static File archive(String archiveDescPath, File[] sourceFileArray,
	    String destZipFilename) {
	String destZip = archiveDescPath + File.separator + destZipFilename;
	Zipper.zip(sourceFileArray, destZip, true);
	return new File(destZipFilename);
    }
    
    /**
     * 压缩文件
     * @param archiveDescPath	压缩保存的目录
     * @param pathFilenamePattern 文件名字通配模式
     * @return File对象
     */
    public static File archive(String archiveDescPath,
	    String pathFilenamePattern) {
	String zipFilename = generateArchiveFilename(archiveDescPath);
	Zipper.zip(pathFilenamePattern, zipFilename, true);
	return new File(zipFilename);
    }
    
    /**
     * 压缩文件
     * @param archiveDescPath		压缩保存的目录
     * @param pathFilenamePattern 	文件名字通配模式
     * @param endDate			文件的最后修改时间在此之前的文件才会被压缩
     * @return File对象
     */
    public static File archive(String archiveDescPath,
	    String pathFilenamePattern, Date endDate) {
	String zipFilename = generateArchiveFilename(pathFilenamePattern);

	DateSelector dateSelector = new DateSelector();
	dateSelector.setWhen(DateSelector.TimeComparisons.BEFORE);

	dateSelector.setDatetime(DateUtil.dateFormatToString(
		endDate == null ? new Date() : endDate, "MM/dd/yyyy HH:mm a"));

	Zipper.zip(pathFilenamePattern, zipFilename, dateSelector, true);
	return new File(zipFilename);
    }
    
    /**
     * 压缩文件
     * @param archiveDescPath		压缩保存的目录
     * @param pathFilenamePattern	文件名字通配模式
     * @param destZipFilename		压缩后的文件名(请声名扩展名)
     * @return
     */
    public static File archive(String archiveDescPath,
	    String pathFilenamePattern, String destZipFilename) {
	String destZip = archiveDescPath + File.separator + destZipFilename;

	Zipper.zip(pathFilenamePattern, destZip, true);
	return new File(destZipFilename);
    }
    
    /**
     * 压缩文件
     * @param archiveDescPath		压缩保存的目录
     * @param sourceFilenameArray	文件字符串数组
     * @param destZipFilename		压缩后的文件名(请 声名扩展名)
     * @return File对象
     */
    public static File archive(String archiveDescPath,
	    String[] sourceFilenameArray, String destZipFilename) {
	String destZip = archiveDescPath + File.separator + destZipFilename;
	Zipper.zip(sourceFilenameArray, destZip, true);
	return new File(destZipFilename);
    }
    
    /**
     * 根据压缩保存的目录生成压缩后的文件名
     * @param archiveDescPath	压缩保存的目录
     * @return 文件名
     */
    public static String generateArchiveFilename(String archiveDescPath) {
	StringBuffer destZipFilename = new StringBuffer();

	destZipFilename
		.append(archiveDescPath == null ? "." : archiveDescPath)
		.append(File.separator)
		.append(DateUtil.dateFormatToString(new Date(), "yyyyMMddHHmmssSSS"))
		.append(".zip");
	return destZipFilename.toString();
    }
}
