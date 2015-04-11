package com.coscon.shipsuite.common.session;

import java.io.Serializable;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@SuppressWarnings("serial")
public class GenericSession implements ISession, Serializable {
    private String userId;
    private String token;
    private Locale locale = Locale.US;
    private Map<String, Serializable> cookieMap = new ConcurrentHashMap<String, Serializable>();
    private Map<String, Serializable> keyValueMap = new ConcurrentHashMap<String, Serializable>();

    public GenericSession() {
    }

    public GenericSession(String userId, String token) {
	this.userId = userId;
	this.token = token;
    }

    public Serializable getCookieValue(String cookieName) {
	return (Serializable) this.cookieMap.get(cookieName);
    }

    public Locale getLocale() {
	return this.locale;
    }

    public String getToken() {
	return this.token;
    }

    public String getUserId() {
	return this.userId;
    }

    public Object getValue(String key) {
	return this.keyValueMap.get(key);
    }

    public void putKeyValue(String key, Serializable value) {
	this.keyValueMap.put(key, value);
    }

    public void setCookie(String cookieName, Serializable cookieValue) {
	this.cookieMap.put(cookieName, cookieValue);
    }

    public void setLocale(Locale locale) {
	this.locale = locale;
    }

    public void setToken(String token) {
	this.token = token;
    }

    public void setUserId(String userId) {
	this.userId = userId;
    }
}
