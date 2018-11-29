package com.ex.group.mail.addressbook.activity;

//한독

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.ex.group.mail.addressbook.data.MemberSearchSQLite;
import com.ex.group.mail.addressbook.data.MinistryData;
import com.ex.group.mail.addressbook.data.TreeData;
import com.skt.pe.common.conf.Constants;
import com.skt.pe.common.data.SKTUtil;
import com.skt.pe.common.exception.SKTException;
import com.skt.pe.common.service.Controller;
import com.skt.pe.common.service.Parameters;
import com.skt.pe.common.service.XMLData;
import com.skt.pe.util.StringUtil;
import com.skt.pe.util.XMLUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.ex.group.folder.R;
/**
 * 부서 목록 화면
 * @author jokim
 *
 */
public class AddressListActivity extends BaseActivity implements OnItemClickListener,
        OnClickListener, OnItemLongClickListener {
	
	
    private ArrayList<MinistryData> mCurTeamData = new ArrayList<MinistryData>();
    private ArrayList<MinistryData> mLastTeamData = new ArrayList<MinistryData>();
    private ArrayList<MinistryData> mTempTeamData = new ArrayList<MinistryData>();

    private LinearLayout mUpButton;
    private LinearLayout mLinearLayout;
    private LinearLayout eLinearLayout;
    private LinearLayout mSearchbg_view;
//    private LinearLayout mLogoLayout;
    private TextView mUpButtonText;
    private TextView mSuffixDeptText;
    private ImageView tempView;
    int mLongIndex;
    
    TreeData temTreepData;
    String myDeptCode = "";
    String tempTitle = null;
    String tempCompany = null;
    String tempCompanyCd = null;
    String tempCompanyNm = null;
    String tempParentTitle = null;
    String tempDeptCode = null;
    String tempPretempDeptCode = "";
    String tempParentDeptCode = null;
    String tempParenttempDeptCode = "";
    String tempMemberCnt = "0";
    String treeTempDeptCode = "";
    String treeTempParentDeptCode = "";
    String tempReqCompanyCd = "";
    boolean m_bIsChild = false;
    boolean mIsTreeView = false;
    boolean mIsBackOff = false;			// 트리로 가서 트리 클릭 후 리스트로 왔을 때 back으로 트리로 돌아가도록
    boolean mIsLastView = false;
    boolean mIsMultiSelect = false;
    boolean mIsSingleSelect = false;
    boolean mIsAddTop = true;   // 트리보기 선택시 처음에 한국도로공사 추가
    boolean top = false;
    private ListView mListView;
    private ListView mTreeView;
//    private ListView mLastView;
    private TreeArrayAdapter mTreeAdapter;
    private TreeArrayAdapter mCopyTreeAdapter;
    MemberSearchSQLite m_dbHelper = null;
    SQLiteDatabase m_dbMember = null;
    
    final int PEOPLE_DEPT = 1;
    final int PEOPLE_DEPT_ADD = 2;
    final int PEOPLE_MY_COMPANY = 3;
    final int PEOPLE_COMPANY = 4;
    final int PEOPLE_DEPT1 = 5;
    
    final int DIALOG_MEMBER_LIST = 1111;
    final int DIALOG_NO_APP = 1113;
    final int DIALOG_SEND_FAIL = 2222;
    final int DIALOG_NO_MEMBER= 3333;
    final int DIALOG_MEMBER_SEARCH = 4444;
    
    final int LAST_VIEW = 0;
    final int SEND_SMS = 1;
    final int SEND_EMAIL = 2;
    final int CHANGE_VIEW_TYPE = 3;
    private final int CALL_MOBILE = 4;
    private final int VIEW_CONTENT = 8;
    
    final int C_LAST_VIEW = 10;
    final int C_SEND_SMS = 11;
    final int C_SEND_EMAIL = 12;
    final int C_CHANGE_VIEW_TYPE = 13;
    private final int C_CALL_MOBILE = 14;
    private final int C_VIEW_CONTENT = 18;
    
    final int LIST_LAYOUT = 0;
    final int TREE_LAYOUT = 1;
    final int LAST_LAYOUT = 2;
    
    private String mApprovalType;
    private String mEmpType;
    private String tempArg1 = "";
    
    private static final String TOP_DEPTCODE = "N00001";
    private int mMemberCnt = 0;
    private int mTeamCnt = 0;
    
    private Map<String, String> dataName = new HashMap<String, String>();
    private Map<String, String> dataEmpId = new HashMap<String, String>();
    private Map<String, String> dataVvip = new HashMap<String, String>();
    private Map<String, String> dataNumber = new HashMap<String, String>();
    private Map<String, String> dataEmail = new HashMap<String, String>();
    private Map<String, String> dataDept = new HashMap<String, String>();
    
	@Override
	protected int assignLayout() {
		return R.layout.mail_member_memberlist;
	}

    @Override
    public void onCreateX(Bundle savedInstanceState) {
    	super.onCreateX(savedInstanceState);        
        onPostCreateX();
    }
    
	protected void onPostCreateX() 
	{
		mIsMultiSelect = getIntent().getBooleanExtra("isMultiSelect", false);
		mIsSingleSelect = getIntent().getBooleanExtra("isSingleSelect", false);
		mApprovalType = getIntent().getStringExtra("approvalType");
		mEmpType = getIntent().getStringExtra("empType");
		tempArg1 = getIntent().getStringExtra("arg1");
		
		mListView = (ListView)findViewById(R.id.list);
		mTreeView = (ListView)findViewById(R.id.tree_layout);
//		mLastView = (ListView)findViewById(R.id.last_view);
		mTreeAdapter = new TreeArrayAdapter(this);
		mCopyTreeAdapter = new TreeArrayAdapter(this);
		mTreeView.setAdapter(mTreeAdapter);
		mTreeView.setOnItemClickListener(this);
		mListView.setOnItemLongClickListener(this);
        if(mIsMultiSelect) {
        	findViewById(R.id.multiselect_footer).setVisibility(View.VISIBLE);
        	findViewById(R.id.multiselect_confirm).setOnClickListener(this);
        	findViewById(R.id.multiselect_cancel).setOnClickListener(this);
        } else {
        	findViewById(R.id.multiselect_footer).setVisibility(View.GONE);
        }
		
		mLinearLayout = (LinearLayout)findViewById(R.id.up_button_layout);
	
		mSearchbg_view = (LinearLayout)findViewById(R.id.searchbg_view);
		mUpButton = (LinearLayout)findViewById(R.id.up_button);
		mUpButtonText = (TextView)findViewById(R.id.up_button_text);
		mSuffixDeptText = (TextView)findViewById(R.id.suffix_dept_text);
		
		try {
			tempReqCompanyCd = SKTUtil.getCheckedCompanyCd(this);
		} catch (SKTException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if(mIsMultiSelect || mIsSingleSelect) {
			mIsTreeView = false;
			mIsBackOff = false;
			requestData(PEOPLE_DEPT, "");
		} else {
			mIsTreeView = true;
			mIsBackOff = true;
			changeViewType();
		}
		registerForContextMenu(mListView);
		
	}
	
	public String getCompanyName() {
		String ret = "";
		
		try {
			String code = SKTUtil.getCheckedCompanyCd(this);
			Map<String, String> map = SKTUtil.getCompanyList(this);
			
			ret = map.get(code);
		} catch(SKTException e) {
			e.printStackTrace();
		}
		
		return ret;
	}
    
    @Override
	public boolean onCreateOptionsMenu(Menu menu)
    {
    	menu.add(0, LAST_VIEW, 0, R.string.mail_label_view_mygroup);
    	menu.add(0, CHANGE_VIEW_TYPE, 0, R.string.mail_view_tree);
    	menu.add(0, SEND_SMS, 0, R.string.mail_send_group_sms);
    	menu.add(0, SEND_EMAIL, 0, R.string.mail_send_group_email);
    	
		return super.onCreateOptionsMenu(menu);
	}
	
	/* (non-Javadoc)
	 * @see com.skt.pe.common.activity.SKTActivity#onPrepareOptionsMenu(android.view.Menu)
	 */
	@Override
	public boolean onPrepareOptionsMenu(Menu menu)
	{
		MenuItem item = menu.findItem(CHANGE_VIEW_TYPE);
		
//		if(mIsLastView) {
//			menu.findItem(LAST_VIEW).setTitle(R.string.viewmyteam);
//		} else {
//			menu.findItem(LAST_VIEW).setTitle(R.string.viewlast);
//		}
		
		if(mIsTreeView) {
			menu.findItem(LAST_VIEW).setVisible(true);
			menu.findItem(CHANGE_VIEW_TYPE).setVisible(false);
		} else {
			menu.findItem(LAST_VIEW).setVisible(true);
			menu.findItem(CHANGE_VIEW_TYPE).setVisible(true);
		}
		
		if(mIsTreeView) {
			item.setTitle(R.string.mail_label_view_mygroup);
		} else {
			item.setTitle(R.string.mail_view_tree);
		}
		
		if(mIsTreeView) {
			menu.findItem(SEND_SMS).setVisible(false);
			menu.findItem(SEND_EMAIL).setVisible(false);
		} else {
			menu.findItem(SEND_SMS).setVisible(true);
			menu.findItem(SEND_EMAIL).setVisible(true);
		}
		
		return super.onPrepareOptionsMenu(menu);
	}
	
	/* (non-Javadoc)
	 * 목록 Long Press 이벤트 핸들러
	 * @see android.widget.AdapterView.OnItemLongClickListener#onItemLongClick(android.widget.AdapterView, android.view.View, int, long)
	 */
	public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int arg2,
                                   long arg3) {
			mLongIndex = arg2;
			return mListView.showContextMenu();
	}
	
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenuInfo menuInfo) {
		
		MinistryData data = (MinistryData) mListView.getItemAtPosition(mLongIndex);
		SKTUtil.log(Log.DEBUG, "xxxxxxx", "xxxxxxxxxx" + data.isMember );
		if(!data.isMember) {
			return;
		}
		if (!StringUtil.isNull(data.m_szCellPhoneNo)) {
			menu.add(0, C_CALL_MOBILE, 0, R.string.mail_callMobile);
		}
//			if (!StringUtil.isNull(data.m_szOfficePhoneNo)) {
//				menu.add(0, CALL_OFFICE, 0, R.string.callOffice);
//			}
		if (!StringUtil.isNull(data.m_szCellPhoneNo)) {
			menu.add(0, C_SEND_SMS, 0, R.string.mail_sendSMS);
		}
		if (!StringUtil.isNull(data.m_szEmail)) {
			menu.add(0, C_SEND_EMAIL, 0, R.string.mail_sendEmail);
		}
		menu.add(0, C_VIEW_CONTENT, 0, R.string.mail_viewContent);
		super.onCreateContextMenu(menu, v, menuInfo);
	}
	
	@Override
	public boolean onContextItemSelected(MenuItem item)
	{
SKTUtil.log(Log.DEBUG, "xxxxxxx", "xxxxxxxxxxxxx   onContextItemSelected "  );
		Intent intent = null;
		MinistryData data = (MinistryData) mListView.getItemAtPosition(mLongIndex);
		
		if(item.getItemId() == C_CALL_MOBILE) {
			
			Uri uri = Uri.parse("tel:" + data.m_szCellPhoneNo);
			intent = new Intent(Intent.ACTION_DIAL, uri);
			startActivity(intent);
			return true;
		} else if(item.getItemId() == C_SEND_SMS) {
			Uri uri = Uri.parse("sms:" + data.m_szCellPhoneNo);
            intent = new Intent(Intent.ACTION_SENDTO, uri);
            startActivity(intent);
			return true;
		} else if(item.getItemId() == C_SEND_EMAIL) {
			if(	"".equals(data.m_szEmail)) {
				m_szTitle = getString(R.string.mail_text_error);
				m_szDialogMessage = getString(R.string.mail_no_email);
				showDialog(DIALOG_MEMBER_SEARCH);
				return true;
			}
			try {
				intent = new Intent(ACTION_EMAIL_CLIENT);
				SKTUtil.log(Log.DEBUG, "xxxxxxxxxx", "xxxxxxxxxxxx ACTION_EMAIL_CLIENT : " + ACTION_EMAIL_CLIENT);
				String[] empname = new String[1];
				String[] empEmail = new String[1];
				String[] empVvip = new String[1];
				
				empname[0] = data.m_szName;
				empEmail[0] = data.m_szEmail;
				empVvip[0] = data.m_szVvip;
				intent.putExtra("wtype", "urlforward");
//				if (!"E".equals(mType) ) {
//					intent.putExtra("toText", data.m_szInnerMail);
//				}else{
//					intent.putExtra("toText", data.m_szEmpId);
//				}
//				approvalIntent.addCategory(Intent.CATEGORY_LAUNCHER);
				intent.addCategory(intent.CATEGORY_DEFAULT);
				intent.putExtra("type", "side");
				intent.putExtra("names", empname);
				intent.putExtra("emails", empEmail);				            
				intent.putExtra("vvip", empVvip);	
				startActivity(intent);
			} catch(Exception e) {
				e.printStackTrace();
				m_szTitle = getString(R.string.mail_title_no_mail);
				m_szDialogMessage = getString(R.string.mail_no_emailclient);
				showDialog(DIALOG_NO_APP);
			}
			return true;

		} else if(item.getItemId() == C_VIEW_CONTENT) {
			SKTUtil.log(Log.DEBUG, "xxxxxxx", "xxxxxxxxxxxxx   onContextItemSelected 4444"  );
			intent = new Intent(this, AddressInfoActivity.class);
			intent.putExtra("type", "M");
			/*
			if(!mIsLastView) {
	    		intent.putExtra("type", mType);
	    		Log.e("aaaaaaaaaaaaaaa  VIEW_CONTENT", mType);
        	} else {
	    		intent.putExtra("type", data.m_szType);
    	    	intent.putExtra("isLast", true);
        	}
        	*/
			
	    	intent.putExtra("companyCode", data.m_szCompanyCd);
        	intent.putExtra("employee", data.m_szSerialNo);
        	intent.putExtra("deptCode", data.m_szDepartment);
			startActivityForResult(intent, 1);
			return true;
		}
		
		return super.onContextItemSelected(item);
	}
	
	/* (non-Javadoc)
	 * 옵션 메뉴 클릭 이벤트 핸들러<br>
	 * - 최근 검색 보기<br>
	 * - 문자 보내기<br>
	 * - 이메일 보내기<br>
	 * - 트리/목록 보기
	 * @see android.app.Activity#onMenuItemSelected(int, android.view.MenuItem)
	 */
	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
