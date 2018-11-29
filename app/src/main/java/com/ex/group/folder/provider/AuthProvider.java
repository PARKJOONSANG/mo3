package com.ex.group.folder.provider;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.skt.pe.common.conf.Constants;
import com.skt.pe.common.data.Auth;
import com.skt.pe.common.data.AuthData;
import com.skt.pe.common.data.AuthJob;
import com.skt.pe.common.data.SKTUtil;
import com.skt.pe.common.exception.SKTException;
import com.skt.pe.common.service.AuthService;
import com.skt.pe.util.Base64Util;
import com.skt.pe.util.EncryptUtil;

import java.net.URLEncoder;
import java.util.Iterator;
import java.util.List;

import static com.skt.pe.common.data.SKTUtil.log;

public class AuthProvider extends ContentProvider {


    public static final Uri CONTENT_URI = Constants.Auth.CONTENT_URI;

    private static Auth   auth = null;
    private static AuthInfo authInfo = null;
    private static String mac  = null;
    private static Object lock = new Object();

    public static SharedPreferences userInfo =null;
    private static Context context;

    static {
        auth = new Auth("", "");
        authInfo =new AuthInfo();
    }

    @Override
    public boolean onCreate() {
        authInfo =new AuthInfo();
        context = getContext().getApplicationContext();
         userInfo =null;
        if(context.getSharedPreferences("USERINFO",Context.MODE_PRIVATE)!=null){
            userInfo =context.getSharedPreferences("USERINFO",Context.MODE_PRIVATE);
        }
        SharedPreferences pref = null;
        if(context.getSharedPreferences("UPDATE_GMP", Context.MODE_PRIVATE)!= null){
            pref = context.getSharedPreferences("UPDATE_GMP", Context.MODE_PRIVATE);
        }


        if(pref != null && !("").equals(pref.getString("secretKey", ""))){
            Intent intent = new Intent(getContext(), AuthService.class);
            getContext().startService(intent);

            context = getContext();


            Intent alarmIntent = new Intent(Constants.Action.AUTH_SERVICE);
            PendingIntent pIntent = PendingIntent.getBroadcast(getContext(), 0, alarmIntent, 0);
            AlarmManager alarmManager = (AlarmManager)getContext().getSystemService(Context.ALARM_SERVICE);
            alarmManager.set(AlarmManager.RTC, System.currentTimeMillis()+1000, pIntent);

           /* SKTUtil.log(Log.DEBUG, "[AUTH-PROVIDER]", "OnCreate : AuthService.auth=[" + AuthService.auth + "]");*/
           mac = SKTUtil.getMdn(getContext());

           try {
                mac = SKTUtil.getMdn(getContext());
                Log.i("AuthProvider", "Folder mac ============"+mac);
                String[] list = context.fileList();
                if(list != null) {
                    for(String file : list) {
                        context.deleteFile(file);
                    }
                }
            } catch(Exception e) { }

           /* if(AuthService.auth != null) {
                auth = AuthService.auth;
            }*/
            authInfo.setCompanyCd("EX");
            authInfo.setCompanyNm("한국도로공사");
            authInfo.setSecretKey(userInfo.getString("SECURITYKEY",""));
            authInfo.setEncPwd(userInfo.getString("ENCPWD",""));
            authInfo.setNonce(userInfo.getString("NONCE",""));
            authInfo.setEncPwd(userInfo.getString("ENCPWD",""));
            authInfo.setId(userInfo.getString("USERID",""));
            authInfo.setPwd(userInfo.getString("USERPWD",""));
            authInfo.setTimestamp(userInfo.getString("NONCEUPDATEDT","'"));

            if(auth.getSecretKey() == null || auth.getSecretKey().length() <= 0){
                Log.d("Launcher AuthProvider prefInfo[secretKey]:",pref.getString("secretKey", ""));
                Log.d("Launcher AuthProvider prefInfo[nonce]:",pref.getString("nonce", ""));
                Log.d("Launcher AuthProvider prefInfo[id]:",pref.getString("id", ""));
                Log.d("Launcher AuthProvider prefInfo[pwd]:",pref.getString("pwd", ""));
                Log.d("Launcher AuthProvider prefInfo[timestamp]:",pref.getString("timestamp", ""));
                Log.d("Launcher AuthProvider prefInfo[auth]:", auth.toString());
                Log.d("Launcher AuthProvider prefInfo[secretkey]==============",">>>>>>>>>>>>>"+pref.getString("secretKey", ""));

                auth.setSecretKey(pref.getString("secretKey", ""));
                auth.setNonce(pref.getString("nonce", ""));
                auth.setId(pref.getString("id", ""));
                auth.setPwd(pref.getString("pwd", ""));
                auth.setTimestamp(pref.getString("timestamp", ""));
                auth.setCheckedCompanyCd("EX");


//					MDM 5.0은
//				 * secretKey가 없음?
//				 *

                try{
                    String pwd = auth.getPwd();
                    byte[] buf = EncryptUtil.generateAesEncode(pwd.getBytes(), auth.getSecretKey());
                    pwd = Base64Util.encodeBinary(buf);

                    AuthJob aj = new AuthJob("EX","한국도로공사",pwd);
                    auth.setBasicJob(aj);
                }catch(Exception e){
                    e.printStackTrace();
                    Log.i("AuthProvider", "AuthProvider Error...");
                }



                Log.d("prefInfo[auth set]:",auth.toString());
            }
        }
        else{

        }


        return false;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return 0;
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }

