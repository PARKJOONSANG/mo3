package com.ex.group.folder.service;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;


import com.ex.group.folder.utility.CommonUtil;
import com.ex.group.folder.utility.Constants;
import com.ex.group.folder.utility.HttpUtil;
import com.ex.group.folder.utility.LogMaker;
import com.ex.group.folder.utility.XMLData;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import java.text.SimpleDateFormat;
import java.util.Date;


public class FirebaseIDService extends FirebaseInstanceIdService {
    final static String TAG = "FirebaseIDService";
    String token;
    String mdn;
    String imei;
    String mac;

    Context context;



    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();

        context = getApplicationContext();
        token = FirebaseInstanceId.getInstance().getToken();

        Log.i(TAG, " : "+token);

        if(token != null && !("").equals(token)){
            sendToken(context, token);
        }
    }

    public static void sendToken(final Context context, String token){

        String url = "http://travel.ex.co.kr/cloudpush/mergeClientInfo.do?app_info="+context.getPackageName()+"&device_id="+ CommonUtil.getMdn(context)+"&device_imei="+CommonUtil.getImei(context)+"&device_token="+token;

        Log.v("TOKEN SEND URL","====\n\n="+url);
        if(token!=null && !("").equals(token)){
                Log.i(TAG, "fcm date : "+context.getSharedPreferences("USERINFO", MODE_PRIVATE).getString("FCMDATE", ""));

            HttpUtil.request(context, url, new HttpUtil.HttpCallback() {
                @Override
                public void onResponse(String response) {
                    try{
                        LogMaker.logStart();
                        Log.i(TAG, "response : "+response);
                        LogMaker.logEnd();

                        XMLData responseXML =  new XMLData(response);
                        if((Constants.FCM.SUCCESS).equals(responseXML.get("resStatus"))){
                            Log.i(TAG, "fcm success : "+responseXML.get("resStatus"));
                            SharedPreferences userPref = context.getSharedPreferences("USERINFO", MODE_PRIVATE);
                            SharedPreferences.Editor editor = userPref.edit();
                            editor.putString("FCMDATE", new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
                            editor.apply();
                        }
                    }catch(Exception e){
                        e.printStackTrace();
                    }
                }
            });
        }
    }

}
