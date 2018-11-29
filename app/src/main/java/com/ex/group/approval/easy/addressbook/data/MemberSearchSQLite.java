package com.ex.group.approval.easy.addressbook.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * 최근 검색 데이터베이스 클래스
 * @author jokim
 *
 */
public class MemberSearchSQLite extends SQLiteOpenHelper {
	public static final String DATABASE_CREATE = 
			"create table if not exists member (id integer primary key autoincrement, " +
			"serial text not null, " +
			"deptname text not null, " +
			"name text not null, " +
			"telnum text , " +
			"role text , " +
			"work text , " +
			"company text not null, " +
			"phone text, " +
			"email text, " +
			"type text not null, " +
			"infocompanycd text, " +
			"empid text, " +
			"companycd text not null, " +
			"searchdate date , " +
			"deptcode text not null, " +
			"currentkey text, " +
			"landline text, " +
			"vvip text, " +
			"teamManager text, " +
			"memo text);";
	
	public static final String DATABASE_NAME = "MemberSearch.db";
	public static final String DATABASE_TABLE = "member";
	public static final int DATABASE_VERSION = 11;

	public MemberSearchSQLite(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	public void onCreate(SQLiteDatabase db) {
		db.execSQL(DATABASE_CREATE);
	}

	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS member");
		onCreate(db);
	}
}