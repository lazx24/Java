package com.test.util.bean;

import static org.junit.Assert.fail;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import org.junit.Test;

import com.common.util.bean.GenericsUtil;
import com.common.util.string.StringUtil;
import com.test.RedDog;

public class GenericsUtilTest {

    @Test
    public void testGetSuperClassGenricTypeClassOfQInt() {
	System.out.println(GenericsUtil.getSuperClassGenricType(RedDog.class, 0));
	
    }

    @Test
    public void testGetMethodGenericReturnTypeMethodInt() throws SecurityException, NoSuchMethodException {
	Method[] methods = RedDog.class.getMethods();
	for (int i = 0; i < methods.length; i++) {
	    if(StringUtil.isEqual(methods[i].getName(), "getObject")){
		System.out.println(GenericsUtil.getMethodGenericReturnType(methods[i], 0));
	    }
	}
    }

    @Test
    public void testGetMethodGenericParameterTypesMethodInt() {
	Method[] methods = RedDog.class.getMethods();
	for (int i = 0; i < methods.length; i++) {
	    if(StringUtil.isEqual(methods[i].getName(), "getParam")){
		System.out.println(GenericsUtil.getMethodGenericParameterTypes(methods[i], 0));
	    }
	}
    }

    @Test
    public void testGetFieldGenericTypeFieldInt() throws SecurityException, NoSuchFieldException {
	Field field=RedDog.class.getField("array");
	System.out.println(GenericsUtil.getFieldGenericType(field, 0));
    }

   

}
