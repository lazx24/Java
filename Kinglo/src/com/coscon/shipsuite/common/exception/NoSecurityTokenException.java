package com.coscon.shipsuite.common.exception;

public final class NoSecurityTokenException extends ShipSuiteRuntimeException {
    private static final long serialVersionUID = 1L;

    public NoSecurityTokenException() {
	init();
    }

    public NoSecurityTokenException(Throwable cause) {
	super(cause);
	init();
    }

    private void init() {
	setMsgKey("Not found any valid security token.");
    }
}
