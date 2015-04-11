package com.coscon.shipsuite.common.enums;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

public enum DumpTypeEnums {
    IMPDP("IMPDP"), EXPDP("EXPDP");

    private String dumpType;

    private DumpTypeEnums(String dumpType) {
	this.dumpType = dumpType;
    }

    public String getDumpType() {
	return this.dumpType;
    }

    public void setDumpType(String dumpType) {
	this.dumpType = dumpType;
    }

    public static List<DumpTypeEnums> getAll() {
	List<DumpTypeEnums> list = new ArrayList<DumpTypeEnums>();
	list.addAll(EnumSet.allOf(DumpTypeEnums.class));
	return list;
    }
}