SKTUtil.log(Log.DEBUG, "xxxxxxxxxxx", "xxxxxxxxxxx  onMenuItemSelected " );
		if(item.getItemId() == LAST_VIEW) {
//			if(mIsLastView) {
				showLayout(LIST_LAYOUT);
				mIsLastView = false;
				mIsTreeView = false;
				try {
					tempReqCompanyCd = SKTUtil.getCheckedCompanyCd(this);
					requestData(PEOPLE_DEPT, "");
				} catch (SKTException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
//			} else {
//				getLastSearch();
//			}
		} else if(item.getItemId() == SEND_SMS || item.getItemId() == SEND_EMAIL) {
			Intent intent = new Intent(this, SendGroupMessageActivity.class);
			SKTUtil.log(Log.DEBUG, "xxxxx", "xxxxxxxxxx myDeptCode ::   "+ myDeptCode);
			if (item.getItemId() == SEND_SMS) {
				intent.putExtra("mType", "sms");	
			}else{
				intent.putExtra("mType", "email");
			}
			intent.putExtra("sendType", item.getItemId());
			intent.putExtra("deptCode", myDeptCode);
			intent.putExtra("appType", "MEMBERSEARCH");
			startActivityForResult(intent, 4444);
		}
/*		else if(item.getItemId() == SEND_EMAIL  || item.getItemId() == SEND_SMS ) {
    		Intent intent = new Intent(AddressListActivity.this, AddressMultiSelectActivity.class);
    		intent.putExtra("orgChart", true);
    		int max = 0;
//    		if(mIsLastView) {
//    			max = mLastTeamData.size() - 1;
//    		} else {
//    			if(item.getItemId() == SEND_SMS && m_bIsChild) {
    		for (int i = 0; i < mListView.getCount(); i++) {
    			MinistryData data = (MinistryData)mListView.getItemAtPosition(i);
    			if(data.isMember) {
    				max++;
    			}
			}
//    		for(MinistryData data: mCurTeamData) {
//    				}
//    			} else {
//    				max = mCurTeamData.size();
//    			}
//    		}
    		if(max <= 0) {
    			showDialog(DIALOG_NO_MEMBER);
    			return true; 
    		}
    		String[] names = new String[max];
			String[] codes = new String[max];
			String[] infos = new String[max];
			boolean[] isData = new boolean[max];
			boolean[] isMembers = new boolean[max];
						
			//팀을 새로 추가.. !!
			String[] teams = new String[max];
			//직급을 새로 추가.. !!
			String[] role = new String[max];
			
//			int i = 0;
			for (int i = 0; i < mCurTeamData.size(); i++) {
				
				MinistryData data = mCurTeamData.get(i);
			
//			for(MinistryData data: (mIsLastView ? mLastTeamData : mCurTeamData)) {
				
				if(data.m_szSerialNo.equals("")) {
					continue;
				}
//				if(item.getItemId() == SEND_SMS && (mIsLastView || !m_bIsChild)) {
//				if(item.getItemId() == SEND_SMS) {
					if(m_bIsChild && !data.isMember) {
						continue;
					}
					names[i] = data.m_szName;
					if(item.getItemId() == SEND_SMS) {
						codes[i] = data.m_szCellPhoneNo;
						infos[i] = data.m_szCellPhoneNo;
					} else {
						codes[i] = data.m_szEmail;
						infos[i] = data.m_szEmail;
					}
					isData[i] = data.m_szCellPhoneNo.equals("") ? false : true;
					
					//팀을 새로 추가.. !!
					teams[i] = data.m_szTeam;
					//직급을 새로 추가.. !!
					role[i] = data.m_szRole;
				}
//				else 
//				{		
//					if(m_bIsChild && !data.isMember) {
//						continue;
//					}
//					names[i] = data.m_szName;
//					codes[i] = data.m_szSerialNo;
//					
//					//팀을 새로 추가.. !!
//					teams[i] = data.m_szTeam;
//					//직급을 새로 추가.. !!
//					role[i] = data.m_szRole;
//					
//					if(m_bIsChild) {
//						if(data.isMember) {
//							if(item.getItemId() == SEND_SMS) {
//								infos[i] = data.m_szCellPhoneNo;
//								isData[i] = data.m_szCellPhoneNo.equals("") ? false : true;
//							} else {
//								infos[i] = data.m_szEmail;
//								isData[i] = data.m_szEmail.equals("") ? false : true;
//							}
//						} else {
//							infos[i] = "";
//							isData[i] = true;
//						}
//					} else {
//						if(item.getItemId() == SEND_SMS) {
//							infos[i] = data.m_szCellPhoneNo;
//							isData[i] = data.m_szCellPhoneNo.equals("") ? false : true;
//						} else {
//							infos[i] = data.m_szEmail;
//							isData[i] = data.m_szEmail.equals("") ? false : true;
//						}
//					}
//				}
//				isMembers[i] = data.isMember;
//				i++;
//			}
			if(item.getItemId() == SEND_SMS) {
				intent.putExtra("type", "sms");
			} else {
				intent.putExtra("type", "email");
			}
			intent.putExtra("names", names);
			intent.putExtra("codes", codes);
			intent.putExtra("infos", infos);
			intent.putExtra("isData", isData);
			intent.putExtra("isMembers", isMembers);
			//팀을 새로 추가.. !!						
			intent.putExtra("team", teams);
			//직급을 새로 추가.. !!
			intent.putExtra("role", role);
			if(mIsLastView) {
				intent.putExtra("isChild", false);
			} else {
				intent.putExtra("isChild", m_bIsChild);
			}
			startActivityForResult(intent, 1);
		} */
		else if(item.getItemId() == CHANGE_VIEW_TYPE) 
		{			
			if(mIsTreeView) {
				mIsTreeView = false;
				mIsBackOff = false;
			} else {
				mIsTreeView = true;
				mIsBackOff = true;
			}
			changeViewType();
		}
		
		return super.onMenuItemSelected(featureId, item);
	}
	
	

	/**
	 * 트리/목록 보기 변경
	 */
	private void changeViewType() {
		if(mIsTreeView) {
			showLayout(TREE_LAYOUT);
			
			if(mCopyTreeAdapter.getCount() == 0) {
				tempParenttempDeptCode = "";
				try {
					tempReqCompanyCd = SKTUtil.getCheckedCompanyCd(this);
				} catch (SKTException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
//				requestData(PEOPLE_DEPT, treeTempParentDeptCode);
				requestData(PEOPLE_DEPT, TOP_DEPTCODE);
	
//				insertTreeData(i, a_XMLData.get(i, "suffixDept"), treeTempDeptCode,
//						a_XMLData.get(i, "deptCode"), 1, haveChild,
//						i == a_XMLData.size() - 1,
//						tempParenttempDeptCode.equals("") ? true :
//								(haveChild ? false : true), tempInfoCompanyCd);
				
//				insertTreeData(1, "N0000", a_ParamB, a_ParamC, depth, hasChild, isLastItem, isExpanded, companyCd);
				
			}
		} else {
			top = false;
			showLayout(LIST_LAYOUT);
			
			if(mListView.getCount() == 0) {
				requestData(PEOPLE_DEPT, "");
			}
		}
		mIsLastView = false;
	}
	
	/**
	 * 트리/목록/최근 조회 보기 변경
	 * @param layout 트리/목록/최근 조회
	 */
	private void showLayout(int layout) {
		if(layout == LIST_LAYOUT) {
			findViewById(R.id.list_layout).setVisibility(View.VISIBLE);
			findViewById(R.id.tree_layout).setVisibility(View.GONE);
			findViewById(R.id.last_layout).setVisibility(View.GONE);
		} else if(layout == TREE_LAYOUT) {
			findViewById(R.id.list_layout).setVisibility(View.GONE);
			findViewById(R.id.tree_layout).setVisibility(View.VISIBLE);
			findViewById(R.id.last_layout).setVisibility(View.GONE);
		} else if(layout == LAST_LAYOUT) {
			findViewById(R.id.list_layout).setVisibility(View.GONE);
			findViewById(R.id.tree_layout).setVisibility(View.GONE);
			findViewById(R.id.last_layout).setVisibility(View.VISIBLE);
		}
	}
	
	@Override
	protected void onActivityResultX(int requestCode, int resultCode, Intent data) {
		if (requestCode==1) {
			if (resultCode==2)
				finish();
		}
	}

	protected Dialog onCreateDialog(int a_nId) {
		Dialog aDialog = null;
    	
		switch (a_nId) {
		
			case DIALOG_MEMBER_LIST:
			case DIALOG_SEND_FAIL:
				aDialog = createDialog(a_nId, m_szDialogMessage, DIALOG_ONE_BUTTON, getString(R.string.mail_ok_button), null, null);
	    		break;
			case DIALOG_NO_MEMBER:
				aDialog = createDialog(a_nId, getString(R.string.mail_dialog_no_member), DIALOG_ONE_BUTTON, getString(R.string.mail_ok_button), null, null);
	    		break;
			case DIALOG_NO_APP:
				aDialog = createDialog(a_nId, m_szDialogMessage, DIALOG_ONE_BUTTON,
						getString(R.string.mail_ok_button), null, null);
	    		break;
				
		} // end switch (a_nId)
    	
    	return aDialog;
	}
    
    /* (non-Javadoc)
     * 팝업 버튼 클릭 이벤트 핸들러
     * @see com.sk.pe.group.activity.BaseActivity#onClickDialog(int, java.lang.String, int)
     */
    public void onClickDialog(int a_nDialogId, String a_szButtonText, int a_nClickedButton) {
		switch (a_nDialogId) {
		
			case DIALOG_MEMBER_LIST:
//				finish();
	    		break;
			case DIALOG_NO_APP:
				
				Intent intent = new Intent(Constants.Action.STORE_DETAIL_VIEW);
				intent.putExtra("APP_ID", "MOGP000001");
				startActivity(intent);
				
				break;
		} // end switch (a_nDialogId)
	} // end public void onClickDialog(int a_nDialogId, String a_szButtonText, int a_nClickedButton)
    
//    @Override
//	public boolean onKeyDown(int keyCode, KeyEvent event) {
//		if (keyCode==KeyEvent.KEYCODE_BACK) {
//			if (!tempParentDeptCode.equals("")) {			
//				if (m_bIsChild) {
//					requestData(PEOPLE_DEPT, tempParentDeptCode);
//		        	tempParenttempDeptCode = tempDeptCode;
//				}else {
//					if (tempPretempDeptCode.equals(tempDeptCode))
//		        		requestData(PEOPLE_DEPT, tempDeptCode);
//		        	else
//		        		requestData(PEOPLE_DEPT, tempParentDeptCode);
//		        	tempPretempDeptCode = "";
//		        	tempParenttempDeptCode = tempDeptCode;
//				}
//			}
//		}
//		return super.onKeyDown(keyCode, event);
//	}
    
    /**
     * 트리 구조 복사
     */
    private void copyTreeAdapter() {
    	mTreeAdapter.clear();
    	for(int i = 0; i < mCopyTreeAdapter.getCount(); i++) {
    		TreeData data = mCopyTreeAdapter.getItem(i);
    		
    		if(data.m_bIsVisible) {
    			mTreeAdapter.add(data);
    		}
    	}
    }
    
    private String setString(String value) {
    	if(StringUtil.isNull(value)) {
    		return "";
    	} else {
    		return value;
    	}
    }

	/* (non-Javadoc)
	 * 액션 처리 후 UI 세팅<br>
	 * - 부서 목록 요청<br>
	 * - 부서 목록 및 멤버 요청<br>
	 * - 내가 소록 회사 코드 요청
	 * @see com.sk.pe.group.activity.BaseActivity#onPostThread(com.skt.pe.common.service.XMLData, com.skt.pe.common.exception.SKTException)
	 */
	public void onPostThread(XMLData a_XMLData, SKTException a_exception) {
    	// 정상 응답이  발생하였으면 오류 메시지 보여주고 리턴]
		if(a_exception != null) {
			m_szTitle = getString(R.string.mail_text_error);
    		m_szDialogMessage = a_exception.getMessage();
			showDialog(DIALOG_MEMBER_LIST);
			
			if(m_nRequestType == PEOPLE_DEPT_ADD) {
				if(temTreepData.m_bIsExpanded) {
					temTreepData.m_bIsExpanded = false;
					tempView.setImageResource(R.drawable.mail_member_arrow_diagram_down);
				} else {
					temTreepData.m_bIsExpanded = true;
					tempView.setImageResource(R.drawable.mail_member_arrow_diagram_right);
				}
			}
			return;
		} // end else if (m_szResponseCode != null && m_szResponseCode.equals("1000") == false)
		try {
			switch (m_nRequestType) 
			{
//			case PEOPLE_DEPTMEMBER :				
//				m_szResponseCode = a_XMLData.get("result");
//				m_szResponseMessage = a_XMLData.get("resultMessage");
//				
//				tempParentDeptCode = treeTempParentDeptCode = a_XMLData.get("parentDeptCode");
//				
//				tempDeptCode = a_XMLData.get("deptCode");
//				myDeptCode = tempDeptCode;
//				tempTitle = a_XMLData.get("suffixDept");
//				tempCompany = a_XMLData.get("companyNm");
//				tempCompanyCd = a_XMLData.get("companyCd");
//				a_XMLData.setList("deptMember");
//				
//				//영문명 추가의 필요.. !!!!
//				mCurTeamData.clear();
//				for (int i=0; i<a_XMLData.size(); i++) {
//					addCurTeamData(
//							a_XMLData.get(i, "id"),
//							a_XMLData.get(i, "name"),
//							a_XMLData.get(i, "telNum"),
//							a_XMLData.get(i, "role"),
//							tempDeptCode,
//							a_XMLData.get(i, "email"),
//							a_XMLData.get(i, "empId"),
//							tempTitle,
//							tempCompany,
//							setString(a_XMLData.get(i, "engName")),
//							a_XMLData.get(i, "vvip"),
//							a_XMLData.get(i, "teamManager"),
//							a_XMLData.get(i, "companyCd"));
//				}
//				
//				showTeam(tempTitle, tempCompanyCd);
//				setTeamName(a_XMLData.get("parentDeptName"), tempTitle);
//				
//				break;
				
			case PEOPLE_DEPT:
				m_szResponseCode = a_XMLData.get("result");
				m_szResponseMessage = a_XMLData.get("resultMessage");
				tempCompanyCd = a_XMLData.get("companyCd");
				tempCompanyNm = a_XMLData.get("companyNm");
				if(!mIsTreeView) {
					tempParentDeptCode = a_XMLData.get("parentDeptCode");
				} else {
					treeTempParentDeptCode = a_XMLData.get("parentDeptCode");
				}
				//tempDeptCode = a_XMLData.get("deptCode");
				treeTempDeptCode = "";
				if(mIsTreeView) {
					treeTempDeptCode =
							XMLUtil.getTextContent(a_XMLData.getNode("/mo/dept/deptCode"));
				} else {
					tempDeptCode = XMLUtil.getTextContent(a_XMLData.getNode("/mo/dept/deptCode"));
					myDeptCode = tempDeptCode;
				}
				tempTitle = XMLUtil.getTextContent(a_XMLData.getNode("/mo/dept/suffixDept"));
				
				if (!StringUtil.isNull(a_XMLData.get("parentDeptNm"))) {
					tempParentTitle = a_XMLData.get("parentDeptNm");
				}else if(!StringUtil.isNull(a_XMLData.get("tempCompanyNm"))) {
					tempParentTitle = tempCompanyNm;
				}else{
					tempParentTitle = getString(R.string.mail_groupcompany);
				}
				
				tempMemberCnt = a_XMLData.get("memberCnt");
				if (tempMemberCnt==null)
					tempMemberCnt = "0";

				if(mIsTreeView) {
					a_XMLData.setList("childDept");
					for(int i = 0; i < a_XMLData.size(); i++) {
						
						String tempInfoCompanyCd = "";
						if (!StringUtil.isNull(a_XMLData.get(i,"companyCd"))) {
							tempInfoCompanyCd = a_XMLData.get(i,"companyCd");
						}else{
							tempInfoCompanyCd = tempCompanyCd;
						}
						boolean haveChild = a_XMLData.get(i, "haveChild").equals("Y");
						
						if(tempParenttempDeptCode.equals("")) {
							
							if (mIsAddTop) {
								addTreeData("한국도로공사", "", TOP_DEPTCODE, 1, true, false, true, tempReqCompanyCd);
								mIsAddTop = false;
							}
							addTreeData(a_XMLData.get(i, "suffixDept"), treeTempDeptCode,
									a_XMLData.get(i, "deptCode"), 2, haveChild,
									i == a_XMLData.size() - 1, haveChild ? false : true, tempInfoCompanyCd);
						} else {
							
							if(tempParenttempDeptCode.equals(a_XMLData.get(i, "deptCode"))) {
								tempParenttempDeptCode = "";
							}
							insertTreeData(i, a_XMLData.get(i, "suffixDept"), treeTempDeptCode,
									a_XMLData.get(i, "deptCode"), 1, haveChild,
									i == a_XMLData.size() - 1,
									tempParenttempDeptCode.equals("") ? true :
											(haveChild ? false : true), tempInfoCompanyCd);
						}
					}
					
					//최상위 코드 99999라면 더이상에 트리 검색은 멈추게 된다.
					
					if(StringUtil.isNull(treeTempParentDeptCode) ){
					 
						copyTreeAdapter();
						mTreeAdapter.notifyDataSetChanged();
					} else {
						for(int i = 0; i < mCopyTreeAdapter.getCount(); i++) {
							TreeData data = mCopyTreeAdapter.getItem(i);
							data.m_nDepth += 1;
						}
						tempParenttempDeptCode = treeTempDeptCode;
						if (!top) {
							tempReqCompanyCd = tempCompanyCd;
							requestData(PEOPLE_DEPT, treeTempParentDeptCode);
						}
						
					}
				} else {
					mCurTeamData.clear();
					mMemberCnt = 0;
					a_XMLData.setList("deptMember");
					for (int i=0; i<a_XMLData.size(); i++) {
						mMemberCnt++;
						String tempInfoCompanyCd = "";
						if (!StringUtil.isNull(a_XMLData.get(i,"companyCd"))) {
							tempInfoCompanyCd = a_XMLData.get(i,"companyCd");
						}else{
							tempInfoCompanyCd = tempCompanyCd; 
						}
						addCurTeamData(
								a_XMLData.get(i, "id"), 
								a_XMLData.get(i, "name"),
								a_XMLData.get(i, "telNum"),
								a_XMLData.get(i, "role"),
								tempTitle,
								tempCompany,
								"", 
								"",
								a_XMLData.get(i, "email"),
								a_XMLData.get(i, "empId"),
								true,
								tempTitle,
								setString(a_XMLData.get(i, "engName")),
								a_XMLData.get(i, "vvip"),
								a_XMLData.get(i, "teamManager"),
								tempInfoCompanyCd,
								a_XMLData.get(i, "phone"));
					}
					
					a_XMLData.setList("childDept");
					for (int i=0; i<a_XMLData.size(); i++) {
						String tempInfoCompanyCd = "";
						if (!StringUtil.isNull(a_XMLData.get(i,"companyCd"))) {
							tempInfoCompanyCd = a_XMLData.get(i,"companyCd");
						}else{
							tempInfoCompanyCd = tempCompanyCd; 
						}
						addCurTeamData(
								a_XMLData.get(i, "deptCode"),
								a_XMLData.get(i, "suffixDept"),
								tempCompany,
								a_XMLData.get(i, "role"), a_XMLData.get(i, "haveChild"), "", "", "", "", "", false,
								tempTitle, setString(a_XMLData.get(i, "engName")), 
								a_XMLData.get(i, "vvip"), a_XMLData.get(i, "teamManager"), tempInfoCompanyCd,
								a_XMLData.get(i, "phone"));
					}
					if(StringUtil.isNull(treeTempDeptCode))
					{	
						setTeamName(tempParentTitle, tempTitle);
	    				showDepartment(tempParentTitle, tempCompanyCd);	    				
					} 
					else 
					{
						setTeamName(null, tempTitle);
						
//						Bitmap img = null;
//						
//						try {
//							String szLogo = a_XMLData.get("companyLogo");
//							if(szLogo.startsWith("http")) {
//								img = ResourceUtil.getBitmapByUrl(szLogo);
//							} else {
//								img = ResourceUtil.decodeBitmap(szLogo);
//							}
//							ImageView logo = (ImageView)findViewById(R.id.LOGO_COMPANY);
//							logo.setImageBitmap(img);
//						} catch(Exception e) {
//							e.printStackTrace();
//						}
	    				showOrgAll();
					}
				}
				break;
			case PEOPLE_DEPT_ADD:
				m_szResponseCode = a_XMLData.get("result");
				m_szResponseMessage = a_XMLData.get("resultMessage");
				
				treeTempParentDeptCode = a_XMLData.get("parentDeptCode");
				//tempDeptCode = a_XMLData.get("deptCode");
				treeTempDeptCode = XMLUtil.getTextContent(a_XMLData.getNode("/mo/dept/deptCode"));
				tempTitle = a_XMLData.get("suffixDept");
				tempMemberCnt = a_XMLData.get("memberCnt");
				if (tempMemberCnt==null)
					tempMemberCnt = "0";
				a_XMLData.setList("childDept");

				if(mIsTreeView) {
					int i;
					int j;
					int depth = 0;
					
					for(j = 0; j < mCopyTreeAdapter.getCount(); j++) {
						TreeData data = mCopyTreeAdapter.getItem(j);
						if(data.m_szDept.equals(treeTempDeptCode)) {
							depth = data.m_nDepth;
							break;
						}
					}
					for(i = 0; i < a_XMLData.size(); i++) {
						
						String tempInfoCompanyCd = "";
						if (!StringUtil.isNull(a_XMLData.get(i,"companyCd"))) {
							tempInfoCompanyCd = a_XMLData.get(i,"companyCd");
						}else{
							tempInfoCompanyCd = tempCompanyCd; 
						}
						boolean haveChild = a_XMLData.get(i, "haveChild").equals("Y");
						
						insertTreeData(j + i + 1, a_XMLData.get(i, "suffixDept"), treeTempDeptCode,
								a_XMLData.get(i, "deptCode"), depth + 1, haveChild,
								i == a_XMLData.size() - 1, haveChild ? false : true, tempInfoCompanyCd);
					}
					
					copyTreeAdapter();
					mTreeAdapter.notifyDataSetChanged();
				}
				break;
	    	case PEOPLE_MY_COMPANY:
	    		SKTUtil.log(Log.DEBUG, "xxxxxx", "xxxxx  ==========   http"  );
//	    		requestData(PEOPLE_DEPTMEMBER, "");
				
				break;
	    	case PEOPLE_COMPANY:
//	    		m_szResponseCode = a_XMLData.get("result");
//				m_szResponseMessage = a_XMLData.get("resultMessage");
//				
//				treeTempParentDeptCode = a_XMLData.get("parentDeptCode");
//				//tempDeptCode = a_XMLData.get("deptCode");

	    		
	    		
	    		
	    		
	    		break;
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
	}

	/* (non-Javadoc)
	 * 액션 처리 핸들러<br>
	 * - 부서 목록 요청<br>
	 * - 부서 목록 및 멤버 요청<br>
	 * - 내가 소록 회사 코드 요청
	 * @see com.sk.pe.group.activity.BaseActivity#onProcessThread()
	 */
	public XMLData onProcessThread() throws SKTException {
		Parameters params = null;
	    Controller controller = null;
	    XMLData nXMLData = null;
		String req = (String)m_requestObject;
	    
		switch (m_nRequestType) {
//		case PEOPLE_DEPTMEMBER:
////			if(req != null && !req.equals("")) {
////				if(req.indexOf(".") < 0) {
////					comCd = req;
////				} else {
////					comCd = req.substring(0, req.indexOf("."));
////				}
////			}
//			
//			params = new Parameters("COMMON_PEOPLE_DEPTMEMBER");
//        	params.put("infoCompanyCd", tempReqCompanyCd);
//			params.put("deptCode", req);
//			if(mApprovalType != null && !mApprovalType.equals("")) {
//				params.put("approvalType", mApprovalType);
//			}
//			if(mEmpType != null && !mEmpType.equals("")) {
//				params.put("empType", mEmpType);
//			}
//			break;
		case PEOPLE_DEPT:
			
//			Log.e("xx",req);
//			if(req != null && !req.equals("")) {
//				comCd = req.substring(0, req.indexOf("."));
//			}			
        	params = new Parameters("COMMON_PEOPLE_DEPT");
//        	if("all".equals((String)m_requestObject)){
//        		params.put("infoCompanyCd", "all");
//        		params.put("deptCode", "");
//        	}else{
        		params.put("infoCompanyCd", tempReqCompanyCd);
        		params.put("deptCode", (String)m_requestObject);
//        	}
			if(mApprovalType != null && !mApprovalType.equals("")) {
				params.put("approvalType", mApprovalType);
			}
			if(mEmpType != null && !mEmpType.equals("")) {
				params.put("empType", mEmpType);
			}
			break;
		case PEOPLE_DEPT_ADD:
//			if(req != null && !req.equals("")) {
//				comCd = req.substring(0, req.indexOf("."));
//			}
        	params = new Parameters("COMMON_PEOPLE_DEPT");
        	params.put("infoCompanyCd", tempReqCompanyCd);
			params.put("deptCode", (String)m_requestObject);
			if(mApprovalType != null && !mApprovalType.equals("")) {
				params.put("approvalType", mApprovalType);
			}
			if(mEmpType != null && !mEmpType.equals("")) {
				params.put("empType", mEmpType);
			}
			break;
		case PEOPLE_MY_COMPANY:
			params = new Parameters("COMMON_PEOPLE_MEMBERCONTENT");
			params.put("type", "M");
			break;
			
		case PEOPLE_COMPANY:
			params = new Parameters("COMMON_PEOPLE_COMPANY");
			params.put("infoCompanyCd", "");
			break;
		
		}

		controller = new Controller(this);
		nXMLData = controller.request(params);
		
		return nXMLData;
	}
	
	protected boolean checkedItem() {
		for (int i=0; i<mCurTeamData.size(); i++) {
			if (mCurTeamData.get(i).m_szDepartment.equals("") && mCurTeamData.get(i).m_szLocation.equals(""))
				return false;
		}
		return true;
	}
	
	private void addCurTeamData(String a_ParamA, String a_ParamB, String a_ParamC, String a_ParamD,
                                String a_ParamE, String a_ParamF, String a_ParamG, String teamName, String companyNm,
                                String engName, String vvip, String teamManager, String companyCd, String officePhoneNo) {
		addCurTeamData(a_ParamA, a_ParamB, a_ParamC, a_ParamD, a_ParamE, "", "", "", a_ParamF,
				a_ParamG, false, teamName, engName, vvip, teamManager, companyCd, officePhoneNo);
	}

	private void addCurTeamData(String a_ParamA, String a_ParamB, String a_ParamC, String a_ParamD,
                                String a_ParamE, String a_ParamF, String a_ParamG, String a_ParamH, String a_ParamI,
                                String a_ParamJ, boolean isMember, String teamName, String engName, String vvip, String teamManager, String companyCd, String officePhoneNo) {
		MinistryData data = new MinistryData();
		data.m_szSerialNo = checkTrim(a_ParamA);
		data.m_szName = checkTrim(a_ParamB);
		data.m_szCellPhoneNo = checkPhone(a_ParamC);
		data.m_szRole = checkTrim(a_ParamD);
		data.m_szDepartment = checkTrim(a_ParamE);
		data.m_szCompany = checkTrim(a_ParamF);
		data.m_szType = checkTrim(a_ParamG);
		data.m_szCompanyCd = checkTrim(companyCd);
		data.m_szEmail = checkTrim(a_ParamI);
		data.m_szEmpId = checkTrim(a_ParamJ);
		data.m_nCountNum = mCurTeamData.size();
		data.m_bIsChecked = false;
		data.isMember = isMember;
		data.m_szTeam = teamName;
		data.m_szEngName = engName;
		data.m_szVvip = vvip;
		data.m_szTeamManager = teamManager;
		data.m_szOfficePhoneNo = officePhoneNo;
		
		if(mIsLastView) {
			mLastTeamData.add(data);
		} else {
			mCurTeamData.add(data);
		}
	}

	private void addTreeData(String a_ParamA, String a_ParamB, String a_ParamC, int depth,
                             boolean hasChild, boolean isLastItem, boolean isExpanded, String companyCd) {
		TreeData data = new TreeData();
		data.m_szName = checkTrim(a_ParamA);
		data.m_szUpDept = checkTrim(a_ParamB);
		data.m_szDept = checkTrim(a_ParamC);
		data.m_nDepth = depth;
		data.m_bHasChild = hasChild;
		data.m_bIsExpanded = isExpanded;
		data.m_bIsLastItem = isLastItem;
		data.m_bIsVisible = true;
		data.m_szCompanyCd = companyCd;
//		mTreeAdapter.add(data);
		mCopyTreeAdapter.add(data);
	}

	private void insertTreeData(int index, String a_ParamA, String a_ParamB, String a_ParamC,
                                int depth, boolean hasChild, boolean isLastItem, boolean isExpanded, String companyCd) {
		TreeData data = new TreeData();
		data.m_szName = checkTrim(a_ParamA);
		data.m_szUpDept = checkTrim(a_ParamB);
		data.m_szDept = checkTrim(a_ParamC);
		data.m_nDepth = depth;
		data.m_bHasChild = hasChild;
		data.m_bIsExpanded = isExpanded;
		data.m_bIsLastItem = isLastItem;
		data.m_bIsVisible = true;
		data.m_szCompanyCd = companyCd;
//		mTreeAdapter.insert(data, index);
		mCopyTreeAdapter.insert(data, index);

	}
	
	private String checkTrim(String a_sParam) {
		if(a_sParam == null) {
			return "";
		}
		
		return a_sParam.trim();
	}
	
	private String checkPhone(String phone) {
		String ret = "";
		
		if(phone == null) {
			return ret;
		}
		
		ret = phone.replace("-", "").trim();
		
		return ret;
	}

	private void setTeamName(String parentTeamName, String currentTeamName)
	{
//		if(parentTeamName != null)
//			mSuffixDeptText.setText(parentTeamName);
//		
		SKTUtil.log(Log.DEBUG, "xxxx", "xxxx currentTeamName :;  " + currentTeamName);
//		if(currentTeamName != null)
			mSuffixDeptText.setText(currentTeamName);		
	}
	
	/**
	 * 팀명 보이기
	 * @param a_szTeamName 팀명
	 */
	private void showTeam(String a_szTeamName, String CompanyCd)
	{		
		mLinearLayout.setVisibility(View.VISIBLE);
//    	mLogoLayout.setVisibility(View.GONE);
//    	if (!StringUtil.isNull(a_szTeamName)) {
//    		mSuffixDeptText.setText(a_szTeamName);	
//		}else{
//			mSuffixDeptText.setText(getString(R.string.groupcompany));
//		}
    	tempReqCompanyCd = CompanyCd;
    	//    	findViewById(R.id.up_line).setVisibility(View.GONE);
    		mUpButton.setOnClickListener(new OnClickListener() {
    			public void onClick(View view)
    			{
    				if (tempPretempDeptCode.equals(tempDeptCode)){
    					requestData(PEOPLE_DEPT, tempDeptCode);
    				}else{
    					requestData(PEOPLE_DEPT, tempParentDeptCode);
    				}
    				tempPretempDeptCode = "";
    				tempParenttempDeptCode = tempDeptCode;
    			}
    		});
        m_bIsChild = false;
        mListView.setAdapter(mEmaployeeAdapter);
        mListView.setOnItemClickListener(mMemeberClickListener);
    }

    /**
     * 부서명 보이기
     * @param a_szTeamName 부서명
     */
    private void showDepartment(String a_szTeamName, String CompanyCd) {
    	mLinearLayout.setVisibility(View.VISIBLE);
//    	N00001
//    	mLogoLayout.setVisibility(View.GONE);
//    	mSuffixDeptText.setText(a_szTeamName);
    	tempReqCompanyCd = CompanyCd;
//    	findViewById(R.id.up_line).setVisibility(View.VISIBLE);
    	if("N00001".equals(tempDeptCode)){
    		mUpButton.setOnClickListener(null);
    		mUpButton.setVisibility(View.GONE);
    	} else{
    		mUpButton.setVisibility(View.VISIBLE);
    		mUpButton.setOnClickListener(new OnClickListener()
    		{
    			public void onClick(View view)
    			{
    				requestData(PEOPLE_DEPT, tempParentDeptCode);
    				tempParenttempDeptCode = tempDeptCode;
    			}
    		});
    	}
        m_bIsChild = true;
        mListView.setAdapter(mTeamAdapter);
        mListView.setOnItemClickListener(mTeamClickListener);
    }

    /**
     * 회사명 보이기
     */
    private void showOrgAll() {
    	mLinearLayout.setVisibility(View.GONE);
//    	mLogoLayout.setVisibility(View.VISIBLE);
        m_bIsChild = true;
        mListView.setAdapter(mTeamAdapter);
        mListView.setOnItemClickListener(mTeamClickListener);
    }

//    public OnItemClickListener mDepartmentClickListener = new OnItemClickListener()
//    {
//		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
//				long arg3) {
//			// TODO Auto-generated method stub
//        	MinistryData department = (MinistryData)mCurTeamData.get(arg2);
//
//    		if(department.m_szDepartment.equals("N")) {
//    			requestData(PEOPLE_DEPTMEMBER, department.m_szSerialNo);
//    		} else {
//    			requestData(PEOPLE_DEPT, department.m_szSerialNo);
//    		}
//		}
//    };

    public OnItemClickListener mTeamClickListener = new OnItemClickListener()
    {
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,

                                long arg3) {
            MinistryData data = (MinistryData)mCurTeamData.get(arg2);
            
            if(data.isMember) {
            	if(mIsMultiSelect) {
    				ImageView phone = (ImageView)arg1.findViewById(R.id.ROW_EMPLOYEE_PHONE);
					if(dataEmpId.containsKey(data.m_szSerialNo)) {
						data.m_bIsChecked = false;
						phone.setImageResource(R.drawable.mail_member_check_off);
						dataEmpId.remove(data.m_szSerialNo);
	            		dataName.remove(data.m_szSerialNo);
	            		dataVvip.remove(data.m_szSerialNo);
	            		dataNumber.remove(data.m_szSerialNo);
	            		dataEmail.remove(data.m_szSerialNo);
	            		dataDept.remove(data.m_szDepartment);
					} else {
						data.m_bIsChecked = true;
						phone.setImageResource(R.drawable.mail_member_check_on);
						dataEmpId.put(data.m_szSerialNo, data.m_szSerialNo);
						dataName.put(data.m_szSerialNo, data.m_szName);
						dataVvip.put(data.m_szSerialNo, data.m_szVvip);
						dataNumber.put(data.m_szSerialNo, data.m_szCellPhoneNo);
						dataEmail.put(data.m_szSerialNo, data.m_szEmail);
						dataDept.put(data.m_szSerialNo, data.m_szDepartment);
					}
            	} else if(mIsSingleSelect) {
        			Intent intent = new Intent();
        			intent.putExtra("name", data.m_szName);
        			intent.putExtra("empId", data.m_szSerialNo);
        			intent.putExtra("role", data.m_szRole);
        			intent.putExtra("suffixDept", data.m_szTeam);
        			intent.putExtra("arg1", tempArg1);
        			AddressTabActivity parent = (AddressTabActivity)getParent();
        			parent.setResult(RESULT_OK, intent);
        			finish();
            	} else {
	            	Intent intent = new Intent(AddressListActivity.this, AddressInfoActivity.class);
	            	intent.putExtra("employee", data.m_szSerialNo);
	            	intent.putExtra("deptCode", data.m_szDepartment);
	            	intent.putExtra("companyCode", data.m_szCompanyCd);
	            	startActivityForResult(intent, 1);
            	}
            } else {
	            if(data.m_szDepartment.equals("N")) {
	            	requestData(PEOPLE_DEPT, data.m_szSerialNo);
	            } else {
	            	tempReqCompanyCd = data.m_szCompanyCd;
	            	requestData(PEOPLE_DEPT, data.m_szSerialNo);
	            }
            }
		}
    };
    
    public OnItemClickListener mMemeberClickListener = new OnItemClickListener() {
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                long arg3) {
			// TODO Auto-generated method stub
			MinistryData data = (MinistryData)mCurTeamData.get(arg2);
			
			if(mIsMultiSelect) {
				ImageView phone = (ImageView)arg1.findViewById(R.id.ROW_EMPLOYEE_PHONE);
        		if(dataEmpId.containsKey(data.m_szSerialNo)) {
        			data.m_bIsChecked = false;
        			phone.setImageResource(R.drawable.mail_member_check_off);
        		} else {
        			data.m_bIsChecked = true;
        			phone.setImageResource(R.drawable.mail_member_check_on);
        		}
			} else if(mIsSingleSelect) {
    			Intent intent = new Intent();
    			intent.putExtra("name", data.m_szName);
    			intent.putExtra("empId", data.m_szSerialNo);
    			intent.putExtra("role", data.m_szRole);
    			intent.putExtra("suffixDept", data.m_szTeam);
    			intent.putExtra("arg1", tempArg1);
    			AddressTabActivity parent = (AddressTabActivity)getParent();
    			parent.setResult(RESULT_OK, intent);
    			finish();
			} else {
	        	Intent intent = new Intent(AddressListActivity.this, AddressInfoActivity.class);
	        	intent.putExtra("employee", data.m_szSerialNo);
	        	intent.putExtra("deptCode", data.m_szDepartment);
            	intent.putExtra("companyCode", data.m_szCompanyCd);
	        	startActivityForResult(intent, 1);
			}
		}
    };
	
	/**
	 * 선택한 멤버 또는 부서 리턴
	 * @return 멤버 또는 부서 목록
	 */
	private ArrayList<MinistryData> getSelectedNames() {
		ArrayList<MinistryData> data = new ArrayList<MinistryData>();
		for(MinistryData min: (mIsLastView ? mLastTeamData : mCurTeamData)) {
			if(min.m_bIsChecked) {
				data.add(min);
			}
		}
		
		return data;
	}

	/* (non-Javadoc)
	 * 클릭 이벤트 핸들러<br>
	 * - 선택 버튼<br>
	 * - 취소 버튼
	 * @see android.view.View.OnClickListener#onClick(android.view.View)
	 */
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if(v.getId() == R.id.multiselect_confirm) {
//			ArrayList<MinistryData> data = getSelectedNames();
//			String[] names = new String[data.size()];
//			String[] depts = new String[data.size()];
//			String[] emails = new String[data.size()];
//			String[] empids = new String[data.size()];
//			
//			for(int i = 0; i < data.size(); i++) {
//				MinistryData employee = data.get(i);
//				names[i] = employee.m_szName;
//				empids[i] = employee.m_szSerialNo;
//				
//				depts[i] = employee.m_szDepartment;
//				emails[i] = employee.m_szEmail;
//			}
			Iterator<String> it = dataName.keySet().iterator();
			String[] empId = new String[dataName.size()];
			String[] empname = new String[dataName.size()];
			String[] empNumber = new String[dataName.size()];
			String[] empVvip = new String[dataName.size()];
			String[] empEmail = new String[dataName.size()];
			String[] empDept = new String[dataName.size()];
			int i = 0;
			while (it.hasNext()) {
				String key = it.next();
				empId[i] = key;
				empname[i] = dataName.get(key);
				empNumber[i] = dataNumber.get(key);
				empVvip[i] = dataVvip.get(key);
				empEmail[i] = dataEmail.get(key);
				empDept[i] = dataDept.get(key);
				
				SKTUtil.log(Log.DEBUG, "xxxxxxxx", "xxxxxxxxxx key : " + key);
				SKTUtil.log(Log.DEBUG, "xxxxxxxx", "xxxxxxxxxx empname[i] : " + empname[i]);
				i++;
			}
			
			dataEmpId.clear();
			dataName.clear();
			dataNumber.clear();
			dataVvip.clear();
			dataEmail.clear();
			dataDept.clear();
			
			Intent intent = new Intent();
			intent.putExtra("names", empname);
			intent.putExtra("empids", empId);
			intent.putExtra("depts", empDept);
			intent.putExtra("emails", empEmail);
			intent.putExtra("phonenos", empNumber);
			intent.putExtra("arg1", tempArg1);
			AddressTabActivity parent = (AddressTabActivity)getParent();
			parent.setResult(RESULT_OK, intent);
			finish();
		} else if(v.getId() == R.id.multiselect_cancel) {
			AddressTabActivity parent = (AddressTabActivity)getParent();
			parent.setResult(RESULT_CANCELED);
			finish();
		}
	}

    public BaseAdapter mEmaployeeAdapter = new BaseAdapter()
    {
    	public View getView(int position, View convertView, ViewGroup parent)
        {
            if(convertView == null || convertView.findViewById(R.id.ROW_EMPLOYEE_NAME) == null) {
                convertView = getLayoutInflater().inflate(R.layout.mail_member_row_employee_1, null);
            }
            final MinistryData data = (MinistryData) getItem(position);
            convertView.findViewById(R.id.employee_circle).setVisibility(View.INVISIBLE);
          
            TextView name = (TextView)convertView.findViewById(R.id.ROW_EMPLOYEE_NAME);
            name.setText(data.m_szName);
            TextView team = (TextView)convertView.findViewById(R.id.ROW_EMPLOYEE_CORP);
            team.setText(data.m_szRole);
//            TextView role = (TextView)convertView.findViewById(R.id.ROW_EMPLOYEE_TEAM);
//            role.setText(data.m_szTeam);
            
            ImageView sms = (ImageView)convertView.findViewById(R.id.ROW_EMPLOYEE_SMS);
            final ImageView phone = (ImageView)convertView.findViewById(R.id.ROW_EMPLOYEE_PHONE);
            
            if (StringUtil.isNull(data.m_szCellPhoneNo)) {
            	sms.setVisibility(View.INVISIBLE);
			}else{
				sms.setVisibility(View.VISIBLE);
			}
            sms.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Uri uri = Uri.parse("sms:" + data.m_szCellPhoneNo);
        			Intent intent = new Intent(Intent.ACTION_SENDTO, uri);
        			startActivity(intent);
				}
			});
            
            if (StringUtil.isNull(data.m_szOfficePhoneNo)) {
            	phone.setVisibility(View.INVISIBLE);
			}else{
				phone.setVisibility(View.VISIBLE);
			}
            phone.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					// TODO Auto-generated method stub
					if(mIsMultiSelect) {
						if(dataEmpId.containsKey(data.m_szSerialNo)) {
							SKTUtil.log("xxxxxxxxx", "xxxxxxxxxxx dataEmpId.size() 1464 :: " +  dataEmpId.size());
							data.m_bIsChecked = false;
							phone.setImageResource(R.drawable.mail_member_check_off);
							dataEmpId.remove(data.m_szSerialNo);
		            		dataName.remove(data.m_szSerialNo);
		            		dataVvip.remove(data.m_szSerialNo);
		            		dataNumber.remove(data.m_szSerialNo);
		            		dataEmail.remove(data.m_szSerialNo);
						} else {
							data.m_bIsChecked = true;
							phone.setImageResource(R.drawable.mail_member_check_on);
							dataEmpId.put(data.m_szSerialNo, data.m_szEmpId);
							dataName.put(data.m_szSerialNo, data.m_szName);
							dataVvip.put(data.m_szSerialNo, data.m_szVvip);
							dataNumber.put(data.m_szSerialNo, data.m_szCellPhoneNo);
							dataEmail.put(data.m_szSerialNo, data.m_szEmail);
						}
					} else {
						Uri uri = Uri.parse("tel:" + data.m_szCellPhoneNo);
						Intent intent = new Intent(Intent.ACTION_DIAL, uri);
						startActivity(intent);
					}
				}
			});
            
            ImageView office = (ImageView)convertView.findViewById(R.id.ROW_EMPLOYEE_LANDLINE);
            if (StringUtil.isNull(data.m_szOfficePhoneNo)) {
				office.setVisibility(View.INVISIBLE);
			}else{
				office.setVisibility(View.VISIBLE);
			}

            office.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					Uri uri = Uri.parse("tel:" + data.m_szOfficePhoneNo);
					Intent intent = new Intent(Intent.ACTION_DIAL, uri);
					startActivity(intent);
				}
			});
            
        	if(mIsMultiSelect) {
        		if(dataEmpId.containsKey(data.m_szSerialNo)) {
        			phone.setImageResource(R.drawable.mail_member_check_on);
        		} else {
        			phone.setImageResource(R.drawable.mail_member_check_off);
        		}
        		phone.setVisibility(View.VISIBLE);
        		sms.setVisibility(View.GONE);
        		office.setVisibility(View.GONE);
        	} else if(mIsSingleSelect) {
        		phone.setVisibility(View.GONE);
        		office.setVisibility(View.GONE);
        		sms.setVisibility(View.GONE);
        	}            
            return convertView;
        }
    	

        public long getItemId(int position)
        {
            return position;
        }

        public Object getItem(int position)
        {
            return mCurTeamData.get(position);
        }

        public int getCount()
        {
            return mCurTeamData.size();
        }
    };
    
    public OnItemClickListener mLastClickListener = new OnItemClickListener() {
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                long arg3) {
			// TODO Auto-generated method stub
			MinistryData data = (MinistryData)mLastTeamData.get(arg2);
			
			if(mIsMultiSelect) {
				ImageView phone = (ImageView)arg1.findViewById(R.id.ROW_EMPLOYEE_PHONE);
        		if(dataEmpId.containsKey(data.m_szSerialNo)) {
        			data.m_bIsChecked = false;
        			phone.setImageResource(R.drawable.mail_member_check_off);
        		} else {
        			data.m_bIsChecked = true;
        			phone.setImageResource(R.drawable.mail_member_check_on);
        		}
			} else {
	        	Intent intent = new Intent(AddressListActivity.this, AddressInfoActivity.class);
	        	intent.putExtra("type", data.m_szType);
		    	intent.putExtra("isLast", true);
		    	intent.putExtra("companyCode", data.m_szCompanyCd);
		    	intent.putExtra("deptCode", data.m_szDepartment);
	        	intent.putExtra("employee", data.m_szSerialNo);
	        	startActivityForResult(intent, 1);
			}
		}
    };

    public BaseAdapter mTeamAdapter = new BaseAdapter() {
    	public View getView(int position, View convertView, ViewGroup parent)
        {
            final MinistryData data = (MinistryData)getItem(position);
            
            if(data.isMember) 
            {
            	convertView = getLayoutInflater().inflate(R.layout.mail_member_row_employee_1, null);
            	
            	convertView.findViewById(R.id.employee_circle).setVisibility(View.VISIBLE);
            	
            	ImageView line = (ImageView)convertView.findViewById(R.id.employee_circle);
            	if ((getCount()-1) == position ) {
            		line.setBackgroundDrawable(getResources().getDrawable(R.drawable.mail_member_line_02));
//            		line.setBackgroundDrawable(getResources().getDrawable(R.drawable.member_line_02));
            		/// 이미지 받으면 수정 
            	} else {
					line.setBackgroundDrawable(getResources().getDrawable(R.drawable.mail_member_line_02));
				}
            	
            	ImageView line1 = (ImageView)convertView.findViewById(R.id.employee_line);
            	line1.setVisibility(View.VISIBLE);
            	if ((mMemberCnt -1 ) == position ) {
            		line1.setBackgroundDrawable(getResources().getDrawable(R.drawable.mail_member_line_04));
				} else {
					line1.setBackgroundDrawable(getResources().getDrawable(R.drawable.mail_member_line_03));
				}
            	
                TextView name = (TextView)convertView.findViewById(R.id.ROW_EMPLOYEE_NAME);
                name.setText(data.m_szName);
                TextView team = (TextView)convertView.findViewById(R.id.ROW_EMPLOYEE_CORP);
                team.setText(data.m_szRole);
//                TextView role = (TextView)convertView.findViewById(R.id.ROW_EMPLOYEE_TEAM);
//                role.setText(data.m_szTeam);
                if("Y".equals(data.m_szTeamManager)) {
                	name.setTextColor(Color.rgb(0x1C, 0x8C, 0xC8));
                } else {
                	name.setTextColor(Color.BLACK);
                }
                
                if("Y".equals(data.m_szVvip)) {
                	team.setVisibility(View.INVISIBLE);
                } else {
                	team.setVisibility(View.VISIBLE);
                }
                
                ImageView sms = (ImageView)convertView.findViewById(R.id.ROW_EMPLOYEE_SMS);
                final ImageView phone =
                		(ImageView)convertView.findViewById(R.id.ROW_EMPLOYEE_PHONE);
                
                ImageView office = (ImageView)convertView.findViewById(R.id.ROW_EMPLOYEE_LANDLINE);
	                
                if (StringUtil.isNull(data.m_szCellPhoneNo)) {
                	sms.setVisibility(View.INVISIBLE);
    			}else{
    				sms.setVisibility(View.VISIBLE);
    			}
                sms.setOnClickListener(new OnClickListener() {
    				public void onClick(View v) {
    					// TODO Auto-generated method stub
    					Uri uri = Uri.parse("sms:" + data.m_szCellPhoneNo);
            			Intent intent = new Intent(Intent.ACTION_SENDTO, uri);
            			startActivity(intent);
    				}
    			});
                
                if (StringUtil.isNull(data.m_szCellPhoneNo)) {
                	phone.setVisibility(View.INVISIBLE);
    			}else{
    				phone.setVisibility(View.VISIBLE);
    			}
                phone.setOnClickListener(new OnClickListener() {;
    				public void onClick(View v) {
    					// TODO Auto-generated method stub
						if(mIsMultiSelect) {
							if(dataEmpId.containsKey(data.m_szSerialNo)) {
								data.m_bIsChecked = false;
								phone.setImageResource(R.drawable.mail_member_check_off);
								dataEmpId.remove(data.m_szSerialNo);
			            		dataName.remove(data.m_szSerialNo);
			            		dataVvip.remove(data.m_szSerialNo);
			            		dataNumber.remove(data.m_szSerialNo);
			            		dataEmail.remove(data.m_szSerialNo);
							} else {
								data.m_bIsChecked = true;
								phone.setImageResource(R.drawable.mail_member_check_on);
								dataEmpId.put(data.m_szSerialNo, data.m_szEmpId);
								dataName.put(data.m_szSerialNo, data.m_szName);
								dataVvip.put(data.m_szSerialNo, data.m_szVvip);
								dataNumber.put(data.m_szSerialNo, data.m_szCellPhoneNo);
								dataEmail.put(data.m_szSerialNo, data.m_szEmail);
							}
							
						} else {
	    					Uri uri = Uri.parse("tel:" + data.m_szCellPhoneNo);
	    					Intent intent = new Intent(Intent.ACTION_DIAL, uri);
	    					startActivity(intent);
						}
    				}
    			});
                
                if (StringUtil.isNull(data.m_szOfficePhoneNo)) {
    				office.setVisibility(View.INVISIBLE);
    			}else{
    				office.setVisibility(View.VISIBLE);
    			}

                office.setOnClickListener(new OnClickListener() {
    				
    				@Override
    				public void onClick(View v) {
    					Uri uri = Uri.parse("tel:" + data.m_szOfficePhoneNo);
    					Intent intent = new Intent(Intent.ACTION_DIAL, uri);
    					startActivity(intent);
    				}
    			});
                
                if(mIsMultiSelect) 
            	{
            		if(dataEmpId.containsKey(data.m_szSerialNo)) {
            			phone.setImageResource(R.drawable.mail_member_check_on);
            		} else {
            			phone.setImageResource(R.drawable.mail_member_check_off);
            		}
            		
            		phone.setVisibility(View.VISIBLE);
            		sms.setVisibility(View.GONE);
            		office.setVisibility(View.GONE);
            	} else if(mIsSingleSelect) {
            		phone.setVisibility(View.GONE);
            		sms.setVisibility(View.GONE);
            		office.setVisibility(View.GONE);
            	}
            } 
            else 
            {
                convertView = getLayoutInflater().inflate(R.layout.mail_member_row_team, null);
                convertView.findViewById(R.id.team_line).setVisibility(View.VISIBLE);
            	ImageView line = (ImageView)convertView.findViewById(R.id.team_line);
            	if ((getCount()-1) == position ) {
            		line.setBackgroundDrawable(getResources().getDrawable(R.drawable.mail_member_line_05));
				} else {
					line.setBackgroundDrawable(getResources().getDrawable(R.drawable.mail_member_line_01));
				}
//                if(position == getCount() - 1) {
//                	line.setImageResource(R.drawable.member_dot_tree_01);
//                } else {
//                	line.setImageResource(R.drawable.member_dot_tree_02);
//                }
            	SKTUtil.log(Log.DEBUG, "xxxxx", "xxxxxxxxx 1588  " +data.isMember  );
                TextView name = (TextView)convertView.findViewById(R.id.ROW_TEAM_NAME);
                name.setText(data.m_szName);
            }
            
            return convertView;
        }

        public long getItemId(int position)
        {
            return position;
        }

        public Object getItem(int position)
        {
            return mCurTeamData.get(position);
        }

        public int getCount()
        {
            return mCurTeamData.size();
        }
    };
    
    public BaseAdapter mLastViewAdapter = new BaseAdapter()
    {
        public View getView(int position, View convertView, ViewGroup parent)
        {
            final MinistryData data = (MinistryData)getItem(position);
            
        	if(data.m_szSerialNo.trim().length() == 0) {
				convertView = getLayoutInflater().inflate(R.layout.mail_member_recentsearchtitleview, null);
				TextView tv = (TextView) convertView.findViewById(R.id.recentsearchtitle);
            	tv.setText(data.m_szName);
        	} else {
	            convertView = getLayoutInflater().inflate(R.layout.mail_member_grouplistsearchresultview, null);

	            TextView company = (TextView)convertView.findViewById(R.id.ROW_COMPANY);
	            company.setText(data.m_szCompany);
	            if(data.m_szCompany.equals("")) {
	            	company.setVisibility(View.GONE);
	            } else {
	            	company.setVisibility(View.VISIBLE);
	            }
	            TextView name = (TextView)convertView.findViewById(R.id.ROW_EMPLOYEE_NAME);
	            name.setText(data.m_szName);
	            TextView team = (TextView)convertView.findViewById(R.id.ROW_EMPLOYEE_CORP);
	            team.setText(data.m_szTeam);
	            TextView role = (TextView)convertView.findViewById(R.id.ROW_EMPLOYEE_DEPT);
	            role.setText(data.m_szRole);
	            
	            if(data.m_szTeam.equals("")) {
	            	team.setVisibility(View.GONE);
	            } else {
	            	team.setVisibility(View.VISIBLE);
	            }
	            if(data.m_szRole.equals("")) {
	            	role.setVisibility(View.GONE);
	            } else {
	            	role.setVisibility(View.VISIBLE);
	            }
	            TextView work = (TextView)convertView.findViewById(R.id.ROW_EMPLOYEE_WORK);
	            work.setVisibility(View.GONE);
	            
	            final ImageView phone =
	            		(ImageView)convertView.findViewById(R.id.ROW_EMPLOYEE_PHONE);
	            
	        	if(mIsMultiSelect) {
	        		if(dataEmpId.containsKey(data.m_szSerialNo)) {
	        			phone.setImageResource(R.drawable.mail_member_check_on);
	        		} else {
	        			phone.setImageResource(R.drawable.mail_member_check_off);
	        		}
	        		phone.setVisibility(View.VISIBLE);
	        	} else {
	        		
	                if(data.m_szCellPhoneNo == null || data.m_szCellPhoneNo.equals("")) {
	                	phone.setVisibility(View.GONE);
	                } else {
	                	phone.setVisibility(View.VISIBLE);
	                }
	        	}
	            phone.setOnClickListener(new OnClickListener() {;
					public void onClick(View v) {
						// TODO Auto-generated method stub
						if(dataEmpId.containsKey(data.m_szSerialNo)) {
							if(dataEmpId.containsKey(data.m_szSerialNo)) {
								SKTUtil.log("xxxxxxxxx", "xxxxxxxxxxx dataEmpId.size() 1760 :: " +  dataEmpId.size());
								data.m_bIsChecked = false;
								phone.setImageResource(R.drawable.mail_member_check_off);
								dataEmpId.remove(data.m_szSerialNo);
			            		dataName.remove(data.m_szSerialNo);
			            		dataVvip.remove(data.m_szSerialNo);
			            		dataNumber.remove(data.m_szSerialNo);
			            		dataEmail.remove(data.m_szSerialNo);
							} else {
								data.m_bIsChecked = true;
								phone.setImageResource(R.drawable.mail_member_check_on);
								dataEmpId.put(data.m_szSerialNo, data.m_szEmpId);
								dataName.put(data.m_szSerialNo, data.m_szName);
								dataVvip.put(data.m_szSerialNo, data.m_szVvip);
								dataNumber.put(data.m_szSerialNo, data.m_szCellPhoneNo);
								dataEmail.put(data.m_szSerialNo, data.m_szEmail);
							}
						} else {
							Uri uri = Uri.parse("tel:" + data.m_szCellPhoneNo);
							Intent intent = new Intent(Intent.ACTION_DIAL, uri);
							startActivity(intent);
						}
					}
				});

//	            office.setOnClickListener(new OnClickListener() {
//					
//					@Override
//					public void onClick(View v) {
//						
//						Uri uri = Uri.parse("tel:" + data.m_szOfficePhoneNo);
//						Intent intent = new Intent(Intent.ACTION_DIAL, uri);
//						startActivity(intent);
//						
//					}
//				});
        	}
        	
            return convertView;
        }

        public long getItemId(int position)
        {
            return position;
        }

        public Object getItem(int position)
        {
            return mLastTeamData.get(position);
        }

        public int getCount()
        {
            return mLastTeamData.size();
        }
    };
    
    /**
     * 트리 구조 업데이트
     * @param index 트리 인덱스
     * @param isExpand 트리 확장 여부
     */
    private void updateTree(int index, boolean isExpand) {
    	TreeData data = (TreeData)mTreeView.getItemAtPosition(index);
    	int depth = data.m_nDepth;
    	int copyIndex = 0;

		for(copyIndex = 0; copyIndex < mCopyTreeAdapter.getCount(); copyIndex++) {
			if(data == mCopyTreeAdapter.getItem(copyIndex)) {
				break;
			}
		}
		
    	if(isExpand && data.m_bHasChild) 
    	{    		
    		if(copyIndex + 1 >= mCopyTreeAdapter.getCount()) 
    		{
    			tempReqCompanyCd = data.m_szCompanyCd;
    			requestData(PEOPLE_DEPT_ADD, data.m_szDept);    			
    			return;
    		}
    		
    		TreeData td = (TreeData)mCopyTreeAdapter.getItem(copyIndex + 1);
    		if(td.m_nDepth <= depth) 
    		{
    			tempReqCompanyCd = data.m_szCompanyCd;
    			requestData(PEOPLE_DEPT_ADD, data.m_szDept);
    			return;
    		}
    	}
    	
    	for(int i = copyIndex + 1; i < mCopyTreeAdapter.getCount(); i++) {
    		TreeData data1 = (TreeData)mCopyTreeAdapter.getItem(i);
    		
    		if(depth >= data1.m_nDepth) {
    			break;
    		} else {
    			if(isExpand) {
    				if(depth + 1 == data1.m_nDepth) {
    	    			data1.m_bIsVisible = isExpand;
    	    			data1.m_bIsExpanded = false;
    				}
    			} else {
	    			data1.m_bIsVisible = isExpand;
	    			data1.m_bIsExpanded = false;
    			}
    		}
    	}
    	
    	copyTreeAdapter();
    	mTreeAdapter.notifyDataSetChanged();
    }

	/* (non-Javadoc)
	 * 리스트의 아이템 클릭 이벤트 핸들러<br>
	 * - 트리 클릭
	 * @see android.widget.AdapterView.OnItemClickListener#onItemClick(android.widget.AdapterView, android.view.View, int, long)
	 */
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub
		mIsTreeView = false;
		findViewById(R.id.list_layout).setVisibility(View.VISIBLE);
		mTreeView.setVisibility(View.GONE);
		TreeData data = mTreeAdapter.getItem(arg2);
		tempReqCompanyCd = data.m_szCompanyCd;
		//		if(data.m_bHasChild) {
			requestData(PEOPLE_DEPT, data.m_szDept);
