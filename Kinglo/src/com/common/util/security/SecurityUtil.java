package com.common.util.security;

import com.common.enums.SecurityType;
import com.common.exception.ShipSuiteRuntimeException;
import com.common.log.ISystemLogger;
import com.common.log.LoggerFactory;
import com.common.util.hardware.SystemUtil;
import com.common.util.string.StringUtil;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.security.KeyPair;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.UnrecoverableKeyException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateExpiredException;
import java.security.cert.CertificateNotYetValidException;
import java.security.cert.X509Certificate;
import java.util.Date;

public final class SecurityUtil {
    private static ISystemLogger log = LoggerFactory
	    .getSystemLogger(SecurityUtil.class);

    public static byte[] decryptDES(byte[] data) {
	return DESCrypto.decrypt(data);
    }

    public static String decryptDES(String data) {
	return DESCrypto.decrypt(data);
    }

    public static byte[] encryptDES(byte[] data) {
	return DESCrypto.encrypt(data);
    }

    public static String encryptDES(String data) {
	return DESCrypto.encrypt(data);
    }

    public static KeyPair getKeyPair() {
	return getKeyPair(SecurityConst.KEY_ALIAS, SecurityConst.KEY_PASSWORD,
		SecurityConst.KEY_STORE_FILE);
    }

    public static KeyPair getKeyPair(String keystoreAlias, String keyPassword,
	    String keyStorePath) {
	KeyPair keyPair = null;
	try {
	    InputStream fis = null;
	    try {
		fis = new FileInputStream(keyStorePath);
	    } catch (IOException e) {
		fis = Thread.currentThread().getContextClassLoader()
			.getResourceAsStream(keyStorePath);
	    }
	    KeyStore ks = KeyStore.getInstance("JKS");
	    char[] keyCharArray = keyPassword.toCharArray();
	    ks.load(fis, keyCharArray);

	    Certificate c = ks.getCertificate(keystoreAlias);

	    X509Certificate t = (X509Certificate) c;

	    byte[] sig = t.getSignature();

	    PrivateKey privateKey = (PrivateKey) ks.getKey(keystoreAlias,
		    keyCharArray);
	    PublicKey publicKey = t.getPublicKey();

	    keyPair = new KeyPair(publicKey, privateKey);

	    Date TimeNow = new Date();
	    t.checkValidity(TimeNow);
	} catch (CertificateExpiredException e) {
	    log.error(null, "证书的日期有效性检查:过期");
	    throw new ShipSuiteRuntimeException(e);
	} catch (CertificateNotYetValidException e) {
	    log.error(null, "证书的日期有效性检查:尚未生效");
	    throw new ShipSuiteRuntimeException(e);
	} catch (CertificateException e) {
	    log.error(e);
	    throw new ShipSuiteRuntimeException(e);
	} catch (FileNotFoundException e) {
	    log.error(e);
	    throw new ShipSuiteRuntimeException(e);
	} catch (IOException e) {
	    log.error(e);
	    throw new ShipSuiteRuntimeException(e);
	} catch (KeyStoreException e) {
	    log.error(e);
	    throw new ShipSuiteRuntimeException(e);
	} catch (NoSuchAlgorithmException e) {
	    log.error(e);
	    throw new ShipSuiteRuntimeException(e);
	} catch (UnrecoverableKeyException e) {
	    log.error(e);
	    throw new ShipSuiteRuntimeException(e);
	}
	return keyPair;
    }

    public static String getServiceSignKey(String userId) {
	StringBuilder sb = new StringBuilder();
	sb.append(userId);
	sb.append("|");
	sb.append(SecurityConst.KEY_PASSWORD);

	return sb.toString();
    }

    public static String getServiceSignKey(String userId, Long timestamp) {
	StringBuilder sb = new StringBuilder();
	sb.append(userId);
	sb.append("|");
	sb.append(timestamp);
	sb.append("|");
	sb.append(SecurityConst.KEY_PASSWORD);

	return sb.toString();
    }

    public static String hexMD5(String content) {
	return MessageDigester.hexMD5(content);
    }

    public static String hexMD5(byte[] content) {
	return MessageDigester.hexMD5(content);
    }

    public static String sign(SecurityType securityType) {
	return sign(SecurityConst.KEY_PASSWORD, securityType);
    }

    public static String sign(String securityType) {
	return sign(SecurityType.valueOf(securityType));
    }

    public static String sign(String content, SecurityType securityType) {
	switch (securityType) {
	case RSA:
	    return DESCrypto.encrypt(content);
	case SSL:
	    return MessageDigester.hexMD5(content);
	case MD5:
	case NONE:
	    PrivateKey privateKey = getKeyPair(SecurityConst.KEY_ALIAS,
		    SecurityConst.KEY_PASSWORD, SecurityConst.KEY_STORE_FILE)
		    .getPrivate();
	    if (privateKey == null) {
		throw new RuntimeException("Not found private key!");
	    }
	    return DigitalSignatureRSA.sign(content, privateKey);
	case DES:
	    return content;
	}
	throw new RuntimeException("Not support this security type.");
    }

    public static String sign(String content, String securityType) {
	return sign(content, SecurityType.valueOf(securityType));
    }

    public static Boolean verify(String content, String signature,
	    SecurityType securityType) {
	boolean isPass = false;
	switch (securityType) {
	case RSA:
	    isPass = verifyDES(content, signature).booleanValue();
	    break;
	case SSL:
	    isPass = verifyMD5(content, signature).booleanValue();
	    break;
	case NONE:
	    KeyPair keyPair = getKeyPair();
	    if (keyPair == null) {
		throw new RuntimeException("Can not found public key!");
	    }
	    PublicKey publicKey = keyPair.getPublic();
	    isPass = verifyRSA(content, signature, publicKey).booleanValue();
	}
	return Boolean.valueOf(isPass);
    }

    public static Boolean verify(String content, String signature,
	    String securityType) {
	return verify(content, signature, SecurityType.valueOf(securityType));
    }

    public static Boolean verifyDES(String content, String encryptedContent) {
	return Boolean.valueOf(DESCrypto.encrypt(content).equals(
		encryptedContent));
    }

    public static Boolean verifyMD5(String content, String encryptedContent) {
	return Boolean.valueOf(MessageDigester.hexMD5(content).equals(
		encryptedContent));
    }

    public static Boolean verifyRSA(String content, String signature,
	    PublicKey publicKey) {
	return Boolean.valueOf(DigitalSignatureRSA.verify(content, signature,
		publicKey));
    }

    public static void main(String[] args) {
	System.out.println(generateLicense());
    }

    public static String generateLicense() {
	String license = "";
	try {
	    license = sign(
		    SystemUtil.getHDSerial("C") + SystemUtil.getCPUSerial(),
		    SecurityType.RSA);
	} catch (Throwable localThrowable) {
	}
	return license;
    }

    private static String LICENSE_CODE = generateLicense();
    private static String[] LICENSE_ENV_ARRAY = { "smu" };

    public static boolean checkLicense(String deployEnv, String licenseCode) {
	for (String licenseEnv : LICENSE_ENV_ARRAY) {
	    if (licenseEnv.equalsIgnoreCase(deployEnv)) {
		if (StringUtil.isNullOrEmpty(licenseCode)) {
		    return false;
		}
		return LICENSE_CODE.trim().equals(licenseCode.trim());
	    }
	}
	return true;
    }
}
