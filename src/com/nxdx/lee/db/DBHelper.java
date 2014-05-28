package com.nxdx.lee.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * @author 
 * @version 1.0
 * @created 2014-3-30下午2:00:22
 * 类说明：数据库帮助类
 */
public class DBHelper extends SQLiteOpenHelper {
	
	private static final int VERSION = 1;
	
	/**调用父类构造方法*/
	public DBHelper(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);
	}
	
	/**升级数据库时调用*/
	public DBHelper(Context context, String name, int version) {
		this(context, name, null, version);
	}
	
	/**插入或更新数据内容时调用*/
	public DBHelper(Context context, String name) {
		this(context, name, VERSION);
	}

	/**第一次创建数据库时执行，用来新建表结构和插入固定数据*/
	@Override
	public void onCreate(SQLiteDatabase db) {
		
		/**创建t_ntype:便签类型表*/
		db.execSQL(
				"CREATE TABLE t_ntype (" +	
				"type_code INTEGER PRIMARY KEY AUTOINCREMENT," +
				"type_name VARCHAR(20))"
				);
		
		/**创建t_note：便签表*/
		db.execSQL(
				"CREATE TABLE t_note (" +
				"id INTEGER PRIMARY KEY AUTOINCREMENT," +
				"type INT," +
				"title VARCHAR(100)," +
				"content TEXT," +
				"priority INT," +
				"audioURL VARCHAR(300)," +
				"imageURL VARCHAR(300)," +
				"create_time DATE," +
				"update_time DATE," +
				"FOREIGN KEY(type) REFERENCES t_ntype(type_code))"
				);
		
		/**插入便签类型数据*/
		db.execSQL("INSERT INTO t_ntype VALUES(1, '文字笔记')");
		db.execSQL("INSERT INTO t_ntype VALUES(2, '图片笔记')");
		db.execSQL("INSERT INTO t_ntype VALUES(3, '音频笔记')");
	}

	/**升级数据库时调用*/
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {	
		
	}
}
