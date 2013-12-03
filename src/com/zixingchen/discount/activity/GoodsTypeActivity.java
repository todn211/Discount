package com.zixingchen.discount.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.zixingchen.discount.R;
import com.zixingchen.discount.model.Goods;
import com.zixingchen.discount.model.GoodsType;

/**
 * 选择关注商品类
 * @author 陈梓星
 */
public class GoodsTypeActivity extends Activity implements OnItemClickListener{

	private ListView lvGoodsType;//商品选择列表，选择要关注的商品
	private List<GoodsType> goodsTypes;//商品类型（或者商品）集合
	private boolean prevActivityIsMain;//上一个Activity是否为MainActivity，true时为是。
	private TextView tvTitle;//窗口标题
	private Button btLeft;//工具栏左边按钮
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.goods_type_activity);
		
		prevActivityIsMain = this.getIntent().getBooleanExtra("prevActivityIsMain", false);
		
		//初始工具栏
		initToolbar();
		
		//初始化商品类型集合数据
		initLvGoodsType();
	}
	
	/**
	 * 初始工具栏
	 */
	private void initToolbar(){
		//初始化标题
		tvTitle = (TextView) this.findViewById(R.id.tvTitle);
		Intent intent = this.getIntent();
		String title = intent.getStringExtra("title") == null ? "选择关注" : intent.getStringExtra("title");
		tvTitle.setText(title);
		
		//初始化按钮
		btLeft = (Button) this.findViewById(R.id.btLeft);
		if(prevActivityIsMain){
			btLeft.setVisibility(View.INVISIBLE);
		}else{
//			btLeft.setText(this.getResources().getString(r.s));
			btLeft.setVisibility(View.VISIBLE);
		}
	}
	
	/**
	 * 初始化商品类型集合数据
	 */
	private void initLvGoodsType(){
		lvGoodsType = (ListView) this.findViewById(R.id.lvGoodsType);
		
		if(goodsTypes == null || goodsTypes.size() == 0){
			goodsTypes = new ArrayList<GoodsType>();
			
			for (int i = 1; i <= 10; i++) {
				GoodsType goodsType = new Goods();
				goodsType.setId(i);
				goodsType.setName("商品类型" + i);
				goodsTypes.add(goodsType);
			}
		}
		
		lvGoodsType.setAdapter(new LvGoodsTypeSelectedAdapater());
		
		lvGoodsType.setOnItemClickListener(this);
	}
	
	/**
	 * 商品点击事件
	 */
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		GoodsType goodsType = goodsTypes.get(position);
		goodsType.setLeaf(true);
		Intent intent = null;
		//如果商品类型是最后一级，就切换到商品页面，否则依然在商品类型页面切换
		if(goodsType.isLeaf()){
			intent = new Intent(this,GoodsItemActivity.class);
		}else{
			intent = new Intent(this,GoodsTypeActivity.class);
		}
		
		intent.putExtra("title", goodsTypes.get(position).getName());
		this.startActivity(intent);
		this.overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
	}
	
	
	/**
	 * 返回物理键点击监听器
	 */
	@Override
	public void onBackPressed() {
		close(null);
	}
	
	/**
	 * 关闭按键事件监听
	 * @param view
	 */
	public void close(View view){
		if(prevActivityIsMain)
			this.setResult(RESULT_OK);
		
		this.finish();
		
		//执行页面切换效果
		doTransitionAnimation(prevActivityIsMain);
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
	 * 商品类型列表适配器
	 * @author 陈梓星
	 */
	private class LvGoodsTypeSelectedAdapater extends BaseAdapter{

		@Override
		public int getCount() {
			return goodsTypes.size();
		}

		@Override
		public Object getItem(int i) {
			return goodsTypes.get(i);
		}

		@Override
		public long getItemId(int i) {
			return i;
		}

		@Override
		public View getView(int i, View view, ViewGroup viewgroup) {
			if(view == null)
				view = GoodsTypeActivity.this
											.getLayoutInflater()
											.inflate(R.layout.lv_goods_type_item, viewgroup, false);
			
			TextView tvName = (TextView) view.findViewById(R.id.tvName);
			tvName.setText(goodsTypes.get(i).getName());
			return view;
		}
	}
}