package com.ex.group.folder.utility;

import android.app.Activity;
import android.app.KeyguardManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.PersistableBundle;
import android.os.Vibrator;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.ex.group.folder.LoginActivity;
import com.ex.group.folder.retrofitclient.pojo.RequestInitInfo;

import java.security.KeyStore;
import java.util.Comparator;

import com.ex.group.folder.R;
import com.samsung.android.sdk.pass.Spass;
import com.samsung.android.sdk.pass.SpassFingerprint;

import javax.crypto.KeyGenerator;

public class BaseActivity extends AppCompatActivity {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);

    }


    public void vibrate(int i){
        Vibrator vibe =(Vibrator)getSystemService(VIBRATOR_SERVICE);
        vibe.vibrate(i);
    };



    public static void logStart(){
        Log.v("","-\n\n\n--------------------------------------------------[START]----------");
    }
    public static void logEnd(){
        Log.v("","--------------------------------------------------[THE END]----------\n\n\n\n-");
    }
    public static void logmaking(String name, String value){

        Log.v("","["+name+"]-["+value+"]");

    }
    public static void logmaking(String name, int value){

        Log.v("","["+name+"]-["+value+"]");

    }
    public static void logmaking(String name, boolean value){

        Log.v("","["+name+"]-["+value+"]");

    }


    public  void setSharedString(String name , String value){
        SharedPreferences user =getSharedPreferences("USERINFO",MODE_PRIVATE);
        SharedPreferences.Editor edit = user.edit();
        edit.putString(name,value);
        logmaking("SHARED MAP edit ",name+"]-["+value);
        edit.commit();
    }
    public void setSharedint(String  name ,int value){
        SharedPreferences user =getSharedPreferences("USERINFO",MODE_PRIVATE);
        SharedPreferences.Editor edit = user.edit();
        edit.putInt(name,value);
        logmaking("SHARED MAP edit ",name+"]-["+value);
        edit.commit();
    }
    public void setSharedlong(String  name ,long value){
        SharedPreferences user =getSharedPreferences("USERINFO",MODE_PRIVATE);
        SharedPreferences.Editor edit = user.edit();
        edit.putLong(name,value);
        logmaking("SHARED MAP edit ",name+"]-["+value);
        edit.commit();
    }
    public void setSharedboolean(String  name ,boolean value){
        SharedPreferences user =getSharedPreferences("USERINFO",MODE_PRIVATE);
        SharedPreferences.Editor edit = user.edit();
        edit.putBoolean(name,value);
        logmaking("SHARED MAP edit ",name+"]-["+value);
        edit.commit();
    }
    public String getSharedString(String name){
        SharedPreferences user =getSharedPreferences("USERINFO",MODE_PRIVATE);
        if(!user.contains(name)){setSharedString(name,"");}
        final String value = user.getString(name,"");
        logmaking("SHARED MAP get ",name+"]-["+value);
        return value;
    }
    public int getSharedint(String name){
        SharedPreferences user =getSharedPreferences("USERINFO",MODE_PRIVATE);
        if(!user.contains(name)){setSharedint(name,0);}
        int value = user.getInt(name,0);
        logmaking("SHARED MAP get ",name+"]-["+value);
        return value;
    }
    public long getSharedlong(String name){
        SharedPreferences user =getSharedPreferences("USERINFO",Activity.MODE_PRIVATE);
        if(!user.contains(name)){setSharedlong(name,0);}
        final long value = user.getLong(name,0);
        logmaking("SHARED MAP get ",name+"]-["+value);
        return value;
    }
    public boolean getSharedboolean(String name){
        SharedPreferences user =getSharedPreferences("USERINFO",MODE_PRIVATE);
        if(!user.contains(name)){setSharedboolean(name,false);}
        final boolean value = user.getBoolean(name,false);
        logmaking("SHARED MAP get ",name+"]-["+value);
        return value;
    }
    public void setShared(){
        USERID= getSharedString("USERID");
        USERPWD=getSharedString("USERPWD");
        WAYTOLOGIN=getSharedint("WAYTOLOGIN");
        SAVEIDSTATE =getSharedboolean("SAVEIDSTATE");
        PINCODE =getSharedString("PINCODE");

    }
    public String USERID;
    public String USERPWD;
    public int WAYTOLOGIN;
    public boolean SAVEIDSTATE;
    public String PINCODE;

    public String getVersion(){
        try{
            PackageInfo i = getApplicationContext().getPackageManager().getPackageInfo(getApplicationContext().getPackageName(),0);
            return i.versionName;
        }catch (PackageManager.NameNotFoundException e){
            return "";
        }
    }

    public static void finishAndRemoveTask(Activity activity){
        if(Build.VERSION.SDK_INT>Build.VERSION_CODES.LOLLIPOP){
            activity.finishAndRemoveTask();
        }else {
            activity.finish();
        }


    }




}
