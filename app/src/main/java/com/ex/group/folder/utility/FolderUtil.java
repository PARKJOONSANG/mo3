package com.ex.group.folder.utility;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;

import com.sktelecom.ssm.lib.SSMLib;
import com.sktelecom.ssm.remoteprotocols.ResultCode;
import com.ex.group.folder.R;

import static com.ex.group.folder.utility.ClientUtil.SSM_APP;

public class FolderUtil implements ResultCode {
	SSMLib ssmLib;

	public static Drawable getStateDrawable(Drawable iconSelect, Drawable iconDeSelect) {
		if(iconSelect == null || iconDeSelect == null) {
			return null;
		}
		StateListDrawable d = new StateListDrawable();
		
		d.addState(new int[]{android.R.attr.state_focused}, iconSelect);
		d.addState(new int[]{android.R.attr.state_pressed}, iconSelect);
		d.addState(new int[]{android.R.attr.state_selected}, iconSelect);
		d.addState(new int[]{}, iconDeSelect);
		
		return d;
	}
	public static boolean isRooted(){
		boolean isRootingFlag = false;
		try {
			Runtime.getRuntime().exec("su");
			isRootingFlag = true;
		} catch ( Exception e){
			isRootingFlag = false;
		}
		return isRootingFlag;
	}

	public SSMLib initSSMLib(Context context){
		if(ssmLib == null){
			ssmLib = SSMLib.getInstance(context);
			LogMaker.logmaking("SSMLINITILIZE","NOT_OK");
		}
		if(ssmLib.initialize() != OK){
			ssmLib.initialize();
			LogMaker.logmaking("SSMLINITILIZE","OK");
		}
		return ssmLib;
	}

	/****
	 *
	 * @param code
	 * @param appName
	 * @return ssm, v3 인증 상태 등에 따른 리턴 메시지
	 */
	public static String getMessage(Context context, int code, String appName){
		String msg = "";
		switch (code) {
//            case NOT_INSTALLED:
//                if((SSM_APP).equals(appName)){
//                    msg = getString(R.string.ssmUnInstalled);
//                }
//                else{
//                    msg = getString(R.string.V3UnInstalled);
//                }
//                break;

			case UNREGISTERED:
				if(SSM_APP.equals(appName)){
					msg = context.getString(R.string.ssmUnRegistered);
				}
				else{
					msg = context.getString(R.string.V3Failed);
				}
				break;
			case NOT_INSTALLED:
				UNREGISTERED:
				if(SSM_APP.equals(appName)){
					msg = context.getString(R.string.ssmNotInstalled);
				}
				else{
					msg = context.getString(R.string.V3NotInstalled);
				}
				break;


			case OLD_VERSION_INSTALLED:
				msg = context.getString(R.string.ssmOldVersion);
				break;

			case NO_PERMISSION:
				msg = context.getString(R.string.ssmNoPermission);
				break;


            case ERROR_CONNECTION:

				if(SSM_APP.equals(appName)){
					msg = context.getString(R.string.ssmConnFailed);
				}
				else{
					msg = context.getString(R.string.V3ConnFailed);
				}
                break;

            case FAILED:

				if(SSM_APP.equals(appName)){
					msg = context.getString(R.string.ssmFailed);
				}
				else{
					msg = context.getString(R.string.V3Failed);
				}
                break;

            case ERROR:

				if(SSM_APP.equals(appName)){
					msg = context.getString(R.string.ssmExecuteFailed);
				}
				else{
					msg = context.getString(R.string.V3ExecuteFailed);
				}
                break;
		}
		return msg;
	}
}
