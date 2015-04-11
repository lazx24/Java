package com.coscon.shipsuite.common.permissions;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name="KINGLO_LOG")
public class LogRecord {
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE,generator="LOG_SEQ")
	@SequenceGenerator(name="LOG_SEQ",sequenceName="LOG_SEQUENCE",initialValue=1,allocationSize=999999999)
	private int id;
	
	private String moduleName;//模块名
	
	private String name;//操作人
	
	private String ip;
	
	private String requestParam;
	
	public String getRequestParam() {
		return requestParam;
	}

	public void setRequestParam(String requestParam) {
		this.requestParam = requestParam;
	}

	public String getMethodName() {
		return methodName;
	}

	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}

	private String methodName;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getModuleName() {
		return moduleName;
	}

	public void setModuleName(String moduleName) {
		this.moduleName = moduleName;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}
	
}
