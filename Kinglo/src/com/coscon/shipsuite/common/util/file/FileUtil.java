package com.coscon.shipsuite.common.util.file;

import info.monitorenter.cpdetector.io.ASCIIDetector;
import info.monitorenter.cpdetector.io.CodepageDetectorProxy;
import info.monitorenter.cpdetector.io.JChardetFacade;
import info.monitorenter.cpdetector.io.ParsingDetector;
import info.monitorenter.cpdetector.io.UnicodeDetector;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.RandomAccessFile;
import java.io.Reader;
import java.io.StreamTokenizer;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.math.BigDecimal;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Enumeration;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import org.apache.commons.io.IOUtils;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;
import org.springframework.util.StringUtils;

import com.coscon.shipsuite.common.exception.ShipSuiteRuntimeException;
import com.coscon.shipsuite.common.util.generic.Queue;
import com.coscon.shipsuite.common.util.string.StringUtil;
import com.coscon.shipsuite.common.util.string.SystemUtil;

public class FileUtil {

    /**
     * 文件大小单位：1KB
     */
    public static final long ONE_KB = 1024;

    /**
     * 文件大小单位：1MB
     */
    public static final long ONE_MB = ONE_KB * ONE_KB;

    /**
     * 文件大小单位：1GB
     */
    public static final long ONE_GB = ONE_KB * ONE_MB;

    /**
     * 缓存文件大小
     */
    private static final int BUFFER_SIZE = 4096;

    /**
     * 行分割符
     */
    public static final String LINE_SEPARATOR = System
	    .getProperty("line.separator");

    /**
     * 文件分割符
     */
    public static final String FILE_SEPARATOR = System
	    .getProperty("file.separator");

    private static PathMatcher pathMatcher = new AntPathMatcher();
    
    /************************************** 压缩解压 ****************************/
    public static void init(){
	
    }
    
    /**
     * 压缩文件或目录
     * @param baseDirName	压缩的根目录
     * @param fileName		根目录下待压缩的文件或文件夹名, 星号*表示压缩根目录下的全部文件
     * @param targetFileName	压缩后的文件名
     */
    public static void zipFile(String baseDirName, String fileName,
	    String targetFileName) {
	// 检测根目录是否存在
	if (baseDirName == null) {
	    return;
	}
	File baseDir = new File(baseDirName);
	if (!baseDir.exists() || (!baseDir.isDirectory())) {
	    return;
	}
	String baseDirPath = baseDir.getAbsolutePath();
	// 目标文件
	File targetFile = new File(targetFileName);
	try {
	    // 创建一个zip输出流来压缩数据并写入到zip文件
	    ZipOutputStream out = new ZipOutputStream(new FileOutputStream(
		    targetFile));
	    if (fileName.equals("*")) {
		// 将baseDir目录下的所有文件压缩到ZIP
		dirToZip(baseDirPath, baseDir, out);
	    } else {
		File file = new File(baseDir, fileName);
		if (file.isFile()) {
		    fileToZip(baseDirPath, file, out);
		} else {
		    dirToZip(baseDirPath, file, out);
		}
	    }
	    out.close();
	} catch (IOException e) {
	    e.printStackTrace();
	}
    }
    
    /**
     * 解压缩ZIP文件
     * @param zipFileName	待解压缩的ZIP文件名
     * @param targetBaseDirName 目标目录
     */
    public static void upzipFile(String zipFileName, String targetBaseDirName) {
	if (!targetBaseDirName.endsWith(File.separator)) {
	    targetBaseDirName += File.separator;
	}
	try {
	    // 根据ZIP文件创建ZipFile对象
	    ZipFile zipFile = new ZipFile(zipFileName);
	    ZipEntry entry = null;
	    String entryName = null;
	    String targetFileName = null;
	    byte[] buffer = new byte[4096];
	    int bytes_read;
	    // 获取ZIP文件里所有的entry
	    Enumeration entrys = zipFile.entries();
	    // 遍历所有entry
	    while (entrys.hasMoreElements()) {
		entry = (ZipEntry) entrys.nextElement();
		// 获得entry的名字
		entryName = entry.getName();
		targetFileName = targetBaseDirName + entryName;
		if (entry.isDirectory()) {
		    // 如果entry是一个目录，则创建目录
		    new File(targetFileName).mkdirs();
		    continue;
		} else {
		    // 如果entry是一个文件，则创建父目录
		    new File(targetFileName).getParentFile().mkdirs();
		}

		// 否则创建文件
		File targetFile = new File(targetFileName);
		// 打开文件输出流
		FileOutputStream os = new FileOutputStream(targetFile);
		// 从ZipFile对象中打开entry的输入流
		InputStream is = zipFile.getInputStream(entry);
		while ((bytes_read = is.read(buffer)) != -1) {
		    os.write(buffer, 0, bytes_read);
		}
		// 关闭流
		os.close();
		is.close();
	    }
	} catch (IOException err) {
	    System.err.println("解压缩文件失败: " + err);
	    err.printStackTrace();
	}
    }
    
    /**
     * 将目录压缩到ZIP输出流
     * @param baseDirPath
     * @param dir
     * @param out
     */
    private static void dirToZip(String baseDirPath, File dir,
	    ZipOutputStream out) {
	if (dir.isDirectory()) {
	    // 列出dir目录下所有文件
	    File[] files = dir.listFiles();
	    // 如果是空文件夹
	    if (files.length == 0) {
		ZipEntry entry = new ZipEntry(getEntryName(baseDirPath, dir));
		// 存储目录信息
		try {
		    out.putNextEntry(entry);
		    out.closeEntry();
		} catch (IOException e) {
		    e.printStackTrace();
		}
		return;
	    }
	    for (int i = 0; i < files.length; i++) {
		if (files[i].isFile()) {
		    // 如果是文件，调用fileToZip方法
		    fileToZip(baseDirPath, files[i], out);
		} else {
		    // 如果是目录，递归调用
		    dirToZip(baseDirPath, files[i], out);
		}
	    }
	}
    }

    /**
     * 将文件压缩到ZIP输出流
     * @param baseDirPath
     * @param file
     * @param out
     */
    private static void fileToZip(String baseDirPath, File file,
	    ZipOutputStream out) {
	FileInputStream in = null;
	ZipEntry entry = null;
	// 创建复制缓冲区
	byte[] buffer = new byte[4096];
	int bytes_read;
	if (file.isFile()) {
	    try {
		// 创建一个文件输入流
		in = new FileInputStream(file);
		// 做一个ZipEntry
		entry = new ZipEntry(getEntryName(baseDirPath, file));
		// 存储项信息到压缩文件
		out.putNextEntry(entry);
		// 复制字节到压缩文件
		while ((bytes_read = in.read(buffer)) != -1) {
		    out.write(buffer, 0, bytes_read);
		}
		out.closeEntry();
		in.close();
	    } catch (IOException e) {
		e.printStackTrace();
	    }
	}
    }
    
