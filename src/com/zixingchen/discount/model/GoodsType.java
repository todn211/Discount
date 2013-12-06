package com.zixingchen.discount.model;

import java.io.Serializable;

/**
 * 商品类型
 * @author 陈梓星
 */
public class GoodsType implements Serializable{
	private static final long serialVersionUID = 5689883900739188514L;
	
	private Long id;
	private Long parentId;//所属分类ID
	private String name;//类型名
	private String subTitle;//类型子标签
	private String icon;//商品图标
	private boolean isLeaf;//是否为最后一级
	
	public boolean isLeaf() {
		return isLeaf;
	}
	public void setLeaf(boolean isLeaf) {
		this.isLeaf = isLeaf;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getParentId() {
		return parentId;
	}
	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getSubTitle() {
		return subTitle;
	}
	public void setSubTitle(String subTitle) {
		this.subTitle = subTitle;
	}
	public String getIcon() {
		return icon;
	}
	public void setIcon(String icon) {
		this.icon = icon;
	}
}
