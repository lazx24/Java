package com.common.proxy;

import com.common.util.bean.BeanUtil;

import java.lang.reflect.Method;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

public final class HashCglibProxy<T> implements MethodInterceptor {
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
	if (method.getName().equals("hashCode")) {
	    return Integer.valueOf(BeanUtil.hashCodeByProperties(obj,
		    this.ignoreCollectionProperty));
	}
	if ((method.getName().equals("equals")) && (args.length == 1)) {
	    if (args[0] == null) {
		return Boolean.valueOf(false);
	    }
	    if (args[0].toString().startsWith(
		    this.target.getClass().getName() + "$$EnhancerByCGLIB$$")) {
		if (hashCode() == args[0].hashCode()) {
		    return Boolean.valueOf(true);
		}
		return Boolean.valueOf(false);
	    }
	    if (BeanUtil.hashCodeByProperties(this.target,
		    this.ignoreCollectionProperty) == BeanUtil
		    .hashCodeByProperties(args[0],
			    this.ignoreCollectionProperty)) {
		return Boolean.valueOf(true);
	    }
	    return Boolean.valueOf(false);
	}
	return proxy.invokeSuper(obj, args);
    }

    public int hashCode() {
	return BeanUtil.hashCodeByProperties(this.target,
		this.ignoreCollectionProperty);
    }

    public boolean equals(Object obj) {
	return BeanUtil.hashCodeByProperties(this.target,
		this.ignoreCollectionProperty) == BeanUtil
		.hashCodeByProperties(obj, this.ignoreCollectionProperty);
    }
}
