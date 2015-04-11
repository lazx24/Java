package com.coscon.shipsuite.common.util.hardware;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.lang.reflect.Field;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Date;
import java.util.List;
import java.util.Queue;
import java.util.StringTokenizer;
import java.util.concurrent.ArrayBlockingQueue;

import com.coscon.shipsuite.common.context.CommonConstant;
import com.coscon.shipsuite.common.log.ISystemLogger;
import com.coscon.shipsuite.common.log.LoggerFactory;
import com.coscon.shipsuite.common.session.SessionManager;
import com.coscon.shipsuite.common.util.email.EMailSender;
import com.coscon.shipsuite.common.util.file.FileUtil;
import com.coscon.shipsuite.common.util.file.PropertyLoader;
import com.coscon.shipsuite.common.util.file.ServiceFile;
import com.coscon.shipsuite.common.util.generic.DateUtil;
import com.coscon.shipsuite.common.util.generic.NumberUtil;
import com.coscon.shipsuite.common.util.html.HtmlUtil;
import com.coscon.shipsuite.common.util.string.StringUtil;

public class SystemUtil {
    private static final int MB = 1048576;
    private static final int CPUTIME = 30;
    private static final int PERCENT = 100;
    private static final int FAULTLENGTH = 10;
    private static String linuxVersion = null;
    private static final int RECENT_TIMES = 10;
    private static final Queue<Double> cpuRatioQueue = new ArrayBlockingQueue<Double>(
	    10);
    private static volatile Date lastWarningDatetime;
    private static ISystemLogger logger = LoggerFactory
	    .getSystemLogger(SystemUtil.class);

    private static MemoryInfoBean getPhysicalMemoryInfoForWindows() {
	String jsResultFile = "c:/log/mem.txt";
	String jsFilename = "c:/log/mem.js";
	String jsContent = generateWinMemoryJavaScript(jsResultFile);
	FileUtil.writeFile(jsContent, jsFilename);
	try {
	    Process p = Runtime.getRuntime().exec(
		    "cmd /c call cscript.exe \"" + jsFilename + "\"");
	    try {
		p.waitFor();
	    } catch (Exception localException1) {
	    }
	} catch (Exception e) {
	    e.printStackTrace();
	}
	MemoryInfoBean bean = new MemoryInfoBean();
	bean.setMemoryMaxByMB(Integer.valueOf(
		PropertyLoader.getProperty(jsResultFile, "MemoryMax", "0"))
		.intValue());
	bean.setMemoryFreeByMB(Integer.valueOf(
		PropertyLoader.getProperty(jsResultFile, "MemoryFree", "0"))
		.intValue());
	try {
	    FileUtil.deleteFile(jsResultFile);
	} catch (Exception localException2) {
	}
	return bean;
    }

