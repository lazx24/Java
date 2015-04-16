package com.common.permissions;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name="KINGLO_USER_INFO")
public class UserInfo {
	
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE,generator="USER_INFO_SEQ")
	@SequenceGenerator(name="USER_INFO_SEQ",sequenceName="USER_INFO_SEQUENCE",initialValue=1,allocationSize=999999999)
	private int id;
	
	private String name;
	
	@Temporal(TemporalType.DATE)
	private Date birthday;
	
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

	public Date getBirthday() {
		return birthday;
	}

	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}

}
