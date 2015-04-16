package com.example.file;

import java.io.FileWriter;
import java.io.IOException;
import java.io.RandomAccessFile;

import com.common.util.string.SystemUtil;

/**
 * 将内容追加到文件尾部
 */
public class AppendToFile {

	/**
	 * A方法追加文件：使用RandomAccessFile
	 * @param fileName	文件名
	 * @param content	追加的内容
	 */
	public static void appendMethodByRandomAccessFile(String fileName, String content){
		try {
			//	打开一个随机访问文件流，按读写方式
			RandomAccessFile randomFile = new RandomAccessFile(fileName, "rw");
			//	文件长度，字节数
			long fileLength = randomFile.length();
			//将写文件指针移到文件尾。
			randomFile.seek(fileLength);
			randomFile.writeBytes(content);
			randomFile.close();
		} catch (IOException e){
			e.printStackTrace();
		}
	}
	/**
	 * B方法追加文件：使用FileWriter(效率高)
	 * @param fileName
	 * @param content
	 */
	public static void appendMethodByFileWriter(String fileName, String content){
		try {
			//打开一个写文件器，构造函数中的第二个参数true表示以追加形式写文件
			FileWriter writer = new FileWriter(fileName, true);
			writer.write(content);
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 在新行上添加内容
	 * @param fileName
	 * @param content
	 */
	public static void appendToNewLine(String fileName,String content){
		try {
			String line_separator="";
			if(SystemUtil.IS_OS_WINDOWS){
				line_separator="\r\n";
			}else if(SystemUtil.IS_OS_MAC){
				line_separator="\n";
			}else{
				line_separator="\r";
			}
			FileWriter writer=new FileWriter(fileName,true);
			writer.write(line_separator);
			writer.write(content);
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		
		String fileName = "D:\\test.txt";
		String content = "new append!";
		//按方法A追加文件
		AppendToFile.appendMethodByRandomAccessFile(fileName, content);
		AppendToFile.appendMethodByRandomAccessFile(fileName, "append end. \r\n");
		//显示文件内容
		ReadFromFile.readFileByLines(fileName);
		//按方法B追加文件
		AppendToFile.appendMethodByFileWriter(fileName, content);
		AppendToFile.appendMethodByFileWriter(fileName, "append end. \r\n");
		//显示文件内容
		ReadFromFile.readFileByLines(fileName);
		
		appendToNewLine(fileName,"newLine");
		//显示文件内容
		ReadFromFile.readFileByLines(fileName);
	}
}

