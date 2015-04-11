package com.coscon.shipsuite.common.util.security;

import com.coscon.shipsuite.common.enums.SecurityAlgorithm;
import com.sun.crypto.provider.SunJCE;
import java.security.InvalidKeyException;
import java.security.Security;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

public final class DESCrypto {
    private static SecretKey secretKey;
    private static String algorithm = SecurityAlgorithm.DES.getAlgorithm();
    private static Cipher cipher;

    static {
	Security.addProvider(new SunJCE());
	try {
	    byte[] byteKey = SecurityConst.KEY_PASSWORD.getBytes();
	    DESKeySpec desKS = new DESKeySpec(byteKey);

	    secretKey = SecretKeyFactory.getInstance(algorithm).generateSecret(
		    desKS);

	    cipher = Cipher.getInstance(algorithm);
	} catch (Exception e) {
	    throw new RuntimeException(e);
	}
    }

    public static byte[] decrypt(byte[] data) {
	if (data == null) {
	    return null;
	}
	byte[] original = (byte[]) null;
	try {
	    cipher.init(2, secretKey);
	    original = cipher.doFinal(data);
	} catch (IllegalBlockSizeException e) {
	    throw new RuntimeException(e);
	} catch (BadPaddingException e) {
	    throw new RuntimeException(e);
	} catch (InvalidKeyException e) {
	    throw new RuntimeException(e);
	}
	return original;
    }

    public static String decrypt(String data) {
	if (data == null) {
	    return null;
	}
	byte[] byteData = MessageDigester.base64ToByte(data);
	byte[] byteResult = decrypt(byteData);
	return new String(byteResult);
    }

    public static byte[] encrypt(byte[] data) {
	if (data == null) {
	    return null;
	}
	byte[] result = (byte[]) null;
	try {
	    cipher.init(1, secretKey);
	    result = cipher.doFinal(data);
	} catch (IllegalBlockSizeException e) {
	    throw new RuntimeException(e);
	} catch (BadPaddingException e) {
	    throw new RuntimeException(e);
	} catch (InvalidKeyException e) {
	    throw new RuntimeException(e);
	}
	return result;
    }

    public static String encrypt(String data) {
	if (data == null) {
	    return null;
	}
	byte[] byteData = data.getBytes();
	byte[] byteResult = encrypt(byteData);

	return MessageDigester.base64(byteResult);
    }
}
