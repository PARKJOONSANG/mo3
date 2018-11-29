package com.ex.group.mail.addressbook.activity;

import android.app.Activity;
import android.app.LocalActivityManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.RelativeLayout;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TabWidget;

import com.skt.pe.common.activity.SKTActivity;
import com.skt.pe.common.conf.EnvironManager;
import com.skt.pe.common.exception.SKTException;
import com.skt.pe.common.service.XMLData;

import com.ex.group.folder.R;
/**
 * 첫 시작. 탭
 */

public class AddressTabActivity extends SKTActivity implements OnTabChangeListener {
	
	private final String TAG = "AddressTabActivity";

	private TabHost mTabHost;
	private boolean mIsMultiSelect;
	private boolean mIsSingleSelect;
	private String mApprovalType;
	private String mEmpType;
	private String arg1;
	public AddressTabActivity() {
		mLocalActivityManager = new LocalActivityManager(this, true);
	}

	@Override
	protected int assignLayout() {
		return R.layout.mail_member_maintab;
	}

	@Override
	protected void onCreateX(Bundle savedInstanceState)
	{		
		EnvironManager.setNeedEncPwd(true);

		Bundle states = savedInstanceState != null
		? (Bundle)savedInstanceState.getBundle("states") : null;
		mLocalActivityManager.dispatchCreate(states);

		mIsMultiSelect = getIntent().getBooleanExtra("isMultiSelect", false);
		mIsSingleSelect = getIntent().getBooleanExtra("isSingleSelect", false);
		mApprovalType = getIntent().getStringExtra("approvalType");
		mEmpType = getIntent().getStringExtra("empType");
		arg1 = getIntent().getStringExtra("arg1");
		int tabIndex = getIntent().getIntExtra("tab", 0);

		if(tabIndex < 0 || tabIndex > 3) {
			tabIndex = 0;
		}
		if(mIsMultiSelect && tabIndex == 3) {
			tabIndex = 0;
		}

//		TextView tab1 = new TextView(this);
//		tab1.setText(R.string.tab1_label);
//		tab1.setTextSize(12);
//		tab1.setTextColor(getResources().getColorStateList(R.color.tab_text_color_selector));
//		tab1.setGravity(Gravity.CENTER_HORIZONTAL);
//		tab1.setPadding(0, 60,0, 0);
//		tab1.setBackgroundResource(R.drawable.member_tab_back_selector);
//
//		TextView tab2 = new TextView(this);
//		tab2.setText(R.string.tab2_label);
//		tab2.setTextSize(12);
//		tab2.setTextColor(getResources().getColorStateList(R.color.tab_text_color_selector));
//		tab2.setGravity(Gravity.CENTER_HORIZONTAL);
//		tab2.setPadding(0, 60,0, 0);
//		tab2.setBackgroundResource(R.drawable.member_tab_back_selector);

		//TextView tab3 = new TextView(this);
		//tab3.setText(R.string.tab3_label);
		//tab3.setTextSize(12);
		//tab3.setTextColor(getResources().getColorStateList(R.color.tab_text_color));
		//tab3.setGravity(Gravity.CENTER_HORIZONTAL);
		//tab3.setPadding(0, 60,0, 0);
		//tab3.setBackgroundResource(R.drawable.member_find_tab03_selector);
		//
		//TextView tab4 = new TextView(this);
		//tab4.setText(R.string.tab4_label);
		//tab4.setTextSize(12);
		//tab4.setTextColor(getResources().getColorStateList(R.color.tab_text_color));
		//tab4.setGravity(Gravity.CENTER_HORIZONTAL);
		//tab4.setPadding(0, 60,0, 0);
		//tab4.setBackgroundResource(R.drawable.member_find_tab04_selector);

		mTabHost = (TabHost)findViewById(R.id.mainTab);
		mTabHost.setup(mLocalActivityManager);		
		mTabHost.clearAllTabs();
		
		// AddressSearchActivity 구성원, 연락처
		// AddressListActivity 조직도
//		mTabHost.addTab(mTabHost.newTabSpec("tab1").setIndicator(getString(R.string.label_tab1),
		mTabHost.addTab(mTabHost.newTabSpec("tab1").setIndicator("",
				getResources().getDrawable(R.drawable.mail_member_tabicon_group1_selector))
				.setContent(new Intent(this, AddressSearchActivity.class)
				.putExtra("isMultiSelect", mIsMultiSelect)
				.putExtra("isSingleSelect", mIsSingleSelect)
				.putExtra("arg1", arg1)
				.putExtra("approvalType", mApprovalType).putExtra("empType", mEmpType)));
		

//		mTabHost.addTab(mTabHost.newTabSpec("tab2").setIndicator(getString(R.string.label_tab2),
		mTabHost.addTab(mTabHost.newTabSpec("tab2").setIndicator("",
				getResources().getDrawable(R.drawable.mail_member_tabicon_group2_selector))
				.setContent(new Intent(this, AddressListActivity.class)
				.putExtra("isMultiSelect", mIsMultiSelect)
				.putExtra("isSingleSelect", mIsSingleSelect)
				.putExtra("arg1", arg1)
				.putExtra("approvalType", mApprovalType).putExtra("empType", mEmpType)));
		
//		if(!mIsSingleSelect) 
//		{
/*			2015-03-19 Join 수정 시작 - 전자문서 고도화로 인하여 그룹목록 서비스 중지에 따른 주석처리
			mTabHost.addTab(mTabHost.newTabSpec("tab4").setIndicator(getString(R.string.label_tab3),
					getResources().getDrawable(R.drawable.member_tabicon_group3_selector))
					.setContent(new Intent(this, AddressSearchActivity.class)
					.putExtra("type", "E")
					.putExtra("isMultiSelect", mIsMultiSelect)
					.putExtra("isSingleSelect", mIsSingleSelect)
					.putExtra("arg1", arg1)
					.putExtra("approvalType", mApprovalType).putExtra("empType", mEmpType)));
			2015-03-19 Join 수정 끝
					*/
//		}
 
		TabWidget widget = mTabHost.getTabWidget();
		float iScale = getResources( ).getDisplayMetrics( ).density;
		
		
		//galaxy S4 용 Tab 텍스트 사이즈 조정
		Context context = getApplicationContext();
		Display display = ((WindowManager)context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
//		Log.d(TAG, "iScale =========== " + iScale);
		Log.d(TAG, "display.getWidth() ======= " + display.getWidth() + "\ndisplay.getHeight() ======= " + display.getHeight());
		
		if(display.getWidth() == 1080 && display.getHeight() == 1920)		iScale = (float) (iScale*0.80);
		
		//갤럭시탭 10.1 용 텍스트 사이즈 조정
		String deviceName = Build.DEVICE;
//		Log.d(TAG, "deviceName =========== " + deviceName);
		
		if(deviceName != null && deviceName.contains("SHW-M380") || deviceName.contains("SHW-M460")) {
			iScale = (float) (iScale*3.00);
//			Log.d(TAG, deviceName + " iScale ========= " + iScale);
		}
		
		for(int i = 0; i < widget.getChildCount(); i++)
		{			
			
			RelativeLayout layout = (RelativeLayout)widget.getChildAt(i);
			// 2015-04-10 Join 수정
//			layout.setBackgroundResource(R.drawable.member_tab_back_selector);
			layout.setBackgroundColor(Color.rgb(245, 245, 245));		// android:background="#F5F5F5" 와 같음
			
			//갤럭시탭 10.1 용 사이즈 조정 (배경)
			if(deviceName != null && deviceName.contains("SHW-M380") || deviceName.contains("SHW-M460")){
				/*if(i  != 1){
					layout.setLayoutParams(new TabWidget.LayoutParams(267,100));	
				}else{
					layout.setLayoutParams(new TabWidget.LayoutParams(266,100));
				}*/
				layout.setLayoutParams(new TabWidget.LayoutParams(display.getWidth() / widget.getChildCount(),100));
			}
			
			ImageView iv = (ImageView)layout.getChildAt(0);
			RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams)iv.getLayoutParams();
			params.topMargin = 5; //(int)(14 / iScale);
			params.bottomMargin = 5;
			//갤럭시탭 10.1 용 사이즈 조정 이미지크기, 위치
			if(deviceName != null && deviceName.contains("SHW-M380") || deviceName.contains("SHW-M460")){
				params.width = display.getWidth() / widget.getChildCount();
				params.height = 70;
				params.topMargin = 10;
				params.bottomMargin = 10;
			}
			else {
				params.width = display.getWidth() / widget.getChildCount();
			}
			
			iv.setScaleType(ScaleType.FIT_XY);
			iv.setLayoutParams(params);

			/*
			TextView tv = (TextView)layout.getChildAt(1);
//			Log.d(TAG, "iScale2222222222 =========== " + (int) (4 * iScale));
			// 2015-03-06 Join 수정 시작 - 갤럭시 노트4용 텍스트 사이즈 조정
			if(!(display.getWidth() == 1440 && display.getHeight() == 2560)) {
				tv.setTextSize((int) (8 * iScale));
			}
			else {
				tv.setTextSize((int) (4 * iScale));
			}
			// 2015-03-06 Join 수정 끝
			tv.setTextColor(getResources().getColorStateList(R.color.btn_text_color_selector));
			params = (RelativeLayout.LayoutParams)tv.getLayoutParams();
			params.bottomMargin = 10; //(int) (10 / iScale);
			//갤럭시탭 10.1 용 사이즈 조정 (텍스트위치)
			if(deviceName != null && deviceName.contains("SHW-M380") || deviceName.contains("SHW-M460")){
				params.bottomMargin = 15;
			}
			
			tv.setLayoutParams(params);
			*/			
			// 2015-04-10 Join 수정
		}		

		mTabHost.setCurrentTab(tabIndex);
		mTabHost.setOnTabChangedListener(this);
	}

