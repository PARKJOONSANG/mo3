package com.ex.group.board.activity;

import android.Manifest;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;

import com.skt.pe.common.activity.SKTActivity;
import com.skt.pe.common.exception.SKTException;
import com.skt.pe.common.service.XMLData;

/**
 * 
 *  <pre>
 *	com.ex.group.board.activity
 *	BoardActivity.java
 *	</pre>
 *
 *	@Author : 박정호
 * 	@E-MAIL : yee1074@innoace.com
 *	@Date	: 2011. 11. 22. 
 *
 *	TODO
 */
public abstract class BoardActivity extends SKTActivity {

	
	@Override
	protected int assignLayout() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	protected XMLData onAction(String primitive, String... args)
			throws SKTException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void onActionPost(String primitive, XMLData result, SKTException e)
			throws SKTException {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected int onActionPre(String primitive) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	protected void onCreateX(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		setInit();
		userPref = getSharedPreferences("USERINFO", MODE_PRIVATE);
		boolean resultPermission =false;
		resultPermission = permissionCheck();

	}
	
	@Override
	protected void onResumeX() {
		// TODO Auto-generated method stub
		super.onResumeX();






	}
	
	public void setInit() {
	
		setLayout();
	}
	
	public void setLayout(){
		
	}
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}
	SharedPreferences userPref;
	public boolean permissionCheck() {
		if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.M){
			if (       checkSelfPermission(android.Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED
					|| checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
					|| checkSelfPermission(Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED
					|| checkSelfPermission(Manifest.permission.WRITE_CONTACTS) != PackageManager.PERMISSION_GRANTED
					|| checkSelfPermission(Manifest.permission.USE_FINGERPRINT) != PackageManager.PERMISSION_GRANTED)
			{
				requestPermissions(permissions, PERMISSIONS_REQUEST_READ_PHONE_STATE);
				return false;
			}
			else {
				if("".equals(userPref.getString("FIRSTRUN",""))){
					requestPermissions(permissions, PERMISSIONS_REQUEST_READ_PHONE_STATE);
					SharedPreferences.Editor edit = userPref.edit();
					edit.putString("FIRSTRUN", "N");
					edit.apply();
				}return true;
			}
		}else{
			return true;
		}
	}
	@Override
	public boolean shouldShowRequestPermissionRationale(@NonNull String permission) {
		return super.shouldShowRequestPermissionRationale(permission);
	}
	public static int PERMISSIONS_REQUEST_READ_PHONE_STATE = 2;
	public static String[] permissions = {
			Manifest.permission.READ_PHONE_STATE,
			Manifest.permission.WRITE_EXTERNAL_STORAGE,
			Manifest.permission.READ_SMS,
			Manifest.permission.WRITE_CONTACTS/*,
														Manifest.permission.REQUEST_INSTALL_PACKAGES*/
	};

	public boolean hasAllPermissionGranted(int[] grantResults) {
		boolean check = false;
		for (int result : grantResults) {
			//   Log.i(TAG, "result..."+result);
			if (result == PackageManager.PERMISSION_DENIED) {
				return false;
			}
		}
		return true;
	}
	@Override
	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
		super.onRequestPermissionsResult(requestCode, permissions, grantResults);

		switch(requestCode){
			case 2:
				Log.v("","=\n\n=======================onRequestPermissionResult=======================");
				if(hasAllPermissionGranted(grantResults)){}

				break;
		}
	}

}
