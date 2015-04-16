package com.common.permissions;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name="KINGLO_ROLE")
public class Role {
	
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE,generator="ROLE_SEQ")
	@SequenceGenerator(name="ROLE_SEQ",sequenceName="ROLE_SEQUENCE",initialValue=1,allocationSize=999999999)
	private int id;
	
	private String roleName;
	
	@ManyToMany(mappedBy="role")
	private Set<Group> group=new HashSet<Group>();
	
	@ManyToMany(mappedBy="role")
	private Set<Function> function=new HashSet<Function>();
	
	public Set<Function> getFunction() {
		return function;
	}

	public void setFunction(Set<Function> function) {
		this.function = function;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public Set<Group> getGroup() {
		return group;
	}

	public void setGroup(Set<Group> group) {
		this.group = group;
	}

}
