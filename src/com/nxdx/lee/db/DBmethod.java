package com.nxdx.lee.db;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import com.nxdx.lee.bean.Note;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * @author huailiang
 * @version 1.0
 * @created 2014-3-30下午2:56:55
 * 类说明：数据库操作
 */
public class DBmethod {
	
	private DBHelper mDBhelper;
	private SQLiteDatabase mDB;
	private static String updateTime = "暂无数据";
	
	private ArrayList<Note> mList;
	private static final String DB_NAME = "lee_fastnote.db";
	
	
	/**
	 * 第一使用时创建数据库
	 * @param context
	 */
	public void createDb(Context context) {
		mDBhelper = new DBHelper(context, DB_NAME);
		mDB = mDBhelper.getReadableDatabase();
	}
	
	/**
	 * 添加笔记
	 */
	public void addNote(Context context, Note mNote) {
		mDBhelper = new DBHelper(context, DB_NAME);
		mDB = mDBhelper.getReadableDatabase();
		
		Calendar mDate = Calendar.getInstance();
		//SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String date = sdf.format(mDate.getTime());
		
		ContentValues note = new ContentValues();
		//note.put("id", 1);
		note.put("type", mNote.getType());
		note.put("title", mNote.getTitle());
		note.put("content", mNote.getContent());
		//note.put("priority", mNote.getP);
		note.put("audioURL", mNote.getAudioURL());
		note.put("imageURL", mNote.getImageURL());
		note.put("create_time",date);
		//note.put("update_time","2013-4-1");
		updateTime = date;
		
		mDB.insert("t_note", null, note);	
	}
	
	/**
	 * 更新笔记
	 */
	public void updateNote(Context context, Note mNote) {
		
		mDBhelper = new DBHelper(context, DB_NAME);
		mDB = mDBhelper.getReadableDatabase();
		
		//public int update (String table, ContentValues values
		//, String whereClause, String[] whereArgs)
		
		Calendar mDate = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		//SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String date = sdf.format(mDate.getTime());
		
		ContentValues note = new ContentValues();
		//note.put("id", 1);
		note.put("type", mNote.getType());
		note.put("title", mNote.getTitle());
		note.put("content", mNote.getContent());
		//note.put("priority", mNote.getP);
		note.put("audioURL", mNote.getAudioURL());
		note.put("imageURL", mNote.getImageURL());
		//note.put("create_time",date);
		note.put("update_time",date);
		
		int id = mNote.getId();
		updateTime = date;
		
		mDB.update("t_note", note, "id=?", new String[]{""+id});
	}
	
	/**
	 * 获取所有笔记
	 */
	public ArrayList<Note> getNoteData(Context context) {
		
		mDBhelper = new DBHelper(context, DB_NAME);
		mDB = mDBhelper.getReadableDatabase();
		
		//String table, String[] columns, String selection, String[] selectionArgs
		//, String groupBy, String having, String orderBy
		mList = new ArrayList<Note>();
		Cursor cursor = mDB.query("t_note", new String[]{"*"}, null, null, null, null, null);
		while(cursor.moveToNext()){
			Note note = new Note();
			note.setId(cursor.getInt((cursor.getColumnIndex("id"))));
			note.setType(cursor.getInt((cursor.getColumnIndex("type"))));
			note.setTitle(cursor.getString((cursor.getColumnIndex("title"))));
			note.setContent(cursor.getString((cursor.getColumnIndex("content"))));
			note.setAudioURL(cursor.getString((cursor.getColumnIndex("audioURL"))));
			note.setImageURL(cursor.getString((cursor.getColumnIndex("imageURL"))));
			note.setCreateTime(cursor.getString((cursor.getColumnIndex("create_time"))));
			note.setUpdateTime(cursor.getString((cursor.getColumnIndex("update_time"))));
			
			mList.add(note);
		}
		return mList;
	}
	
	public Note getContentById(Context context, int id) {
		
		mDBhelper = new DBHelper(context, DB_NAME);
		mDB = mDBhelper.getReadableDatabase();
		
		Note note = new Note();
		Cursor cursor = mDB.query("t_note", new String[]{"*"}, "id=?", new String[]{id+""}, null, null, null);
		while(cursor.moveToNext()) {
			note.setId(cursor.getInt((cursor.getColumnIndex("id"))));
			note.setType(cursor.getInt((cursor.getColumnIndex("type"))));
			note.setTitle(cursor.getString((cursor.getColumnIndex("title"))));
			note.setContent(cursor.getString((cursor.getColumnIndex("content"))));
			note.setAudioURL(cursor.getString((cursor.getColumnIndex("audioURL"))));
			note.setImageURL(cursor.getString((cursor.getColumnIndex("imageURL"))));
			note.setCreateTime(cursor.getString((cursor.getColumnIndex("create_time"))));
			note.setUpdateTime(cursor.getString((cursor.getColumnIndex("update_time"))));
		}
		return note;
	}
	
	/**
	 * 获取最新更新时间
	 * @return
	 */
	public String getUpdateTime() {
		return updateTime;
	}	
	
	public void delete(Context context, int id) {
		mDBhelper = new DBHelper(context, DB_NAME);
		mDB = mDBhelper.getReadableDatabase();
		
		mDB.delete("t_note", "id=?", new String[]{""+id});
		
	}
}
