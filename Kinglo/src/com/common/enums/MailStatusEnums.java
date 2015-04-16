package com.common.enums;

import java.io.Serializable;

public enum MailStatusEnums implements Serializable {
    WAITING("WAITING"), ERROR("ERROR"), PROCESSING("PROCESSING"), SUCCESS(
	    "SUCCESS");

    private String status;

    private MailStatusEnums(String status) {
	this.status = status;
    }

    public String getStatus() {
	return this.status;
    }
}
