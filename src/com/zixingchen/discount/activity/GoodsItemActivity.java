package com.zixingchen.discount.activity;

import java.io.File;
import java.util.List;

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

import com.nostra13.universalimageloader.cache.disc.impl.TotalSizeLimitedDiscCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.nostra13.universalimageloader.utils.StorageUtils;
import com.zixingchen.discount.R;
import com.zixingchen.discount.business.GoodsItemBusiness;
import com.zixingchen.discount.common.Page;
import com.zixingchen.discount.model.Goods;
import com.zixingchen.discount.model.GoodsType;

/**
 * 商品项页面
 * @author 陈梓星
 */
public class GoodsItemActivity extends Activity implements OnItemClickListener{

	private List<Goods> goodses;//商品集合 
	private ListView lvGoodsItem;//商品列表
	private GoodsType goodsType;//所属商品类型对象
	private GoodsItemHandler handler = new GoodsItemHandler();
	private static ImageLoader imageLoader;//图片加载器
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.goods_item_activity);
		
		Intent intent = this.getIntent();
		
		//初始化图片加载器
		initImageLoader();
		
		//初始化商品类型对象
		goodsType = (GoodsType) intent.getSerializableExtra("goodsType");
		
		//初始化商品列表
		initLvGoodsItem();
		
		//初始化标题
		TextView tvTitle = (TextView) this.findViewById(R.id.tvTitle);
		tvTitle.setText(goodsType.getName());
	}
	
	/**
	 * 初始化图片加载器
	 */
	private void initImageLoader(){
		if(imageLoader == null){
			DisplayImageOptions options = new DisplayImageOptions.Builder()
	        .showImageOnLoading(R.drawable.ic_launcher)
	        .showImageForEmptyUri(R.drawable.ic_launcher)
	        .showImageOnFail(R.drawable.ic_launcher)
	        .cacheInMemory(true)
	        .cacheOnDisc(true)
	        .imageScaleType(ImageScaleType.EXACTLY_STRETCHED)//图片缩放方式
	        .displayer(new RoundedBitmapDisplayer(5))//图片圆角
	        .build();
			
			File cacheDir = StorageUtils.getCacheDirectory(this);
			ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this)
												.defaultDisplayImageOptions(options)
			 									.discCache(new TotalSizeLimitedDiscCache(cacheDir,10485760))
			 									.discCacheSize(10485760)//内存卡缓存10M图片
//			 									.memoryCacheSize(10485760)//内存缓存10M图片
//			 									.memoryCacheSize(memoryCacheSize)
												.build();
			
			imageLoader = ImageLoader.getInstance();
			imageLoader.init(config);
		}
	}
	
	/**
	 * 初始化商品列表
	 */
	private void initLvGoodsItem(){
		lvGoodsItem = (ListView) this.findViewById(R.id.lvGoodsItem);
		lvGoodsItem.setOnItemClickListener(this);
		
		//远程加载商品列表
		new Thread(){
			public void run() {
				try {
					GoodsItemBusiness bussiness = new GoodsItemBusiness(GoodsItemActivity.this);
					goodses = bussiness.findGoodsByGoodsType(goodsType, new Page(),handler);
				} catch (Exception e) {
					e.printStackTrace();
					Message msg = Message.obtain();
					msg.what = GoodsItemBusiness.FIND_GOODS_FAILURE;
					handler.sendMessage(msg);
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
	private class GoodsItemHandler extends Handler{
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case GoodsItemBusiness.FIND_GOODS_SUCCESS:
				lvGoodsItem.setAdapter(new LvGoodsItemAdapter());
				break;
			case GoodsItemBusiness.FIND_GOODS_FAILURE:
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
			imageLoader.displayImage(goods.getIcon(), ivGoodsIcon);
			return convertView;
		}
	}
}
