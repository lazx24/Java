package com.example.file;



import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Writer;
/**
 * 多种方式写文件
 */
public class WriteToFile {
	
	/**
	 * 从String中创建文件，如果此文件存在覆盖掉，如果不存在创建此文件。
	 * 
	 * @param filePath
	 * @param fileData
	 * @return 写文件是否成功.
	 * @throws IOException
	 */
	public static boolean writeFile(String filePath, String fileData,
			String charsetName) {
		if (filePath == null || fileData == null) {
			System.out.println("the fileName or fileData is null: fileName="
					+ filePath + " fileData=" + fileData);
			return false;
		} else if (filePath.equals("") || filePath.trim().equals("")) {
			System.out.println("the fileName or fileData is   : fileName=" + filePath
					+ " fileData=" + fileData);
			return false;
		}
		FileOutputStream fileOutputStream = null;
		try {
			byte[] data = fileData.getBytes(charsetName);
			File file = new File(filePath);
			if (!file.exists()) {
				System.out.println("this file is not exist!:" + filePath);
				file.createNewFile();
				System.out.println("creat file!:" + filePath);
			}
			fileOutputStream = new FileOutputStream(filePath);
			fileOutputStream.write(data);
			fileOutputStream.close();
			System.out.println("write file:" + filePath);
			return true;
		} catch (FileNotFoundException e) {
			System.out.println(e.getMessage());
		} catch (IOException e) {
			System.out.println(e.getMessage());
		} finally {
			if (fileOutputStream != null) {
				try {
					fileOutputStream.close();
				} catch (IOException e) {
					System.out.println(e.toString());
					e.printStackTrace();
				}
			}
		}
		return false;
	}
	
	/**
	 * 将byte[]的数据写入文件，如果此文件存在覆盖掉，如果不存在创建此文件。
	 * 
	 * @param filePath
	 *            文件全路径.
	 * @param fileData
	 *            文件数据.
	 * @return 写文件是否成功.
	 */
	public static boolean writeFile(String filePath, byte[] fileData) {
		if (filePath == null || fileData == null) {
			System.out.println("filePath or fileData is null");
			return false;
		} else if (filePath.trim().equals("")) {
			System.out.println("filePath is \"\"!");
			return false;
		}
		FileOutputStream write;
		try {
			write = new FileOutputStream(filePath);
			write.write(fileData);
			write.close();
			System.out.println("write file:" + filePath + " success!");
			return true;
		} catch (FileNotFoundException e) {
			System.out.println(e.getMessage());
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
		return false;
	}

	/**
	 * 从String中创建文件，如果此文件存在覆盖掉，如果不存在创建此文件,如果文件路径上的目录没有则创建此目录。
	 * 
	 * @param filePath
	 * @param fileData
	 * @return 写文件是否成功.
	 * @throws IOException
	 */
	public static boolean directWriteFile(String filePath, String fileData,
			String charsetName) {
		if (filePath == null || fileData == null) {
			System.out.println("the fileName or fileData is null: fileName="
					+ filePath + " fileData=" + fileData);
			return false;
		} else if (filePath.equals("") || filePath.trim().equals("")) {
			System.out.println("the fileName or fileData is   : fileName=" + filePath
					+ " fileData=" + fileData);
			return false;
		}
		String fileDir = filePath.substring(0, filePath.lastIndexOf(System
				.getProperty("file.separator")));
		boolean flag = makeDirectory(fileDir);
		if (!flag) {
			return false;
		}
		FileOutputStream fileOutputStream = null;
		try {
			byte[] data = fileData.getBytes(charsetName);
			File file = new File(filePath);
			if (!file.exists()) {
				System.out.println("this file is not exist!:" + filePath);
				file.createNewFile();
				System.out.println("creat file!:" + filePath);
			}
			fileOutputStream = new FileOutputStream(filePath);
			fileOutputStream.write(data);
			fileOutputStream.close();
			System.out.println("write file:" + filePath);
			return true;
		} catch (FileNotFoundException e) {
			System.out.println(e.getMessage());
		} catch (IOException e) {
			System.out.println(e.getMessage());
		} finally {
			if (fileOutputStream != null) {
				try {
					fileOutputStream.close();
				} catch (IOException e) {
					System.out.println(e.toString());
					e.printStackTrace();
				}
			}
		}
		return false;
	}
	
	/**
	 * 将byte[]的数据写入文件，如果此文件存在则覆盖掉，如果不存在创建此文件,目录不存在也直接创建其目录。
	 * 
	 * @param filePath
	 *            文件全路径.
	 * @param fileData
	 *            文件数据.
	 * @return 写文件是否成功.
	 */
	public static boolean directWriteFile(String filePath, byte[] fileData) {
		if (filePath == null || fileData == null) {
			System.out.println("filePath or fileData is null");
			return false;
		} else if (filePath.trim().equals("")) {
			System.out.println("filePath is \"\"!");
			return false;
		}
		String fileDir = filePath.substring(0, filePath.lastIndexOf(System
				.getProperty("file.separator")));
		boolean flag = makeDirectory(fileDir);
		if (!flag) {
			return false;
		}
		FileOutputStream write;
		try {
			write = new FileOutputStream(filePath);
			write.write(fileData);
			write.close();
			System.out.println("write file:" + filePath + " success!");
			return true;
		} catch (FileNotFoundException e) {
			System.out.println(e.getMessage());
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
		return false;
	}
	
	/**
	 * 从给定的字符串中创建文件系统路径。
	 * 
	 * @param directory
	 *            给定的路径表示字符串。
	 * @return 是否成功 成功==true.
	 */
	public static boolean makeDirectory(String directory) {
		File file = new File(directory);
		if (!file.exists()) {
			if (file.mkdirs()) {
				System.out.println("make dirctory success!:" + directory);
				return true;
			} else {
				System.out.println("make dirctory <<<<<<<faile>>>>>>>!:" + directory);
				return false;
			}
		} else {
			System.out.println("this directory is existed!:" + directory);
			return true;
		}
	}
	
	public static void main(String[] args) {
		
	}
}

