package com.zixingchen.discount.business;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;

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
	/**
	 * 查找商品列表失败
	 */
	public static final int FIND_GOODS_FAILURE = 0;
	
	/**
	 * 查找商品列表成功
	 */
	public static final int FIND_GOODS_SUCCESS = 1;
	
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
		params.put("style", "list");
		params.put("json", "on");
		return params;
	}
	
	/**
	 * 根据商品类型获取相应的商品列表
	 * @param goodsType 商品类型
	 * @param page 分页对象
	 * @param handler 消息分配器
	 * @return 商品列表
	 * @throws Exception 
	 */
	public List<Goods> findGoodsByGoodsType(GoodsType goodsType,Page page,final Handler handler) throws Exception{
		final List<Goods> goodses = new ArrayList<Goods>();
		
		
//		String url = "http://list.taobao.com/itemlist/default.htm?_input_charset=UTF-8&style=list&json=on&pSize=20&cat=1512&data-value=0&q=Huawei/华为";
		
		RequestParams params = createBaseParams();
		params.put("pSize", String.valueOf(page.getPageSize()));//每页显示行数
		params.put("data-value", String.valueOf(page.getStartRecord()));//从哪行开始获取数据
		params.put("cat", goodsType.getTypeCode());
		if(!TextUtils.isEmpty(goodsType.getKeyWord()))
			params.put("q", URLEncoder.encode(goodsType.getKeyWord(), "GBK"));
		
		AsyncHttpClient ahc = new AsyncHttpClient();
		ahc.post(TaobaoUtil.GOODS_ITEM_LIST_URL, params, new JsonHttpResponseHandler(){
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
					
					Message msg = Message.obtain();
					msg.what = FIND_GOODS_SUCCESS;
					handler.sendMessage(msg);
				} catch (Exception e) {
					e.printStackTrace();
					Message msg = Message.obtain();
					msg.what = FIND_GOODS_FAILURE;
					handler.sendMessage(msg);
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
			goods.setIcon(goodsItem.getString("image") + "_sum.jpg");
			goodses.add(goods);
		}
	}
}
