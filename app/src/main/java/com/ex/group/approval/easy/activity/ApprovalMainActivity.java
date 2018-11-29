package com.ex.group.approval.easy.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ListView;

import com.ex.group.folder.R;
import com.ex.group.approval.easy.activity.helper.ApprovalTypeHelper;
import com.ex.group.approval.easy.adapter.MainListAdapter;
import com.ex.group.approval.easy.constant.ApprovalConstant;
import com.ex.group.approval.easy.constant.UserInfo;
import com.ex.group.approval.easy.domain.MainListItem;
import com.ex.group.approval.easy.primitive.CountPrimitive;
import com.ex.group.approval.easy.primitive.DraftFormPrimitive;
import com.ex.group.approval.easy.primitive.ListPrimitive;
import com.ex.group.approval.easy.primitive.VacCodePrimitive;
import com.skt.pe.common.data.AuthData;
import com.skt.pe.common.data.SKTUtil;
import com.skt.pe.common.dialog.DialogButton;
import com.skt.pe.common.exception.SKTException;
import com.skt.pe.common.primitive.Primitive;

import java.util.HashMap;
import java.util.Map;

public class ApprovalMainActivity extends ApprovalCommonActivity implements AdapterView.OnItemClickListener, OnClickListener {
	private final String TAG = "ApprovalMainActivity";
	private MainListAdapter adapter = null;
	private MainListItem[] mainListItems = new MainListItem[4];
	
	UserInfo userInfo;
	
	
	
	@Override
	protected int assignLayout() {
		return R.layout.easy_main;
	}
	
	@Override
    public void onCreateX(Bundle savedInstanceState) {
		setTitle("간이결재");
		
		// 2015-06-26 Join 추가 - 결재선 지정 시 첫 번째 줄은 기안자 자신으로 설정되게 하기 위해 추가
		userInfo = UserInfo.getInstance();
		
		try {
			Map<String, String> hm = new HashMap<String, String>();
			hm = SKTUtil.getGMPAuth(ApprovalMainActivity.this);
			Log.d(TAG, "사번 ==================== " + hm.get(AuthData.ID_ID));
			
			if(hm != null && hm.get(AuthData.ID_ID) != null && !"".equals(hm.get(AuthData.ID_ID))) {
				userInfo.setUserID(hm.get(AuthData.ID_ID));
				//userInfo.setUserID("19117410");
//				userInfo.setUserID("20609710");

			}
			
		} catch(Exception e2) {
			e2.printStackTrace();
		}
		// 2015-06-26 Join 추가 끝
		
		CountPrimitive cPrim = new CountPrimitive();
		executePrimitive(cPrim);

		findViewById(R.id.approval).setOnClickListener(this);		
		
//		initUI();
    }
	
	@Override
	protected void onReceive(Primitive primitive, SKTException e) {
		super.onReceive(primitive, e);
		
		if (e == null) {
			if (primitive instanceof CountPrimitive) {
				CountPrimitive cPrim = (CountPrimitive) primitive;
				
				Log.d(TAG, "이름 ==================== " + cPrim.getUser_name());
				Log.d(TAG, "직급 ==================== " + cPrim.getUser_role());
				Log.d(TAG, "부서 ==================== " + cPrim.getUser_dept());
				
				userInfo.setUserName(cPrim.getUser_name());
				userInfo.setUserRole(cPrim.getUser_role());
				userInfo.setUserSuffixDept(cPrim.getUser_dept());
				
				initUI(cPrim.getSanc_wait());
				
			} else if (primitive instanceof ListPrimitive) {
				ListPrimitive lPrim = (ListPrimitive) primitive;
				
				Intent intent = new Intent(ApprovalMainActivity.this, ApprovalListActivity.class);
				intent.putExtra(ApprovalListActivity.IntentArg.TITLE_NAME, (String)lPrim.getTag(ListPrimitive.Extras.APPROVAL_TITLE));
				intent.putExtra(ApprovalListActivity.IntentArg.PRIMITIVE, lPrim);
				
				startActivityForResult(intent, ApprovalConstant.RequestCode.REFRESH);
			}
		} else {
			e.alert(this, new DialogButton(0) {
				public void onClick(DialogInterface dialog, int which) {
					finish();
				}
			});
		}		
	}
	
