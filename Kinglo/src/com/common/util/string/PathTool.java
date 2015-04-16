package com.common.util.string;

import java.io.File;
import java.net.URL;
import java.security.CodeSource;
import java.security.ProtectionDomain;

@SuppressWarnings("unchecked")
public class PathTool {

    /**
     * 方法描述：根据class获取web应用的绝对路径 例如 D:\work\testPE\WebRoot\
     * 
     * @return String 绝对路径
     */
    public static String getWebRootPath() {
	String classPath = getClassPath();
	String webInfUrl = classPath.substring(0, classPath.indexOf("classes"));
	String webrootUrl = webInfUrl
		.substring(0, classPath.indexOf("WEB-INF"));
	return webrootUrl;
    }

    /**
     * 方法描述：获取WEB-INF的绝对路径 例如 D:\work\testPE\WebRoot\WEB-INF\
     * 
     * @return String 获取WEB-INF的绝对路径
     */
    public static String getWebInfPath() {
	String classPath = getClassPath();
	String webInfUrl = classPath.substring(0, classPath.indexOf("classes"));
	return webInfUrl;
    }

    /**
     * 方法描述：获取conf的绝对路径 例如 D:\work\testPE\WebRoot\conf\
     * 
     * @return String 获取conf的绝对路径
     */
    public static String getConfPath() {
	String classPath = getClassPath();

	String webInfUrl = classPath.substring(0, classPath.indexOf("classes"));
	String webrootUrl = webInfUrl
		.substring(0, classPath.indexOf("WEB-INF"));
	String confUrl = webrootUrl + "conf/";
	return confUrl;
    }

    /**
     * 方法描述：获取web应用下的class路径绝对路径 例如 D:\work\testPE\WebRoot\WEB-INF\classes\
     * 
     * @return ClassPath 绝对路径
     */
    public static String getClassPath() {
	String classPath = getClassPath(PathTool.class);
	String classPathUrl = null;
	try {
	    classPathUrl = classPath.substring(0, classPath.indexOf("classes"));
	} catch (Exception e) {
	    File file = new File("");
	    classPathUrl = file.getAbsolutePath() + "/WebRoot/WEB-INF/";

	    // classPathUrl = Thread.currentThread().getContextClassLoader()
	    // .getResource("").getPath();
	}
	classPathUrl += "classes/";
	return classPathUrl;
    }

    /**
     * 方法描述：根据class获取web应用的路径绝对路径
     * 
     * @param c
     *            用来定位的类
     * @return ClassPath 绝对路径
     */
    private static String getClassPath(Class<?> c) {
	ProtectionDomain pd = c.getProtectionDomain();

	if (pd == null) {
	    return null;
	}
	CodeSource cs = pd.getCodeSource();

	if (cs == null) {
	    return null;
	}

	URL url = cs.getLocation();
	if (url == null) {
	    return null;
	}
	String classPath = url.getPath();

	return classPath;
    }
}
