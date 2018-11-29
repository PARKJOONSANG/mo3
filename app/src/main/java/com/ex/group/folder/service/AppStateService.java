package com.ex.group.folder.service;

/*
 *                                                                            -CREATED BY JSP 2018.11
 */

import android.app.Activity;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Binder;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.ex.group.folder.MainActivity;
import com.ex.group.folder.R;
import com.ex.group.folder.dialog.CommonDialog_oneButton;
import com.ex.group.folder.utility.ClientUtil;
import com.skt.pe.common.vpn.SGVPNConnection;
import com.sktelecom.ssm.lib.SSMLib;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Logger;

import static com.ex.group.folder.utility.ClientUtil.SGN_PACKAGE;
import static com.sktelecom.ssm.lib.constants.SSMProtocolParam.LOGIN;
import static com.sktelecom.ssm.lib.constants.SSMProtocolParam.LOGOUT;

public class AppStateService extends Service {

    public static  TimerTask saveVpnSErvice;
    public static Timer secondTimer;
    private final IBinder mBinder = new LocalBinder();

    public static SGVPNConnection vpnConn;
    public static IBinder tempService = null;
    private SgnServiceConnection mConnection;
    private class SgnServiceConnection implements ServiceConnection{

        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            Log.i("", "===================onServiceConnected===================");
            tempService = iBinder;
            if(vpnConn == null)
            {
                vpnConn = SGVPNConnection.getInstance(tempService);
                Log.i("","Service Connected");
            }

        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            Log.i("SgnServiceConnection","==========onServiceDisconneted=============");
            System.out.println("Service Disconneted");
            tempService = null;

            Intent intent  = new Intent(ClientUtil.SGVPN_API);
            intent.setPackage(ClientUtil.SGN_PACKAGE);
            if(!bindService(intent, mConnection,BIND_AUTO_CREATE)){
                Log.i(TAG,"servic binding error");

            }else{
                try{
                    startService(intent);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }

        }
    }
    public class LocalBinder extends Binder {
        public AppStateService getService(){
            return AppStateService.this;
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.e(TAG("onBind"),"BINDSUCCESS");

        return mBinder;

    }


    private String TAG="===APPSTATESERVICE===";
    private String TAG(String Name){
        String tag ="===APPSTATESERVICE===";
        return tag+"["+Name+"]";
    }

    @Override
    public void onCreate() {


        super.onCreate();





        Log.e(TAG("onCreate"),"SERVICECREATED");


    }

  @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
        /*
        int TRIM_MEMORY_BACKGROUND = 40;
        int TRIM_MEMORY_COMPLETE = 80;
        int TRIM_MEMORY_MODERATE = 60;
        int TRIM_MEMORY_RUNNING_CRITICAL = 15;
        int TRIM_MEMORY_RUNNING_LOW = 10;
        int TRIM_MEMORY_RUNNING_MODERATE = 5;
        int TRIM_MEMORY_UI_HIDDEN = 20;*/
        Log.e(TAG("onTrimMemory"), String.valueOf(level));
        switch (level) {
            case TRIM_MEMORY_UI_HIDDEN:
                Log.e(TAG("onTrimMemory"), "STATE [TRIM_MEMORY_UI_HIDDEN]");
                break;
            case TRIM_MEMORY_RUNNING_MODERATE:
                Log.e(TAG("onTrimMemory"), "STATE [TRIM_MEMORY_RUNNING_MODERATE]");

                break;
            case TRIM_MEMORY_RUNNING_LOW:
                Log.e(TAG("onTrimMemory"), "STATE [TRIM_MEMORY_RUNNING_LOW]");

                break;
            case TRIM_MEMORY_RUNNING_CRITICAL:
                Log.e(TAG("onTrimMemory"), "STATE [TRIM_MEMORY_RUNNING_CRITICAL]");
                break;
            case TRIM_MEMORY_BACKGROUND:
                Log.e(TAG("onTrimMemory"), "STATE [TRIM_MEMORY_BACKGROUND]");
                break;
            case TRIM_MEMORY_MODERATE:
                Log.e(TAG("onTrimMemory"), "STATE [TRIM_MEMORY_MODERATE]");
                break;
            case TRIM_MEMORY_COMPLETE:
                Log.e(TAG("onTrimMemory"), "STATE [TRIM_MEMORY_COMPLETE]");
                break;
        }




        if (level > TRIM_MEMORY_COMPLETE) {

            if (MainActivity.vpnConn != null) {

                MainActivity.vpnConn.disconnection();
                SSMLib ssmLib = SSMLib.getInstance(getBaseContext());
                ssmLib.setLoginStatus(LOGOUT);
                Log.e(TAG("onTrimMemmory"), "VPN 종료 성공");
                MainActivity.vpnConn.disconnection();

            }else{
                mConnection = new SgnServiceConnection();
                Intent intent  = new Intent(ClientUtil.SGVPN_API);
                intent.setPackage(ClientUtil.SGN_PACKAGE);
                bindService(intent, mConnection,BIND_AUTO_CREATE);
                if(!bindService(intent,mConnection,BIND_AUTO_CREATE)){
                    Log.i(TAG,"Service bind error");
                }

                vpnConn = SGVPNConnection.getInstance(tempService);
                Log.e(TAG,"new VPN Connection");

                vpnConn.disconnection();
            }

                SSMLib ssmLib = SSMLib.getInstance(getBaseContext());
                ssmLib.setLoginStatus(LOGOUT);
                Log.e(TAG("onTrimMemmory"), "VPN 종료 재시도 성공");
                SharedPreferences user = getSharedPreferences("USERINFO", MODE_PRIVATE);
                SharedPreferences.Editor edit = user.edit();
                edit.putString("LOGINSTATE", "LOGOUT");
                edit.commit();
                Log.e(TAG("LOGINSTATE"), "LOGOUT");
                Toast.makeText(getBaseContext(),"로그아웃되었습니다.",Toast.LENGTH_SHORT).show();
            }







        }



