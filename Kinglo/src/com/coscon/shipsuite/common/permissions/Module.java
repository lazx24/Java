package com.coscon.shipsuite.common.permissions;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;


@Entity
@Table(name="KINGLO_MODULE")
public class Module {
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE,generator="MODULE_SEQ")
	@SequenceGenerator(name="MODULE_SEQ",sequenceName="MODULE_SEQUENCE",initialValue=1,allocationSize=999999999)
	private int id;
	
	private String moduleName;
	
	private String url;
	
	private String width;
	
	private String height;
	
	private String moduleCode;//菜单代码
	
	public String getModuleCode() {
		return moduleCode;
	}

	public void setModuleCode(String moduleCode) {
		this.moduleCode = moduleCode;
	}

	public int getModuleOrder() {
		return moduleOrder;
	}

	public void setModuleOrder(int moduleOrder) {
		this.moduleOrder = moduleOrder;
	}

	private int moduleOrder;//菜单排序
	
	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getWidth() {
		return width;
	}

	public void setWidth(String width) {
		this.width = width;
	}

	public String getHeight() {
		return height;
	}

	public void setHeight(String height) {
		this.height = height;
	}

	@ManyToOne
	@JoinColumn(name="parent_module_id")
	private Module module;

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

	public Module getModule() {
		return module;
	}

	public void setModule(Module module) {
		this.module = module;
	}
	
	
}