//		} else {
//			try {
//				requestData(PEOPLE_DEPTMEMBER, SKTUtil.getCheckedCompanyCd(this));
//			} catch(SKTException e) {
//				e.printStackTrace();
//			}
//		}
	}
    
    /* (non-Javadoc)
	 * @see android.app.Activity#onBackPressed()
	 */
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		if(mIsBackOff && !mIsTreeView) {
			mIsTreeView = true;
			changeViewType();
		} else {
			if (DeleteDB()) {
				super.onBackPressed();
			}
//			showCloseDlg();
		}
	}
	
//	private void showNoSearch(boolean isShow) {
//		View tempView;
//		
//		if(isShow) {
//			tempView = findViewById(R.id.last_view);
//			tempView.setVisibility(View.INVISIBLE);
//			tempView = findViewById(R.id.notice_text);
//			tempView.setVisibility(View.VISIBLE);
//		} else {
//			tempView = findViewById(R.id.last_view);
//			tempView.setVisibility(View.VISIBLE);
//			tempView = findViewById(R.id.notice_text);
//			tempView.setVisibility(View.INVISIBLE);
//		}
//	}
	
//	private void getLastSearch() {
//		mIsLastView = true;
//		mIsTreeView = false;
//		mLastTeamData.clear();
//		m_dbHelper = new MemberSearchSQLite(this);
//		m_dbMember = m_dbHelper.getWritableDatabase();
//		Cursor cs = null;
//
//		try {
//			cs = m_dbMember.query(MemberSearchSQLite.DATABASE_TABLE,
//					new String[] { "serial", "deptname", "name", "telnum", "role", "work",
//							"company", "phone", "email", "type", "companycd", "searchdate" }, null,
//					null, null, null, "searchdate desc");
//			
//			if (cs == null || cs.getCount() == 0) {
//				showNoSearch(true);
//			} else {
//				showNoSearch(false);
//				
//				addCurTeamData("", getString(R.string.recent_search), "", "", "", "");
//				
//				cs.moveToFirst();
//				do {
//					addCurTeamData(cs.getString(0), cs.getString(2), cs.getString(3),
//							cs.getString(4), cs.getString(1), cs.getString(6), cs.getString(9),
//							cs.getString(10), cs.getString(8), false);
//				} while(cs.moveToNext());
//			}
//		} catch(SQLiteException e) {
//			android.util.Log.e("MemberSearch", e.getMessage());
//		} finally {
//			if(cs != null) {
//				cs.close();
//			}
//		}
//		
//		m_dbMember.close();
//		
//		showLayout(LAST_LAYOUT);
//		mLastView.setAdapter(mLastViewAdapter);
//		mLastView.setOnItemClickListener(mLastClickListener);
//	}

	class TreeArrayAdapter extends ArrayAdapter<TreeData> {
    	public TreeArrayAdapter(Context context) {
    		super(context, 0);
    	}
    	
        public View getView(final int position, View convertView, ViewGroup parent)
        {
            if(convertView == null) {
                convertView = getLayoutInflater().inflate(R.layout.mail_member_row_dept, null);
            }
            
            final TreeData data = (TreeData)getItem(position);
            final ImageView expand = (ImageView)convertView.findViewById(R.id.button_expand);
            
            if(data.m_bHasChild) 
            {
            	expand.setClickable(true);
            	
	            expand.setOnClickListener(new OnClickListener()
	            {
					public void onClick(View v)
					{	
						temTreepData = data;
						tempView = expand;
						
						if(data.m_bIsExpanded) {
							data.m_bIsExpanded = false;
							expand.setImageResource(R.drawable.mail_member_arrow_diagram_down);
							
							updateTree(position, false);
						} else {
							data.m_bIsExpanded = true;
							expand.setImageResource(R.drawable.mail_member_arrow_diagram_right);
							
							updateTree(position, true);
						}
					}
				});
            } else {
            	expand.setClickable(false);
            }
            if(!data.m_bHasChild) {
            	
            	data.m_bIsExpanded = true;
            }
            if(data.m_bIsExpanded) {
            	expand.setImageResource(R.drawable.mail_member_arrow_diagram_right);
            } else {
            	expand.setImageResource(R.drawable.mail_member_arrow_diagram_down);
            }
            if(!data.m_bHasChild) {
            	expand.setImageResource(R.drawable.mail_member_arrow_diagram_end);
            } 

            
            TextView name = (TextView)convertView.findViewById(R.id.team_name);
            name.setText(data.m_szName);
            
            LinearLayout layout = (LinearLayout)convertView.findViewById(R.id.layout_dept);
            
            if(data.m_bIsVisible) {
            	layout.setVisibility(View.VISIBLE);
            } else {
            	layout.setVisibility(View.GONE);
            	return convertView;
            }

            while(layout.getChildAt(0).getId() != R.id.button_expand) {
            	layout.removeViewAt(0);
            }
            if(data.m_nDepth > 1) {
	            ImageView line = getLastLine(data);
            	layout.addView(line, 0);
            	if(data.m_nDepth > 2) {
            		for(int i = data.m_nDepth - 2; i > 0; i--) {
            			ImageView line1 = getMiddleLine(data, position, i);
            			layout.addView(line1, 0);
            		}
            	}
            }
            
            return convertView;
        }
        
        private ImageView getLastLine(TreeData data) {
        	ImageView line = new ImageView(getContext());
            LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT,
            		LayoutParams.FILL_PARENT);
        	params.gravity = Gravity.CENTER;
        	line.setLayoutParams(params);
        	line.setScaleType(ScaleType.FIT_XY);
        	line.setTag("line" + (data.m_nDepth - 1));
        	if(data.m_bIsLastItem) {
        		line.setImageResource(R.drawable.mail_member_dot_tree_01);
        	} else {
        		line.setImageResource(R.drawable.mail_member_dot_tree_02);
        	}
        	
        	return line;
        }
        
        private ImageView getMiddleLine(TreeData data, int position, int index) {
        	ImageView line = new ImageView(getContext());
            LayoutParams params = new LayoutParams(60, LayoutParams.FILL_PARENT);
        	params.gravity = Gravity.CENTER;
        	line.setLayoutParams(params);
        	line.setScaleType(ScaleType.FIT_XY);
        	line.setTag("line" + index);
        	
            while(--position != 0) {
            	TreeData data1 = (TreeData)getItem(position);
            	
            	if(data1.m_nDepth != index + 1) {
            		continue;
            	}
            	if(data1.m_bIsLastItem) {
            		line.setImageDrawable(null);
            	} else {
            		line.setImageResource(R.drawable.mail_member_dot_tree_03);
            	}
            	break;
            }
        	
        	return line;
        }
    }

	public boolean DeleteDB(){
		m_dbHelper = new MemberSearchSQLite(this);
		m_dbMember = m_dbHelper.getWritableDatabase();

		try 
		{
			m_dbMember.execSQL("DELETE FROM member where 1 = 1");
		} catch(Exception e) {
			e.printStackTrace();
			return false;
		}
		m_dbMember.close();
		m_dbHelper.close();
		return true;
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
	public boolean onKeyDown(int KeyCode, KeyEvent event) {
		
		super.onKeyDown(KeyCode, event);
		if (event.getAction() == KeyEvent.ACTION_DOWN) {
			switch (KeyCode) {
			case KeyEvent.KEYCODE_BACK: // 뒤로 키와 같은 기능을 한다.
				if (false) {
					Toast.makeText(this, "'뒤로' 버튼을 한번 더 누르시면 종료됩니다.",
							Toast.LENGTH_SHORT).show();
					// 버튼클릭시 true
					m_Flag = true;
					// Handler 호출 (2초 이후 back_Check 값 false)
					back_Handler.sendEmptyMessageDelayed(0, 2000);
					return false;
				} else {
					
					try{
						SKTUtil.runApp(this, "com.ex.group.folder");
					}catch(Exception e){
						e.printStackTrace();
					}						
					
					DeleteDB();
					moveTaskToBack(true);
					finish();
					return true;
				}
			}
			return false;
		}		
		return false;
	}		

}