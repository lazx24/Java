package com.coscon.shipsuite.common.util.string;

import java.io.Serializable;
import java.util.Locale;

public abstract interface IMessageHelper extends Serializable {
    public abstract String getMessageString(String paramString,
	    Locale paramLocale);

    public abstract String getMessageString(String paramString,
	    Locale paramLocale, String... paramVarArgs);

    public abstract String getMessageString(String paramString,
	    String... paramVarArgs);
}