    /**
     * 获取待压缩文件在ZIP文件中entry的名字
     * 即相对于跟目录的相对路径名
     * @param baseDirPath
     * @param file
     * @return
     */
    private static String getEntryName(String baseDirPath, File file) {
	if (!baseDirPath.endsWith(File.separator)) {
	    baseDirPath += File.separator;
	}
	String filePath = file.getAbsolutePath();
	// 对于目录，必须在entry名字后面加上"/"，表示它将以目录项存储。
	if (file.isDirectory()) {
	    filePath += "/";
	}
	int index = filePath.indexOf(baseDirPath);
	return filePath.substring(index + baseDirPath.length());
    }

    /**
     * 获取压缩文件目录和文件名列表
     * @param file
     * @throws Exception
     */
    public static void readZipFile(String file) throws Exception {
	ZipFile zf = new ZipFile(file);
	InputStream in = new BufferedInputStream(new FileInputStream(file));
	ZipInputStream zin = new ZipInputStream(in);
	ZipEntry ze;
	while ((ze = zin.getNextEntry()) != null) {
	    if (ze.isDirectory()) {
	    } else {
		System.err.println("file - " + ze.getName() + " : "
			+ ze.getSize() + " bytes");
		long size = ze.getSize();
		if (size > 0) {
		    BufferedReader br = new BufferedReader(
			    new InputStreamReader(zf.getInputStream(ze)));
		    String line;
		    while ((line = br.readLine()) != null) {
			System.out.println(line);
		    }
		    br.close();
		}
		System.out.println();
	    }
	}
	zin.closeEntry();
    }
    
    

    /************************************** 复制文件 ****************************/

    /**
     * 复制单个文件 如果文件存在 则不覆盖
     */
    public static boolean copyFile(String srcFileName, String destFileName) {
	return copyFile(srcFileName, destFileName, false);
    }

    /**
     * 复制单个文件
     * 
     * @param srcFileName
     *            待复制的文件名
     * @param destFileName
     *            目标文件名
     * @param overlay
     *            如果目标文件存在，是否覆盖
     * @return
     */
    public static boolean copyFile(String srcFileName, String destFileName,
	    boolean overlay) {
	// 判断原文件是否存在
	File srcFile = new File(srcFileName);
	if (!srcFile.exists()) {
	    return false;
	} else if (!srcFile.isFile()) {
	    return false;
	}
	File destFile = new File(destFileName);
	if (destFile.exists()) {
	    if (overlay) {
		if (!delete(destFileName)) {
		    return false;
		}
	    } else {
		return false;
	    }
	} else {
	    if (!destFile.getParentFile().exists()) {
		if (!destFile.getParentFile().mkdirs()) {
		    return false;
		}
	    }
	}
	// 准备复制文件
	int byteread = 0; // 读取的位数
	InputStream in = null;
	OutputStream out = null;
	try {
	    in = new FileInputStream(srcFile);
	    out = new FileOutputStream(destFile);
	    byte[] buffer = new byte[1024];
	    while ((byteread = in.read(buffer)) != -1) {
		out.write(buffer, 0, byteread);
	    }
	    return true;
	} catch (Exception e) {
	    return false;
	} finally {
	    if (out != null) {
		try {
		    out.close();
		} catch (IOException e) {
		    e.printStackTrace();
		}
	    }
	    if (in != null) {
		try {
		    in.close();
		} catch (IOException e) {
		    e.printStackTrace();
		}
	    }
	}
    }

    /**
     * 复制整个目录的内容 如果目标目录存在 不覆盖
     * 
     * @param srcDirName
     *            待复制的目录名
     * @param destDirName
     *            目标目录名
     * @return
     */
    public static boolean copyDirectory(String srcDirName, String destDirName) {
	return copyDirectory(srcDirName, destDirName, false);
    }

    /**
     * 复制整个目录的内容
     * 
     * @param srcDirName
     *            待复制的目录名
     * @param destDirName
     *            目标目录名
     * @param overlay
     *            如果目标目录存在,是否覆盖
     * @return
     */
    public static boolean copyDirectory(String srcDirName, String destDirName,
	    boolean overlay) {
	// 判断原目录是否存在
	File srcDir = new File(srcDirName);
	if (!srcDir.exists()) {
	    return false;
	} else if (!srcDir.isDirectory()) {
	    return false;
	}
	// 如果目标文件夹名不以文件分隔符结尾，自动添加文件分隔符
	if (!destDirName.endsWith(File.separator)) {
	    destDirName = destDirName + File.separator;
	}
	File destDir = new File(destDirName);
	if (destDir.exists()) {
	    if (overlay) {
		if (!delete(destDirName)) {
		}
	    } else {
		return false;
	    }
	} else {
	    if (!destDir.mkdirs()) {
		return false;
	    }
	}
	boolean flag = true;
	// 列出源文件夹下所有文件（包括子目录）的文件名
	File[] files = srcDir.listFiles();
	for (int i = 0; i < files.length; i++) {
	    if (files[i].isFile()) {
		flag = copyFile(files[i].getAbsolutePath(), destDirName
			+ files[i].getName());
		if (!flag) {
		    break;
		}
	    }
	    if (files[i].isDirectory()) {
		flag = copyDirectory(files[i].getAbsolutePath(), destDirName
			+ files[i].getName());
		if (!flag) {
		    break;
		}
	    }
	}
	if (!flag) {
	    return false;
	}
	return true;
    }

    /************************************** 将内容追加到文件尾部 ****************************/

    /**
     * 使用RandomAccessFile追加文件内容
     */
    public static void appendMethodByRandomAccessFile(String fileName,
	    String content) {
	try {
	    // 打开一个随机访问文件流，按读写方式
	    RandomAccessFile randomFile = new RandomAccessFile(fileName, "rw");
	    // 文件长度，字节数
	    long fileLength = randomFile.length();
	    // 将写文件指针移到文件尾。
	    randomFile.seek(fileLength);
	    randomFile.writeBytes(content);
	    randomFile.close();
	} catch (IOException e) {
	    e.printStackTrace();
	}
    }

    /**
     * 使用FileWriter追加内容(效率高)
     * 
     * @param fileName
     * @param content
     */
    public static void appendMethodByFileWriter(String fileName, String content) {
	try {
	    // 打开一个写文件器，构造函数中的第二个参数true表示以追加形式写文件
	    FileWriter writer = new FileWriter(fileName, true);
	    writer.write(content);
	    writer.close();
	} catch (IOException e) {
	    e.printStackTrace();
	}
    }

    /**
     * 在新行上追加内容
     * 
     * @param fileName
     * @param content
     */
    public static void appendToNewLine(String fileName, String content) {
	try {
	    String line_separator = "";
	    if (SystemUtil.IS_OS_WINDOWS) {
		line_separator = "\r\n";
	    } else if (SystemUtil.IS_OS_MAC) {
		line_separator = "\n";
	    } else {
		line_separator = "\r";
	    }
	    FileWriter writer = new FileWriter(fileName, true);
	    writer.write(line_separator);
	    writer.write(content);
	    writer.close();
	} catch (IOException e) {
	    e.printStackTrace();
	}
    }

    /************************************** 创建新文件和目录 ****************************/

