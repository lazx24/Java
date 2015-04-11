package com.coscon.shipsuite.common.util.bean;

import com.coscon.shipsuite.common.exception.ShipSuiteRuntimeException;
import com.coscon.shipsuite.common.log.ISystemLogger;
import com.coscon.shipsuite.common.log.LoggerFactory;
import com.coscon.shipsuite.common.util.string.StringUtil;
import com.thoughtworks.xstream.XStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public final class ObjectUtil {
    private static final String JAVASSIT_SYMBOL_EXP = "_\\$\\$_javassist_\\d+";
    private static final ISystemLogger logger = LoggerFactory
	    .getSystemLogger(ObjectUtil.class);
    
    /**
     * 对象序列化
     * @param obj
     * @return
     */
    public static byte[] serialize(Object obj) {
	if (obj == null) {
	    return null;
	}
	ByteArrayOutputStream baos = new ByteArrayOutputStream();
	ObjectOutputStream oos = null;
	try {
	    oos = new ObjectOutputStream(baos);
	    oos.writeObject(obj);

	    return baos.toByteArray();
	} catch (IOException e) {
	    throw new ShipSuiteRuntimeException(e);
	} finally {
	    if (oos != null) {
		try {
		    oos.close();
		} catch (IOException e) {
		    e.printStackTrace();
		}
	    }
	    try {
		baos.close();
	    } catch (IOException localIOException2) {
	    }
	}
    }
    
    
    /**
     * 获取所有序列化对象长度之和
     * @param objects
     * @return
     */
    public static long size(Object[] objects) {
	if ((objects == null) || (objects.length == 0)) {
	    return 0L;
	}
	long size = 0L;
	Object[] arrayOfObject = objects;
	int j = objects.length;
	for (int i = 0; i < j; i++) {
	    Object obj = arrayOfObject[i];
	    if (obj != null) {
		size += serialize(obj).length;
	    }
	}
	return size;
    }
    
    /**
     * 反序列化
     * @param data
     * @return
     */
    public static Object deserialize(byte[] data) {
	if (data == null) {
	    return null;
	}
	try {
	    ByteArrayInputStream bais = new ByteArrayInputStream(data);
	    ObjectInputStream ois = new ObjectInputStream(bais);
	    Object obj = ois.readObject();

	    ois.close();
	    return obj;
	} catch (Exception ex) {
	    throw new ShipSuiteRuntimeException(ex);
	}
    }
    
    /**
     * Object对象转换成Xml
     * @param bean
     * @return
     */
    public static String object2xml(Object bean) {
	if (bean == null) {
	    return null;
	}
	XStream xstream = new XStream();
	String xml = xstream.toXML(bean);

	return xml.replaceAll("_\\$\\$_javassist_\\d+", "");
    }
    
    /**
     * Xml转换为Object
     * @param xml
     * @return
     */
    public static Object xml2object(String xml) {
	if (StringUtil.isNullOrEmpty(xml)) {
	    return null;
	}
	try {
	    xml = xml.replaceAll("_\\$\\$_javassist_\\d+", "");
	    XStream xstream = new XStream();
	    return xstream.fromXML(xml);
	} catch (Exception ex) {
	    logger.error(xml, ex);
	    throw new ShipSuiteRuntimeException(ex);
	}
    }
}
