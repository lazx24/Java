package com.common.util.zip;

import com.common.exception.ShipSuiteRuntimeException;
import com.common.util.string.StringUtil;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.zip.Deflater;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.Inflater;
import java.util.zip.InflaterInputStream;

/**
 * 压缩类
 * 类的描述:基础数据的压缩与解压缩
 * 创建人:邹建华
 * 创建时间:2015-4-11
 */
public final class ZipUtil {
    
    /**
     * 压缩字符串
     * @param data 	字符串数据
     * @return 		字符串
     */
    public static String compressString(String data) {
	if (StringUtil.isNullOrEmpty(data)) {
	    return data;
	}
	byte[] bytes = compress(StringUtil.stringToBytes(data));
	return StringUtil.bytesToString(bytes, "ISO-8859-1");
    }
    
    /**
     * 压缩字符串成字节数组
     * @param data 	字符串数据
     * @return 		字符串
     */
    public static byte[] compressString2Byte(String data) {
	if (data == null) {
	    return null;
	}
	return compress(StringUtil.stringToBytes(data));
    }
    
    /**
     * 字符串解压缩
     * @param data 	字符串数据
     * @return		字符串
     */
    public static String decompressString(String data) {
	if (StringUtil.isNullOrEmpty(data)) {
	    return data;
	}
	byte[] bytes = StringUtil.stringToBytes(data, "ISO-8859-1");
	return StringUtil.bytesToString(decompress(bytes));
    }
    
    /**
     * 字节数组解压缩为字符串
     * @param data 	字节数组
     * @return 		字符串
     */
    public static String decompressStringByBytes(byte[] data) {
	if (data == null) {
	    return null;
	}
	return StringUtil.bytesToString(decompress(data));
    }
    
    /**
     * 压缩字节数组成字节数组
     * @param data 	字节数组
     * @return 		字节数组
     */
    public static byte[] compress(byte[] data) {
	byte[] output = new byte[0];

	Deflater compresser = new Deflater();

	compresser.reset();
	compresser.setInput(data);
	compresser.finish();
	ByteArrayOutputStream bos = new ByteArrayOutputStream(data.length);
	try {
	    byte[] buf = new byte[1024];
	    while (!compresser.finished()) {
		int i = compresser.deflate(buf);
		bos.write(buf, 0, i);
	    }
	    output = bos.toByteArray();
	} catch (Exception e) {
	    output = data;
	    e.printStackTrace();
	    try {
		bos.close();
	    } catch (IOException ex) {
		ex.printStackTrace();
	    }
	} finally {
	    try {
		bos.close();
	    } catch (IOException e) {
		e.printStackTrace();
	    }
	}
	compresser.end();
	return output;
    }
    
    /**
     * 压缩字节数组输出流
     * @param data	字节数组
     * @param os	输出流
     */
    public static void compress(byte[] data, OutputStream os) {
	DeflaterOutputStream dos = new DeflaterOutputStream(os);
	try {
	    dos.write(data, 0, data.length);

	    dos.finish();

	    dos.flush();
	} catch (IOException e) {
	    throw new ShipSuiteRuntimeException(e);
	}
    }
    
    /**
     * 字节数组解压缩成字节数组
     * @param data	字节数组
     * @return		字节数组
     */
    public static byte[] decompress(byte[] data) {
	byte[] output = new byte[0];

	Inflater decompresser = new Inflater();
	decompresser.reset();
	decompresser.setInput(data);

	ByteArrayOutputStream o = new ByteArrayOutputStream(data.length);
	try {
	    byte[] buf = new byte[1024];
	    while (!decompresser.finished()) {
		int i = decompresser.inflate(buf);
		o.write(buf, 0, i);
	    }
	    output = o.toByteArray();
	} catch (Exception e) {
	    output = data;
	    e.printStackTrace();
	    try {
		o.close();
	    } catch (IOException ex) {
		ex.printStackTrace();
	    }
	} finally {
	    try {
		o.close();
	    } catch (IOException e) {
		e.printStackTrace();
	    }
	}
	decompresser.end();
	return output;
    }
    
    /**
     * 输入流解压缩成字节数组
     * @param is	输入流
     * @return		字节数组
     */
    public static byte[] decompress(InputStream is) {
	InflaterInputStream iis = new InflaterInputStream(is);
	ByteArrayOutputStream o = new ByteArrayOutputStream(1024);
	try {
	    int i = 1024;
	    byte[] buf = new byte[i];
	    while ((i = iis.read(buf, 0, i)) > 0) {
		o.write(buf, 0, i);
	    }
	} catch (IOException e) {
	    throw new ShipSuiteRuntimeException(e);
	}
	return o.toByteArray();
    }
    
    /**
     * 压缩对象成字节数组
     * @param obj	Object对象
     * @return		字节数组
     */
    public static byte[] compressObject(Object obj) {
	ByteArrayOutputStream baos = new ByteArrayOutputStream();
	ObjectOutputStream oos = null;
	try {
	    oos = new ObjectOutputStream(baos);
	    oos.writeObject(obj);

	    return compress(baos.toByteArray());
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
	    } catch (IOException e) {
		e.printStackTrace();
	    }
	}
    }
    
    /**
     * 字节数组解压缩成Object对象
     * @param data	字节数组
     * @return		Object对象
     */
    public static Object decompressObject(byte[] data) {
	try {
	    ByteArrayInputStream bais = new ByteArrayInputStream(
		    decompress(data));
	    ObjectInputStream ois = new ObjectInputStream(bais);
	    Object obj = ois.readObject();

	    ois.close();
	    return obj;
	} catch (Exception ex) {
	    throw new ShipSuiteRuntimeException(ex);
	}
    }
}
