package com.zixingchen.discount.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.zixingchen.discount.R;
import com.zixingchen.discount.model.Goods;

/**
 * 选择关注商品类
 * @author 陈梓星
 */
public class GoodsSelectedActivity extends Activity {

	private ListView lvGoodsSelected;//商品选择列表，选择要关注的商品
	private List<Goods> goodses;//商品类型（或者商品）集合
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.goods_selected_activity);
		
		lvGoodsSelected = (ListView) this.findViewById(R.id.lvGoodsSelected);
		//初始化商品类型集合数据
		if(goodses == null || goodses.size() == 0){
			goodses = new ArrayList<Goods>();
			
			for (int i = 1; i <= 10; i++) {
				Goods goods = new Goods();
				goods.setId(i);
				goods.setName("商品类型" + i);
				goodses.add(goods);
			}
		}
		lvGoodsSelected.setAdapter(new LvGoodsSelectedAdapater());
	}
	
	private class LvGoodsSelectedAdapater extends BaseAdapter{

		@Override
		public int getCount() {
			return goodses.size();
		}

		@Override
		public Object getItem(int i) {
			return goodses.get(i);
		}

		@Override
		public long getItemId(int i) {
			return i;
		}

		@Override
		public View getView(int i, View view, ViewGroup viewgroup) {
			if(view == null)
				view = GoodsSelectedActivity.this
											.getLayoutInflater()
											.inflate(R.layout.lv_goods_selected_item, viewgroup, false);
			
			TextView tvName = (TextView) view.findViewById(R.id.tvName);
			tvName.setText(goodses.get(i).getName());
			return view;
		}
		
	}
	
	@Override
	public void onBackPressed() {
		super.onBackPressed();
		this.overridePendingTransition(0,R.anim.out_from_top);
	}
	
	public void close(View view){
		this.setResult(1);
		this.finish();
		this.overridePendingTransition(0,R.anim.out_from_top);
	}
}