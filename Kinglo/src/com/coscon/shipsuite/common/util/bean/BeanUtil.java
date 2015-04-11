package com.coscon.shipsuite.common.util.bean;

import com.coscon.shipsuite.common.exception.ShipSuiteRuntimeException;
import com.coscon.shipsuite.common.proxy.CommonInvocationHandler;
import com.coscon.shipsuite.common.proxy.HashCglibProxy;
import com.coscon.shipsuite.common.util.string.StringUtil;
import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Clob;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;
import org.apache.commons.beanutils.NestedNullException;
import org.apache.commons.beanutils.PropertyUtils;
import org.hibernate.Hibernate;
import org.hibernate.LazyInitializationException;
import org.hibernate.collection.PersistentSet;
import org.springframework.beans.SimpleTypeConverter;
import org.springframework.beans.TypeConverter;
import org.springframework.beans.TypeMismatchException;
import org.springframework.util.ClassUtils;
import org.springframework.util.ReflectionUtils;

public final class BeanUtil {
    /**
     *深复制Bean
     * @param <T>
     * @param bean
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <T> T clone(T bean) {
	if (bean == null) {
	    return null;
	}
	T newBean = null;
	try {
	    if (bean.getClass().getName().contains("_$$_javassist_")) {
		String clsName = bean
			.getClass()
			.getName()
			.substring(
				0,
				bean.getClass().getName()
					.indexOf("_$$_javassist_"));
		Class<?> cls = bean.getClass().getClassLoader().loadClass(clsName);
		newBean = (T) cls.newInstance();
	    } else {
		newBean = (T) bean.getClass().newInstance();
	    }
	    Stack stack = new Stack();
	    copyProperties(bean, newBean, stack, false);
	} catch (Exception e) {
	    e.printStackTrace();
	}
	return newBean;
    }
    
    /**
     * 只复制属性部分
     * @param <T>
     * @param oldObj	待复制的对象
     * @param newObj	目标对象
     * @param properties 复制属性部分
     * @return
     */
    public static <T> T copyBeans(Object oldObj, T newObj, String... properties) {
	Set<String> propertySet = null;
	if ((properties != null) && (properties.length > 0)) {
	    propertySet = new HashSet<String>();
	    for (String property : properties) {
		propertySet.add(property);
	    }
	}
	PropertyDescriptor[] oldPds = PropertyUtils
		.getPropertyDescriptors(oldObj);
	PropertyDescriptor[] newPds = PropertyUtils
		.getPropertyDescriptors(newObj);
	String[] propertys = new String[oldPds.length];
	int ss = 0;
	for (int i = 0; i < oldPds.length; i++) {
	    PropertyDescriptor oldPd = oldPds[i];
	    if ((propertySet == null)
		    || (propertySet.contains(oldPd.getDisplayName()))) {
		for (int t = 0; t < newPds.length; t++) {
		    PropertyDescriptor newPd = newPds[t];
		    if ((oldPd.getDisplayName().equals(newPd.getDisplayName()))
			    && (!newPd.getDisplayName().equals("class"))) {
			propertys[ss] = oldPd.getDisplayName();
			ss++;
			break;
		    }
		}
	    }
	}
	for (int i = 0; i < propertys.length; i++) {
	    if (propertys[i] == null) {
		return newObj;
	    }
	    try {
		Object indate = PropertyUtils.getNestedProperty(oldObj,
			propertys[i]);
		if ((indate instanceof java.util.Date)) {
		    indate = new java.sql.Date(
			    ((java.util.Date) indate).getTime());
		}
		if ((propertySet == null)
			|| (propertySet.contains(propertys[i]))) {
		    PropertyUtils.setNestedProperty(newObj, propertys[i],
			    indate);
		}
	    } catch (Exception localException) {
	    }
	}
	return newObj;
    }
    
    /**
     * 只复制属性部分
     * @param <T>
     * @param oldObj	待复制的对象
     * @param newObj	目标对象
     * @param properties 复制属性部分
     * @return
     */
    public static <T> T copyBeans(Object oldObj, T newObj,
	    String[][] propertyArray) {
	newObj = copyBeans(oldObj, newObj, new String[0]);
	for (int i = 0; i < propertyArray.length; i++) {
	    for (int t = 0; t < propertyArray[i].length; t++) {
		if (propertyArray[i].length < 2) {
		    return newObj;
		}
		if (propertyArray[i][t] == null) {
		    return newObj;
		}
	    }
	    try {
		Object indate = PropertyUtils.getNestedProperty(oldObj,
			propertyArray[i][0]);
		if ((indate instanceof java.util.Date)) {
		    indate = new java.sql.Date(
			    ((java.util.Date) indate).getTime());
		}
		PropertyUtils.setNestedProperty(newObj, propertyArray[i][1],
			indate);
	    } catch (Exception e) {
		throw new ShipSuiteRuntimeException(e);
	    }
	}
	return newObj;
    }
    