    private static String generateWinMemoryJavaScript(String memlogFilename) {
	if (StringUtil.isNullOrEmpty(memlogFilename)) {
	    memlogFilename = "c:/log/mem.txt";
	} else {
	    memlogFilename.replaceAll("\\\\", "/").replaceAll("//", "/");
	}
	StringBuffer memJavaScriptSb = new StringBuffer();

	memJavaScriptSb.append("function getPhysicMemCap(){ ").append("\r\n");
	memJavaScriptSb
		.append("var locator = new ActiveXObject ('WbemScripting.SWbemLocator');  ")
		.append("\r\n");
	memJavaScriptSb.append("var service = locator.ConnectServer('.');  ")
		.append("\r\n");

	memJavaScriptSb
		.append(" var memory = new Enumerator (service.ExecQuery('SELECT * FROM Win32_PhysicalMemory')); ")
		.append("\r\n");

	memJavaScriptSb
		.append("var system=new Enumerator (service.ExecQuery('SELECT * FROM Win32_ComputerSystem')).item(); ")
		.append("\r\n");
	memJavaScriptSb
		.append("var physicMemCap=Math.ceil(system.TotalPhysicalMemory/1024/1024)")
		.append("\r\n");

	memJavaScriptSb.append(" return physicMemCap; ").append("\r\n");
	memJavaScriptSb.append("} ").append("\r\n");

	memJavaScriptSb.append(
		"var oFSO = new ActiveXObject('Scripting.FileSystemObject');")
		.append("\r\n");
	memJavaScriptSb.append("var oFile = oFSO.OpenTextFile('")
		.append(memlogFilename).append("',2,true);").append("\r\n");
	memJavaScriptSb.append("var oWMI = GetObject('Winmgmts:');").append(
		"\r\n");
	memJavaScriptSb
		.append("var oRefresher = new ActiveXObject('WbemScripting.SWbemRefresher');")
		.append("\r\n");
	memJavaScriptSb
		.append("var oMemory = oRefresher.AddEnum(oWMI,'Win32_OperatingSystem').ObjectSet; ")
		.append("\r\n");

	memJavaScriptSb.append("oRefresher.Refresh();").append("\r\n");

	memJavaScriptSb.append("var s = '';").append("\r\n");
	memJavaScriptSb.append("var oTime = new Date();").append("\r\n");
	memJavaScriptSb.append("var colMemory = new Enumerator(oMemory);  ")
		.append("\r\n");

	memJavaScriptSb.append(" s += '#Datetime:'+'\\r\\n'").append("\r\n");
	memJavaScriptSb
		.append("s += 'Datetime='+oTime.toLocaleString() + ':' + oTime.getMilliseconds() +'\\r\\n';")
		.append("\r\n");

	memJavaScriptSb.append(" s += '#Total Physical Memory:'+'\\r\\n'")
		.append("\r\n");
	memJavaScriptSb
		.append("s += 'MemoryMax='+getPhysicMemCap() +'\\r\\n';")
		.append("\r\n");
	memJavaScriptSb.append("s += '#Free Physical Memory:'+'\\r\\n'")
		.append("\r\n");
	memJavaScriptSb
		.append("s +='MemoryFree='+ Math.ceil(colMemory.item().FreePhysicalMemory/1024)+'\\r\\n';")
		.append("\r\n");

	memJavaScriptSb.append("oFile.Write(s); ").append("\r\n");
	memJavaScriptSb.append(" ");

	return memJavaScriptSb.toString();
    }

    public static String getHDSerial(String drive) {
	String result = "";
	try {
	    File file = File.createTempFile("tmp", ".vbs");
	    file.deleteOnExit();
	    FileWriter fw = new FileWriter(file);

	    String vbs = "Set objFSO = CreateObject(\"Scripting.FileSystemObject\")\nSet colDrives = objFSO.Drives\nSet objDrive = colDrives.item(\""
		    +

		    drive + "\")\n" + "Wscript.Echo objDrive.SerialNumber";
	    fw.write(vbs);
	    fw.close();
	    Process p = Runtime.getRuntime().exec(
		    "cscript //NoLogo " + file.getPath());
	    BufferedReader input = new BufferedReader(new InputStreamReader(
		    p.getInputStream()));
	    String line;
	    while ((line = input.readLine()) != null) {
		result = result + line;
	    }
	    input.close();
	    file.delete();
	} catch (Exception localException) {
	}
	if ((result.trim().length() < 1) || (result == null)) {
	    result = "No disk ID read.";
	}
	return result.trim();
    }

    /**
     * 获取CPU序列号
     * 
     * @return
     */
    public static String getCPUSerial() {
	String result = "";
	try {
	    File file = File.createTempFile("tmp", ".vbs");
	    file.deleteOnExit();
	    FileWriter fw = new FileWriter(file);

	    String vbs = "On Error Resume Next \r\n\r\nstrComputer = \".\"  \r\nSet objWMIService = GetObject(\"winmgmts:\" _ \r\n    & \"{impersonationLevel=impersonate}!\\\\\" & strComputer & \"\\root\\cimv2\") \r\nSet colItems = objWMIService.ExecQuery(\"Select * from Win32_Processor\")  \r\n For Each objItem in colItems\r\n     Wscript.Echo objItem.ProcessorId  \r\n     exit for  ' do the first cpu only! \r\nNext                    ";

	    fw.write(vbs);
	    fw.close();
	    Process p = Runtime.getRuntime().exec(
		    "cscript //NoLogo " + file.getPath());
	    BufferedReader input = new BufferedReader(new InputStreamReader(
		    p.getInputStream()));
	    String line;
	    while ((line = input.readLine()) != null) {
		result = result + line;
	    }
	    input.close();
	    file.delete();
	} catch (Exception e) {
	    e.fillInStackTrace();
	}
	if ((result.trim().length() < 1) || (result == null)) {
	    result = "No CPU_ID read.";
	}
	return result.trim();
    }

