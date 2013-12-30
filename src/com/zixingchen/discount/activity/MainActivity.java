package com.zixingchen.discount.activity;

import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnGroupExpandListener;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.zixingchen.discount.R;
import com.zixingchen.discount.business.GoodsBusiness;
import com.zixingchen.discount.business.GoodsTypeBusiness;
import com.zixingchen.discount.common.Page;
import com.zixingchen.discount.model.Goods;
import com.zixingchen.discount.model.GoodsType;
import com.zixingchen.discount.utils.ImageLoaderUtils;
import com.zixingchen.discount.widgetex.ExpandableListViewSuper.ExpandableListViewSuper;
import com.zixingchen.discount.widgetex.ExpandableListViewSuper.MyFocusGoodsItemLoyout;
import com.zixingchen.discount.widgetex.ExpandableListViewSuper.OnChildOperationListener;

/**
 * 主页
 * @author 陈梓星
 */
public class MainActivity extends Activity implements OnGroupExpandListener,OnChildOperationListener{
	
	private ExpandableListViewSuper lvMyFocus;//关注的列表
	private List<GoodsType> goodsTypes;//关注的商品类型集合
	private Button btRefresh;//刷新
	private Button btAdd;//添加关注 
	private GoodsTypeBusiness goodsTypeBusiness;
	private GoodsBusiness goodsBusiness;
	private View testView;
	
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
	@SuppressLint("NewApi")
	public void onBtRefreshClick(View view){
//		this.startActivity(new Intent(this,SettingsActivity.class));
		
		int[] point = new int[2];
		view.getLocationOnScreen(point);
		int x = point[0] + view.getWidth() / 2;
		int y = point[1] + view.getHeight();
		
		PopupWindow popupWindow = new PopupWindow(this);
		View contentView = this.getLayoutInflater().inflate(R.layout.more_menu_container, null);
		popupWindow.setBackgroundDrawable(new BitmapDrawable());
		popupWindow.setContentView(contentView);
//		popupWindow.setOutsideTouchable(true);
		popupWindow.setFocusable(true);
		popupWindow.setWindowLayoutMode(ViewGroup.LayoutParams.WRAP_CONTENT,
	            ViewGroup.LayoutParams.WRAP_CONTENT);
		popupWindow.showAtLocation(findViewById(android.R.id.content) , Gravity.LEFT | Gravity.TOP, x, y);
	}
	
	/**
	 * 跳转到添加商品关注的界面
	 * @param view
	 */
	public void onBtAddClick(View view){
		Intent intent = new Intent(this,GoodsTypeActivity.class);
		intent.putExtra("prevActivityIsMain", true);
		this.startActivity(intent);
		this.overridePendingTransition(R.anim.in_from_bottom,R.anim.no_anim);
	}
	

	/**
	 * 初始化关注列表
	 */
	private void initLvlvMyFocus(){
		lvMyFocus = (ExpandableListViewSuper)this.findViewById(R.id.lvMyFocus);
		
		//初始化商品类型集合数据
		if(goodsTypes == null || goodsTypes.size() == 0){
			goodsTypes = goodsTypeBusiness.findFocusGoodsTypes();
		}
		
		lvMyFocus.setAdapter(new lvMyFocusAdapter());
		lvMyFocus.setOnGroupExpandListener(this);
		lvMyFocus.setOnChildOperationListener(this);
		
		//默认展开系统一项
		if(goodsTypes != null && goodsTypes.size() > 0)
			lvMyFocus.expandGroup(0);
	}
	
	/**
	 * 切换到商品详细页面
	 */
	@Override
	public void onChildClick(ExpandableListView parent, View v,int groupPosition, int childPosition) {
		Intent intent = new Intent(this,GoodsDeailActivity.class);
		intent.putExtra("GoodsItem", goodsTypes.get(groupPosition).getGoodses().get(childPosition));
		this.startActivity(intent);
	}

	/**
	 * 删除关注
	 */
	@Override
	public void onChildDelete(ExpandableListView parent, View v,int groupPosition, int childPosition) {
		lvMyFocusAdapter adapter = (lvMyFocusAdapter)lvMyFocus.getExpandableListAdapter();
		GoodsType goodsType = goodsTypes.get(groupPosition);
		Goods goods = goodsType.getGoodses().get(childPosition);
		boolean deleteResult = goodsBusiness.deleteFocusGoodsById(goods.getId());
		goods = null;
		if(deleteResult){
			goodsType.getGoodses().remove(childPosition);
			adapter.notifyDataSetChanged();
		}else
			Toast.makeText(this, "消息关注失败！", Toast.LENGTH_SHORT).show();
		
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
			
			//把当前索引传递给视图
			MyFocusGoodsItemLoyout myFocusGoodsItemLoyout = (MyFocusGoodsItemLoyout)convertView;
			myFocusGoodsItemLoyout.setGroupPosition(groupPosition);
			myFocusGoodsItemLoyout.setChildPosition(childPosition);
			myFocusGoodsItemLoyout.resetViewPosition();
			
			Goods goods = goodsTypes.get(groupPosition).getGoodses().get(childPosition);
			
			TextView tvName = (TextView) convertView.findViewById(R.id.tvName);
			tvName.setText(goods.getName());
			
			TextView tvPrePrice = (TextView) convertView.findViewById(R.id.tvPrePrice);
			tvPrePrice.setText("上次价格：" + goods.getPrePrice());
			
			//加载当前价格
			TextView tvCurrentPrice = (TextView) convertView.findViewById(R.id.tvCurrentPrice);
			if(TextUtils.isEmpty(goods.getPriceCache())){
				goodsBusiness.loadGoodsPrice(goods, tvCurrentPrice);
			}else{
				tvCurrentPrice.setText(goods.getPriceCache());
			}
			
			//加载当前图标
			ImageView ivIcon = (ImageView) convertView.findViewById(R.id.ivIcon);
			ImageLoaderUtils.getInstance().displayImage(goods.getIcon(), ivIcon);
			
			testView = convertView;
			
			return convertView;
		}

		@Override
		public boolean isChildSelectable(int groupPosition, int childPosition) {
			return true;
		}
	}
}