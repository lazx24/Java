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
@Table(name="KINGLO_DEPARTMENT")
public class Department {
	
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE,generator="DEPARTMENT_SEQ")
	@SequenceGenerator(name="DEPARTMENT_SEQ",sequenceName="DEPARTMENT_SEQUENCE",initialValue=1,allocationSize=999999999)
	private int id;
	
	private String name;
	
	@ManyToOne
	@JoinColumn(name="PARENT_DEPARTMENT_ID")
	private Department department;
	
	@OneToOne
	@JoinColumn(nullable=true,name="DEPARTMENT_USER_ID")
	private User departmentUser;

	public User getDepartmentUser() {
		return departmentUser;
	}

	public void setDepartmentUser(User departmentUser) {
		this.departmentUser = departmentUser;
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

	public Department getDepartment() {
		return department;
	}

	public void setDepartment(Department department) {
		this.department = department;
	}
}
