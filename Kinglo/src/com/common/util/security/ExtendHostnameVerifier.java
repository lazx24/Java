package com.common.util.security;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

public class ExtendHostnameVerifier implements HostnameVerifier {
    private static ExtendHostnameVerifier instance = new ExtendHostnameVerifier();

    public static ExtendHostnameVerifier getInstance() {
	return instance;
    }

    public boolean verify(String hostname, SSLSession session) {
	return true;
    }
}
