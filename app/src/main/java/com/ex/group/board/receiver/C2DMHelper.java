package com.ex.group.board.receiver;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.ex.group.board.custom.CustomLog;

public class C2DMHelper {
	private final static String emailOfSender = "incross.superb@gmail.com";
	
	public static void registC2DM(Context context) {
		Intent registrationIntent = new Intent("com.google.android.c2dm.intent.REGISTER");
		registrationIntent.putExtra("app", PendingIntent.getBroadcast(context, 0, new Intent(), 0));
		registrationIntent.putExtra("sender", emailOfSender);
		registrationIntent.setPackage("com.ex.group.board");
		
		CustomLog.L("TEST", "Gmail address: " + emailOfSender);
		context.startService(registrationIntent);
	}
	
	public static void unRegistC2DM(Context context) {
		Intent unregIntent = new Intent("com.google.android.c2dm.intent.UNREGISTER");
		unregIntent.putExtra("app", PendingIntent.getBroadcast(context, 0, new Intent(), 0));
		context.startService(unregIntent);
	}
}
