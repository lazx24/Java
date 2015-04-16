package com.common.util.common;

import java.util.List;

/**
 * 
 * 类的描述:分页类
 * 创建人:邹建华
 * 创建时间:2015-4-12
 */
public class Page<T> {
	
	private int pageRecorders;//list数量
	private int currentPage;//当前页
	private int totalPage;//总页数
	private int totalRow;//总行数
	private int pageNumber;//当前页数
	private List<T> result=null;
	private Param param=null;
	
	public int getPageRecorders() {
		return pageRecorders;
	}
	public void setPageRecorders(int pageRecorders) {
		this.pageRecorders = pageRecorders;
	}
	public int getCurrentPage() {
		return currentPage;
	}
	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
	}
	public int getTotalPage() {
		return totalPage;
	}
	public void setTotalPage(int totalPage) {
		this.totalPage = totalPage;
	}
	public int getTotalRow() {
		return totalRow;
	}
	public void setTotalRow(int totalRow) {
		this.totalRow = totalRow;
	}
	public int getPageNumber() {
		return pageNumber;
	}
	public void setPageNumber(int pageNumber) {
		this.pageNumber = pageNumber;
	}
	public List<T> getResult() {
		return result;
	}
	public void setResult(List<T> result) {
		this.result = result;
	}
	public Param getParam() {
		return param;
	}
	public void setParam(Param param) {
		this.param = param;
	}
}