    /**
     * 获取所有系统参数
     * 
     * @return
     */
    public static SysInfoBean getRealtimeSysInfo() {
	SysInfoBean infoBean = new SysInfoBean();

	String osName = getOsName();
	infoBean.setOsName(osName);

	long totalJvmHeapMemory = Runtime.getRuntime().totalMemory() / 1048576L;

	long freeJvmHeapMemory = Runtime.getRuntime().freeMemory() / 1048576L;

	long maxJvmHeapMemory = Runtime.getRuntime().maxMemory() / 1048576L;

	infoBean.setTotalJvmHeapMemory(totalJvmHeapMemory);
	infoBean.setFreeJvmHeapMemory(freeJvmHeapMemory);
	infoBean.setMaxJvmHeapMemory(maxJvmHeapMemory);

	MemoryInfoBean directMemBean = getDirectMemoryInfo();
	infoBean.setMaxDirectMemory(directMemBean.getMemoryMaxByMB());
	infoBean.setFreeDirectMemory(directMemBean.getMemoryFreeByMB());
	MemoryInfoBean physicalMemBean;
	if (osName.toLowerCase().startsWith("windows")) {
	    physicalMemBean = getPhysicalMemoryInfoForWindows();
	} else {
	    physicalMemBean = getPhysicalMemoryInfoForLunux();
	}
	infoBean.setFreePhysicalMemory(physicalMemBean.getMemoryFreeByMB());
	infoBean.setTotalPhysicalMemory(physicalMemBean.getMemoryMaxByMB());
	infoBean.setUsedPhysicalMemory(physicalMemBean.getMemoryMaxByMB()
		- physicalMemBean.getMemoryFreeByMB());

	infoBean.setCpuRatio(getCpuInfoBean());

	infoBean.setThreadsCount(getTotalThreadsCount());
	return infoBean;
    }

    private static MemoryInfoBean getPhysicalMemoryInfoForLunux() {
	Double[] result = new Double[4];
	File file = new File("/proc/meminfo");
	BufferedReader br = null;
	try {
	    br = new BufferedReader(new InputStreamReader(new FileInputStream(
		    file)));

	    String str = null;
	    StringTokenizer token = null;
	    while ((str = br.readLine()) != null) {
		token = new StringTokenizer(str);
		if (token.hasMoreTokens()) {
		    str = token.nextToken();
		    if (token.hasMoreTokens()) {
			if (str.equalsIgnoreCase("MemTotal:")) {
			    result[0] = Double.valueOf(Double.parseDouble(token
				    .nextToken()) / 1024.0D);
			} else if (str.equalsIgnoreCase("MemFree:")) {
			    result[1] = Double.valueOf(Double.parseDouble(token
				    .nextToken()) / 1024.0D);
			} else if (str.equalsIgnoreCase("SwapTotal:")) {
			    result[2] = Double.valueOf(Double.parseDouble(token
				    .nextToken()) / 1024.0D);
			} else if (str.equalsIgnoreCase("SwapFree:")) {
			    result[3] = Double.valueOf(Double.parseDouble(token
				    .nextToken()) / 1024.0D);
			}
		    }
		}
	    }
	} catch (Exception e) {
	    e.printStackTrace();
	    if (br != null) {
		try {
		    br.close();
		} catch (IOException localIOException) {
		}
	    }
	} finally {
	    if (br != null) {
		try {
		    br.close();
		} catch (IOException localIOException1) {
		}
	    }
	}
	MemoryInfoBean bean = new MemoryInfoBean(result[0].longValue(),
		result[1].longValue());

	return bean;
    }

