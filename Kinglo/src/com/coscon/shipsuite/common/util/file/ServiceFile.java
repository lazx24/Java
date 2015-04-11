package com.coscon.shipsuite.common.util.file;

import com.coscon.shipsuite.common.util.string.StringUtil;
import java.io.File;
import java.io.Serializable;

public class ServiceFile implements Serializable {
    private static final long serialVersionUID = -7006485127422982437L;
    private String filename;
    private byte[] data;
    private String localFilename;

    public ServiceFile() {
    }

    public ServiceFile(String filename) {
	this.filename = filename;
    }

    public ServiceFile(File file) {
	this.filename = file.getName();
	this.data = FileUtil.readBytesFromFile(file);
    }

    public ServiceFile(String filename, byte[] data) {
	this.filename = filename;
	this.data = data;
    }

    public byte[] getData() {
	return this.data;
    }

    public String getFilename() {
	return this.filename;
    }

    public String getLocalFilename() {
	return this.localFilename;
    }

    public void setData(byte[] data) {
	this.data = data;
    }

    public void setFilename(String name) {
	this.filename = name;
	if (this.filename != null) {
	    int i = this.filename.lastIndexOf("/");
	    if ((i >= 0) && (i < this.filename.length())) {
		this.filename = this.filename.substring(i + 1);
	    }
	    i = this.filename.lastIndexOf("\\");
	    if ((i >= 0) && (i < this.filename.length())) {
		this.filename = this.filename.substring(i + 1);
	    }
	    if (StringUtil.isNullOrEmpty(this.localFilename)) {
		this.localFilename = name;
	    }
	}
    }

    public void setLocalFilename(String localFilename) {
	this.localFilename = localFilename;
    }
}
