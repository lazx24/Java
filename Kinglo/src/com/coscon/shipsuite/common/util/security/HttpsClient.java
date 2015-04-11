package com.coscon.shipsuite.common.util.security;

import com.coscon.shipsuite.common.context.ApplicationContextUtil;
import java.io.FileInputStream;
import java.net.URL;
import java.security.GeneralSecurityException;
import java.security.KeyStore;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;

public class HttpsClient {
    public static KeyStore getKeyStore(String password, String keyStorePath)
	    throws Exception {
	KeyStore ks = KeyStore.getInstance("JKS");

	FileInputStream is = new FileInputStream(keyStorePath);

	ks.load(is, password.toCharArray());

	is.close();
	return ks;
    }

    public static SSLContext getSSLContext(String password,
	    String keyStorePath, String trustStorePath) throws Exception {
	KeyManagerFactory keyManagerFactory = KeyManagerFactory
		.getInstance(KeyManagerFactory.getDefaultAlgorithm());

	KeyStore keyStore = getKeyStore(password, keyStorePath);

	keyManagerFactory.init(keyStore, password.toCharArray());

	TrustManagerFactory trustManagerFactory = TrustManagerFactory
		.getInstance(TrustManagerFactory.getDefaultAlgorithm());

	KeyStore trustStore = getKeyStore(password, trustStorePath);

	trustManagerFactory.init(trustStore);

	SSLContext ctx = SSLContext.getInstance("TLS");

	ctx.init(keyManagerFactory.getKeyManagers(),
		trustManagerFactory.getTrustManagers(), null);

	return ctx;
    }

    public static void initHttpsURLConnection(String password,
	    String keyStorePath, String trustStorePath) throws Exception {
	SSLContext sslContext = null;

	HostnameVerifier hnv = ExtendHostnameVerifier.getInstance();
	try {
	    sslContext = getSSLContext(password, keyStorePath, trustStorePath);
	} catch (GeneralSecurityException e) {
	    e.printStackTrace();
	}
	if (sslContext != null) {
	    HttpsURLConnection.setDefaultSSLSocketFactory(sslContext
		    .getSocketFactory());
	}
	HttpsURLConnection.setDefaultHostnameVerifier(hnv);
    }

    public static HttpsURLConnection getHttpsURLConnection(String httpsUrl)
	    throws Exception {
	initHttpsURLConnection(SecurityConst.KEY_PASSWORD,
		ApplicationContextUtil.getSystemInfo("user.home") + "/"
			+ SecurityConst.KEY_STORE_FILE,
		SecurityConst.KEY_STORE_FILE);

	return (HttpsURLConnection) new URL(httpsUrl).openConnection();
    }
}
