package com.common.permissions;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name="KINGLO_GROUP")
public class Group {
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE,generator="GROUP_SEQ")
	@SequenceGenerator(name="GROUP_SEQ",sequenceName="GROUP_SEQUENCE",initialValue=1,allocationSize=999999999)
	private int id;
	
	private String groupName;
	
	@ManyToMany
	@JoinTable(name="ROLE_GROUP",joinColumns={@JoinColumn(name="groupId")},inverseJoinColumns={@JoinColumn(name="roleId")})
	private Set<Role> role=new HashSet<Role>();

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public Set<Role> getRole() {
		return role;
	}

	public void setRole(Set<Role> role) {
		this.role = role;
	}
	
}
