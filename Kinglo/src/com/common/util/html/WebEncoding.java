package com.common.util.html;

import info.monitorenter.cpdetector.io.CodepageDetectorProxy;
import info.monitorenter.cpdetector.io.HTMLCodepageDetector;
import info.monitorenter.cpdetector.io.JChardetFacade;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 获取文件编码辅助类
 * @author zou
 * 2015-2-2
 */
public final class WebEncoding {
    private static CodepageDetectorProxy detector = CodepageDetectorProxy
	    .getInstance();

    static {
	detector.add(new HTMLCodepageDetector(false));

	detector.add(JChardetFacade.getInstance());
    }
    
    /**
     * 获取URL路径的字符编码
     * @param strurl
     * @return
     * @throws IOException
     */
    public static String getCharset(String strurl) throws IOException {
	URL url = new URL(strurl);

	HttpURLConnection urlConnection = (HttpURLConnection) url
		.openConnection();

	urlConnection.connect();

	String strencoding = null;

	Map<String, List<String>> map = urlConnection.getHeaderFields();
	Set<String> keys = map.keySet();
	Iterator<String> iterator = keys.iterator();

	String key = null;
	String tmp = null;
	while (iterator.hasNext()) {
	    key = (String) iterator.next();
	    tmp = ((List<String>) map.get(key)).toString().toLowerCase();
	    if ((key != null) && (key.equals("Content-Type"))) {
		int m = tmp.indexOf("charset=");
		if (m != -1) {
		    strencoding = tmp.substring(m + 8).replace("]", "");
		    return strencoding;
		}
	    }
	}
	StringBuffer sb = new StringBuffer();
	try {
	    BufferedReader in = new BufferedReader(new InputStreamReader(
		    url.openStream()));
	    String line;
	    while ((line = in.readLine()) != null) {
		sb.append(line);
	    }
	    in.close();
	} catch (Exception e) {
	    System.err.println(e);
	    System.err
		    .println("Usage:   java   HttpClient   <URL>   [<filename>]");
	}
	String htmlcode = sb.toString();

	String strbegin = "<meta";
	String strend = ">";

	int begin = htmlcode.indexOf(strbegin);
	int end = -1;
	while (begin > -1) {
	    end = htmlcode.substring(begin).indexOf(strend);
	    if ((begin > -1) && (end > -1)) {
		String strtmp = htmlcode.substring(begin, begin + end)
			.toLowerCase();
		int inttmp = strtmp.indexOf("charset");
		if (inttmp > -1) {
		    strencoding =

		    strtmp.substring(inttmp + 7, end).replace("=", "")
			    .replace("/", "").replace("\"", "")
			    .replace("'", "").replace(" ", "");
		    return strencoding;
		}
	    }
	    htmlcode = htmlcode.substring(begin);
	    begin = htmlcode.indexOf(strbegin);
	}
	strencoding = getFileEncoding(url);
	if (strencoding == null) {
	    strencoding = "GBK";
	}
	return strencoding;
    }
    
    /**
     * 获取URL地址的字符编码
     * @param url URL地址
     * @return
     * @throws IOException
     */
    public static String getFileEncoding(URL url) throws IOException {
	Charset charset = null;

	charset = detector.detectCodepage(url);
	if (charset != null) {
	    return charset.name();
	}
	return null;
    }
}