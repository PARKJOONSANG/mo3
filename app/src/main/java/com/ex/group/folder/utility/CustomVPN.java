/*
package com.ex.group.folder.utility;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.util.Log;

import com.sgvpn.vpn_service.api.MobileApi;
import com.skt.pe.common.vpn.SGVPNConnection;
import com.skt.pe.common.vpn.StartRunnable;
import com.skt.pe.common.vpn.StopRunnable;

public class CustomVPN {
    public static String TAG = "SGVPNConnection";
    public static final String SGN_PACKAGE = "com.sgvpn.vpn_service";
    public static String vpnUrl = "128.200.121.123";
    private static com.skt.pe.common.vpn.SGVPNConnection instance = new com.skt.pe.common.vpn.SGVPNConnection();
    public static IBinder tempService = null;
    Context mContext;

    public CustomVPN() {
    }

    public static com.skt.pe.common.vpn.SGVPNConnection getInstance() {
        if (instance == null) {
            instance = new com.skt.pe.common.vpn.SGVPNConnection();
            Log.i(TAG, "SGVPNConnection instance is null");
        } else {
            Log.i(TAG, "SGVPNConnection instance is not null");
        }

        return instance;
    }

    public static CustomVPN getInstance(IBinder tempservice) {
        Log.i(TAG, "getinstance tempservice  : " + tempservice);
        if (tempservice != null) {
            tempService = tempservice;
        }

        if (instance == null) {
            instance = new com.skt.pe.common.vpn.SGVPNConnection();
            Log.i(TAG, "SGVPNConnection instance is null");
        } else {
            Log.i(TAG, "SGVPNConnection instance is not null");
        }

        return instance;
    }

    public int getStatus() {
        int status = 0;
        Log.i(TAG, "getStatus");
        if (tempService != null) {
            MobileApi objAidl = MobileApi.Stub.asInterface(tempService);

            try {
                status = objAidl.statusVPN();
                Log.i(TAG, "status : " + status);
            } catch (Exception var4) {
                var4.printStackTrace();
            }
        }

        return status;
    }

    public static IBinder getTempService() {
        return tempService;
    }

    public Intent getService() {
        Intent requestpermission = null;
        MobileApi objAidl = MobileApi.Stub.asInterface(tempService);

        try {
            requestpermission = objAidl.prepareVPNService();
        } catch (Exception var4) {
            Log.e(TAG, "getService exception : " + var4);
            var4.printStackTrace();
        }

        return requestpermission;
    }

    public Intent getPermissionCheck() {
        Intent permissioncheck = null;
        MobileApi objAidl = MobileApi.Stub.asInterface(tempService);

        try {
            permissioncheck = objAidl.preparePermissionCheck();
        } catch (Exception var4) {
            Log.e(TAG, "getPermissionCheck exception : " + var4);
        }

        return permissioncheck;
    }

    public void connection(String id, String pw) {
        Log.i(TAG, "connection tempService : " + tempService);
        StartRunnable start_VPN = new StartRunnable(tempService, vpnUrl, id, pw);
        Thread start = new Thread(start_VPN);
        start.start();
    }

    public void disconnection() {
        StopRunnable stop_VPN = new StopRunnable(tempService);
        Thread stop = new Thread(stop_VPN);
        stop.start();
    }

    public static class SgnServiceConnection implements ServiceConnection {
        public SgnServiceConnection() {
        }

        public void onServiceConnected(ComponentName name, IBinder service) {
            if (com.skt.pe.common.vpn.SGVPNConnection.tempService != null) {
                com.skt.pe.common.vpn.SGVPNConnection.tempService = service;
                Log.i(com.skt.pe.common.vpn.SGVPNConnection.TAG, "=======SgnServiceConnection Service Connected=======");
            }

        }

        public void onServiceDisconnected(ComponentName name) {
            Log.i(com.skt.pe.common.vpn.SGVPNConnection.TAG, "=======SgnServiceConnection Service Disconnected=======");
            com.skt.pe.common.vpn.SGVPNConnection.tempService = null;
        }
    }
}*/
