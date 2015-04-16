package com.common.proxy;

import java.util.Collection;
import org.hibernate.Hibernate;
import org.hibernate.proxy.HibernateProxy;

public final class HibernateProxyUtil {
    private static <T> T initAndUnproxy(T var) {
	if (var == null) {
	    return var;
	}
	if (!Hibernate.isInitialized(var)) {
	    Hibernate.initialize(var);
	}
	if ((var instanceof HibernateProxy)) {
	    var = (T)((HibernateProxy) var).getHibernateLazyInitializer().getImplementation();
	}
	return var;
    }

    public static <T> T initializeAndUnproxy(T var) {
	if (var == null) {
	    return var;
	}
	if ((var instanceof Collection)) {
	    for (Object o : (Collection) var) {
		o = initAndUnproxy(o);
	    }
	} else {
	    var = initAndUnproxy(var);
	}
	return var;
    }
}