    /**
     * 深度复制对象
     * @param dest
     * @param orig
     */
    public static void copyObject(Object dest, Object orig) {
	try {
	    Stack stack = new Stack();
	    copyProperties(orig, dest, stack, false);
	} catch (Exception e) {
	    throw new ShipSuiteRuntimeException(e);
	}
    }
    
    /**
     * 深度复制
     * @param <T>
     * @param ob
     * @return
     */
    public static <T> T deepClone(T ob) {
	if (ob == null) {
	    return null;
	}
	if (ob.getClass().toString().startsWith("class com.sun.proxy")) {
	    return null;
	}
	ByteArrayOutputStream bo = new ByteArrayOutputStream();
	T newOb = null;
	try {
	    ObjectOutputStream oo = new ObjectOutputStream(bo);
	    oo.writeObject(ob);
	    ByteArrayInputStream bi = new ByteArrayInputStream(bo.toByteArray());
	    ObjectInputStream oi = new ObjectInputStream(bi);
	    newOb = (T) oi.readObject();
	} catch (Exception e) {
	    throw new ShipSuiteRuntimeException(e);
	}
	return newOb;
    }
    
    /**
     * 获取Class所有的属性名
     * @param cls
     * @return
     */
    public static String[] getProperties(Class<?> cls) {
	List<String> propertyList = new ArrayList<String>();
	Method[] methods = cls.getDeclaredMethods();
	Map<String, Method> methodMap = new HashMap<String,Method>();
	for (Method method : methods) {
	    methodMap.put(method.getName(), method);
	}
	for (Method method : methods) {
	    if (((method.getName().startsWith("get"))
		    || (method.getName().startsWith("is")) || (method.getName()
		    .startsWith("isIs")))
		    && (method.getParameterTypes().length == 0)) {
		String propertyName = "";
		if (method.getName().startsWith("get")) {
		    propertyName = method.getName().substring(3);
		} else if (method.getName().startsWith("is")) {
		    propertyName = method.getName().substring(2);
		}
		String setMethodName = "set" + propertyName;
		Method setMethod = (Method) methodMap.get(setMethodName);
		if (setMethod != null) {
		    if ((setMethod.getReturnType().equals(Void.TYPE))
			    && (setMethod.getParameterTypes().length == 1)
			    && (setMethod.getParameterTypes()[0].equals(method
				    .getReturnType()))) {
			propertyList.add(propertyName.substring(0, 1)
				.toLowerCase() + propertyName.substring(1));
		    }
		}
	    }
	}
	return (String[]) propertyList.toArray(new String[propertyList.size()]);
    }
    
    /**
     * 获得Bean属性值
     * @param bean	Bean对象
     * @param property	属性名
     * @return	属性值
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     * @throws NoSuchMethodException
     */
    public static Object getProperty(Object bean, String property)
	    throws IllegalAccessException, InvocationTargetException,
	    NoSuchMethodException {
	try {
	    return PropertyUtils.getProperty(bean, property);
	} catch (NestedNullException ex) {
	}
	return null;
    }
    
    /**
     * 获取Bean属性类型
     * @param bean	Bean对象
     * @param property	属性名
     * @return	属性类型
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     * @throws NoSuchMethodException
     * @throws InstantiationException
     */
    public static Class<?> getPropertyType(Object bean, String property)
	    throws IllegalAccessException, InvocationTargetException,
	    NoSuchMethodException, InstantiationException {
	Class<?> propertyType = null;
	try {
	    return PropertyUtils.getPropertyType(bean, property);
	} catch (IllegalArgumentException ex) {
	    if ((ex.getMessage().startsWith("Null property value for"))
		    && (property.contains("."))) {
		String parentProperty = property.substring(0,
			property.lastIndexOf("."));

		Class<?> parentClass = getPropertyType(bean, parentProperty);
		if ((parentClass == null)
			|| (Collection.class.isAssignableFrom(parentClass))) {
		    return null;
		}
		Object parentObject = parentClass.newInstance();

		String nestedProperty = property.substring(property
			.lastIndexOf(".") + 1);

		propertyType = getPropertyType(parentObject, nestedProperty);
	    }
	}
	return propertyType;
    }
    
