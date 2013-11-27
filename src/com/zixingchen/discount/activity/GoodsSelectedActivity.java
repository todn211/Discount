package com.zixingchen.discount.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.zixingchen.discount.R;

/**
 * 选择关注商品类
 * @author 陈梓星
 */
public class GoodsSelectedActivity extends Activity {

	private ListView lvGoodsSelected;//商品选择列表，选择要关注的商品
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.goods_selected_activity);
		
		lvGoodsSelected = (ListView) this.findViewById(R.id.lvGoodsSelected);
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