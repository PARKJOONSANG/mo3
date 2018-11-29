package com.ex.group.mail.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.skt.pe.crypto.seed.SeedCrypter;
import com.skt.pe.util.StringUtil;

import java.io.File;

import com.ex.group.folder.R;

/**
 * 상수 및 static 메소드 정의
 * @author sjsun5318
 *
 */
public class EmailClientUtil {

//	public final static String					COMMON_MAIL_LIST							= "COMMON_MAIL_LIST";
//	public final static String 					COMMON_MAIL_BOX							= "COMMON_MAIL_BOX";
//	public final static String					COMMON_MAIL_GETADDRESS 				= "COMMON_MAIL_GETADDRESS";
//	public final static String					COMMON_MAIL_DEL								= "COMMON_MAIL_DEL";
//	public final static String					COMMON_MAIL_ISREAD						= "COMMON_MAIL_ISREAD";
//	public final static String 					COMMON_MAIL_CONTENT					= "COMMON_MAIL_CONTENT";
//	public final static String 					COMMON_MAIL_SEND							= "COMMON_MAIL_SEND";
//	public final static String 					COMMON_MAIL_REPLY							= "COMMON_MAIL_REPLY";
//	public final static String 					COMMON_MAIL_FORWARD					= "COMMON_MAIL_FORWARD";
//	public final static String 					COMMON_ISS_RETRIEVE						= "COMMON_ISS_RETRIEVE";
//	public final static String					COMMON_MAIL_SAVE							= "COMMON_MAIL_SAVE";
//	public final static String					COMMON_MAIL_RESEND						= "COMMON_MAIL_RESEND";
//	public final static String 					COMMON_MAIL_GROUPCONTACTLIST	= "COMMON_MAIL_GROUPCONTACTLIST";
//	public final static String					COMMON_MAIL_DL								= "COMMON_MAIL_DL";
//	public final static String					COMMON_MAIL_CONTACTLIST				="COMMON_MAIL_CONTACTLIST";
	
	//2014-02-06 JSJ  primitive 변경
	public final static String COMMON_MAIL_LIST							= "COMMON_MAILNEW_LIST";
	public final static String COMMON_MAIL_BOX							= "COMMON_MAILNEW_BOX";
	public final static String COMMON_MAIL_GETADDRESS 				= "COMMON_MAILNEW_GETADDRESS";  //고도화에서도 사용
	public final static String COMMON_MAIL_GETUSERINFO 				= "COMMON_MAILADV_GETUSERINFO";  //겸직자 처리를 위해 추가 (2016.07.25)
//	public final static String					COMMON_MAIL_GETUSERINFO 				= "COMMON_MAILTEST_GETUSERINFO";  //겸직자 처리를 위해 추가 (2016.07.25)
	public final static String COMMON_MAIL_DEL								= "COMMON_MAILNEW_DEL";
	public final static String COMMON_MAIL_ISREAD						= "COMMON_MAILNEW_ISREAD";
	public final static String COMMON_MAIL_CONTENT					= "COMMON_MAILNEW_CONTENT";
	public final static String COMMON_MAIL_SEND							= "COMMON_MAILNEW_SEND";
	public final static String COMMON_MAIL_REPLY							= "COMMON_MAILNEW_REPLY";
	public final static String COMMON_MAIL_FORWARD					= "COMMON_MAILNEW_FORWARD";
	public final static String COMMON_ISS_RETRIEVE						= "COMMON_ISSNEW_RETRIEVE";
	public final static String COMMON_MAIL_SAVE							= "COMMON_MAILNEW_SAVE";
	public final static String COMMON_MAIL_RESEND						= "COMMON_MAILNEW_RESEND";
	public final static String COMMON_MAIL_GROUPCONTACTLIST	= "COMMON_MAILNEW_GROUPCONTACTLIST";
	public final static String COMMON_MAIL_DL								= "COMMON_MAILNEW_DL";
	public final static String COMMON_MAIL_CONTACTLIST				="COMMON_MAILNEW_CONTACTLIST";
	
