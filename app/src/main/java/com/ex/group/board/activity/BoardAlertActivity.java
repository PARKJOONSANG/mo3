package com.ex.group.board.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import com.ex.group.board.constants.Constants.RequestCode;
import com.ex.group.board.custom.CustomLog;
import com.ex.group.board.data.C2dmData;
import com.ex.group.folder.R;

public class BoardAlertActivity extends Activity {
	private Context context = this;
	private String tag = this.getClass().getName().toString();
	private Intent intent;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.board_alert_activity);
		intent = getIntent();
		if(intent != null && intent.getStringExtra("bType")!= null && !"".equals(intent.getStringExtra("bType"))){
			CustomLog.L(tag, intent.getStringExtra("bType"));
			CustomLog.L(tag, "c2dmData.getbType() " + intent.getStringExtra("bType"));
			
			C2dmData c2dmData = ((BoardApplication)getApplicationContext()).getC2dmData();
			CustomLog.L(tag, "c2dmData.getbType() num2 " + c2dmData.getbType());
			String msg = (("".equals(c2dmData.getMsg())||c2dmData.getMsg()==null) ? "새로운 게시물이 등록되었습니다." : c2dmData.getMsg());
			BoardAlertC2dm("[hi-moffice "+c2dmData.getbName() +" 알림]", msg);
		}else{
			((Activity)context).finish();
		}
		
	}
	
	public void BoardAlertC2dm(String title, String msg){
		AlertDialog.Builder builder = new AlertDialog.Builder(context)
		//.setIcon(R.drawable.icon)
		.setTitle(title)
		.setMessage(msg)
		.setCancelable(false)
		.setPositiveButton("보기", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				NotificationManager nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
				nm.cancel(RequestCode.NotiRegCod);
				Intent i = new Intent(context, BoardListActivity.class);
				i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP| Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
				C2dmData c2dmData = ((BoardApplication)getApplicationContext()).getC2dmData();
				i.putExtra("bType", c2dmData.getbType());
				i.putExtra("bName", c2dmData.getbName());
				i.putExtra("msg", c2dmData.getMsg());
				context.startActivity(i);
				/*nm.cancelAll();*/
				dialog.dismiss();
				((Activity)context).finish();
			}
		})
		.setNegativeButton("닫기", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				dialog.dismiss();
				((Activity)context).finish();
			}
		});
		AlertDialog dialog = builder.create();
		dialog.show();
	}

}
