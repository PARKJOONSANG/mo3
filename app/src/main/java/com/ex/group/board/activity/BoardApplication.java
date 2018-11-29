package com.ex.group.board.activity;

import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.ex.group.board.custom.CustomLog;
import com.ex.group.board.data.C2dmData;

public class BoardApplication extends Application {
	
	private C2dmData c2dmData;
	
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		InitApp();
	}
	
	public void InitApp(){
		c2dmData = new C2dmData();
	}	
	
	public C2dmData getC2dmData() {
		if(c2dmData == null) c2dmData = new C2dmData();
		SharedPreferences setting = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
		c2dmData.setMsg(setting.getString(C2dmData.KeyBmsg, ""));
		c2dmData.setbType(setting.getString(C2dmData.KeyBType, ""));
		c2dmData.setbName(setting.getString(C2dmData.KeyBName, ""));
		//setC2dmData(new C2dmData());
		return c2dmData;
	}

	public void setC2dmData(C2dmData c2dmData) {
		SharedPreferences setting = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
		SharedPreferences.Editor editor = setting.edit();
		CustomLog.L("BoardApplication", c2dmData.toString());
		editor.putString(C2dmData.KeyBmsg, c2dmData.getMsg());
		editor.putString(C2dmData.KeyBType, c2dmData.getbType());
		editor.putString(C2dmData.KeyBName, c2dmData.getbName());
		editor.commit();	
	}


}
