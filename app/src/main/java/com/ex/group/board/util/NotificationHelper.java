package com.ex.group.board.util;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.ex.group.folder.R;
import com.ex.group.board.activity.NullActivity;

public class NotificationHelper {
	public static void notify(Context context, int notiId, String notiTicker, String notiTitle, String notiMessage) {
		NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
		
		Notification noti = new Notification(R.drawable.board_icon, notiTicker, System.currentTimeMillis());
		
		noti.defaults |= (Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE);
		noti.flags |= Notification.FLAG_AUTO_CANCEL;
		
		Intent intent = new Intent(context, NullActivity.class);
//		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//		intent.putExtra(IntentConstants.TAB_ID, TabIds.TAB_MENTION);
		
		PendingIntent content = PendingIntent.getActivity(context, 0, intent, 0);
		//noti.setLatestEventInfo(context, notiTitle, notiMessage, content);
		
		manager.notify(notiId, noti);
	}
}
