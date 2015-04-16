package com.common.util.html;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import javax.servlet.http.HttpServletRequest;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.common.exception.ShipSuiteRuntimeException;
import com.common.util.fulltext.FulltextUtil;
import com.common.util.string.StringUtil;

public final class HtmlUtil {
    private static final String ENCODE_UTF8 = "UTF-8";
    
    /**
     * 获取URL文件编码
     * @param url
     * @return
     */
    public static String detectCharset(URL url) {
	String charset = null;
	try {
	    charset = WebEncoding.getFileEncoding(url);
	} catch (Exception e) {
	    throw new ShipSuiteRuntimeException(e);
	}
	if ((charset.equals("void")) || (charset.equalsIgnoreCase("big5"))) {
	    return "GBK";
	}
	return charset;
    }
    
    /**
     * 自动生成HTTP请求
     * @param url  请求路径
     * @return
     */
    public static String fixHttpUrl(String url) {
	if ((StringUtil.isNotNullAndNotEmpty(url))
		&& (!url.trim().toLowerCase().startsWith("http"))) {
	    url = "http://" + url.trim();
	}
	return url;
    }
    
    /**
     * 自动生成带端口的HTTPS请求
     * @param url		请求路径
     * @param httpsPort		请求端口
     * @return
     */
    public static String fixHttpsUrl(String url, int httpsPort) {
	if (StringUtil.isNotNullAndNotEmpty(url)) {
	    url = url.trim();
	    if (!url.toLowerCase().startsWith("http")) {
		url = "https://" + url;
	    } else if (url.toLowerCase().startsWith("http://")) {
		url = "https://" + url.substring("http://".length());
	    }
	    String hostnamePort = getHostnamePort(url);

	    int portIndex = hostnamePort.indexOf(":");
	    if (portIndex > 0) {
		String port = hostnamePort.substring(portIndex + 1);
		url = url.replaceAll(":" + port, ":" + httpsPort);
	    } else {
		url = url.replaceAll("//" + hostnamePort, "//" + hostnamePort
			+ ":" + httpsPort);
	    }
	}
	return url;
    }
    
    /**
     * 获取响应的HTML
     * @param url	请求路径
     * @return
     */
    public static String getResponseHtml(String url) {
	return getResponseHtml(url, null, "UTF-8");
    }
    
    /**
     * 获取响应的HTML
     * @param url	请求路径
     * @param charset	字符编码
     * @return
     */
    public static String getResponseHtml(String url, String charset) {
	return getResponseHtml(url, null, charset);
    }
    
    /**
     * 获取响应的HTML
     * @param url	请求路径
     * @param paramMap	参数Map
     * @return
     */
    public static String getResponseHtml(String url,
	    Map<String, String> paramMap) {
	return getResponseHtml(url, paramMap, "UTF-8");
    }
   
    /**
     * 获取响应的HTML
     * @param url	请求路径
     * @param paramMap	参数Map
     * @param charset	字符编码
     * @return
     */
    public static String getResponseHtml(String url,
	    Map<String, String> paramMap, String charset) {
	HttpClient httpclient = new DefaultHttpClient();
	StringBuffer urlSb = new StringBuffer(url.trim());
	StringBuffer paramSb = new StringBuffer();
	if (paramMap != null) {
	    for (Map.Entry<String, String> entry : paramMap.entrySet()) {
		if (paramSb.length() > 0) {
		    paramSb.append("&");
		}
		try {
		    paramSb.append(
			    URLEncoder.encode(((String) entry.getKey()).trim(),
				    charset))
			    .append("=")
			    .append(URLEncoder.encode(
				    entry.getValue() == null ? ""
					    : ((String) entry.getValue())
						    .trim(), charset));
		} catch (UnsupportedEncodingException e) {
		    throw new ShipSuiteRuntimeException(e);
		}
	    }
	    if (!url.contains("?")) {
		urlSb.append("?");
	    } else {
		urlSb.append("&");
	    }
	    urlSb.append(paramSb);
	}
	HttpGet httpget = new HttpGet(urlSb.toString());
	HttpResponse response = null;
	try {
	    response = httpclient.execute(httpget);
	} catch (Exception ex) {
	    throw new ShipSuiteRuntimeException(ex);
	}
	HttpEntity entity = response.getEntity();
	if (entity == null) {
	    return null;
	}
	InputStream instream = null;
	BufferedReader bufferReader = null;
	InputStreamReader isReader = null;
	try {
	    instream = entity.getContent();
	    isReader = new InputStreamReader(instream, charset);
	    bufferReader = new BufferedReader(isReader);

	    StringBuffer htmlSb = new StringBuffer();
	    String line;
	    while ((line = bufferReader.readLine()) != null) {
		htmlSb.append(line);
	    }
	    return htmlSb.toString();
	} catch (Exception ex) {
	    throw new ShipSuiteRuntimeException(ex);
	} finally {
	    try {
		instream.close();
	    } catch (IOException localIOException3) {
	    }
	    if (isReader != null) {
		try {
		    isReader.close();
		} catch (IOException localIOException4) {
		}
	    }
	    if (bufferReader != null) {
		try {
		    bufferReader.close();
		} catch (IOException localIOException5) {
		}
	    }
	}
    }
    
