package com.zixingchen.discount.common;

/**
 * 分页类
 * @author 陈梓星
 */
public class Page {
	private int pageSize = 20;//一页显示的行数
	private int pageNumber = 1;//当前页码
	
	public int getPageSize() {
		return pageSize;
	}
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}
	public int getPageNumber() {
		return pageNumber;
	}
	public void setPageNumber(int pageNumber) {
		this.pageNumber = pageNumber;
	}	
}