package com.ex.group.approval.easy.activity.test;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.ex.group.folder.R;
import com.skt.pe.common.activity.PECommonActivity;
import com.skt.pe.common.primitive.Primitive;

public class TestResultActivity extends PECommonActivity {

	@Override
	protected int assignLayout() {
		// TODO Auto-generated method stub
		return R.layout.easy_testresult;
	}
	
	@Override
    public void onCreateX(Bundle savedInstanceState) {
    	TextView parameter = (TextView) findViewById(R.id.testresult_textview_parameter);
    	TextView result = (TextView) findViewById(R.id.testresult_textview_result);
    	Intent intent = getIntent();
    	Primitive prim = (Primitive) intent.getSerializableExtra("primitive");
    	parameter.setText(prim.getParameters().toString());
    	result.setText(prim.toString());
    }

}
