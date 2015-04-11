package com.coscon.shipsuite.common.permissions;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name="KINGLO_USER")
public class User {
	
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE,generator="USER_SEQ")
	@SequenceGenerator(name="USER_SEQ",sequenceName="USER_SEQUENCE",initialValue=1,allocationSize=999999999)
	private int id;
	
	private String username;
	
	private String password;
	
	@OneToOne
	@JoinColumn(name="USER_INFO_ID")
	private UserInfo userInfo;
	
	@ManyToOne
	@JoinColumn(name="DEPARTMENT_ID")
	private Department department;
	
	@ManyToOne
	@JoinColumn(name="groupId")
	private Group group;
	
	public Group getGroup() {
		return group;
	}

	public void setGroup(Group group) {
		this.group = group;
	}

	private String classType;//类型

	public Department getDepartment() {
		return department;
	}

	public void setDepartment(Department department) {
		this.department = department;
	}

	public String getClassType() {
		return classType;
	}

	public void setClassType(String classType) {
		this.classType = classType;
	}

	public UserInfo getUserInfo() {
		return userInfo;
	}

	public void setUserInfo(UserInfo userInfo) {
		this.userInfo = userInfo;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
}
