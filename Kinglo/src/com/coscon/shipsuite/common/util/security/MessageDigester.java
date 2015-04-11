package com.coscon.shipsuite.common.util.security;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

public abstract class MessageDigester {
    public static MessageDigest instanceSHA1;
    public static MessageDigest instanceMD5;

    static {
	try {
	    instanceSHA1 = MessageDigest.getInstance("SHA1");
	    instanceMD5 = MessageDigest.getInstance("MD5");
	} catch (NoSuchAlgorithmException e) {
	    throw new RuntimeException(e);
	}
    }

    private static BASE64Encoder bASE64Encoder = new BASE64Encoder();

    public static String base64(byte[] b) {
	String result = bASE64Encoder.encode(b);

	return result;
    }

    public static String base64MD5(String msg) {
	String result = base64(MD5(msg));
	int i = result.indexOf('=');
	if (i > -1) {
	    result = result.substring(0, i);
	}
	return result;
    }

    public static String base64SHA1(String msg) {
	String result = base64(SHA1(msg));
	int i = result.indexOf('=');
	if (i > -1) {
	    result = result.substring(0, i);
	}
	return result;
    }

    public static byte[] base64ToByte(String BASE64StringData) {
	BASE64Decoder dec = new BASE64Decoder();
	byte[] byteData = (byte[]) null;
	try {
	    byteData = dec.decodeBuffer(BASE64StringData);
	} catch (IOException e1) {
	    e1.printStackTrace();
	}
	return byteData;
    }

    public static String hex(byte[] b) {
	StringBuffer result = new StringBuffer(b.length);
	for (int i = 0; i < b.length; i++) {
	    String hex = Integer.toHexString(b[i] & 0xFF);
	    if (hex.length() == 1) {
		result.append("0").append(hex);
	    } else {
		result.append(hex);
	    }
	}
	return result.toString();
    }

    public static String hexMD5(String msg) {
	return hex(MD5(msg));
    }

    public static String hexMD5(byte[] msg) {
	return hex(MD5(msg));
    }

    public static String hexSHA1(String msg) {
	return hex(SHA1(msg));
    }

    public static byte[] MD5(String msg) {
	return instanceMD5.digest(msg.getBytes());
    }

    public static byte[] MD5(byte[] msg) {
	return instanceMD5.digest(msg);
    }

    public static byte[] SHA1(String msg) {
	return instanceSHA1.digest(msg.getBytes());
    }
}
