package com.ex.group.board.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.ex.group.folder.R;
import com.ex.group.board.constants.IntentConstants;
import com.ex.group.board.util.NotificationHelper;
import com.skt.pe.common.data.SKTUtil;

/**
 *
 */
public class PushReceiver extends BroadcastReceiver {
	private final static int defaultNotiId = 99912;
	@Override
	public void onReceive(Context context, Intent intent) {
		SKTUtil.log("TEST", "Board Push Arrived");
		if(IntentConstants.Action.PUSH_PARAMETER.equals(intent.getAction())) {
			String appId = intent.getStringExtra(IntentConstants.Key.APPID);
			String notiTicker = intent.getStringExtra(IntentConstants.Key.TICKER);
			String notiTitle = intent.getStringExtra(IntentConstants.Key.TITLE);
			String notiMessage = intent.getStringExtra(IntentConstants.Key.MESSAGE);
//			String notiParameter = intent.getStringExtra(IntentConstants.Key.PARAMETER);
//			Log.d("TEST", "C2DM Message: ");
//			Log.d("TEST", "notiId: " + notiId);
//			Log.d("TEST", "notiTicker: " + notiTicker);
//			Log.d("TEST", "notiTitle: " + notiTitle);
//			Log.d("TEST", "notiMessage: " + notiMessage);
//			Log.d("TEST", "AppPush Send: " + appId + ", " + notiMessage + ", " + notiParameter);
			
			//
			// appId가 트위터이면...
			// 
			String myAppId = context.getResources().getString(R.string.board_app_id).substring(2);
			SKTUtil.log("TEST", "MyAppId: " + myAppId + ", AppId: " + appId);
			if (myAppId.equals(appId))
				NotificationHelper.notify(context, defaultNotiId, notiTicker, notiTitle, notiMessage);
		}
	}
}
