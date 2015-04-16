package com.common.util.security;

import com.common.enums.SecurityType;
import com.common.util.file.FileUtil;
import com.common.util.hardware.SystemUtil;
import com.common.util.security.SecurityUtil;

import java.io.PrintStream;
import org.junit.Test;

public class SecurityUtilTest {
    @Test
    public void verifySign() {
	System.out.println(SecurityUtil.sign(SystemUtil.getHDSerial("C")
		+ SystemUtil.getCPUSerial(), SecurityType.RSA));

	System.out.println();

	System.out.println(SecurityUtil.sign("-3910579200FABFBFF000206F2",
		SecurityType.RSA));

	String LICENSE_FILE = "c:\\temp\\license.smu.txt";

	String licenseCode = "";
	try {
	    licenseCode = FileUtil.readFile(LICENSE_FILE);
	    System.out.println(licenseCode);
	} catch (Exception localException) {
	}
	String LICENSE_CODE = SecurityUtil.generateLicense();
	boolean ok = LICENSE_CODE.trim().equalsIgnoreCase(licenseCode.trim());
	System.out.println(ok);
    }

    @Test
    public void des() {
	String s = SecurityUtil.encryptDES("ashprod");
	System.out.println(s);
	System.out.println(SecurityUtil.decryptDES("pJpdEOGdEIg//j2mSFwdbA=="));
    }

    @Test
    public void md5() {
	String s = "G8VRQB";
	String signature = SecurityUtil.sign(s, SecurityType.MD5);
	System.out.println("signature:" + signature);
    }
}
