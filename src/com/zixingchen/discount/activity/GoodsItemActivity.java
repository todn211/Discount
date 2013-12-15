package com.zixingchen.discount.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.zixingchen.discount.R;
import com.zixingchen.discount.model.Goods;
import com.zixingchen.discount.model.GoodsType;
import com.zixingchen.discount.utils.TaobaoUtil;

/**
 * 商品项页面
 * @author 陈梓星
 */
public class GoodsItemActivity extends Activity implements OnItemClickListener{

	private List<Goods> goodses;//商品集合 
	private ListView lvGoodsItem;//商品列表
	private GoodsType goodsType;//所属商品类型对象
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.goods_item_activity);
		
		Intent intent = this.getIntent();
		
		//初始化商品类型对象
		goodsType = (GoodsType) intent.getSerializableExtra("goodsType");
		
		//初始化商品列表
		initLvGoodsItem();
		
		//初始化标题
		TextView tvTitle = (TextView) this.findViewById(R.id.tvTitle);
		tvTitle.setText(goodsType.getName());
	}
	
	/**
	 * 初始化商品列表
	 */
	private void initLvGoodsItem(){
		lvGoodsItem = (ListView) this.findViewById(R.id.lvGoodsItem);
		lvGoodsItem.setOnItemClickListener(this);
		
		//远程加载商品列表
//		goodses = new ArrayList<Goods>();
//		for (int i = 0; i < 20; i++) {
//			Goods goods = new Goods();
//			goods.setName("商品" + i);
//			goods.setCurrentPrice(i);
//			goodses.add(goods);
//		}
//		
//		lvGoodsItem.setAdapter(new LvGoodsItemAdapter());
		
		new Thread(){
			public void run() {
				try {
					Map<String,String> paramsMap = new HashMap<String, String>();
					paramsMap.put("method", "taobao.items.search");
					paramsMap.put("fields", "num_iid,title,nick,pic_url,cid,price,type,delist_time,post_fee");
					paramsMap.put("cid", goodsType.getId().toString());
					RequestParams params = new RequestParams(TaobaoUtil.generateApiParams(paramsMap, null));
					
					AsyncHttpClient ahc = new AsyncHttpClient();
					ahc.post(TaobaoUtil.URL, params, new JsonHttpResponseHandler(){
						@Override
						public void onSuccess(JSONObject response) {
							try {
								goodses = new ArrayList<Goods>();
								JSONArray itemCats = response
														.getJSONObject("itemcats_get_response")
														.getJSONObject("item_cats")
														.getJSONArray("item_cat");
								for (int i=0; i<itemCats.length();i++) {
									Goods goods = new Goods();
									JSONObject item = itemCats.optJSONObject(i);
//									goods.setId(id);
//									goods.setName(name);
//									goods.setCurrentPrice(currentPrice);
//									goods.setIcon(icon);
									goodses.add(goods);
								}
								
								GoodsItemActivity.this.runOnUiThread(new Thread(){
									public void run() {
										lvGoodsItem.setAdapter(new LvGoodsItemAdapter());
									};
								});
								
							} catch (Exception e) {
								e.printStackTrace();
							}
							
						}
					});
				} catch (Exception e) {
					e.printStackTrace();
				}
			};
		}.start();
	}
	
	/**
	 * 关注商品
	 */
	public void attentionGoods(View view){
		System.out.println("************");
	}

	/**
	 * 切换到商品详细页面
	 */
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		this.startActivity(new Intent(this,GoodsDeailActivity.class));
	}
	
	/**
	 * 返回物理键点击监听器
	 */
	@Override
	public void onBackPressed() {
		back(null);
	}
	
	/**
	 * 返回按键事件监听
	 * @param view
	 */
	public void back(View view){
		this.finish();
		//执行页面切换效果
		doTransitionAnimation(false);
	}
	
	/**
	 * 跳转到主页
	 * @param view
	 */
	public void goToHome(View view){
		Intent intent = new Intent(this,MainActivity.class);
		this.startActivity(intent);
		this.finish();
		
		//执行页面切换效果
		doTransitionAnimation(true);
	}
	
	/**
	 * 执行页面切换效果
	 */
	private void doTransitionAnimation(boolean prevActivityIsMain){
		if(prevActivityIsMain)
			this.overridePendingTransition(0,R.anim.out_from_top);
		else
			this.overridePendingTransition(R.anim.in_from_left,R.anim.out_to_right);
	}
	
	/**
	 * 商品项列表适配器
	 * @author 陈梓星
	 */
	private class LvGoodsItemAdapter extends BaseAdapter{

		@Override
		public int getCount() {
			return goodses.size();
		}

		@Override
		public Object getItem(int position) {
			return goodses.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if(convertView == null)
				convertView = GoodsItemActivity.this.getLayoutInflater().inflate(R.layout.lv_goods_item, parent,false);
			
			ImageView ivGoodsIcon = (ImageView) convertView.findViewById(R.id.ivGoodsIcon);
			TextView tvName = (TextView) convertView.findViewById(R.id.tvName);
			TextView tvPrice = (TextView) convertView.findViewById(R.id.tvPrice);
			
			Goods goods = goodses.get(position);
			tvName.setText(goods.getName());
			tvPrice.setText("￥" + goods.getCurrentPrice());
			
//			Bitmap bm = BitmapFactory.decodeStream(is, outPadding, opts)
//			ivGoodsIcon.setImageBitmap(bm);
			
			return convertView;
		}
	}
}
