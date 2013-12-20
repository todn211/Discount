package com.zixingchen.discount.utils;

import com.zixingchen.discount.dao.DBHelp;

import android.app.Application;

/**
 * 程序启动时执行的类，用于在普通Object中方便获取context对象
 * @author 陈梓星
 */
public class ContextUtil extends Application {
	private static ContextUtil instance;
	
	@Override
	public void onCreate() {
		super.onCreate();
		instance = this;
		
        //初始化数据库
		new DBHelp(this,DBHelp.VERSION).getReadableDatabase();
	}
	
	public static ContextUtil getInstance() {
		return instance;
	}
}