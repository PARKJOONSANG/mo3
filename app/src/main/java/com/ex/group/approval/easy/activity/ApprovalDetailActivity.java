package com.ex.group.approval.easy.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ex.group.folder.R;
import com.ex.group.approval.easy.activity.helper.ApprovalTypeHelper;
import com.ex.group.approval.easy.constant.ApprovalConstant;
import com.ex.group.approval.easy.domain.SancLine;
import com.ex.group.approval.easy.primitive.ApprovePrimitive;
import com.ex.group.approval.easy.primitive.DetailPrimitive;
import com.skt.pe.common.activity.ifaces.CommonUI;
import com.skt.pe.common.dialog.DialogButton;
import com.skt.pe.common.exception.SKTException;
import com.skt.pe.common.primitive.Primitive;

public class ApprovalDetailActivity extends ApprovalCommonActivity implements OnClickListener, CommonUI {
	private String TAG = "ApprovalDetailActivity";
	private DetailPrimitive dPrim = null;
	private ApprovePrimitive aPrim = new ApprovePrimitive();
	private ApprovalTypeHelper approvalHelper = null;
//	private LocalActivityManager laManager = new LocalActivityManager(this, true);
	
	@Override
	protected int assignLayout() {
		return R.layout.easy_approval_detail;
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}
	
	@Override
    public void onCreateX(Bundle savedInstanceState) {
//		laManager.dispatchCreate(savedInstanceState);
//		setSubTitle("결재 문서 타이틀 | 기안자");
		
		Intent intent = getIntent();
		
		this.dPrim = (DetailPrimitive) intent.getSerializableExtra(IntentArg.PRIMITIVE);
		approvalHelper = (ApprovalTypeHelper) intent.getSerializableExtra(IntentArg.APPROVAL_TYPE);
		
		aPrim.setDocID(dPrim.getDocID());
//		aPrim.setDocHref(dPrim.getDocHref());
		
		initUI();
    }
	
	public boolean onCreateOptionsMenu(Menu menu) {
		return false;
	}
	
