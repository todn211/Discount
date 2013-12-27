package com.zixingchen.discount.widgetex;

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
	private boolean isInterceptEevntToChild;//是否拦截事件传递到子控件
	private View moveTarget;//位于顶层的，需要向左移动的容器

	public MyFocusGoodsItemLoyout(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		this.setLongClickable(true);
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
					float currentX = moveTarget.getX();
					if(currentX != -50)
						moveTarget.setX(0);
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
			else if(targetX < -50)
				targetX = -50f;
			moveTarget.setX(targetX);
			
			//去掉点击产生的背景色
			moveTarget.getBackground().setState(EMPTY_STATE_SET);
			return true;
		}
	}
}
