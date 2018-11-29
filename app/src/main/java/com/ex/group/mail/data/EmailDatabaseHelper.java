package com.ex.group.mail.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

import com.ex.group.mail.activity.EmailReceiveActivity;
import com.ex.group.mail.service.EmailMainThread;
import com.ex.group.mail.service.EmailReceiveThread;
import com.ex.group.mail.util.EmailClientUtil;
import com.skt.pe.common.exception.SKTException;
import com.skt.pe.util.StringUtil;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * EmailTable.db
 * @author sjsun5318
 *
 */
public class EmailDatabaseHelper extends SQLiteOpenHelper {
	private String TAG = "DBHelper";
	
	
	private static final String DATABASE_NAME = "HI_MOFFICE_MAIL.db";
	private static final int DATABASE_VERSION = 1;
	private SQLiteDatabase mSQLiteDatabase = null;
	public static final String MAIN_TABLE 			= "EX_TABLE_MAIN";
	public static final String EMAILADDRESS_TABLE 	= "EX_TABLE_ADDRESS";
	public static final String MAIL_LIST_TABLE		= "EX_TABLE_MAIL_LIST_";
	public static final String MAIN_UPDATE_TABLE 	= "EX_TABLE_MAIN_UPDATE";

	private final String MAIN_ID 		= "_ID";
	private final String RECEIVE_ID 	= "_ID";
	private final String ADDRESS_ID 	= "_ID";
	private final String BOXUPDATE_ID 	= "_ID";
	private final String MDN 			= "MDN";
	private final String COMPANYCD 		= "COMPANY_CD";
	private final String BOXTYPE 		= "BOX_TYPE";
	private final String PARENTBOXTYPE	= "PARENT_BOX_TYPE";
	private final String BOXID 			= "BOX_ID";
	private final String BOXCHANGKEY 	= "BOX_CHANGKEY";
	private final String BOXNAME 		= "BOX_NAME";
	private final String TOTALCNT 		= "TOTAL_COUNT";
	private final String UPDATEDATE		= "UPDATE_DATE";
	private final String BOXORDER		= "BOX_ORDER";
	private final String BOXUNREADCOUNT	= "BOX_UNREAD_COUNT";
	private final String BOXLEVEL		= "BOX_LEVEL";

	private final String ADDRESS		= "ADDRESS";
	
	private final String BOXUPDATE		= "BOX_UPDATE";

	private final String MAILID 		= "MAIL_ID";
	private final String MAILCHANGEKEY 	= "MAIL_CHANGEKEY";
	private final String MAILSUBJECT 	= "MAIL_SUBJECT";
	private final String HASATTACHMENTS = "HAS_ATTACHMENTS";
	private final String RECEIVEDATE 	= "RECEIVE_DATE";
	private final String SENDDATE 		= "SEND_DATE";
	private final String ISREAD 		= "IS_READ";
	private final String FROMINFO 		= "FROM_INFO";
	private final String TOLIST 		= "TO_LIST";
	private final String MAILTYPE 		= "MAILTYPE";

	private final String DATABASE_CREATE_MAIN_TABLE = "create table " + MAIN_TABLE +

	" ( " +  MAIN_ID+ " INTEGER PRIMARY KEY AUTOINCREMENT,"  +
	MDN		 		+ " text not null, " +
	COMPANYCD 		+ " text not null, " +
	BOXTYPE 		+ " text , " +
	PARENTBOXTYPE	+ " text , " +
	BOXID 			+ " text , " +
	BOXCHANGKEY 	+ " text , " +
	BOXNAME 		+ " text , " +
	TOTALCNT 		+ " text , " + 
	UPDATEDATE 		+ " text , " + 
	BOXORDER 		+ " text , " +
	BOXLEVEL 		+ " text , " +
	BOXUNREADCOUNT	+ " text  ) ;";

	private final String DATABASE_ADDRESS_CRATE_TABLE = "create table " + EMAILADDRESS_TABLE +
	" (" + 	ADDRESS_ID+ " INTEGER PRIMARY KEY AUTOINCREMENT,"  +
	MDN 			+ " text not null, " +
	COMPANYCD 		+ " text not null, " +
	ADDRESS			+ " text not null );";

	private final String DATABASE_BOX_UPDATE_CRATE_TABLE = "create table " + MAIN_UPDATE_TABLE +
	" (" + 	BOXUPDATE_ID+ " INTEGER PRIMARY KEY AUTOINCREMENT,"  +
	MDN 			+ " text not null, " +
	COMPANYCD 		+ " text not null, " +
	BOXUPDATE		+ " text );";
	
	private static final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

	private static Map<String, String> RECEIVE_TABLE_LIST = null;

