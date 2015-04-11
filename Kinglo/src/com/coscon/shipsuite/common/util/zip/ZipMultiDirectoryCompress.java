package com.coscon.shipsuite.common.util.zip;

import com.coscon.shipsuite.common.exception.ShipSuiteRuntimeException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipOutputStream;

/**
 * 压缩多个目录辅助类
 * 此压缩辅助类压缩成功后不会删除相应的目录和文件
 * @author zou
 * 2015-2-4
 */
public final class ZipMultiDirectoryCompress {
    /**
     * 压缩目录
     * @param directory		待压缩的目录
     * @param zipFilename	压缩后的文件名
     */
    public static void zip(String directory, String zipFilename) {
	zip(directory, zipFilename, null);
    }
    
    /**
     * 此方法不可用
     * @param directory
     * @param zipFilename
     * @param includeFiles
     */
    public static void zip(String directory, String zipFilename,
	    Collection<String> includeFiles) {
	ZipMultiDirectoryCompress zipCompress = new ZipMultiDirectoryCompress();

	String defaultParentPath = "";
	ZipOutputStream zos = null;
	try {
	    zos = new ZipOutputStream(new FileOutputStream(zipFilename));

	    zipCompress.startCompress(zos, defaultParentPath, directory,
		    includeFiles);
	} catch (Exception e) {
	    throw new ShipSuiteRuntimeException(e);
	} finally {
	    try {
		if (zos != null) {
		    zos.close();
		}
	    } catch (IOException e) {
		e.printStackTrace();
	    }
	}
    }

    private void startCompress(ZipOutputStream zos, String oppositePath,
	    String directory, Collection<String> includeFiles) {
	File file = new File(directory);
	if (file.isDirectory()) {
	    File[] files = file.listFiles();
	    for (int i = 0; i < files.length; i++) {
		File f = files[i];
		if (f.isDirectory()) {
		    String newOppositePath = oppositePath + f.getName() + "/";

		    compressDirectory(zos, oppositePath, f);

		    startCompress(zos, newOppositePath, f.getPath(),
			    includeFiles);
		} else if ((includeFiles == null) || (includeFiles.isEmpty())
			|| (includeFiles.contains(f.getPath()))) {
		    compressFile(zos, oppositePath, f);
		}
	    }
	} else {
	    compressFile(zos, oppositePath, file);
	}
    }
    
    private void compressFile(ZipOutputStream zos, String oppositePath, File file) {
	ZipEntry entry = new ZipEntry(oppositePath + file.getName());
	InputStream is = null;
	try {
	    zos.putNextEntry(entry);

	    is = new FileInputStream(file);
	    int length = 0;
	    int bufferSize = 1024;
	    byte[] buffer = new byte[bufferSize];
	    while ((length = is.read(buffer, 0, bufferSize)) >= 0) {
		zos.write(buffer, 0, length);
	    }
	    zos.closeEntry();
	} catch (IOException ex) {
	    throw new ShipSuiteRuntimeException(ex);
	} finally {
	    try {
		if (is != null) {
		    is.close();
		}
	    } catch (IOException ex) {
		ex.printStackTrace();
	    }
	}
    }

    private void compressDirectory(ZipOutputStream zos, String oppositePath,
	    File file) {
	ZipEntry entry = new ZipEntry(oppositePath + file.getName() + "/");
	try {
	    zos.putNextEntry(entry);
	    zos.closeEntry();
	} catch (IOException e) {
	    throw new ShipSuiteRuntimeException(e);
	}
    }
}
