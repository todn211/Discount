package com.zixingchen.discount.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.zixingchen.discount.R;
import com.zixingchen.discount.model.Goods;

/**
 * 商品项页面
 * @author 陈梓星
 */
public class GoodsItemActivity extends Activity {

	private List<Goods> goodses;//商品集合 
	private ListView lvGoodsItem;//商品列表
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.goods_item_activity);
		
		//初始化商品列表
		initLvGoodsItem();
		
		//初始化标题
		TextView tvTitle = (TextView) this.findViewById(R.id.tvTitle);
		tvTitle.setText(this.getIntent().getStringExtra("title"));
	}
	
	/**
	 * 初始化商品列表
	 */
	private void initLvGoodsItem(){
		lvGoodsItem = (ListView) this.findViewById(R.id.lvGoodsItem);
		
		//远程加载商品列表
		goodses = new ArrayList<Goods>();
		for (int i = 0; i < 20; i++) {
			Goods goods = new Goods();
			goods.setName("商品" + i);
			goods.setCurrentPrice(i);
			goodses.add(goods);
		}
		
		lvGoodsItem.setAdapter(new LvGoodsItemAdapter());
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
			tvPrice.setText(String.valueOf(goods.getCurrentPrice()));
			
//			Bitmap bm = BitmapFactory.decodeStream(is, outPadding, opts)
//			ivGoodsIcon.setImageBitmap(bm);
			
			return convertView;
		}
	}
}