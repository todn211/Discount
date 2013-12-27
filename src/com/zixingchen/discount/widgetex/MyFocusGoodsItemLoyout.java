package com.zixingchen.discount.widgetex;

import com.zixingchen.discount.R;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;

public class MyFocusGoodsItemLoyout extends RelativeLayout {

	private GestureDetector gestureDetector;
	private Context context;
	private boolean isInterceptEevntToChild = true;//是否拦截事件传递到子控件
	private View moveTarget;//位于顶层的，需要向左移动的容器
	private int btDeleteWidth;//删除按键的宽度

	public MyFocusGoodsItemLoyout(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		this.setLongClickable(true);
	}
	
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		
		btDeleteWidth = this.findViewById(R.id.btDelete).getMeasuredWidth();
	}
	
	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
		gestureDetector = new GestureDetector(context, new MyGestureListener());
		this.setOnTouchListener(new OnTouchListener() {
			
			@Override
			@SuppressLint("NewApi")
			public boolean onTouch(View v, MotionEvent event) {
				if(MotionEvent.ACTION_UP == event.getAction()){
					if(moveTarget.getX() != -btDeleteWidth){
						moveTarget.setX(0);
						isInterceptEevntToChild = true;
					}else{
						isInterceptEevntToChild = false;
					}
				}
				return gestureDetector.onTouchEvent(event);
			}
		});
	}
	
	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		super.onInterceptTouchEvent(ev);
		return isInterceptEevntToChild;
	}

	private class MyGestureListener extends GestureDetector.SimpleOnGestureListener {
		public MyGestureListener() {
			moveTarget = MyFocusGoodsItemLoyout.this.getChildAt(1);
		}
		
		@Override
		@SuppressLint("NewApi")
		public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
			float targetX = moveTarget.getX() - distanceX;
			if(targetX > 0)
				targetX = 0f;
			else if(targetX < -btDeleteWidth)
				targetX = -btDeleteWidth;
			moveTarget.setX(targetX);
			
			//去掉点击产生的背景色
			moveTarget.getBackground().setState(EMPTY_STATE_SET);
			return true;
		}
	}
}