    /**
     * 创建单个文件
     */
    public static boolean createFile(String destFileName) {
	File file = new File(destFileName);
	if (file.exists()) {
	    return false;
	}
	if (destFileName.endsWith(File.separator)) {
	    return false;
	}
	// 判断目标文件所在的目录是否存在
	if (!file.getParentFile().exists()) {
	    if (!file.getParentFile().mkdirs()) {
		return false;
	    }
	}
	// 创建目标文件
	try {
	    if (file.createNewFile()) {
		return true;
	    } else {
		return false;
	    }
	} catch (IOException e) {
	    e.printStackTrace();
	    return false;
	}
    }

    /**
     * 创建目录
     * 
     * @param destDirName
     *            目标目录名
     * @return
     */
    public static boolean createDir(String destDirName) {
	File dir = new File(destDirName);
	if (dir.exists()) {
	    return false;
	}
	if (!destDirName.endsWith(File.separator)) {
	    destDirName = destDirName + File.separator;
	}
	// 创建目标目录
	if (dir.mkdirs()) {
	    return true;
	} else {
	    return false;
	}
    }

    /**
     * 创建临时文件
     * 
     * @param prefix
     *            临时文件名的前缀
     * @param suffix
     *            临时文件名的后缀
     * @param dirName
     *            临时文件所在的目录，如果输入null，则在用户的文档目录下创建临时文件
     * @return
     */
    public static String createTempFile(String prefix, String suffix,
	    String dirName) {
	File tempFile = null;
	if (dirName == null) {
	    try {
		// 在默认文件夹下创建临时文件
		tempFile = File.createTempFile(prefix, suffix);
		// 返回临时文件的路径
		return tempFile.getCanonicalPath();
	    } catch (IOException e) {
		e.printStackTrace();
		return null;
	    }
	} else {
	    File dir = new File(dirName);
	    // 如果临时文件所在目录不存在，首先创建
	    if (!dir.exists()) {
		if (createDir(dirName)) {
		    return null;
		}
	    }
	    try {
		// 在指定目录下创建临时文件
		tempFile = File.createTempFile(prefix, suffix, dir);
		return tempFile.getCanonicalPath();
	    } catch (IOException e) {
		e.printStackTrace();
		return null;
	    }
	}
    }

    /************************************** 删除文件 ****************************/

    /**
     * 删除文件 可以是单个文件或文件夹
     */
    public static boolean delete(String fileName) {
	File file = new File(fileName);
	if (!file.exists()) {
	    return false;
	} else {
	    if (file.isFile()) {
		return deleteFile(fileName);
	    } else {
		return deleteDirectory(fileName);
	    }
	}
    }

    /**
     * 删除单个文件
     * 
     * @param fileName
     * @return
     */
    public static boolean deleteFile(String fileName) {
	File file = new File(fileName);
	// 如果文件路径对应的文件存在，并且是一个文件，则直接删除。
	if (file.exists() && file.isFile()) {
	    if (file.delete()) {
		return true;
	    } else {
		return false;
	    }
	} else {
	    return false;
	}
    }

    /**
     * 删除目录（文件夹）以及目录下的文件
     * 
     * @param dir
     *            被删除目录的文件路径
     * @return
     */
    public static boolean deleteDirectory(String dir) {
	// 如果dir不以文件分隔符结尾，自动添加文件分隔符。
	if (!dir.endsWith(File.separator)) {
	    dir = dir + File.separator;
	}
	File dirFile = new File(dir);
	if (!dirFile.exists() || (!dirFile.isDirectory())) {
	    return false;
	}
	boolean flag = true;
	// 删除文件夹下所有文件（包括子目录）
	File[] files = dirFile.listFiles();
	for (int i = 0; i < files.length; i++) {
	    // 删除子文件
	    if (files[i].isFile()) {
		flag = deleteFile(files[i].getAbsolutePath());
		if (!flag) {
		    break;
		}
	    }
	    // 删除子目录
	    else if (files[i].isDirectory()) {
		flag = deleteDirectory(files[i].getAbsolutePath());
		if (!flag) {
		    break;
		}
	    }
	}
	if (!flag) {
	    return false;
	}
	// 删除当前目录
	if (dirFile.delete()) {
	    return true;
	} else {
	    return false;
	}
    }

    /****************** 文件分割合并器 ;将大文件分割成若干小文件;将多个小文件合并到一个大文件 **************/

    /**
     * 分割文件
     * 
     * @param fileName
     *            待分割的文件名
     * @param size
     *            小文件的大小，单位字节
     * @param filePrefix
     *            小文件的后缀名
     * @return 分割成的小文件的文件名
     */
    public static String[] divide(String fileName, long size, String filePrefix)
	    throws Exception {

	File inFile = new File(fileName);
	if (!inFile.exists() || (!inFile.isFile())) {
	    throw new Exception("指定文件不存在！");
	}
	// 获得被分割文件父文件，将来被分割成的小文件便存在这个目录下
	File parentFile = inFile.getParentFile();

	// 取得文件的大小
	long fileLength = inFile.length();
	if (size <= 0) {
	    size = fileLength / 2;
	}
	// 取得被分割后的小文件的数目
	int num = (fileLength % size != 0) ? (int) (fileLength / size + 1)
		: (int) (fileLength / size);
	// 存放被分割后的小文件名
	String[] outFileNames = new String[num];
	// 输入文件流，即被分割的文件
	FileInputStream in = new FileInputStream(inFile);

	// 读输入文件流的开始和结束下标
	long inEndIndex = 0;
	int inBeginIndex = 0;

	// 根据要分割的数目输出文件
	for (int outFileIndex = 0; outFileIndex < num; outFileIndex++) {
	    // 对于前num - 1个小文件，大小都为指定的size
	    File outFile = new File(parentFile, inFile.getName() + outFileIndex
		    + "." + filePrefix);
	    // 构建小文件的输出流
	    FileOutputStream out = new FileOutputStream(outFile);
	    // 将结束下标后移size
	    inEndIndex += size;
	    inEndIndex = (inEndIndex > fileLength) ? fileLength : inEndIndex;
	    // 从输入流中读取字节存储到输出流中
	    for (; inBeginIndex < inEndIndex; inBeginIndex++) {
		out.write(in.read());
	    }
	    out.close();
	    outFileNames[outFileIndex] = outFile.getAbsolutePath();
	}
	in.close();
	return outFileNames;
    }

    /**
     * 合并文件
     * 
     * @param fileNames
     *            待合并的文件名，是一个数组
     * @param TargetFileName
     *            目标文件名
     * @return 目标文件的全路径
     * @throws Exception
     */
    public static String unite(String[] fileNames, String TargetFileName)
	    throws Exception {
	File inFile = null;
	// 构建文件输出流
	File outFile = new File(TargetFileName);
	FileOutputStream out = new FileOutputStream(outFile);

	for (int i = 0; i < fileNames.length; i++) {
	    // 打开文件输入流
	    inFile = new File(fileNames[i]);
	    FileInputStream in = new FileInputStream(inFile);
	    // 从输入流中读取数据，并写入到文件数出流中
	    int c;
	    while ((c = in.read()) != -1) {
		out.write(c);
	    }
	    in.close();
	}
	out.close();

	return outFile.getAbsolutePath();
    }

    /******************************** 文件查找器 *************************/

