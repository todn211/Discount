package com.zixingchen.discount.activity;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.zixingchen.discount.R;
import com.zixingchen.discount.model.Goods;

/**
 * 商品详细信息展示页面 
 * @author 陈梓星
 */
public class GoodsDeailActivity extends Activity {
	
	private WebView wvGoodsDetail;
	private Goods goodsItem;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.goods_detail_activity);
		
		//获取商品项对象
		goodsItem = (Goods) this.getIntent().getSerializableExtra("GoodsItem");
		
		//初始化商品内容容器
		initWvGoodsDetail();
	}
	
	/**
	 * 初始化商品内容容器
	 */
	private void initWvGoodsDetail(){
		wvGoodsDetail = (WebView) this.findViewById(R.id.wvGoodsDetail);
		wvGoodsDetail.getSettings().setJavaScriptEnabled(true);
		wvGoodsDetail.setWebViewClient(new WebViewClient(){
			//点击网页中按钮时，在原页面打开
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				view.loadUrl(url);
				return true;
			}
		});
		wvGoodsDetail.loadUrl(goodsItem.getHref());
	}
}