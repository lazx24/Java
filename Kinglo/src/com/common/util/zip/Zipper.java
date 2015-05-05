package com.common.util.zip;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.apache.tools.ant.Project;
import org.apache.tools.ant.taskdefs.Zip;
import org.apache.tools.ant.types.resources.FileResource;
import org.apache.tools.ant.types.selectors.DateSelector;
import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipFile;

import com.common.context.ApplicationContextUtil;
import com.common.util.date.DateUtil;
import com.common.util.file.FileUtil;
import com.common.util.file.ServiceFile;
import com.common.util.generic.CollectionUtil;

/**
 * 
 * 类的描述:解压缩
 * 创建人:邹建华
 * 创建时间:2015-4-11
 */
public final class Zipper {

    private static boolean betweenDate(File file, Date fromDate, Date toDate) {
	if ((fromDate == null) && (toDate == null)) {
	    return true;
	}
	Date fileDate = new Date(file.lastModified());
	if (fromDate == null) {
	    return !toDate.before(fileDate);
	}
	if (toDate == null) {
	    return !fromDate.after(fileDate);
	}
	if (fromDate.after(fileDate)) {
	    return false;
	}
	if (toDate.before(fileDate)) {
	    return false;
	}
	return true;
    }

    private static Set<File> getArchiveFileSet(String srcPathName) {
	File srcdir = new File(srcPathName);

	Set<File> archiveFileSet = null;
	if (srcdir.exists()) {
	    if (srcdir.isDirectory()) {
		archiveFileSet = FileUtil.findMatchingFileSystemResources(
			srcdir, "*");
	    } else if (srcdir.isFile()) {
		archiveFileSet = new HashSet<File>(1);
		archiveFileSet.add(srcdir);
	    }
	} else if ((srcPathName.contains("?")) || (srcPathName.contains("*"))) {
	    int index = srcPathName.lastIndexOf(File.separator);
	    srcdir = new File(srcPathName.substring(0, index));
	    if (!srcdir.exists()) {
		throw new RuntimeException(srcdir.getName() + " not exits.");
	    }
	    String pattern = srcPathName.substring(index + 1);
	    archiveFileSet = FileUtil.findMatchingFileSystemResources(srcdir,
		    pattern);
	} else {
	    archiveFileSet = Collections.emptySet();
	}
	return archiveFileSet;
    }

    /**
     * 解压缩
     * @param zipFileName	压缩文件名
     * @param placePath		解压缩目录
     * @param filenames		需要解压的文件名
     * @throws IOException
     */
    public static void unZip(String zipFileName, String placePath,
	    String[] filenames) throws IOException {
	ZipFile zipFile = new ZipFile(zipFileName);
	Enumeration<?> e = zipFile.getEntries();
	ZipEntry zipEntry = null;
	if ((!placePath.trim().endsWith("/"))
		&& (!placePath.trim().endsWith("\\")))
	    placePath = placePath.trim() + "/";
	File placeFile = new File(placePath);
	if (!placeFile.exists()) {
	    placeFile.mkdir();
	}
	while (e.hasMoreElements()) {
	    zipEntry = (ZipEntry) e.nextElement();
	    String entryName = zipEntry.getName();
	    boolean matched = false;
	    if ((filenames != null) && (filenames.length > 0)) {
		for (String name : filenames) {
		    if (entryName.equalsIgnoreCase(name)) {
			matched = true;
			break;
		    }
		}
	    }

	    if ((matched) || (!matched && (filenames == null || filenames.length ==0 ))) {
		String[] names = entryName.split("/");
		int length = names.length;
		String path = placePath;

		for (int v = 0; v < length; v++) {
		    if (v < length - 1) {
			path = path + names[v] + "/";
			new File(path).mkdir();
		    } else if (entryName.endsWith("/")) {
			new File(placePath + entryName).mkdir();
		    } else {
			InputStream in = zipFile.getInputStream(zipEntry);
			OutputStream os = new FileOutputStream(new File(
				placePath + entryName));
			byte[] buf = new byte[1024];
			int len;
			while ((len = in.read(buf)) > 0) {
			    os.write(buf, 0, len);
			}
			in.close();
			os.close();
		    }
		}
	    }
	}

	zipFile.close();
    }