    /**
     * 查找文件
     * 
     * @param baseDirName
     *            待查找的目录
     * @param targetFileName
     *            目标文件名，支持通配符形式
     * @param count
     *            满足查询条件的文件名列表
     * @return 满足查询条件的文件名列表
     * 
     *         算法简述： 从某个给定的需查找的文件夹出发，搜索该文件夹的所有子文件夹及文件
     *         若为文件，则进行匹配，匹配成功则加入结果集，若为子文件夹，则进队列 队列不空，重复上述操作，队列为空，程序结束，返回结果
     */
    public static List<File> findFiles(String baseDirName,
	    String targetFileName, int count) {
	List<File> fileList = new ArrayList<File>();
	// 判断目录是否存在
	File baseDir = new File(baseDirName);
	if (!baseDir.exists() || !baseDir.isDirectory()) {
	    return fileList;
	}
	String tempName = null;
	// 创建一个队列，Queue在第四章有定义
	Queue queue = new Queue(); // 实例化队列
	queue.add(baseDir); // 入队
	File tempFile = null;
	while (!queue.isEmpty()) {
	    // 从队列中取目录
	    tempFile = (File) queue.pop();
	    if (tempFile.exists() && tempFile.isDirectory()) {
		File[] files = tempFile.listFiles();
		for (int i = 0; i < files.length; i++) {
		    // 如果是目录则放进队列
		    if (files[i].isDirectory()) {
			queue.add(files[i]);
		    } else {
			// 如果是文件则根据文件名与目标文件名进行匹配
			tempName = files[i].getName();
			if (wildcardMatch(targetFileName, tempName)) {
			    // 匹配成功，将文件名添加到结果集
			    fileList.add(files[i].getAbsoluteFile());
			    // 如果已经达到指定的数目，则退出循环
			    if ((count != 0) && (fileList.size() >= count)) {
				return fileList;
			    }
			}
		    }
		}
	    }
	}

	return fileList;
    }

    /**
     * 通配符匹配
     * 
     * @param pattern
     *            通配符模式
     * @param str
     *            待匹配的字符串
     * @return 匹配成功则返回true，否则返回false
     */
    private static boolean wildcardMatch(String pattern, String str) {
	int patternLength = pattern.length();
	int strLength = str.length();
	int strIndex = 0;
	char ch;
	for (int patternIndex = 0; patternIndex < patternLength; patternIndex++) {
	    ch = pattern.charAt(patternIndex);
	    if (ch == '*') {
		// 通配符星号*表示可以匹配任意多个字符
		while (strIndex < strLength) {
		    if (wildcardMatch(pattern.substring(patternIndex + 1),
			    str.substring(strIndex))) {
			return true;
		    }
		    strIndex++;
		}
	    } else if (ch == '?') {
		// 通配符问号?表示匹配任意一个字符
		strIndex++;
		if (strIndex > strLength) {
		    // 表示str中已经没有字符匹配?了。
		    return false;
		}
	    } else {
		if ((strIndex >= strLength) || (ch != str.charAt(strIndex))) {
		    return false;
		}
		strIndex++;
	    }
	}
	return (strIndex == strLength);
    }

    /************************************* 得到文件的详细信息 *******************************/

    /**
     * 得到文件的详细信息
     * 
     * @param file
     *            单个文件
     */
    public static void getFileInfos(File file) {
	// 获取文件的名字，不包括路径
	System.out.println("文件名:\t" + file.getName());
	// 将抽象路径名中的文件分隔符用系统默认分隔符替换
	System.out.println("文件路径:\t" + file.getPath());
	// 获取文件的绝对路径
	System.out.println("绝对路径:\t" + file.getAbsolutePath());
	// 获取抽象路径名的父抽象路径
	System.out.println("父目录:\t" + file.getParent());
	System.out.println("文件是否存在:\t" + file.exists());
	System.out.println("是否可读:\t" + file.canRead());
	System.out.println("是否可写:\t" + file.canWrite());
	System.out.println("是否是隐藏文件:\t" + file.isHidden());
	System.out.println("是否是普通文件:\t" + file.isFile());
	System.out.println("是否是文件目录:\t" + file.isDirectory());
	System.out.println("文件路径是否是绝对路径:\t" + file.isAbsolute());
	System.out.println("文件路径的URI:\t" + file.toURI());
	System.out.println("文件最后修改时间:\t" + new Date(file.lastModified()));
	System.out.println("文件大小:\t" + file.length() + " bytes");
    }

    /**************************************** 移动文件或目录 **************/

    /**
     * 移动单个文件 不覆盖已存在的目标文件
     * 
     * @param srcFileName
     *            待移动的原文件名
     * @param destFileName
     *            目标文件名
     * @return 文件移动成功返回true，否则返回false
     */
    public static boolean moveFile(String srcFileName, String destFileName) {
	return moveFile(srcFileName, destFileName, false);
    }

    /**
     * 移动单个文件
     * 
     * @param srcFileName
     *            待移动的原文件名
     * @param destFileName
     *            目标文件名
     * @param overlay
     *            如果目标文件存在，是否覆盖
     * @return 文件移动成功返回true，否则返回false
     */
    public static boolean moveFile(String srcFileName, String destFileName,
	    boolean overlay) {
	// 判断原文件是否存在
	File srcFile = new File(srcFileName);
	if (!srcFile.exists()) {
	    return false;
	} else if (!srcFile.isFile()) {
	    return false;
	}
	File destFile = new File(destFileName);
	// 如果目标文件存在
	if (destFile.exists()) {
	    // 如果允许文件覆盖
	    if (overlay) {
		// 删除已存在的目标文件，无论目标文件是目录还是单个文件
		if (!delete(destFileName)) {
		    return false;
		}
	    } else {
		return false;
	    }
	} else {
	    if (!destFile.getParentFile().exists()) {
		// 如果目标文件所在的目录不存在，则创建目录
		if (!destFile.getParentFile().mkdirs()) {
		    return false;
		}
	    }
	}
	// 移动原文件至目标文件
	if (srcFile.renameTo(destFile)) {
	    return true;
	} else {
	    return true;
	}
    }

    /**
     * 移动目录，不覆盖已存在的目标目录
     * 
     * @param srcDirName
     *            待移动的原目录名
     * @param destDirName
     *            目标目录名
     * @return
     */
    public static boolean moveDirectory(String srcDirName, String destDirName) {
	return moveDirectory(srcDirName, destDirName, false);
    }