    @Override
    public void onTaskRemoved(Intent rootIntent) {
        super.onTaskRemoved(rootIntent);
        Log.e(TAG("onTaskRemoved"), "rootIntent"+rootIntent);


        if (MainActivity.vpnConn != null) {

            MainActivity.vpnConn.disconnection();
            SSMLib ssmLib = SSMLib.getInstance(getBaseContext());
            ssmLib.setLoginStatus(LOGOUT);
            Log.e(TAG("onTaskRemoved"), "VPN 종료 성공");
        }
        else{
            Log.e(TAG("onTaskRemoved"), "VPN 종료 재시도");
            mConnection = new SgnServiceConnection();
            Intent intent  = new Intent(ClientUtil.SGVPN_API);
            intent.setPackage(ClientUtil.SGN_PACKAGE);
            bindService(intent, mConnection,BIND_AUTO_CREATE);
            if(!bindService(intent,mConnection,BIND_AUTO_CREATE)){
                Log.i(TAG,"Service bind error");
            }

            vpnConn = SGVPNConnection.getInstance(tempService);
            Log.e(TAG,"new VPN Connection  Status : "+String.valueOf(vpnConn.getStatus()));

            vpnConn.disconnection();

            Log.e(TAG("onTaskRemoved"), "VPN 종료 재시도 성공");
        }




        SharedPreferences user = getSharedPreferences("USERINFO", MODE_PRIVATE);
        SharedPreferences.Editor edit = user.edit();
        edit.putString("LOGINSTATE", "LOGOUT");
        edit.commit();
        Log.e(TAG("LOGINSTATE"), "LOGOUT");
        Toast.makeText(getBaseContext(),"로그아웃되었습니다.",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        Log.e(TAG("onDestroy"), "");


        if (MainActivity.vpnConn != null) {

            MainActivity.vpnConn.disconnection();
            SSMLib ssmLib = SSMLib.getInstance(getBaseContext());
            ssmLib.setLoginStatus(LOGOUT);
            Log.e(TAG("onDestroy"), "VPN 종료 성공");

        }
        else{
            mConnection = new SgnServiceConnection();
            Intent intent  = new Intent(ClientUtil.SGVPN_API);
            intent.setPackage(ClientUtil.SGN_PACKAGE);
            bindService(intent, mConnection,BIND_AUTO_CREATE);
            if(!bindService(intent,mConnection,BIND_AUTO_CREATE)){
                Log.i(TAG,"Service bind error");
            }

            vpnConn = SGVPNConnection.getInstance(tempService);
            Log.e(TAG,"new VPN Connection");

            vpnConn.disconnection();

        }



        SharedPreferences user = getSharedPreferences("USERINFO", MODE_PRIVATE);
        SharedPreferences.Editor edit = user.edit();
        edit.putString("LOGINSTATE", "LOGOUT");
        edit.commit();
        Log.e(TAG("LOGINSTATE"), "LOGOUT");;
        Toast.makeText(getBaseContext(),"로그아웃되었습니다.",Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onUnbind(Intent intent) {


        Log.e(TAG("onUnBIND"),"");
        return super.onUnbind(intent);
    }

    public void AidleNetwork() {
        ConnectivityManager manager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo mobile = manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        NetworkInfo wi_fi = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if (mobile.isConnected() || wi_fi.isConnected()) {
            try {


           Method setMobileDataEnabledMethod = ConnectivityManager.class.getDeclaredMethod("setMobileDataEnabled",boolean.class);
           setMobileDataEnabledMethod.setAccessible(false);
           setMobileDataEnabledMethod.invoke(manager,false);}catch (Exception e){e.printStackTrace();}
        } else {

        }

    }
    private void setMobileDataEnabled(Context context, boolean enabled) throws Exception {
        final ConnectivityManager conman = (ConnectivityManager)  context.getSystemService(Context.CONNECTIVITY_SERVICE);
        final Class conmanClass = Class.forName(conman.getClass().getName());
        final Field connectivityManagerField = conmanClass.getDeclaredField("mService");
        connectivityManagerField.setAccessible(true);
        final Object connectivityManager = connectivityManagerField.get(conman);
        final Class connectivityManagerClass =  Class.forName(connectivityManager.getClass().getName());
        final Method setMobileDataEnabledMethod = connectivityManagerClass.getDeclaredMethod("setMobileDataEnabled", Boolean.TYPE);
        setMobileDataEnabledMethod.setAccessible(true);
        setMobileDataEnabledMethod.invoke(connectivityManager, enabled);
    }


}
