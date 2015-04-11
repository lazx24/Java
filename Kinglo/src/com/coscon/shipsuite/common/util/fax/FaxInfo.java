package com.coscon.shipsuite.common.util.fax;

import com.coscon.shipsuite.common.util.file.ServiceFile;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class FaxInfo implements Serializable {
    private static final long serialVersionUID = 8572968091649185002L;
    String country;
    String area;
    String localFaxNumber;
    String sender;
    String receiver;
    List<ServiceFile> faxFileList;

    public FaxInfo() {
    }

    public FaxInfo(String areaNumber, String localFaxNumber,
	    List<ServiceFile> faxFileList) {
	this.area = areaNumber;
	this.localFaxNumber = localFaxNumber;
	this.faxFileList = faxFileList;
    }

    public FaxInfo(String areaNumber, String localFaxNumber, ServiceFile faxFile) {
	this.area = areaNumber;
	this.localFaxNumber = localFaxNumber;
	if (this.faxFileList == null) {
	    this.faxFileList = new ArrayList<ServiceFile>();
	}
	this.faxFileList.add(faxFile);
    }

    public String getArea() {
	return this.area;
    }

    public String getCountry() {
	return this.country;
    }

    public List<ServiceFile> getFaxFileList() {
	return this.faxFileList;
    }

    public String getLocalFaxNumber() {
	return this.localFaxNumber;
    }

    public String getReceiver() {
	return this.receiver;
    }

    public String getSender() {
	return this.sender;
    }

    public void setArea(String area) {
	this.area = area;
    }

    public void setCountry(String country) {
	this.country = country;
    }

    public void setFaxFileList(List<ServiceFile> faxFileList) {
	this.faxFileList = faxFileList;
    }

    public void setLocalFaxNumber(String localFaxNumber) {
	this.localFaxNumber = localFaxNumber;
    }

    public void setReceiver(String receiver) {
	this.receiver = receiver;
    }

    public void setSender(String sender) {
	this.sender = sender;
    }
}
