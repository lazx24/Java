package com.common.util.security;

import com.common.util.security.DESCrypto;

import java.io.PrintStream;
import org.junit.Assert;
import org.junit.Test;

public class DESCryptoTest {
    @Test
    public void testByteDES() {
	String sdata = "Hello World!Hello W";
	byte[] data = sdata.getBytes();
	System.out.println("Original data : " + new String(data));
	byte[] result = DESCrypto.encrypt(data);

	System.out.println("Encrypted data: " + new String(result));
	data = DESCrypto.decrypt(result);

	System.out.println("Original data : " + new String(data));
	Assert.assertEquals(sdata, new String(data));
    }

    @Test
    public void testStringDES() {
	String data = "study_ebuser";
	System.out.println("Original data : " + data);
	String result = DESCrypto.encrypt(data);

	System.out.println("Encrypted data: " + result);
	String newdata = DESCrypto.decrypt(result);

	System.out.println("Original data : " + data);
	Assert.assertEquals(data, newdata);
    }
}