    /**
     * 获取HTML网页href链接
     * @param htmlContent  网页内容
     * @return
     */
    public static Map<String, String> getHrefMap(String htmlContent) {
	Document doc = Jsoup.parse(htmlContent);

	Elements links = doc.select("a[href]");
	Map<String, String> linkMap = new HashMap<String,String>(links.size());
	for (Element link : links) {
	    String href = link.attr("href");
	    String text = link.text();
	    if ((StringUtil.isNotNullAndNotEmptyWithTrim(href))
		    && (StringUtil.isNotNullAndNotEmptyWithTrim(text))
		    && (!linkMap.containsKey(href))) {
		linkMap.put(href, text.trim());
	    }
	}
	return linkMap;
    }
    
    /**
     * 获取HTML网页href链接
     * @param htmlContent  网页内容
     * @return
     */
    public static List<String> getHref(String htmlContent) {
	Map<String, String> map = getHrefMap(htmlContent);
	List<String> list = new ArrayList<String>(map.values());
	return list;
    }
    
    
    public static Map<String, String> getHrefMap(String htmlContent,
	    String baseUrl) {
	Document doc = Jsoup.parse(htmlContent, baseUrl);
	return getHrefMap(doc);
    }
    
    /**
     * 获取HTML网页href链接
     * @param doc 转换后的Document对象
     * @return
     */
    private static LinkedHashMap<String, String> getHrefMap(Document doc) {
	Elements links = doc.select("a[href]");
	LinkedHashMap<String, String> linkMap = new LinkedHashMap<String,String>(links.size());
	for (Element link : links) {
	    String href = link.absUrl("href");
	    String text = link.text();
	    if ((StringUtil.isNotNullAndNotEmptyWithTrim(href))
		    && (StringUtil.isNotNullAndNotEmptyWithTrim(text))
		    && (!linkMap.containsKey(href))) {
		linkMap.put(href, text.trim());
	    }
	}
	return linkMap;
    }
    
    /**
     * 获取HTML网页href链接
     * @param url URL地址
     * @return
     */
    public static Map<String, String> getHrefMap(URL url) {
	Document doc;
	try {
	    doc = Jsoup.connect(url.toString()).get();
	} catch (IOException e) {
	    throw new ShipSuiteRuntimeException(e);
	}
	return getHrefMap(doc);
    }
    
    /**
     * 获取URL地址HTML响应结果
     * @param url  URL地址
     * @return
     */
    public static HtmlResponseResult parseUrl(URL url) {
	Document doc;
	try {
	    doc = Jsoup.connect(url.toString()).get();
	} catch (IOException e) {
	    throw new ShipSuiteRuntimeException(e);
	}
	return getHtmlResponseResult(doc);
    }

    private static HtmlResponseResult getHtmlResponseResult(Document doc) {
	HtmlResponseResult result = new HtmlResponseResult();
	result.setHrefMap(getHrefMap(doc));
	result.setHtmlContent(doc.html());
	result.setPlainText(Jsoup.parse(result.getHtmlContent()).text());

	return result;
    }
    
