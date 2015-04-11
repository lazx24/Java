package com.coscon.shipsuite.common.result;

public class ExtjsFormResponse {
    private boolean success;
    private String msg = "";

    public boolean isSuccess() {
	return this.success;
    }

    public void setSuccess(boolean success) {
	this.success = success;
    }

    public ExtjsFormResponse() {
    }

    public ExtjsFormResponse(boolean success) {
	this.success = success;
    }

    public ExtjsFormResponse(boolean success, String msg) {
	this.success = success;
	this.msg = msg;
    }

    public String getMsg() {
	return this.msg;
    }

    public void setMsg(String msg) {
	this.msg = msg;
    }
}
