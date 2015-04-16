package com.common.result;

public class ResponseResult {
    private boolean success;
    private String msg = "";

    public boolean isSuccess() {
	return this.success;
    }

    public void setSuccess(boolean success) {
	this.success = success;
    }

    public ResponseResult() {
    }

    public ResponseResult(boolean success) {
	this.success = success;
    }

    public ResponseResult(boolean success, String msg) {
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
