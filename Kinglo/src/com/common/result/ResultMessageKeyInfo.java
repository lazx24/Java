package com.common.result;

import java.io.Serializable;

public class ResultMessageKeyInfo implements Serializable {
    private static final long serialVersionUID = 5184637501487507866L;
    private String messageKey;
    private String[] messageArgs;

    public ResultMessageKeyInfo() {
    }

    public ResultMessageKeyInfo(String messageKey) {
	this.messageKey = messageKey;
    }

    public ResultMessageKeyInfo(String messageKey, String[] messageArgs) {
	this.messageKey = messageKey;
	this.messageArgs = messageArgs;
    }

    public String[] getMessageArgs() {
	return this.messageArgs;
    }

    public String getMessageKey() {
	return this.messageKey;
    }

    public void setMessageArgs(String[] messageArgs) {
	this.messageArgs = messageArgs;
    }

    public void setMessageKey(String messageKey) {
	this.messageKey = messageKey;
    }
}