    /**
     * 移动目录
     * 
     * @param srcDirName
     *            待移动的原目录名
     * @param destDirName
     *            目标目录名
     * @param overlay
     *            如果目标目论存在，是否覆盖
     * @return 目录移动成功返回true，否则返回false
     */
    public static boolean moveDirectory(String srcDirName, String destDirName,
	    boolean overlay) {
	// 判断原目录是否存在
	File srcDir = new File(srcDirName);
	if (!srcDir.exists()) {
	    return false;
	} else if (!srcDir.isDirectory()) {
	    return false;
	}
	// 如果目标文件夹名不以文件分隔符结尾，自动添加文件分隔符
	if (!destDirName.endsWith(File.separator)) {
	    destDirName = destDirName + File.separator;
	}
	File destDir = new File(destDirName);
	// 如果目标文件夹存在，
	if (destDir.exists()) {
	    if (overlay) {
		// 允许覆盖则删除已存在的目标目录
		if (!delete(destDirName)) {
		    System.out.println("移动目录失败：删除目标目录" + destDirName + "失败！");
		}
	    } else {
		return false;
	    }
	} else {
	    // 创建目标目录
	    if (!destDir.mkdirs()) {
		return false;
	    }
	}
	boolean flag = true;
	// 移动原目录下的文件和子目录到目标目录下
	File[] files = srcDir.listFiles();
	for (int i = 0; i < files.length; i++) {
	    // 移动子文件
	    if (files[i].isFile()) {
		flag = moveFile(files[i].getAbsolutePath(), destDirName
			+ files[i].getName(), overlay);
		if (!flag) {
		    break;
		}
	    }
	    // 移动子目录
	    else if (files[i].isDirectory()) {
		flag = moveDirectory(files[i].getAbsolutePath(), destDirName
			+ files[i].getName(), overlay);
		if (!flag) {
		    break;
		}
	    }
	}
	if (!flag) {
	    return false;
	}
	// 删除原目录
	if (deleteDirectory(srcDirName)) {
	    return true;
	} else {
	    return false;
	}
    }

    /********************************** 统计文件中的字符数 **************************/

    /**
     * 统计文件中的字符数
     * 
     * @param fileName
     *            文件名
     * @return
     */
    public static long statis(String fileName) {

	FileReader fileReader = null;
	try {
	    fileReader = new FileReader(fileName);
	    // 创建分析给定字符流的标记生成器
	    StreamTokenizer st = new StreamTokenizer(new BufferedReader(
		    fileReader));

	    // ordinaryChar方法指定字符参数在此标记生成器中是“普通”字符。
	    // 下面指定单引号、双引号和注释符号是普通字符
	    st.ordinaryChar('\'');
	    st.ordinaryChar('\"');
	    st.ordinaryChar('/');

	    String s;
	    int numberSum = 0;
	    int wordSum = 0;
	    int symbolSum = 0;
	    int total = 0;
	    // nextToken方法读取下一个Token.
	    // TT_EOF指示已读到流末尾的常量。
	    while (st.nextToken() != StreamTokenizer.TT_EOF) {
		// 在调用 nextToken 方法之后，ttype字段将包含刚读取的标记的类型
		switch (st.ttype) {
		// TT_EOL指示已读到行末尾的常量。
		case StreamTokenizer.TT_EOL:
		    break;
		// TT_NUMBER指示已读到一个数字标记的常量
		case StreamTokenizer.TT_NUMBER:
		    // 如果当前标记是一个数字，nval字段将包含该数字的值
		    s = String.valueOf((st.nval));
		    System.out.println(s);
		    numberSum += s.length();
		    break;
		// TT_WORD指示已读到一个文字标记的常量
		case StreamTokenizer.TT_WORD:
		    // 如果当前标记是一个文字标记，sval字段包含一个给出该文字标记的字符的字符串
		    s = st.sval;
		    wordSum += s.length();
		    break;
		default:
		    // 如果以上3中类型都不是，则为英文的标点符号
		    s = String.valueOf((char) st.ttype);
		    symbolSum += s.length();
		}
	    }
	    System.out.println("sum of number = " + numberSum);
	    System.out.println("sum of word = " + wordSum);
	    System.out.println("sum of symbol = " + symbolSum);
	    total = symbolSum + numberSum + wordSum;
	    System.out.println("Total = " + total);
	    return total;
	} catch (Exception e) {
	    e.printStackTrace();
	    return -1;
	} finally {
	    if (fileReader != null) {
		try {
		    fileReader.close();
		} catch (IOException e1) {
		}
	    }
	}
    }

    /*********************************** 读取文件 ********************/

    /**
     * 文件名转换为InputStream
     * 
     * @param filename
     * @return
     */
    public static InputStream getInputStream(String filename) {
	InputStream fis = null;
	try {
	    fis = new BufferedInputStream(new FileInputStream(
		    new File(filename)));
	} catch (FileNotFoundException ex) {
	    try {
		fis = new FileInputStream(new File(filename));
	    } catch (FileNotFoundException ex1) {
		fis = Thread.currentThread().getContextClassLoader()
			.getResourceAsStream(filename);
		if (fis == null) {
		    throw new ShipSuiteRuntimeException(
			    new FileNotFoundException(filename));
		}
	    }
	}
	return fis;
    }

    /**
     * InputStream转换为字节数组
     * 
     * @param inputStream
     * @return
     * @throws IOException
     */
    public static byte[] readAsByteArray(InputStream inputStream)
	    throws IOException {
	return org.aspectj.util.FileUtil.readAsByteArray(inputStream);
    }

    /**
     * InputStream转换为String
     * 
     * @param inputStream
     *            输入流
     * @param encode
     *            字符编码
     * @return
     * @throws UnsupportedEncodingException
     * @throws IOException
     */
    public static String readAsString(InputStream inputStream, String encode)
	    throws UnsupportedEncodingException, IOException {
	return new String(readAsByteArray(inputStream), encode);
    }

    /**
     * File转换为字节数组
     * 
     * @param file
     *            单个文件
     * @return
     */
    public static byte[] readBytesFromFile(File file) {
	InputStream fis = null;
	try {
	    fis = new BufferedInputStream(new FileInputStream(file));
	    return IOUtils.toByteArray(fis);
	} catch (IOException e) {
	    throw new ShipSuiteRuntimeException(e);
	} finally {
	    if (fis != null) {
		try {
		    fis.close();
		} catch (IOException localIOException2) {
		}
	    }
	}
    }

    /**
     * 文件名转换为字节数组
     * 
     * @param fileName
     *            文件名
     * @return
     */
    public static byte[] readBytesFromFile(String fileName) {
	File file = new File(fileName);
	return readBytesFromFile(file);
    }

    /**
     * 读取文件得到字符串
     * 
     * @param filePath
     *            文件路径
     * @return
     */
    public static String readFile(String filePath) {
	return readFile(filePath, null);
    }

    /**
     * 读取文件
     * 
     * @param filePath
     *            文件路径
     * @param charsetName
     *            字符编码
     * @return
     */
    public static String readFile(String filePath, String charsetName) {
	InputStreamReader inputStreamReader = null;
	BufferedReader bufferReader = null;
	try {
	    if (charsetName != null) {
		inputStreamReader = new InputStreamReader(
			getInputStream(filePath), charsetName);
	    } else {
		inputStreamReader = new InputStreamReader(
			getInputStream(filePath));
	    }
	    bufferReader = new BufferedReader(inputStreamReader);

	    return IOUtils.toString(bufferReader);
	} catch (Exception ex) {
	    throw new ShipSuiteRuntimeException(ex);
	} finally {
	    if (bufferReader != null) {
		try {
		    bufferReader.close();
		    bufferReader = null;
		} catch (IOException localIOException2) {
		}
	    }
	    if (inputStreamReader != null) {
		try {
		    inputStreamReader.close();
		    inputStreamReader = null;
		} catch (IOException localIOException3) {
		}
	    }
	}
    }

