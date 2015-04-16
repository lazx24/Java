package com.common.util.security;

import com.common.util.security.DigitalSignatureRSA;

import java.io.PrintStream;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import org.junit.Test;

public class DigitalSignatureRSATest {
    @Test
    public void validate() throws NoSuchAlgorithmException,
	    InvalidKeyException, SignatureException {
	StringBuilder sb = new StringBuilder("这是一个测试的文本");
	for (int i = 0; i < 100; i++) {
	    sb.append("文本文本文本文本文本文本文本文本文本文本").append(i);
	}
	String inputText = sb.toString();
	KeyPair key = DigitalSignatureRSA.generateKeyPair(1024);
	System.out.println("Finish generating RSA key");
	System.out.println("private key:" + key.getPrivate());
	System.out.println("public key:" + key.getPublic());

	String signature = DigitalSignatureRSA
		.sign(inputText, key.getPrivate());
	System.out.println("signature:" + signature);

	System.out.println("\nStart signature verification");
	boolean isVerified = DigitalSignatureRSA.verify(inputText, signature,
		key.getPublic());
	System.out.println("Verify result:" + isVerified);
    }
}