	/**
	 * onClick 이벤트 핸들러<br>
	 */
	@Override
	public void onClick(View v) {
		if(v.getId() == R.id.approval) {
			String title = "결재 신청하기";
			int stringArrayId = R.array.easyaproval_common_approval_type;
			super.selectSingleChoiceDialog(title, stringArrayId, 0, approvalSelectListener, new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					Intent intent = null;
					Primitive prim = null;
					switch (getSelectApprovalType()) {
						case OptionMenuSelectValue.OUTSIDE_FORM:
							intent = new Intent(ApprovalMainActivity.this, ApplyOutsideApprovalActivity.class);
							DraftFormPrimitive dformPrim = new DraftFormPrimitive();
							dformPrim.setTag(ApprovalConstant.Tags.INTENT, intent);
							prim = dformPrim;
							break;
							
						case OptionMenuSelectValue.VACATION_FORM:
//							Toast.makeText(ApprovalMainActivity.this, "사용 가능한 휴가 정보가 존재하지 않습니다.", Toast.LENGTH_SHORT).show();
							intent = new Intent(ApprovalMainActivity.this, ApplyVacationApprovalActivity.class);
							VacCodePrimitive vcPrim = new VacCodePrimitive(ApprovalMainActivity.this);
							vcPrim.setTag(ApprovalConstant.Tags.INTENT, intent);
							prim = vcPrim;
							break;
					}
					if (intent != null) {
						executePrimitive(prim);
					}
				}
			}, null);			
		}
	}	
	
	@Override
	protected void onRefresh() {
		super.onRefresh();
		CountPrimitive cPrim = new CountPrimitive();
		executePrimitive(cPrim);
	}

	private void initUI(int count1) {
		String[] mainListViewTitles = getResources().getStringArray(R.array.easyaproval_main_listview_itemtitle);
		int[] mainListViewImages = {
				R.drawable.easy_main_selector_item01, R.drawable.easy_main_selector_item02, R.drawable.easy_main_selector_item03, R.drawable.easy_main_selector_item04
		};
		
		ListView mainList = (ListView) findViewById(R.id.main_listview_main);
		adapter = new MainListAdapter(this, R.layout.easy_main_listview_item);
		
		for (int i=0; i<mainListViewTitles.length; i++) {
			mainListItems[i] = new MainListItem();
			mainListItems[i].imageId = mainListViewImages[i];
			mainListItems[i].title = mainListViewTitles[i];
			if (i == 0)
				mainListItems[i].newItemCount = count1;
			else
				mainListItems[i].newItemCount = -1;
			adapter.add(mainListItems[i]);
		}
		
		mainList.setAdapter(adapter);
		mainList.setOnItemClickListener(this);
	}
	
	@Override
	public void onItemClick(AdapterView<?> adapter, View view, int position, long id) {
		String docType = null;
		ListPrimitive lPrim = new ListPrimitive();
		// 2014-07-18 Join 수정 : 간이결재 고도화로 인한 수정
		lPrim.setPage(1);
		
		switch (position) {
			// 결재하기
			case 0:
				docType = ApprovalConstant.DocType.APPROVAL_LIST;
				break;
			// 결재 상황보기
			case 1:
				docType = ApprovalConstant.DocType.APPROVAL_ING;
				break;
			// 결재 완료함
			case 2:
				docType = ApprovalConstant.DocType.APPROVAL_DONE;
				break;
			// 반려함
			case 3:
				docType = ApprovalConstant.DocType.APPROVAL_RETURN;
				break;
		}

		lPrim.setDocType(docType);
		lPrim.setTag(ListPrimitive.Extras.APPROVAL_TITLE, mainListItems[position].title);
		lPrim.setTag(ListPrimitive.Extras.APPROVAL_TYPE, ApprovalTypeHelper.Factory.getInstance(docType));
		
		executePrimitive(lPrim);
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		Log.i(TAG, "==================Approval Main onDestroy===================");
//		finish();
	}
	
	// Handler 를 이용하여 처리하기
	private boolean m_Flag = false;
	Handler back_Handler = new Handler(new Callback() {
		public boolean handleMessage(Message msg) {
			if(msg.what == 0) {
				m_Flag = false;
			}
			return true;
		}
	});	

	// 뒤로가기버튼설정
	/*public boolean onKeyDown(int KeyCode, KeyEvent event) {
		super.onKeyDown(KeyCode, event);
		if (event.getAction() == KeyEvent.ACTION_DOWN) {
			switch (KeyCode) {
			case KeyEvent.KEYCODE_BACK: // 뒤로 키와 같은 기능을 한다.
				if (false) {
					Toast.makeText(this, "'뒤로' 버튼을 한번 더 누르시면 종료됩니다.",Toast.LENGTH_SHORT).show();
					// 버튼클릭시 true
					m_Flag = true;
					// Handler 호출 (2초 이후 back_Check 값 false)
					back_Handler.sendEmptyMessageDelayed(0, 2000);
					return false;
				} else {
//					try {
//						SKTUtil.runApp(this, "com.ex.group.folder");
					} catch (Exception e) {
						e.printStackTrace();
					}

//					moveTaskToBack(true);
					finish();
					return true;
				}
			}
			return false;
		}		
		return false;
	}*/		
}