package com.common.util.ftp;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;
import sun.net.TelnetInputStream;
import sun.net.TelnetOutputStream;
import sun.net.ftp.FtpClient;

public class FTPUtil {
	
	private static Logger logger=Logger.getLogger(FTPUtil.class);
	
	private static FtpClient ftpClient;
	
	public static void connServer(String ip,String username,String password,String path){
		try {
			
			ftpClient=new FtpClient();//创建对象
			
			ftpClient.openServer(ip);//开启服务
			
			ftpClient.login(username, password);//登陆服务器
			
			if(path.length()>0){
				ftpClient.cd(path);//跳转到path子目录下
			}
			
			ftpClient.binary();  //用二进制进行上传和下载
		} catch (IOException e) {
			logger.info("conn ftp server throw Exception!",e);
		}
	}
	
	/**
	 * 断开FTP连接
	 * @throws IOException 
	 */
	public static void closeServer() {
		try {
			if(null!=ftpClient){
				ftpClient.closeServer();
			}
		} catch (IOException e) {
			logger.info("close ftp server throw Exception!",e);
		}
	}
	
	/**
	 * 上传文件
	 * @return
	 */
	public static boolean uploadFile(String newName,File uploadFile){
		boolean flag=false;
		TelnetOutputStream tos=null;
		FileInputStream fis=null;
		try {
			//ftpclient的put方法 调用后将路径传进去 返回TelnetOutputStream 就可以进行文件上传操作了 			tos=ftpClient.put(newName);
			tos=ftpClient.put(newName);
			fis=new FileInputStream(uploadFile);
			byte[] bytes=new byte[2048];
			int len;
			while((len=fis.read(bytes))!=-1){
				tos.write(bytes, 0, len);
			}
			flag=true;
		} catch (Exception e) {
			logger.info("ftp server upload file throw Exception!",e);
		}finally{
			try {
				if(null!=tos){
					tos.flush();
					tos.close();
				}
				if(null!=fis){
					fis.close();
				}
			} catch (Exception e2) {
				logger.info("FTPUtil.class unable to close stream!",e2);
			}
		}
		return flag;
	}
	
	/**
	 * 下载文件
	 */
	public static InputStream downloadFile(String ip,String username,String password,String path,String fileName){
		connServer(ip,username,password,path);
		TelnetInputStream tls=null;
		try {
			if(null!=ftpClient){
				tls=ftpClient.get(fileName);
			}
		} catch (Exception e) {
			logger.info("FTPUtil.class downloadFile method throw Exception!",e);
		}
		return tls;
	}
	
	/**
	 * 文件如果不存在  无限循环的创建文件夹
	 */
	public static boolean createDir(String dirPath,String[] names){
		String[] dirs = dirPath.split("/");  
		String curDir = "";  
		for (int i = 0; i < dirs.length; i++)  
		{  
			String dir = dirs[i];  
			if (dir != null && dir.length() > 0)  
			{  
				try
				{
					curDir += dir+"/";  
					for (int j = 0; j < names.length; j++) {
						if (dir.equals(names[j])) {
							ftpClient.cd(names[j]);  
						}
					}
				} catch (Exception e1)
				{
					//通过远程命令 创建一个files文件夹  
					ftpClient.sendServer("MKD " + curDir + "\r\n");
					createDir(dirPath, names);
					try
					{
						//创建远程文件夹   
						//远程命令包括  
						//USER    PORT    RETR    ALLO    DELE    SITE    XMKD    CDUP    FEAT<br>  
						//      PASS    PASV    STOR    REST    CWD     STAT    RMD     XCUP    OPTS<br>  
						//      ACCT    TYPE    APPE    RNFR    XCWD    HELP    XRMD    STOU    AUTH<br>  
						//      REIN    STRU    SMNT    RNTO    LIST    NOOP    PWD     SIZE    PBSZ<br>  
						//      QUIT    MODE    SYST    ABOR    NLST    MKD     XPWD    MDTM    PROT<br>  
						//           在服务器上执行命令,如果用sendServer来执行远程命令(不能执行本地FTP命令)的话，所有FTP命令都要加上/r/n<br>  
						//                ftpclient.sendServer("XMKD /test/bb/r/n"); //执行服务器上的FTP命令<br>  
						//                ftpclient.readServerResponse一定要在sendServer后调用<br>  
						//                nameList("/test")获取指目录下的文件列表<br>  
						//                XMKD建立目录，当目录存在的情况下再次创建目录时报错<br>  
						//                XRMD删除目录<br>  
						//                DELE删除文件<br>  
						//这个方法必须在 这两个方法中间调用 否则 命令不管用  
						//ftpClient.binary(); 
						ftpClient.readServerResponse();
					} catch (Exception e)
					{
						e.printStackTrace();
						return false;
					}
					return true;  
				}
			}  
		} 
		return true;
	}
	