	public EmailDatabaseHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	/**
	 * 테이블 만들기
	 */
	@Override
	public void onCreate(SQLiteDatabase database) {
		try{
			database.execSQL(DATABASE_CREATE_MAIN_TABLE);
			database.execSQL(DATABASE_ADDRESS_CRATE_TABLE);
			database.execSQL(DATABASE_BOX_UPDATE_CRATE_TABLE);
			mSQLiteDatabase = database;	
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
	}

	/**
	 * 만들테이블 체크
	 */
	public void setReceiveTableList() {
		Map<String,String> map = null;

		Cursor cursor = null;
		try {
			cursor = getReadableDatabase().rawQuery("select tbl_name from sqlite_master where tbl_name like '" + MAIL_LIST_TABLE + "%'", null);
			if(cursor!=null) {
				map = new HashMap<String, String>();
				while(cursor.moveToNext()) {
					String name = cursor.getString(cursor.getColumnIndex("tbl_name")).toUpperCase();
					int index = name.indexOf(MAIL_LIST_TABLE) + MAIL_LIST_TABLE.length();
					map.put(name.substring(index), name);
					
				}
				
				synchronized(EmailReceiveActivity.class) {
					RECEIVE_TABLE_LIST = map;
				}
			}
		} catch(SQLException e) {
			e.printStackTrace();
		} finally {
			if(cursor != null) {
				cursor.close();
			}
		}
	}
	
	/**
	 * 테이블 SEQ 체크
	 * @param seq
	 * @return
	 */
	public static boolean existReceiveTableList(String seq) {
		if(RECEIVE_TABLE_LIST != null) {
			return RECEIVE_TABLE_LIST.containsKey(seq);
		} else {
			return false;
		}
	}

	/**
	 * 테이블 있는지 여부
	 * @return
	 */
	public static boolean existReceiveTableList() {
		if(RECEIVE_TABLE_LIST != null) {
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * 버전체크후 테이블 만들기
	 */
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

		Cursor cursor = null;
		try {
			
			cursor = db.rawQuery("select tbl_name from sqlite_master",null);
			if(cursor != null) {
				while (cursor.moveToNext()) {
					String[] table = cursor.getString(cursor.getColumnIndex("tbl_name")).split("_");
					if(table[0].equals("EX")) {
						db.execSQL("drop table if exists " + cursor.getString(cursor.getColumnIndex("tbl_name")));
					}
				}
			}
		} catch(SQLException e) {
			e.printStackTrace();
		} finally {
			if(cursor != null) {
				cursor.close();
			}
		}
		
		onCreate(db);
	}
	
	/**
	 * 테이블 삭제
	 */
	public void dropTables() {

		int ret = 0;
		Cursor cursor = null;
		try {
			ret += getWritableDatabase().delete(MAIN_TABLE, null, null);
			cursor = getReadableDatabase().rawQuery("select tbl_name from sqlite_master",null);
			if(cursor != null) {
				while (cursor.moveToNext()) {
					String table = cursor.getString(cursor.getColumnIndex("tbl_name"));
					if(table.indexOf(MAIL_LIST_TABLE) != -1) {
						getWritableDatabase().execSQL("drop table if exists " + cursor.getString(cursor.getColumnIndex("tbl_name")));
					}
				}
			}
		} catch(SQLException e) {
			e.printStackTrace();
		} finally {
			if(cursor != null) {
				cursor.close(); 
			}
		}
	}
	
	/**
	 * 메일함 폴더 마다 테이블 만들기
	 * @param _id
	 */
	public void createTable(String _id) {
		String tableName = MAIL_LIST_TABLE+_id;
		lock();
		Log.i(TAG, "createTable name =====>> "+tableName);
		String createReceiveTable = "create table " + tableName +
		" ( " +  RECEIVE_ID+ " INTEGER PRIMARY KEY AUTOINCREMENT,"  +
		MAILID		 		+ " text not null, " +
		MAILCHANGEKEY 		+ " text not null, " +
		MAILSUBJECT 		+ " text , " +
		HASATTACHMENTS		+ " text , " +
		RECEIVEDATE 		+ " text not null, " +
		SENDDATE 			+ " text not null, " +
		ISREAD 				+ " text not null, " +
		FROMINFO 			+ " text not null, " +
		TOLIST				+ " text not null, " +
		TOTALCNT			+ " text not null, " +
		MAILTYPE			+ " text not null );";
		
		getWritableDatabase().execSQL(createReceiveTable);

		setReceiveTableList();

		unlock();
	}

	/**
	 * db 에서 데이타 가져오기
	 * @param tableName
	 * @param columns
	 * @param selection
	 * @param order
	 * @return
	 */
	private Cursor queryTable(String tableName , String[] columns, String selection, String order) {
		
		Cursor cursor = null;
		readLock();
		try {
			cursor = getReadableDatabase().query(true, tableName, columns, selection, null,
					null, null, order, null);
		} catch(SQLException e) {
			e.printStackTrace();
		} finally {
			unReadLock();
		}
		return cursor;
	}

	/**
	 * 테이블 삭제
	 * @param id
	 */
	private void deleteReceiveTable(String id ) {
		String tableName = MAIL_LIST_TABLE+id;
		mSQLiteDatabase = getWritableDatabase();
		mSQLiteDatabase.execSQL("drop table if exists " + tableName);
	}
	
	/**
	 * 메일함 데이타 insert
	 * @param emailData
	 * @param thread
	 * @return
	 * @throws SKTException
	 */
	public long insertMainTable(ArrayList<EmailMainListData> emailData, EmailMainThread thread) throws SKTException {
		long ret = 0;
		lock();
		try {
			getWritableDatabase().beginTransaction();
			String value = "";
			EmailMainListData[] oldData = null;
			SQLiteStatement insert = null;
			if(emailData == null || emailData.size() <= 0 ) {
			} else {
				
				String sql = "";
				sql = "insert into " + MAIN_TABLE 	+ " ( " 
				+ MDN 			+ ","
				+ COMPANYCD 	+ ","
				+ BOXTYPE 		+ ","
				+ PARENTBOXTYPE	+ ","
				+ BOXID 		+ ","
				+ BOXCHANGKEY 	+ ","
				+ BOXNAME 		+ ","
				+ TOTALCNT 		+ ","
				+ UPDATEDATE	+ ","
				+ BOXORDER		+ ","
				+ BOXLEVEL		+ ","
				+ BOXUNREADCOUNT+
				" ) values (?,?,?,?,?,?,?,?,?,?,?,?)";
				List<String> oldId = new ArrayList<String>();
				
				if(thread.stopFlag)
					throw new SKTException("xxxxxxxxxxx main a1");
				
				oldData = getMainTableData(emailData.get(0).getMdn(),emailData.get(0).getCompanyCd());

				if(thread.stopFlag)
					throw new SKTException("xxxxxxxxxxx main a2");

				for (int a = 0; a < emailData.size() ; a++) {
					EmailMainListData data = emailData.get(a);
					if(oldData != null) {
						for(int b = 0 ; b < oldData.length ; b ++ ) {
							if(data.getBoxType().equals("P")) {
								//if(data.getBoxId().equals(oldData[b].getBoxId()) && data.getBoxChangeKey().equals(oldData[b].getBoxChangeKey())) {
								if(data.getBoxId().equals(oldData[b].getBoxId())) {
									oldId.add(oldData[b].getId());
								}
							} else {
								if(data.getBoxType().equals(oldData[b].getBoxType())) {
									oldId.add(oldData[b].getId());
								}
							}
						}
					}
				}
				
				if(oldId.size() > 0 ){
					String[] ids = new String[oldId.size()];
					ids = oldId.toArray(ids);
					
					if(thread.stopFlag)
						throw new SKTException("xxxxxxxxxxx main a3");

					deleteReceiveTable(ids,emailData.get(0).getMdn(),emailData.get(0).getCompanyCd());
					
					if(thread.stopFlag)
						throw new SKTException("xxxxxxxxxxx main a4");

					deleteMainTable(ids,emailData.get(0).getMdn(),emailData.get(0).getCompanyCd());
					
					if(thread.stopFlag)
						throw new SKTException("xxxxxxxxxxx main a5");
				}
				
				for (int a = 0; a < emailData.size() ; a++) {
					EmailMainListData data = emailData.get(a);
					value = getMainTableString(data);
					
					if(StringUtil.isNull(value) || value.equals("0")) {
						insert = getWritableDatabase().compileStatement(sql);
						data.setEncryption();
						insert.bindString(1, StringUtil.isNull(data.getMdn()) 			? "" : data.getMdn());
						insert.bindString(2, StringUtil.isNull(data.getCompanyCd())		? "" : data.getCompanyCd() );
						insert.bindString(3, StringUtil.isNull(data.getBoxType())		? "" : data.getBoxType() );
						insert.bindString(4, StringUtil.isNull(data.getParentBoxType())	? "" : data.getParentBoxType() );
						insert.bindString(5, StringUtil.isNull(data.getBoxId())			? "" : data.getBoxId() ); 
						insert.bindString(6, StringUtil.isNull(data.getBoxChangeKey())	? "" : data.getBoxChangeKey() ); 
						insert.bindString(7, StringUtil.isNull(data.getBoxName())		? "" : data.getBoxName() ); 
						insert.bindString(8, StringUtil.isNull(data.getTotalCnt())		? "" : data.getTotalCnt() );
						insert.bindString(9, StringUtil.isNull(data.getUpdatedate())	? "" : data.getUpdatedate() );
						insert.bindString(10, StringUtil.isNull(data.getBoxOrder())		? "" : data.getBoxOrder() );
						insert.bindString(11, StringUtil.isNull(data.getBoxLevel())		? "" : data.getBoxLevel() );
						insert.bindString(12, StringUtil.isNull(data.getUnreadCnt())	? "" : data.getUnreadCnt() );

						if(thread.stopFlag)
							throw new SKTException("xxxxxxxxxxx main a6");

						insert.executeInsert();

						if(thread.stopFlag)
							throw new SKTException("xxxxxxxxxxx main a7");
					} else {						
						if(thread.stopFlag)
							throw new SKTException("xxxxxxxxxxx main a8");

						updateTable(value,data);

						if(thread.stopFlag)
							throw new SKTException("xxxxxxxxxxx main a9");
					}
				}

			} 
			getWritableDatabase().setTransactionSuccessful();

		} catch(SQLException e) {
			throw new SKTException(e);
		} finally {
			getWritableDatabase().endTransaction();
			unlock();
		} 

		return ret;
	}
	
	
	/**
	 * 메일함 데이타 insert
	 * @param data
	 * @return
	 */
	public long insertMainTable(EmailMainListData data) {
		long ret = 0;
		lock();
		try {

			getWritableDatabase().beginTransaction();
			String value = "";
			SQLiteStatement insert = null;
			if(data == null ) {
				throw new SQLException();
			} else {
				String sql = "";
				sql = "insert into " + MAIN_TABLE 	+ " ( " 
				+ MDN 			+ ","
				+ COMPANYCD 	+ ","
				+ BOXTYPE 		+ ","
				+ PARENTBOXTYPE	+ ","
				+ BOXID 		+ ","
				+ BOXCHANGKEY 	+ ","
				+ BOXNAME 		+ ","
				+ TOTALCNT 		+ ","
				+ UPDATEDATE	+ ","
				+ BOXORDER		+ ","
				+ BOXLEVEL		+ ","
				+ BOXUNREADCOUNT+
				" ) values (?,?,?,?,?,?,?,?,?,?,?,?)";

				
				value = getMainTableString(data);
				if(StringUtil.isNull(value) || value.equals("0")) {
					insert = getWritableDatabase().compileStatement(sql);

					data.setEncryption();
					
					insert.bindString(1, StringUtil.isNull(data.getMdn()) 			? "" : data.getMdn());
					insert.bindString(2, StringUtil.isNull(data.getCompanyCd())		? "" : data.getCompanyCd() );
					insert.bindString(3, StringUtil.isNull(data.getBoxType())		? "" : data.getBoxType() );
					insert.bindString(4, StringUtil.isNull(data.getParentBoxType())	? "" : data.getParentBoxType() );
					insert.bindString(5, StringUtil.isNull(data.getBoxId())			? "" : data.getBoxId() ); 
					insert.bindString(6, StringUtil.isNull(data.getBoxChangeKey())	? "" : data.getBoxChangeKey() ); 
					insert.bindString(7, StringUtil.isNull(data.getBoxName())		? "" : data.getBoxName() ); 
					insert.bindString(8, StringUtil.isNull(data.getTotalCnt())		? "" : data.getTotalCnt() );
					insert.bindString(9, StringUtil.isNull(data.getUpdatedate())	? "" : data.getUpdatedate() );
					insert.bindString(10, StringUtil.isNull(data.getBoxOrder())		? "" : data.getBoxOrder() );
					insert.bindString(11, StringUtil.isNull(data.getBoxLevel())		? "0" : data.getBoxLevel() );
					insert.bindString(12, StringUtil.isNull(data.getUnreadCnt())	? "" : data.getUnreadCnt() );
					insert.executeInsert();

				} else {
					updateTable(value,data);
				}

			} 
			getWritableDatabase().setTransactionSuccessful();

		} catch(SQLException e) {
			e.printStackTrace();
		} finally {
			getWritableDatabase().endTransaction();
			unlock();
		} 

		return ret;
	}

	/**
	 * 데이타 삭제
	 * @param id
	 * @param mdn
	 * @param companyCd
	 * @return
	 */
	private int deleteMainTable(String[] id , String mdn , String companyCd) {
					
		int ret = 0;
			try {
				String ids = "";
				for(int i = 0; i < id.length; i++) {
					ids += MAIN_ID + " != " + id[i] + " and ";
				}
				ret += getWritableDatabase().delete(MAIN_TABLE, ids + MDN + " = '" + mdn	+ "' and " + COMPANYCD + " = '" + companyCd	+ "'",null);
				
				
			} catch(SQLException e) {
				e.printStackTrace();
			} finally {
			}
			return ret;
	}
	
	/**
	 * 메일리스트 데이타 삭제
	 * @param id
	 * @param mdn
	 * @param companyCd
	 */
	private void deleteReceiveTable(String[] id , String mdn , String companyCd) {
		Cursor cursor = null;
		try {

			String[] selection = new String[] {	MAIN_ID	};
			
			String ids = "";
			for(int i = 0 ; i < id.length ; i++) {
				ids += MAIN_ID + " != " + id[i] + " and ";
			}
			cursor = queryTable(MAIN_TABLE,selection, ids + MDN + " = '" + mdn	+ "' and " + COMPANYCD + " = '" + companyCd	+ "'",null);

			if(cursor != null) {
				while (cursor.moveToNext()) {
					deleteReceiveTable(Integer.toString(cursor.getInt(cursor.getColumnIndex(MAIN_ID))));
				}
			}
		} catch(SQLException e) {
			e.printStackTrace();
		} finally {
			if(cursor != null) {
				cursor.close();
			}
		}
		
	}
	
	/**
	 * 메일함 SEQ 가져오기
	 * @param dataList
	 * @return
	 */
	public String getMainTableString(EmailMainListData dataList) {
		Cursor cursor = null;
		int value = 0;
		try {
			String[] selection = new String[] {	MAIN_ID	};
			if(dataList.getBoxType().equals("P")) {
				cursor = queryTable(MAIN_TABLE,selection ,	MDN 			+ " = '" + dataList.getMdn()			+ "' and " +
															COMPANYCD 		+ " = '" + dataList.getCompanyCd()		+ "' and " +
															BOXTYPE 		+ " = '" + EmailClientUtil.seedEncrypt(dataList.getBoxType())		+ "' and " +
															BOXID 			+ " = '" + EmailClientUtil.seedEncrypt(dataList.getBoxId())			+ "' and " +
															BOXCHANGKEY 	+ " = '" + EmailClientUtil.seedEncrypt(dataList.getBoxChangeKey())	+ "'",null);
			} else {
				cursor = queryTable(MAIN_TABLE,selection ,	MDN 			+ " = '" + dataList.getMdn()			+ "' and " +
															COMPANYCD 		+ " = '" + dataList.getCompanyCd()		+ "' and " +
															BOXTYPE 		+ " = '" + EmailClientUtil.seedEncrypt(dataList.getBoxType())		+ "'",null);
			}
			if(cursor != null && cursor.moveToFirst()) {
				
				value = cursor.getInt(cursor.getColumnIndex(MAIN_ID));
			} else {
				return Integer.toString(value);

			}
		} catch(SQLException e) {
			e.printStackTrace();
		} finally {
			if(cursor != null) {
				cursor.close();
			}
		}

		return Integer.toString(value);
	}

	/**
	 * 메일함 데이타 리스트로 가져오기
	 * @param dataList
	 * @return
	 */
	public EmailMainListData[] getMainTableData(EmailMainListData dataList) {
		Cursor cursor = null;
		EmailMainListData[] data = null;
		try {
			String[] selection = new String[] {
					MAIN_ID,MDN, COMPANYCD,BOXTYPE, PARENTBOXTYPE, BOXID,BOXCHANGKEY,BOXNAME,TOTALCNT,UPDATEDATE,BOXORDER,BOXLEVEL,BOXUNREADCOUNT
			};
			cursor = queryTable(MAIN_TABLE,selection ,	MDN 			+ " = '" + dataList.getMdn()			+ "' and " +
														COMPANYCD 		+ " = '" + dataList.getCompanyCd()		+ "' and " ,
														BOXORDER 		+ " desc");

			if(cursor != null) {
				int a = 0 ; 
				data = new EmailMainListData[cursor.getCount()];
				while (cursor.moveToNext()) {

					data[a] = new EmailMainListData();
					data[a].setId(cursor.getString(cursor.getColumnIndex(MAIN_ID)));
					data[a].setMdn(cursor.getString(cursor.getColumnIndex(MDN)));
					data[a].setCompanyCd(cursor.getString(cursor.getColumnIndex(COMPANYCD)));
					data[a].setBoxType(cursor.getString(cursor.getColumnIndex(BOXTYPE)));
					data[a].setParentBoxType(cursor.getString(cursor.getColumnIndex(PARENTBOXTYPE)));
					data[a].setBoxId(cursor.getString(cursor.getColumnIndex(BOXID)));
					data[a].setBoxChangeKey(cursor.getString(cursor.getColumnIndex(BOXCHANGKEY)));
					data[a].setBoxName(cursor.getString(cursor.getColumnIndex(BOXNAME)));
					data[a].setTotalCnt(cursor.getString(cursor.getColumnIndex(TOTALCNT)));
					data[a].setUpdatedate(cursor.getString(cursor.getColumnIndex(UPDATEDATE)));
					data[a].setBoxOrder(cursor.getString(cursor.getColumnIndex(BOXORDER)));
					data[a].setBoxLevel(cursor.getString(cursor.getColumnIndex(BOXLEVEL)));
					data[a].setUnreadCnt(cursor.getString(cursor.getColumnIndex(BOXUNREADCOUNT)));
					data[a].setDecryption();
					a++;

				}

			} else {
				return data;

			}
		} catch(SQLException e) {
			e.printStackTrace();
		} finally {
			if(cursor != null) {
				cursor.close();
			}
		}

		return data;
	}
	
	/**
	 * 메인 메일 편지함 리스트 가져오기
	 * @param mdn
	 * @param compayCd
	 * @return
	 */
	public EmailMainListData[] getMainTableData(String mdn , String compayCd ) {
		Cursor cursor = null;
		EmailMainListData[] data = null;
		try {
			String[] selection = new String[] {
					MAIN_ID,MDN, COMPANYCD,BOXTYPE, PARENTBOXTYPE, BOXID,BOXCHANGKEY,BOXNAME,TOTALCNT,UPDATEDATE,BOXORDER,BOXLEVEL,BOXUNREADCOUNT
			};
			cursor = queryTable(MAIN_TABLE,selection ,	MDN 			+ " = '" + mdn			+ "' and " +
														COMPANYCD 		+ " = '" + compayCd		+ "'" ,
														"CAST(" + BOXORDER + " AS UNSIGNED)" 		);

			if(cursor.getCount() > 0) {
				
				int a = 0 ; 
				data = new EmailMainListData[4];

				if(cursor.moveToFirst()){								
						do{
							if(a < 4){
								Log.i("EmailDatabaseHelper", a+". box name : "+cursor.getString(cursor.getColumnIndex(BOXNAME)));
									data[a] = new EmailMainListData();
									data[a].setId(cursor.getString(cursor.getColumnIndex(MAIN_ID)));
									data[a].setMdn(cursor.getString(cursor.getColumnIndex(MDN)));
									data[a].setCompanyCd(cursor.getString(cursor.getColumnIndex(COMPANYCD)));
									data[a].setBoxType(cursor.getString(cursor.getColumnIndex(BOXTYPE)));
									data[a].setParentBoxType(cursor.getString(cursor.getColumnIndex(PARENTBOXTYPE)));
									data[a].setBoxId(cursor.getString(cursor.getColumnIndex(BOXID)));
									data[a].setBoxChangeKey(cursor.getString(cursor.getColumnIndex(BOXCHANGKEY)));
									data[a].setBoxName(cursor.getString(cursor.getColumnIndex(BOXNAME)));
									data[a].setTotalCnt(cursor.getString(cursor.getColumnIndex(TOTALCNT)));
									data[a].setUpdatedate(cursor.getString(cursor.getColumnIndex(UPDATEDATE)));
									data[a].setBoxOrder(cursor.getString(cursor.getColumnIndex(BOXORDER)));
									data[a].setBoxLevel(cursor.getString(cursor.getColumnIndex(BOXLEVEL)));
									data[a].setUnreadCnt(cursor.getString(cursor.getColumnIndex(BOXUNREADCOUNT)));
									data[a].setDecryption();
									a++;
							}
						}while(cursor.moveToNext());
				}
			} else {
				return data;

			}
		} catch(SQLException e) {
			e.printStackTrace();
		} finally {
			if(cursor != null) {
				cursor.close();
			}
		}
		return data;
	}
	/**
	 * 메인 메일함 문서함 리스트 가져오기
	 * @param mdn
	 * @param compayCd
	 * @return
	 */	
	
	public EmailMainListData[] getMainTableDocData(String mdn , String compayCd ) {
		Cursor cursor = null;
		EmailMainListData[] data = null;
		try {
			String[] selection = new String[] {
					MAIN_ID,MDN, COMPANYCD,BOXTYPE, PARENTBOXTYPE, BOXID,BOXCHANGKEY,BOXNAME,TOTALCNT,UPDATEDATE,BOXORDER,BOXLEVEL,BOXUNREADCOUNT
			};
			cursor = queryTable(MAIN_TABLE,selection ,	MDN 			+ " = '" + mdn			+ "' and " +
					COMPANYCD 		+ " = '" + compayCd		+ "'" ,
					"CAST(" + BOXORDER + " AS UNSIGNED)" 		);
			

			if(cursor.getCount() > 4) {
				int a = 0 ; 
				data = new EmailMainListData[cursor.getCount()-4];
				
				if(cursor.moveToPosition(4)){	
					do {
						data[a] = new EmailMainListData();
						data[a].setId(cursor.getString(cursor.getColumnIndex(MAIN_ID)));
						data[a].setMdn(cursor.getString(cursor.getColumnIndex(MDN)));
						data[a].setCompanyCd(cursor.getString(cursor.getColumnIndex(COMPANYCD)));
						data[a].setBoxType(cursor.getString(cursor.getColumnIndex(BOXTYPE)));
						data[a].setParentBoxType(cursor.getString(cursor.getColumnIndex(PARENTBOXTYPE)));
						data[a].setBoxId(cursor.getString(cursor.getColumnIndex(BOXID)));
						data[a].setBoxChangeKey(cursor.getString(cursor.getColumnIndex(BOXCHANGKEY)));
						data[a].setBoxName(cursor.getString(cursor.getColumnIndex(BOXNAME)));
						data[a].setTotalCnt(cursor.getString(cursor.getColumnIndex(TOTALCNT)));
						data[a].setUpdatedate(cursor.getString(cursor.getColumnIndex(UPDATEDATE)));
						data[a].setBoxOrder(cursor.getString(cursor.getColumnIndex(BOXORDER)));
						data[a].setBoxLevel(cursor.getString(cursor.getColumnIndex(BOXLEVEL)));
						data[a].setUnreadCnt(cursor.getString(cursor.getColumnIndex(BOXUNREADCOUNT)));
						data[a].setDecryption();
						a++;
					}while (cursor.moveToNext());
				}
			} else {
				return data;
			}
		} catch(SQLException e) {
			e.printStackTrace();
		} finally {
			if(cursor != null) {
				cursor.close();
			}
		}
		return data;
	}

	/**
	 * 메일리스트 테이블 SEQ 가져오기
	 * @param mdn
	 * @param compayCd
	 * @param boxType
	 * @return
	 */
	public String getMainTableString(String mdn , String compayCd , String boxType) {
		
		Cursor cursor = null;
		int value = 0;
		try {

			String[] selection = new String[] {	MAIN_ID	};
			cursor = queryTable(MAIN_TABLE,selection ,
					MDN 			+ " = '" + mdn			+ "' and " +
					COMPANYCD 		+ " = '" + compayCd		+ "' and " +
					BOXTYPE 		+ " = '" + boxType		+ "'",null);

			if(cursor != null && cursor.moveToFirst()) {
				value = cursor.getInt(cursor.getColumnIndex(MAIN_ID));

			} else {
				return Integer.toString(value);

			}
		} catch(SQLException e) {
			e.printStackTrace();
		} finally {
			if(cursor != null) {
				cursor.close();
			}
		}

		return Integer.toString(value);
	}
	
	/**
	 * 메인테이블 데이타 가져오기
	 * @param mdn
	 * @param compayCd
	 * @param boxType
	 * @param boxId
	 * @param boxChangeKey
	 * @return
	 */
	public EmailMainListData getMainTable(String mdn , String compayCd , String boxType , String boxId , String boxChangeKey) {
		
		Cursor cursor = null;
		EmailMainListData data = null;
		try {
			String[] selection = new String[] {
					MAIN_ID,MDN, COMPANYCD,BOXTYPE, PARENTBOXTYPE, BOXID,BOXCHANGKEY,BOXNAME,TOTALCNT,UPDATEDATE,BOXORDER,BOXLEVEL,BOXUNREADCOUNT
			};
			if(boxType.equals("P")) {
				cursor = queryTable(MAIN_TABLE,selection ,
						MDN 			+ " = '" + mdn			+ "' and " +
						COMPANYCD 		+ " = '" + compayCd		+ "' and " +
						BOXID 			+ " = '" + boxId		+ "' and " +
						BOXCHANGKEY 	+ " = '" + boxChangeKey	+ "' and " +
						BOXTYPE 		+ " = '" + boxType		+ "'",null);
			} else {
				cursor = queryTable(MAIN_TABLE,selection ,
						MDN 			+ " = '" + mdn			+ "' and " +
						COMPANYCD 		+ " = '" + compayCd		+ "' and " +
						BOXTYPE 		+ " = '" + boxType		+ "'",null);
			}
			
			if(cursor != null && cursor.moveToFirst()) {
				data = new EmailMainListData();
				data.setId(cursor.getString(cursor.getColumnIndex(MAIN_ID)));
				data.setMdn(cursor.getString(cursor.getColumnIndex(MDN)));
				data.setCompanyCd(cursor.getString(cursor.getColumnIndex(COMPANYCD)));
				data.setBoxType(cursor.getString(cursor.getColumnIndex(BOXTYPE)));
				data.setParentBoxType(cursor.getString(cursor.getColumnIndex(PARENTBOXTYPE)));
				data.setBoxId(cursor.getString(cursor.getColumnIndex(BOXID)));
				data.setBoxChangeKey(cursor.getString(cursor.getColumnIndex(BOXCHANGKEY)));
				data.setBoxName(cursor.getString(cursor.getColumnIndex(BOXNAME)));
				data.setTotalCnt(cursor.getString(cursor.getColumnIndex(TOTALCNT)));
				data.setUpdatedate(cursor.getString(cursor.getColumnIndex(UPDATEDATE)));
				data.setBoxOrder(cursor.getString(cursor.getColumnIndex(BOXORDER)));
				data.setBoxLevel(cursor.getString(cursor.getColumnIndex(BOXLEVEL)));
				data.setUnreadCnt(cursor.getString(cursor.getColumnIndex(BOXUNREADCOUNT)));
				data.setDecryption();

			}
		} catch(SQLException e) {
			e.printStackTrace();
		} finally {
			if(cursor != null) {
				cursor.close();
			}
		}
		return data;
	}
	
	/**
	 * 메일함 테이블 데이타 수정
	 * @param value
	 * @param data
	 * @return
	 */
	private int updateTable(String value  , EmailMainListData data) {
		int ret = 0;

		try {
			ContentValues values = new ContentValues();
			data.setEncryption();
			values.put(BOXID 			, data.getBoxId());
			values.put(BOXCHANGKEY 		, data.getBoxChangeKey());
			values.put(BOXNAME 			, data.getBoxName());
			values.put(TOTALCNT 		, data.getTotalCnt());
			values.put(BOXORDER 		, data.getBoxOrder());
			values.put(BOXLEVEL 		, data.getBoxLevel());
			values.put(BOXUNREADCOUNT	, data.getUnreadCnt());

			ret = getWritableDatabase().update(MAIN_TABLE, values,MAIN_ID + " = '" + value + "'" , null);

		} catch(SQLException e) {
			e.printStackTrace();
		} finally {
		}

		return ret;
	}
	
	/**
	 * SEQ로 테이블 수정
	 * @param id
	 * @param key
	 * @param value
	 * @return
	 */
	public int updateTable(String id , String[] key , String[] value) {
		int ret = 0;

		lock();
		try {
			ContentValues values = new ContentValues();
			for(int a = 0 ; a < key.length ; a ++ ) {
				values.put(key[a] , EmailClientUtil.seedEncrypt(value[a]));
			}

			ret = getWritableDatabase().update(MAIN_TABLE, values,MAIN_ID + " = " + id  , null);


		} catch(SQLException e) {
			e.printStackTrace();
		} finally {
			unlock();
		}


		return ret;
	}
	
	/**
	 * 메일리스트 테이블 수정
	 * @param id
	 * @param mailId
	 * @param mailChangeKey
	 * @param key
	 * @param value
	 * @return
	 */
	public int updateReceiveTable(String id , String mailId , String mailChangeKey , String[] key , String[] value) {
		int ret = 0;
		String tableName = MAIL_LIST_TABLE + id	;
		lock();
		try {
			ContentValues values = new ContentValues();
			for(int a = 0 ; a < key.length ; a ++ ) {
				
				values.put(key[a] , EmailClientUtil.seedEncrypt(value[a]));
			}

			ret = getWritableDatabase().update(tableName, values,MAILID + " = '" + EmailClientUtil.seedEncrypt(mailId) +"' and "+MAILCHANGEKEY + " = '" + EmailClientUtil.seedEncrypt(mailChangeKey) + "'"  , null);


		} catch(SQLException e) {
			e.printStackTrace();
		} finally {
			unlock();
		}


		return ret;
	}

	/**
	 * 메일리스트 테이블 insert
	 * @param emailData
	 * @param _id
	 * @param thread
	 * @return
	 * @throws SKTException
	 */
	public long insertReceiveTable(ArrayList<EmailReceiveListData> emailData, String _id, EmailReceiveThread thread) throws SKTException {

		long ret = 0;
		lock();
		getWritableDatabase().beginTransaction();
		try {
			if(emailData == null || emailData.size() <= 0 ) {
			} else {
				String tableName = MAIL_LIST_TABLE+_id;
				String sql = "";
				sql = "insert into " + tableName + " ( " 
				+ MAILID + ","
				+ MAILCHANGEKEY + ","
				+ MAILSUBJECT + ","
				+ HASATTACHMENTS + ","
				+ RECEIVEDATE + ","
				+ SENDDATE + ","
				+ ISREAD + ","
				+ FROMINFO + ","
				+ TOLIST + ","
				+ MAILTYPE + ","
				+ TOTALCNT
				+ " ) values (?,?,?,?,?,?,?,?,?,?,?)";
				
				if(thread.stopFlag)
					throw new SKTException("xxxxxxxxxxx a1");

				SQLiteStatement insert = getWritableDatabase().compileStatement(sql);

				if(thread.stopFlag)
					throw new SKTException("xxxxxxxxxxx a2");
				
				for(EmailReceiveListData data : emailData) {
					data.setEncryption();
					insert.bindString(1, StringUtil.isNull(data.getMailId()) ? "" : data.getMailId());
					insert.bindString(2, StringUtil.isNull(data.getMailChangeKey())? "" : data.getMailChangeKey() );
					insert.bindString(3, StringUtil.isNull(data.getMailSubject())? "" : data.getMailSubject() );
					insert.bindString(4, StringUtil.isNull(data.getHasAttachments())? "" : data.getHasAttachments() ); 
					insert.bindString(5, StringUtil.isNull(data.getReceivedDate())? "" : data.getReceivedDate() ); 
					insert.bindString(6, StringUtil.isNull(data.getSendDate())? "" : data.getSendDate() ); 
					insert.bindString(7, StringUtil.isNull(data.getIsRead())? "" : data.getIsRead() );
					insert.bindString(8, StringUtil.isNull(data.getFromInfo())? "" : data.getFromInfo() );
					insert.bindString(9, StringUtil.isNull(data.getToList())? "" : data.getToList() );
					insert.bindString(10, StringUtil.isNull(data.getMailType()) ? "" : data.getMailType());
					insert.bindString(11, StringUtil.isNull(data.getTotalCnt()) ? "" : data.getTotalCnt());
					
					if(thread.stopFlag)
						throw new SKTException("xxxxxxxxxxx a3");

					insert.executeInsert();
					
					if(thread.stopFlag)
						throw new SKTException("xxxxxxxxxxx a4");
				}
			}

			ContentValues values = new ContentValues();
			values.put("UPDATE_DATE", EmailClientUtil.seedEncrypt(getNowDate()));
			
			if(thread.stopFlag)
				throw new SKTException("xxxxxxxxxxx a5");
			
			getWritableDatabase().update(MAIN_TABLE, values,MAIN_ID + " = " + _id  , null);
			
			if(thread.stopFlag)
				throw new SKTException("xxxxxxxxxxx a6");

			getWritableDatabase().setTransactionSuccessful();

		} catch(SQLException e) {
			throw new SKTException(e);
		} finally {
			getWritableDatabase().endTransaction();
			unlock();
		} 

		return ret;
	}

	/**
	 * 현재 시간 가져오기
	 * @return
	 */
	public static String getNowDate() {
		DateFormat dateFormat = new SimpleDateFormat("MM.dd a hh:mm");
        Date date = new Date();
        return dateFormat.format(date);
	}

	/**
	 * 리스트 테이블 insert
	 * @param emailData
	 * @param _id
	 * @param thread
	 * @return
	 * @throws SKTException
	 */
	public long selectInsertReceiveTable(ArrayList<EmailReceiveListData> emailData, String _id, EmailReceiveThread thread) throws SKTException {

		long ret = 0;
		lock();
		getWritableDatabase().beginTransaction();
		try {
			if(emailData == null || emailData.size() <= 0 ) {
			} else {
				String tableName = MAIL_LIST_TABLE+_id;
				String sql = "";
				sql = "insert into " + tableName + " ( " 
				+ MAILID + ","
				+ MAILCHANGEKEY + ","
				+ MAILSUBJECT + ","
				+ HASATTACHMENTS + ","
				+ RECEIVEDATE + ","
				+ SENDDATE + ","
				+ ISREAD + ","
				+ FROMINFO + ","
				+ TOLIST + ","
				+ MAILTYPE + ","
				+ TOTALCNT
				+ " ) values (?,?,?,?,?,?,?,?,?,?,?)";

				if(thread.stopFlag)
					throw new SKTException("xxxxxxxxxxx b1");

				SQLiteStatement insert = getWritableDatabase().compileStatement(sql);

				if(thread.stopFlag)
					throw new SKTException("xxxxxxxxxxx b2");
				
				for(EmailReceiveListData data : emailData) {
					if(thread.stopFlag)
						throw new SKTException("xxxxxxxxxxx b3");

					EmailReceiveListData oldData = selectReceiveTable(_id,data.getMdn(),data.getCompanyCd(),data.getMailId(),data.getMailChangeKey());
					
					if(thread.stopFlag)
						throw new SKTException("xxxxxxxxxxx b4");

					if(oldData != null) {
						continue;
					}

					data.setEncryption();
					insert.bindString(1, StringUtil.isNull(data.getMailId()) ? "" : data.getMailId());
					insert.bindString(2, StringUtil.isNull(data.getMailChangeKey())? "" : data.getMailChangeKey() );
					insert.bindString(3, StringUtil.isNull(data.getMailSubject())? "" : data.getMailSubject() );
					insert.bindString(4, StringUtil.isNull(data.getHasAttachments())? "" : data.getHasAttachments() ); 
					insert.bindString(5, StringUtil.isNull(data.getReceivedDate())? "" : data.getReceivedDate() ); 
					insert.bindString(6, StringUtil.isNull(data.getSendDate())? "" : data.getSendDate() ); 
					insert.bindString(7, StringUtil.isNull(data.getIsRead())? "" : data.getIsRead() );
					insert.bindString(8, StringUtil.isNull(data.getFromInfo())? "" : data.getFromInfo() );
					insert.bindString(9, StringUtil.isNull(data.getToList())? "" : data.getToList() );
					insert.bindString(10, StringUtil.isNull(data.getMailType()) ? "" : data.getMailType());
					insert.bindString(11, StringUtil.isNull(data.getTotalCnt()) ? "" : data.getTotalCnt());
					
					if(thread.stopFlag)
						throw new SKTException("xxxxxxxxxxx b5");

					insert.executeInsert();
					
					if(thread.stopFlag)
						throw new SKTException("xxxxxxxxxxx b6");
				}
			}
			
			ContentValues values = new ContentValues();
			values.put("UPDATE_DATE", EmailClientUtil.seedEncrypt(getNowDate()));
			
			if(thread.stopFlag)
				throw new SKTException("xxxxxxxxxxx b7");
			
			getWritableDatabase().update(MAIN_TABLE, values,MAIN_ID + " = " + _id  , null);
			
			if(thread.stopFlag)
				throw new SKTException("xxxxxxxxxxx b8");

			getWritableDatabase().setTransactionSuccessful();

		} catch(SQLException e) {
			throw new SKTException(e);
		} finally {
			getWritableDatabase().endTransaction();
			unlock();
		} 

		return ret;
	}

	/**
	 * 메일리스트 테이블 삭제하고 넣기
	 * @param emailData
	 * @param _id
	 * @param thread
	 * @return
	 * @throws SKTException
	 */
	public long deleteInsertReceiveTable(ArrayList<EmailReceiveListData> emailData, String _id, EmailReceiveThread thread) throws SKTException {
		long ret = 0;
		lock();
		getWritableDatabase().beginTransaction();
		
		String tableName = MAIL_LIST_TABLE+_id;

		try {
			if(emailData == null || emailData.size() <= 0 ) {
				if(thread.stopFlag)
					throw new SKTException("xxxxxxxxxxx c11");

				@SuppressWarnings("unused")
				int del =+ getWritableDatabase().delete(tableName,null, null);
				
				if(thread.stopFlag)
					throw new SKTException("xxxxxxxxxxx c22");
			} else {
				String sql = "";
				sql = "insert into " + tableName + " ( " 
				+ MAILID + ","
				+ MAILCHANGEKEY + ","
				+ MAILSUBJECT + ","
				+ HASATTACHMENTS + ","
				+ RECEIVEDATE + ","
				+ SENDDATE + ","
				+ ISREAD + ","
				+ FROMINFO + ","
				+ TOLIST + ","
				+ MAILTYPE + ","
				+ TOTALCNT
				+ " ) values (?,?,?,?,?,?,?,?,?,?,?)";
				
				if(thread.stopFlag)
					throw new SKTException("xxxxxxxxxxx c1");
				
				SQLiteStatement insert = getWritableDatabase().compileStatement(sql);

				if(thread.stopFlag)
					throw new SKTException("xxxxxxxxxxx c2");

				@SuppressWarnings("unused")
				int del =+ getWritableDatabase().delete(tableName,null, null);
				
				if(thread.stopFlag)
					throw new SKTException("xxxxxxxxxxx c3");

				for(EmailReceiveListData data : emailData) {
					data.setEncryption();
					insert.bindString(1, StringUtil.isNull(data.getMailId()) ? "" : data.getMailId());
					insert.bindString(2, StringUtil.isNull(data.getMailChangeKey())? "" : data.getMailChangeKey() );
					insert.bindString(3, StringUtil.isNull(data.getMailSubject())? "" : data.getMailSubject() );
					insert.bindString(4, StringUtil.isNull(data.getHasAttachments())? "" : data.getHasAttachments() ); 
					insert.bindString(5, StringUtil.isNull(data.getReceivedDate())? "" : data.getReceivedDate() ); 
					insert.bindString(6, StringUtil.isNull(data.getSendDate())? "" : data.getSendDate() ); 
					insert.bindString(7, StringUtil.isNull(data.getIsRead())? "" : data.getIsRead() );
					insert.bindString(8, StringUtil.isNull(data.getFromInfo())? "" : data.getFromInfo() );
					insert.bindString(9, StringUtil.isNull(data.getToList())? "" : data.getToList() );
					insert.bindString(10, StringUtil.isNull(data.getMailType()) ? "" :  data.getMailType());
					insert.bindString(11, StringUtil.isNull(data.getTotalCnt()) ? "" : data.getTotalCnt());
					
					if(thread.stopFlag)
						throw new SKTException("xxxxxxxxxxx c4");

					insert.executeInsert();
					
					if(thread.stopFlag)
						throw new SKTException("xxxxxxxxxxx c5");
				}
			}
			
			ContentValues values = new ContentValues();
			values.put("UPDATE_DATE", EmailClientUtil.seedEncrypt(getNowDate()));
			
			if(thread.stopFlag)
				throw new SKTException("xxxxxxxxxxx c6");
			
			getWritableDatabase().update(MAIN_TABLE, values,MAIN_ID + " = " + _id  , null);
			
			if(thread.stopFlag)
				throw new SKTException("xxxxxxxxxxx c7");

			getWritableDatabase().setTransactionSuccessful();

		} catch(SQLException e) {
			throw new SKTException(e);
		} finally {
			getWritableDatabase().endTransaction();
			unlock();
		} 

		return ret;
	}

	/**
	 * 메일 리스트 select
	 * @param _id
	 * @param mdn
	 * @param companyCd
	 * @param mailId
	 * @param mailChangeKey
	 * @return
	 */
	public EmailReceiveListData selectReceiveTable(String _id , String mdn , String companyCd, String mailId, String mailChangeKey) {
		Cursor cursor = null;
		String tableName = MAIL_LIST_TABLE+_id;
		EmailReceiveListData data = null;
		mSQLiteDatabase = getReadableDatabase();
		try {
			cursor = mSQLiteDatabase.rawQuery("select "	+ MAIN_TABLE + "." + MDN + ","
														+ MAIN_TABLE + "." + COMPANYCD + ","
														+ MAIN_TABLE + "." + BOXTYPE + ","
														+ MAIN_TABLE + "." + PARENTBOXTYPE + ","
														+ MAIN_TABLE + "." + BOXID + ","
														+ MAIN_TABLE + "." + BOXCHANGKEY + ","
														+ MAIN_TABLE + "." + BOXNAME + ","
														+ MAIN_TABLE + "." + UPDATEDATE + ","
														+ tableName  + ".*" + 
														" from " + MAIN_TABLE + "," + tableName + 
														" where " + MAIN_TABLE + "." + MAIN_ID + " = '" + _id +
														"' and " + MAIN_TABLE + "." + MDN + " = '" + mdn +
														"' and " + MAIN_TABLE + "." + COMPANYCD + " = '" + companyCd +
														"' and " + tableName + "." + MAILID + " = '" + mailId +
														"' and " + tableName + "." + MAILCHANGEKEY + " = '" + mailChangeKey +"'", null);

			if(cursor != null && cursor.moveToFirst()) {
				data = new EmailReceiveListData();
				data.setMdn(cursor.getString(cursor.getColumnIndex(MDN)));
				data.setCompanyCd(cursor.getString(cursor.getColumnIndex(COMPANYCD)));
				data.setBoxType(cursor.getString(cursor.getColumnIndex(BOXTYPE)));
				data.setParentBoxType(cursor.getString(cursor.getColumnIndex(PARENTBOXTYPE)));
				data.setBoxId(cursor.getString(cursor.getColumnIndex(BOXID)));
				data.setBoxChangKey(cursor.getString(cursor.getColumnIndex(BOXCHANGKEY)));
				data.setBoxNmae(cursor.getString(cursor.getColumnIndex(BOXNAME)));
				data.setTotalCnt(cursor.getString(cursor.getColumnIndex(TOTALCNT)));
				data.setUpdateDate(cursor.getString(cursor.getColumnIndex(UPDATEDATE)));
				data.setMailId(cursor.getString(cursor.getColumnIndex(MAILID)));
				data.setMailChangeKey(cursor.getString(cursor.getColumnIndex(MAILCHANGEKEY)));
				data.setMailSubject(cursor.getString(cursor.getColumnIndex(MAILSUBJECT)));
				data.setHasAttachments(cursor.getString(cursor.getColumnIndex(HASATTACHMENTS)));
				data.setReceivedDate(cursor.getString(cursor.getColumnIndex(RECEIVEDATE)));
				data.setSendDate(cursor.getString(cursor.getColumnIndex(SENDDATE)));
				data.setIsRead(cursor.getString(cursor.getColumnIndex(ISREAD)));
				data.setFromInfo(cursor.getString(cursor.getColumnIndex(FROMINFO)));
				data.setToList(cursor.getString(cursor.getColumnIndex(TOLIST)));
				data.setMailType(cursor.getString(cursor.getColumnIndex(MAILTYPE)));
				data.setDecryption();
			}

		} catch(SQLException e) {
			e.printStackTrace();
			return data;
		} finally {
			if(cursor != null) {
				cursor.close();
			}
		}

		return data;
		
	}
	
	/**
	 * SEQ로 메일리스트 select
	 * @param _id
	 * @return
	 */
	public ArrayList<EmailReceiveListData> selectReceiveTable(String _id) {
		Cursor cursor = null;
		
		String tableName = MAIL_LIST_TABLE+_id;
		ArrayList<EmailReceiveListData> list = new ArrayList<EmailReceiveListData>();
		readLock();
		mSQLiteDatabase = getReadableDatabase();
		try {
			cursor = mSQLiteDatabase.rawQuery("select "	+ MAIN_TABLE + "." + MDN + ","
														+ MAIN_TABLE + "." + COMPANYCD + ","
														+ MAIN_TABLE + "." + BOXTYPE + ","
														+ MAIN_TABLE + "." + PARENTBOXTYPE + ","
														+ MAIN_TABLE + "." + BOXID + ","
														+ MAIN_TABLE + "." + BOXCHANGKEY + ","
														+ MAIN_TABLE + "." + BOXNAME + ","
														+ MAIN_TABLE + "." + UPDATEDATE + ","
														+ tableName  + ".*" + 
														" from " + MAIN_TABLE + "," + tableName + 
														" where " + MAIN_TABLE + "." + MAIN_ID + " = '" + _id + "' order by "+tableName+"."+RECEIVEDATE+" desc", null);

			if(cursor != null) {
				while(cursor.moveToNext()) {
					EmailReceiveListData data = new EmailReceiveListData();

					data.setMdn(cursor.getString(cursor.getColumnIndex(MDN)));
					data.setCompanyCd(cursor.getString(cursor.getColumnIndex(COMPANYCD)));
					data.setBoxType(cursor.getString(cursor.getColumnIndex(BOXTYPE)));
					data.setParentBoxType(cursor.getString(cursor.getColumnIndex(PARENTBOXTYPE)));
					data.setBoxId(cursor.getString(cursor.getColumnIndex(BOXID)));
					data.setBoxChangKey(cursor.getString(cursor.getColumnIndex(BOXCHANGKEY)));
					data.setBoxNmae(cursor.getString(cursor.getColumnIndex(BOXNAME)));
					data.setTotalCnt(cursor.getString(cursor.getColumnIndex(TOTALCNT)));
					data.setUpdateDate(cursor.getString(cursor.getColumnIndex(UPDATEDATE)));
					data.setMailId(cursor.getString(cursor.getColumnIndex(MAILID)));
					data.setMailChangeKey(cursor.getString(cursor.getColumnIndex(MAILCHANGEKEY)));
					data.setMailSubject(cursor.getString(cursor.getColumnIndex(MAILSUBJECT)));
					data.setHasAttachments(cursor.getString(cursor.getColumnIndex(HASATTACHMENTS)));
					data.setReceivedDate(cursor.getString(cursor.getColumnIndex(RECEIVEDATE)));
					data.setSendDate(cursor.getString(cursor.getColumnIndex(SENDDATE)));
					data.setIsRead(cursor.getString(cursor.getColumnIndex(ISREAD)));
					data.setFromInfo(cursor.getString(cursor.getColumnIndex(FROMINFO)));
					data.setToList(cursor.getString(cursor.getColumnIndex(TOLIST)));
					data.setMailType(cursor.getString(cursor.getColumnIndex(MAILTYPE)));
					data.setDecryption();
					list.add(data);
				}
			}

		} catch(SQLException e) {
			e.printStackTrace();
		} finally {
			if(cursor != null) {
				cursor.close();
			}
			unReadLock();
		}

		return list;
	}

	/**
	 * 메일리스트 테이블  페이지별로 가져오기
	 * @param _id
	 * @param page
	 * @param count
	 * @return
	 */
	public ArrayList<EmailReceiveListData> selectReceiveTable(String _id, int page , int count) {
		Cursor cursor = null;
		
		String tableName = MAIL_LIST_TABLE+_id;
		ArrayList<EmailReceiveListData> list = new ArrayList<EmailReceiveListData>();
		readLock();
		mSQLiteDatabase = getReadableDatabase();
		try {
			
			cursor = mSQLiteDatabase.rawQuery("select "	+ MAIN_TABLE + "." + MDN + ","
														+ MAIN_TABLE + "." + COMPANYCD + ","
														+ MAIN_TABLE + "." + BOXTYPE + ","
														+ MAIN_TABLE + "." + PARENTBOXTYPE + ","
														+ MAIN_TABLE + "." + BOXID + ","
														+ MAIN_TABLE + "." + BOXCHANGKEY + ","
														+ MAIN_TABLE + "." + BOXNAME + ","
														+ MAIN_TABLE + "." + UPDATEDATE + ","
														+ tableName  + ".*" + 
														" from " + MAIN_TABLE + "," + tableName + 
														" where " + MAIN_TABLE + "." + MAIN_ID + " = '" + _id + "' order by "+tableName+"."+RECEIVEDATE+" desc limit " + Integer.toString((page-1)*20) + ", " + Integer.toString(count), null);

			if(cursor != null) {
				while(cursor.moveToNext()) {
					EmailReceiveListData data = new EmailReceiveListData();
					Log.i(TAG, "box name .......... "+cursor.getString(cursor.getColumnIndex(BOXNAME)));
					data.setMdn(cursor.getString(cursor.getColumnIndex(MDN)));
					data.setCompanyCd(cursor.getString(cursor.getColumnIndex(COMPANYCD)));
					data.setBoxType(cursor.getString(cursor.getColumnIndex(BOXTYPE)));
					data.setParentBoxType(cursor.getString(cursor.getColumnIndex(PARENTBOXTYPE)));
					data.setBoxId(cursor.getString(cursor.getColumnIndex(BOXID)));
					data.setBoxChangKey(cursor.getString(cursor.getColumnIndex(BOXCHANGKEY)));
					data.setBoxNmae(cursor.getString(cursor.getColumnIndex(BOXNAME)));
					data.setTotalCnt(cursor.getString(cursor.getColumnIndex(TOTALCNT)));
					data.setUpdateDate(cursor.getString(cursor.getColumnIndex(UPDATEDATE)));
					data.setMailId(cursor.getString(cursor.getColumnIndex(MAILID)));
					data.setMailChangeKey(cursor.getString(cursor.getColumnIndex(MAILCHANGEKEY)));
					data.setMailSubject(cursor.getString(cursor.getColumnIndex(MAILSUBJECT)));
					data.setHasAttachments(cursor.getString(cursor.getColumnIndex(HASATTACHMENTS)));
					data.setReceivedDate(cursor.getString(cursor.getColumnIndex(RECEIVEDATE)));
					data.setSendDate(cursor.getString(cursor.getColumnIndex(SENDDATE)));
					data.setIsRead(cursor.getString(cursor.getColumnIndex(ISREAD)));
					data.setFromInfo(cursor.getString(cursor.getColumnIndex(FROMINFO)));
					data.setToList(cursor.getString(cursor.getColumnIndex(TOLIST)));
					data.setMailType(cursor.getString(cursor.getColumnIndex(MAILTYPE)));
					data.setDecryption();
					list.add(data);
				}
			}

		} catch(SQLException e) {
			e.printStackTrace();
		} finally {
			if(cursor != null){
			cursor.close();
			}
			unReadLock();
		}

		return list;
	}

	/**
	 * 메일함 테이블 데이타 가져오기
	 * @param _id
	 * @return
	 */
	public EmailMainListData getMainTableData(String _id) {
		Cursor cursor = null;
		EmailMainListData data = new EmailMainListData();
		try {
			String[] selection = new String[] {
					MAIN_ID,MDN, COMPANYCD,BOXTYPE, PARENTBOXTYPE, BOXID,BOXCHANGKEY,BOXNAME,TOTALCNT,UPDATEDATE,BOXORDER,BOXLEVEL,BOXUNREADCOUNT
			};
			cursor = queryTable(MAIN_TABLE,selection , MAIN_ID + " = " + _id ,null);

			if(cursor != null && cursor.moveToFirst()) {
				data.setId(cursor.getString(cursor.getColumnIndex(MAIN_ID)));
				data.setMdn(cursor.getString(cursor.getColumnIndex(MDN)));
				data.setCompanyCd(cursor.getString(cursor.getColumnIndex(COMPANYCD)));
				data.setBoxType(cursor.getString(cursor.getColumnIndex(BOXTYPE)));
				data.setParentBoxType(cursor.getString(cursor.getColumnIndex(PARENTBOXTYPE)));
				data.setBoxId(cursor.getString(cursor.getColumnIndex(BOXID)));
				data.setBoxChangeKey(cursor.getString(cursor.getColumnIndex(BOXCHANGKEY)));
				data.setBoxName(cursor.getString(cursor.getColumnIndex(BOXNAME)));
				data.setTotalCnt(cursor.getString(cursor.getColumnIndex(TOTALCNT)));
				data.setUpdatedate(cursor.getString(cursor.getColumnIndex(UPDATEDATE)));
				data.setBoxOrder(cursor.getString(cursor.getColumnIndex(BOXORDER)));
				data.setBoxLevel(cursor.getString(cursor.getColumnIndex(BOXLEVEL)));
				data.setUnreadCnt(cursor.getString(cursor.getColumnIndex(BOXUNREADCOUNT)));
				data.setDecryption();
			} else {
				return data;

			}
		} catch(SQLException e) {
			e.printStackTrace();
		} finally {
			if(cursor != null) {
				cursor.close();
			}
		}

		return data;
		
	}
	
	/**
	 * 메일리스트 테이블 데이타 삭제
	 * @param id
	 * @param mailId
	 * @param mailChangeKey
	 * @return
	 */
	public int deleteReceiveTable(String id , String[] mailId , String[] mailChangeKey) {
		int ret = 0;
		String tableName = MAIL_LIST_TABLE+id;
		
		lock();
		try {
			getWritableDatabase().beginTransaction();
			for(int a = 0 ; a < mailId.length ; a ++ ) {
				ret += getWritableDatabase().delete(tableName,MAILID + " = '" + EmailClientUtil.seedEncrypt(mailId[a]) + "' and " + MAILCHANGEKEY + " = '" + EmailClientUtil.seedEncrypt(mailChangeKey[a]) + "'" , null);
			}
			getWritableDatabase().setTransactionSuccessful();
		} catch(SQLException e) {
			e.printStackTrace();
		}finally {
			getWritableDatabase().endTransaction();
			unlock();
		} 
		return ret;
	}
	
	/**
	 * 메일리스트 테이블 전부 삭제
	 * @param id
	 * @return
	 */
	public int deleteReceiveAll(String id) {
		int ret = 0;
		String tableName = MAIL_LIST_TABLE+id;
		
		lock();
		try {
			getWritableDatabase().beginTransaction();
			ret += getWritableDatabase().delete(tableName,null, null);
			getWritableDatabase().setTransactionSuccessful();
		} catch(SQLException e) {
			e.printStackTrace();
		}finally {
			getWritableDatabase().endTransaction();
			unlock();
		} 
		return ret;
	}
	
	/**
	 * 메일 update 테이블 넣기
	 * @param mdn
	 * @param companyCd
	 * @param update
	 * @return
	 */
	public long insertBoxUpdate(String mdn , String companyCd , String update) {
		
		long ret = 0;

		lock();
		try {

			getWritableDatabase().beginTransaction();
			String value = "";
			SQLiteStatement insert = null;
			String sql = "";
			sql = "insert into " + MAIN_UPDATE_TABLE 	+ " ( " 
			+ MDN 			+ ","
			+ COMPANYCD 	+ ","
			+ BOXUPDATE 		+ 
			" ) values (?,?,?)";

			value = getBoxUpdate( mdn ,  companyCd);
			update = EmailClientUtil.seedEncrypt(update);
			if(StringUtil.isNull(value)) {
				insert = getWritableDatabase().compileStatement(sql);

				insert.bindString(1, mdn);
				insert.bindString(2, companyCd);
				insert.bindString(3, update);
				insert.executeInsert();

			} else {
				setBoxUpdate(mdn, companyCd, update);
			}

			getWritableDatabase().setTransactionSuccessful();

		} catch(SQLException e) {
			e.printStackTrace();
		} finally {
			getWritableDatabase().endTransaction();
			unlock();
		} 

		return ret;
	}

	/**
	 * update 테이블 테이타 가져오기
	 * @param mdn
	 * @param companyCd
	 * @return
	 */
	public String getBoxUpdate(String mdn , String companyCd) {
		
		return getNowDate();
	}
	
	/**
	 * 메일함 테이블 업데이트
	 * @param mdn
	 * @param companyCd
	 * @param update
	 * @return
	 */
	private int setBoxUpdate(String mdn , String companyCd , String update) {
		
		int ret = 0;
		try {
			ContentValues values = new ContentValues();
			values.put(BOXUPDATE , EmailClientUtil.seedEncrypt(update));

			ret = getWritableDatabase().update(MAIN_UPDATE_TABLE, values,MDN + " = '" + mdn +"' and "+COMPANYCD + " = '" + companyCd + "'"  , null);
			
		} catch(SQLException e) {
			e.printStackTrace();
		} finally {
		}
		return ret;
	}

	/**
	 * 테이블 락 걸기
	 */
	private void lock() {
		lock.writeLock().lock();
	}
	
	/**
	 * 테이블 락 풀기
	 */
	private void unlock() {
		lock.writeLock().unlock();
	}

	/**
	 * 테이블 쓰기 락 걸기
	 */
	private void readLock() {
		lock.readLock().lock();
	}
	
	/**
	 * 테이블 쓰기 락 풀기
	 */
	private void unReadLock() {
		lock.readLock().unlock();
	}

}
