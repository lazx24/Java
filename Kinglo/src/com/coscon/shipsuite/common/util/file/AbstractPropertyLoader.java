package com.coscon.shipsuite.common.util.file;

import com.coscon.shipsuite.common.exception.ShipSuiteRuntimeException;
import com.coscon.shipsuite.common.util.cache.CacheManager;
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

    public static String getApplicationResource(String key, Locale localeType) {
	String propertites = String.format(
		"properties/messageResource_%s.properties",
		new Object[] { localeType.toString() });

	return getApplicationResource(key, localeType, propertites);
    }

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

    public static String getApplicationResource(String key,
	    String[] messageArgs, Locale localeType) {
	String propertites = String.format(
		"properties/messageResource_%s.properties",
		new Object[] { localeType.toString() });

	return getApplicationResource(key, messageArgs, localeType, propertites);
    }

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

    public static String getDefaultPropertyDir() {
	return defaultPropertyDir;
    }

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

    public static void setDefaultPropertyDir(String propertyDir) {
	defaultPropertyDir = propertyDir;
    }

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

    protected Enumeration<Object> getKeys(Properties myResource) {
	if (myResource != null) {
	    return myResource.keys();
	}
	return null;
    }

    /* Error */
    public static String getXmlConfig(String xmlConfigFilename, String configKey) {
	// Byte code:
	// 0: aload_0
	// 1: invokestatic 44
	// com/coscon/shipsuite/common/util/file/AbstractPropertyLoader:loadFile
	// (Ljava/lang/String;)Ljava/io/InputStream;
	// 4: astore_2
	// 5: aload_2
	// 6: ifnonnull +40 -> 46
	// 9: new 64 java/lang/StringBuilder
	// 12: dup
	// 13: invokestatic 337
	// com/coscon/shipsuite/common/util/file/AbstractPropertyLoader:getDefaultPropertyDir
	// ()Ljava/lang/String;
	// 16: invokestatic 206 java/lang/String:valueOf
	// (Ljava/lang/Object;)Ljava/lang/String;
	// 19: invokespecial 68 java/lang/StringBuilder:<init>
	// (Ljava/lang/String;)V
	// 22: aload_0
	// 23: aload_0
	// 24: ldc 120
	// 26: invokevirtual 339 java/lang/String:lastIndexOf
	// (Ljava/lang/String;)I
	// 29: iconst_1
	// 30: iadd
	// 31: invokevirtual 343 java/lang/String:substring
	// (I)Ljava/lang/String;
	// 34: invokevirtual 71 java/lang/StringBuilder:append
	// (Ljava/lang/String;)Ljava/lang/StringBuilder;
	// 37: invokevirtual 77 java/lang/StringBuilder:toString
	// ()Ljava/lang/String;
	// 40: astore_3
	// 41: aload_3
	// 42: invokestatic 44
	// com/coscon/shipsuite/common/util/file/AbstractPropertyLoader:loadFile
	// (Ljava/lang/String;)Ljava/io/InputStream;
	// 45: astore_2
	// 46: aconst_null
	// 47: astore_3
	// 48: aload_2
	// 49: ifnonnull +29 -> 78
	// 52: aload_2
	// 53: ifnull +12 -> 65
	// 56: aload_2
	// 57: invokevirtual 52 java/io/InputStream:close ()V
	// 60: goto +5 -> 65
	// 63: astore 8
	// 65: aload_3
	// 66: ifnull +10 -> 76
	// 69: aload_3
	// 70: invokeinterface 384 1 0
	// 75: pop
	// 76: aconst_null
	// 77: areturn
	// 78: new 390 org/dom4j/io/SAXReader
	// 81: dup
	// 82: invokespecial 392 org/dom4j/io/SAXReader:<init> ()V
	// 85: astore 4
	// 87: aload 4
	// 89: aload_2
	// 90: invokevirtual 393 org/dom4j/io/SAXReader:read
	// (Ljava/io/InputStream;)Lorg/dom4j/Document;
	// 93: astore_3
	// 94: aload_3
	// 95: invokeinterface 396 1 0
	// 100: astore 5
	// 102: aload 5
	// 104: aload_1
	// 105: invokestatic 400
	// com/coscon/shipsuite/common/util/file/AbstractPropertyLoader:recuriseElement
	// (Lorg/dom4j/Element;Ljava/lang/String;)Ljava/lang/String;
	// 108: astore 7
	// 110: aload_2
	// 111: ifnull +12 -> 123
	// 114: aload_2
	// 115: invokevirtual 52 java/io/InputStream:close ()V
	// 118: goto +5 -> 123
	// 121: astore 8
	// 123: aload_3
	// 124: ifnull +10 -> 134
	// 127: aload_3
	// 128: invokeinterface 384 1 0
	// 133: pop
	// 134: aload 7
	// 136: areturn
	// 137: astore 4
	// 139: new 354
	// com/coscon/shipsuite/common/exception/ShipSuiteRuntimeException
	// 142: dup
	// 143: new 64 java/lang/StringBuilder
	// 146: dup
	// 147: ldc_w 356
	// 150: invokespecial 68 java/lang/StringBuilder:<init>
	// (Ljava/lang/String;)V
	// 153: aload_0
	// 154: invokevirtual 71 java/lang/StringBuilder:append
	// (Ljava/lang/String;)Ljava/lang/StringBuilder;
	// 157: invokevirtual 77 java/lang/StringBuilder:toString
	// ()Ljava/lang/String;
	// 160: aload 4
	// 162: invokespecial 404
	// com/coscon/shipsuite/common/exception/ShipSuiteRuntimeException:<init>
	// (Ljava/lang/String;Ljava/lang/Throwable;)V
	// 165: athrow
	// 166: astore 6
	// 168: aload_2
	// 169: ifnull +12 -> 181
	// 172: aload_2
	// 173: invokevirtual 52 java/io/InputStream:close ()V
	// 176: goto +5 -> 181
	// 179: astore 8
	// 181: aload_3
	// 182: ifnull +10 -> 192
	// 185: aload_3
	// 186: invokeinterface 384 1 0
	// 191: pop
	// 192: aload 6
	// 194: athrow
	// 195: astore_2
	// 196: new 354
	// com/coscon/shipsuite/common/exception/ShipSuiteRuntimeException
	// 199: dup
	// 200: new 64 java/lang/StringBuilder
	// 203: dup
	// 204: ldc 66
	// 206: invokespecial 68 java/lang/StringBuilder:<init>
	// (Ljava/lang/String;)V
	// 209: aload_0
	// 210: invokevirtual 71 java/lang/StringBuilder:append
	// (Ljava/lang/String;)Ljava/lang/StringBuilder;
	// 213: ldc 75
	// 215: invokevirtual 71 java/lang/StringBuilder:append
	// (Ljava/lang/String;)Ljava/lang/StringBuilder;
	// 218: invokevirtual 77 java/lang/StringBuilder:toString
	// ()Ljava/lang/String;
	// 221: aload_2
	// 222: invokespecial 404
	// com/coscon/shipsuite/common/exception/ShipSuiteRuntimeException:<init>
	// (Ljava/lang/String;Ljava/lang/Throwable;)V
	// 225: athrow
	// Line number table:
	// Java source line #361 -> byte code offset #0
	// Java source line #362 -> byte code offset #5
	// Java source line #363 -> byte code offset #9
	// Java source line #364 -> byte code offset #22
	// Java source line #365 -> byte code offset #24
	// Java source line #364 -> byte code offset #31
	// Java source line #363 -> byte code offset #37
	// Java source line #366 -> byte code offset #41
	// Java source line #368 -> byte code offset #46
	// Java source line #371 -> byte code offset #48
	// Java source line #383 -> byte code offset #52
	// Java source line #385 -> byte code offset #56
	// Java source line #386 -> byte code offset #63
	// Java source line #389 -> byte code offset #65
	// Java source line #390 -> byte code offset #69
	// Java source line #372 -> byte code offset #76
	// Java source line #374 -> byte code offset #78
	// Java source line #375 -> byte code offset #87
	// Java source line #376 -> byte code offset #94
	// Java source line #378 -> byte code offset #102
	// Java source line #383 -> byte code offset #110
	// Java source line #385 -> byte code offset #114
	// Java source line #386 -> byte code offset #121
	// Java source line #389 -> byte code offset #123
	// Java source line #390 -> byte code offset #127
	// Java source line #378 -> byte code offset #134
	// Java source line #379 -> byte code offset #137
	// Java source line #380 -> byte code offset #139
	// Java source line #381 -> byte code offset #153
	// Java source line #380 -> byte code offset #162
	// Java source line #382 -> byte code offset #166
	// Java source line #383 -> byte code offset #168
	// Java source line #385 -> byte code offset #172
	// Java source line #386 -> byte code offset #179
	// Java source line #389 -> byte code offset #181
	// Java source line #390 -> byte code offset #185
	// Java source line #391 -> byte code offset #192
	// Java source line #393 -> byte code offset #195
	// Java source line #395 -> byte code offset #196
	// Java source line #396 -> byte code offset #213
	// Java source line #395 -> byte code offset #222
	// Local variable table:
	// start length slot name signature
	// 0 226 0 xmlConfigFilename String
	// 0 226 1 configKey String
	// 4 169 2 inputStream InputStream
	// 195 27 2 e Exception
	// 40 2 3 newPath String
	// 47 139 3 document org.dom4j.Document
	// 85 3 4 saxReader org.dom4j.io.SAXReader
	// 137 24 4 ex1 Exception
	// 100 3 5 root Element
	// 166 27 6 localObject Object
	// 63 1 8 localException1 Exception
	// 121 1 8 localException2 Exception
	// 179 1 8 localException3 Exception
	// Exception table:
	// from to target type
	// 56 60 63 java/lang/Exception
	// 114 118 121 java/lang/Exception
	// 48 52 137 java/lang/Exception
	// 78 110 137 java/lang/Exception
	// 48 52 166 finally
	// 78 110 166 finally
	// 137 166 166 finally
	// 172 176 179 java/lang/Exception
	// 0 76 195 java/lang/Exception
	// 78 134 195 java/lang/Exception
	// 137 195 195 java/lang/Exception
	return "";
    }

    private static boolean hasAtributes(Element e, String key) {
	for (Object attr : e.attributes()) {
	    if (key.equals(((Attribute) attr).getValue())) {
		return true;
	    }
	}
	return false;
    }

//    private static String recuriseElement(Element element, String key) {
//	for (Element e : element.elements()) {
//	    if ((e.getName().equals(key)) || (hasAtributes(e, key))) {
//		return e.getStringValue().trim();
//	    }
//	    String v = recuriseElement(e, key);
//	    if (v != null) {
//		return v;
//	    }
//	}
//	return null;
//    }
}
