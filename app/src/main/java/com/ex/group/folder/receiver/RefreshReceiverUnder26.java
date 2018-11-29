package com.ex.group.folder.receiver;

import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.os.Vibrator;
import android.util.Log;

import com.ex.group.folder.MainActivity;
import com.ex.group.folder.utility.Constants;
import com.sktelecom.ssm.lib.SSMLib;
import com.sktelecom.ssm.lib.constants.SSMProtocolParam;

import static android.content.Context.MODE_PRIVATE;
import static android.content.Context.VIBRATOR_SERVICE;
import static android.content.Intent.FLAG_ACTIVITY_CLEAR_TOP;
import static com.sktelecom.ssm.remoteprotocols.ResultCode.OK;

/**
 * Created by JSP 18.11.21
 */

public class RefreshReceiverUnder26 extends BroadcastReceiver {
    public RefreshReceiverUnder26() {
    }

    ;

    String TAG = "====RefreshReceiverUnder26====";

    @Override
    public void onReceive(Context context, Intent intent) {


        MainActivity contextMain = (MainActivity) MainActivity.contextMain;
        if (contextMain != null) {
            context = contextMain;
            contextMain.vibrate(300);
        }

        if (intent.getAction().equals("com.ex.group.store.GO_MOFFICE")) {
            Log.d(TAG, "====  RefreshReceiver  ====" + intent.getStringExtra("packageName"));

            SharedPreferences user = context.getSharedPreferences("USERINFO", MODE_PRIVATE);

            if (user.getString("LOGINSTATE", "").equals("LOGIN")) {                                                                          //현재 로그인 상태라면

                if (MainActivity.vpnConn != null) {                                                                                                  //vpn이 살아 있다면

                    if (MainActivity.vpnConn.getStatus() == Constants.Status.Connection_N_Status.LEVEL_CONNECTED.ordinal()) {                      //vpn접속중이라면
                        Log.v("packageName", "::::" + intent.getStringExtra("packageName"));
                        try {

                            if(contextMain !=null) {
                                Intent intent3 = new Intent(intent.getStringExtra("packageName") + ".LAUNCH_MAIN");
                                context.startActivity(intent3.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                            }else{
                                Intent intent3 = new Intent(intent.getStringExtra("packageName") + ".LAUNCH_MAIN");
                                context.startActivity(intent3);
                            }

                        } catch (Exception e) {
                            if (contextMain != null) {
                                Intent intent3 = context.getPackageManager().getLaunchIntentForPackage(intent.getStringExtra("packageName"));
                                context.startActivity(intent3.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                            } else {
                                Intent intent3 = context.getPackageManager().getLaunchIntentForPackage(intent.getStringExtra("packageName"));
                                context.startActivity(intent3);
                            }
                        }
                    } else {
                        if (contextMain != null) {
                        Intent intent3 = new Intent("com.ex.group.folder" + ".LAUNCH_MAIN");
                        context.startActivity(intent3.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));}
                        else{
                            Intent intent3 = new Intent("com.ex.group.folder" + ".LAUNCH_MAIN");
                            context.startActivity(intent3);
                        }

                    }

                } else {
                    /*MainActivity contextMain = (MainActivity) MainActivity.contextMain;// 접속중이지만 vpn이 죽어 있다면
                    if(contextMain!=null){contextMain.finish();}*/
                    if (contextMain != null) {
                    Intent intent3 = new Intent("com.ex.group.folder" + ".LAUNCH_MAIN");
                    /* intent3.setFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);*/
                    context.startActivity(intent3.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));}
                    else{
                        Intent intent3 = new Intent("com.ex.group.folder" + ".LAUNCH_MAIN");
                        /* intent3.setFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);*/
                        context.startActivity(intent3);
                    }
                }
            }                                                                                                                                     //현재 로그인 상태가 아니라면
            else if (user.getString("LOGINSTATE", "").equals("LOGOUT")) {

                if (contextMain != null) {
                    Intent intent3 = context.getPackageManager().getLaunchIntentForPackage("com.ex.group.folder");
                    context.startActivity(intent3.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                } else {
                    Intent intent3 = context.getPackageManager().getLaunchIntentForPackage("com.ex.group.folder");
                    context.startActivity(intent3);
                }


            }


        } else if (intent.getAction().equals("com.ex.group.store.REFRESH_APP")) {
            Log.d(TAG, "====  RefreshReceiver  ====" + intent.getAction());

            //contextMain.vibrate(300);

        }

    }


}
