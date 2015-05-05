package com.common.util.file;

import com.common.exception.ShipSuiteRuntimeException;
import com.common.util.cache.CacheManager;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Locale;
import java.util.Properties;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import org.dom4j.Attribute;
import org.dom4j.Element;
import org.simpleframework.xml.core.Persister;

public abstract class AbstractPropertyLoader {
    private static final ReentrantReadWriteLock locker = new ReentrantReadWriteLock();
    protected static final long duration = 60L;
    protected static final CacheManager<Properties> propertiesCacheManager = new CacheManager<Properties>();
    protected static volatile String defaultPropertyDir = "";
    
    /**
     * 加载属性文件
     * @param prpFileName 属性文件名
     * @return		  Properties
     */
    private static Properties loadPropertiesDirectly(String prpFileName) {
	String propertyFileName = prpFileName;

	Properties myResource = new Properties();
	try {
	    InputStream fis = loadFile(propertyFileName);
	    if (fis == null) {
		return null;
	    }
	    try {
		myResource.load(fis);
	    } catch (Exception ex1) {
		return null;
	    } finally {
		if (fis != null) {
		    try {
			fis.close();
		    } catch (Exception localException2) {
		    }
		}
	    }
	    try {
		fis.close();
	    } catch (Exception localException3) {
	    }
	    return myResource;
	} catch (Exception e) {
	    e.printStackTrace();
	    throw new RuntimeException("Loading " + prpFileName
		    + " file fails!");
	}
    }
    
    /**
     * 读取messageResource_Locale.properties并获取key的值
     * @param key		属性键
     * @param localeType	语言类型
     * @return
     */
    public static String getApplicationResource(String key, Locale localeType) {
	String propertites = String.format(
		"properties/messageResource_%s.properties",
		new Object[] { localeType.toString() });

	return getApplicationResource(key, localeType, propertites);
    }
    
    /**
     * 如果属性文件中没有这个键值  那么读取messageResourceFrm_Locale.properties并获取key的值
     * @param key		属性
     * @param localeType	 语言类型
     * @param propertyPath	属性文件名的路径
     * @return
     */
    public static String getApplicationResource(String key, Locale localeType,
	    String propertyPath) {
	String path = propertyPath;
	if (path.contains("//")) {
	    path = path.replaceAll("//", "/");
	}
	String keyValue = getProperty(path, key);
	if (keyValue == null) {
	    path = String.format("properties/messageFrm_%s.properties",
		    new Object[] { localeType.toString() });
	    try {
		keyValue = getProperty(path, key);
	    } catch (Exception ex) {
		keyValue = null;
	    }
	}
	if (keyValue == null) {
	    keyValue = key;
	}
	return keyValue;
    }
    
    /**
     * 读取messageResource_LocaleType.properties文件 传递参数 获取提示内容
     * @param key		键
     * @param messageArgs	消息参数
     * @param localeType	语言类型
     * @return			键的Value
     */
    public static String getApplicationResource(String key,
	    String[] messageArgs, Locale localeType) {
	String propertites = String.format(
		"properties/messageResource_%s.properties",
		new Object[] { localeType.toString() });

	return getApplicationResource(key, messageArgs, localeType, propertites);
    }
    
    /**
     * 读取属性文件 传递参数 获取提示内容
     * @param key		键
     * @param messageArgs	消息参数
     * @param localeType	语言类型
     * @param propertyPath	属性文件名
     * @return			键的Value
     */
    public static String getApplicationResource(String key,
	    String[] messageArgs, Locale localeType, String propertyPath) {
	if (key == null) {
	    return null;
	}
	String path = propertyPath;
	if (path.contains("//")) {
	    path = path.replaceAll("//", "/");
	}
	String keyValue = getProperty(path, key);
	if (keyValue == null) {
	    path = String.format("properties/messageFrm_%s.properties",
		    new Object[] { localeType.toString() });
	    try {
		keyValue = getProperty(path, key);
	    } catch (Exception ex) {
		keyValue = null;
	    }
	}
	StringBuilder sb = new StringBuilder();
	if ((keyValue != null) && (messageArgs != null)) {
	    for (int i = 0; i < messageArgs.length; i++) {
		sb.setLength(0);
		sb.append("\\{");
		sb.append(i);
		sb.append("\\}");

		keyValue = keyValue.replaceAll(sb.toString(), messageArgs[i]);
	    }
	}
	if (keyValue == null) {
	    keyValue = key;
	}
	return keyValue;
    }
    
    /**
     * 获取默认的属性目录
     * @return
     */
    public static String getDefaultPropertyDir() {
	return defaultPropertyDir;
    }
    
    /**
     * 获取键的值
     * @param filename	属性文件名
     * @param key	键
     * @return		键的Value
     */
    public static String getProperty(String filename, String key) {
	if (key == null) {
	    throw new RuntimeException("Can not load NULL key!");
	}
	Properties myResource = loadProperties(filename);
	if (myResource != null) {
	    String value = myResource.getProperty(key);
	    if (value != null) {
		return value.trim();
	    }
	}
	return null;
    }
    
