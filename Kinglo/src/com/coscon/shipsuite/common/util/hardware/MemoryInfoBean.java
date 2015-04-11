package com.coscon.shipsuite.common.util.hardware;

class MemoryInfoBean {
    private long memoryMaxByMB;
    private long memoryFreeByMB;

    public MemoryInfoBean() {
    }

    public MemoryInfoBean(long memoryMaxByMB, long memoryFreeByMB) {
	this.memoryMaxByMB = memoryMaxByMB;
	this.memoryFreeByMB = memoryFreeByMB;
    }

    public long getMemoryMaxByMB() {
	return this.memoryMaxByMB;
    }

    public void setMemoryMaxByMB(int memoryMaxByMB) {
	this.memoryMaxByMB = memoryMaxByMB;
    }

    public long getMemoryFreeByMB() {
	return this.memoryFreeByMB;
    }

    public void setMemoryFreeByMB(int memoryFreeByMB) {
	this.memoryFreeByMB = memoryFreeByMB;
    }
}