    /**
     * 获取实际的属性类型
     * @param bean	Bean对象
     * @param property	属性名
     * @return	属性类型
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     * @throws NoSuchMethodException
     * @throws InstantiationException
     */
    public static Class<?> getActualPropertyType(Object bean, String property)
	    throws IllegalAccessException, InvocationTargetException,
	    NoSuchMethodException, InstantiationException {
	Class<?> propertyType = null;

	propertyType = getPropertyType(bean, property);
	if ((propertyType != null)
		&& (!Collection.class.isAssignableFrom(propertyType))) {
	    return propertyType;
	}
	String[] propertyNames = property.split("\\.");

	propertyType = bean.getClass();
	for (String pName : propertyNames) {
	    pName = pName.trim();
	    String name = "get" + pName.substring(0, 1).toUpperCase()
		    + pName.substring(1);
	    Method propertyMethod = null;
	    try {
		propertyMethod = propertyType.getMethod(name, null);
	    } catch (NoSuchMethodException ex) {
		name = "is" + pName.substring(0, 1).toUpperCase()
			+ pName.substring(1);
		try {
		    propertyMethod = bean.getClass().getMethod(name, null);
		} catch (NoSuchMethodException e) {
		    throw new ShipSuiteRuntimeException(
			    "Not found any matched propertyName for "
				    + property + " of " + bean.toString());
		}
	    }
	    propertyType = propertyMethod.getReturnType();
	    if (Collection.class.isAssignableFrom(propertyType)) {
		propertyType = GenericsUtil
			.getMethodGenericReturnType(propertyMethod);
	    }
	}
	return propertyType;
    }
    
    /**
     * 设置属性值
     * @param bean	Bean对象
     * @param property	属性名
     * @param value	属性值
     * @return	是否设置成功
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     * @throws NoSuchMethodException
     * @throws InstantiationException
     */
    public static boolean setProperty(Object bean, String property, Object value)
	    throws IllegalAccessException, InvocationTargetException,
	    NoSuchMethodException, InstantiationException {
	Object orgiValue = getProperty(bean, property);
	boolean isChanged;
	if (value != null) {
	    isChanged = !value.equals(orgiValue);
	} else {
	    isChanged = value != orgiValue;
	}
	if (!isChanged) {
	    return isChanged;
	}
	try {
	    PropertyUtils.setProperty(bean, property, value);
	} catch (IllegalArgumentException ex) {
	    if ((ex.getMessage().startsWith("Null property value for"))
		    && (property.contains("."))) {
		String parentProperty = property.substring(0,
			property.lastIndexOf("."));

		Class<?> parentClass = getPropertyType(bean, parentProperty);

		Object parentObject = parentClass.newInstance();

		String nestedProperty = property.substring(property
			.lastIndexOf(".") + 1);

		setProperty(parentObject, nestedProperty, value);

		setProperty(bean, parentProperty, parentObject);
	    }
	}
	return isChanged;
    }
    
    /**
     * Bean转换为Xml
     * @param bean Bean对象
     * @return
     */
    public static String bean2xml(Object bean) {
	return ObjectUtil.object2xml(bean);
    }
    
    /**
     * Xml转换为Bean
     * @param xml Xml
     * @return
     */
    public static Object xml2bean(String xml) {
	return ObjectUtil.xml2object(xml);
    }
    
    /**
     * 获取Bean对象所有属性值
     * @param bean Bean对象
     * @return 
     */
    public static Object[] getProperties(Object bean) {
	Object[] properties = (Object[]) null;
	try {
	    BeanInfo bi = Introspector.getBeanInfo(bean.getClass());
	    PropertyDescriptor[] pds = bi.getPropertyDescriptors();
	    properties = new Object[pds.length];
	    for (int i = 0; i < pds.length; i++) {
		try {
		    properties[i] = getProperty(bean, pds[i].getName());
		} catch (Exception e) {
		    if ((e instanceof LazyInitializationException)) {
			properties[i] = null;
		    } else {
			throw new ShipSuiteRuntimeException(e);
		    }
		}
	    }
	    return properties;
	} catch (IntrospectionException e) {
	    throw new ShipSuiteRuntimeException(e);
	}
    }
    
