package com.zixingchen.discount.business;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.zixingchen.discount.common.Page;
import com.zixingchen.discount.model.Goods;
import com.zixingchen.discount.model.GoodsType;
import com.zixingchen.discount.utils.TaobaoUtil;

/**
 * 商品列表逻辑处理类
 * @author 陈梓星
 */
public class GoodsItemBusiness {
	private Context context;
	
	public GoodsItemBusiness(Context context) {
		this.context = context;
	}
	
	/**
	 * 创建参数对象
	 * @return 参数对象
	 */
	private RequestParams createBaseParams(){
		RequestParams params = new RequestParams();
		params.put("_input_charset", "UTF-8");
		params.put("json", "on");
		return params;
	}
	
	/**
	 * 根据商品类型获取相应的商品列表
	 * @param goodsType 商品类型
	 * @return 商品列表
	 */
	public List<Goods> findGoodsByGoodsType(GoodsType goodsType,Page page){
		final List<Goods> goodses = new ArrayList<Goods>();
		
//		http://list.taobao.com/itemlist/default.htm?
//		_input_charset=utf-8&json=on&cat=50011131&pSize=6&data-value=12
		
		RequestParams params = createBaseParams();
		params.put("cats", goodsType.getTypeCode());
		params.put("q", goodsType.getKeyWord());
		params.put("pSize", String.valueOf(page.getPageSize()));//每页显示行数
		params.put("data-value", String.valueOf(page.getPageSize() * page.getPageNumber()));//从哪行开始获取数据

		String url = "http://list.taobao.com/itemlist/default.htm?_input_charset=utf-8&json=on&cat=50011131&pSize=20&data-value=1";
		
		AsyncHttpClient ahc = new AsyncHttpClient();
//		ahc.post(TaobaoUtil.GOODS_ITEM_LIST_URL, params, new JsonHttpResponseHandler(){
		ahc.post(url, new JsonHttpResponseHandler(){
			@Override
			public void onSuccess(JSONObject response) {
				try {
					if(response.has("itemList")){
						JSONArray goodsItems = response.getJSONArray("itemList");
						
						addGoodsToList(goodses, goodsItems);
					}
					
					if(response.has("mallItemList")){
						JSONArray goodsItems = response.getJSONArray("mallItemList");
						
						addGoodsToList(goodses, goodsItems);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				
			}
			
		});
		
		return goodses;
	}
	
	/**
	 * 把JSON形式的商品对象转化成LIST
	 * @param goodses 要添加商品的集合
	 * @param goodsItems 商品集合的JSON对象
	 * @throws JSONException
	 */
	private void addGoodsToList(final List<Goods> goodses,JSONArray goodsItems) throws JSONException {
		for (int i=0; i<goodsItems.length();i++) {
			Goods goods = new Goods();
			JSONObject goodsItem = goodsItems.optJSONObject(i);
			goods.setId(Long.parseLong(goodsItem.getString("itemId")));
			goods.setName(goodsItem.getString("tip"));
			goods.setCurrentPrice(Float.parseFloat(goodsItem.getString("currentPrice")));
			goods.setIcon(goodsItem.getString("image"));
			goodses.add(goods);
		}
	}
}
