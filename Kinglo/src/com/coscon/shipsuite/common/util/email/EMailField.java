package com.coscon.shipsuite.common.util.email;

public final class EMailField {
    private EMailColor fontColor = EMailColor.black;
    private String text;
    private boolean selfDefinedStyleText;
    private Integer size;

    public EMailField(String text) {
	this.text = text;
    }

    public EMailField(String text, EMailColor fontColor, Integer size) {
	this.text = text;
	this.fontColor = fontColor;
	this.size = size;
    }

    public EMailColor getFontColor() {
	return this.fontColor;
    }

    public int getSize() {
	return this.size.intValue();
    }

    public String getText() {
	return this.text;
    }

    public boolean isSelfDefinedStyleText() {
	return this.selfDefinedStyleText;
    }

    public void setFontColor(EMailColor fontColor) {
	this.fontColor = fontColor;
    }

    public void setSelfDefinedStyleText(boolean selfDefinedStyleText) {
	this.selfDefinedStyleText = selfDefinedStyleText;
    }

    public void setSize(Integer i) {
	this.size = i;
    }

    public void setText(String text) {
	this.text = text;
    }

    public String toHtml() {
	if (!this.selfDefinedStyleText) {
	    StringBuilder sb = new StringBuilder();
	    if ((this.fontColor != null) && (this.size != null)) {
		sb.append("<font color=\"");
		sb.append(this.fontColor.toString());
		sb.append("\" size=\"");
		sb.append(this.size);
		sb.append("\">");
		sb.append(this.text);
		sb.append("</font>");
	    } else if (this.fontColor != null) {
		sb.append("<font color=\"");
		sb.append(this.fontColor.toString());
		sb.append("\">");
		sb.append(this.text);
		sb.append("</font>");
	    } else if (this.fontColor != null) {
		sb.append("<font size=\"");
		sb.append(this.size);
		sb.append("\">");
		sb.append(this.text);
		sb.append("</font>");
	    } else {
		return this.text;
	    }
	    return sb.toString();
	}
	return this.text;
    }
}
