package com.common.exception;

public final class ValidationException extends ShipSuiteRuntimeException {
    
    private static final long serialVersionUID = 7928336037021516156L;

    public ValidationException(String msgKey) {
	super(msgKey);
    }

    public ValidationException(String msgKey, String[] msgArgs) {
	super(msgKey, msgArgs);
    }

    public ValidationException(String msgKey, String[] msgArgs,
	    Throwable throwable) {
	super(msgKey, msgArgs, throwable);
    }
}
