package me.yeso.yeelink.util;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class YeelinkDBHelper extends SQLiteOpenHelper {

	private static final String CTEATE_TABLE_USER_SQL=
			"create table user(user_id INTEGER PRIMARY KEY AUTOINCREMENT,username nvarchar(64) not null unique,password nvarchar(64) not null,apikey nvarchar(32) not null unique)";

	public YeelinkDBHelper(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		//首次使用时创建表
		db.execSQL(CTEATE_TABLE_USER_SQL);

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub

	}

}
