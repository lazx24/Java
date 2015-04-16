package com.test.util.html;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import org.junit.Test;

import com.common.util.html.WebEncoding;

public class WebEncodingTest {

    @Test
    public void testGetCharset() {
	try {
	    System.out.println(WebEncoding.getCharset("http://www.baidu.com"));
	} catch (IOException e) {
	    e.printStackTrace();
	}
    }

    @Test
    public void testGetFileEncoding() {
	try {
	    System.out.println(WebEncoding.getFileEncoding(new URL("D:\\a.txt")));
	} catch (MalformedURLException e) {
	    e.printStackTrace();
	} catch (IOException e) {
	    e.printStackTrace();
	}
    }

}