    /**
     * 此方法不可用
     * @param targetClass
     * @param targetMethod
     * @param arguments
     * @return
     */
    private static Object[] doFindMatchingMethodArguments(Class<?> targetClass,
	    String targetMethod, Object[] arguments) {
	TypeConverter converter = new SimpleTypeConverter();

	Method matchingMethod = null;
	int argCount = arguments == null ? 0 : arguments.length;
	Method[] candidates = ReflectionUtils
		.getAllDeclaredMethods(targetClass);
	int minTypeDiffWeight = 2147483647;
	Object[] argumentsToUse = (Object[]) null;
	for (Method candidate : candidates) {
	    if (candidate.getName().equals(targetMethod)) {
		Class<?>[] paramTypes = candidate.getParameterTypes();
		if (paramTypes.length == argCount) {
		    Object[] convertedArguments = new Object[argCount];
		    boolean match = true;
		    for (int j = 0; (j < argCount) && (match); j++) {
			try {
			    convertedArguments[j] = converter
				    .convertIfNecessary(arguments[j],
					    paramTypes[j]);
			} catch (TypeMismatchException ex) {
			    match = false;
			}
		    }
		    if (match) {
			int typeDiffWeight = getTypeDifferenceWeight(
				paramTypes, convertedArguments);
			if (typeDiffWeight < minTypeDiffWeight) {
			    minTypeDiffWeight = typeDiffWeight;
			    matchingMethod = candidate;
			    argumentsToUse = convertedArguments;
			}
		    }
		}
	    }
	}
	if (matchingMethod != null) {
	    return argumentsToUse;
	}
	return arguments;
    }
    
    /**
     * 此方法不可用
     * @param paramTypes
     * @param args
     * @return
     */
    private static int getTypeDifferenceWeight(Class<?>[] paramTypes, Object[] args) {
	int result = 0;
	for (int i = 0; i < paramTypes.length; i++) {
	    if (!ClassUtils.isAssignableValue(paramTypes[i], args[i])) {
		return 2147483647;
	    }
	    if (args[i] != null) {
		Class<?> paramType = paramTypes[i];
		Class<?> superClass = args[i].getClass().getSuperclass();
		while (superClass != null) {
		    if (paramType.equals(superClass)) {
			result += 2;
			superClass = null;
		    } else if (ClassUtils.isAssignable(paramType, superClass)) {
			result += 2;
			superClass = superClass.getSuperclass();
		    } else {
			superClass = null;
		    }
		}
		if (paramType.isInterface()) {
		    result++;
		}
	    }
	}
	return result;
    }
    
    /**
     * Clob转换为字符串
     * @param clob
     * @return
     */
    public static String getClobString(Clob clob) {
	try {
	    StringBuilder sb = new StringBuilder();
	    Long leftLen = Long.valueOf(clob.length());
	    int subLen = leftLen.longValue() > 2147483647L ? 2147483647
		    : leftLen.intValue();
	    while (leftLen.longValue() > 0L) {
		sb.append(clob.getSubString(1L, subLen));
		leftLen = Long.valueOf(leftLen.longValue() - subLen);
		subLen = leftLen.longValue() > 2147483647L ? 2147483647
			: leftLen.intValue();
	    }
	    return sb.toString();
	} catch (SQLException e) {
	    throw new RuntimeException(e);
	}
    }
    
    /**
     * 获取Java实体UUID的属性名称
     * 只适用于Hibernate Annotation
     * @param <T>
     * @param entityClass
     * @return
     */
    public static <T extends Serializable> String getEntityUuidPropertyName(
	    Class<T> entityClass) {
	try {
	    for (Method method : entityClass.getDeclaredMethods()) {
		for (Annotation annotation : method.getAnnotations()) {
		    if (annotation.toString().contains(
			    "@javax.persistence.Id()")) {
			String uuidPropetyName = method.getName().substring(
				method.getName().indexOf("get") + 3);
			return uuidPropetyName.substring(0, 1).toLowerCase()
				+ uuidPropetyName.substring(1);
		    }
		}
	    }
	} catch (Throwable e) {
	    throw new ShipSuiteRuntimeException(e);
	}
	return null;
    }
    
    /**
     * 获取Java实体UUID的属性名称
     * 只适用于Hibernate Annotation
     * @param <T>
     * @param entity
     * @return
     */
    public static <T extends Serializable> String getEntityUuidPropertyName(
	    T entity) {
	try {
	    for (Method method : entity.getClass().getDeclaredMethods()) {
		for (Annotation annotation : method.getAnnotations()) {
		    if (annotation.toString().contains(
			    "@javax.persistence.Id()")) {
			return method.getName().substring(
				method.getName().indexOf("get") + 3);
		    }
		}
	    }
	} catch (Throwable e) {
	    throw new ShipSuiteRuntimeException(e);
	}
	return null;
    }
    