    /**
     * 获取Linux系统下的CPU使用率
     * 
     * @return
     */
    public static double getCpuRatioForLinux() {
	InputStream is = null;
	InputStreamReader isr = null;
	BufferedReader brStat = null;
	StringTokenizer tokenStat = null;
	try {
	    System.out.println("Get usage rate of CUP , linux version: "
		    + linuxVersion);
	    Process process = Runtime.getRuntime().exec("top -b -n 1");
	    is = process.getInputStream();
	    isr = new InputStreamReader(is);
	    brStat = new BufferedReader(isr);
	    if (linuxVersion.equals("2.4")) {
		brStat.readLine();
		brStat.readLine();
		brStat.readLine();
		brStat.readLine();
		tokenStat = new StringTokenizer(brStat.readLine());
		tokenStat.nextToken();
		tokenStat.nextToken();
		String user = tokenStat.nextToken();
		tokenStat.nextToken();
		String system = tokenStat.nextToken();
		tokenStat.nextToken();
		String nice = tokenStat.nextToken();
		System.out.println(user + " , " + system + " , " + nice);
		user = user.substring(0, user.indexOf("%"));
		system = system.substring(0, system.indexOf("%"));
		nice = nice.substring(0, nice.indexOf("%"));
		float userUsage = new Float(user).floatValue();
		float systemUsage = new Float(system).floatValue();
		float niceUsage = new Float(nice).floatValue();
		return (userUsage + systemUsage + niceUsage) / 100.0F;
	    }
	    brStat.readLine();
	    brStat.readLine();
	    tokenStat = new StringTokenizer(brStat.readLine());
	    tokenStat.nextToken();
	    tokenStat.nextToken();
	    tokenStat.nextToken();
	    tokenStat.nextToken();
	    tokenStat.nextToken();
	    tokenStat.nextToken();
	    tokenStat.nextToken();
	    String cpuUsage = tokenStat.nextToken();
	    System.out.println("CPU idle : " + cpuUsage);
	    Float usage = new Float(
		    cpuUsage.substring(0, cpuUsage.indexOf("%")));
	    return 1.0F - usage.floatValue();
	} catch (IOException ioe) {
	    System.out.println(ioe.getMessage());
	    freeResource(is, isr, brStat);
	    return 1.0D;
	} finally {
	    freeResource(is, isr, brStat);
	}
    }

    private static void freeResource(InputStream is, InputStreamReader isr,
	    BufferedReader br) {
	try {
	    if (is != null) {
		is.close();
	    }
	    if (isr != null) {
		isr.close();
	    }
	    if (br != null) {
		br.close();
	    }
	} catch (IOException ioe) {
	    System.out.println(ioe.getMessage());
	}
    }

    /**
     * 获取CPU使用率相关信息
     * 
     * @return
     */
    public static double getCpuRatio() {
	double cpuRatio = 0.0D;
	String osName = getOsName();
	if (osName.toLowerCase().startsWith("windows")) {
	    cpuRatio = getCpuRatioForWindows();
	} else {
	    cpuRatio = getCpuRatioForLinux();
	}
	if (cpuRatio > 100.0D) {
	    return 100.0D;
	}
	if (cpuRatio < 0.0D) {
	    return 0.0D;
	}
	return cpuRatio;
    }

    /**
     * 获取CPU使用率相关信息
     * 
     * @return
     */
    public static CpuInfoBean getCpuInfoBean() {
	CpuInfoBean cpuInfoBean = new CpuInfoBean();
	double cpuRatio = getCpuRatio();
	cpuInfoBean.setCpuRatio(cpuRatio);
	if (cpuRatioQueue.size() >= 10) {
	    cpuRatioQueue.poll();
	}
	cpuRatioQueue.offer(Double.valueOf(cpuRatio));

	Double[] cpuRatioArray = (Double[]) cpuRatioQueue
		.toArray(new Double[cpuRatioQueue.size()]);

	cpuInfoBean.setCpuRatioAvg(NumberUtil.avg(cpuRatioArray).doubleValue());
	cpuInfoBean.setCpuRatioMax(NumberUtil.max(cpuRatioArray).doubleValue());
	return cpuInfoBean;
    }

    /**
     * 获取Window系统下的CPU使用率
     * 
     * @return
     */
    public static double getCpuRatioForWindows() {
	try {
	    String procCmd = System.getenv("windir")
		    + "//system32//wbem//wmic.exe process get Caption,KernelModeTime,ReadOperationCount,ThreadCount,UserModeTime,WriteOperationCount";

	    long[] c0 = readCpu(Runtime.getRuntime().exec(procCmd));
	    Thread.sleep(30L);
	    long[] c1 = readCpu(Runtime.getRuntime().exec(procCmd));
	    if ((c0 != null) && (c1 != null)) {
		long idletime = c1[0] - c0[0];
		long busytime = c1[1] - c0[1];
		return Double.valueOf(100L * busytime / (busytime + idletime))
			.doubleValue();
	    }
	    return 0.0D;
	} catch (Exception ex) {
	    ex.printStackTrace();
	}
	return 0.0D;
    }

