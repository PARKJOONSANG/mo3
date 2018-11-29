package com.ex.group.folder.dialog;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.TextView;


import com.ex.group.folder.utility.AppInfoPackage;
import com.ex.group.folder.utility.ClientUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import com.ex.group.folder.R;

import static com.ex.group.folder.utility.ClientUtil.FILE_PATH;
import static com.ex.group.folder.utility.ClientUtil.SGN_APK;
import static com.ex.group.folder.utility.ClientUtil.SSM_APK;
import static com.ex.group.folder.utility.ClientUtil.SSM_INSTALLER_APK;
import static com.ex.group.folder.utility.ClientUtil.V3_APK;


/**
 * Created by heejung on 2017-04-19.
 */

public class InstallerDialog2 extends Activity implements View.OnClickListener{
    final static String TAG = "InstallerDialog";
    final static int SSM_INSTALLER_CODE = 0;
    final static int SSM_CODE = 1;
    final static int V3_CODE = 2;
    final static int SGN_CODE = 3;

    ArrayList<AppInfoPackage> appList;
    int total;
    int cnt;
    TextView tv_title;
    TextView tv_install;
    TextView btn_confirm;
    SharedPreferences cntShared;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setFinishOnTouchOutside(false);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_dialog2);

        cntShared = getSharedPreferences("installed", MODE_PRIVATE);
        cnt = cntShared.getInt("cnt", 0);

        Log.d(TAG, "------------onCreate------------");
        tv_title = (TextView)findViewById(R.id.tv_title);
        tv_install = (TextView)findViewById(R.id.tv_install);
        btn_confirm = (TextView)findViewById(R.id.btn_confirm);

        btn_confirm.setOnClickListener(this);
        appList = new ArrayList<AppInfoPackage>();
        appList = getIntent().getParcelableArrayListExtra("uninstalledList");
        total = getIntent().getIntExtra("total", 0);
//        total = appList.size();       베가 아이언2(IM-910S) 에서 무조건 1로만 받아옴(3개가 미설치 상태인 경우에도)
        Log.d(TAG, "total : "+total);
        for(int i=0; i<appList.size(); i++){
            Log.d(TAG, i+" packageName : "+appList.get(i).getPackageName());
        }
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "------------onResume------------");
        cnt = cntShared.getInt("cnt", 0);
        Log.i(TAG,"RESUME SHARE CNT : "+cnt);       //2

        tv_title.setText(getString(R.string.category_core)+" - "+appList.get(cnt).getAppName()+" ("+(int)(cnt+1)+"/"+total+")");
        tv_install.setText(getInstallMessage(appList.get(cnt).getPackageName()));
        //tv_install.setText(getInstallMessage(appList.get(cnt).getPackageName())+"   ("+(int)(cnt+1)+"/"+total+")");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_confirm:
                cnt = cntShared.getInt("cnt", 0);
                Log.d(TAG, "onclick cnt============>>>>"+cnt);
                Log.d(TAG, "onclick install app name============>>>>"+appList.get(cnt).getApkName());

                install(appList.get(cnt).getApkName());


                SharedPreferences.Editor editor = cntShared.edit();
                editor.putInt("cnt", cnt++);
                editor.apply();

                Log.i(TAG,"onClick 후 cnt : "+cntShared.getInt("cnt", 0));

                finish();
                break;
        }
    }

    //설치 공통
    public void install(String apkName){
        if(copyApkFromAssets(apkName)){

            Uri apkUri = null;
            File toInstall = new File(FILE_PATH, apkName);

            apkUri = Uri.fromFile(toInstall);
            Intent intent = new Intent(Intent.ACTION_INSTALL_PACKAGE);
            intent.setData(apkUri);
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

            cnt = cntShared.getInt("cnt", 0);
            Log.i(TAG, "install(apkName ) cnt : "+cnt);
            Log.i(TAG, "install(apkName ) total : "+total);

            if(cnt == total-1){
                startActivity(intent);
                finish();
            }
            else{
                startActivityForResult(intent, setRequestCode(appList.get(cnt++).getApkName()));
            }
            Log.i(TAG, "onclick cnt : "+cntShared.getInt("cnt", 0));

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SharedPreferences.Editor editor=  cntShared.edit();
        editor.clear();
        editor.apply();
    }

    public int setRequestCode(String appName){
        int code = -1;

        if((SSM_APK).equals(appName)){
            code = SSM_CODE;
        }
        else if(SSM_INSTALLER_APK.equals(appName)){
            code = SSM_INSTALLER_CODE;
        }
        else if(V3_APK.equals(appName)){
            code = V3_CODE;
        }
        else if(SGN_APK.equals(appName)){
            code = SGN_CODE;
        }
        Log.i(TAG, "setRequestCode : "+code);
        return code;
    }
    // apk 설치 위해 asset 에 있는 파일 복사
    public boolean copyApkFromAssets(String appName) {

        boolean copyIsFinish = false;
        try {
            InputStream is = getAssets().open(appName);
            File file = new File(FILE_PATH+"/"+appName);
            file.createNewFile();

            FileOutputStream fos = new FileOutputStream(file);
            byte[] temp = new byte[1024];
            int i = 0;
            while ((i = is.read(temp)) > 0) {
                fos.write(temp, 0, i);
            }
            fos.close();
            is.close();
            copyIsFinish = true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return copyIsFinish;
    }


    public String getInstallMessage(String packageName){
        String message = "";

        if((ClientUtil.SSM_INSTALLER_PACKAGE).equals(packageName)){
            message = getString(R.string.ssmUnInstalled);
        }
        else if((ClientUtil.SSM_PACKAGE).equals(packageName)){
            message = "";
        }
        else if((ClientUtil.V3_PACKAGE).equals(packageName)){
            message = getString(R.string.V3UnInstalled);
        }
        else if((ClientUtil.SGN_PACKAGE).equals(packageName)){
            message = getString(R.string.installsslvpn);
           // SpannableStringBuilder sb = new SpannableStringBuilder(getString(R.string.installsslvpn));
           // sb.setSpan(new ForegroundColorSpan(Color.parseColor("#FF0000")), 1, 5, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        return message;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case SSM_INSTALLER_CODE :
                Log.d(TAG, "SSM_INSTALLER_CODE RESULT CODE : "+resultCode);
                if(resultCode == RESULT_OK){
                    install(SSM_INSTALLER_APK);
                }
                break;
            case V3_CODE :
                Log.d(TAG, "V3_CODE RESULT CODE : "+resultCode);
                if(resultCode == RESULT_OK){
                    install(V3_APK);
                }
                break;
            case SGN_CODE:
                Log.d(TAG, "SGN_CODE RESULT CODE : "+resultCode);
                if(resultCode == RESULT_OK){
                    install(SGN_APK);
                }
                break;

        }


    }
}
