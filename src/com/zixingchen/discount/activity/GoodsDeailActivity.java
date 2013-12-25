package com.zixingchen.discount.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.zixingchen.discount.R;
import com.zixingchen.discount.business.GoodsBusiness;
import com.zixingchen.discount.model.Goods;

/**
 * 商品详细信息展示页面 
 * @author 陈梓星
 */
public class GoodsDeailActivity extends Activity {
	
	private WebView wvGoodsDetail;//显示商品详细信息的容器
	private Goods goods;//当前商品对象
	private RelativeLayout llShade;//遮罩层
	private GoodsBusiness bussiness = new GoodsBusiness();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.goods_detail_activity);
		
		//获取商品对象
		goods = (Goods) this.getIntent().getSerializableExtra("GoodsItem");
		
		//阻止遮罩层事件往下传递
		llShade = (RelativeLayout) this.findViewById(R.id.llShade);
		llShade.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				return true;
			}
		});
		
		//初始化商品内容容器
		initWvGoodsDetail();
	}
	
	/**
	 * 初始化商品内容容器
	 */
	@SuppressLint("SetJavaScriptEnabled")
	private void initWvGoodsDetail(){
		wvGoodsDetail = (WebView) this.findViewById(R.id.wvGoodsDetail);
		wvGoodsDetail.getSettings().setJavaScriptEnabled(true);
		
		wvGoodsDetail.setWebViewClient(new WebViewClient(){
			/**
			 * 点击网页中按钮时，在原页面打开
			 */
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				view.loadUrl(url);
				return true;
			}
			
			/**
			 * 页面加载完成时删除遮罩层
			 */
			@Override
			public void onPageFinished(WebView view, String url) {
				super.onPageFinished(view, url);
				FrameLayout goodsDeailLayout = (FrameLayout)GoodsDeailActivity.this.findViewById(R.id.goodsDeailLayout);
				goodsDeailLayout.removeView(llShade);
			}
			
			@Override
			public void onReceivedError(WebView view, int errorCode,
					String description, String failingUrl) {
				super.onReceivedError(view, errorCode, description, failingUrl);
				Toast.makeText(GoodsDeailActivity.this, "加载页面失败！", Toast.LENGTH_LONG).show();
			}
		});
		
		wvGoodsDetail.loadUrl(goods.getHref());
	}
	
	/**
	 * 返回上一级
	 * @param view
	 */
	public void onBtBackClick(View view){
		this.finish();
		this.overridePendingTransition(R.anim.in_from_left,R.anim.out_to_right);
	}
	
	/**
	 * 关注商品
	 * @param view
	 */
	public void onBtFocusGoodsClick(View view){
		boolean addResult = bussiness.addFocusGoods(goods);
		if(addResult){
			Toast.makeText(this, "关注商品成功！", Toast.LENGTH_SHORT).show();
		}else{
			Toast.makeText(this, "关注商品失败！", Toast.LENGTH_SHORT).show();
		}
	}
}