    /**
     * 按行读取文件
     * 
     * @param filePath
     *            文件路径
     * @return
     */
    public static List<String> readFileLines(String filePath) {
	return readFileLines(filePath, null);
    }

    /**
     * 按行读取文件
     * 
     * @param filePath
     *            文件路径
     * @param charsetName
     *            字符编码
     * @return
     */
    public static List<String> readFileLines(String filePath, String charsetName) {
	List<String> lineStringList = new ArrayList<String>();
	InputStreamReader inputStreamReader = null;
	BufferedReader bufferReader = null;
	try {
	    if (charsetName != null) {
		inputStreamReader = new InputStreamReader(
			getInputStream(filePath), charsetName);
	    } else {
		inputStreamReader = new InputStreamReader(
			getInputStream(filePath));
	    }
	    bufferReader = new BufferedReader(inputStreamReader);
	    String line;
	    while ((line = bufferReader.readLine()) != null) {
		lineStringList.add(line);
	    }
	    return lineStringList;
	} catch (Exception ex) {
	    throw new ShipSuiteRuntimeException(ex);
	} finally {
	    if (bufferReader != null) {
		try {
		    bufferReader.close();
		    bufferReader = null;
		} catch (IOException localIOException2) {
		}
	    }
	    if (inputStreamReader != null) {
		try {
		    inputStreamReader.close();
		    inputStreamReader = null;
		} catch (IOException localIOException3) {
		}
	    }
	}
    }

    /*********************************** 写文件 ********************/
    public static boolean writeBytesToFile(byte[] bytes, String fileName) {
	return writeBytesToFile(bytes, fileName, false);
    }

    public static boolean writeBytesToFile(byte[] bytes, String fileName,
	    boolean append) {
	boolean rtn = false;
	OutputStream fos = null;
	try {
	    correctPath(fileName);
	    fos = new BufferedOutputStream(new FileOutputStream(fileName));
	    IOUtils.write(bytes, fos);
	    rtn = true;
	} catch (Exception e) {
	    throw new ShipSuiteRuntimeException(e);
	} finally {
	    try {
		if (fos != null) {
		    fos.close();
		}
	    } catch (IOException localIOException) {
	    }
	}
	return rtn;
    }

    public static void writeFile(InputStream inputStream, String filePath) {
	writeFile(inputStream, filePath, false);
    }

    public static void writeFile(InputStream inputStream, String filePath,
	    boolean append) {
	if (StringUtil.isNullOrEmpty(filePath)) {
	    return;
	}
	OutputStream fileOutputStream = null;
	String path = getPathOfFile(filePath);
	File f = new File(path);
	if (!f.exists()) {
	    f.mkdir();
	}
	try {
	    correctPath(filePath);
	    fileOutputStream = new BufferedOutputStream(new FileOutputStream(
		    filePath, append));
	    int bytesRead = 0;
	    byte[] buffer = new byte[1024];
	    while ((bytesRead = inputStream.read(buffer)) != -1) {
		fileOutputStream.write(buffer, 0, bytesRead);
	    }
	} catch (IOException e) {
	    throw new ShipSuiteRuntimeException(e);
	} finally {
	    try {
		if (inputStream != null) {
		    inputStream.close();
		}
		if (fileOutputStream != null) {
		    fileOutputStream.close();
		}
	    } catch (IOException e) {
		e.printStackTrace();
	    }
	}
    }

    public static void writeFile(String content, String filepath) {
	writeFile(content, filepath, null);
    }

    public static void writeFile(String content, String filepath, boolean append) {
	writeFile(content, filepath, null, append);
    }

    public static void writeFile(String content, String filePath,
	    String charsetName) {
	writeFile(content, filePath, charsetName, false);
    }

    public static void writeFile(String content, String filePath,
	    String charsetName, boolean append) {
	if (StringUtil.isNullOrEmpty(filePath)) {
	    return;
	}
	OutputStreamWriter outputStreamWriter = null;
	BufferedWriter bufferWriter = null;

	String path = getPathOfFile(filePath);
	File f = new File(path);
	if (!f.exists()) {
	    f.mkdir();
	}
	try {
	    correctPath(filePath);
	    if (StringUtil.isNullOrEmpty(charsetName)) {
		outputStreamWriter = new OutputStreamWriter(
			new FileOutputStream(filePath, append));
	    } else {
		outputStreamWriter = new OutputStreamWriter(
			new FileOutputStream(filePath, append), charsetName);
	    }
	    bufferWriter = new BufferedWriter(outputStreamWriter);
	    bufferWriter.write(content);
	} catch (IOException e) {
	    throw new ShipSuiteRuntimeException(e);
	} finally {
	    try {
		if (bufferWriter != null) {
		    bufferWriter.close();
		}
	    } catch (IOException localIOException1) {
	    }
	    try {
		if (outputStreamWriter != null) {
		    outputStreamWriter.close();
		}
	    } catch (IOException localIOException2) {
	    }
	}
    }

    /********************************* 计算文件大小以及转换 ********************/
    /**
     * 获得文件大小(单位:字节)
     * 
     * @param file
     * @return
     * @throws FileNotFoundException
     */
    public static int getFileSize(File file) throws FileNotFoundException {
	try {
	    InputStream input = new FileInputStream(file);
	    return input.available();
	} catch (Exception e) {
	    throw new FileNotFoundException("File NOT FOUND！");
	}
    }

    /**
     * 递归的获取文件夹的每一个文件的大小,并计算总的大小
     * 
     * @param directoryPath
     * @param size
     * @return
     * @throws FileNotFoundException
     */
    private static long getDirectorySize(String directoryPath, long size)
	    throws FileNotFoundException {
	File file = new File(directoryPath);
	File[] files = file.listFiles();
	for (int i = 0; i < files.length; i++) {
	    String filePath = files[i].getAbsolutePath();
	    if (files[i].isDirectory()) {
		size = getDirectorySize(filePath, size);
	    } else {
		size += getFileSize(filePath);
	    }
	}
	return size;
    }

    /**
     * 返回一个文件夹的大小(单位:字节)
     * 
     * @param directoryPath
     * @return
     * @throws FileNotFoundException
     */
    public static long getDirectorySize(String directoryPath)
	    throws FileNotFoundException {
	return getDirectorySize(directoryPath, 0);
    }

    /**
     * 返回一个文件的大小
     * 
     * @param filePath
     * @return
     * @throws FileNotFoundException
     */
    public static long getFileSize(String filePath)
	    throws FileNotFoundException {
	File file = new File(filePath);
	if (!file.exists()) {
	    throw new FileNotFoundException("this file is not exited: "
		    + filePath);
	}
	return file.length();
    }

    /**
     * 获取文件或目录大小
     * 
     * @param path
     * @return
     * @throws FileNotFoundException
     */
    public static long getSize(String path) throws FileNotFoundException {
	File file = new File(path);
	if (file.isFile()) {
	    return getFileSize(path);
	} else {
	    return getDirectorySize(path, 0);
	}
    }

