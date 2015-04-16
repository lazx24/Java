package com.common.result;

import com.common.util.security.SecurityUtil;
import com.common.util.string.StringUtil;
import com.common.util.zip.ZipUtil;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class OperationResult<V> implements Serializable {
    private static final long serialVersionUID = 3621893497549016287L;
    private String errorMessageKey = "";
    private boolean operationSuccess = false;
    private V result;
    private byte[] zipResult;
    private String[] msgArgs;
    private List<ResultMessageKeyInfo> messageKeyInfoList;
    private boolean isZip = false;
    private String orginalZipResultMd5 = null;

    public OperationResult() {
	this(null, false, "", true, null);
    }

    public OperationResult(boolean operationSuccess) {
	this(null, false, null, operationSuccess, null);
    }

    public OperationResult(String errorMessageKey) {
	this(null, false, errorMessageKey, false, null);
    }

    public OperationResult(String errorMessageKey, String msgArg) {
	this(null, false, errorMessageKey, false, new String[] { msgArg });
    }

    public OperationResult(String errorMessageKey, String[] msgArgs) {
	this(null, false, errorMessageKey, false, msgArgs);
    }

    public OperationResult(V result) {
	this(result, false, "", true, null);
    }

    public OperationResult(V result, boolean isZip) {
	this(result, isZip, "", true, null);
    }

    protected OperationResult(V result, boolean isZip, String errorMessageKey,
	    boolean operationSuccess, String[] msgArgs) {
	setResult(result, isZip);

	this.errorMessageKey = errorMessageKey;
	this.operationSuccess = operationSuccess;
	this.msgArgs = msgArgs;
    }

    public void addMessageKeyInfo(ResultMessageKeyInfo messageKeyInfo) {
	if (messageKeyInfo == null) {
	    return;
	}
	if (this.messageKeyInfoList == null) {
	    this.messageKeyInfoList = new ArrayList();
	}
	this.messageKeyInfoList.add(messageKeyInfo);
    }

    public OperationResult<Object> convertToGeneric() {
	OperationResult<Object> operationResult = new OperationResult();

	operationResult.setErrorMessageKey(getErrorMessageKey());
	operationResult.setMsgArgs(getMsgArgs());
	operationResult.setResult(getResult());
	operationResult.setMessageKeyInfoList(getMessageKeyInfoList());
	operationResult.setOperationSuccess(isOperationSuccess());

	return operationResult;
    }

    public String getErrorMessageKey() {
	return this.errorMessageKey;
    }

    public List<ResultMessageKeyInfo> getMessageKeyInfoList() {
	return this.messageKeyInfoList;
    }

    public String[] getMsgArgs() {
	return this.msgArgs;
    }

    public V getResult() {
	if (this.result != null) {
	    return this.result;
	}
	if (this.isZip) {
	    if (this.zipResult == null) {
		return null;
	    }
	    this.result = (V)ZipUtil.decompressObject(this.zipResult);
	}
	return this.result;
    }

    public boolean hasMsgArgs() {
	return this.msgArgs != null;
    }

    public boolean isOperationSuccess() {
	return this.operationSuccess;
    }

    public void removeMessageKeyInfo(ResultMessageKeyInfo messageKeyInfo) {
	if (messageKeyInfo == null) {
	    return;
	}
	if ((this.messageKeyInfoList == null)
		|| (this.messageKeyInfoList.size() == 0)) {
	    return;
	}
	this.messageKeyInfoList.remove(messageKeyInfo);
    }

    public void setErrorMessageKey(String errorMessageKey) {
	this.errorMessageKey = errorMessageKey;
	if (StringUtil.isNotNullAndNotEmpty(errorMessageKey)) {
	    this.operationSuccess = false;
	}
    }

    public void setMessageKeyInfoList(
	    List<ResultMessageKeyInfo> messageKeyInfoList) {
	this.messageKeyInfoList = messageKeyInfoList;
    }

    public void setMsgArgs(String[] msgArgs) {
	this.msgArgs = msgArgs;
    }

    public void setOperationSuccess(boolean operationSuccess) {
	this.operationSuccess = operationSuccess;
    }

    public void setResult(V value, boolean isZip) {
	this.isZip = isZip;
	if (isZip) {
	    this.result = null;
	    if (value == null) {
		this.zipResult = null;
	    } else {
		this.zipResult = ZipUtil.compressObject(value);
		this.orginalZipResultMd5 = SecurityUtil.hexMD5(this.zipResult);
	    }
	} else {
	    this.result = value;
	    this.zipResult = null;
	}
    }

    public void setZipResult(byte[] zipResult) {
	if (zipResult != null) {
	    this.zipResult = zipResult;
	    this.result = null;
	    this.isZip = true;
	    this.orginalZipResultMd5 = SecurityUtil.hexMD5(this.zipResult);
	}
    }

    public void setResult(V value) {
	setResult(value, false);
    }

    public String toString() {
	String TAB = "    ";

	String retValue = "";

	retValue = "OperationResult ( " + super.toString() + "    "
		+ "errorMessageKey = " + this.errorMessageKey + "    "
		+ "operationResult = " + this.operationSuccess + "    " + " )";

	return retValue;
    }

    public boolean isZip() {
	return this.isZip;
    }

    public byte[] getZipResult() {
	return this.zipResult;
    }
}