    //	*//**
//	 * 데이타 조회하기
//	 * @see android.content.ContentProvider#insert(android.net.Uri, android.content.ContentValues)
//	 *//*
    @Override
    public Uri insert(Uri uri, ContentValues values) {

        List<String> reqValue = uri.getPathSegments();
        Log.i("AuthProvider Launcher", "launcher auth provider insert.................reqValue size:::::"+reqValue.size());
        if(reqValue.size() > 0) {
            String serviceType = reqValue.get(0);
//			Log.i("AuthProvider Launcher", "service type==========>>>>"+serviceType);

            log(Log.DEBUG, "[AUTH-PROVIDER-QUERY]", "serviceType=[" + serviceType + "]");

            String result  = AuthData.RET_SUCCESS;

            //SKTUtil.log(Log.DEBUG, "[AUTH-PROVIDER-QUERY]", "insert : AuthService.auth=[" + AuthService.auth + "]");

            try {
//				try {
//					auth.checkAuth();
//				} catch(SKTException e) {
//					result = e.getErrCode();
//					throw new SKTException("", result);
//				}

                //CJ 수정사항
                //mdn 다시 복귀
                String mdn   = values.getAsString(AuthData.ID_MDN);
//				String mdn   = auth.getId();

                // authKey 얻기
                if(serviceType.equals(AuthData.ID_AUTH_KEY))
                {

                    String appId = values.getAsString(AuthData.ID_APP_ID);

                    log(Log.DEBUG, "[AUTH-PROVIDER-QUERY(" + serviceType +")]" , "secretKey=[" + authInfo.getSecretKey() + "]");
                    log(Log.DEBUG, "[AUTH-PROVIDER-QUERY(" + serviceType +")]" , "mdn=[" + mdn + "]");
                    log(Log.DEBUG, "[AUTH-PROVIDER-QUERY(" + serviceType +")]" , "appId=[" + appId + "]");
                    log(Log.DEBUG, "[AUTH-PROVIDER-QUERY(" + serviceType +")]" , "nonce=[" + authInfo.getNonce() + "]");

                    String authKey = "";

                        authInfo.setSecretKey(userInfo.getString("SECURITYKEY",""));
                        authKey = authInfo.getAuthKey(mdn, appId,authInfo.getSecretKey());

                    log(Log.DEBUG, "[AUTH-PROVIDER-QUERY(" + serviceType +")]", "authKey=[" + authKey + "] /" + result);
                    return Uri.parse(Constants.Auth.CONTENT_URI_STRING + "/" + serviceType + "/" + result + "/" + URLEncoder.encode(authKey));
                    // secretKey 얻기

                }

                else if(serviceType.equals(AuthData.ID_SECRET_KEY))
                {

                    String secretkey = "";

                        secretkey = authInfo.getSecretKey();

                    log(Log.DEBUG, "[AUTH-PROVIDER-QUERY(" + serviceType + ")]", "secretKey=[" + secretkey + "] /" + result);
                    return Uri.parse(Constants.Auth.CONTENT_URI_STRING + "/" + serviceType + "/" + result + "/" + URLEncoder.encode(secretkey));
                    // nonce 얻기

                }

                else if(serviceType.equals(AuthData.ID_NONCE))
                {

                    String nonce = "";

                        nonce = authInfo.getNonce();

                    log(Log.DEBUG, "[AUTH-PROVIDER-QUERY(" + serviceType + ")]", "nonce=[" + nonce + "] /" + result);
                    return Uri.parse(Constants.Auth.CONTENT_URI_STRING + "/" + serviceType + "/" + result + "/" + URLEncoder.encode(nonce));

                    // authKey, companyCd 얻기
                }

                else if(serviceType.equals(AuthData.ID_GMP_AUTH))
                {


                    String appId = values.getAsString(AuthData.ID_APP_ID);

                    log(Log.DEBUG, "[AUTH-PROVIDER-QUERY(" + serviceType +")]" , "secretKey=[" + authInfo.getSecretKey() + "]");
                    log(Log.DEBUG, "[AUTH-PROVIDER-QUERY(" + serviceType +")]" , "mdn=[" + mdn + "]");
                    log(Log.DEBUG, "[AUTH-PROVIDER-QUERY(" + serviceType +")]" , "appId=[" + appId + "]");
                    log(Log.DEBUG, "[AUTH-PROVIDER-QUERY(" + serviceType +")]" , "nonce=[" + authInfo.getNonce() + "]");

                    String authKey = "";
                    String checkedCompanyCd = "";

                    authInfo.setSecretKey(userInfo.getString("SECURITYKEY",""));
                    authKey = authInfo.getAuthKey(mdn, appId,authInfo.getSecretKey());
                        checkedCompanyCd = "EX";
                    authInfo.setId(userInfo.getString("USERID",""));

                    log(Log.DEBUG, "[AUTH-PROVIDER-QUERY(" + serviceType +")]", "authKey=[" + authKey + "] | companyCd=[" + checkedCompanyCd + "] / " + result);

                    Log.i("AuthProvider Launcher","uri ====" +Constants.Auth.CONTENT_URI_STRING + "/" + serviceType + "/" + result + "/" + URLEncoder.encode(authKey) + "/" + URLEncoder.encode(checkedCompanyCd) +"/"+ URLEncoder.encode(authInfo.getId()));
                    //CJ 변경
                    return Uri.parse(Constants.Auth.CONTENT_URI_STRING + "/" + serviceType + "/" + result + "/" + URLEncoder.encode(authKey) + "/" + URLEncoder.encode(checkedCompanyCd) +"/"+ URLEncoder.encode(authInfo.getId()));
                    // authKey, companyCd, encPwd 얻기
                }

                else if(serviceType.equals(AuthData.ID_GMP_AUTH_PWD))
                {

                    String appId = values.getAsString(AuthData.ID_APP_ID);

                    log(Log.DEBUG, "[AUTH-PROVIDER-QUERY(" + serviceType +")]" , "secretKey=[" + authInfo.getSecretKey() + "]");
                    log(Log.DEBUG, "[AUTH-PROVIDER-QUERY(" + serviceType +")]" , "mdn=[" + mdn + "]");
                    log(Log.DEBUG, "[AUTH-PROVIDER-QUERY(" + serviceType +")]" , "appId=[" + appId + "]");
                    log(Log.DEBUG, "[AUTH-PROVIDER-QUERY(" + serviceType +")]" , "nonce=[" + authInfo.getNonce() + "]");

                    String authKey = "";
                    String checkedCompanyCd = "";
                    String encPwd  = "";

                    authInfo.setSecretKey(userInfo.getString("SECURITYKEY",""));
                    authKey = authInfo.getAuthKey(mdn, appId,authInfo.getSecretKey());
                        checkedCompanyCd = "EX";
                        authInfo.setEncPwd(userInfo.getString("ENCPWD",""));
                        encPwd  = authInfo.getEncPwd();
                    authInfo.setId(userInfo.getString("USERID",""));

                    Log.v("[authKey]  - ",authKey);
                    Log.v("[authKey]  - ",checkedCompanyCd);
                    Log.v("[authKey]  - ",encPwd);
                    log(Log.DEBUG, "[AUTH-PROVIDER-QUERY(" + serviceType +")]", "authKey=[" + authKey + "] | companyCd=[" + checkedCompanyCd + "] | encPwd=[" + encPwd + "] / " + result);
                    //CJ 변경
                    return Uri.parse(Constants.Auth.CONTENT_URI_STRING + "/" + serviceType + "/" + result + "/" + URLEncoder.encode(authKey) + "/" + URLEncoder.encode(checkedCompanyCd) + "/" + URLEncoder.encode(encPwd) + "/" + URLEncoder.encode(authInfo.getId()));
                    // checkedCompanyCd 얻기
                }

                else if(serviceType.equals(AuthData.ID_CHECKED_COMPANY_CD))
                {
                    String appId = values.getAsString(AuthData.ID_APP_ID);
                    String authKey = "";
                    String checkedCompanyCd = "";
                    String encPwd  = "";
                    authInfo.setSecretKey(userInfo.getString("SECURITYKEY",""));
                    authKey = authInfo.getAuthKey(mdn, appId,authInfo.getSecretKey());
                    checkedCompanyCd = "EX";
                    authInfo.setEncPwd(userInfo.getString("ENCPWD",""));
                    encPwd  = authInfo.getEncPwd();
                    authInfo.setId(userInfo.getString("USERID",""));





                    log(Log.DEBUG, "[AUTH-PROVIDER-QUERY(" + serviceType +")]", "checkedCompanyCd=[" + checkedCompanyCd + "] / " + result);
                    //CJ 변경
                    return Uri.parse(Constants.Auth.CONTENT_URI_STRING + "/" + serviceType + "/" + result + "/" + URLEncoder.encode(checkedCompanyCd) + "/" + URLEncoder.encode(authInfo.getId()));
                    // companyList 얻기(basicJob, addJob)
                }

                else if(serviceType.equals(AuthData.ID_COMPANY_LIST))

                {


                    StringBuffer sb = new StringBuffer();
                    if(AuthData.RET_SUCCESS.equals(result)) {
                        sb.append(authInfo.getCompanyCd() + "|" + authInfo.getCompanyNm());
                    }

                    log(Log.DEBUG, "[AUTH-PROVIDER-QUERY(" + serviceType +")]", "companyList=[" + sb.toString() + "] / " + result);
                    return Uri.parse(Constants.Auth.CONTENT_URI_STRING + "/" + serviceType + "/" + result + "/" + URLEncoder.encode(sb.toString()));
                    //CJ 추가
                }

                else if(serviceType.equals(AuthData.ID_ID))
                {
                    String pwd = "";
                    try {

                        String secretkey = authInfo.getSecretKey();
                        pwd = authInfo.getPwd();
                        byte[] buf = EncryptUtil.generateAesEncode(pwd.getBytes(), secretkey);
                        pwd = Base64Util.encodeBinary(buf);
                    } catch(SKTException e) {
                        result = e.getErrCode();
                        throw new SKTException("", result);
                    } catch (Exception e) {
                        throw new SKTException("", "E999");
                    }

                    return Uri.parse(Constants.Auth.CONTENT_URI_STRING + "/" + serviceType + "/" + result + "/" + (mac==null || mac.trim().length()==0 ? "NONE" : mac) + "/" + URLEncoder.encode(authInfo.getId()) + "/" + URLEncoder.encode(pwd) + "/" + URLEncoder.encode(authInfo.getTimestamp()));
                }


            } catch(SKTException e) {
                e.printStackTrace();
                result = e.getErrCode();
                return Uri.parse(Constants.Auth.CONTENT_URI_STRING + "/" + serviceType + "/" + result);
            }
        }

        return Constants.Auth.CONTENT_URI;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        return null;
    }

