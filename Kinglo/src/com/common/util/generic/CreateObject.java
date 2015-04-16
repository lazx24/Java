package com.common.util.generic;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * 创建对象
 * @author zou
 * 2015-2-3
 */
public final class CreateObject {
	
	private CreateObject(){
		
	}
	
	/**
	 * 创建对象
	 * @param className
	 * @return
	 * @throws Exception
	 */
	public static Object createObject(String className) throws Exception{
		return createObject(Class.forName(className));
	}
	
	/**
	 * 创建对象
	 * @param classes
	 * @return
	 * @throws Exception
	 */
	public static Object createObject(Class<?> classes) throws Exception{
		return classes.newInstance();
	}
	
	/**
	 * 创建对象
	 * @param className
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public static Object createObject(String className,Object[] params) throws Exception{
		return createObject(Class.forName(className),params);
	}
	
	/**
	 * 创建对象
	 * @param classes
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public static Object createObject(Class<?> classes,Object[] params) throws Exception{
		Constructor[] constructor=classes.getConstructors();
		Object object=null;
		try {
			for (int i = 0; i < constructor.length; i++) {
				object=constructor[i].newInstance(params);
			}
		} catch (Exception e) {
			if(e instanceof InvocationTargetException){
				((InvocationTargetException) e).getTargetException().printStackTrace();
			}
		}
		if(object==null){
			throw new InstantiationException();
		}
		return object;
	}
}
