package com.coscon.shipsuite.common.exception;

public final class SessionTimeoutException extends ShipSuiteRuntimeException {
    private static final long serialVersionUID = 1L;

    public SessionTimeoutException() {
	init();
    }

    public SessionTimeoutException(String msgKey) {
	super(msgKey);
    }

    public SessionTimeoutException(String msgKey, String[] msgArgs) {
	super(msgKey, msgArgs);
    }

    public SessionTimeoutException(String msgKey, String[] msgArgs,
	    Throwable cause) {
	super(msgKey, msgArgs, cause);
    }

    public SessionTimeoutException(String message, Throwable cause) {
	super(message, cause);
    }

    public SessionTimeoutException(Throwable cause) {
	super(cause);
	init();
    }

    private void init() {
	setMsgKey("Session timeout, please relogin.");
    }
}
