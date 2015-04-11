package com.coscon.shipsuite.common.enums;

public enum SecurityType {
    NONE("NONE"), SSL("SSL"), RSA("RSA"), DES("DES"), MD5("MD5");

    private String algorithm;

    private SecurityType(String algorithm) {
	this.algorithm = algorithm;
    }

    public String getAlgorithm() {
	return this.algorithm;
    }

    public void setAlgorithm(String algorithm) {
	this.algorithm = algorithm;
    }
}
