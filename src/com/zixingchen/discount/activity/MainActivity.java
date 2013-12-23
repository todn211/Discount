package com.zixingchen.discount.activity;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupExpandListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.zixingchen.discount.R;
import com.zixingchen.discount.business.GoodsBusiness;
import com.zixingchen.discount.business.GoodsTypeBusiness;
import com.zixingchen.discount.common.Page;
import com.zixingchen.discount.model.Goods;
import com.zixingchen.discount.model.GoodsType;
import com.zixingchen.discount.utils.ImageLoaderUtils;

/**
 * 主页
 * @author 陈梓星
 */
public class MainActivity extends Activity implements OnGroupExpandListener,OnChildClickListener{
	
	private ExpandableListView lvMyFocus;//关注的列表
	private List<GoodsType> goodsTypes;//关注的商品类型集合
//	private List<List<Goods>> goodses;//关注的商品集合
	private Button btRefresh;//刷新
	private Button btAdd;//添加关注 
	private GoodsTypeBusiness goodsTypeBusiness;
	private GoodsBusiness goodsBusiness;
	
	@Override 
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.main_activity);
		
		goodsTypeBusiness = new GoodsTypeBusiness();
		goodsBusiness = new GoodsBusiness();
		
		//初始化关注列表 
		initLvlvMyFocus();
		
		btRefresh = (Button) this.findViewById(R.id.btRefresh);
		btAdd = (Button) this.findViewById(R.id.btAdd);
		getWindow().setWindowAnimations(0);
	}
	/**
	 * 刷新关注的列表
	 * @param view
	 */
	public void onBtRefreshClick(View view){
//		new DBHelp(this, 1).insertGoodsType();
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
	private void initLvlvMyFocus(){
		lvMyFocus = (ExpandableListView)this.findViewById(R.id.lvMyFocus);
		
		//初始化商品类型集合数据
		if(goodsTypes == null || goodsTypes.size() == 0){
			goodsTypes = goodsTypeBusiness.findFocusGoodsTypes();
		}
		
		lvMyFocus.setAdapter(new lvMyFocusAdapter());
		lvMyFocus.setOnGroupExpandListener(this);
		lvMyFocus.setOnChildClickListener(this);
		lvMyFocus.expandGroup(0);//默认展开系统一项
	}
	
	/**
	 * 切换到商品详细页面
	 */
	@Override
	public boolean onChildClick(ExpandableListView parent, View v,int groupPosition, int childPosition, long id) {
		Intent intent = new Intent(this,GoodsDeailActivity.class);
		intent.putExtra("GoodsItem", goodsTypes.get(groupPosition).getGoodses().get(childPosition));
		this.startActivity(intent);
		return true;
	}
	
	/**
	 * 商品类型展开监听器
	 */
	@Override
	public void onGroupExpand(int groupPosition) {
		lvMyFocusAdapter adapter = (lvMyFocusAdapter)lvMyFocus.getExpandableListAdapter();
		
		if(!adapter.isExpand[groupPosition]){
			GoodsType goodsType = goodsTypes.get(groupPosition);
			List<Goods> goods = goodsBusiness.findFocusGoodsByGoodsType(goodsType, new Page<Goods>());
			goodsTypes.get(groupPosition).getGoodses().addAll(goods);
			
			adapter.notifyDataSetChanged();
			adapter.isExpand[groupPosition] = true;
		}
	}
	
	/**
	 * 商品列表适配器
	 * @author XING
	 */
	private class lvMyFocusAdapter extends BaseExpandableListAdapter{
		private boolean[] isExpand;
		
		public lvMyFocusAdapter() {
			isExpand = new boolean[getGroupCount()];
		}
		
		@Override
		public int getGroupCount() {
			return goodsTypes.size();
		}

		@Override
		public int getChildrenCount(int groupPosition) {
			return goodsTypes.get(groupPosition).getGoodses().size();
		}

		@Override
		public Object getGroup(int groupPosition) {
			return goodsTypes.get(groupPosition);
		}

		@Override
		public Object getChild(int groupPosition, int childPosition) {
			return goodsTypes.get(groupPosition).getGoodses().get(childPosition);
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
				convertView = MainActivity.this.getLayoutInflater().inflate(R.layout.lv_my_focus_group, parent,false);
			}
			TextView tvName = (TextView) convertView.findViewById(R.id.tvName);
			tvName.setText(goodsTypes.get(groupPosition).getName());
			return convertView;
		}

		@Override
		public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
			if(convertView == null){
				convertView = MainActivity.this.getLayoutInflater().inflate(R.layout.lv_my_focus_child, parent,false);
			}
			Goods goods = goodsTypes.get(groupPosition).getGoodses().get(childPosition);
			
			TextView tvName = (TextView) convertView.findViewById(R.id.tvName);
			tvName.setText(goods.getName());
			
			TextView tvPrePrice = (TextView) convertView.findViewById(R.id.tvPrePrice);
			tvPrePrice.setText("上次价格：" + goods.getPrePrice());
			
			TextView tvCurrentPrice = (TextView) convertView.findViewById(R.id.tvCurrentPrice);
			tvCurrentPrice.setText("当前价格：" + goods.getCurrentPrice());
			
			ImageView ivIcon = (ImageView) convertView.findViewById(R.id.ivIcon);
			ImageLoaderUtils.getInstance().displayImage(goods.getIcon(), ivIcon);
			
			return convertView;
		}

		@Override
		public boolean isChildSelectable(int groupPosition, int childPosition) {
			return true;
		}
	}
}