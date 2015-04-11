package com.coscon.shipsuite.common.util.hardware;

class CpuInfoBean {
    private double cpuRatio;
    private double cpuRatioAvg;
    private double cpuRatioMax;

    public double getCpuRatio() {
	return this.cpuRatio;
    }

    public void setCpuRatio(double cpuRatio) {
	this.cpuRatio = cpuRatio;
    }

    public double getCpuRatioAvg() {
	return this.cpuRatioAvg;
    }

    public void setCpuRatioAvg(double cpuRatioAvg) {
	this.cpuRatioAvg = cpuRatioAvg;
    }

    public double getCpuRatioMax() {
	return this.cpuRatioMax;
    }

    public void setCpuRatioMax(double cpuRatioMax) {
	this.cpuRatioMax = cpuRatioMax;
    }

    public String toString() {
	return "CpuInfoBean [cpuRatio=" + this.cpuRatio + ", cpuRatioAvg="
		+ this.cpuRatioAvg + ", cpuRatioMax=" + this.cpuRatioMax + "]";
    }
}
