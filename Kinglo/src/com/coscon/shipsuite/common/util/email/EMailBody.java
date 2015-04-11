package com.coscon.shipsuite.common.util.email;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

public final class EMailBody {
    private EMailField prefixField;
    private EMailField suffixField;
    private LinkedHashMap<EMailField, EMailField> contentTableMap;

    public LinkedHashMap<EMailField, EMailField> getContentTableMap() {
	return this.contentTableMap;
    }

    public EMailField getPrefixField() {
	return this.prefixField;
    }

    public EMailField getSuffixField() {
	return this.suffixField;
    }

    public void setContentTableMap(
	    LinkedHashMap<EMailField, EMailField> contentMap) {
	this.contentTableMap = contentMap;
    }

    public void setPrefixField(EMailField prefixField) {
	this.prefixField = prefixField;
    }

    public void setSuffixField(EMailField suffixField) {
	this.suffixField = suffixField;
    }

    public String toHtml() {
	StringBuilder sb = new StringBuilder();
	if (this.prefixField != null) {
	    sb.append(this.prefixField.toHtml());
	}
	sb.append("<form><table>");

	Iterator<?> localIterator = this.contentTableMap.entrySet().iterator();
	while (localIterator.hasNext()) {
	    Map.Entry<EMailField, EMailField> entry = (Map.Entry) localIterator.next();
	    sb.append("<tr><td width=\"30%\" align=\"left\">");
	    sb.append(((EMailField) entry.getKey()).toHtml());
	    sb.append("</td><td width=\"30%\" align=\"left\">");
	    sb.append(((EMailField) entry.getValue()).toHtml());
	    sb.append("</td></tr>");
	}
	sb.append("</table></form>");
	if (this.suffixField != null) {
	    sb.append(this.suffixField.toHtml());
	}
	return sb.toString();
    }
}