    //	*//**
//	 * 데이타를 업데이트한다(비밀키 || NONCE || CHECKED_COMPANYCD)
//	 * @see android.content.ContentProvider#update(android.net.Uri, android.content.ContentValues, java.lang.String, java.lang.String[])
//	 *//*
    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {


        // 로그아웃 추후 추가된 사항이라 기존 인증어플의 경우 무시된다.
        if(values.size() == 0) {
            authInfo.setSecretKey("");
            authInfo.setNonce("");
            authInfo.setCompanyCd("");
            authInfo.setCompanyNm("");
            authInfo.setEncPwd("");
            log(Log.DEBUG, "[AUTH-PROVIDER-LOGOUT]", "LOGOUT");

            SharedPreferences pref = context.getSharedPreferences("UPDATE_GMP", Context.MODE_PRIVATE);
            SharedPreferences.Editor ed = pref.edit();

            ed.putString("secretKey", "");
            ed.putString("nonce", "");
            ed.putString("id", "");
            ed.putString("pwd", "");
            ed.putString("timestamp", "");
            ed.commit();

            getContext().sendBroadcast(new Intent(Constants.Action.LOGOUT_TICK));
            return 1;
        }

        // secretKey 올리기
        if(values.containsKey(AuthData.ID_SECRET_KEY)) {
            String secretKey = values.getAsString(AuthData.ID_SECRET_KEY);
            if(secretKey!=null && secretKey.trim().length()>0) {
                synchronized(lock) {
                    authInfo.setSecretKey(values.getAsString(AuthData.ID_SECRET_KEY));
                }
                log(Log.DEBUG, "[AUTH-PROVIDER-UPDATE(" + AuthData.ID_SECRET_KEY + ")]", "[" + authInfo.getSecretKey() + "]");
            } else {
                log(Log.DEBUG, "[AUTH-PROVIDER-UPDATE-SKIP(" + AuthData.ID_SECRET_KEY + ")]", "[" + authInfo.getSecretKey() + "]");
            }
        }

        // nonce 올리기
        if(values.containsKey(AuthData.ID_NONCE)) {
            String nonce = values.getAsString(AuthData.ID_NONCE);
            if(nonce!=null && nonce.trim().length()>0) {
                synchronized(lock) {
                    authInfo.setNonce(values.getAsString(AuthData.ID_NONCE));
                }
                log(Log.DEBUG, "[AUTH-PROVIDER-UPDATE(" + AuthData.ID_NONCE + ")]", "[" + authInfo.getNonce() + "]");
            } else {
                log(Log.DEBUG, "[AUTH-PROVIDER-UPDATE-SKIP(" + AuthData.ID_NONCE + ")]", "[" + authInfo.getNonce() + "]");
            }
        }

        // checkedCompnayCd 올리기
        if(values.containsKey(AuthData.ID_CHECKED_COMPANY_CD)) {
            synchronized(lock) {
                authInfo.setCompanyCd(values.getAsString(AuthData.ID_CHECKED_COMPANY_CD));
            }
            log(Log.DEBUG, "[AUTH-PROVIDER-UPDATE(" + AuthData.ID_CHECKED_COMPANY_CD + ")]", "[" + authInfo.getCompanyCd() + "]");
        }

        return 1;
    }

