package com.common.util.serializable;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * 序列化
 * 
 * @author zou 2015-2-3
 */
public class SerializeUtil {
    
    /**
     * Object转换为字节数组
     * @param object Object对象
     * @return
     * @throws Exception
     */
    public static byte[] objectToByte(Object object) throws Exception {
	ByteArrayOutputStream byteArray = new ByteArrayOutputStream();
	ObjectOutputStream os = new ObjectOutputStream(byteArray);
	os.writeObject(object);
	return byteArray.toByteArray();
    }
    
    /**
     * 字节数组转换为Object
     * @param bytes	字节数组
     * @return
     * @throws Exception
     */
    public static Object byteToObject(byte[] bytes) throws Exception {
	ByteArrayInputStream in = new ByteArrayInputStream(bytes);
	ObjectInputStream objectInput = new ObjectInputStream(in);
	return objectInput.readObject();
    }
}