    /**
     * 获取Java实体UUID的属性值
     * @param <T>
     * @param entity Java实体
     * @return
     */
    public static <T> String getEntityUuidPropertyValue(T entity) {
	if (entity == null) {
	    return null;
	}
	try {
	    for (Method method : entity.getClass().getDeclaredMethods()) {
		for (Annotation annotation : method.getAnnotations()) {
		    if (annotation.toString().contains(
			    "@javax.persistence.Id()")) {
			CommonInvocationHandler<T> invocation = new CommonInvocationHandler<T>();
			invocation.bind(entity);
			return (String) invocation
				.invoke(method, new Object[0]);
		    }
		}
	    }
	} catch (Throwable e) {
	    throw new ShipSuiteRuntimeException(e);
	}
	return null;
    }
    
    /**
     * 获取实体类UUID的值
     * @param element			实体类
     * @param accessPropertyName	属性名
     * @return
     */
    public static String getAccessUnitUuid(Object element,
	    String accessPropertyName) {
	if ((element == null) || ((element instanceof Long))
		|| ((element instanceof Integer))
		|| (element.getClass().isPrimitive())
		|| (element.getClass().isArray())
		|| ((element instanceof Collection))) {
	    return null;
	}
	try {
	    Object accessUnit = getProperty(element, accessPropertyName);
	    String accessUnitUuid = null;
	    if ((accessUnit instanceof String)) {
		accessUnitUuid = accessUnit.toString();
	    }
	    return getEntityUuidPropertyValue(accessUnit);
	} catch (Exception e) {
	    throw new ShipSuiteRuntimeException(e);
	}
    }
    
    /**
     * 是否是基本数据类型
     * @param bean Object对象
     * @return
     */
    public static boolean isBasicType(Object bean) {
	if ((bean instanceof String)) {
	    return true;
	}
	if ((bean instanceof Boolean)) {
	    return true;
	}
	if ((bean instanceof Character)) {
	    return true;
	}
	if ((bean instanceof Byte)) {
	    return true;
	}
	if ((bean instanceof Short)) {
	    return true;
	}
	if ((bean instanceof Integer)) {
	    return true;
	}
	if ((bean instanceof Long)) {
	    return true;
	}
	if ((bean instanceof Float)) {
	    return true;
	}
	if ((bean instanceof Double)) {
	    return true;
	}
	if ((bean instanceof java.util.Date)) {
	    return true;
	}
	if ((bean instanceof java.sql.Date)) {
	    return true;
	}
	if ((bean instanceof Calendar)) {
	    return true;
	}
	if ((bean instanceof Void)) {
	    return true;
	}
	return bean.getClass().isPrimitive();
    }
    
    /**
     * 设置没有初始化的属性为NULL
     * @param bean
     */
    public static void setUninitializeProperties2null(Object bean) {
	Stack stack = new Stack();

	copyProperties(bean, bean, stack, true);
    }

