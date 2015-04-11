package com.coscon.shipsuite.common.util.security;

import com.coscon.shipsuite.common.util.security.MessageDigester;
import com.coscon.shipsuite.common.util.string.IdGenerator;
import java.io.PrintStream;
import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;

public class MessageDigesterTest {
    @Before
    public void setUp() throws Exception {
    }

    @Test
    public void encrypt() {
	Assert.assertEquals("d41d8cd98f00b204e9800998ecf8427e",
		MessageDigester.hexMD5(""));
	Assert.assertEquals("0cc175b9c0f1b6a831c399e269772661",
		MessageDigester.hexMD5("a"));
	Assert.assertEquals("900150983cd24fb0d6963f7d28e17f72",
		MessageDigester.hexMD5("abc"));
	Assert.assertEquals("f96b697d7cb7938d525a2f31aaf161d0",
		MessageDigester.hexMD5("message digest"));
	Assert.assertEquals("c3fcd3d76192e4007dfb496cca67e13b",
		MessageDigester.hexMD5("abcdefghijklmnopqrstuvwxyz"));
	for (int i = 0; i < 4; i++) {
	    String[] userPass = { "123456" };
	    for (String pass : userPass) {
		System.out.println("uuid: " + IdGenerator.getUUID()
			+ "\tpass: " + pass + ": "
			+ MessageDigester.hexMD5(pass));
	    }
	}
    }
}
