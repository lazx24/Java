package com.coscon.shipsuite.common.session;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import com.coscon.shipsuite.common.context.ApplicationContextUtil;
import com.coscon.shipsuite.common.context.CommonConstant;
import com.coscon.shipsuite.common.enums.SecurityType;
import com.coscon.shipsuite.common.exception.SessionTimeoutException;
import com.coscon.shipsuite.common.exception.ShipSuiteRuntimeException;
import com.coscon.shipsuite.common.log.ISystemLogger;
import com.coscon.shipsuite.common.log.LoggerFactory;
import com.coscon.shipsuite.common.util.cache.CacheManager;
import com.coscon.shipsuite.common.util.cache.CacheObject;
import com.coscon.shipsuite.common.util.generic.CollectionUtil;
import com.coscon.shipsuite.common.util.hardware.SystemUtil;
import com.coscon.shipsuite.common.util.security.SecurityUtil;
import com.coscon.shipsuite.common.util.string.StringUtil;
import com.coscon.shipsuite.common.util.timetask.ITimeTaskHandler;

public final class SessionManager implements ITimeTaskHandler {
    private static final String DEFAULT_NAMESPACE = "/";
    private static final ReentrantReadWriteLock locker = new ReentrantReadWriteLock();
    private static final Map<String, SessionManager> sessionManagerMap = new HashMap<String,SessionManager>();
    private static final Map<String, String> activeUserMap = new ConcurrentHashMap<String,String>();
    private static ISystemLogger logger = LoggerFactory.getSystemLogger(SessionManager.class);
    private int multiLoginMax = 0;
    private int multiLoginCount = 0;
    private String sessionNamespace;
    private static final CacheManager<ISession> userTokenCacheManager = new CacheManager<ISession>();
    private long sessionTimeout = CommonConstant.SessionConstant.SESSION_TIME_OUT_MINIUTES;
    private static final Date appStartedDatetime = new Date();
    private static final long nanoTime = System.nanoTime();

    private SessionManager(String sessionNamespace) {
	this.sessionNamespace = sessionNamespace;
    }

    public static SessionManager getInstance() {
	return getInstance("/");
    }

    public static SessionManager getInstance(int multiLoginMax) {
	SessionManager instance = getInstance(ApplicationContextUtil.getWebId());
	instance.setMultiLoginMax(multiLoginMax);
	return instance;
    }

    public static SessionManager getInstance(String sessionNamespace,
	    int multiLoginMax) {
	SessionManager instance = getInstance(sessionNamespace);
	instance.setMultiLoginMax(multiLoginMax);
	return instance;
    }

    public static SessionManager getInstance(String sessionNamespace) {
	if (StringUtil.isNullOrEmpty(sessionNamespace)) {
	    sessionNamespace = "/";
	}
	SessionManager sessionManager;
	try {
	    locker.readLock().lock();

	    sessionManager = (SessionManager) sessionManagerMap
		    .get(sessionNamespace);
	} finally {
	    locker.readLock().unlock();
	}
	if (sessionManager == null) {
	    try {
		locker.writeLock().lock();
		sessionManager = new SessionManager(sessionNamespace);
		sessionManagerMap.put(sessionNamespace, sessionManager);
	    } finally {
		locker.writeLock().unlock();
	    }
	}
	return sessionManager;
    }

    public int getMultiLoginMax() {
	return this.multiLoginMax;
    }

    public void setMultiLoginMax(int multiLoginMax) {
	this.multiLoginMax = multiLoginMax;
    }

    public List<String> getOnlinseUserIdList() {
	List<String> userList = new ArrayList<String>();
	userList.addAll(new HashSet<String>(activeUserMap.values()));

	CollectionUtil.sort(userList, new String[0]);
	return userList;
    }

    void checkSessionStatus() {
	Set<String> keySet = new HashSet<String>();
	keySet.addAll(userTokenCacheManager.keySet());
	int i = 0;
	for (String key : keySet) {
	    CacheObject<ISession> session = userTokenCacheManager
		    .getCacheObject(key);
	    if ((session != null) && (!session.isNotOverTime())) {
		removeSession(key);
		i++;
	    } else if (session != null) {
		ISession s = (ISession) session.getObjectWithinDuration();
		activeUserMap.put(s.getToken(), s.getUserId());
	    }
	}
	SystemUtil.checkSystemStatus();
    }

    public GenericSession createSession(String userId) {
	return (GenericSession) createSession(userId, GenericSession.class);
    }

    public String getSessionNamespace() {
	return this.sessionNamespace;
    }