    private static void copyProperties(Object beanSource, Object beanTarget,
	    Stack stack, boolean forceUninitialized2null) {
	if (stack.contains(beanSource)) {
	    return;
	}
	stack.add(beanSource);
	if (beanSource == null) {
	    return;
	}
	if (beanSource.getClass().isArray()) {
	    for (Object obj : (Object[]) beanSource) {
		copyProperties(obj, beanTarget, stack, forceUninitialized2null);
	    }
	} else if ((beanSource instanceof Collection)) {
	    for (Object obj : (Collection<?>) beanSource) {
		copyProperties(obj, beanTarget, stack, forceUninitialized2null);
	    }
	} else {
	    Method[] methodsSource = beanSource.getClass().getMethods();
	    CommonInvocationHandler invocationSource = new CommonInvocationHandler();
	    invocationSource.bind(beanSource);
	    Method[] methodsTarget = beanTarget.getClass().getMethods();
	    CommonInvocationHandler invocationTarget = new CommonInvocationHandler();
	    invocationTarget.bind(beanTarget);

	    Map<Method, Method> methodMap = new HashMap(methodsSource.length);
	    for (Method getMethod : methodsSource) {
		String setName = null;
		if ((getMethod.getName().startsWith("get"))
			&& (getMethod.getParameterTypes().length == 0)) {
		    setName = "set" + getMethod.getName().substring(3);
		} else if ((getMethod.getName().startsWith("is"))
			&& (getMethod.getParameterTypes().length == 0)) {
		    setName = "set" + getMethod.getName().substring(2);
		}
		if (StringUtil.isNotNullAndNotEmpty(setName)) {
		    for (Method setMethod : methodsTarget) {
			if ((setMethod.getName().equals(setName))
				&& (setMethod.getParameterTypes().length == 1)) {
			    methodMap.put(getMethod, setMethod);
			}
		    }
		}
	    }
	    for (Map.Entry<Method, Method> entry : methodMap.entrySet()) {
		Method getMethod = (Method) entry.getKey();
		Method setMethod = (Method) entry.getValue();
		Object propertyValueSource = null;
		try {
		    propertyValueSource = invocationSource.invoke(getMethod,
			    new Object[0]);
		} catch (Throwable localThrowable1) {
		}
		try {
		    if (!Hibernate.isInitialized(propertyValueSource)) {
			if ((forceUninitialized2null)
				|| ((propertyValueSource instanceof PersistentSet))) {
			    invocationTarget.invoke(setMethod, new Object[1]);
			} else {
			    invocationTarget.invoke(setMethod,
				    new Object[] { propertyValueSource });
			}
		    } else if ((propertyValueSource != null)
			    && (!isBasicType(propertyValueSource))
			    && (!propertyValueSource.getClass().isEnum())
			    && (forceUninitialized2null)) {
			if (propertyValueSource.getClass().isArray()) {
			    for (Object obj : (Object[]) propertyValueSource) {
				copyProperties(obj, obj, stack,
					forceUninitialized2null);
			    }
			} else if ((propertyValueSource instanceof Collection)) {
			    for (Object obj : (Collection<?>) propertyValueSource) {
				copyProperties(obj, obj, stack,
					forceUninitialized2null);
			    }
			} else {
			    invocationTarget.invoke(setMethod,
				    new Object[] { propertyValueSource });
			    if (forceUninitialized2null) {
				copyProperties(propertyValueSource,
					propertyValueSource, stack,
					forceUninitialized2null);
			    }
			}
		    } else {
			invocationTarget.invoke(setMethod,
				new Object[] { propertyValueSource });
		    }
		} catch (Throwable ex) {
		    try {
			invocationTarget.invoke(setMethod, new Object[1]);
		    } catch (Throwable e) {
			e.printStackTrace();
		    }
		}
	    }
	}
    }
    
    /**
     * 设置Bean的所有属性都为NULL
     * @param <T>
     * @param obj
     * @return
     */
    public static <T> T emptyProxy(T obj) {
	if (obj == null) {
	    return null;
	}
	T newObj = null;
	try {
	    newObj = deepClone(obj);
	    String[] properties = getProperties(obj.getClass());
	    for (String p : properties) {
		setProperty(newObj, p, null);
	    }
	    CommonInvocationHandler<T> invocation = new CommonInvocationHandler<T>();
	    return invocation.bind(newObj);
	} catch (Exception e) {
	    e.printStackTrace();
	}
	return newObj;
    }
    
    /**
     * 生成代理实体
     * @param cls
     * @return
     */
    public static Object newHashCglibEntiy(Class<?> cls) {
	try {
	    Object bean = cls.newInstance();

	    HashCglibProxy invocation = new HashCglibProxy();
	    return invocation.getInstance(bean);
	} catch (Exception e) {
	    throw new ShipSuiteRuntimeException(e);
	}
    }

    private static String getPropertyByMethod(Method method) {
	return method.getName().substring(3, 5).toLowerCase()
		+ method.getName().substring(4);
    }

    private static Class<?> getActualClass(Object bean) {
	Class<?> beanClass = bean.getClass();
	if (bean.getClass().getName().contains("$$EnhancerByCGLIB$$")) {
	    String beanClsName = bean
		    .getClass()
		    .getName()
		    .substring(
			    0,
			    bean.getClass().getName()
				    .indexOf("$$EnhancerByCGLIB$$"));
	    try {
		beanClass = bean.getClass().getClassLoader()
			.loadClass(beanClsName);
	    } catch (ClassNotFoundException e) {
		throw new ShipSuiteRuntimeException(e);
	    }
	}
	return beanClass;
    }
    
