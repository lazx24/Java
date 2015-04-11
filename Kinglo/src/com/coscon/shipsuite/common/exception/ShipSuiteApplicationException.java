package com.coscon.shipsuite.common.exception;

import com.coscon.shipsuite.common.util.string.IMessageHelper;
import com.coscon.shipsuite.common.util.string.MessageHelper;

public class ShipSuiteApplicationException extends Exception {
    private static final long serialVersionUID = 1L;
    protected String msgKey;
    protected String[] msgArgs;
    protected IMessageHelper messageHelper;

    public ShipSuiteApplicationException() {
    }

    public ShipSuiteApplicationException(String msgKey) {
	super(msgKey);
	this.msgKey = msgKey;
    }

    public ShipSuiteApplicationException(String msgKey, String[] msgArgs) {
	super(msgKey);
	this.msgKey = msgKey;
	this.msgArgs = msgArgs;
    }

    public ShipSuiteApplicationException(String msgKey, String[] msgArgs,
	    Throwable cause) {
	super(msgKey, cause);
	this.msgKey = msgKey;
	this.msgArgs = msgArgs;
    }

    public ShipSuiteApplicationException(String message, Throwable cause) {
	this(message, null, cause);
    }

    public ShipSuiteApplicationException(Throwable cause) {
	this(null, cause);
    }

    public String getMessage() {
	if (this.messageHelper == null) {
	    this.messageHelper = MessageHelper.getInstance();
	}
	try {
	    if (this.msgKey == null) {
		return super.getMessage();
	    }
	    return this.messageHelper.getMessageString(this.msgKey,
		    new String[0]);
	} catch (Throwable ex) {
	}
	return this.msgKey;
    }

    public IMessageHelper getMessageHelper() {
	return this.messageHelper;
    }

    public String[] getMsgArgs() {
	return this.msgArgs;
    }

    public String getMsgKey() {
	return this.msgKey;
    }

    public void setMessageHelper(IMessageHelper messageHelper) {
	this.messageHelper = messageHelper;
    }

    public void setMsgArgs(String[] msgArgs) {
	this.msgArgs = msgArgs;
    }

    public void setMsgKey(String msgKey) {
	this.msgKey = msgKey;
    }
}