	//2014-07-22 사내매일 고도화로 인해 변경
//	public final static String					COMMON_MAILADV_PRIVATEBOX		=	"COMMON_MAILADV_PRIVATEBOX";
//	public final static String					COMMON_MAILADV_LIST				=	"COMMON_MAILADV_LIST";
//	public final static String					COMMON_MAILADV_PRIVATELIST		=	"COMMON_MAILADV_PRIVATELIST";
//	public final static String 					COMMON_MAILADV_CONTENT			= 	"COMMON_MAILADV_CONTENT";
//	public final static String 					COMMON_MAILADV_SEND				=	"COMMON_MAILADV_SEND";
//	public final static String					COMMON_MAILADV_DEL				=	"COMMON_MAILADV_DEL";
	
	//2016-08-25  겸직자 기능 테스트
	public final static String COMMON_MAILADV_PRIVATEBOX		=	"COMMON_MAILTEST_PRIVATEBOX";
	public final static String COMMON_MAILADV_LIST				=	"COMMON_MAILTEST_LIST";
	public final static String COMMON_MAILADV_PRIVATELIST		=	"COMMON_MAILTEST_PRIVATELIST";
	public final static String COMMON_MAILADV_CONTENT			= 	"COMMON_MAILTEST_CONTENT";
	public final static String COMMON_MAILADV_SEND				=	"COMMON_MAILTEST_SEND";
	public final static String COMMON_MAILADV_DEL				=	"COMMON_MAILTEST_DEL";
	
	public final static String LANG						= "ko";
	public final static String PREF_EMAIL  				= "email";
	public final static String MEMBERSEARCH 				= "com.ex.group.addressbook";
	public final static String MEMBERSEARCH_MAIN 			= "com.ex.group.addressbook.activity.AddressTabActivity";
	
	public static String companyCd					= "";
	public static String id							= "";
	//2014-02-10 JSJ empNm 사용자명 추가, (emailAddress = > empNm) 기존에 사용자명을  emailAddress 에 저장 하엿음.
	public static String empNm      			= "";
	public static String emailAddress      			= "";
	public static String nedmsID      			= "";
	
	public static String emailAddress_id      		= "";
	public static String mdn							= "";
	
	public final static int						START_RUNABLE				= 1 ;
	public final static int						END_RUNABLE					= 2 ;
	public final static int						ADDRESS						= 3 ;
	public final static int						NO_TABLE_ERROR				= 4 ;
	public final static int						UESRINFO					= 5 ;
	public final static int						SKT_EXCEPTION				= 9 ;
	
	public final static String RECEIVE_I1_BOX_NAME      = "받은 편지함-미열람";
	public final static String RECEIVE_I2_BOX_NAME      = "받은 편지함-열람";
	public final static String SEND_BOX_NAME            = "보낸편지함";
	public final static String DELETE_BOX_NAME          = "지운편지함";
	
	public final static int						COUNTPERPAGE			 	= 15;
	
	public static boolean						mainUpdate					= false;
	//public static boolean						receiveUpdate				= false;
	private static SharedPreferences mPref 						= null;
	public static boolean						LOGMODE						= false;
	
	/**
	 * 값 NULL 체크
	 * @param value
	 * @return
	 */
	public static String setValue(String value) {
		if(StringUtil.isNull(value)) {
    		return "";
    	} else {
    		return value;
    	}
	}
	
	public static void createPreferences(Context context) {
		if(mPref == null) {
			mPref = context.getSharedPreferences(PREF_EMAIL, Context.MODE_PRIVATE);
			File f = new File("/data/data/" + context.getPackageName() + "/shared_prefs/");
			if(f!=null && f.exists()) {
				File[] ff = f.listFiles();
				if(ff != null && ff.length > 0) {
					for(File a : ff) {
						if(!a.getName().toLowerCase().equals(PREF_EMAIL + ".xml")) {
							a.delete();
						}
					}
				}
			}
		}
	}
	
	/**
	 * 기본 서명설정 가져오기
	 * @param context
	 * @param key
	 * @return
	 */
	public static String getMail(Context context, String id) {
		if(mPref == null) {
			createPreferences(context);
		}
		return mPref.getString(id + "_MAIL", "");
		
	}
	
	/**
	 * 기본 서명설정 셋팅하기
	 * @param context
	 * @param key
	 * @return
	 */
	public static void setMail(Context context, String id, String value) {
		if(mPref == null) {
			createPreferences(context);
		}
		Log.d("","setMail = " + id +" : " + value);
		mPref.edit().putString(id + "_MAIL", value).commit();
	}
	