    /**
     * 获取URL地址HTML响应结果
     * @param url	URL地址
     * @param charset	字符编码	
     * @param isUserAgent是否使用代理
     * @return
     */
    public static HtmlResponseResult parseUrl(URL url, String charset,
	    boolean isUserAgent) {
	InputStream inputStream = null;
	Document doc;
	try {
	    if (isUserAgent) {
		HttpURLConnection httpUrlConn = (HttpURLConnection) url
			.openConnection();
		httpUrlConn.setDoInput(true);
		httpUrlConn.setRequestMethod("GET");

		httpUrlConn
			.setRequestProperty("User-Agent",
				"Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");

		inputStream = httpUrlConn.getInputStream();
	    } else {
		inputStream = url.openStream();
	    }
	    doc = Jsoup.parse(inputStream, charset, url.toString());
	} catch (IOException e) {
	    throw new ShipSuiteRuntimeException(e);
	} finally {
	    if (inputStream != null) {
		try {
		    inputStream.close();
		} catch (IOException localIOException1) {
		}
	    }
	}
	return getHtmlResponseResult(doc);
    }
    
    /**
     * 移除HTML空格(&nbsp;)
     * @param content
     * @return
     */
    public static String removeHtmlSpace(String content) {
	return content.trim().replaceAll("&nbsp;", "");
    }
    
    /**
     * Text转换为HTML
     * @param text  文本内容
     * @return
     */
    public static String text2html(String text) {
	StringBuffer htmlSb = new StringBuffer();
	htmlSb.append("<html>\r\n");
	htmlSb.append("<head>\r\n");
	htmlSb.append("<META HTTP-EQUIV=\"Content-Type\" CONTENT=\"text/html;charset=UTF-8\">\r\n");

	htmlSb.append("</head>\r\n");
	htmlSb.append("<body>\r\n");
	htmlSb.append(text.replaceAll("\\n", "\n<br>"));
	htmlSb.append("</body>\r\n");
	htmlSb.append("</html>\r\n");
	return htmlSb.toString();
    }
    
    /**
     * HTML转化为Text
     * @param html HTML页面内容
     * @return
     */
    public static String html2text(String html) {
	return Jsoup.parse(html).text();
    }
    
    /**
     * HTML转化为Text
     * @param url URL地址
     * @return
     */
    public static String html2text(URL url) {
	try {
	    return Jsoup.parse(url, 3000).text();
	} catch (IOException e) {
	    throw new ShipSuiteRuntimeException(e);
	}
    }
    
    /**
     * 构建href链接
     * @param hrefUrl href链接
     * @param title   内容
     * @return	<a href="http://www.baidu.com" target="_blank">我是共产党</a>
     */
    public static String buildHref(String hrefUrl, String title) {
	return buildHref(hrefUrl, title, "", "_blank");
    }
    
    /**
     * 构建href链接
     * @param hrefUrl href链接
     * @param title   内容
     * @param target  目标
     * @return
     */
    public static String buildHref(String hrefUrl, String title, String target) {
	return buildHref(hrefUrl, title, "", target);
    }
    
    /**
     * 构建href链接
     * @param hrefUrl	href链接
     * @param title	内容
     * @param altString	悬浮其上显示的文字
     * @param target	目标
     * @return
     */
    public static String buildHref(String hrefUrl, String title,
	    String altString, String target) {
	return buildHref(hrefUrl, title, altString, target, null);
    }
    
    /**
     *  构建href链接
     * @param hrefUrl	href链接
     * @param title	内容
     * @param altString 悬浮其上显示的文字
     * @param target	目标
     * @param paramMap	参数键值对
     * @return
     */
    public static String buildHref(String hrefUrl, String title,
	    String altString, String target, Map<String, String> paramMap) {
	return buildHref(hrefUrl, title, altString, target, paramMap, null);
    }
    
    /**
     * 构建href链接
     * @param hrefUrl 	href链接
     * @param title	内容
     * @param altString 悬浮其上显示的文字
     * @param target	目标
     * @param paramMap	参数键值对
     * @param onclickJavaScript 单击触发脚本
     * @return
     */
    public static String buildHref(String hrefUrl, String title,
	    String altString, String target, Map<String, String> paramMap,
	    String onclickJavaScript) {
	String symbol = "\"";
	if (title.contains(symbol)) {
	    symbol = "'";
	}
	StringBuffer sb = new StringBuffer();
	sb.append("<a href=").append(symbol).append(hrefUrl);
	if ((paramMap != null) && (!paramMap.isEmpty())) {
	    boolean hasParam = false;
	    if ((hrefUrl.contains("?")) || (hrefUrl.contains("&"))) {
		hasParam = true;
	    }
	    if (!hasParam) {
		sb.append("?");
	    }
	    int i = 0;
	    for (Map.Entry<String, String> entry : paramMap.entrySet()) {
		if ((hasParam) || (i > 0)) {
		    sb.append("&");
		}
		sb.append(((String) entry.getKey()).replaceAll("\"", "'"))
			.append("=")
			.append(((String) entry.getValue()).replaceAll("\"",
				"'"));
		i++;
	    }
	}
	sb.append(symbol);
	if (StringUtil.isNotNullAndNotEmpty(onclickJavaScript)) {
	    sb.append(" onclick=").append(symbol).append(onclickJavaScript)
		    .append(symbol);
	}
	if (StringUtil.isNotNullAndNotEmpty(altString)) {
	    sb.append(" alt=").append(symbol).append(altString).append(symbol);
	}
	if (StringUtil.isNotNullAndNotEmpty(target)) {
	    sb.append(" target=").append(symbol).append(target).append(symbol);
	}
	sb.append(">").append(title).append("</a>");

	return sb.toString();
    }
    