    /**
     * 获取属性名的值
     * @param bean Bean对象
     * @param property 属性名
     * @return
     */
    public static Object getPropertyByMethod(Object bean, String property) {
	try {
	    CommonInvocationHandler<Object> invocation = new CommonInvocationHandler<Object>();
	    invocation.bind(bean);
	    String[] getMethodNames = getPropertyGetMethodName(property);

	    Class<?> beanClass = getActualClass(bean);
	    for (Method method : beanClass.getMethods()) {
		if ((method.getName().equals(getMethodNames[0]))
			|| ((method.getName().equals(getMethodNames[1])) && (method
				.getParameterTypes().length == 0))) {
		    return invocation.invoke(method, null);
		}
	    }
	    return null;
	} catch (Throwable ex) {
	    throw new ShipSuiteRuntimeException(ex);
	}
    }
    
    /**
     * 根据属性名得到GET方法的名称
     * @param property
     * @return
     */
    public static String[] getPropertyGetMethodName(String property) {
	String name = property.substring(0, 1).toUpperCase()
		+ property.substring(1);
	return new String[] { "get" + name, "is" + name };
    }
    
    /**
     * 根据属性名得到SET方法的名称
     * @param property
     * @return
     */
    public static String getPropertySetMethodName(String property) {
	String name = property.substring(0, 1).toUpperCase()
		+ property.substring(1);
	return "set" + name;
    }
    
    /**
     * 根据属性获取HashCode 可由此来判断两个对象的值是否相等
     * @param bean
     * @param ignoreCollectionProperty
     * @return
     */
    public static int hashCodeByProperties(Object bean,
	    boolean ignoreCollectionProperty) {
	Class<?> beanClass = bean.getClass();
	if (bean.getClass().getName().contains("$$EnhancerByCGLIB$$")) {
	    String beanClsName = bean
		    .getClass()
		    .getName()
		    .substring(
			    0,
			    bean.getClass().getName()
				    .indexOf("$$EnhancerByCGLIB$$"));
	    try {
		beanClass = bean.getClass().getClassLoader()
			.loadClass(beanClsName);
	    } catch (ClassNotFoundException e) {
		throw new ShipSuiteRuntimeException(e);
	    }
	}
	Map<String, Object> propertyValueMap = new HashMap<String,Object>();
	for (Method method : beanClass.getDeclaredMethods()) {
	    if (((method.toString().startsWith("public ")) && (method.getName()
		    .startsWith("get")))
		    || ((method.getName().startsWith("is"))
			    && (method.getParameterTypes().length == 0) && ((!ignoreCollectionProperty) || (!Collection.class
			    .isAssignableFrom(method.getReturnType()))))) {
		try {
		    Object propertyValue = method.invoke(bean, null);
		    propertyValueMap.put(getPropertyByMethod(method),
			    propertyValue);
		} catch (Exception localException) {
		}
	    }
	}
	return hashCodeByPropertyValues(beanClass, propertyValueMap);
    }
    
    /**
     * 根据属性值来生成HashCode
     * @param beanClass
     * @param propertyValueMap
     * @return
     */
    public static int hashCodeByPropertyValues(Class<?> beanClass,
	    Map<String, Object> propertyValueMap) {
	String beanClassName = beanClass.getName();
	if (beanClassName.contains("$$EnhancerByCGLIB$$")) {
	    beanClassName = beanClass.getName().substring(0,
		    beanClass.getName().indexOf("$$EnhancerByCGLIB$$"));
	}
	int result = 1;
	if ((propertyValueMap != null) && (!propertyValueMap.isEmpty())) {
	    List<String> propertyNameList = new ArrayList<String>(
		    propertyValueMap.keySet());
	    Collections.sort(propertyNameList);
	    for (String p : propertyNameList) {
		Object v = propertyValueMap.get(p);
		if (v != null) {
		    result = 31 * result + v.hashCode();
		}
	    }
	}
	result = (beanClassName = beanClassName + "@" + result).hashCode();
	return result;
    }

    private static String getParentPropertyName(String propertyName) {
	if (propertyName.contains(".")) {
	    int index = propertyName.lastIndexOf(".");
	    return propertyName.substring(0, index);
	}
	return null;
    }

    private static String getPurePropertyName(String propertyName) {
	if (propertyName.contains(".")) {
	    int index = propertyName.lastIndexOf(".");
	    return propertyName.substring(index + 1);
	}
	return propertyName;
    }
    
    /**
     * 生成Bean的代理对象
     * @param cls
     * @return
     */
    public static Object newHashCglibBean(Class cls) {
	try {
	    Object bean = cls.newInstance();

	    HashCglibProxy invocation = new HashCglibProxy();
	    return invocation.getInstance(bean);
	} catch (Exception e) {
	    throw new ShipSuiteRuntimeException(e);
	}
    }
    
