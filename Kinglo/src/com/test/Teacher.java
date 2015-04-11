package com.test;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Teacher implements Serializable{
	
	private static final long serialVersionUID = 25768974889676253L;
	private Integer id;
	private String name;
	private Integer age;
	
	List<Object> list=new ArrayList<Object>();
	
	public List<Object> getList() {
	    return list;
	}

	public void setList(List<Object> list) {
	    this.list = list;
	}

	public Teacher(){
		
	}
	
	
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	
	public Integer getId() {
	    return id;
	}

	public void setId(Integer id) {
	    this.id = id;
	}

	public Integer getAge() {
	    return age;
	}

	public void setAge(Integer age) {
	    this.age = age;
	}

	public String toString(){
		StringBuffer buffer=new StringBuffer();
		buffer.append("[");
		buffer.append(id).append(",");
		buffer.append(name).append(",");
		buffer.append(age).append(",");
		if(getList()!=null && getList().size()>0){
		    buffer.append("[");
		    for (int i = 0; i < list.size(); i++) {
			    buffer.append(list.get(i).toString()).append(",");
		    }
		    buffer.append("]");
		}
		buffer.append("]");
		return buffer.toString();
	}
}
