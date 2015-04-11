package com.coscon.shipsuite.common.permissions;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;


@Entity
@Table(name="KINGLO_FUNCTION")
public class Function {
	
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE,generator="FUNCTION_SEQ")
	@SequenceGenerator(name="FUNCTION_SEQ",sequenceName="FUNCTION_SEQUENCE",initialValue=1,allocationSize=999999999)
	private int id;
	
	private String name;
	
	private String url;
	
	private String operatorDescribe;//操作描述
	
	public String getOperatorDescribe() {
		return operatorDescribe;
	}

	public void setOperatorDescribe(String operatorDescribe) {
		this.operatorDescribe = operatorDescribe;
	}

	@ManyToMany
	@JoinTable(name="ROLE_FUNCTION",joinColumns={@JoinColumn(name="functionId")},inverseJoinColumns={@JoinColumn(name="roleId")})
	private Set<Role> role=new HashSet<Role>();
	
	@ManyToOne
	@JoinColumn(name="moduleId")
	private Module module;

	public Module getModule() {
		return module;
	}

	public void setModule(Module module) {
		this.module = module;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Set<Role> getRole() {
		return role;
	}

	public void setRole(Set<Role> role) {
		this.role = role;
	}
	
}