    private static long[] readCpu(Process proc) {
	long[] retn = new long[2];
	try {
	    proc.getOutputStream().close();
	    InputStreamReader ir = new InputStreamReader(proc.getInputStream());
	    LineNumberReader input = new LineNumberReader(ir);
	    String line = input.readLine();
	    if ((line == null) || (line.length() < 10)) {
		return null;
	    }
	    int capidx = line.indexOf("Caption");
	    int kmtidx = line.indexOf("KernelModeTime");
	    int rocidx = line.indexOf("ReadOperationCount");
	    int umtidx = line.indexOf("UserModeTime");

	    int wocidx = line.indexOf("WriteOperationCount");
	    long idletime = 0L;
	    long kneltime = 0L;
	    long usertime = 0L;
	    while ((line = input.readLine()) != null) {
		if (line.length() >= wocidx) {
		    String caption = line.substring(capidx, kmtidx - 1).trim();
		    String cmd = line.substring(kmtidx, rocidx - 1).trim();
		    if (cmd.indexOf("wmic.exe") < 0) {
			String s1 = line.substring(kmtidx, rocidx - 1).trim();
			String s2 = line.substring(umtidx, wocidx - 1).trim();
			if ((caption.equals("System Idle Process"))
				|| (caption.equals("System"))) {
			    if (s1.length() > 0) {
				idletime += Long.valueOf(s1).longValue();
			    }
			    if (s2.length() > 0) {
				idletime += Long.valueOf(s2).longValue();
			    }
			} else {
			    if (s1.length() > 0) {
				kneltime += Long.valueOf(s1).longValue();
			    }
			    if (s2.length() > 0) {
				usertime += Long.valueOf(s2).longValue();
			    }
			}
		    }
		}
	    }
	    retn[0] = idletime;
	    retn[1] = (kneltime + usertime);
	    return retn;
	} catch (Exception ex) {
	    ex.printStackTrace();
	} finally {
	    try {
		proc.getInputStream().close();
	    } catch (Exception e) {
		e.printStackTrace();
	    }
	}
	return null;
    }

    /**
     * 装饰SysInfoBean 使展示更加好看
     * 
     * @param bean
     * @return
     */
    public static String getSysInfoPretty(SysInfoBean bean) {
	StringBuffer sb = new StringBuffer("\r\n");
	sb.append("CPU Ratio=").append(bean.getCpuRatio()).append("%\t");
	sb.append("CPU Avg Ratio=").append(bean.getAvgCpuRatio()).append("%\t");
	sb.append("CPU Max Ratio=").append(bean.getMaxCpuRatio())
		.append("%\r\n");

	sb.append("Physical Memory Ratio=")
		.append(bean.getPhysicalMemoryRatio()).append("%\t");
	sb.append("Total Physical Memory=")
		.append(bean.getTotalPhysicalMemory()).append(" MB\t");
	sb.append("Free Physical Memory=").append(bean.getFreePhysicalMemory())
		.append(" MB\t");
	sb.append("Reserved Physical Memory=")
		.append(bean.getUsedPhysicalMemory()).append(" MB\r\n");

	sb.append("JVM HeapMemory Ratio=").append(bean.getJvmHeapMemoryRatio())
		.append("%\t");
	sb.append("JVM Max HeapMemory=").append(bean.getMaxJvmHeapMemory())
		.append(" MB\t");
	sb.append("JVM Free HeapMemory=").append(bean.getFreeJvmHeapMemory())
		.append(" MB\t");
	sb.append("JVM Total HeapMemory=").append(bean.getTotalJvmHeapMemory())
		.append(" MB\t");
	sb.append("JVM Reserved HeapMemory=")
		.append(bean.getTotalJvmHeapMemory()
			- bean.getFreeJvmHeapMemory()).append(" MB\r\n");

	sb.append("JVM DirectMemory Ratio=")
		.append(bean.getJvmDirectMemoryRatio()).append("%\t");
	sb.append("JVM Max DirectMemory=").append(
		bean.getMaxDirectMemory() + " MB\t");
	sb.append("JVM Free DirectMemory=" + bean.getFreeDirectMemory())
		.append(" MB\t");
	sb.append("JVM Reserved DirectMemory=")
		.append(bean.getMaxDirectMemory() - bean.getFreeDirectMemory())
		.append(" MB\r\n");

	sb.append("JVM Total Threads Count=").append(bean.getThreadsCount())
		.append("\r\n");
	return sb.toString();
    }