    public <T extends ISession> T createSession(String userId,
	    Class<T> sessionCls) {
	if (StringUtil.isNullOrEmpty(userId)) {
	    throw new RuntimeException(
		    "User id is empty, can not create session.");
	}
	List<ISession> cachedSessionList = getActiveSessionByUserId(userId);
	if ((!cachedSessionList.isEmpty()) && (this.multiLoginMax > 0)
		&& (this.multiLoginCount >= this.multiLoginMax)) {
	    throw new RuntimeException(String.format(
		    "Multi-login limited(%d), can not create session.",
		    new Object[] { Integer.valueOf(this.multiLoginMax) }));
	}
	StringBuilder sb = new StringBuilder();
	sb.append(userId).append(".").append(System.currentTimeMillis())
		.append(".").append(System.nanoTime() - nanoTime);
	String token = SecurityUtil.sign(sb.toString(), SecurityType.MD5);
	T session;
	try {
	    session = (T) sessionCls.newInstance();
	} catch (Exception e) {
	    throw new ShipSuiteRuntimeException(e);
	}
	session.setUserId(userId);
	session.setToken(token);
	activeUserMap.put(token, userId);

	setSession(token, session);

	this.multiLoginCount += 1;

	return session;
    }

    public ISession createSession(String userId, String token) {

	if ((StringUtil.isNullOrEmpty(userId))
		|| (StringUtil.isNullOrEmpty(token))) {
	    return null;
	}
	ISession session;
	try {
	    session = (ISession) GenericSession.class.newInstance();
	} catch (Exception e) {
	    throw new ShipSuiteRuntimeException(e);
	}
	session.setUserId(userId);
	session.setToken(token);

	setSession(token, session);

	return session;
    }

    public ISession getActiveSession(String token) {
	return (ISession) userTokenCacheManager.getValueWithinDuration(token);
    }

    public Date getAppStartedDatetime() {
	return appStartedDatetime;
    }

    public ISession getSession(String token) throws SessionTimeoutException {
	ISession session = (ISession) userTokenCacheManager
		.getValueWithinDuration(token);
	if (session != null) {
	    return session;
	}
	session = (ISession) userTokenCacheManager.getValueAnyway(token);
	if (session != null) {
	    userTokenCacheManager.removeCacheObject(token);
	    throw new SessionTimeoutException();
	}
	return null;
    }

    public List<ISession> getActiveSessionByUserId(String userId)
	    throws SessionTimeoutException {
	List<ISession> sessionList = userTokenCacheManager.values();
	List<ISession> activeSessionList = new ArrayList<ISession>();
	for (ISession session : sessionList) {
	    if (session.getUserId().equals(userId)) {
		ISession actionSession = getActiveSession(session.getToken());
		if (actionSession != null) {
		    activeSessionList.add(actionSession);
		}
	    }
	}
	return activeSessionList;
    }

    public ISession getSystemSession() {
	List<ISession> systemSessionList = getActiveSessionByUserId("system");
	if (systemSessionList.isEmpty()) {
	    ISession systemSession = getInstance().createSession("system");
	    return systemSession;
	}
	return (ISession) systemSessionList.get(0);
    }

    public long getSessionTimeout() {
	return this.sessionTimeout;
    }

    public void heartBeat(String token) throws SessionTimeoutException {
	boolean isSuccess = false;
	try {
	    isSuccess = userTokenCacheManager
		    .refreshCacheObjectUpdatetTime(token);
	    if (!isSuccess) {
		throw new SessionTimeoutException();
	    }
	} catch (Exception e) {
	    throw new SessionTimeoutException();
	}
    }

    public boolean isActiveUser(String userId) {
	if (StringUtil.isNullOrEmpty(userId)) {
	    return false;
	}
	return activeUserMap.containsValue(userId);
    }

    public void removeSession(String token) {
	CacheObject<ISession> sessionCache = userTokenCacheManager
		.getCacheObject(token);
	if (sessionCache != null) {
	    ISession session = (ISession) sessionCache.getObjectItemAnyway();
	    if (session != null) {
		activeUserMap.remove(token);
		session = null;
	    }
	}
	userTokenCacheManager.removeCacheObject(token);
	this.multiLoginCount -= 1;
    }

    private void setSession(String token, ISession session) {
	userTokenCacheManager.saveCacheObject(token, session,
		this.sessionTimeout * 60L);
    }

    public void setSessionTimeout(long timeoutMinutes) {
	this.sessionTimeout = timeoutMinutes;
	userTokenCacheManager.setDurationMills(timeoutMinutes * 60L * 1000L);
    }

    public void setSessionTimeout(String token, long timeoutMinutes) {
	CacheObject<ISession> sessionCache = userTokenCacheManager
		.getCacheObject(token);
	if (sessionCache != null) {
	    userTokenCacheManager.saveCacheObject(token,
		    (ISession) sessionCache.getObjectItemAnyway(),
		    timeoutMinutes * 60L);
	}
    }

    public void processTask() {
	checkSessionStatus();
    }
}
