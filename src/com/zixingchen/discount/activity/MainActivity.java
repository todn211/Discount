package com.zixingchen.discount.activity;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.zixingchen.discount.R;
import com.zixingchen.discount.model.Goods;

/**
 * 主页
 * @author 陈梓星
 */
public class MainActivity extends Activity{
	
	private ExpandableListView lvMyAttention;//关注的列表
	private List<Goods> goodsTypes;//关注的商品类型集合
	private List<List<Goods>> goodses;//关注的商品集合
	private Button btRefresh;//刷新
	private Button btAdd;//添加关注 
	
	@Override 
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.main_activity);
		
		//初始化关注列表 
		initLvMyAttention();
		
		btRefresh = (Button) this.findViewById(R.id.btRefresh);
		btAdd = (Button) this.findViewById(R.id.btAdd);
		getWindow().setWindowAnimations(0);
	}
	/**
	 * 刷新关注的列表
	 * @param view
	 */
	public void onBtRefreshClick(View view){
		
	}
	
	/**
	 * 跳转到添加商品关注的界面
	 * @param view
	 */
	public void onBtAddClick(View view){
		Intent intent = new Intent(this,GoodsTypeActivity.class);
		intent.putExtra("prevActivityIsMain", true);
		this.startActivity(intent);
//		this.startActivityFromChild(child, intent, requestCode);ActivityOptions
		this.overridePendingTransition(R.anim.in_from_bottom,R.anim.no_anim);
	}
	
	/**
	 * 初始化关注列表
	 */
	private void initLvMyAttention(){
		lvMyAttention = (ExpandableListView)this.findViewById(R.id.lvMyAttention);
		
		//初始化商品类型集合数据
		if(goodsTypes == null || goodsTypes.size() == 0){
			goodsTypes = new ArrayList<Goods>();
			
			for (int i = 1; i <= 10; i++) {
				Goods goodsType = new Goods();
				goodsType.setId(i);
				goodsType.setName("商品类型" + i);
				goodsTypes.add(goodsType);
			}
		}
		
		//初始化商品集合数据
		if(goodses == null || goodses.size() == 0){
			Random random = new Random();
			goodses = new ArrayList<List<Goods>>();
			
			for (int j = 1; j <= goodsTypes.size(); j++) {
				List<Goods> item = new ArrayList<Goods>();
				
				for (int k = 1; k <= 7; k++) {
					float currentPrice = random.nextInt(100);
					float prePrice = random.nextInt(1000);
					
					Goods goods = new Goods();
					goods.setId(j+k);
					goods.setName("商品商品商品商品商品商品商品商品商品商品商品商品" + k);
					goods.setSubTitle("子标题"+k);
					goods.setCurrentPrice(currentPrice);
					goods.setPrePrice(prePrice);
					item.add(goods);
				}
				
				goodses.add(item);
			}
		}
		
		lvMyAttention.setAdapter(new LvMyAttentionAdapter());
		lvMyAttention.expandGroup(0);
	}
	
	/**
	 * 商品列表适配器
	 * @author XING
	 */
	private class LvMyAttentionAdapter extends BaseExpandableListAdapter{
		@Override
		public int getGroupCount() {
			return goodsTypes.size();
		}

		@Override
		public int getChildrenCount(int groupPosition) {
			return goodses.get(groupPosition).size();
		}

		@Override
		public Object getGroup(int groupPosition) {
			return goodsTypes.get(groupPosition);
		}

		@Override
		public Object getChild(int groupPosition, int childPosition) {
			return goodses.get(groupPosition).get(childPosition);
		}

		@Override
		public long getGroupId(int groupPosition) {
			return groupPosition;
		}

		@Override
		public long getChildId(int groupPosition, int childPosition) {
			return childPosition;
		}

		@Override
		public boolean hasStableIds() {
			return false;
		}

		@Override
		public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
			if(convertView == null){
				convertView = MainActivity.this.getLayoutInflater().inflate(R.layout.lv_my_attention_group, parent,false);
			}
			TextView tvName = (TextView) convertView.findViewById(R.id.tvName);
			tvName.setText(goodsTypes.get(groupPosition).getName());
			return convertView;
		}

		@Override
		public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
			if(convertView == null){
				convertView = MainActivity.this.getLayoutInflater().inflate(R.layout.lv_my_attention_child, parent,false);
			}
			Goods goods = goodses.get(groupPosition).get(childPosition);
			
			TextView tvName = (TextView) convertView.findViewById(R.id.tvName);
			tvName.setText(goods.getName());
			
//			TextView tvSubTitle = (TextView) convertView.findViewById(R.id.tvSubTitle);
//			tvSubTitle.setText(goods.getSubTitle());
			
			TextView tvPrePrice = (TextView) convertView.findViewById(R.id.tvPrePrice);
			tvPrePrice.setText("上次价格：" + goods.getPrePrice());
			
			TextView tvCurrentPrice = (TextView) convertView.findViewById(R.id.tvCurrentPrice);
			tvCurrentPrice.setText("当前价格：" + goods.getCurrentPrice());
			return convertView;
		}

		@Override
		public boolean isChildSelectable(int groupPosition, int childPosition) {
			return true;
		}
	}
}