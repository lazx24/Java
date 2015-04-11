package com.coscon.shipsuite.common.result;

import com.coscon.shipsuite.common.context.CommonConstant;
import java.io.Serializable;
import java.util.List;

public class PageResult<T> implements Serializable {
    private static final long serialVersionUID = -1166750945739231843L;
    private long totalCount;
    private int pageSize = CommonConstant.DEFAULT_PAGE_SIZE;
    private int pageCurrent;
    private String pageCurrentString;
    private List<T> result;

    public PageResult() {
    }

    public PageResult(long totalCount, List<T> result) {
	this(totalCount, result, 1);
    }

    public PageResult(long totalCount, List<T> result, int pageCurrent) {
	this(totalCount, result, pageCurrent, Math.max(
		CommonConstant.DEFAULT_PAGE_SIZE, result.size()));
    }

    public PageResult(long totalCount, List<T> result, int pageCurrent,
	    int pageSize) {
	this.totalCount = totalCount;
	this.pageSize = pageSize;
	this.pageCurrent = pageCurrent;
	this.result = result;
    }

    public int getPageCurrent() {
	return this.pageCurrent;
    }

    public int getPageMax() {
	if (this.totalCount == 0L) {
	    return 1;
	}
	if (this.pageSize < 1) {
	    this.pageSize = 1;
	}
	if (this.totalCount % this.pageSize == 0L) {
	    return (int) this.totalCount / this.pageSize;
	}
	return (int) this.totalCount / this.pageSize + 1;
    }

    public int getPageSize() {
	return this.pageSize;
    }

    public List<T> getResult() {
	return this.result;
    }

    public long getTotalCount() {
	return Math.max(this.totalCount,
		this.result == null ? 0 : this.result.size());
    }

    public void setPageCurrent(int pageCurrent) {
	this.pageCurrent = pageCurrent;
    }

    public void setPageSize(int pageSize) {
	this.pageSize = pageSize;
    }

    public void setResult(List<T> result) {
	this.result = result;
    }

    public void setTotalCount(long totalCount) {
	this.totalCount = totalCount;
    }

    public String getPageCurrentString() {
	return this.pageCurrentString;
    }

    public void setPageCurrentString(String pageCurrentString) {
	this.pageCurrentString = pageCurrentString;
    }

    public String toString() {
	return

	"PageResult [totalCount=" + this.totalCount + ", pageSize="
		+ this.pageSize + ", pageCurrent=" + this.pageCurrent
		+ ", pageCurrentString=" + this.pageCurrentString + ", result="
		+ this.result + "]";
    }
}
