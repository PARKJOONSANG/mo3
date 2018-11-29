package com.ex.group.mail.activity;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.ex.group.folder.R;
import com.ex.group.mail.data.UserListData;

import java.util.ArrayList;

import com.ex.group.folder.R;
public class EmailAddJobDialog extends Dialog implements android.view.View.OnClickListener{
	String TAG = "EmailAddJobDialog";
	public Context context;
	private TextView tv_job1;
	private TextView tv_job2;
	private Button[] radioBtn;
	private Button btn_jobConfirm;
	ArrayList<UserListData> userList;
	String selectedJob = "";
	String deptNm = "";
	public EmailAddJobDialog(Context context) {
		super(context);
		this.context = context;
	}
	public EmailAddJobDialog(Context context, ArrayList<UserListData> userList) {
		super(context);
		this.context = context;
		this.userList = userList;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		WindowManager.LayoutParams lpWindow = new WindowManager.LayoutParams();
		lpWindow.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
		lpWindow.dimAmount = 0.8f;
		getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
		getWindow().setAttributes(lpWindow);
		
		setContentView(R.layout.mail_job_dialog);
//		selectedJob = job1;
		tv_job1 = (TextView) findViewById(R.id.job1);
		tv_job2 = (TextView) findViewById(R.id.job2);
		radioBtn = new Button[2];
		radioBtn[0] = (Button) findViewById(R.id.check1);
		radioBtn[1] = (Button) findViewById(R.id.check2);
		btn_jobConfirm = (Button) findViewById(R.id.btn_jobConfirm);
		
		tv_job1.setText(userList.get(0).getDeptName());
		tv_job2.setText(userList.get(1).getDeptName());
		
		tv_job1.setOnClickListener(this);
		tv_job2.setOnClickListener(this);
		radioBtn[0].setOnClickListener(this);
		radioBtn[1].setOnClickListener(this);
		btn_jobConfirm.setOnClickListener(this);
		deptNm = userList.get(1).getDeptName();
		selectedJob = userList.get(1).getId();
	}
	
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.job1:
			toggleBtn((Button)radioBtn[0]);
			selectedJob = userList.get(0).getId();
			deptNm = userList.get(0).getDeptName();
			break;
		case R.id.job2:
			toggleBtn((Button)radioBtn[1]);
			selectedJob = userList.get(1).getId();
			deptNm = userList.get(1).getDeptName();
			break;
		case R.id.check1:
			toggleBtn((Button)v);
			selectedJob = userList.get(0).getId();
			deptNm = userList.get(0).getDeptName();
			break;
		case R.id.check2:
			toggleBtn((Button)v);
			selectedJob = userList.get(1).getId();
			deptNm = userList.get(1).getDeptName();
			break;
		case R.id.btn_jobConfirm:
			dismiss();
			break;

		default:
			break;
		}
	}
	public String getDeptNm(){
		return deptNm;
	}
	public String getJob(){
		return selectedJob;
	}
	
	public void toggleBtn(Button v){
		radioBtn[0].setBackgroundResource(R.drawable.mail_radio_off);
		radioBtn[1].setBackgroundResource(R.drawable.mail_radio_off);
		v.setBackgroundResource(R.drawable.mail_radio_on);
	}
	
	
}
