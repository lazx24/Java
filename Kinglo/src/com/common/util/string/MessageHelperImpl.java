package com.common.util.string;

import com.common.util.file.PropertyLoader;

import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

final class MessageHelperImpl implements IMessageHelper {
   
    private static final long serialVersionUID = -209315200235468035L;
    private static final Map<Locale, MessageHelperImpl> instanceMap = new ConcurrentHashMap<Locale, MessageHelperImpl>();
    private static final Locale DEFAULT_LOCALE = Locale.US;
    private Locale locale;

    public static IMessageHelper getInstance() {
	return getInstance(DEFAULT_LOCALE);
    }

    public static IMessageHelper getInstance(Locale locale) {
	MessageHelperImpl instance = (MessageHelperImpl) instanceMap
		.get(locale);
	if (instance == null) {
	    instance = new MessageHelperImpl(locale);
	    instanceMap.put(locale, instance);
	}
	return instance;
    }

    private MessageHelperImpl(Locale locale) {
	this.locale = locale;
    }
    
    public String getMessageString(String messageKey, Locale locale) {
	if (messageKey == null) {
	    return null;
	}
	String text = PropertyLoader.getApplicationResource(messageKey, locale);
	if (StringUtil.isNotNullAndNotEmpty(text)) {
	    return text;
	}
	return messageKey;
    }

    public String getMessageString(String messageKey, Locale locale,
	    String... messageArgs) {
	if (messageKey == null) {
	    return null;
	}
	String text = PropertyLoader.getApplicationResource(messageKey, locale);

	StringBuffer sb = new StringBuffer();
	if ((text != null) && (messageArgs != null)) {
	    for (int i = 0; i < messageArgs.length; i++) {
		if (messageArgs[i].contains("\\")) {
		    messageArgs[i] = messageArgs[i].replaceAll("\\\\",
			    "\\\\\\\\");
		}
		sb.setLength(0);
		sb.append("\\{");
		sb.append(i);
		sb.append("\\}");
		if (messageArgs[i] != null) {
		    text = text.replaceAll(sb.toString(),
			    getMessageString(messageArgs[i], locale));
		} else {
		    text = text.replaceAll(sb.toString(), "");
		}
	    }
	}
	if (text != null) {
	    return text;
	}
	return messageKey;
    }

    public String getMessageString(String messageKey, String... messageArgs) {
	return getMessageString(messageKey, this.locale, messageArgs);
    }
    
    public static void main(String[] args) {
	String str=getInstance().getMessageString("'{1}' is not null!", new String[]{"zoujianhua"});
	System.out.println(str);
    }
}
