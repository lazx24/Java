package com.common.util.security;

import com.common.context.ApplicationContextUtil;
import com.common.exception.ShipSuiteRuntimeException;

import java.io.FileInputStream;
import java.net.URL;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

public class ExtendX509TrustManager implements X509TrustManager {
    X509TrustManager sunJSSEX509TrustManager;
    private static ExtendX509TrustManager instance = new ExtendX509TrustManager();

    public static ExtendX509TrustManager getInstance(String keyStoreDirectory) {
	return instance;
    }

    private ExtendX509TrustManager() {
	try {
	    KeyStore ks = KeyStore.getInstance("JKS");
	    ks.load(new FileInputStream(ApplicationContextUtil
		    .getSystemInfo("user.home")
		    + "/"
		    + SecurityConst.KEY_STORE_FILE), SecurityConst.KEY_PASSWORD
		    .toCharArray());
	    TrustManagerFactory tmf = TrustManagerFactory.getInstance(
		    "SunX509", "SunJSSE");
	    tmf.init(ks);
	    TrustManager[] tms = tmf.getTrustManagers();
	    for (int i = 0; i < tms.length; i++) {
		if ((tms[i] instanceof X509TrustManager)) {
		    this.sunJSSEX509TrustManager = ((X509TrustManager) tms[i]);
		    return;
		}
	    }
	} catch (Exception ex) {
	    throw new ShipSuiteRuntimeException("Couldn't initialize", ex);
	}
    }

    public void checkClientTrusted(X509Certificate[] chain, String authType)
	    throws CertificateException {
	try {
	    this.sunJSSEX509TrustManager.checkClientTrusted(chain, authType);
	} catch (CertificateException localCertificateException) {
	}
    }

    public void checkServerTrusted(X509Certificate[] chain, String authType)
	    throws CertificateException {
	try {
	    this.sunJSSEX509TrustManager.checkServerTrusted(chain, authType);
	} catch (CertificateException localCertificateException) {
	}
    }

    public X509Certificate[] getAcceptedIssuers() {
	return this.sunJSSEX509TrustManager.getAcceptedIssuers();
    }

    public HttpsURLConnection getHttpsURLConnection(String httpsUrl) {
	try {
	    HostnameVerifier hnv = ExtendHostnameVerifier.getInstance();
	    HttpsURLConnection.setDefaultHostnameVerifier(hnv);

	    TrustManager[] tm = { instance };
	    SSLContext sslContext = SSLContext.getInstance("SSL", "SunJSSE");
	    sslContext.init(null, tm, new SecureRandom());

	    SSLSocketFactory ssf = sslContext.getSocketFactory();

	    URL myURL = new URL(httpsUrl);

	    HttpsURLConnection httpsConn = (HttpsURLConnection) myURL
		    .openConnection();
	    httpsConn.setSSLSocketFactory(ssf);

	    return httpsConn;
	} catch (Exception ex) {
	    throw new ShipSuiteRuntimeException(ex);
	}
    }
}
