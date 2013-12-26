package com.zixingchen.discount.widgetex;

import android.content.Context;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.View;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.widget.LinearLayout;

public class MyFocusGoodsItemLoyout extends LinearLayout {

	private GestureDetector gestureDetector;
	private boolean flag;

	public MyFocusGoodsItemLoyout(Context context, AttributeSet attrs) {
		super(context, attrs);

		this.setLongClickable(true);

		gestureDetector = new GestureDetector(context, new MyGestureListener());

		this.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				flag = gestureDetector.onTouchEvent(event);
				System.out.println(flag);
				return true;
			}
		});
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		System.out.println("***********onTouchEvent*************");
		return true;
	}

	class MyGestureListener extends GestureDetector.SimpleOnGestureListener {
		@Override
		public boolean onSingleTapUp(MotionEvent ev) {
//			System.out.println("*****onSingleTapUp******");
			return flag;
		}

		@Override
		public void onShowPress(MotionEvent ev) {
			// System.out.println("**********onShowPress************");
		}
		
		@Override
		public boolean onDown(MotionEvent ev) {
			System.out.println("*****onDown******");
			return false;
		}
		
		@Override
		public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
			super.onScroll(e1, e2, distanceX, distanceY);
			System.out.println("**********onScroll************");
			return true;
		}

		@Override
		public void onLongPress(MotionEvent ev) {
			super.onLongPress(ev);
			flag = false;
			System.out.println("**********onLongPress************");
		}
	}
}
