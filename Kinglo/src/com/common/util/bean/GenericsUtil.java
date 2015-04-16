package com.common.util.bean;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * 通用辅助类
 * @author zou
 * 2015-2-6
 */
public class GenericsUtil {
    
    /**
     * 获取父类泛型类型
     * @param clazz	class
     * @param index	为泛型的索引
     * @return 返回index位置的泛型类型
     */
    public static Class<?> getSuperClassGenricType(Class<?> clazz, int index) {
	Type genType = clazz.getGenericSuperclass();
	if (!(genType instanceof ParameterizedType)) {
	    return Object.class;
	}
	Type[] params = ((ParameterizedType) genType).getActualTypeArguments();
	if ((index >= params.length) || (index < 0)) {
	    throw new RuntimeException("你输入的索引"
		    + (index < 0 ? "不能小于0" : "超出了参数的总数"));
	}
	if (!(params[index] instanceof Class)) {
	    return Object.class;
	}
	return (Class<?>) params[index];
    }
    
    /**
     * 获取第一个父类的泛型类型
     * @param clazz class
     * @return
     */
    public static Class<?> getSuperClassGenricType(Class<?> clazz) {
	return getSuperClassGenricType(clazz, 0);
    }
    
    /**
     * 获取方法返回参数的泛型类型
     * @param method
     * @param index
     * @return
     */
    public static Class<?> getMethodGenericReturnType(Method method, int index) {
	Type returnType = method.getGenericReturnType();
	if ((returnType instanceof ParameterizedType)) {
	    ParameterizedType type = (ParameterizedType) returnType;
	    Type[] typeArguments = type.getActualTypeArguments();
	    if ((index >= typeArguments.length) || (index < 0)) {
		throw new RuntimeException("你输入的索引"
			+ (index < 0 ? "不能小于0" : "超出了参数的总数"));
	    }
	    return (Class<?>) typeArguments[index];
	}
	return Object.class;
    }
    
    /**
     * 获取方法返回参数的第一个泛型的类型
     * @param method
     * @return
     */
    public static Class<?> getMethodGenericReturnType(Method method) {
	return getMethodGenericReturnType(method, 0);
    }
    
    /**
     * 获取方法参数的泛型类型
     * @param method	
     * @param index
     * @return
     */
    public static List<Class<?>> getMethodGenericParameterTypes(Method method,
	    int index) {
	List<Class<?>> results = new ArrayList<Class<?>>();
	Type[] genericParameterTypes = method.getGenericParameterTypes();
	if ((index >= genericParameterTypes.length) || (index < 0)) {
	    throw new RuntimeException("你输入的索引"
		    + (index < 0 ? "不能小于0" : "超出了参数的总数"));
	}
	Type genericParameterType = genericParameterTypes[index];
	if ((genericParameterType instanceof ParameterizedType)) {
	    ParameterizedType aType = (ParameterizedType) genericParameterType;
	    Type[] parameterArgTypes = aType.getActualTypeArguments();
	    for (Type parameterArgType : parameterArgTypes) {
		Class<?> parameterArgClass = (Class<?>) parameterArgType;
		results.add(parameterArgClass);
	    }
	    return results;
	}
	return results;
    }
    
    /**
     * 获取方法参数的第一个泛型类型
     * @param method
     * @return
     */
    public static List<Class<?>> getMethodGenericParameterTypes(Method method) {
	return getMethodGenericParameterTypes(method, 0);
    }
    
    /**
     * 获取属性的泛型类型
     * @param field
     * @param index
     * @return
     */
    public static Class<?> getFieldGenericType(Field field, int index) {
	Type genericFieldType = field.getGenericType();
	if ((genericFieldType instanceof ParameterizedType)) {
	    ParameterizedType aType = (ParameterizedType) genericFieldType;
	    Type[] fieldArgTypes = aType.getActualTypeArguments();
	    if ((index >= fieldArgTypes.length) || (index < 0)) {
		throw new RuntimeException("你输入的索引"
			+ (index < 0 ? "不能小于0" : "超出了参数的总数"));
	    }
	    return (Class<?>) fieldArgTypes[index];
	}
	return Object.class;
    }
    
    /**
     * 获取属性的第一个泛型类型
     * @param field
     * @return
     */
    public static Class<?> getFieldGenericType(Field field) {
	return getFieldGenericType(field, 0);
    }
}