	/**
	 * 기본 서명설정 가져오기
	 * @param context
	 * @param key
	 * @return
	 */
	public static String getSigne(Context context, String key) {
		if(mPref == null) {
			createPreferences(context);
		}
		mPref = context.getSharedPreferences(key, Context.MODE_PRIVATE);
		return mPref.getString(key , context.getResources().getString(R.string.mail_WRITEVIEW_SUMMARY_TEXT));
	}
	
	/**
	 * 기본 서명설정 셋팅하기
	 * @param context
	 * @param key
	 * @return
	 */
	public static void setSigne(Context context, String key, String value) {
		if(mPref == null) {
			createPreferences(context);
		}
		mPref = context.getSharedPreferences(key, Context.MODE_PRIVATE);
		mPref.edit().putString(key, value).commit();
	}
	
	/**
	 * 파일 다운로드일때 alert창 셋팅
	 * @param context
	 * @param companyCd
	 * @param mdn
	 * @param isYN
	 */
	@SuppressWarnings("static-access")
	public static void setFileView(Context context, String companyCd, String mdn, String isYN) {
		if(mPref == null) {
			createPreferences(context);
		}
		mPref = context.getSharedPreferences(companyCd+mdn+"PREF_TOFFICE_EMAIL", context.MODE_PRIVATE);
		mPref.edit().putString(companyCd+mdn+"PREF_TOFFICE_EMAIL", isYN).commit();
		
	}
	
	/**
	 * 파일 다운로드일때 alert창 값 가져오기
	 * @param context
	 * @param companyCd
	 * @param mdn
	 * @param isYN
	 */
	@SuppressWarnings("static-access")
	public static String getFileView(Context context, String companyCd, String mdn) {
		if(mPref == null) {
			createPreferences(context);
		}
		mPref = context.getSharedPreferences(companyCd+mdn+"PREF_TOFFICE_EMAIL", context.MODE_PRIVATE);
		return mPref.getString(companyCd+mdn+"PREF_TOFFICE_EMAIL", "N");
		
	}
	
	/**
	 * 키패드 내리기
	 * @param a_oView
	 * @return
	 */
	public static boolean hideSoftInputWindow(View a_oView) {
		InputMethodManager imm = (InputMethodManager) a_oView.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
		return imm.hideSoftInputFromWindow(a_oView.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
	}
	
	public static void logPrint(String logTitle, String logText) {
		if(LOGMODE) {
			//Log.d(logTitle, logText + "");
		}
	}
	
	public static String seedEncrypt(String plain) {
		if(StringUtil.isNull(plain)) {
			return "";
		}
		Log.d("xxxxxxxxxxx", "sj   seedEncrypt  " + plain);
		return SeedCrypter.encrypt(plain);
	}
	
	public static String seedDecrypt(String cipher) {
		Log.d("xxxxxxxxxxx", "sj   seedDecrypt  ");
		if(StringUtil.isNull(cipher)) {
			return "";
		}
		Log.d("xxxxxxxxxxx", "sj   seedDecrypt  " + cipher);
		return SeedCrypter.decrypt(cipher);
	}
	
	public static String getMailFolderKind(String boxType){
		String returnValue = "";
		
    	if("I1".equals(boxType)) returnValue = "receive";
    	if("I2".equals(boxType)) returnValue = "receive1";
    	if("S".equals(boxType)) returnValue = "sent"; //send=>sent 변경됨.
    	if("T".equals(boxType)) returnValue = "temp";
    	if("D".equals(boxType)) returnValue = "trash";
    	if("P".equals(boxType)) returnValue = "private";
    	
		return returnValue;
	}
	
	public static String getNameString(String name){
		String returnValue = "";
		String chkValue = "";
		if(name==null) return "";
		
		if(name.indexOf("/")>-1){
			chkValue = name.substring(0, name.indexOf("/"));			
		}else{
			chkValue = name;			
		}
		
		int leftTag  = chkValue.indexOf("<");
		int rightTag = chkValue.indexOf(">");		
		if(leftTag>-1 && rightTag>-1 && leftTag < rightTag){
			if(name.indexOf("외(")>-1){
				String nameCnt = name.substring(name.indexOf("외("),rightTag);
				returnValue = chkValue + nameCnt;
			}else{
				returnValue = chkValue.substring(0,leftTag); //returnValue = chkValue;				
			}
			//returnValue = chkValue.substring(0,leftTag);
		}else{
			returnValue = chkValue;			
		}		
		
		return returnValue;		
	}
}
