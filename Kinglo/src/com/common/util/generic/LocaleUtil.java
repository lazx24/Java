package com.common.util.generic;

import java.util.Locale;

public final class LocaleUtil {
    /**
     * 根据langeuage获取Locale
     * @param language
     * @return
     */
    public static Locale getLocale(String language) {
	for (Locale locale : Locale.getAvailableLocales()) {
	    if (locale.toString().equals(language)) {
		return locale;
	    }
	}
	return null;
    }
    
    public static void main(String[] args) {
	for(Locale locale:Locale.getAvailableLocales()){
	    System.out.println(locale.toString());
	}
    }
}