    /**
     * 获取系统的Mac地址
     * 
     * @return
     */
    public static String getMACAddress() {
	String osName = getOsName();
	if (osName.toLowerCase().startsWith("windows")) {
	    return getMACAddressForWindows();
	}
	return getMACAddressForLinux();
    }

    private static String getMACAddressForLinux() {
	String mac = null;
	BufferedReader bufferedReader = null;
	Process process = null;
	try {
	    process = Runtime.getRuntime().exec("ifconfig eth0");

	    bufferedReader = new BufferedReader(new InputStreamReader(
		    process.getInputStream()));
	    String line = null;
	    int index = -1;
	    while ((line = bufferedReader.readLine()) != null) {
		index = line.toLowerCase().indexOf("hwaddr");
		if (index >= 0) {
		    mac = line.substring(index + "hwaddr".length() + 1).trim();
		    break;
		}
	    }
	} catch (IOException e) {
	    e.printStackTrace();
	} finally {
	    try {
		if (bufferedReader != null) {
		    bufferedReader.close();
		}
	    } catch (IOException e1) {
		e1.printStackTrace();
	    }
	    bufferedReader = null;
	    process = null;
	}
	return mac;
    }

    private static String getMACAddressForWindows() {
	String mac = null;
	BufferedReader bufferedReader = null;
	Process process = null;
	label158: try {
	    process = Runtime.getRuntime().exec("ipconfig /all");
	    bufferedReader = new BufferedReader(new InputStreamReader(
		    process.getInputStream()));
	    String line = null;
	    int index = -1;
	    while ((line = bufferedReader.readLine()) != null) {
		index = line.toLowerCase().indexOf("physical address");
		if (index >= 0) {
		    index = line.indexOf(":");
		    if (index < 0) {
			break;
		    }
		    mac = line.substring(index + 1).trim();
		    break label158;
		}
	    }
	} catch (IOException e) {
	    e.printStackTrace();
	} finally {
	    try {
		if (bufferedReader != null) {
		    bufferedReader.close();
		}
	    } catch (IOException e1) {
		e1.printStackTrace();
	    }
	    bufferedReader = null;
	    process = null;
	}
	return mac;
    }

    /**
     * 获取主机IP地址
     * 
     * @return
     */
    public static String getHostIp() {
	try {
	    InetAddress a = InetAddress.getLocalHost();
	    return a.getHostAddress().toUpperCase();
	} catch (UnknownHostException e) {
	    e.printStackTrace();
	}
	return "N/A";
    }

    /**
     * 检测系统名称
     * 
     * @return
     */
    public static String getOsName() {
	return System.getProperty("os.name");
    }

    /**
     * 获取主机名
     * 
     * @return
     */
    public static String getHostName() {
	try {
	    InetAddress a = InetAddress.getLocalHost();
	    return a.getHostName().toUpperCase();
	} catch (UnknownHostException e) {
	    e.printStackTrace();
	}
	return "N/A";
    }

