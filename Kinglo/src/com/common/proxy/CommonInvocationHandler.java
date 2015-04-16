package com.common.proxy;

import com.common.exception.ShipSuiteRuntimeException;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
/**
 * 代理类
 * @author zou
 * 2015-1-30
 */
public final class CommonInvocationHandler<T> implements InvocationHandler {
    private T target;
    
    /**
     * 产生代理类
     * @param target
     * @return
     */
    @SuppressWarnings("unchecked")
    public T bind(T target) {
	this.target = target;

	return (T) Proxy.newProxyInstance(target.getClass().getClassLoader(),
		target.getClass().getInterfaces(), this);
    }

    public Object invoke(Method method, Object... args) throws Throwable {
	if (this.target == null) {
	    throw new ShipSuiteRuntimeException("Please bind entiy first!");
	}
	return invoke(this.target, method, args);
    }

    public Object invoke(Object proxy, Method method, Object[] args)
	    throws Throwable {
	Object retObject = method.invoke(this.target, args);
	return retObject;
    }

    public T getTarget() {
	return this.target;
    }
}