	public TabHost getTabHost() {
		return mTabHost;
	}

	@Override
	protected XMLData onAction(String primitive, String... args)throws SKTException {return null;}
	@Override
	protected void onActionPost(String primitive, XMLData result, SKTException e) {}
	@Override
	protected int onActionPre(String primitive) {return 0;}

	public Activity getCurrentActivity() {
		return mLocalActivityManager.getCurrentActivity();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if(mLocalActivityManager != null) {
			mLocalActivityManager.dispatchDestroy(isFinishing());
		}		
	}

	@Override
	protected void onPause() {
		super.onPause();
		if(mLocalActivityManager != null) {
			mLocalActivityManager.dispatchPause(isFinishing());
		}
	}

	@Override
	protected void onResumeX() {
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
		if(mLocalActivityManager != null) {
			mLocalActivityManager.dispatchResume();
		}
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		if(mLocalActivityManager != null) {
			Bundle state = mLocalActivityManager.saveInstanceState();
			if(state != null) {
				outState.putBundle("states", state);
			}
		}
	}

	@Override
	protected void onStop() {
		super.onStop();
		if(mLocalActivityManager != null) {
			mLocalActivityManager.dispatchStop();
		}
	}

	public void onTabChanged(String tabId) {
		if(!mIsMultiSelect) {
			Intent intent = new Intent(AddressSearchActivity.LAST_SEARCH_UPDATE);
			sendBroadcast(intent);
		}
	}

//	// Handler 를 이용하여 처리하기
//	private boolean m_Flag = false;
//	Handler back_Handler = new Handler(new Callback() {
//		public boolean handleMessage(Message msg) {
//			if(msg.what == 0) {
//				m_Flag = false;
//			}
//			return true;
//		}
//	});	
//
//
//	// 뒤로가기버튼설정
	public boolean onKeyDown(int KeyCode, KeyEvent event) {
		
		super.onKeyDown(KeyCode, event);
//		if (event.getAction() == KeyEvent.ACTION_DOWN) {
//			switch (KeyCode) {
//			case KeyEvent.KEYCODE_BACK: // 뒤로 키와 같은 기능을 한다.
//				if (!m_Flag) {
//					Toast.makeText(this, "'뒤로' 버튼을 한번 더 누르시면 종료됩니다.",
//							Toast.LENGTH_SHORT).show();
//					// 버튼클릭시 true
//					m_Flag = true;
//					// Handler 호출 (0.5초 이후 back_Check 값 false)
//					back_Handler.sendEmptyMessageDelayed(0, 1000);
//					return false;
//				} else {
//					
//					try{
//						SKTUtil.runApp(this, "com.ex.group.folder");	
//					}catch(Exception e){
//						e.printStackTrace();
//					}						
//					
//					moveTaskToBack(true);
//					finish();
//					return true;
//				}
//			}
//			return false;
//		}		
		return false;
	}		
	
	
}
