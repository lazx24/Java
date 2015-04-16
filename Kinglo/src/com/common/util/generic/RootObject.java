package com.common.util.generic;

import com.common.session.GenericSession;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class RootObject implements Serializable {
    
    private static final long serialVersionUID = 5176851160515613587L;
    
    private String id;
    private GenericSession session;
    private Set<GenericSession> childrenSessionList = new HashSet<GenericSession>();
    private String name;
    private String nick;
    private Date datetime;
    private boolean human = true;

    public String getName() {
	return this.name;
    }

    public void setName(String name) {
	this.name = name;
    }

    public String getNick() {
	return this.nick;
    }

    public void setNick(String nick) {
	this.nick = nick;
    }

    public GenericSession getSession() {
	return this.session;
    }

    public void setSession(GenericSession session) {
	this.session = session;
    }

    public String getId() {
	return this.id;
    }

    public void setId(String id) {
	this.id = id;
    }

    public boolean isHuman() {
	return this.human;
    }

    public void setHuman(boolean human) {
	this.human = human;
    }

    public Date getDatetime() {
	return this.datetime;
    }

    public void setDatetime(Date datetime) {
	this.datetime = datetime;
    }

    public Set<GenericSession> getChildrenSessionList() {
	return this.childrenSessionList;
    }

    public void setChildrenSessionList(Set<GenericSession> childrenSessionList) {
	this.childrenSessionList = childrenSessionList;
    }

    public int hashCode() {
	int result = 1;
	result = 31
		* result
		+ (this.childrenSessionList == null ? 0
			: this.childrenSessionList.hashCode());
	result = 31 * result
		+ (this.datetime == null ? 0 : this.datetime.hashCode());
	result = 31 * result + (this.human ? 1231 : 1237);
	result = 31 * result + (this.id == null ? 0 : this.id.hashCode());
	result = 31 * result + (this.name == null ? 0 : this.name.hashCode());
	result = 31 * result + (this.nick == null ? 0 : this.nick.hashCode());
	result = 31 * result
		+ (this.session == null ? 0 : this.session.hashCode());
	return result;
    }

    public boolean equals(Object obj) {
	if (this == obj) {
	    return true;
	}
	if (obj == null) {
	    return false;
	}
	if (getClass() != obj.getClass()) {
	    return false;
	}
	RootObject other = (RootObject) obj;
	if (this.childrenSessionList == null) {
	    if (other.childrenSessionList != null) {
		return false;
	    }
	} else if (!this.childrenSessionList.equals(other.childrenSessionList)) {
	    return false;
	}
	if (this.datetime == null) {
	    if (other.datetime != null) {
		return false;
	    }
	} else if (!this.datetime.equals(other.datetime)) {
	    return false;
	}
	if (this.human != other.human) {
	    return false;
	}
	if (this.id == null) {
	    if (other.id != null) {
		return false;
	    }
	} else if (!this.id.equals(other.id)) {
	    return false;
	}
	if (this.name == null) {
	    if (other.name != null) {
		return false;
	    }
	} else if (!this.name.equals(other.name)) {
	    return false;
	}
	if (this.nick == null) {
	    if (other.nick != null) {
		return false;
	    }
	} else if (!this.nick.equals(other.nick)) {
	    return false;
	}
	if (this.session == null) {
	    if (other.session != null) {
		return false;
	    }
	} else if (!this.session.equals(other.session)) {
	    return false;
	}
	return true;
    }
}
