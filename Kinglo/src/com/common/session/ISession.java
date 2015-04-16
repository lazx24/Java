package com.common.session;

import java.io.Serializable;
import java.util.Locale;

public abstract interface ISession extends Serializable {
    public abstract Serializable getCookieValue(String paramString);

    public abstract Locale getLocale();

    public abstract String getToken();

    public abstract String getUserId();

    public abstract void setCookie(String paramString,
	    Serializable paramSerializable);

    public abstract void setLocale(Locale paramLocale);

    public abstract void setToken(String paramString);

    public abstract void setUserId(String paramString);
}
