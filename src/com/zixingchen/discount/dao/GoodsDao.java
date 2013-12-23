package com.zixingchen.discount.dao;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.webkit.WebChromeClient.CustomViewCallback;

import com.zixingchen.discount.common.Page;
import com.zixingchen.discount.model.Goods;
import com.zixingchen.discount.model.GoodsType;
import com.zixingchen.discount.utils.ContextUtil;

/**
 * 商品数据库操作类
 * @author 陈梓星
 */
public class GoodsDao {
private DBHelp dbHelp;
	
	public GoodsDao() {
		this.dbHelp = new DBHelp(ContextUtil.getInstance(), DBHelp.VERSION);
	}
	
	/**
	 * 添加关注关注商品
	 * @param goods 要关注的商品
	 * @return 添加成功返回true，否则返回false
	 */
	public boolean addFocusGoods(Goods goods){
		SQLiteDatabase db = dbHelp.getWritableDatabase();
		try {
			db.beginTransaction();
			ContentValues values = new ContentValues();
			values.put("GOODS_TYPE_ID", goods.getGoodsTypeId());
			values.put("NAME", goods.getName());
			values.put("SUB_TITLE", goods.getSubTitle());
			values.put("PRICE", goods.getCurrentPrice());
			values.put("ICON", goods.getIcon());
			values.put("DESCRIPT", goods.getDescript());
			values.put("HREF", goods.getHref());
			db.insert("focus_goods", null, values);
			db.setTransactionSuccessful();
			
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		} finally{
			db.endTransaction();
			db.close();
		}
		return false;
	}
	
	/**
	 * 根据商品类型获取相应关注的商品列表
	 * @param goodsType 商品类型
	 * @param page 分页对象
	 * @return 关注的商品列表
	 */
	public List<Goods> findFocusGoodsByGoodsType(GoodsType goodsType,Page<Goods> page){
		final List<Goods> goodses = new ArrayList<Goods>();
		
		SQLiteDatabase db = dbHelp.getReadableDatabase();
		Cursor cursor = null;
		try {
			String sql = "select * from focus_goods where goods_type_id=?";
			cursor = db.rawQuery(sql, new String[]{goodsType.getId().toString()});
			while(cursor.moveToNext()){
				Goods goods = new Goods();
				goods.setGoodsTypeId(cursor.getLong(cursor.getColumnIndex("GOODS_TYPE_ID")));
				goods.setHref(cursor.getString(cursor.getColumnIndex("HREF")));
				goods.setIcon(cursor.getString(cursor.getColumnIndex("ICON")));
				goods.setId(cursor.getLong(cursor.getColumnIndex("ID")));
				goods.setName(cursor.getString(cursor.getColumnIndex("NAME")));
				goods.setPrePrice(cursor.getFloat(cursor.getColumnIndex("PRICE")));
				
				goodses.add(goods);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally{
			if(cursor != null)
				cursor.close();
			db.close();
		}
		
		return goodses;
	}
}
