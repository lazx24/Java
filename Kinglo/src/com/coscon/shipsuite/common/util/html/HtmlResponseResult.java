package com.coscon.shipsuite.common.util.html;

import java.util.LinkedHashMap;

public final class HtmlResponseResult {
    private String htmlContent;
    private String plainText;
    private LinkedHashMap<String, String> hrefMap;

    public String getHtmlContent() {
	return this.htmlContent;
    }

    public void setHtmlContent(String htmlContent) {
	this.htmlContent = htmlContent;
    }

    public LinkedHashMap<String, String> getHrefMap() {
	return this.hrefMap;
    }

    public void setHrefMap(LinkedHashMap<String, String> hrefMap) {
	this.hrefMap = hrefMap;
    }

    public String getPlainText() {
	return this.plainText;
    }

    public void setPlainText(String plainText) {
	this.plainText = plainText;
    }
}
