package com.example.url;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class WebServiceUtil {
	
	/**
	 * 手机号码归属地
	 */
	public static String mobileAttribution(String mobile){
		final int TIME_OUT = 30000;
		String sCurrentLine = "";
		String sTotalString = "";
		try {
			/**
			 * 可用的URL
			 * http://api.showji.com/Locating/xiying18da.aspx?m="+mobile+"&output=json&callback
			 * http://api.showji.com/Locating/diaoyudao_zhongguode.aspx?m="+mobile+"&output=json&callback=querycallback
			 * http://api.showji.com/Locating/diaoyudao_zhongguode.aspx?m="+mobile+"&output=json&callback=querycallback
			 * http://tcc.taobao.com/cc/json/mobile_tel_segment.htm?tel=13952246053
			 */
			URL postUrl = new URL(
					"http://tcc.taobao.com/cc/json/mobile_tel_segment.htm?tel="+mobile);
			HttpURLConnection connection = (HttpURLConnection) postUrl.openConnection();
			connection.setConnectTimeout(TIME_OUT);
			connection.setReadTimeout(TIME_OUT);
			connection.setDoOutput(true);
			connection.setRequestMethod("POST");
			connection.setUseCaches(false);
			connection.setInstanceFollowRedirects(true);
			connection.setRequestProperty("Content-Type",
					"application/x-www-form-urlencoded");
			connection.connect();
			OutputStreamWriter out = new OutputStreamWriter(connection.getOutputStream());
			out.flush();
			out.close();
			InputStream l_urlStream = connection.getInputStream();
			BufferedReader l_reader = new BufferedReader(new InputStreamReader(
					l_urlStream, "GBK"));
			while ((sCurrentLine = l_reader.readLine()) != null) {
				sTotalString += sCurrentLine + "\r\n";
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (sTotalString.length() > 0) {
			sTotalString = sTotalString.replace("querycallback", "");
			sTotalString = sTotalString.replace("(", "");
			sTotalString = sTotalString.replace(")", "");
			sTotalString = sTotalString.replace(";", "");
		}
		return sTotalString;
	}
	
	
	public static void main(String[] args) {
		System.out.println(mobileAttribution("18301732237"));
	}
}
