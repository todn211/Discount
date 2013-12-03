package com.zixingchen.discount.model;

/**
 * 商品
 * @author 陈梓星
 */
public class Goods extends GoodsType{
	private int id;
	private GoodsType goodsType;//所属分类
	private float prePrice;//商品价格
	private float currentPrice;//当前价格
	private String descript;//商品说明

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

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
}