    /**
     * 设置属性值
     * @param bean	Bean对象
     * @param property	属性名
     * @param value	属性值
     * @return	是否成功
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     * @throws NoSuchMethodException
     * @throws InstantiationException
     */
    public static boolean setPropertyByMethod(Object bean, String property,
	    Object value) throws IllegalAccessException,
	    InvocationTargetException, NoSuchMethodException,
	    InstantiationException {
	Object orgiValue = getPropertyByMethod(bean, property);
	boolean isChanged;
	if (value != null) {
	    isChanged = !value.equals(orgiValue);
	} else {
	    isChanged = value != orgiValue;
	}
	if (!isChanged) {
	    return isChanged;
	}
	try {
	    CommonInvocationHandler<Object> invocation = new CommonInvocationHandler<Object>();
	    invocation.bind(bean);
	    String setMethodNames = getPropertySetMethodName(property);
	    Class<?> beanClass = getActualClass(bean);
	    for (Method method : beanClass.getMethods()) {
		if ((method.getName().equals(setMethodNames))
			&& (method.getParameterTypes().length == 1)) {
		    invocation.invoke(method, new Object[] { value });
		    return true;
		}
	    }
	    return false;
	} catch (IllegalArgumentException ex) {
	    if ((ex.getMessage().startsWith("Null property value for"))
		    && (property.contains("."))) {
		String parentProperty = property.substring(0,
			property.lastIndexOf("."));

		Class<?> parentClass = getPropertyType(bean, parentProperty);

		Object parentObject = parentClass.newInstance();

		String nestedProperty = property.substring(property
			.lastIndexOf(".") + 1);

		setProperty(parentObject, nestedProperty, value);

		setProperty(bean, parentProperty, parentObject);
	    }
	} catch (Throwable ex) {
	    throw new ShipSuiteRuntimeException(ex);
	}

	return isChanged;
    }
    
   /**
    * 将一个 Map 对象转化为一个 JavaBean
    * @param <T>
    * @param type
    * @param map
    * @return
    * @throws IntrospectionException
    * @throws IllegalAccessException
    * @throws InstantiationException
    * @throws InvocationTargetException
    */
   public static <T> T convertMap(Class<T> type, Map<Object,Object> map)
           throws IntrospectionException, IllegalAccessException,
           InstantiationException, InvocationTargetException {
       BeanInfo beanInfo = Introspector.getBeanInfo(type); // 获取类属性
       T obj = type.newInstance(); // 创建 JavaBean 对象

       // 给 JavaBean 对象的属性赋值
       PropertyDescriptor[] propertyDescriptors =  beanInfo.getPropertyDescriptors();
       for (int i = 0; i< propertyDescriptors.length; i++) {
           PropertyDescriptor descriptor = propertyDescriptors[i];
           String propertyName = descriptor.getName();

           if (map.containsKey(propertyName)) {
               // 下面一句可以 try 起来，这样当一个属性赋值失败的时候就不会影响其他属性赋值。
               Object value = map.get(propertyName);

               Object[] args = new Object[1];
               args[0] = value;

               descriptor.getWriteMethod().invoke(obj, args);
           }
       }
       return obj;
   }

   /**
    * 将一个 JavaBean 对象转化为一个  Map
    * @param bean Bean对象
    * @return
    * @throws IntrospectionException
    * @throws IllegalAccessException
    * @throws InvocationTargetException
    */
   public static Map<String,Object> convertBean(Object bean)
           throws IntrospectionException, IllegalAccessException, InvocationTargetException {
       Class<?> type = bean.getClass();
       Map<String,Object> returnMap = new HashMap<String,Object>();
       BeanInfo beanInfo = Introspector.getBeanInfo(type);

       PropertyDescriptor[] propertyDescriptors =  beanInfo.getPropertyDescriptors();
       for (int i = 0; i< propertyDescriptors.length; i++) {
           PropertyDescriptor descriptor = propertyDescriptors[i];
           String propertyName = descriptor.getName();
           if (!propertyName.equals("class")) {
               Method readMethod = descriptor.getReadMethod();
               Object result = readMethod.invoke(bean, new Object[0]);
               if (result != null) {
                   returnMap.put(propertyName, result);
               } else {
                   returnMap.put(propertyName, "");
               }
           }
       }
       return returnMap;
   }
}