	@Override
	protected void onReceive(Primitive primitive, SKTException e) {
		super.onReceive(primitive, e);
		if (e == null) {
			if (primitive instanceof ApprovePrimitive) {
				ApprovePrimitive aPrim = (ApprovePrimitive) primitive;
				alert(approvalHelper.getDialogMessage(aPrim.getAction()), new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						finish();
					}
				});
			}
		} else {
			e.alert(this, new DialogButton(0) {
				public void onClick(DialogInterface dialog, int which) {
				}
			});
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.approval_detail_button_ok:
				approvalHelper.startButton1Action(ApprovalDetailActivity.this, dPrim, aPrim);
				break;
			case R.id.approval_detail_button_cancel:
				approvalHelper.startButton2Action(ApprovalDetailActivity.this, dPrim, aPrim);
				break;
			case R.id.approval_detail_button_attachment:
				super.viewAttachFile(dPrim.getAttachFileList());
				break;
		}
	}
	
	private final int TableMaxLength = 6;
	private void setApprovalTable() {
		int sancLineSize = this.dPrim.getSancLineList().size();
		int tableHeaderId;
		int tableBodyId;
		TextView header = null;
		TextView body = null;
		SancLine sancLine = null;
		
		int tableStart = TableMaxLength - sancLineSize;
		
		for (int i=0; i<TableMaxLength; i++) {
			tableHeaderId = getTableHeaderId(i);
			tableBodyId = getTableBodyId(i);
			
			header = (TextView) findViewById(tableHeaderId);
			body = (TextView) findViewById(tableBodyId);
			
			// 보스리스트의 항목이 존재하면...
			if (i >= tableStart) {
				sancLine = this.dPrim.getSancLineList().get(i-tableStart);
				header.setText(sancLine.getBossPosition());
				if ("기안".equals(sancLine.getSancStatus()) || "승인".equals(sancLine.getSancStatus())) 
					body.setText(sancLine.getBossName());
			} else {
				header.setVisibility(View.GONE);
				body.setVisibility(View.GONE);
			}
		}
	}
	
	private int getTableHeaderId(int index) {
		int id = 0;
		
		switch (index) {
			case 0:
				id = R.id.approval_detail_textview_table_header_1;
				break;
			case 1:
				id = R.id.approval_detail_textview_table_header_2;
				break;
			case 2:
				id = R.id.approval_detail_textview_table_header_3;
				break;
			case 3:
				id = R.id.approval_detail_textview_table_header_4;
				break;
			case 4:
				id = R.id.approval_detail_textview_table_header_5;
				break;
			case 5:
				id = R.id.approval_detail_textview_table_header_6;
				break;
		}
		return id;
	}
	
	private int getTableBodyId(int index) {
		int id = 0;
		
		switch (index) {
			case 0:
				id = R.id.approval_detail_textview_table_body_1;
				break;
			case 1:
				id = R.id.approval_detail_textview_table_body_2;
				break;
			case 2:
				id = R.id.approval_detail_textview_table_body_3;
				break;
			case 3:
				id = R.id.approval_detail_textview_table_body_4;
				break;
			case 4:
				id = R.id.approval_detail_textview_table_body_5;
				break;
			case 5:
				id = R.id.approval_detail_textview_table_body_6;
				break;
		}
		return id;
	}
	
	@Override
	public void initUI() {
		/*
		 * 간이결 본문보기가 이미지뷰어에서 웹뷰어로 바뀌게 되어 해당 코드는 사용되지 않음
		 *
		LinearLayout ll = (LinearLayout) findViewById(R.id.approval_detail_layout_imageview);
		Intent ivIntent = ActivityLauncher.makeImageViewIntent(this, "ABC.hwp", "89", 0.4f);
		Window win = laManager.startActivity("ImageView", ivIntent);
		View imageView = win.getDecorView();
		ll.addView(imageView);
 		*/
		TextView title = (TextView) findViewById(R.id.approval_detail_textview_title);
		TextView writer = (TextView) findViewById(R.id.approval_detail_textview_writer);
		
		Button button1 = (Button) findViewById(R.id.approval_detail_button_ok);
		Button button2 = (Button) findViewById(R.id.approval_detail_button_cancel);
		Button button3 = (Button) findViewById(R.id.approval_detail_button_attachment);
		
		DisplayMetrics metrics = getApplicationContext().getResources().getDisplayMetrics();
		
//		button1.setText(approvalHelper.getButton1Name());
//		button2.setText(approvalHelper.getButton2Name());
//		if (dPrim.getAttachFileList().size() > 0)
//			button3.setText("첨부파일(" + dPrim.getAttachFileList().size() + ")");
		button1.setBackgroundResource(getButtonResId(approvalHelper.getButton1Name()));
		if("목록".equals(approvalHelper.getButton2Name())) {
			LinearLayout.LayoutParams params = (LinearLayout.LayoutParams)button2.getLayoutParams();
			params.width = (int) ((metrics.density * 30f) + 0.5f);
			button2.setLayoutParams(params);
		} 
		button2.setBackgroundResource(getButtonResId(approvalHelper.getButton2Name()));
		if (dPrim.getAttachFileList().size() > 0) {
			button3.setBackgroundResource(this.getResId("btn_approval_attach_file_n", "drawable"));
		} else {
			button3.setEnabled(false);
			button3.setBackgroundResource(this.getResId("btn_approval_attach_file_d", "drawable"));
		}
		
		button1.setOnClickListener(this);
		button2.setOnClickListener(this);
		button3.setOnClickListener(this);
		
		title.setText(dPrim.getTitle());
		writer.setText(dPrim.getDrafter());
		
		WebView webView = (WebView) findViewById(R.id.approval_detail_webview_content);
		webView.getSettings().setJavaScriptEnabled(true);
		webView.getSettings().setBuiltInZoomControls(true);	// 화면 확대/축소 버튼 여부
		webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true); // javascript가 window.open()을 사용할 수 있도록 설정
		webView.getSettings().setPluginState(WebSettings.PluginState.ON_DEMAND); // 플러그인을 사용할 수 있도록 설정
		webView.getSettings().setSupportMultipleWindows(true); // 여러개의 윈도우를 사용할 수 있도록 설정
		webView.getSettings().setLightTouchEnabled(true);
		webView.getSettings().setSavePassword(true);
		webView.getSettings().setSaveFormData(true);
		webView.setWebViewClient(new ApprovalDetailActivityWebViewClient());
