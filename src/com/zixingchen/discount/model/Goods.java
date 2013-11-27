package com.zixingchen.discount.model;

/**
 * 商品
 * @author 陈梓星
 */
public class Goods {
	private int id;
	private Goods parent;//所属分类
	private String name;//商品名
	private String subTitle;//商品子标签
	private float prePrice;//商品价格
	private float currentPrice;//当前价格
	private String icon;//商品图标
	private String descript;//商品说明

	public Goods() {
	}
	
	public Goods(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Goods getParent() {
		return parent;
	}

	public void setParent(Goods parent) {
		this.parent = parent;
	}

	public String getSubTitle() {
		return subTitle;
	}

	public void setSubTitle(String subTitle) {
		this.subTitle = subTitle;
	}

	public float getPrePrice() {
		return prePrice;
	}

	public void setPrePrice(float prePrice) {
		this.prePrice = prePrice;
	}

	public float getCurrentPrice() {
		return currentPrice;
	}

	public void setCurrentPrice(float currentPrice) {
		this.currentPrice = currentPrice;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public String getDescript() {
		return descript;
	}

	public void setDescript(String descript) {
		this.descript = descript;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
}
