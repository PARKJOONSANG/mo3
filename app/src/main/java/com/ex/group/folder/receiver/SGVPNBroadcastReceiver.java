package com.ex.group.folder.receiver;

import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.util.Log;

import com.ex.group.board.constants.IntentConstants;
import com.ex.group.folder.utility.Constants;
import com.ex.group.folder.utility.Constants;
import com.skt.pe.util.io.ExtensionFilter;
import com.skt.pe.util.io.FileUtil;
import com.sktelecom.ssm.lib.SSMLib;
import com.sktelecom.ssm.lib.constants.SSMProtocolParam;

import java.io.File;

import static com.sktelecom.ssm.remoteprotocols.ResultCode.OK;

/**
 *                                                                      Created by JSP on 18.11.07.
 */

public class SGVPNBroadcastReceiver extends BroadcastReceiver {
    String TAG = "SGVPNBroadcastReceiver_Enrolled in Manifest";
    WifiManager wifi;
    BluetoothAdapter bAdapter;

    int Wi_Fi;
    int bluetooth;

    SSMLib ssmLib;
    int ssmCode;
    Handler handler;

    SharedPreferences userPref;



    Runnable runner = new Runnable() {
        @Override
        public void run() {
            int result;
            result = ssmLib.setLoginStatus(SSMProtocolParam.LOGOUT);
            Log.i(TAG, "SSM LOGIN STATUS result : "+result);
            Log.i(TAG, "ssm LOGOUT wifi : "+Wi_Fi);
            Log.i(TAG, "ssm LOGOUT bluetooth : "+bluetooth);

            if(Wi_Fi == ConnectivityManager.TYPE_WIFI){
                if(wifi != null){
                    Log.i(TAG, "Wi_Fi ON!!!!");
                    wifi.setWifiEnabled(true);
                }
            }
            if(bluetooth == BluetoothAdapter.STATE_ON){
                if(bAdapter != null){
                    Log.i(TAG, "Bluetotoh ON!!");
                    bAdapter.enable();
                }
            }
        }
    };



    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i(TAG, "BroadcastReceiver SGVPNBroadcastReceiver========== getAction ========11111111111111"+intent.getAction());
        userPref = context.getSharedPreferences("USERINFO", Context.MODE_PRIVATE);


        ssmLib = SSMLib.getInstance(context);
        wifi = (WifiManager)context.getSystemService(Context.WIFI_SERVICE);
        bAdapter = BluetoothAdapter.getDefaultAdapter();

        Wi_Fi = userPref.getInt("WIFI", ConnectivityManager.TYPE_MOBILE);
        bluetooth = userPref.getInt("BLUETOOTH", BluetoothAdapter.STATE_OFF);

        if(ssmLib.checkSSMValidation() != OK){
            ssmLib.initialize();
        }
        int service_status = intent.getIntExtra("STATUS", 0);
        Log.i(TAG, "service_status ====================== "+service_status+"\t \t ");
        Log.i(TAG,"[Status] : ["+String.valueOf(service_status)+"]");


        if(intent.getAction().equals("com.sgvpn.vpnservice.STATUS")){
            Log.i(TAG,"[Status] : ["+String.valueOf(service_status)+"]");
            if(service_status == Constants.Status.Connection_N_Status.LEVEL_DISCONNECTED_DONE.ordinal() ){       //접속 종료
                handler = new Handler();
                handler.postDelayed(runner, 2200);
                Log.i(TAG,"[Status] : ["+String.valueOf(service_status)+"]");

            }
            else if(service_status == Constants.Status.Connection_N_Status.LEVEL_CONNECTED.ordinal()){        //로그인 버튼을 누르기 전에 업무정책이 반영 되는것을 방지하기 위해 LoginActivity에서 로그인 버튼을 누른경우 정책
                ssmLib.setLoginStatus(SSMProtocolParam.LOGIN);
            }
        }
    }











}
