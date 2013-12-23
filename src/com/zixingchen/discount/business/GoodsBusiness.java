package com.zixingchen.discount.business;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.zixingchen.discount.common.Page;
import com.zixingchen.discount.dao.GoodsDao;
import com.zixingchen.discount.model.Goods;
import com.zixingchen.discount.model.GoodsType;
import com.zixingchen.discount.utils.TaobaoUtil;

/**
 * 商品逻辑处理类
 * @author 陈梓星
 */
public class GoodsBusiness {
	/**
	 * 查找商品列表失败
	 */
	public static final int FIND_GOODS_FAILURE = 0;
	
	/**
	 * 查找商品列表成功
	 */
	public static final int FIND_GOODS_SUCCESS = 1;
	
	/**
	 * 是否加载了所有数据（已经没有下一页）
	 */
	public static final int IS_ALL_DATA = 1;
	
	private GoodsDao goodsDao;
	
	public GoodsBusiness() {
		goodsDao = new GoodsDao();
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
		params.put("module", "page");
		params.put("data-key", "s");
		return params;
	}
	
	/**
	 * 添加关注关注商品
	 * @param goods 要关注的商品
	 * @return 添加成功返回true，否则返回false
	 */
	public boolean addFocusGoods(Goods goods){
		return goodsDao.addFocusGoods(goods);
	}
	
	/**
	 * 根据商品类型获取相应的商品列表
	 * @param goodsType 商品类型
	 * @param page 分页对象
	 * @param handler 消息分配器，负责把查询出来的结果返回给调用者
	 * @throws Exception
	 */
	public void findGoodsByGoodsType(final GoodsType goodsType,final Page<Goods> page,final Handler handler){
		final List<Goods> goodses = new ArrayList<Goods>();
		
		try {
			RequestParams params = createBaseParams();
			params.put("data-value", String.valueOf(page.getStartRecord()));//从哪行开始获取数据
			params.put("pSize", String.valueOf(page.getPageSize()));//每页显示行数
			params.put("cat", goodsType.getTypeCode());
			if(!TextUtils.isEmpty(goodsType.getKeyWord()))
				params.put("q", URLEncoder.encode(goodsType.getKeyWord(), "UTF-8"));
			
			String url = TaobaoUtil.GOODS_ITEM_LIST_URL + "?" + params.toString();
			
			AsyncHttpClient ahc = new AsyncHttpClient();
			ahc.get(url, new JsonHttpResponseHandler(){
				@Override
				public void onSuccess(JSONObject response) {
					try {
						if(!response.isNull("itemList")){
							JSONArray goodsItems = response.getJSONArray("itemList");
							addGoodsToList(goodses, goodsType.getId(), goodsItems);
						}
						
						if(!response.isNull("mallItemList")){
							JSONArray goodsItems = response.getJSONArray("mallItemList");
							addGoodsToList(goodses, goodsType.getId(), goodsItems);
						}
						
						if(!response.isNull("page")){
							JSONObject pageJSON = response.getJSONObject("page");
							if(!pageJSON.isNull("currentPage"))
								page.setPageNumber(pageJSON.getInt("currentPage"));
							
							if(!pageJSON.isNull("totalPage"))
								page.setTotalPage(pageJSON.getInt("totalPage"));
						}
						
						page.setDatas(goodses);
						
						Message msg = Message.obtain();
						msg.what = FIND_GOODS_SUCCESS;
						msg.obj = page;
						handler.sendMessage(msg);
					} catch (Exception e) {
						e.printStackTrace();
						Message msg = Message.obtain();
						msg.what = FIND_GOODS_FAILURE;
						handler.sendMessage(msg);
					}
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
			Message msg = Message.obtain();
			msg.what = FIND_GOODS_FAILURE;
			handler.sendMessage(msg);
		}
	}
	
	/**
	 * 把JSON形式的商品对象转化成LIST
	 * @param goodses 要添加商品的集合
	 * @param goodsItems 商品集合的JSON对象
	 * @throws JSONException
	 */
	private void addGoodsToList(final List<Goods> goodses, Long goodsTypeId,JSONArray goodsItems) throws JSONException {
		for (int i=0; i<goodsItems.length();i++) {
			Goods goods = new Goods();
			JSONObject goodsItem = goodsItems.optJSONObject(i);
			goods.setId(Long.parseLong(goodsItem.getString("itemId")));
			goods.setGoodsTypeId(goodsTypeId);
			goods.setName(goodsItem.getString("tip"));
			goods.setCurrentPrice(Float.parseFloat(goodsItem.getString("currentPrice")));
			goods.setIcon(goodsItem.getString("image") + "_sum.jpg");
			goods.setHref(goodsItem.getString("href"));
			goodses.add(goods);
		}
	}
}