    /**
     * 字节转换为KB
     * 
     * @param bytes
     * @return
     */
    public static String bytesTokb(long bytes) {
	BigDecimal filesize = new BigDecimal(bytes);

	BigDecimal megabyte = new BigDecimal(1048576);

	float returnValue = filesize.divide(megabyte, 2, 0).floatValue();
	if (returnValue > 1.0F) {
	    return returnValue + " MB";
	}
	BigDecimal kilobyte = new BigDecimal(1024);

	returnValue = filesize.divide(kilobyte, 2, 0).floatValue();

	return returnValue + " KB";
    }

    /**
     * 字节自动转换为KB或MB
     * 
     * @param bytes
     * @return
     */
    public static String convertFileSize(long bytes) {
	String sizeInfo;
	if (bytes >= 1048576L) {
	    sizeInfo = String.format("%.2f MB",
		    new Object[] { Double.valueOf(bytes / 1024.0D / 1024.0D) });
	} else {
	    sizeInfo = String.format("%.2f KB",
		    new Object[] { Double.valueOf(bytes / 1024.0D) });
	}
	return sizeInfo;
    }

    /*********************************** 文件编码 **********************/
    public static void is() {

    }

    /**
     * 根据文件名获取文件编码
     * 
     * @param filename
     *            文件路径
     * @return
     */
    public static String getFileEncode(String filename) {
	String exdName = getFilenameExtend(filename);
	if ((StringUtil.isNullOrEmpty(exdName))
		|| (exdName.equalsIgnoreCase("txt"))
		|| (exdName.equalsIgnoreCase("csv"))
		|| (exdName.equalsIgnoreCase("log"))
		|| (exdName.equalsIgnoreCase("properties"))
		|| (exdName.equalsIgnoreCase("ini"))) {
	    return getFileEncode(filename, true);
	}
	return getFileEncode(filename, false);
    }

    /**
     * 根据文件名获取文件编码
     * 
     * @param filename
     *            文件路径
     * @param isPlainText
     *            是否是文本文件
     * @return
     */
    public static String getFileEncode(String filename, boolean isPlainText) {
	BufferedInputStream bin = null;
	try {
	    bin = new BufferedInputStream(new FileInputStream(filename));
	    int p = (bin.read() << 8) + bin.read();
	    String code = null;
	    switch (p) {
	    case 61371:
		code = "UTF-8";
		break;
	    case 65534:
		code = "Unicode";
		break;
	    case 65279:
		code = "UTF-16BE";
		break;
	    case 23669:
		code = "ANSI|ASCII";
		break;
	    default:
		bin.close();
		if (isPlainText) {
		    code = "GBK";
		} else {
		    code = getFileEncode(new URL("file:\\" + filename));
		}
		break;
	    }
	    return code;
	} catch (Exception e) {
	    throw new ShipSuiteRuntimeException(e);
	} finally {
	    try {
		bin.close();
	    } catch (Exception localException2) {
	    }
	}
    }

    public static String getFileEncode(URL url) {
	CodepageDetectorProxy detector = CodepageDetectorProxy.getInstance();

	detector.add(new ParsingDetector(false));

	detector.add(JChardetFacade.getInstance());

	detector.add(ASCIIDetector.getInstance());

	detector.add(UnicodeDetector.getInstance());
	Charset charset = null;
	try {
	    charset = detector.detectCodepage(url);
	} catch (Exception ex) {
	    ex.printStackTrace();
	}
	if (charset != null) {
	    return charset.name();
	}
	return null;
    }

    /********************************* 其它 ***************************/
    /**
     * 得到行分隔符
     * 
     * @return
     */
    public static String getLineSeparator() {
	return LINE_SEPARATOR;
    }

    /**
     * 得到文件分隔符
     * 
     * @return
     */
    public static String getFileSeparator() {
	return FILE_SEPARATOR;
    }

    /**
     * 得到用户的临时路径
     * 
     * @return
     */
    public static String getTempPath() {
	String tempDir = System.getProperty("user.dir") + File.separator
		+ "temp" + File.separator;
	File dirname = new File(tempDir);
	if (!dirname.isDirectory()) {
	    dirname.mkdir();
	}
	return tempDir;
    }

    /**
     * 得到系统用户的临时目录
     * 
     * @return
     */
    public static String getSystemTempPath() {
	return System.getProperty("java.io.tmpdir");
    }

    public static String getRelativePath(String baseDirectory, String filename) {
	int pathIndex = filename.indexOf(baseDirectory);
	if (pathIndex >= 0) {
	    String path = filename.substring(baseDirectory.length());
	    if ((path.startsWith(File.separator)) || (path.startsWith("\\"))
		    || (path.startsWith("/"))) {
		return path.substring(1);
	    }
	    return path;
	}
	return filename;
    }

    public static void correctPath(String filepath) {
	String path = getPathOfFile(filepath);
	File pathFile = new File(path);
	if (!pathFile.exists()) {
	    String parentPath = getPathOfFile(path);
	    File parentFile = new File(parentPath);
	    if (parentFile.exists()) {
		pathFile.mkdir();
	    } else {
		correctPath(path);
		pathFile.mkdir();
	    }
	}
    }

    /**
     * 递归取得某个目录下所有的文件
     * 
     * @param path
     *            目录
     * @return 所有的文件
     */
    public static List<File> getAllFile(String path) {
	File directory = new File(path);
	if (!directory.exists() || !directory.isDirectory()) {
	    throw new IllegalArgumentException("Nonexistent directory[" + path
		    + "]");
	}

	return new Recursiver().getFileList(directory);
    }

    private static class Recursiver {

	private static ArrayList<File> files = new ArrayList<File>();

	public List<File> getFileList(File file) {
	    File children[] = file.listFiles();

	    for (int i = 0; i < children.length; i++) {
		if (children[i].isDirectory()) {
		    new Recursiver().getFileList(children[i]);
		} else {
		    files.add(children[i]);
		}
	    }

	    return files;
	}
    }

    /**
     * 列出某文件夹及其子文件夹下面的文件，并可根据扩展名过滤
     * 
     * @param path
     *            目录
     * @param fileNameSuffix
     */
    public static void getFileName(File path, String fileNameSuffix) {
	if (!path.exists()) {
	    System.out.println("文件名称不存在!");
	} else {
	    if (path.isFile()) {
		if (path.getName().toLowerCase().endsWith("." + fileNameSuffix)
			|| fileNameSuffix.equals("")) { // 文件格式
		    System.out.println(path);
		}
	    } else {
		File[] files = path.listFiles();
		for (int i = 0; i < files.length; i++) {
		    getFileName(files[i], fileNameSuffix);
		}
	    }
	}
    }

    /**
     * 上传文件
     * 
     * @param file
     * @param fileName
     * @param path
     */
    public static void uploadFile(File file, String fileName, String path) {
	OutputStream output = null;
	InputStream input = null;
	try {
	    input = new FileInputStream(file);
	    File newFile = new File(path + "/" + fileName);
	    output = new FileOutputStream(newFile);
	    byte[] bytes = new byte[2048];
	    int len;
	    while ((len = input.read(bytes)) != -1) {
		output.write(bytes, 0, len);
	    }
	} catch (Exception e) {

	} finally {
	    try {
		if (null != output) {
		    output.flush();
		    output.close();
		}
		if (null != input) {
		    input.close();
		}
	    } catch (IOException e) {
		e.printStackTrace();
	    }
	}
    }

