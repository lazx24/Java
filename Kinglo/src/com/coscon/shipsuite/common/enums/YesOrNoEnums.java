package com.coscon.shipsuite.common.enums;

import java.io.Serializable;

public enum YesOrNoEnums implements Serializable {
	Y("YES"), N("NO"), A("ALL");

	private String fullname;

	private YesOrNoEnums(String fullname) {
		this.fullname = fullname;
	}

	public String getFullname() {
		return this.fullname;
	}
}