    /**
     * 获取键的值 如果不存在 用默认值代替  属性文件会进入到缓存中 可再次读取
     * @param filename	属性文件名
     * @param key	键
     * @param defaultVal默认值
     * @return		键的Value
     */
    public static String getProperty(String filename, String key,
	    String defaultVal) {
	if (key == null) {
	    throw new RuntimeException("Can not load NULL key!");
	}
	Properties myResource = loadProperties(filename);
	if (myResource != null) {
	    String value = myResource.getProperty(key, defaultVal);
	    if (value != null) {
		return value.trim();
	    }
	}
	return defaultVal;
    }
    
    /**
     * 直接一次性读取  获取键的值 如果不存在 用默认值代替
     * @param filename	属性文件名
     * @param key	键
     * @param defaultVal默认值
     * @return		键的Value
     */
    public static String getPropertyDirectly(String filename, String key,
	    String defaultVal) {
	if (key == null) {
	    throw new RuntimeException("Can not load NULL key!");
	}
	Properties myResource = loadPropertiesDirectly(filename);
	if (myResource != null) {
	    String value = myResource.getProperty(key, defaultVal);
	    if (value != null) {
		return value.trim();
	    }
	}
	return defaultVal;
    }

    private static InputStream loadFile(String filename) {
	InputStream fis = null;
	try {
	    fis = new FileInputStream(new File(filename));
	} catch (FileNotFoundException ex1) {
	    fis = Thread.currentThread().getContextClassLoader()
		    .getResourceAsStream(filename);
	    if (fis == null) {
		return null;
	    }
	}
	return fis;
    }

    public static Properties loadProperties(String prpFileName) {
	if ((!prpFileName.contains("/")) && (!prpFileName.contains("\\"))) {
	    prpFileName = defaultPropertyDir + "/" + prpFileName;
	}
	Properties properties;
	try {
	    locker.readLock().lock();
	    properties = (Properties) propertiesCacheManager
		    .getValueWithinDuration(prpFileName);
	} finally {
	    locker.readLock().unlock();
	}
	if (properties == null) {
	    try {
		locker.writeLock().lock();
		properties = loadPropertiesDirectly(prpFileName);
		propertiesCacheManager.saveCacheObject(prpFileName, properties,
			60L);
	    } finally {
		locker.writeLock().unlock();
	    }
	}
	return properties;
    }
    
    /**
     * 设置默认的属性文件的目录路径
     * @param propertyDir	目录路径
     */
    public static void setDefaultPropertyDir(String propertyDir) {
	defaultPropertyDir = propertyDir;
    }
    
    /**
     * 设置属性
     * @param filename	属性文件名
     * @param key	键
     * @param value	值
     * @param writeBackToFile	是否写入到文件中
     */
    public static void setProperty(String filename, String key, String value,
	    boolean writeBackToFile) {
	if (key == null) {
	    throw new RuntimeException("Can not load NULL key!");
	}
	Properties myResource = loadProperties(filename);
	if (myResource != null) {
	    myResource.setProperty(key, value);
	    if (writeBackToFile) {
		BufferedReader in = null;
		String fileFullpath = AbstractPropertyLoader.class.getResource(
			"/").getPath()
			+ "/" + filename;

		List<String> contentList = new ArrayList<String>();
		try {
		    in = new BufferedReader(new FileReader(fileFullpath));
		    String keyValueString = null;
		    boolean overrided = false;
		    while ((keyValueString = in.readLine()) != null) {
			if (keyValueString.startsWith(key)) {
			    keyValueString = key + "=" + value;
			    overrided = true;
			}
			contentList.add(keyValueString);
		    }
		    in.close();
		    PrintWriter out = null;

		    out = new PrintWriter(new FileWriter(fileFullpath, false));
		    for (int i = 0; i < contentList.size(); i++) {
			out.println((String) contentList.get(i));
		    }
		    out.close();
		    if (!overrided) {
			keyValueString = String.format("\n%s=%s", new Object[] {
				key, value });
			FileUtil.writeFile(keyValueString, fileFullpath, true);
		    }
		} catch (Exception e) {
		    throw new RuntimeException(e);
		}
	    }
	}
    }
    
    /**
     * XML文件转换为对象
     * @param <T>
     * @param cls	Class对象
     * @param filePath	XML文件路径
     * @return		Java对象
     */
    public static <T> T xml2Object(Class<T> cls, String filePath) {
	T object = null;
	try {
	    InputStream inputStream = loadFile(filePath);
	    if (inputStream == null) {
		String newPath = getDefaultPropertyDir()
			+ filePath.substring(filePath.lastIndexOf("/") + 1);
		inputStream = loadFile(newPath);
	    }
	    try {
		if (inputStream == null) {
		    return object;
		}
		Persister serializer = new Persister();

		object = serializer.read(cls, inputStream);
	    } catch (IOException ex1) {
		throw new ShipSuiteRuntimeException("Error while read from "
			+ filePath);
	    } finally {
		if (inputStream != null) {
		    try {
			inputStream.close();
		    } catch (Exception localException2) {
		    }
		}
	    }
	    if (inputStream != null) {
		try {
		    inputStream.close();
		} catch (Exception localException3) {
		}
	    }
	    return object;
	} catch (Exception e) {
	    throw new ShipSuiteRuntimeException("Loading " + filePath
		    + " file fails!");
	}
    }
    
    /**
     * 获取属性文件中所有键的名称
     * @param myResource 属性文件
     * @return		 Enumeration
     */
    protected Enumeration<Object> getKeys(Properties myResource) {
	if (myResource != null) {
	    return myResource.keys();
	}
	return null;
    }
}