    public static void checkSystemStatus() {
	List<String> userList = SessionManager.getInstance()
		.getOnlinseUserIdList();
	StringBuffer sysInfoSb = new StringBuffer();
	sysInfoSb.append("Onlinse users=").append(userList.size())
		.append("\t[");
	sysInfoSb.append(StringUtil.toString(userList)).append("]\r\n");

	String osName = getOsName();
	String hostName = getHostName();
	String hostIp = getHostIp();
	sysInfoSb.append("Host Server: ").append(hostName).append(" [")
		.append(hostIp).append("]\r\n");

	SysInfoBean sysBean = getRealtimeSysInfo();
	sysInfoSb.append(getSysInfoPretty(sysBean));

	logger.info(null, sysInfoSb.toString());
	
	if ((osName.toLowerCase().startsWith("windows"))
		&& (!osName.toLowerCase().contains("server"))) {
	    return;
	}
	int warningIntervalMinutes = Integer.parseInt(PropertyLoader
		.getDefaultProperty("system.monitor.warning.intervalMinutes",
			"10"));
	if ((lastWarningDatetime != null)
		&& (lastWarningDatetime.after(DateUtil.addMinute(new Date(),
			-warningIntervalMinutes)))) {
	    return;
	}
	int cpuMaxRatio = Integer.parseInt(PropertyLoader.getDefaultProperty(
		"system.monitor.cpu.maxRatio", "80"));
	int physicalMemoryMaxRatio = Integer.parseInt(PropertyLoader
		.getDefaultProperty("system.monitor.physicalMemory.maxRatio",
			"80"));
	int jvmHeapMemoryRatio = Integer.parseInt(PropertyLoader
		.getDefaultProperty("system.monitor.jvmHeapMemory.maxRatio",
			"80"));

	int jvmDirectMemoryRatio = Integer.parseInt(PropertyLoader
		.getDefaultProperty("system.monitor.jvmDirectMemory.maxRatio",
			"80"));
	int jvmGcRatio = Integer.parseInt(PropertyLoader.getDefaultProperty(
		"system.monitor.jvmGc.maxRatio", "60"));
	if ((jvmGcRatio > 0)
		&& ((sysBean.getPhysicalMemoryRatio() > jvmGcRatio)
			|| (sysBean.getJvmHeapMemoryRatio() > jvmGcRatio) || (sysBean
			.getJvmDirectMemoryRatio() > jvmGcRatio))) {
	    logger.info(null, "System.gc();");
	    System.gc();
	}
	if ((sysBean.getAvgCpuRatio() > cpuMaxRatio)
		|| (sysBean.getPhysicalMemoryRatio() > physicalMemoryMaxRatio)
		|| (sysBean.getJvmHeapMemoryRatio() > jvmHeapMemoryRatio)
		|| (sysBean.getJvmDirectMemoryRatio() > jvmDirectMemoryRatio) 
		|| true) {
	    String recipientTo = PropertyLoader.getDefaultProperty(
		    "system.monitor.recipientTo", "temp_zoujh@coscon.com");
	    String recipientCc = PropertyLoader.getDefaultProperty(
		    "system.monitor.recipientCc", "");
	    if (!StringUtil.isNullOrEmpty(recipientTo)) {
		try {
		    StringBuffer contentSb = new StringBuffer();
		    contentSb.append("cpuMaxRatio=").append(cpuMaxRatio)
			    .append("%\t");
		    contentSb.append("physicalMemoryMaxRatio=")
			    .append(physicalMemoryMaxRatio).append("%\t");
		    contentSb.append("jvmHeapMemoryMaxRatio=")
			    .append(jvmHeapMemoryRatio).append("%\t");
		    contentSb.append("jvmDirectMemoryMaxRatio=")
			    .append(jvmDirectMemoryRatio).append("%\r\n\r\n");
		    contentSb.append(sysInfoSb);

		    String subject = String.format(
			    "SIMP System Warning [%s, %s, %s]", new Object[] {
				    "", hostName, hostIp });

		    String contentHtml = HtmlUtil.text2html(contentSb
			    .toString());

		    String SMTP_HOST = PropertyLoader.getProperty(
			    CommonConstant.EmailConfig.getPropetyFile(),
			    "SMTP_HOST").trim();

		    String SMTP_PORT = PropertyLoader.getProperty(
			    CommonConstant.EmailConfig.getPropetyFile(),
			    "SMTP_PORT").trim();
		    String MAIL_SENDER = PropertyLoader.getProperty(
			    CommonConstant.EmailConfig.getPropetyFile(),
			    "MAIL_SENDER").trim();

		    String mailSslEnabled = PropertyLoader.getProperty(
			    CommonConstant.EmailConfig.getPropetyFile(),
			    "SSL_ENABLED");
		    boolean SSL_ENABLED;
		    if ((mailSslEnabled != null)
			    && (mailSslEnabled.trim().equalsIgnoreCase("true"))) {
			SSL_ENABLED = true;
		    } else {
			SSL_ENABLED = false;
		    }
		    String[] MAIL_RECEIVER_CC = EMailSender
			    .getRecipients(PropertyLoader.getProperty(

			    CommonConstant.EmailConfig.getPropetyFile(),
				    "MAIL_RECEIVER_CC"));
		    String[] MAIL_RECEIVER_BCC = EMailSender
			    .getRecipients(PropertyLoader.getProperty(

			    CommonConstant.EmailConfig.getPropetyFile(),
				    "MAIL_RECEIVER_BCC"));

		    String mailSenderEnabledValue = PropertyLoader.getProperty(
			    CommonConstant.EmailConfig.getPropetyFile(),
			    "MAIL_SENDER_ENABLED");
		    boolean MAIL_SENDER_ENABLED;
		    if ((mailSenderEnabledValue != null)
			    && (mailSenderEnabledValue.trim()
				    .equalsIgnoreCase("true"))) {
			MAIL_SENDER_ENABLED = true;
		    } else {
			MAIL_SENDER_ENABLED = false;
		    }
		    String mailAuthValue = PropertyLoader.getProperty(
			    CommonConstant.EmailConfig.getPropetyFile(),
			    "MAIL_AUTH");
		    boolean MAIL_AUTH;
		    if ((mailAuthValue != null)
			    && (mailAuthValue.trim().equalsIgnoreCase("true"))) {
			MAIL_AUTH = true;
		    } else {
			MAIL_AUTH = false;
		    }
		    String MAIL_SENDER_PASSWORD = PropertyLoader.getProperty(
			    CommonConstant.EmailConfig.getPropetyFile(),
			    "MAIL_SENDER_PASSWORD");
		    
		    System.out.println(SMTP_HOST+";"+SMTP_PORT+";"+MAIL_SENDER+";"+MAIL_SENDER_PASSWORD+";------------zoujianhua");
		    EMailSender.getInstance(SMTP_HOST, SMTP_PORT, MAIL_SENDER, MAIL_SENDER_PASSWORD)
		    .sendMail(new String[] { recipientTo }, new String[] { recipientCc }, null, subject, contentHtml, null);
		    

		    lastWarningDatetime = new Date();
		} catch (Exception ex) {
		    logger.error(ex);
		}
	    }
	}
    }

