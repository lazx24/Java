package com.coscon.shipsuite.common.enums;

import java.io.Serializable;

public enum SortEnums implements Serializable {
    ASC("ASC"), DESC("DESC");

    private String type;

    private SortEnums(String type) {
	this.type = type;
    }

    public String getType() {
	return this.type;
    }

    public SortEnums reverse() {
	if (equals(ASC)) {
	    return DESC;
	}
	return ASC;
    }
}
