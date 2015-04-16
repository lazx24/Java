package com.common.util.hardware;

import java.io.PrintStream;
import java.text.DecimalFormat;
import org.junit.Test;

public class SystemUtilTester {
    @Test
    public void getRealtimeSysInfo() {
	for (int i = 0; i < 20; i++) {
	    String info = SystemUtil.getSysInfoPretty(SystemUtil
		    .getRealtimeSysInfo());
	    System.out.println(info);
	    System.out.println("------------");
	}
    }

    @Test
    public void getHostnameIp() {
	System.out.println(SystemUtil.getHostName());
	System.out.println(SystemUtil.getHostIp());
    }

    @Test
    public void getCpuRatio() {
	for (int i = 0; i < 100000; i++) {
	    DecimalFormat df = new DecimalFormat("0.00");
	    CpuInfoBean bean = SystemUtil.getCpuInfoBean();

	    System.out.println(String.format(
		    "%s %s %s",
		    new Object[] {
			    Double.valueOf(Double.parseDouble(df.format(bean
				    .getCpuRatio()))),
			    Double.valueOf(Double.parseDouble(df.format(bean
				    .getCpuRatioAvg()))),
			    Double.valueOf(Double.parseDouble(df.format(bean
				    .getCpuRatioMax()))) }));
	    try {
		Thread.sleep(10L);
	    } catch (InterruptedException e) {
		e.printStackTrace();
	    }
	}
    }
    
    @Test
    public void getAllThreads(){
	Thread[] threads=SystemUtil.getAllThreads();
	for (int i = 0; i < threads.length; i++) {
	    System.out.println(threads[i].getName());
	}
    }
    
    @Test
    public void getTotalThreadsCount(){
	System.out.println(SystemUtil.getTotalThreadsCount());
    }
    
    @Test
    public void checkSystemStatus(){
	SystemUtil.checkSystemStatus();
    }
    
    @Test
    public void getHostName(){
	System.out.println(SystemUtil.getHostName());
    }
    
    @Test
    public void getOsName(){
	System.out.println(SystemUtil.getOsName());
    }
    
    @Test
    public void getMACAddress(){
	System.out.println(SystemUtil.getMACAddress());
    }
    
    @Test
    public void getCpuRatioForWindows(){
	System.out.println(SystemUtil.getCpuRatioForWindows());
    }
    
    @Test
    public void getCpuInfoBean(){
	System.out.println(SystemUtil.getCpuInfoBean());
    }
    
    @Test
    public void getCPUSerial(){
	System.out.println(SystemUtil.getCPUSerial());
    }
}
