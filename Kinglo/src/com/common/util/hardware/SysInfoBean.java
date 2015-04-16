package com.common.util.hardware;

import java.text.DecimalFormat;

public class SysInfoBean {
    private String osName;
    private long totalJvmHeapMemory;
    private long freeJvmHeapMemory;
    private long maxJvmHeapMemory;
    private long maxDirectMemory;
    private long freeDirectMemory;
    private long totalPhysicalMemory;
    private long freePhysicalMemory;
    private long usedPhysicalMemory;
    private double cpuRatio;
    private double avgCpuRatio;
    private double maxCpuRatio;
    private long threadsCount;

    public long getTotalJvmHeapMemory() {
	return this.totalJvmHeapMemory;
    }

    public void setTotalJvmHeapMemory(long totalJvmHeapMemory) {
	this.totalJvmHeapMemory = totalJvmHeapMemory;
    }

    public long getFreeJvmHeapMemory() {
	return this.freeJvmHeapMemory;
    }

    public void setFreeJvmHeapMemory(long freeJvmHeapMemory) {
	this.freeJvmHeapMemory = freeJvmHeapMemory;
    }

    public long getMaxJvmHeapMemory() {
	return this.maxJvmHeapMemory;
    }

    public void setMaxJvmHeapMemory(long maxJvmHeapMemory) {
	this.maxJvmHeapMemory = maxJvmHeapMemory;
    }

    public long getFreePhysicalMemory() {
	return this.freePhysicalMemory;
    }

    public void setFreePhysicalMemory(long freePhysicalMemory) {
	this.freePhysicalMemory = freePhysicalMemory;
    }

    public String getOsName() {
	return this.osName;
    }

    public void setOsName(String osName) {
	this.osName = osName;
    }

    public long getTotalPhysicalMemory() {
	return this.totalPhysicalMemory;
    }

    public void setTotalPhysicalMemory(long totalPhysicalMemory) {
	this.totalPhysicalMemory = totalPhysicalMemory;
    }

    public long getUsedPhysicalMemory() {
	return this.usedPhysicalMemory;
    }

    public void setUsedPhysicalMemory(long usedPhysicalMemory) {
	this.usedPhysicalMemory = usedPhysicalMemory;
    }

    public double getPhysicalMemoryRatio() {
	if (this.totalPhysicalMemory == 0L) {
	    return 0.0D;
	}
	Double ratio = Double.valueOf(Double.valueOf(this.usedPhysicalMemory)
		.doubleValue()
		/ Double.valueOf(this.totalPhysicalMemory).doubleValue());
	DecimalFormat df = new DecimalFormat("0.00");

	return Double.parseDouble(df.format(ratio.doubleValue() * 100.0D));
    }

    public double getJvmHeapMemoryRatio() {
	if (this.maxJvmHeapMemory == 0L) {
	    return 0.0D;
	}
	Double ratio = Double.valueOf(Double.valueOf(
		this.totalJvmHeapMemory - this.freeJvmHeapMemory).doubleValue()
		/ Double.valueOf(this.maxJvmHeapMemory).doubleValue());
	DecimalFormat df = new DecimalFormat("0.00");
	return Double.parseDouble(df.format(ratio.doubleValue() * 100.0D));
    }

    public double getJvmDirectMemoryRatio() {
	if (this.maxDirectMemory == 0L) {
	    return 0.0D;
	}
	Double ratio = Double.valueOf(Double.valueOf(
		this.maxDirectMemory - this.freeDirectMemory).doubleValue()
		/ Double.valueOf(this.maxDirectMemory).doubleValue());
	DecimalFormat df = new DecimalFormat("0.00");
	return Double.parseDouble(df.format(ratio.doubleValue() * 100.0D));
    }

    public double getCpuRatio() {
	DecimalFormat df = new DecimalFormat("0.00");
	return Double.parseDouble(df.format(this.cpuRatio));
    }

    public void setCpuRatio(CpuInfoBean cpuRatioBean) {
	this.cpuRatio = cpuRatioBean.getCpuRatio();
	this.avgCpuRatio = cpuRatioBean.getCpuRatioAvg();
	this.maxCpuRatio = cpuRatioBean.getCpuRatioMax();
    }

    public long getMaxDirectMemory() {
	return this.maxDirectMemory;
    }

    public void setMaxDirectMemory(long maxDirectMemory) {
	this.maxDirectMemory = maxDirectMemory;
    }

    public long getFreeDirectMemory() {
	return this.freeDirectMemory;
    }

    public void setFreeDirectMemory(long freeDirectMemory) {
	this.freeDirectMemory = freeDirectMemory;
    }

    public double getAvgCpuRatio() {
	DecimalFormat df = new DecimalFormat("0.00");
	return Double.parseDouble(df.format(this.avgCpuRatio));
    }

    public void setAvgCpuRatio(double cpuRatioAvg) {
	this.avgCpuRatio = cpuRatioAvg;
    }

    public double getMaxCpuRatio() {
	DecimalFormat df = new DecimalFormat("0.00");
	return Double.parseDouble(df.format(this.maxCpuRatio));
    }

    public void setMaxCpuRatio(double cpuRatioMax) {
	this.maxCpuRatio = cpuRatioMax;
    }

    public void setCpuRatio(double cpuRatio) {
	this.cpuRatio = cpuRatio;
    }

    public long getThreadsCount() {
	return this.threadsCount;
    }

    public void setThreadsCount(long threadsCount) {
	this.threadsCount = threadsCount;
    }
}
