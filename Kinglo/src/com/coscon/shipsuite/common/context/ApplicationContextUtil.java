package com.coscon.shipsuite.common.context;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import javax.servlet.ServletContext;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import com.coscon.shipsuite.common.extend.spring.AdvancedApplicationContext;
import com.coscon.shipsuite.common.util.cache.CacheManager;
import com.coscon.shipsuite.common.util.file.FileUtil;
import com.coscon.shipsuite.common.util.file.PropertyLoader;
import com.coscon.shipsuite.common.util.string.StringUtil;

public class ApplicationContextUtil {
    private static AdvancedApplicationContext applicationContextByEnv = null;
    private static final ReentrantReadWriteLock locker = new ReentrantReadWriteLock();
    private static final CacheManager<Set<String>> cacheManager = new CacheManager<Set<String>>();

    public static final String getRealPath(String path) {
	ServletContext context = getServletContext();
	if (context != null) {
	    return context.getRealPath(path);
	}
	return new File("").getAbsolutePath();
    }

    public static final ServletContext getServletContext() {
	WebApplicationContext context = getWebApplicationContext();

	return context == null ? null : context.getServletContext();
    }

    public static final AdvancedApplicationContext getSpringWebContext() {
	try {
	    return AdvancedApplicationContext
		    .getInstance(WebApplicationContextUtils
			    .getRequiredWebApplicationContext(getServletContext()));
	} catch (Exception ex) {
	}
	return getSpringWebContext(null);
    }

    public static final AdvancedApplicationContext getSpringWebContext(
	    String env) {
	locker.readLock().lock();
	if (applicationContextByEnv == null) {
	    locker.readLock().unlock();
	    try {
		locker.writeLock().lock();
		if (applicationContextByEnv == null) {
		    Set<String> fileSet = getSpringWebConfigurationXmlSet(env);
		    String[] classPaths = {
			    FileUtil.FILE_SEPARATOR + "bin"
				    + FileUtil.FILE_SEPARATOR,
			    FileUtil.FILE_SEPARATOR + "classes"
				    + FileUtil.FILE_SEPARATOR };
		    for (String classPath : classPaths) {
			for (String f : fileSet) {
			    int index = f.indexOf(classPath);
			    if (index >= 0) {
				f = f.substring(index + classPath.length());

				fileSet.add(FileUtil.FILE_SEPARATOR + f);
			    }
			}
		    }
		    ApplicationContext appContext = null;
		    try {
			appContext = new ClassPathXmlApplicationContext(
				(String[]) fileSet.toArray(new String[fileSet
					.size()]));
		    } catch (BeansException ex) {
			if ((ex.getCause() instanceof FileNotFoundException)) {
			    appContext = new FileSystemXmlApplicationContext(
				    (String[]) fileSet
					    .toArray(new String[fileSet.size()]));
			} else {
			    throw ex;
			}
		    }
		    applicationContextByEnv = AdvancedApplicationContext
			    .getInstance(appContext);
		}
		return applicationContextByEnv;
	    } finally {
		locker.writeLock().unlock();
	    }
	}
	locker.readLock().unlock();
	return applicationContextByEnv;
    }

    private static Set<String> getSpringWebConfigurationXmlSet(String env) {
	return getSpringConfigurationXmlSet(env, getRealPath("/"));
    }

    protected static Set<String> getSpringConfigurationXmlSet(String env,
	    String runtimeRootPath) {
	if (StringUtil.isNullOrEmpty(runtimeRootPath)) {
	    runtimeRootPath = "/";
	}
	if (StringUtil.isNullOrEmpty(env)) {
	    env = PropertyLoader.getProperty("properties/deploy.properties",
		    "deploy.env", "");
	}
	Set<String> xmlSet = (Set<String>) cacheManager.getValueAnyway(env);
	if ((xmlSet == null) || (xmlSet.isEmpty())) {
	    Set<File> files = FileUtil.findMatchingFileSystemResources(
		    runtimeRootPath, "spring*." + env + ".xml");
	    xmlSet = new TreeSet<String>();
	    for (File f : files) {
		xmlSet.add(f.getAbsolutePath());
	    }
	    cacheManager.saveCacheObjectForever(env, xmlSet);
	}
	return xmlSet;
    }

    public static String getSystemInfo(String key) {
	Properties props = System.getProperties();
	return props.getProperty(key);
    }

    public static String getTempDirectory() {
	return getSystemInfo("java.io.tmpdir");
    }

    public static final WebApplicationContext getWebApplicationContext() {
	return ContextLoader.getCurrentWebApplicationContext();
    }

    public static final String getWebId() {
	WebApplicationContext context = getWebApplicationContext();
	if (context != null) {
	    return context.getId();
	}
	return null;
    }

    public static void printParameter(NetworkInterface ni)
	    throws SocketException {
	System.out.println(" Name = " + ni.getName());

	System.out.println(" Display Name = " + ni.getDisplayName());

	System.out.println(" Is up = " + ni.isUp());

	System.out.println(" Support multicast = " + ni.supportsMulticast());

	System.out.println(" Is loopback = " + ni.isLoopback());

	System.out.println(" Is virtual = " + ni.isVirtual());

	System.out.println(" Is point to point = " + ni.isPointToPoint());

	System.out.println(" Hardware address = " + ni.getHardwareAddress());

	System.out.println(" MTU = " + ni.getMTU());

	System.out.println("\nList of Interface Addresses:");

	List<InterfaceAddress> list = ni.getInterfaceAddresses();

	Iterator<InterfaceAddress> it = list.iterator();
	while (it.hasNext()) {
	    InterfaceAddress ia = (InterfaceAddress) it.next();

	    System.out.println(" Address = " + ia.getAddress());

	    System.out.println(" Broadcast = " + ia.getBroadcast());

	    System.out.println(" Network prefix length = "
		    + ia.getNetworkPrefixLength());

	    System.out.println("");
	}
    }

    public static String getWebConfig(String configKey) {
	return getWebXmlConfig("/WEB-INF/web.xml", configKey);
    }

    public static String getWebXmlConfig(String xmlConfigFilename,
	    String configKey) {
	String webXmlPath = getServletContext().getRealPath(xmlConfigFilename);

	return PropertyLoader.getXmlConfig(webXmlPath, configKey);
    }
}
