package com.zixingchen.discount.model;

/**
 * 商品类型
 * @author 陈梓星
 */
public class GoodsType {
	private int id;
	private Goods parent;//所属分类
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
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public Goods getParent() {
		return parent;
	}
	public void setParent(Goods parent) {
		this.parent = parent;
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
