package com.ex.group.mail.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * 자동 완성 메일 EmailSendAddr.db
 * @author sjsun5318
 *
 */
public class EmailWriteSQLite extends SQLiteOpenHelper {
	public static final String DATABASE_CREATE =
		"create table if not exists emailsendaddr(_id integer primary key autoincrement,"+
		"name text , email text not null);";
	
	public static final String DATABASE_NAME = "CJ_EmailSendAddr.db";
	public static final String DATABASE_TABLE = "emailsendaddr";
	public static final String DATABASE_SIGN_TABLE = "emailsign";
	public static final int DATABASE_VERSION = 2;

	/**
	 * EmailWriteSQLite 메소드
	 * @param context
	 */
	public EmailWriteSQLite(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	/**
	 * onCreate 메소드
	 */
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(DATABASE_CREATE);
	}

	/**
	 * onUpgrade 메소드
	 */
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS member");
		onCreate(db);
	}
	
}