    private static MemoryInfoBean getDirectMemoryInfo() {
	try {
	    Class<?> c = Class.forName("java.nio.Bits");

	    Field maxMemory = c.getDeclaredField("maxMemory");
	    maxMemory.setAccessible(true);
	    Field reservedMemory = c.getDeclaredField("reservedMemory");
	    reservedMemory.setAccessible(true);
	    long maxMemoryValue = ((Long) maxMemory.get(null)).longValue() / 1048576L;
	    long reservedMemoryValue = ((Long) reservedMemory.get(null))
		    .longValue() / 1048576L;
	    return new MemoryInfoBean(maxMemoryValue, maxMemoryValue
		    - reservedMemoryValue);
	} catch (Exception e) {
	}
	return new MemoryInfoBean();
    }

    /**
     * 得到所有的线程数
     * 
     * @return
     */
    public static int getTotalThreadsCount() {
	ThreadGroup group = Thread.currentThread().getThreadGroup();
	ThreadGroup topGroup = group;
	while (group != null) {
	    topGroup = group;
	    group = group.getParent();
	}
	int estimatedSize = topGroup.activeCount() * 2;
	Thread[] slackList = new Thread[estimatedSize];

	int actualSize = topGroup.enumerate(slackList);

	return actualSize;
    }

    /**
     * 得到所有线程
     * 
     * @return
     */
    public static Thread[] getAllThreads() {
	ThreadGroup group = Thread.currentThread().getThreadGroup();
	ThreadGroup topGroup = group;
	while (group != null) {
	    topGroup = group;
	    group = group.getParent();
	}
	int estimatedSize = topGroup.activeCount() * 2;
	Thread[] slackList = new Thread[estimatedSize];

	int actualSize = topGroup.enumerate(slackList);

	Thread[] list = new Thread[actualSize];
	System.arraycopy(slackList, 0, list, 0, actualSize);
	return list;
    }
}
