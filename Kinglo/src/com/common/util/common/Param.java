package com.common.util.common;

/**
 * 
 * 类的描述:分页参数
 * 创建人:邹建华
 * 创建时间:2015-4-12
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
