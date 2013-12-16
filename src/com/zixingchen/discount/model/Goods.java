package com.zixingchen.discount.model;

import java.io.Serializable;

/**
 * 商品
 * @author 陈梓星
 */
public class Goods implements Serializable{
	private static final long serialVersionUID = 1839147853139127631L;
	
	private Long id;
	private GoodsType goodsType;//所属分类
	private float prePrice;//商品价格
	private float currentPrice;//当前价格
	private String name;//商品名
	private String subTitle;//商品子标题
	private String descript;//商品说明
	private String icon;//图标

	public Goods() {
	}
	
	public Goods(String name) {
		this.setName(name);
	}

	public GoodsType getGoodsType() {
		return goodsType;
	}

	public void setGoodsType(GoodsType goodsType) {
		this.goodsType = goodsType;
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

	public String getDescript() {
		return descript;
	}

	public void setDescript(String descript) {
		this.descript = descript;
	}

	public String getSubTitle() {
		return subTitle;
	}

	public void setSubTitle(String subTitle) {
		this.subTitle = subTitle;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}
}
