package com.common.util.security;

import com.common.enums.SecurityAlgorithm;

import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.SignatureException;

public final class DigitalSignatureRSA {
    private static Signature sig;
    private static String keyAlgorithm = SecurityAlgorithm.RSA.getAlgorithm();
    private static String signAlgorithm = SecurityAlgorithm.MD5WithRSA
	    .getAlgorithm();

    static {
	try {
	    sig = Signature.getInstance(signAlgorithm);
	} catch (NoSuchAlgorithmException e) {
	    throw new RuntimeException(e);
	}
    }

    public static KeyPair generateKeyPair(int securityBitsLength) {
	KeyPairGenerator keyGen;
	try {
	    keyGen = KeyPairGenerator.getInstance(keyAlgorithm);
	} catch (NoSuchAlgorithmException e) {
	    throw new RuntimeException(e);
	}
	keyGen.initialize(securityBitsLength);

	return keyGen.generateKeyPair();
    }

    public static String sign(String plainText, PrivateKey privateKey) {
	String signature = plainText;
	try {
	    sig.initSign(privateKey);

	    sig.update(plainText.getBytes());
	    byte[] signatureByteArray = sig.sign();

	    signature = MessageDigester.base64(signatureByteArray);
	} catch (InvalidKeyException e) {
	    throw new RuntimeException(e);
	} catch (SignatureException e) {
	    throw new RuntimeException(e);
	}
	return signature;
    }

    public static boolean verify(String plainText, String signature,
	    PublicKey publicKey) {
	try {
	    sig.initVerify(publicKey);

	    sig.update(plainText.getBytes());

	    return sig.verify(MessageDigester.base64ToByte(signature));
	} catch (Exception e) {
	}
	return false;
    }
}
