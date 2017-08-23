package com.sky.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DbHelper extends SQLiteOpenHelper {
	
	private static final String name = "calendar.db";
	private static int version = 1;

	public DbHelper(Context context) {
		super(context, name, null, version);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		String sql = "create table t_clock_getup(id integer primary key autoincrement, title varchar(128),"
				+ "time varchar(64), sleep varchar(64), cycle varchar(64), bell varchar(64), type varchar(64), triggertime integer)";
		db.execSQL(sql);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	}

	public void close(SQLiteDatabase database) {
		// TODO Auto-generated method stub
		if(database!=null){
			database.close();
		}
	}

}
