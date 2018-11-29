package com.ex.group.board.receiver;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;

import com.ex.group.folder.R;
import com.ex.group.board.activity.BoardApplication;
import com.ex.group.board.activity.BoardListActivity;
import com.ex.group.board.constants.Constants.RequestCode;
import com.ex.group.board.custom.CustomLog;
import com.ex.group.board.data.C2dmData;
import com.skt.pe.common.exception.SKTException;
import com.skt.pe.common.primitive.Primitive;
import com.skt.pe.common.service.Controller;
import com.skt.pe.common.service.XMLData;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

public class C2DMReceiver extends BroadcastReceiver {
	static String registration_id = null;
	private NotificationManager nm;
	private PendingIntent pendingIntent;
	
	@Override
	public void onReceive(Context context, Intent intent) {
		CustomLog.L("TEST", "C2DM Receiver enterred");
		
		if (intent.getAction().equals("com.google.android.c2dm.intent.REGISTRATION")) {
			handleRegistration(context, intent);
		} else if (intent.getAction().equals("com.google.android.c2dm.intent.RECEIVE")) {
			handleMessage(context, intent);
		}
	}

	private void handleRegistration(Context context, Intent intent) {
		registration_id = intent.getStringExtra("registration_id");
		CustomLog.L("TEST", "registration_id====>"+registration_id);
	     
		if (intent.getStringExtra("error") != null) {
			CustomLog.L("TEST", "C2DM register fail. try again later.");
		} else if (intent.getStringExtra("unregistered") != null) {
			CustomLog.L("TEST", "C2DM unRegistered...");
		} else if (registration_id != null) {
			CustomLog.L("TEST","registration_id complete!!");
			// 여기에서 GW서버에게 등록된 ID를 전송해야한다.
			// Backgroud로 처리가 필요함
			CustomLog.L("C2DMReceiver", "registration_id " + registration_id);
			TokenPrimitive tPrim = new TokenPrimitive();
			tPrim.setToken(registration_id);
			ExecutePrimitiveThread thread = new ExecutePrimitiveThread(context, tPrim, null, 0);
			thread.start();
		}
	}
	
	private void handleMessage(Context context, Intent intent) {
		String c2dm_msg = intent.getExtras().getString("msg");
		String bType = intent.getExtras().getString("bType");
		String bName = intent.getExtras().getString("bName");
		C2dmData c2dmData = new C2dmData();
		CustomLog.L("TEST", "c2dm_msg======> " + c2dm_msg);
		CustomLog.L("TEST", "bType======> " + bType);
		CustomLog.L("TEST", "bName======> " + bName);
		try {			
			c2dm_msg = URLDecoder.decode(c2dm_msg, "utf-8");
			bType = URLDecoder.decode(bType, "utf-8");
			bName = URLDecoder.decode(bName, "utf-8");
			
			c2dmData.setMsg(c2dm_msg);
			c2dmData.setbType(bType);
			c2dmData.setbName(bName);
			((BoardApplication)context.getApplicationContext()).setC2dmData(c2dmData);
			
			CustomLog.L("TEST", "c2dm_msg======> " + c2dm_msg);
			CustomLog.L("TEST", "bType======> " + bType);
			CustomLog.L("TEST", "bName======> " + bName);
			CustomLog.L("TEST", c2dmData.toString());
			
			nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
			nm.cancel(RequestCode.NotiRegCod);
			
			Intent i = new Intent(context, BoardListActivity.class);
			i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP| Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
			i.putExtra("bType", bType);
			i.putExtra("bName", bName);
			i.putExtra("msg", c2dm_msg);
		    pendingIntent = PendingIntent.getActivity(context, 0,	i, 0);
		    
			String title = (("".equals(c2dm_msg)||c2dm_msg==null) ? "새로운 게시물이 등록되었습니다." : c2dm_msg);
			Notification notification = new Notification(R.drawable.board_icon, bName +" 알림", System.currentTimeMillis() + 100);
			notification.defaults |= (Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE);
			notification.flags = notification.flags	| Notification.FLAG_AUTO_CANCEL;
			//notification.setLatestEventInfo(context, "[hi-moffice "+bName +" 알림]", title, pendingIntent);

			nm.notify(RequestCode.NotiRegCod, notification);
			
			
			/*Intent is = new Intent(context, BoardAlertActivity.class);
			is.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
			is.putExtra("bType", bType);
			is.putExtra("bName", bName);
			is.putExtra("msg", c2dm_msg);
		    PendingIntent intent2 =  PendingIntent.getActivity(context, 0,	is, 0);
		    
			try {
				intent2.send();
			} catch (CanceledException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}*/

		} catch (UnsupportedEncodingException e) {
			CustomLog.L("TEST", "Decode Fail");
		}
		

	}
	
	
	
	public class ExecutePrimitiveThread extends Thread {
		private int what;
		private Handler handler;
		private Primitive primitive;
		private Context context;
		
		public ExecutePrimitiveThread(Context context, Primitive primitive, Handler handler, int what) {
			this.primitive = primitive;
			this.context = context;
			this.what = what;
			this.handler = handler;
		}
		@Override
		public void run() {
			Controller controller = new Controller(context);
			Message msg = Message.obtain();
			msg.what = this.what;
			try {
				XMLData result = controller.request(primitive.getParameters(), false, primitive.getUrlPath());
				primitive.convertXML(result);
				msg.obj = primitive;
			} catch (SKTException e) {
				msg.obj = e;
			}
			if (handler != null) 
				handler.sendMessage(msg);
		}
	}
}
