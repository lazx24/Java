package com.coscon.shipsuite.common.util.string;

import com.coscon.shipsuite.common.result.OperationResult;
import com.coscon.shipsuite.common.result.ResultMessageKeyInfo;
import java.util.List;
import java.util.Locale;

public final class MessageHelper {
    public static IMessageHelper getInstance() {
	return MessageHelperImpl.getInstance();
    }

    public static IMessageHelper getInstance(Locale locale) {
	return MessageHelperImpl.getInstance(locale);
    }

    public static String getMessageString(
	    List<ResultMessageKeyInfo> messageKeyInfoList, Locale locale) {
	if ((messageKeyInfoList == null) || (messageKeyInfoList.size() == 0)) {
	    return "";
	}
	StringBuffer infoSb = new StringBuffer();
	for (ResultMessageKeyInfo keyInfo : messageKeyInfoList) {
	    infoSb.append(getMessageString(keyInfo.getMessageKey(), locale,
		    keyInfo.getMessageArgs()));
	    infoSb.append("\n");
	}
	return infoSb.toString();
    }

    public static String getMessageString(String messageKey) {
	return getInstance().getMessageString(messageKey, new String[0]);
    }

    public static String getMessageString(String messageKey, Locale locale) {
	return getInstance().getMessageString(messageKey, locale);
    }

    public static String getMessageString(String messageKey, Locale locale,
	    String... messageArgs) {
	return getInstance().getMessageString(messageKey, locale, messageArgs);
    }

    public static String getMessageString(String messageKey,
	    String[] messageArgs) {
	return getInstance().getMessageString(messageKey, messageArgs);
    }

    public static String getOperationResultMessage(OperationResult<?> result,
	    Locale locale) {
	StringBuffer sb = new StringBuffer();
	if (result.getErrorMessageKey() != null) {
	    sb.append(getMessageString(result.getErrorMessageKey(), locale,
		    result.getMsgArgs()));
	}
	if ((result.getMessageKeyInfoList() != null)
		&& (result.getMessageKeyInfoList().size() > 0)) {
	    if (sb.length() > 0) {
		sb.append("\n");
	    }
	    sb.append(getMessageString(result.getMessageKeyInfoList(), locale));
	}
	if ((sb.length() == 0) && (result.getResult() != null)) {
	    sb.append(getMessageString(result.getResult().toString()));
	}
	return sb.toString();
    }
}