    /**
     * 是否存在此文件
     * 
     * @param filePath
     *            文件路径
     * @return
     */
    public static boolean existsFile(String filePath) {
	if (filePath == null) {
	    return false;
	} else if (filePath.trim().equals("")) {
	    return false;
	}
	File file = new File(filePath);
	if (file.exists()) {
	    return true;
	}
	return false;
    }

    /**
     * 获得所有的文件名(不递归)
     * 
     * @param directoryPath
     * @return
     */
    public static List<String> getAllFileName(String directoryPath) {
	List<String> allFiles = new ArrayList<String>();
	if (!existsFile(directoryPath)) {
	    return allFiles;
	} else {
	    File file = new File(directoryPath);
	    if (file.isDirectory()) {
		File[] files = file.listFiles();
		for (int i = 0; i < files.length; i++) {
		    allFiles.add(files[i].getName());
		}
		return allFiles;
	    } else {
		return allFiles;
	    }
	}
    }

    /**
     * 斜杠替换
     * 
     * @param sourceStr
     * @param lineType
     * @return
     */
    public static String reverse(String sourceStr, boolean lineType) {
	if (lineType) {
	    Pattern pattern = Pattern.compile("/", Pattern.DOTALL);
	    Matcher matcher = pattern.matcher(sourceStr);
	    sourceStr = matcher.replaceAll("\\\\"); // 转换后的
	} else {
	    Pattern pattern = Pattern.compile("\\\\", Pattern.DOTALL);
	    Matcher matcher = pattern.matcher(sourceStr);
	    sourceStr = matcher.replaceAll("/"); // 转换后的
	}
	return sourceStr;
    }

    /**
     * 获取文件路径
     * 
     * @param filename
     *            文件路径
     * @return
     */
    public static String getPathOfFile(String filename) {
	String path = filename.trim();
	int pathIndex = path.lastIndexOf("/");
	if (pathIndex > 0) {
	    path = path.substring(0, pathIndex);
	} else {
	    pathIndex = filename.lastIndexOf("\\");
	    if (pathIndex > 0)
		path = path.substring(0, pathIndex);
	    else
		path = ".";
	}
	return path;
    }

    /**
     * 得到主文件名
     * 
     * @param filename
     *            文件名
     * @return
     */
    public static String getFilenameMain(String filename) {
	filename = getPureFilename(filename);
	int extendIndex = filename.lastIndexOf(".");
	if (extendIndex > 0) {
	    return filename.substring(0, extendIndex);
	}
	return filename;
    }

    /**
     * 提取文件名
     * 
     * @param filename
     *            文件名或文件路径
     * @return
     */
    public static String getPureFilename(String filename) {
	if (StringUtil.isNullOrEmpty(filename))
	    return "";
	int pathIndex = filename.lastIndexOf("/");
	if (pathIndex > 0) {
	    return filename.substring(pathIndex + 1);
	}
	pathIndex = filename.lastIndexOf("\\");
	if (pathIndex > 0) {
	    return filename.substring(pathIndex + 1);
	}
	return filename;
    }

    /**
     * 得到文件扩展名
     * 
     * @param filename
     *            文件名
     * @return
     */
    public static String getFilenameExtend(String filename) {
	filename = getPureFilename(filename);
	int extendIndex = filename.lastIndexOf(".");
	if (extendIndex > 0) {
	    return filename.substring(extendIndex + 1);
	}
	return "";
    }

    /**
     * 关闭输入流
     * 
     * @param in
     *            InputStream
     */
    public static void close(InputStream in) {
	try {
	    if (in != null) {
		in.close();
	    }
	} catch (IOException e) {
	}
    }

    /**
     * 关闭输出流
     * 
     * @param out
     *            OutputStream
     */
    public static void close(OutputStream out) {
	try {
	    if (out != null) {
		out.close();
	    }
	} catch (IOException e) {
	}
    }

    /**
     * 关闭 Reader
     * 
     * @param in
     *            Reader
     */
    public static void close(Reader in) {
	try {
	    if (in != null) {
		in.close();
	    }
	} catch (IOException e) {
	}
    }

    /**
     * 关闭 Writer
     * 
     * @param out
     *            Writer
     */
    public static void close(Writer out) {
	try {
	    if (out != null) {
		out.close();
	    }
	} catch (IOException e) {
	}
    }

    /**
     * 关闭 Closeable 对象
     * 
     * @param closeable
     *            Closeable
     */
    public static void close(Closeable closeable) {
	try {
	    if (closeable != null) {
		closeable.close();
	    }
	} catch (IOException e) {
	}
    }

    public static Set<File> findMatchingFileSystemResources(File rootDir,
	    String pattern) {
	if (!rootDir.exists()) {
	    return Collections.emptySet();
	}
	if (!rootDir.isDirectory()) {
	    return Collections.emptySet();
	}
	if (!rootDir.canRead()) {
	    return Collections.emptySet();
	}
	String fullPattern = StringUtils.replace(rootDir.getAbsolutePath(),
		File.separator, "/");
	if (!pattern.startsWith("/")) {
	    fullPattern = fullPattern + "/";
	}
	fullPattern = fullPattern
		+ StringUtils.replace(pattern, File.separator, "/");
	Set<File> result = new LinkedHashSet();
	try {
	    doRetrieveMatchingFiles(fullPattern, rootDir, result);
	} catch (IOException localIOException) {
	}
	Set<File> deepResult = new LinkedHashSet<File>();
	for (File resFile : result) {
	    if (resFile.isDirectory()) {
		deepResult
			.addAll(findMatchingFileSystemResources(resFile, "*"));
	    }
	}
	result.addAll(deepResult);
	return result;
    }

    private static void doRetrieveMatchingFiles(String fullPattern, File dir,
	    Set<File> result) throws IOException {
	File[] dirContents = dir.listFiles();
	if (dirContents == null) {
	    return;
	}
	for (File content : dirContents) {
	    String currPath = StringUtils.replace(content.getAbsolutePath(),
		    File.separator, "/");
	    if ((content.isDirectory())
		    && (pathMatcher.matchStart(fullPattern, currPath + "/"))) {
		if (content.canRead()) {
		    doRetrieveMatchingFiles(fullPattern, content, result);
		}
	    } else if (content.isDirectory()) {
		result.addAll(findMatchingFileSystemResources(content,
			getPureFilename(fullPattern)));
	    }
	    if (pathMatcher.match(fullPattern, currPath)) {
		result.add(content);
	    }
	}
    }

    public static Set<File> findMatchingFileSystemResources(String rootDir,
	    String pattern) {
	return findMatchingFileSystemResources(new File(rootDir), pattern);
    }

    public static void main(String[] args) {
	System.out.println(getFilenameExtend("a.txt"));
	System.out.println(getFilenameMain("a.txt"));
	System.out.println(getPureFilename("c:\\a.txt"));
	System.out.println(getPathOfFile("D:\\tt\\e.txt"));
	System.out.println(bytesTokb(1024));
	System.out.println(convertFileSize(1024 * 1024));
    }
}