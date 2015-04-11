package com.coscon.shipsuite.common.enums;

import java.io.Serializable;

public enum SecurityAlgorithm implements Serializable {
    DES("DES"), MD5("MD5"), SHA("SHA"), RSA("RSA"), SHA1WithRSA("SHA1WithRSA"), MD5WithRSA(
	    "MD5WithRSA");

    private String algorithm;

    private SecurityAlgorithm(String algorithm) {
	this.algorithm = algorithm;
    }

    public String getAlgorithm() {
	return this.algorithm;
    }

    public void setAlgorithm(String algorithm) {
	this.algorithm = algorithm;
    }
}
