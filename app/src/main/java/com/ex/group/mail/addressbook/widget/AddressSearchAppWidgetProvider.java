package com.ex.group.mail.addressbook.widget;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.view.View;
import android.widget.RemoteViews;

import com.ex.group.mail.addressbook.activity.WidgetSearchActivity;

import com.ex.group.folder.R;

public class AddressSearchAppWidgetProvider extends AppWidgetProvider {
	private final static String PACKAGE_NAME = "com.skt.pe.membersearch";
	private final static String CLASS_NAME =
			"com.skt.pe.membersearch.widget.MemberSearchAppWidgetProvider";
	private final static String SEARCH_ACTION = "com.skt.pe.membersearch.widget.SEARCH_ACTION";
	private final static String DISPLAYUPDATE_TICK =
			"com.skt.pe.membersearch.widget.DISPLAYUPDATE_TICK";
	public final static String SHOW_ACTION = "com.skt.pe.membersearch.widget.SHOW_ACTION";
	
	/* (non-Javadoc)
	 * @see android.appwidget.AppWidgetProvider#onUpdate(android.content.Context, android.appwidget.AppWidgetManager, int[])
	 */
	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager,
                         int[] appWidgetIds) {
		// TODO Auto-generated method stub
		for(int id: appWidgetIds) {
			updateDisplay(context, appWidgetManager, id);
		}
		Intent displayIntent = new Intent(context, MemberDisplayUpdateService.class);
		context.startService(displayIntent);
		super.onUpdate(context, appWidgetManager, appWidgetIds);
	}

	/* (non-Javadoc)
	 * @see android.appwidget.AppWidgetProvider#onReceive(android.content.Context, android.content.Intent)
	 */
	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		if(intent.getAction().equals(SEARCH_ACTION)) {
			RemoteViews views = new RemoteViews(context.getPackageName(),
					R.layout.mail_member_membersearch_appwidget);
			AppWidgetManager manager = AppWidgetManager.getInstance(context);
			views.setViewVisibility(R.id.ALL, View.INVISIBLE);
			manager.updateAppWidget(manager.getAppWidgetIds(new ComponentName(context, getClass())),
					views);
			Intent intent1 = new Intent(context, WidgetSearchActivity.class);
			intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			context.startActivity(intent1);
		} else if(intent.getAction().equals(SHOW_ACTION)) {
			showLayout(context);
		} else if(intent.getAction().equals(DISPLAYUPDATE_TICK)) {
			Intent serviceIntent = new Intent(context, MemberDisplayUpdateService.class);
			context.startService(serviceIntent);
		}
		super.onReceive(context, intent);
	}
	
	/**
	 * 구성원 검색 위젯을 클릭하면 위젯을 안보이게 한 후에
	 * Activity를 빠져 나온 후 다시 보이도록 동작
	 * @param context Context
	 */
	private void showLayout(Context context) {
		RemoteViews views = new RemoteViews(context.getPackageName(),
				R.layout.mail_member_membersearch_appwidget);
		AppWidgetManager manager = AppWidgetManager.getInstance(context);
		views.setViewVisibility(R.id.ALL, View.VISIBLE);
		manager.updateAppWidget(manager.getAppWidgetIds(new ComponentName(context, getClass())),
				views);
	}
	
	/**
	 * 화면을 업데이트한다.
	 * @param context context
	 * @param manager AppWidgetManager
	 * @param id AppWidget Id
	 */
	public static void updateDisplay(Context context, AppWidgetManager manager, int id) {
		RemoteViews views = new RemoteViews(context.getPackageName(),
				R.layout.mail_member_membersearch_appwidget);
		Intent intent = new Intent(SEARCH_ACTION);
		PendingIntent pIntent = PendingIntent.getBroadcast(context, 0, intent, 0);
		views.setOnClickPendingIntent(R.id.TEXT_INPUT, pIntent);
		views.setOnClickPendingIntent(R.id.BUTTON_INPUT, pIntent);
		manager.updateAppWidget(id, views);
	}
	
	public static class MemberDisplayUpdateService extends Service {
		private final static int UPDATE_INTERVAL = 500;
		
		@Override
		public IBinder onBind(Intent arg0) {
			// TODO Auto-generated method stub
			return null;
		}

		/* (non-Javadoc)
		 * @see android.app.Service#onStartCommand(android.content.Intent, int, int)
		 */
		@Override
		public int onStartCommand(Intent intent, int flags, int startId) {
			// TODO Auto-generated method stub
			Context context = getBaseContext();
			AppWidgetManager manager = AppWidgetManager.getInstance(context);
			ComponentName name = new ComponentName(PACKAGE_NAME, CLASS_NAME);
			int[] m_nAppWidgetIds = manager.getAppWidgetIds(name);
			
			for(int appWidgetId: m_nAppWidgetIds) {
				AddressSearchAppWidgetProvider.updateDisplay(context, manager, appWidgetId);
			}
			
			Intent alarmIntent = new Intent(DISPLAYUPDATE_TICK);
			PendingIntent pIntent = PendingIntent.getBroadcast(context, 0, alarmIntent, 0);
			AlarmManager alarmManager =
					(AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
			alarmManager.set(AlarmManager.RTC, System.currentTimeMillis() + UPDATE_INTERVAL,
					pIntent);
			return super.onStartCommand(intent, flags, startId);
		}
	}
}