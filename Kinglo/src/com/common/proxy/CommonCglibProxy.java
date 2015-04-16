package com.common.proxy;

import java.lang.reflect.Method;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

public final class CommonCglibProxy<T> implements MethodInterceptor {
    private Object target;
    private boolean ignoreCollectionProperty = true;

    public Object getInstance(Object target, boolean ignoreCollectionProperty) {
	this.ignoreCollectionProperty = ignoreCollectionProperty;
	this.target = target;
	Enhancer enhancer = new Enhancer();
	enhancer.setSuperclass(this.target.getClass());

	enhancer.setCallback(this);

	return enhancer.create();
    }

    public Object getInstance(Object target) {
	return getInstance(target, true);
    }

    public Object intercept(Object obj, Method method, Object[] args,
	    MethodProxy proxy) throws Throwable {
	return proxy.invokeSuper(obj, args);
    }
}