    /*public static Auth getAuth() {
        return auth;
    }

    public static void updateGMP(Auth nowAuth) {
        synchronized(lock) {
            auth = nowAuth;
        }
    }*/

    //	*//**
//	 * GMP 로그인 정보 일괄 쓰기
//	 * @param secretKey secretKey
//	 * @param nonce nonce
//	 * @param basicJob 주계정
//	 * @param addJob 부계정
//	 *//*
    public static void updateGMP(String secretKey, String nonce, AuthJob basicJob, AuthJob[] addJob) {
        synchronized(lock) {
            authInfo.setSecretKey(secretKey);
            authInfo.setNonce(nonce);
            if(basicJob != null) {
                authInfo.setEncPwd(basicJob.getEncPwd());
                authInfo.setCompanyCd(basicJob.getCompanyNm());
                authInfo.setCompanyCd(basicJob.getCompanyCd());

            }
        }
    }
    //CJ 추가
    public static void updateGMP(String secretKey, String nonce, AuthJob basicJob, AuthJob[] addJob, String id, String pwd, String timestamp) {
        Log.i("AuthProvider", "====updateGMP====");
        SharedPreferences pref = context.getSharedPreferences("UPDATE_GMP", Context.MODE_PRIVATE);
        SharedPreferences.Editor ed = pref.edit();

        ed.putString("secretKey", secretKey);
        ed.putString("nonce", nonce);
        ed.putString("id", id);
        ed.putString("pwd", pwd);
        ed.putString("timestamp", timestamp);
        ed.commit();

        updateGMP(secretKey, nonce, basicJob, addJob);
        synchronized(lock) {
            authInfo.setId(id);
            authInfo.setPwd(pwd);
            authInfo.setTimestamp(timestamp);
        }
    }

    //**
//	 * GMP Legacy 로그인 정보 일괄 쓰기
//	 * @param companyCd companyCd
//	 * @param encPwd encPwd
//	 *//*
    /*public static void updateLegacy(String companyCd, String encPwd) {
        synchronized(lock) {
            auth.setAddJob(new AuthJob(companyCd, null, encPwd));
        }
    }*/

    /*public static void changeCompany(String companyCd) {
        synchronized(lock) {
            auth.setCheckedCompanyCd(companyCd);
        }
    }*/

}