    /**
     * 解压缩文件转换为ServiceFile
     * @param zipFileName	解压缩文件名
     * @param filenames		需要压缩的文件名
     * @return			ServiceFile文件的集合
     * @throws IOException
     */
    public static List<ServiceFile> unZip2ServiceFile(String zipFileName,
	    String... filenames) throws IOException {
	ZipFile zipFile = new ZipFile(zipFileName);
	Enumeration<?> e = zipFile.getEntries();
	ZipEntry zipEntry = null;
	List<ServiceFile> serviceFileList = new ArrayList<ServiceFile>();
	while (e.hasMoreElements()) {
	    zipEntry = (ZipEntry) e.nextElement();
	    String entryName = zipEntry.getName();
	    boolean matched = false;
	    if ((!entryName.endsWith("/")) && (!entryName.endsWith("\\"))) {
		if ((filenames != null) && (filenames.length > 0)) {
		    for (String name : filenames) {
			if (entryName.equalsIgnoreCase(name)) {
			    matched = true;
			    break;
			}
		    }
		    if (!matched) {
			break;
		    }
		}
		
		if((matched) || (!matched && (filenames == null || filenames.length ==0 ))){
		    InputStream in = zipFile.getInputStream(zipEntry);
		    ByteArrayOutputStream os = new ByteArrayOutputStream(1024);
		    byte[] buf = new byte[1024];
		    int len;
		    while ((len = in.read(buf)) > 0) {
			os.write(buf, 0, len);
		    }
		    in.close();

		    ServiceFile serviceFile = new ServiceFile(entryName,
			    os.toByteArray());
		    os.close();

		    serviceFileList.add(serviceFile);
		}
	    }
	}
	zipFile.close();
	return serviceFileList;
    }
    
    /**
     * 压缩文件
     * @param srcFileArray	待压缩的文件列表
     * @param destZipFilename	压缩后文件名
     * @param fromDate		文件最后修改时间
     * @param toDate		文件最后修改时间
     * @param archiveMode	是否存档
     */
    public static void zip(Collection<File> srcFileArray,
	    String destZipFilename, Date fromDate, Date toDate,
	    boolean archiveMode) {
	FileResourceCollection fileResourceCollection = new FileResourceCollection();
	for (File srcFile : srcFileArray) {
	    fileResourceCollection.add(srcFile);
	}
	zip(fileResourceCollection, destZipFilename, fromDate, toDate,
		archiveMode);
    }
    
    /**
     * 压缩文件
     * @param srcFile           待压缩的文件
     * @param destZipFilename	压缩后的文件名
     * @param archiveMode	是否存档
     */
    public static void zip(File srcFile, String destZipFilename,
	    boolean archiveMode) {
	zip(new File[] { srcFile }, destZipFilename, null, null, archiveMode);
    }
    
    /**
     * 压缩文件
     * @param srcFileArray	待压缩的文件数组
     * @param destZipFilename	压缩后的文件名
     * @param archiveMode	是否存档
     */
    public static void zip(File[] srcFileArray, String destZipFilename,
	    boolean archiveMode) {
	zip(srcFileArray, destZipFilename, null, null, archiveMode);
    }
    
    /**
     * 压缩文件
     * @param srcFileArray	待压缩的文件数组
     * @param destZipFilename	压缩后的文件名
     * @param fromDate		文件的最后修改时间
     * @param toDate		文件的最后修改时间
     * @param archiveMode	是否存档
     */
    public static void zip(File[] srcFileArray, String destZipFilename,
	    Date fromDate, Date toDate, boolean archiveMode) {
	FileResourceCollection fileResourceCollection = new FileResourceCollection();
	File[] arrayOfFile = srcFileArray;
	int j = srcFileArray.length;
	for (int i = 0; i < j; i++) {
	    File srcFile = arrayOfFile[i];
	    fileResourceCollection.add(srcFile);
	}
	zip(fileResourceCollection, destZipFilename, fromDate, toDate,
		archiveMode);
    }

