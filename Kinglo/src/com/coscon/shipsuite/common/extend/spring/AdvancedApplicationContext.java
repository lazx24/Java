package com.coscon.shipsuite.common.extend.spring;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.Hashtable;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.MessageSourceResolvable;
import org.springframework.context.NoSuchMessageException;
import org.springframework.core.env.Environment;
import org.springframework.core.io.Resource;

public final class AdvancedApplicationContext implements ApplicationContext {
    private ApplicationContext springApplicationContext;
    private static Map<ApplicationContext, AdvancedApplicationContext> instanceMap = new Hashtable<ApplicationContext, AdvancedApplicationContext>();
    private static final ReentrantReadWriteLock locker = new ReentrantReadWriteLock();

    public static AdvancedApplicationContext getInstance(
	    ApplicationContext context) {
	locker.readLock().lock();
	AdvancedApplicationContext instance = (AdvancedApplicationContext) instanceMap
		.get(context);
	if (instanceMap.get(context) == null) {
	    locker.readLock().unlock();
	    try {
		locker.writeLock().lock();
		instance = new AdvancedApplicationContext(context);
		instanceMap.put(context, instance);
	    } finally {
		locker.writeLock().unlock();
	    }
	} else {
	    locker.readLock().unlock();
	}
	return instance;
    }

    private AdvancedApplicationContext(ApplicationContext context) {
	this.springApplicationContext = context;
    }

    public boolean containsBeanDefinition(String arg0) {
	return this.springApplicationContext.containsBeanDefinition(arg0);
    }

    public <A extends Annotation> A findAnnotationOnBean(String arg0,
	    Class<A> arg1) {
	return this.springApplicationContext.findAnnotationOnBean(arg0, arg1);
    }

    public int getBeanDefinitionCount() {
	return this.springApplicationContext.getBeanDefinitionCount();
    }

    public String[] getBeanDefinitionNames() {
	return this.springApplicationContext.getBeanDefinitionNames();
    }

    public <T> Map<String, T> getBeansOfType(Class<T> arg0)
	    throws BeansException {
	return this.springApplicationContext.getBeansOfType(arg0);
    }

    public <T> Map<String, T> getBeansOfType(Class<T> arg0, boolean arg1,
	    boolean arg2) throws BeansException {
	return this.springApplicationContext.getBeansOfType(arg0, arg1, arg2);
    }

    public Map<String, Object> getBeansWithAnnotation(
	    Class<? extends Annotation> arg0) throws BeansException {
	return this.springApplicationContext.getBeansWithAnnotation(arg0);
    }

    public boolean containsBean(String arg0) {
	return this.springApplicationContext.containsBean(arg0);
    }

    public String[] getAliases(String arg0) {
	return this.springApplicationContext.getAliases(arg0);
    }

    public Object getBean(String arg0) throws BeansException {
	return this.springApplicationContext.getBean(arg0);
    }

    public <T> T getBean(Class<T> arg0) throws BeansException {
	return this.springApplicationContext.getBean(arg0);
    }

    public <T> T getBean(String arg0, Class<T> arg1) throws BeansException {
	return this.springApplicationContext.getBean(arg0, arg1);
    }

    public Object getBean(String arg0, Object... arg1) throws BeansException {
	return this.springApplicationContext.getBean(arg0, arg1);
    }

    public Class<?> getType(String arg0) throws NoSuchBeanDefinitionException {
	return this.springApplicationContext.getType(arg0);
    }

    public boolean isPrototype(String arg0)
	    throws NoSuchBeanDefinitionException {
	return this.springApplicationContext.isPrototype(arg0);
    }

    public boolean isSingleton(String arg0)
	    throws NoSuchBeanDefinitionException {
	return this.springApplicationContext.isSingleton(arg0);
    }

    public boolean containsLocalBean(String arg0) {
	return this.springApplicationContext.containsLocalBean(arg0);
    }

    public BeanFactory getParentBeanFactory() {
	return this.springApplicationContext.getParentBeanFactory();
    }

    public String getMessage(MessageSourceResolvable arg0, Locale arg1)
	    throws NoSuchMessageException {
	return this.springApplicationContext.getMessage(arg0, arg1);
    }

    public String getMessage(String arg0, Object[] arg1, Locale arg2)
	    throws NoSuchMessageException {
	return this.springApplicationContext.getMessage(arg0, arg1, arg2);
    }

    public String getMessage(String arg0, Object[] arg1, String arg2,
	    Locale arg3) {
	return this.springApplicationContext.getMessage(arg0, arg1, arg2, arg3);
    }

    public void publishEvent(ApplicationEvent arg0) {
	this.springApplicationContext.publishEvent(arg0);
    }

    public Resource[] getResources(String arg0) throws IOException {
	return this.springApplicationContext.getResources(arg0);
    }

    public ClassLoader getClassLoader() {
	return this.springApplicationContext.getClassLoader();
    }

    public Resource getResource(String arg0) {
	return this.springApplicationContext.getResource(arg0);
    }

    public AutowireCapableBeanFactory getAutowireCapableBeanFactory()
	    throws IllegalStateException {
	return this.springApplicationContext.getAutowireCapableBeanFactory();
    }

    public String getDisplayName() {
	return this.springApplicationContext.getDisplayName();
    }

    public String getId() {
	return this.springApplicationContext.getId();
    }

    public ApplicationContext getParent() {
	return this.springApplicationContext.getParent();
    }

    public long getStartupDate() {
	return this.springApplicationContext.getStartupDate();
    }

    public Environment getEnvironment() {
	return this.springApplicationContext.getEnvironment();
    }

    public String[] getBeanNamesForType(Class<?> arg0) {
	return this.springApplicationContext.getBeanNamesForType(arg0);
    }

    public String[] getBeanNamesForType(Class<?> arg0, boolean arg1,
	    boolean arg2) {
	return this.springApplicationContext.getBeanNamesForType(arg0, arg1,
		arg2);
    }

    public boolean isTypeMatch(String arg0, Class<?> arg1)
	    throws NoSuchBeanDefinitionException {
	return this.springApplicationContext.isTypeMatch(arg0, arg1);
    }

    public String getApplicationName() {
	return this.springApplicationContext.getApplicationName();
    }
}
