package com.coscon.shipsuite.common.util.common;
/**
 * 自定义json格式分页
 * @author Administrator
 *
 */
public class Param {
	
	private int currentPage;
	
	private int pageNumber;
	
	public Param(int currentPage,int pageNumber){
		this.currentPage=currentPage;
		this.pageNumber=pageNumber;
	}

	public int getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
	}

	public int getPageNumber() {
		return pageNumber;
	}

	public void setPageNumber(int pageNumber) {
		this.pageNumber = pageNumber;
	}
}
