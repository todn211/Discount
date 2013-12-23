package com.zixingchen.discount.business;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.zixingchen.discount.activity.GoodsListActivity.LvGoodsListAdapter;
import com.zixingchen.discount.common.Page;
import com.zixingchen.discount.dao.GoodsDao;
import com.zixingchen.discount.model.Goods;
import com.zixingchen.discount.model.GoodsType;
import com.zixingchen.discount.utils.ContextUtil;
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
	 * 初始化商品价格
	 */
	public static final int INIT_PRICE = 2;
	
	/**
	 * 是否加载了所有数据（已经没有下一页）
	 */
	public static final int IS_ALL_DATA = 1;
	
	private GoodsDao goodsDao;
	private static GoodsHandler handler = new GoodsHandler();
	
	
	
	public GoodsBusiness() {
		goodsDao = new GoodsDao();
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
	 * 根据商品类型获取相应关注的商品列表
	 * @param goodsType 商品类型
	 * @param page 分页对象
	 * @return 关注的商品列表
	 */
	public List<Goods> findFocusGoodsByGoodsType(final GoodsType goodsType,final Page<Goods> page){
		return goodsDao.findFocusGoodsByGoodsType(goodsType, page);
	}
	
	/**
	 * 根据商品类型获取相应的商品列表
	 * @param goodsType 商品类型
	 * @param page 分页对象
	 * @param handler 消息分配器，负责把查询出来的结果返回给调用者
	 * @throws Exception
	 */
	public void loadGoodsByGoodsType(final GoodsType goodsType,final Page<Goods> page,final LvGoodsListAdapter adapter){
		new Thread(){
			public void run() {
				final List<Goods> goodses = new ArrayList<Goods>();
				
				try {
					RequestParams params = createBaseParams();
					params.put("data-value", String.valueOf(page.getStartRecord()));//从哪行开始获取数据
					params.put("pSize", String.valueOf(page.getPageSize()));//每页显示行数
					params.put("cat", goodsType.getTypeCode());
					if(!TextUtils.isEmpty(goodsType.getKeyWord()))
						params.put("q", URLEncoder.encode(goodsType.getKeyWord(), "UTF-8"));
					
					String url = TaobaoUtil.GOODS_ITEM_LIST_URL + "?" + params.toString();
					
					AsyncHttpClient httpClient = new AsyncHttpClient();
					httpClient.get(url, new JsonHttpResponseHandler(){
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
								
								//初始化要传递的参数
								Map<String,Object> params = new HashMap<String,Object>();
								params.put("page", page);
								params.put("adapter", adapter);
								
								Message msg = Message.obtain();
								msg.what = FIND_GOODS_SUCCESS;
								msg.obj = params;
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
		}.start();
	}
	
	/**
	 * 根据商品链接获取商品价格
	 * @param href 商品链接
	 * @return 商品价格
	 */
	public void loadGoodsPrice(final String href,final TextView textView){
		new Thread(){
			public void run() {
				try {
					AsyncHttpClient httpClient = new AsyncHttpClient();
					httpClient.get(href, new AsyncHttpResponseHandler(){
						@Override
						public void onSuccess(String response) {
							Message msg = Message.obtain();
							msg.obj = textView;
							msg.what = INIT_PRICE;
							handler.sendMessage(msg);
						}
					});
				} catch (Exception e) {
					e.printStackTrace();
				}
			};
		}.start();
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
	
	/**
	 * 消息处理器
	 * @author 陈梓星
	 */
	@SuppressLint("HandlerLeak")
	private static class GoodsHandler extends Handler{
		@SuppressWarnings("unchecked")
		@Override
		public void handleMessage(Message msg) {
			switch(msg.what){
				//初始化商品当前价格
				case GoodsBusiness.INIT_PRICE :
					TextView textView = (TextView) msg.obj;
					textView.setText("当前价格：" + 100.55);
					break;
				
				//添加商品列表到前台展示
				case GoodsBusiness.FIND_GOODS_SUCCESS:
					Map<String,Object> params = (Map<String, Object>) msg.obj;
					Page<Goods> page = (Page<Goods>)params.get("page");
					LvGoodsListAdapter adapter = (LvGoodsListAdapter) params.get("adapter");
					List<Goods> newDatas = page.getDatas();
					if(newDatas != null && newDatas.size()>0){
						adapter.getDatas().addAll(newDatas);
						adapter.setPage(page.clonePageNotDatas());
						adapter.notifyDataSetChanged();
					}
					break;
				
				//加载商品列表失败
				case GoodsBusiness.FIND_GOODS_FAILURE:
					Toast.makeText(ContextUtil.getInstance(), "加载商品列表失败！", Toast.LENGTH_LONG).show();
					break;
			}
			
		}
	}
}
