package com.example.url;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
public class URLUtil {

  private URLConnection connection = null;
  private URL url = null;
  private boolean timedOut = false;

  protected URLUtil() {}

  protected URLUtil(URL url) {
    this.url = url;
  }

  protected synchronized URLConnection openConnection(int timeout) throws
      IOException {
    Thread t = new Thread(new URLConnectorThread());
    t.start();

    try {
      Thread.currentThread().sleep(1000*60*1);
      System.out.println("线程等待");
      this.wait(timeout);
      System.out.println("主线程继续执行!");
    } catch (InterruptedException e) {
      if (connection == null)
        timedOut = true;
      else
        close(connection);
      throw new IOException("Connection never established");
    }

    if (connection != null) {
      return connection;
    } else {
      timedOut = true;
      throw new IOException("Connection timed out");
    }
  }

  public static URLConnection openConnection(URL url) throws IOException {
    return openConnection(url, 30000);
  }

  public static URLConnection openConnection(URL url, int timeout) throws
      IOException {
    URLUtil uc = new URLUtil(url);
    return uc.openConnection(timeout);
  }

  // special thread to open the connection
  private class URLConnectorThread implements Runnable {
    public void run() {
      URLConnection con = null;
      try {
        con = url.openConnection();
      } catch (IOException e) {}

      synchronized (URLUtil.this) {
        if (timedOut && con != null)
          close(con);
        else {
          connection = con;
          URLUtil.this.notify();
          System.out.println("唤醒线程!");
        }
      }
    }
  }

  // closes the HttpURLConnection does nothing to others
  private static void close(URLConnection con) {
    if (con instanceof HttpURLConnection) {
      ( (HttpURLConnection) con).disconnect();
    }
  }

  public static void main(String[] args) throws IOException {
	String mobile="18301732237";
	String urlStr="http://tcc.taobao.com/cc/json/mobile_tel_segment555.htm?tel="+mobile;
	URL url=new URL(urlStr);
	System.out.println("开始连接。。。。");
	URLConnection connection=openConnection(url);
	InputStream in=connection.getInputStream();
  }
}	