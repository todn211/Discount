package com.zixingchen.discount.activity;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.zixingchen.discount.R;
import com.zixingchen.discount.business.GoodsBusiness;
import com.zixingchen.discount.common.Page;
import com.zixingchen.discount.model.Goods;
import com.zixingchen.discount.model.GoodsType;
import com.zixingchen.discount.utils.ImageLoaderUtils;

/**
 * 商品项页面
 * @author 陈梓星
 */
@SuppressLint("HandlerLeak")
public class GoodsItemActivity extends Activity implements OnItemClickListener{

	private List<Goods> goodses;//商品集合 
	private ListView lvGoodsItem;//商品列表
	private GoodsType goodsType;//所属商品类型对象
	private GoodsItemHandler handler = new GoodsItemHandler();
	private GoodsBusiness bussiness = new GoodsBusiness();
	
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
		goodses = new ArrayList<Goods>();
		lvGoodsItem.setAdapter(new LvGoodsItemAdapter());
		
		//远程加载商品列表
		new Thread(){
			public void run() {
				try {
					bussiness.findGoodsByGoodsType(goodsType, new Page<Goods>(),handler);
				} catch (Exception e) {
					e.printStackTrace();
					Message msg = Message.obtain();
					msg.what = GoodsBusiness.FIND_GOODS_FAILURE;
					handler.sendMessage(msg);
				}
			};
		}.start();
	}
	
	/**
	 * 关注商品
	 */
	public void attentionGoods(View view){
//		bussiness.addFocusGoods(goods);
	}

	/**
	 * 切换到商品详细页面
	 */
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		Intent intent = new Intent(this,GoodsDeailActivity.class);
		intent.putExtra("GoodsItem", goodses.get(position));
		this.startActivity(intent);
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
	 * 商品项列表消息分配器、处理器
	 * @author 陈梓星
	 */
	@SuppressWarnings("unchecked")
	private class GoodsItemHandler extends Handler{
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case GoodsBusiness.FIND_GOODS_SUCCESS:
				Page<Goods> page = (Page<Goods>)msg.obj;
				List<Goods> newDatas = ((Page<Goods>)msg.obj).getDatas();
				if(newDatas != null && newDatas.size()>0){
					goodses.addAll(newDatas);
					LvGoodsItemAdapter adapter = ((LvGoodsItemAdapter)lvGoodsItem.getAdapter());
					adapter.setPage(page.clonePageNotDatas());
					adapter.notifyDataSetChanged();
				}
				break;
			case GoodsBusiness.FIND_GOODS_FAILURE:
				Toast.makeText(GoodsItemActivity.this, "加载商品列表失败！", Toast.LENGTH_LONG).show();
				break;
			}
		}
	}
	
	/**
	 * 商品项列表适配器
	 * @author 陈梓星
	 */
	private class LvGoodsItemAdapter extends BaseAdapter{
		private Page<Goods> page;

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
			
			ImageView ivIcon = (ImageView) convertView.findViewById(R.id.ivIcon);
			TextView tvName = (TextView) convertView.findViewById(R.id.tvName);
			TextView tvPrice = (TextView) convertView.findViewById(R.id.tvPrice);
			
			Goods goods = goodses.get(position);
			tvName.setText(goods.getName());
			tvPrice.setText("￥" + goods.getCurrentPrice());
			ImageLoaderUtils.getInstance().displayImage(goods.getIcon(), ivIcon);
			
			//判断当前下标是否到达倒数第三个，且判断当前页是不是最后一页，如果不是最后一页就加载下一页的数据
			if(position == goodses.size()-3 && !page.isLastPage()){
				page.setPageNumber(page.getPageNumber() + 1);
				bussiness.findGoodsByGoodsType(goodsType, page,handler);
			}
			return convertView;
		}
		
		public void setPage(Page<Goods> page){
			this.page = page;
		}
	}
}
