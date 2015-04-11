package com.coscon.shipsuite.common.util.html;

public final class JavaScriptUtil {
    public static String getScriptCloseMe() {
	return "function closeMe(){ parent.window.opener = null;parent.window.open('', '_self');parent.window.close();parent.window.location.href = '';window.close(); }";
    }
}