	/**
	 * 删除服务器的文件
	 */
	public static boolean deleteFile(String path,String fileName){
		try {
			if(null!=ftpClient){
				ftpClient.cd(path);
				ftpClient.sendServer("dele "+fileName+"\r\n");
				ftpClient.readServerResponse();
				return true;
			}else{
				throw new Exception();
			}
		} catch (Exception e) {
			logger.info("FTPUtil.class delete file throw Exception!",e);
			return false;
		}
	}
	
	/**
	 * 获得当前目录
	 */
	public static String getCurrentDir(){
		try {
			if(null!=ftpClient){
				return ftpClient.pwd();
			}
		} catch (Exception e) {
			logger.info("unable to get current dir!",e);
		}
		return "";
	}
	
	/**
	 *返回FTP目录下的文件列表
	 */
	@SuppressWarnings("deprecation")
	public static List<String> getFileNameList(String ftpDirectionary){
		List<String> list=new ArrayList<String>();
		try {
			DataInputStream dis=new DataInputStream(ftpClient.nameList(ftpDirectionary));
			String fileName;
			while((fileName=dis.readLine())!=null){
				list.add(fileName);
			}
		} catch (Exception e) {
			logger.info("get ftp filename list failed! ",e);
		}
		return list;
	}
	
	/**
	* 取得指定目录下的所有文件名，不包括目录名称
	 * 分析nameList得到的输入流中的数，得到指定目录下的所有文件名
	 *  @param  fullPath String
	 *  @return  ArrayList
	 *  @throws  Exception
	 */
	public  List<String> fileNames(String fullPath)  throws  Exception  {
		ftpClient.ascii();  // 注意，使用字符模式
		TelnetInputStream list  =  ftpClient.nameList(fullPath);
		byte [] names  =   new   byte [ 2048 ];
		int  bufsize  =   0 ;
		bufsize  =  list.read(names,0,names.length);   // 从流中读取
		list.close();
		List<String> namesList  =   new  ArrayList<String>();
		int  i  =   0 ;
		int  j  =   0 ;
		while  (i  <  bufsize)  {
			if  (names[i]  ==   10 )  {  
				String tempName  =   new  String(names, j, i  -  j);
				namesList.add(tempName);
				j  =  i  +   1 ;   // 上一次位置字符模式
			}
			i  =  i  +   1 ;
		}
		return  namesList;
	}
	
	/**
	 * 下载一个目录的文件
	 * @param rwfiledir
	 * @param filepathname
	 * @return
	 */
	public DataInputStream downloadDirFile(String rwfiledir,String filepathname){ 
		DataInputStream puts = null;
		if (ftpClient != null) 
		{ 
			//System.out.println("正在下载文件"+filepathname+",请等待...."); 
			try{ 
				int ch; 
				File fi = new File(filepathname); 
				RandomAccessFile getfile = new RandomAccessFile(fi,"rw"); 
				getfile.seek(0); 
				TelnetInputStream fget=ftpClient.get(rwfiledir); 
				puts = new DataInputStream(fget); 
				while ((ch = puts.read()) >= 0) { 
					getfile.write(ch); 
				} 
				fget.close(); 
				getfile.close(); 
				//message = "下载"+rwfiledir+"文件到"+filepathname +"目录成功!"; 
				//System.out.println(message); 
			} 
			catch(IOException e){ 
				e.printStackTrace();
				//message = "下载"+rwfiledir+"文件到"+filepathname +"目录失败!"+e; 
				//System.out.println(message); 
				//result = false ; 
			}
		} 
		return puts; 
	}
	
	public static void uploadTextFile(String newName,String content) throws IOException
	{
		TelnetOutputStream tos=null;//telnet输出流
		java.io.ByteArrayInputStream fis=null;//地文件读取流
		try {
			//ftpclient的put方法 调用后将路径传进去 返回TelnetOutputStream 就可以进行文件上传操作了  
			tos=ftpClient.put(newName);
			fis=new java.io.ByteArrayInputStream(content.getBytes());
			byte[] bytes=new byte[1024];
			int len;
			while((len=fis.read(bytes))>0)
			{
				tos.write(bytes, 0, len);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			if(tos!=null)
			{
				tos.close();//关闭telnet输出流
			}
			if(fis!=null)
			{
				fis.close();//关闭本地文件读取流
			}
		}	
	}
}
