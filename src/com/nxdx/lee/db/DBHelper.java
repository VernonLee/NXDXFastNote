package com.nxdx.lee.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * @author 
 * @version 1.0
 * @created 2014-3-30����2:00:22
 * ��˵�������ݿ������
 */
public class DBHelper extends SQLiteOpenHelper {
	
	private static final int VERSION = 1;
	
	/**���ø��๹�췽��*/
	public DBHelper(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);
	}
	
	/**�������ݿ�ʱ����*/
	public DBHelper(Context context, String name, int version) {
		this(context, name, null, version);
	}
	
	/**����������������ʱ����*/
	public DBHelper(Context context, String name) {
		this(context, name, VERSION);
	}

	/**��һ�δ������ݿ�ʱִ�У������½���ṹ�Ͳ���̶�����*/
	@Override
	public void onCreate(SQLiteDatabase db) {
		
		/**����t_ntype:��ǩ���ͱ�*/
		db.execSQL(
				"CREATE TABLE t_ntype (" +	
				"type_code INTEGER PRIMARY KEY AUTOINCREMENT," +
				"type_name VARCHAR(20))"
				);
		
		/**����t_note����ǩ��*/
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
		
		/**�����ǩ��������*/
		db.execSQL("INSERT INTO t_ntype VALUES(1, '���ֱʼ�')");
		db.execSQL("INSERT INTO t_ntype VALUES(2, 'ͼƬ�ʼ�')");
		db.execSQL("INSERT INTO t_ntype VALUES(3, '��Ƶ�ʼ�')");
	}

	/**�������ݿ�ʱ����*/
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {	
		
	}
}
