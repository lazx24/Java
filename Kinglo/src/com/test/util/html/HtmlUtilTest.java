package com.test.util.html;

import static org.junit.Assert.fail;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.coscon.shipsuite.common.util.html.HtmlUtil;

public class HtmlUtilTest {

    @Test
    public void testDetectCharset() {
	String url="http://www.baidu.com";
	try {
	    HtmlUtil.detectCharset(new URL(url));
	} catch (MalformedURLException e) {
	    e.printStackTrace();
	}
    }

    @Test
    public void testFixHttpUrl() {
	
    }

    @Test
    public void testFixHttpsUrl() {
	fail("Not yet implemented");
    }

    @Test
    public void testGetResponseHtmlString() {
	fail("Not yet implemented");
    }

    @Test
    public void testGetResponseHtmlStringString() {
	fail("Not yet implemented");
    }

    @Test
    public void testGetResponseHtmlStringMapOfStringString() {
	fail("Not yet implemented");
    }

    @Test
    public void testGetResponseHtmlStringMapOfStringStringString() {
	fail("Not yet implemented");
    }

    @Test
    public void testGetHrefMapString() {
	fail("Not yet implemented");
    }

    @Test
    public void testGetHref() {
	fail("Not yet implemented");
    }

    @Test
    public void testGetHrefMapStringString() {
	fail("Not yet implemented");
    }

    @Test
    public void testGetHrefMapURL() {
	fail("Not yet implemented");
    }

    @Test
    public void testParseUrlURL() {
	fail("Not yet implemented");
    }

    @Test
    public void testParseUrlURLStringBoolean() {
	fail("Not yet implemented");
    }

    @Test
    public void testRemoveHtmlSpace() {
	fail("Not yet implemented");
    }

    @Test
    public void testText2html() {
	fail("Not yet implemented");
    }

    @Test
    public void testHtml2textString() {
	fail("Not yet implemented");
    }

    @Test
    public void testHtml2textURL() {
	fail("Not yet implemented");
    }

    @Test
    public void testBuildHrefStringString() {
	Map map=new HashMap();
	map.put("a", "b");
	System.out.println(HtmlUtil.buildHref("http://www.baidu.com","我是共产党","a","_blank",map));
    }

   
    @Test
    public void testBuildHrefStringStringStringStringMapOfStringStringString() {
	Map map=new HashMap();
	map.put("a", "b");
	String onclick="alert(a);";
	System.out.println(HtmlUtil.buildHref("http://www.baidu.com","我是共产党","a","_blank",map,onclick));
    }

    @Test
    public void testGetHostname() {
	System.out.println(HtmlUtil.getHostname("http://www.baidu.com"));
    }

    @Test
    public void testGetPort() {
	System.out.println(HtmlUtil.getPort("http://www.baidu.com"));
    }

    @Test
    public void testGetSiteUrl() {
	System.out.println(HtmlUtil.getSiteUrl("sssfshttp://www.baidu.comsdfsfdsf"));
    }

    @Test
    public void testAddHtmlCssStyleClass() {
	System.out.println(HtmlUtil.addHtmlCssStyle("<p><span><a href='www.baidu.com'>111</a></span></p>", "<a href='www.baidu.com'>111</a>", "className"));
    }

    @Test
    public void testAddHtmlCssStyleStringStringString() {
	fail("Not yet implemented");
    }

    @Test
    public void testAddHtmlCssStyleStringStringStringString() {
	fail("Not yet implemented");
    }

    @Test
    public void testHighLightKeywords() {
	System.out.println(HtmlUtil.highLightKeywords("<span>我是共产党</span>", "我是共产党", "red"));
    }

    @Test
    public void testWrapHtmlPageContent() {
	fail("Not yet implemented");
    }

    @Test
    public void testGetRequestQueryStringMap() {
	fail("Not yet implemented");
    }

}
