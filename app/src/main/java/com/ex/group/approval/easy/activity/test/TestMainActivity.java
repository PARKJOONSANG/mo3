package com.ex.group.approval.easy.activity.test;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.ex.group.folder.R;
import com.ex.group.approval.easy.constant.ApprovalConstant;
import com.ex.group.approval.easy.primitive.ApprovePrimitive;
import com.ex.group.approval.easy.primitive.CountPrimitive;
import com.ex.group.approval.easy.primitive.DetailPrimitive;
import com.ex.group.approval.easy.primitive.DraftFormPrimitive;
import com.ex.group.approval.easy.primitive.DraftPrimitive;
import com.ex.group.approval.easy.primitive.LineListPrimitive;
import com.ex.group.approval.easy.primitive.ListPrimitive;
import com.ex.group.approval.easy.primitive.OptionListPrimitive;
import com.ex.group.approval.easy.primitive.PrivateLineListPrimitive;
import com.ex.group.approval.easy.primitive.VacCodePrimitive;
import com.skt.pe.common.activity.PECommonActivity;
import com.skt.pe.common.dialog.DialogButton;
import com.skt.pe.common.exception.SKTException;
import com.skt.pe.common.primitive.Primitive;

public class TestMainActivity extends PECommonActivity implements OnClickListener {
	EditText t1, t2, t3, t4, t5;
	CountPrimitive cp = new CountPrimitive();
	ListPrimitive lp = new ListPrimitive();
	LineListPrimitive llp = new LineListPrimitive();
	DetailPrimitive dp = new DetailPrimitive();
	ApprovePrimitive ap = new ApprovePrimitive();
	OptionListPrimitive olp = new OptionListPrimitive();
	DraftPrimitive draftp = new DraftPrimitive();
	VacCodePrimitive vcp = new VacCodePrimitive(null);
	DraftFormPrimitive dfp = new DraftFormPrimitive();
	PrivateLineListPrimitive pllp = new PrivateLineListPrimitive();
	
	@Override
	protected int assignLayout() {
		return R.layout.easy_test_main;
	}
	
	@Override
    public void onCreateX(Bundle savedInstanceState) {
    	Button btn1 = (Button) findViewById(R.id.button1);
    	btn1.setOnClickListener(this);
    	
    	Button btn2 = (Button) findViewById(R.id.button2);
    	btn2.setOnClickListener(this);
    	
    	Button btn3 = (Button) findViewById(R.id.button3);
    	btn3.setOnClickListener(this);
    	
    	Button btn4 = (Button) findViewById(R.id.button4);
    	btn4.setOnClickListener(this);
    	
    	Button btn5 = (Button) findViewById(R.id.button5);
    	btn5.setOnClickListener(this);
    	
    	Button btn6 = (Button) findViewById(R.id.button6);
    	btn6.setOnClickListener(this);
    	
    	Button btn7 = (Button) findViewById(R.id.button7);
    	btn7.setOnClickListener(this);
    	
    	Button btn8 = (Button) findViewById(R.id.button8);
    	btn8.setOnClickListener(this);
    	
    	Button btn9 = (Button) findViewById(R.id.button9);
    	btn9.setOnClickListener(this);
    	
    	Button btn10 = (Button) findViewById(R.id.button10);
    	btn10.setOnClickListener(this);
    	
    	Button btn11 = (Button) findViewById(R.id.button11);
    	btn11.setOnClickListener(this);
    	
    	t1 = (EditText) findViewById(R.id.text1);
    	t2 = (EditText) findViewById(R.id.text2);
    	t3 = (EditText) findViewById(R.id.text3);
    	t4 = (EditText) findViewById(R.id.text4);
    	t5 = (EditText) findViewById(R.id.text5);
    }

	@Override
	protected void onReceive(Primitive primitive, SKTException e) {
		if (e == null) {
			Intent intent = new Intent(this, TestResultActivity.class);
			intent.putExtra("primitive", primitive);
			startActivity(intent);
		} else {
			e.alert(this, new DialogButton(0) {
				public void onClick(DialogInterface dialog, int which) {
				}
			});
		}
	}

	@Override
	public void onClick(View v) {
		Primitive prim = null;
		
		String[] args = new String[5];
		args[0] = t1.getText().toString();
		args[1] = t2.getText().toString();
		args[2] = t3.getText().toString();
		args[3] = t4.getText().toString();
		args[4] = t5.getText().toString();
		
		switch (v.getId()) {
			case R.id.button1:
//				cp.setSystemType(ApprovalConstant.SystemType.EASY);
				cp.setProtID("RpcMobileSancWaitReviewRecvCount");
				prim = cp;
				break;
			case R.id.button2:
				lp.setDocType("0");
				lp.setPage(1);
				prim = lp;
				break;
			case R.id.button3:
				llp.setSystemType(ApprovalConstant.SystemType.EASY);
				llp.setWdid("521418000");
				llp.setDocHref("201109,521418000,yonginner");
				prim = llp;
				break;
			case R.id.button4:
				dp.setDocID("521418000");
				prim = dp;
				break;
			case R.id.button5:
				prim = dfp;
				break;
			case R.id.button6:
				break;
			case R.id.button7:
				ap.setAction(ApprovalConstant.Action.APPROVE);
//				ap.setOpinion("의견입니다.");
//				ap.setSystemType(ApprovalConstant.SystemType.EASY);
//				ap.setDocID("712040099");
//				ap.setDocHref("201104,712040099,gvinner");
				prim = ap;
				break;
			case R.id.button8:
				olp.setDocHref("201104,712040099,gvinner");
				olp.setSystemType(ApprovalConstant.SystemType.EASY);
				olp.setWdid("712040099");
				prim = olp;
				break;
			case R.id.button9:
				draftp.setSystemType(ApprovalConstant.SystemType.EASY);
				draftp.setDraftKind("0");
//				draftp.setTargetDate("2011/10/01");
//				draftp.setFromTime("14:30");
//				draftp.setUntilTime("15:30");
				draftp.setFormCode("0");
				draftp.setLocation("testLocation");
				draftp.setDescript("testDescript");
				draftp.setIsOwnCar(true);
				draftp.setCooperator("20609710|19641416");
				prim = draftp;
				break;
			case R.id.button10:
				vcp.setSystemType(ApprovalConstant.SystemType.EASY, null);
				prim = vcp;
				break;
			case R.id.button11:
				prim = pllp;
				break;
		}
//		prim.setParameters(args);
		executePrimitive(prim);
	}

}