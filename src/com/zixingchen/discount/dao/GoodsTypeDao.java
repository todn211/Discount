package com.zixingchen.discount.dao;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

import com.zixingchen.discount.model.GoodsType;

/**
 * 商品类别数据库操作类
 * @author 陈梓星
 */
public class GoodsTypeDao {
	private DBHelp dbHelp;
	
	public GoodsTypeDao(Context context) {
		this.dbHelp = new DBHelp(context, DBHelp.VERSION);
	}
	
	/**
	 * 查询商品类别
	 * @param filter 要过滤的条件
	 * @return 商品类别集合
	 */
	public List<GoodsType> findGoodsTypes(GoodsType filter){
		List<GoodsType> goodsTypes = null;
		List<String> args = new ArrayList<String>();
		StringBuilder sql = new StringBuilder("select * from goods_type where 1=1 ");
		if(filter != null){
			if(filter.getId() != null){
				sql.append("and id=? ");
				args.add(String.valueOf(filter.getId()));
			}
			
			if(filter.getParentId() != null){
				sql.append("and parent_id=? ");
				args.add(String.valueOf(filter.getParentId()));
			}
			
			if(!TextUtils.isEmpty(filter.getName())){
				sql.append("and name=? ");
				args.add(filter.getName());
			}
			
			if(!TextUtils.isEmpty(filter.getTypeCode())){
				sql.append("and type_code=? ");
				args.add(filter.getTypeCode());
			}
			
			if(!TextUtils.isEmpty(filter.getKeyWord())){
				sql.append("and key_word=?");
				args.add(filter.getKeyWord());
			}
		}
		
		SQLiteDatabase db = dbHelp.getReadableDatabase();
		Cursor cursor = null;
		try {
			cursor = db.rawQuery(sql.toString(), args.toArray(new String[args.size()]));
			
			if(cursor.getCount() > 0){
				goodsTypes = new ArrayList<GoodsType>(cursor.getCount());
				while(cursor.moveToNext()){
					Long id = cursor.getLong(cursor.getColumnIndex("id"));
					Long parentId = cursor.getLong(cursor.getColumnIndex("parent_id"));
					String name = cursor.getString(cursor.getColumnIndex("name"));
					String typeCode = cursor.getString(cursor.getColumnIndex("type_code"));
					String keyWord = cursor.getString(cursor.getColumnIndex("key_word"));
					String isLeaf = cursor.getString(cursor.getColumnIndex("is_leaf"));
					
					GoodsType goodsType = new GoodsType(id,parentId,name,typeCode,keyWord,isLeaf);
					goodsTypes.add(goodsType);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			if(cursor != null)
				cursor.close();
			
			if(db != null)
				db.close();
		}
		
		return goodsTypes;
	}
}
