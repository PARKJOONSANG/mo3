package com.ex.group.mail.addressbook.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.ex.group.mail.addressbook.data.EmployeeData;

import java.util.ArrayList;

import com.ex.group.folder.R;

public class SelectJobDialog extends Dialog implements android.view.View.OnClickListener{
	
	Context mContext;
	private TextView job1;
	private TextView job2;
	private Button[] check;
	private Button btn_confirm;

	ArrayList<EmployeeData> userList;
	String userID = "";
	
	public SelectJobDialog(Context context, ArrayList<EmployeeData> dupUserList) {
		super(context);
		this.mContext = context;
		this.userList = dupUserList;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState){
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		WindowManager.LayoutParams lpWindow = new WindowManager.LayoutParams();
		lpWindow.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
		lpWindow.dimAmount = 0.8f;
		getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
		getWindow().setAttributes(lpWindow);
		
		setContentView(R.layout.mail_member_dupjobdialog);
		
		initUI();
		
		job1.setText(userList.get(1).m_szDepartment);
		job2.setText(userList.get(0).m_szDepartment);
		
		userID = userList.get(1).m_szEmpId;
	}
	public void initUI(){
		job1 = (TextView)findViewById(R.id.job1);
		job2 = (TextView)findViewById(R.id.job2);
		check = new Button[2];
		check[0] = (Button)findViewById(R.id.check1);
		check[1] = (Button)findViewById(R.id.check2);
		btn_confirm = (Button)findViewById(R.id.btn_confirm);
		
		job1.setOnClickListener(this);
		job2.setOnClickListener(this);
		check[0].setOnClickListener(this);
		check[1].setOnClickListener(this);
		btn_confirm.setOnClickListener(this);
		
	}


	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.job1:
			toggle(check[0]);
			userID = userList.get(1).m_szEmpId;
			break;
		case R.id.job2:
			toggle(check[1]);
			userID = userList.get(0).m_szEmpId;
			break;
		case R.id.check1:
			toggle(check[0]);
			userID = userList.get(1).m_szEmpId;
			break;
		case R.id.check2:
			toggle(check[1]);
			userID = userList.get(0).m_szEmpId;
			break;
		
		case R.id.btn_confirm:
			Toast.makeText(mContext, "userID,...."+userID, Toast.LENGTH_SHORT).show();
			dismiss();
			break;
			
		default:
			break;
		}
	}
	public String getUserID(){
		return userID;
	}
	
	public void toggle(Button v){
		check[0].setBackgroundResource(R.drawable.mail_member_radio_off);
		check[1].setBackgroundResource(R.drawable.mail_member_radio_off);
		v.setBackgroundResource(R.drawable.mail_member_radio_on);
	}
	
	
	

}
