package com.coscon.shipsuite.common.exception;

import com.coscon.shipsuite.common.util.string.IMessageHelper;
import com.coscon.shipsuite.common.util.string.MessageHelper;

public class ShipSuiteRuntimeException extends RuntimeException {
    private static final long serialVersionUID = 1L;
    protected String msgKey;
    protected String[] msgArgs;
    protected IMessageHelper messageHelper;

    public ShipSuiteRuntimeException() {
    }

    public ShipSuiteRuntimeException(String msgKey) {
	this(msgKey, null, null);
    }

    public ShipSuiteRuntimeException(String msgKey, String[] msgArgs) {
	this(msgKey, msgArgs, null);
    }

    public ShipSuiteRuntimeException(String msgKey, String[] msgArgs,
	    Throwable cause) {
	super(msgKey, cause);
	this.msgKey = msgKey;
	this.msgArgs = msgArgs;
    }

    public ShipSuiteRuntimeException(String message, Throwable cause) {
	this(message, null, cause);
    }

    public ShipSuiteRuntimeException(Throwable cause) {
	this(null, cause);
	setMsgKey(cause.getMessage());
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
		    this.msgArgs);
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
