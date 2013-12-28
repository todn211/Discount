package com.zixingchen.discount.widgetex.ExpandableListViewSuper;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RelativeLayout;
import com.zixingchen.discount.R;

/**
 * 我的关注列表子元素视图
 * @author 陈梓星
 */
@SuppressLint("NewApi")
public class MyFocusGoodsItemLoyout extends RelativeLayout {

	private GestureDetector gestureDetector;
	private Context context;
	private Button btDelete;
	private boolean isInterceptEevntToChild = true;//是否拦截事件传递到子控件
	private View moveTarget;//位于顶层的，需要向左移动的容器
	private int btDeleteWidth;//删除按键的宽度
	private int groupPosition;//分组位置
	private int childPosition;//组下面元素子位置

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
		
		//发布删除事件
		btDelete = (Button) this.findViewById(R.id.btDelete);
		btDelete.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				ExpandableListViewSuper expandableListView = (ExpandableListViewSuper) MyFocusGoodsItemLoyout.this.getParent();
				expandableListView.getOnChildOperationListener()
									.onChildDelete(expandableListView, MyFocusGoodsItemLoyout.this, groupPosition, childPosition);
			}
		});
		
		//监听触摸事件
		gestureDetector = new GestureDetector(context, new MyGestureListener());
		this.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if(MotionEvent.ACTION_UP == event.getAction()){
					if(moveTarget.getLeft() != -btDeleteWidth){
						resetViewPosition();
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
	
	/**
	 * 重置视图位置
	 */
	public void resetViewPosition(){
//		RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) moveTarget.getLayoutParams();
//		params.leftMargin = 0;
//		params.width = getMeasuredWidth();
//		params.height = getMeasuredHeight();
//		moveTarget.setLayoutParams(params);
		
		moveTarget.setX(0);
	}
	
	/**
	 * 手势识别对象
	 */
	private class MyGestureListener extends GestureDetector.SimpleOnGestureListener {
		public MyGestureListener() {
			moveTarget = MyFocusGoodsItemLoyout.this.getChildAt(1);
		}
		
		/**
		 * 我的关注元素点击事件
		 */
		@Override
		public boolean onSingleTapUp(MotionEvent e) {
			super.onSingleTapUp(e);
			ExpandableListViewSuper expandableListView = (ExpandableListViewSuper) MyFocusGoodsItemLoyout.this.getParent();
			expandableListView.getOnChildOperationListener()
								.onChildClick(expandableListView, MyFocusGoodsItemLoyout.this, groupPosition, childPosition);
			return true;
		}
		
		/**
		 * 顶层视图向左滑动，显示出删除按钮
		 */
		@Override
		public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
			int targetX = moveTarget.getLeft() - (int)distanceX;
			if(targetX > 0)
				targetX = 0;
			else if(targetX < -btDeleteWidth)
				targetX = -btDeleteWidth;
			
			RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) moveTarget.getLayoutParams();
			params.leftMargin = targetX;
			params.width = MyFocusGoodsItemLoyout.this.getMeasuredWidth();
			moveTarget.setLayoutParams(params);
			
			//去掉点击产生的背景色
			moveTarget.getBackground().setState(EMPTY_STATE_SET);
			return true;
		}
	}

	public int getGroupPosition() {
		return groupPosition;
	}

	public void setGroupPosition(int groupPosition) {
		this.groupPosition = groupPosition;
	}

	public int getChildPosition() {
		return childPosition;
	}

	public void setChildPosition(int childPosition) {
		this.childPosition = childPosition;
	}
}
