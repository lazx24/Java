package com.common.enums;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

public enum ReportFormatEnums implements Serializable {
    HTML("html"), XLS("xls"), PDF("pdf"), DOC("doc"), PPT("ppt"), POSTSCRIPT(
	    "ps");

    private String format;

    public static List<ReportFormatEnums> getAll() {
	List<ReportFormatEnums> list = new ArrayList<ReportFormatEnums>();
	list.addAll(EnumSet.allOf(ReportFormatEnums.class));
	return list;
    }

    private ReportFormatEnums(String format) {
	this.format = format;
    }

    public String getFormat() {
	return this.format;
    }
}