    /**
     * 提取URL中的主机名
     * @param url URL地址
     * @return
     */
    public static String getHostname(String url) {
	String hostnamePort = getHostnamePort(url);
	int portIndex = hostnamePort.indexOf(":");
	if (portIndex > 0) {
	    return hostnamePort.substring(0, portIndex);
	}
	return hostnamePort;
    }
    
    /**
     * 提取URL中端口号
     * @param url URL地址
     * @return
     */
    public static int getPort(String url) {
	String hostnamePort = getHostnamePort(url);
	int portIndex = hostnamePort.indexOf(":");
	if (portIndex > 0) {
	    return Integer.valueOf(hostnamePort.substring(portIndex + 1))
		    .intValue();
	}
	if (url.trim().toLowerCase().startsWith("https://")) {
	    return 443;
	}
	return 80;
    }
    
    /**
     * 获取除HTTP://或者HTTPS://的URL的其余部分
     * @param url URL地址
     * @return
     */
    public static String getHostnamePort(String url) {
	int index = url.toLowerCase().indexOf("https://");
	if (index == 0) {
	    url = url.substring("https://".length());
	    index = url.indexOf("/");
	    if (index > 0) {
		return url.substring(0, index);
	    }
	    return url;
	}
	index = url.toLowerCase().indexOf("http://");
	if (index == 0) {
	    url = url.substring("http://".length());
	    index = url.indexOf("/");
	    if (index > 0) {
		return url.substring(0, index);
	    }
	    return url;
	}
	return "localhost";
    }
    
    /**
     * 提取网站URL
     * @param url URL路径
     * @return
     */
    public static String getSiteUrl(String url) {
	if (StringUtil.isNullOrEmpty(url)) {
	    return null;
	}
	String urlLower = url.toLowerCase();
	String urlContent = urlLower.replaceAll("http://", "").replaceAll(
		"https://", "");
	int i = urlContent.indexOf("/");
	if (i < 0) {
	    return url;
	}
	urlContent = urlContent.substring(0, i);
	if (urlLower.startsWith("https://")) {
	    return "https://" + urlContent;
	}
	return "http://" + urlContent;
    }
    
    /**
     * 添加HTML CSS StyleClass
     * @param html	HTML
     * @param sentence  HMTL中需要添加样式的节点部分
     * @param styleClsName 样式名称
     * @return
     */
    public static String addHtmlCssStyleClass(String html, String sentence,
	    String styleClsName) {
	return addHtmlCssStyle(html, sentence, null, styleClsName);
    }
    
    /**
     * 添加HTML CSS StyleDetail
     * @param html	HTML
     * @param sentence  HMTL中需要添加样式的节点部分
     * @param styleDetail 样式
     * @return
     */
    public static String addHtmlCssStyle(String html, String sentence,
	    String styleDetail) {
	return addHtmlCssStyle(html, sentence, styleDetail, null);
    }
    