    public static void zip(FileResourceCollection fileResourceCollection,
	    String destZipFilename, Date fromDate, Date toDate,
	    boolean archiveMode) {
	List<File> dirList = new ArrayList<File>();
	FileResourceCollection allFileResourceCollection = new FileResourceCollection();

	Iterator<?> localIterator1 = fileResourceCollection
		.getFileResourceList().iterator();
	File file;
	while (localIterator1.hasNext()) {
	    FileResource fileResource = (FileResource) localIterator1.next();
	    File f = fileResource.getFile();
	    if (f.isDirectory()) {
		dirList.add(f);
		for (Iterator<?> localIterator2 = FileUtil
			.findMatchingFileSystemResources(f, "*").iterator(); localIterator2
			.hasNext();) {
		    file = (File) localIterator2.next();
		    if (betweenDate(file, fromDate, toDate)) {
			allFileResourceCollection.add(file);
		    }
		}
	    } else {
		if (betweenDate(f, fromDate, toDate)) {
		    allFileResourceCollection.add(f);
		}
		if (!dirList.contains(f.getParentFile())) {
		    dirList.add(f.getParentFile());
		}
	    }
	}
	List<File> rootDirList = new ArrayList<File>();
	File f;
	if (dirList.isEmpty()) {
	    rootDirList.add(new File("/"));
	} else {
	    CollectionUtil.sort(dirList, new String[] { "path" });
	    String root = ((File) dirList.get(0)).getPath();
	    rootDirList.add(new File(root));
	    for (Iterator<File> iterator = dirList.iterator(); iterator
		    .hasNext();) {
		f = (File) iterator.next();
		if (!f.getPath().startsWith(root)) {
		    root = f.getPath();
		    rootDirList.add(new File(root));
		}
	    }
	}
	for (File root : rootDirList) {
	    Zip zip = new Zip();
	    zip.setUpdate(true);

	    Project prj = new Project();
	    prj.setBaseDir(root);
	    zip.setProject(prj);

	    zip.setDestFile(new File(destZipFilename));

	    zip.add(allFileResourceCollection);

	    zip.execute();
	}
	if ((archiveMode) && (fileResourceCollection != null)
		&& (!fileResourceCollection.getFileResourceList().isEmpty())) {
	    try {
		ZipFile zipFile = new ZipFile(destZipFilename);
		String zipPath = FileUtil.getPathOfFile(destZipFilename)
			+ File.separator;
		ZipEntry zipEntry = null;
		Enumeration<?> e = zipFile.getEntries();
		Set<String> zippedFilenameSet = new HashSet<String>();
		while (e.hasMoreElements()) {
		    zipEntry = (ZipEntry) e.nextElement();

		    zippedFilenameSet.add(zipPath + zipEntry.getName());
		}
		Iterator<?> localIterator3 = fileResourceCollection
			.getFileResourceList().iterator();
		while (localIterator3.hasNext()) {
		    FileResource fr = (FileResource) localIterator3.next();
		    fr.getFile().delete();
		}
	    } catch (IOException e1) {
		e1.printStackTrace();
	    }
	}
    }

    public static void zip(String srcPathName, String destZipFilename) {
	zip(srcPathName, destZipFilename, null, false);
    }

    public static void zip(String srcPathName, String destZipFilename,
	    boolean archiveMode) {
	zip(srcPathName, destZipFilename, null, archiveMode);
    }

    public static void zip(String srcPathName, String destZipFilename,
	    DateSelector dateSelector) {
	Set<File> archiveFileSet = getArchiveFileSet(srcPathName);

	zip(archiveFileSet, destZipFilename, null, null, false);
    }

    public static void zip(String srcPathName, String destZipFilename,
	    DateSelector dateSelector, boolean archiveMode) {
	Set<File> archiveFileSet = getArchiveFileSet(srcPathName);

	zip(archiveFileSet, destZipFilename, null, null, archiveMode);
    }

    public static void zip(String[] srcFilenameArray, String destZipFilename,
	    boolean archiveMode) {
	Set<File> archiveFileSet = new LinkedHashSet<File>();
	String[] arrayOfString = srcFilenameArray;
	int j = srcFilenameArray.length;
	for (int i = 0; i < j; i++) {
	    String srcPathName = arrayOfString[i];
	    archiveFileSet.addAll(getArchiveFileSet(srcPathName));
	}
	zip(archiveFileSet, destZipFilename, null, null, archiveMode);
    }

    public static byte[] zip(ServiceFile[] files, boolean archiveMode) {
	if ((files == null) || (files.length == 0)) {
	    return null;
	}
	List<File> fileList = new ArrayList<File>();
	String tempDir = ApplicationContextUtil.getTempDirectory();
	ServiceFile[] arrayOfServiceFile = files;
	int j = files.length;
	for (int i = 0; i < j; i++) {
	    ServiceFile file = arrayOfServiceFile[i];
	    String path = tempDir + file.getFilename();
	    fileList.add(new File(path));
	}
	String destZipFilename = tempDir + DateUtil.dateFormatToString(new Date(), DateUtil.MILLIONS_DATE_FORMAT)
		+ ".zip";
	File[] fileArray = new File[fileList.size()];
	zip((File[]) fileList.toArray(fileArray), destZipFilename, archiveMode);
	return FileUtil.readBytesFromFile(destZipFilename);
    }
    
    /**
     * 解压缩文件成ServiceFile
     * @param data	字节数组
     * @param filenames 需要压缩的文件名
     * @return		ServiceFile的集合
     * @throws IOException
     */
    public static List<ServiceFile> unZip2ServiceFile(byte[] data,
	    String... filenames) throws IOException {
	String tempDir = ApplicationContextUtil.getTempDirectory();
	String zipFilename = tempDir + DateUtil.dateFormatToString(new Date(),DateUtil.MILLIONS_DATE_FORMAT)
		+ ".zip";
	return unZip2ServiceFile(zipFilename, filenames);
    }
}
