package com.coscon.shipsuite.common.util.dump;

import com.coscon.shipsuite.common.enums.ProcessStatusEnums;
import com.coscon.shipsuite.common.util.file.FileUtil;
import com.coscon.shipsuite.common.util.string.IdGenerator;
import java.io.File;
import java.io.Serializable;
import java.util.Date;

public class DbDump implements Serializable {
    private static final long serialVersionUID = 1L;
    private String uuid = IdGenerator.getUUID();
    private String sachemaUserName;
    private String dumpFilename;
    private String dumpFilePath;
    private Date dumpDate;
    private ProcessStatusEnums status;
    private long dumpBytesSize;
    private String remark;
    private String dumpLog;

    public String getSachemaUserName() {
	return this.sachemaUserName;
    }

    public void setSachemaUserName(String sachemaUserName) {
	this.sachemaUserName = sachemaUserName;
    }

    public String getDumpFilename() {
	return this.dumpFilename;
    }

    public void setDumpFilename(String dumpFilename) {
	this.dumpFilename = dumpFilename;
    }

    public String getDumpFilePath() {
	return this.dumpFilePath;
    }

    public void setDumpFilePath(String dumpFilePath) {
	this.dumpFilePath = dumpFilePath;
    }

    public Date getDumpDate() {
	return this.dumpDate;
    }

    public void setDumpDate(Date dumpDate) {
	this.dumpDate = dumpDate;
    }

    public ProcessStatusEnums getStatus() {
	return this.status;
    }

    public void setStatus(ProcessStatusEnums status) {
	this.status = status;
    }

    public long getDumpBytesSize() {
	File file = new File(this.dumpFilePath + "/" + this.dumpFilename);
	if (file.exists()) {
	    this.dumpBytesSize = file.length();
	} else {
	    this.dumpBytesSize = 0L;
	}
	return this.dumpBytesSize;
    }

    public String getDumpByteSizeString() {
	return FileUtil.convertFileSize(getDumpBytesSize());
    }

    public void setDumpByteSizeString(String s) {
    }

    public void setDumpBytesSize(long dumpBytesSize) {
	this.dumpBytesSize = dumpBytesSize;
    }

    public String getUuid() {
	return this.uuid;
    }

    public void setUuid(String uuid) {
	this.uuid = uuid;
    }

    public String getRemark() {
	return this.remark;
    }

    public void setRemark(String remark) {
	this.remark = remark;
    }

    public String getDumpLog() {
	return this.dumpLog;
    }

    public void setDumpLog(String dumpLog) {
	this.dumpLog = dumpLog;
    }

    public int hashCode() {
	int result = 1;
	result = 31 * result
		+ (this.dumpDate == null ? 0 : this.dumpDate.hashCode());
	result = 31
		* result
		+ (this.dumpFilePath == null ? 0 : this.dumpFilePath.hashCode());
	result = 31
		* result
		+ (this.dumpFilename == null ? 0 : this.dumpFilename.hashCode());
	result = 31 * result
		+ (this.remark == null ? 0 : this.remark.hashCode());
	result = 31
		* result
		+ (this.sachemaUserName == null ? 0 : this.sachemaUserName
			.hashCode());
	result = 31 * result
		+ (this.status == null ? 0 : this.status.hashCode());
	result = 31 * result + (this.uuid == null ? 0 : this.uuid.hashCode());
	return result;
    }

    public boolean equals(Object obj) {
	if (this == obj) {
	    return true;
	}
	if (obj == null) {
	    return false;
	}
	if (getClass() != obj.getClass()) {
	    return false;
	}
	DbDump other = (DbDump) obj;
	if (this.dumpDate == null) {
	    if (other.dumpDate != null) {
		return false;
	    }
	} else if (!this.dumpDate.equals(other.dumpDate)) {
	    return false;
	}
	if (this.dumpFilePath == null) {
	    if (other.dumpFilePath != null) {
		return false;
	    }
	} else if (!this.dumpFilePath.equals(other.dumpFilePath)) {
	    return false;
	}
	if (this.dumpFilename == null) {
	    if (other.dumpFilename != null) {
		return false;
	    }
	} else if (!this.dumpFilename.equals(other.dumpFilename)) {
	    return false;
	}
	if (this.remark == null) {
	    if (other.remark != null) {
		return false;
	    }
	} else if (!this.remark.equals(other.remark)) {
	    return false;
	}
	if (this.sachemaUserName == null) {
	    if (other.sachemaUserName != null) {
		return false;
	    }
	} else if (!this.sachemaUserName.equals(other.sachemaUserName)) {
	    return false;
	}
	if (this.status != other.status) {
	    return false;
	}
	if (this.uuid == null) {
	    if (other.uuid != null) {
		return false;
	    }
	} else if (!this.uuid.equals(other.uuid)) {
	    return false;
	}
	return true;
    }
}