    /**
     * 添加HTML CSS
     * @param html     		HTML
     * @param sentence		HMTL中需要添加样式的节点部分
     * @param styleDetail	样式
     * @param styleClsName	样式名称
     * @return
     */
    public static String addHtmlCssStyle(String html, String sentence,
	    String styleDetail, String styleClsName) {
	if (!StringUtil.isNullOrEmptyWithTrim(html)) {
	    StringBuffer styleBeginSb = new StringBuffer("<span");
	    if (StringUtil.isNotNullAndNotEmpty(styleClsName)) {
		styleBeginSb.append(" class='").append(styleClsName)
			.append("'");
	    }
	    if (StringUtil.isNotNullAndNotEmpty(styleDetail)) {
		styleDetail = styleDetail.replaceAll("'", "\\\"");
		styleBeginSb.append(" style='").append(styleDetail).append("'");
	    }
	    styleBeginSb.append(">");

	    String styleBegin = styleBeginSb.toString();
	    String styleEnd = "</span>";

	    StringBuffer sb = new StringBuffer();
	    String htmlContent = html;

	    int keyLen = sentence.length();
	    int index;
	    while ((index = htmlContent.indexOf(sentence)) >= 0) {
		String pre = htmlContent.substring(0, index);
		if (inTag(pre)) {
		    sb.append(pre).append(sentence);
		} else {
		    sb.append(pre).append(styleBegin).append(sentence)
			    .append(styleEnd);
		}
		htmlContent = htmlContent.substring(index + keyLen);
	    }
	    sb.append(htmlContent);

	    return sb.toString();
	}
	return "";
    }
    
    /**
     * 关键字高亮显示
     * @param content   文本内容
     * @param keywords  文本内容中的关键字
     * @param lightColor 高亮显示的颜色
     * @return
     */
    public static String highLightKeywords(String content, String keywords,
	    String lightColor) {
	if (!StringUtil.isNullOrEmptyWithTrim(content)) {
	    String colorBegin = String.format("<font color='%s'>",
		    new Object[] { lightColor });
	    String colorEnd = "</font>";
	    List<String> keywordList = FulltextUtil.getTermList(keywords);

	    StringBuffer sb = new StringBuffer();
	    String html = content;
	    for (String keyword : keywordList) {
		sb.setLength(0);
		int keyLen = keyword.length();
		StringBuffer preHtml = new StringBuffer();
		int index;
		while ((index = html.indexOf(keyword)) >= 0) {
		    String before = html.substring(0, index);
		    if (inTag(before, preHtml.toString())) {
			sb.append(before).append(keyword);
			preHtml.append(before);
		    } else {
			sb.append(before).append(colorBegin).append(keyword)
				.append(colorEnd);
			preHtml.setLength(0);
		    }
		    html = html.substring(index + keyLen);
		}
		sb.append(html);
		html = sb.toString();
	    }
	    return sb.toString();
	}
	return "";
    }

    private static final boolean inTag(String html, String preHtml) {
	if (inTag(preHtml + html)) {
	    return true;
	}
	return inTag(html);
    }

    private static final boolean inTag(String html) {
	int lastIndex = html.lastIndexOf("<");
	if (lastIndex < 0) {
	    return false;
	}
	if (lastIndex > html.lastIndexOf(">")) {
	    return true;
	}
	String lowerHtml = html.toLowerCase().trim();
	lastIndex = lowerHtml.lastIndexOf("<");
	if (lastIndex == lowerHtml.lastIndexOf("<title>")) {
	    return true;
	}
	if (lastIndex == lowerHtml.lastIndexOf("<link ")) {
	    return true;
	}
	if (lastIndex == lowerHtml.lastIndexOf("<script ")) {
	    return true;
	}
	return false;
    }
    
    /**
     * 包装HTML页面主内容
     * @param pageContent 页面主内容(无HTML基本标签)
     * @return
     */
    public static String wrapHtmlPageContent(String pageContent) {
	StringBuffer sb = new StringBuffer();
	sb.append("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">\r\n");
	sb.append("<html xmlns=\"http://www.w3.org/1999/xhtml\">\r\n");
	sb.append("<head>\r\n");
	sb.append("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\"/>\r\n");
	sb.append("</head>\r\n");
	sb.append("<body>\r\n");
	sb.append(pageContent);
	sb.append("</body>\r\n</html>");

	return sb.toString();
    }
    
    /**
     * 获取请求参数的键值对
     * @param request HttpServeltRequest请求
     * @return
     */
    public static Map<String, String> getRequestQueryStringMap(
	    HttpServletRequest request) {
	Map<String, String> paramMap = new HashMap<String,String>();
	String queryString = request.getQueryString();
	if (StringUtil.isNullOrEmpty(queryString)) {
	    return paramMap;
	}
	StringTokenizer st = new StringTokenizer(queryString, "&");
	while (st.hasMoreTokens()) {
	    String pairs = st.nextToken();
	    String key = pairs.substring(0, pairs.indexOf('='));
	    String value = pairs.substring(pairs.indexOf('=') + 1);
	    paramMap.put(key, value);
	}
	return paramMap;
    }
}