//		webView.loadUrl("http://128.200.121.68");		// WebView 테스트용 IP
//		webView.loadUrl("http://172.16.90.11:8090" + dPrim.getContentUrl());			// 개발서버 IP 추가 (contentUrl이 <contentUrl>/nanum/wflow/test/report.html</contentUrl> 이와 같이 넘어오기 때문에 추가함)
//		webView.loadUrl("http://nedms.ex.co.kr:8090" + dPrim.getContentUrl());			// 운영서버 IP 추가 (contentUrl이 <contentUrl>/nanum/wflow/test/report.html</contentUrl> 이와 같이 넘어오기 때문에 추가함)
		webView.loadUrl(ApprovalConstant.Detail.WEBVIEW_BASE_URL + dPrim.getContentUrl());
//		webView.loadDataWithBaseURL(null, ApprovalConstant.Detail.WEBVIEW_BASE_URL + dPrim.getContentUrl(), "text/html", "utf-8", null);

//		String data = "<html><body><table width=100% height=100%><tr><td><p class=Approval_Title>근태신청서</p><p class=Approval_SubTitle>작성번호 : 2014-19516018-027</p><p class=Approval_Body>금번 ( 간이결제테스트 ) 로 인하여 (09월 26일 20:00부터 23:00까지) 취업규정 제20조에 의거 ( 외출(공무) )을 신청하오니 승인하여 주시기 바랍니다.</p><p class=Approval_SubTitle>소　　속 : 정보처 정보계획팀</p><p class=Approval_SubTitle>직　　급 : 4 급</p><p class=Approval_SubTitle      >성　　명 : 한도영</p><p class=Approval_BottomSubTitle>2014년 09월 26일</p><p class=Approval_BottomTitle>한국도로공사사장귀하</p></td></tr></table></body></html>";
//		webView.loadData(data, "text/html; charset=UTF-8", null);
		
		/////////////////////////////////////////////////////////////////////////
		
		/*try {
			SKTWebUtil.loadWebViewWithoutZoom(this, webView, true, dPrim.getHtmlInfo().getHtml(), true);
		} catch (SKTException e) {
			e.alert(this, new DialogButton(0) {
				public void onClick(DialogInterface dialog, int which) {
					finish();
				}
			});
		}*/
		setApprovalTable();
		
		approvalHelper.initUI(this, R.id.approval_detail_button_cancel, dPrim);
	}
	
	public int getButtonResId(String buttonName) {
		if("결재 승인".equals(buttonName)) {
			return getResId("btn_approval_ok", "drawable");
		}
		else if("결재 반려".equals(buttonName)) {
			return getResId("btn_approval_cancel", "drawable");
		}
		else if("결재 회수".equals(buttonName)) {
			return getResId("btn_approval_recall", "drawable");
		}
		else if("목록".equals(buttonName)) {
			return getResId("btn_approval_list", "drawable");
		}
		else if("상황 보기".equals(buttonName)) {
			return getResId("btn_approval_condition", "drawable");
		}
		else {
			return 0;
		}
	}

	@Override
	public void resetUI() {
	}

	@Override
	public String validationUI() {
		return null;
	}
	
	public static class IntentArg {
		final static String PRIMITIVE = "INTENT_PRIMITIVE";
		final static String APPROVAL_TYPE = "INTENT_APPROVAL_TYPE";
	}
	
	private class ApprovalDetailActivityWebViewClient extends WebViewClient {
		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			// TODO Auto-generated method stub
//			return super.shouldOverrideUrlLoading(view, url);
			return false;
		}
	}
}