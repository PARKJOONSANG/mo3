package com.ex.group.approval.easy.addressbook.activity;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.database.Cursor;
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
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.SimpleExpandableListAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.ex.group.folder.R;
import com.ex.group.approval.easy.addressbook.data.EmployeeData;
import com.ex.group.approval.easy.addressbook.data.MemberSearchSQLite;
import com.ex.group.approval.easy.addressbook.dialog.OptionDialog;
import com.ex.group.approval.easy.addressbook.dialog.SelectJobDialog;
import com.skt.pe.common.conf.Constants;
import com.skt.pe.common.data.AuthData;
import com.skt.pe.common.data.SKTUtil;
import com.skt.pe.common.exception.SKTException;
import com.skt.pe.common.service.Controller;
import com.skt.pe.common.service.Parameters;
import com.skt.pe.common.service.XMLData;
import com.skt.pe.crypto.seed.SeedCrypter;
import com.skt.pe.util.StringUtil;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;


/**
 * 멤버 목록/검색 리스트 화면
 * @author jokim
 *
 */
public class AddressSearchActivity extends BaseActivity implements OnGroupClickListener,
        OnItemLongClickListener, OnClickListener
{
	
	private final String TAG = "AddressSearchActivity";
	static Context thisContext;
	private final int START_INDEX = 0x1111;
	private String chosungs[] = {
			"가", "나", "다", "라", "마", "바", "사", "아", "자", "차", "카", "타", "파", "하", "A"
	};
	public final static String LAST_SEARCH_UPDATE =
		"com.sk.pe.group.membersearch.LAST_SEARCH_UPDATE";
	private final int CATEGORY_NAME = 0;
	private final int CATEGORY_TEAM = 1;
	private final int CATEGORY_WORK = 2;
	private final int CATEGORY_EMAIL = 3;
	private final int CATEGORY_MOBILE = 4;
	private final int CATEGORY_PHONE = 5;
	private final int CATEGORY_ID = 6;
	private final int CATEGORY_MAX = 7;

	private final int VIEW_LAST = 0;
	private final int MY_PROFILE = 1;
	private final int SEND_GROUP_SMS = 2;
	private final int SEND_GROUP_EMAIL = 3;
	private final int CALL_MOBILE = 4;
	private final int CALL_OFFICE = 5;
	private final int SEND_SMS = 6;
	private final int SEND_EMAIL = 7;
	private final int VIEW_CONTENT = 8;
	private final int SAVE_ADDR = 9;
	private final int VIEW_MY_TEAM = 10;

	String emailID = "";
	ArrayList<EmployeeData> dupJobUser;
	
	ExpandableListView mListView;
	MyListAdapter mAdapter;
	ArrayList<Map<String, EmployeeData>> groupData = null;

	MemberSearchSQLite m_dbHelper = null;
	SQLiteDatabase m_dbMember = null;

	int nTotalIndex = 0;
	int nCurrentPage = 1;
	int[] nListCount;
	int mLongIndex = 0;
	boolean mIsLastView = true;

	String myCompanyCd = "";
	String myId = "";
	String mMyCompany;
	String m_szCurSearch = "";
	String m_szListEnd = "Y";
	String mType;
	String[] mCompanyList;
	String[] mCompanyCode = new String[] { "" };
	int mCompanyIndex = 0;
	boolean isMultiSelect;
	boolean isSingleSelect;
	String orderType = "G";
	ProgressBar mBar;
	TextView mBarText;
	TextView mBarContent;
	TextView suffix_dept_text;
	LinearLayout mSearchbg_view;
	LinearLayout eFolder_view;
	Button mBarButton;
	boolean mIsDialogExit = false;
	boolean mIsWait = true;
	long mId = 0;
	EmployeeData tempEmployeeData = null;

	private String arg1 = "";
	private int currentMenu = VIEW_MY_TEAM;

	SelectJobDialog selectDialog;
	
	Handler mHandler = new Handler(new Callback() {
		public boolean handleMessage(Message msg) {
			if(msg.what == 0) {
				String[] cntString = ((String)msg.obj).split(":");
				int cnt = Integer.valueOf(cntString[0]);
				int max = Integer.valueOf(cntString[1]);
				mBar.setMax(max);
				mBar.setProgress(cnt);
				mBarText.setText((cnt * 100 / max) + " %");
			} else if(msg.what == 1) {
				showDialog(DIALOG_OVERWRITE_CONFIRM);
			} else if(msg.what == 2) {
				mBarContent.setText(R.string.easyaproval_copy_complete_addr);
				mBarButton.setText(R.string.easyaproval_ok_button);
			}
			return true;
		}
	});

	final int PEOPLE_MEMBERSEARCH = 0;
	final int PEOPLE_MEMBERSEARCH_LOGIN = 1;
	final int PEOPLE_COMPANY = 2;
	final int MAIL_CONTACTLIST = 3;
	final int PEOPLE_DEPTMEMBER = 4;
	final int MAILADV_GETUSERINFO = 5;
	
	LinearLayout m_frameLayout;               // 더 보기
	final int DIALOG_MEMBER_SEARCH = 1111;
	final int DIALOG_OVERWRITE_CONFIRM = 1112;
	final int DIALOG_NO_APP = 1113;

	OptionDialog mCompanyDialog = null;
	private String mApprovalType;
	private String mEmpType;

	@Override
	protected int assignLayout() 
	{
		mType = getIntent().getStringExtra("type");
		if(mType == null || "".equals(mType)) {
			mType = "M";
		}

		return R.layout.easy_member_main;
	}

	/**
	 * 회사 이름 리턴
	 * @return 회사 이름
	 * @throws SKTException
	 */
	public String getCompanyName() throws SKTException {
		String ret = "";

		try {
			String code = SKTUtil.getCheckedCompanyCd(this);
			Map<String, String> map = SKTUtil.getCompanyList(this);

			ret = map.get(code);
		} catch(SKTException e) {
			throw e;
		}

		return ret;
	}

	public void onCreateX(Bundle instanceState){
		super.onCreateX(instanceState);
		registerReceiver(new LastSearchUpdateReceiver(), new IntentFilter(LAST_SEARCH_UPDATE));
		
		thisContext = this;
		isMultiSelect = getIntent().getBooleanExtra("isMultiSelect", false);
		isSingleSelect = getIntent().getBooleanExtra("isSingleSelect", false);
		mApprovalType = getIntent().getStringExtra("approvalType");
		mEmpType = getIntent().getStringExtra("empType");
		arg1 = getIntent().getStringExtra("arg1");

		nListCount = new int[CATEGORY_MAX];
		mListView = (ExpandableListView)findViewById(R.id.MSMainList);
		mSearchbg_view = (LinearLayout)findViewById(R.id.searchbg_view);
		eFolder_view = (LinearLayout)findViewById(R.id.mail_folder_up);
		suffix_dept_text = (TextView)findViewById(R.id.suffix_dept_text);
		mListView.setGroupIndicator(null);
		mListView.setOnGroupClickListener(this);
		mListView.setOnItemLongClickListener(this);
		registerForContextMenu(mListView);

		m_frameLayout = (LinearLayout) LayoutInflater.from(this)
		.inflate(R.layout.easy_member_membersearchfooter, null);
		// Set up our adapter
		groupData = new ArrayList<Map<String, EmployeeData>>();
		if (groupData != null) {
			groupData.clear();
		}
		//구성원 검색시
		if("M".equals(mType)) {
			eFolder_view.setVisibility(View.GONE);
			mSearchbg_view.setVisibility(View.VISIBLE);
			mAdapter = new MyListAdapter(this, groupData, R.layout.easy_member_listsearchresultview,
					R.layout.easy_member_listsearchresultview, new String[] { "data" }, null, null,
					R.layout.easy_member_childview, R.layout.easy_member_childview, null, null);
		} 
		
		//연락처 검색시
		else if("E".equals(mType)) 
		{
			eFolder_view.setVisibility(View.GONE);
			mSearchbg_view.setVisibility(View.GONE);
			orderType = "G";
			mAdapter = new MyListAdapter(this, groupData, R.layout.easy_member_emaillistsearchresultview,
					R.layout.easy_member_hiportal_listview, new String[] { "data" }, null, null,
					R.layout.easy_member_childview, R.layout.easy_member_childview, null, null);
		}
		
		
		//		else if("G".equals(mType)) {
		//		mAdapter = new MyListAdapter(this, groupData, R.layout.member_grouplistsearchresultview,
		//				R.layout.member_grouplistsearchresultview, new String[] { "data" }, null, null,
		//				R.layout.member_childview, R.layout.member_childview, null, null);
		//	} 		

		//        mListView.setAdapter(mAdapter);
		
		if(isMultiSelect) {
			findViewById(R.id.multiselect_footer).setVisibility(View.VISIBLE);
			findViewById(R.id.multiselect_confirm).setOnClickListener(this);
			findViewById(R.id.multiselect_cancel).setOnClickListener(this);
		} else {
			findViewById(R.id.multiselect_footer).setVisibility(View.GONE);
		}

		onPostCreateX();

		// 구성원 검색 위젯으로 부터 검색어를 받았을 경우
		Intent intent = getIntent();
		String keyword = intent.getStringExtra("keyword");
		if(keyword != null && !"".equals(keyword)) {
			EditText et = (EditText) findViewById(R.id.MSMainSearchInputText);
			et.setText(keyword);
			//        	ImageView mUpButton = (ImageView)findViewById(R.id.MSMainSearchSearchButton);
			//        	mUpButton.performClick();
		} 

		if("M".equals(mType)) 
		{        		
			groupData.clear();

			try {
				SKTUtil.log(Log.DEBUG, "xxxxxxx", "xxxxxxxxxxxx  http ++++ 282   " );
				requestData(PEOPLE_DEPTMEMBER, SKTUtil.getCheckedCompanyCd(this));
			} catch(SKTException e) {
				e.printStackTrace();
			}
		}
		 
		if("E".equals(mType)) 
		{        		
			groupData.clear();

			requestData(MAIL_CONTACTLIST, "");

			eFolder_view.setVisibility(View.GONE);		//조직도 상위보기
			findViewById(R.id.up_button).setOnClickListener(new OnClickListener()
			{
				public void onClick(View view)
				{
					if ("M".equals(orderType)) {
						orderType = "G";
						requestData(MAIL_CONTACTLIST, "");
					} 
				}
			});
		}

		try {
			myId = SKTUtil.getAuthId(this).get(AuthData.ID_ID);
			myCompanyCd = SKTUtil.getCheckedCompanyCd(this);
		} catch (Exception e) {
			// TODO: handle exception
		}

	}

	public void onClick(View v)
	{		 		
		if(v.getId() == R.id.multiselect_confirm){
			ArrayList<EmployeeData> data = getSelectedNames();
			String[] names = new String[data.size()];
			String[] empids = new String[data.size()];
			String[] depts = new String[data.size()];
			String[] emails = new String[data.size()];
			String[] vvip	= new String[data.size()];
			String[] team	= new String[data.size()];
			String[] nfuId = new String[data.size()];

			for(int i = 0; i < data.size(); i++) {
				EmployeeData employee = data.get(i);
				names[i] = employee.m_szName;
				depts[i] = employee.m_szDepartment;
				team[i] = employee.m_szTeam;
				nfuId[i] = employee.m_nfuId;
				if ("E".equals(mType)) {
					emails[i] = employee.m_szMail;
					empids[i] = employee.m_szEmpId;
				}else{
					empids[i] = employee.m_szEmpId;
					emails[i] = employee.m_szMail;
				}
				vvip[i] = employee.m_szVvip.equals("Y") ? "true" : "false";

			}

			Intent intent = new Intent();
			intent.putExtra("names", names);
			intent.putExtra("empids", empids);
			intent.putExtra("nfuId", nfuId);
			intent.putExtra("depts", depts);
			intent.putExtra("emails", emails);
			intent.putExtra("vvip", vvip);
			intent.putExtra("suffixDept", team);
			intent.putExtra("arg1", arg1);
			AddressTabActivity parent = (AddressTabActivity)getParent();
			parent.setResult(RESULT_OK, intent);
			finish();
		} else if(v.getId() == R.id.multiselect_cancel) {
			AddressTabActivity parent = (AddressTabActivity)getParent();
			parent.setResult(RESULT_CANCELED);
			finish();
		} else if(v.getId() >= START_INDEX && v.getId() < (START_INDEX + chosungs.length)) {
			searchChosung(v.getId() - START_INDEX);
		}
	}

	/**
	 * 선택된 멤버 목록 리턴
	 * @return 선택된 멤버 목록
	 */
	private ArrayList<EmployeeData> getSelectedNames() {
		ArrayList<EmployeeData> data = new ArrayList<EmployeeData>();

		for(Map<String, EmployeeData> map: groupData) {
			EmployeeData employee = map.get("data");
			if(employee.isSelected) {
				data.add(employee);
			}
		}
		return data;
	}
	

	/**
	 * 회사 목록 팝업 출력
	 */
	private void showCompanyDialog() 
	{
		Configuration config = getResources().getConfiguration();

		mCompanyDialog = new OptionDialog(this, getString(R.string.easyaproval_company_option), mCompanyList,
				new OptionDialog.OnOptionClickListener() {
			public void onOptionClick(int index) {
				// TODO Auto-generated method stub
				if(index != -1) {
					mCompanyIndex = index;
					//							TextView view = (TextView)findViewById(R.id.groupText);
					//							view.setText(mCompanyList[mCompanyIndex]);
				}
			}
		}, mCompanyIndex);
		mCompanyDialog.setOwnerActivity(this);
		mCompanyDialog.show();
		onConfigurationChanged(config);
	}

	/* (non-Javadoc)
	 * @see android.app.Activity#onConfigurationChanged(android.content.res.Configuration)
	 */
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		// TODO Auto-generated method stub
		if(mCompanyDialog != null) {
			if(newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
				LinearLayout v = (LinearLayout)mCompanyDialog.findViewById(R.id.ROOT_VIEW);
				v.getLayoutParams().height = 400;
			} else {
				LinearLayout v = (LinearLayout)mCompanyDialog.findViewById(R.id.ROOT_VIEW);
				v.getLayoutParams().height = 537;
			}
		}

		super.onConfigurationChanged(newConfig);
	}

	protected void onPostCreateX() 
	{	

		ImageView mSearchButton = (ImageView)findViewById(R.id.MSMainSearchSearchButton);

		//서치버튼을 눌렀을시에 따른 동작을 정의한다.
		mSearchButton.setOnClickListener(new OnClickListener(){
			public void onClick(View view) {
				hideSoftInputWindow(view);

				EditText et = (EditText)findViewById(R.id.MSMainSearchInputText);
				m_szCurSearch = et.getText().toString();

				char[] digit = m_szCurSearch.toCharArray();
				int len = digit.length;

				if(len < 2) {
					m_szTitle = getString(R.string.easyaproval_error_input_search);
					m_szDialogMessage = getString(R.string.easyaproval_editbox_alert);
					showDialog(DIALOG_MEMBER_SEARCH);
					return;
				} else if(len < 3) {
					boolean isAllDigit = true;
					for(int i = 0; i < len; i++) {
						if(digit[i] < 0x30 || digit[i] > 0x39) {
							isAllDigit = false;
							break;
						}
					}
					if(isAllDigit) {
						m_szTitle = getString(R.string.easyaproval_error_input_search);
						m_szDialogMessage = getString(R.string.easyaproval_editbox_alert);
						showDialog(DIALOG_MEMBER_SEARCH);
						return;
					}
				}

				if(!StringUtil.checkKeyCode(m_szCurSearch)) {
					m_szTitle = getString(R.string.easyaproval_error_input_search);
					m_szDialogMessage = getString(R.string.easyaproval_editbox_alert2);
					showDialog(DIALOG_MEMBER_SEARCH);
					return;
				}

				//            	for (int i=0; i<len; i++) {
				//            		if ((0x30<=digit[i] && 0x39>=digit[i]) || (0x41<=digit[i] && 0x5A>=digit[i]) || (0x61<=digit[i] && 0x7A>=digit[i]) //
				//            				|| (0x41<=digit[i] && 0x5A>=digit[i]) || (0xAC00<=digit[i] && digit[i]<=0xD7A3) || (0x3131<=digit[i] && digit[i]<=0x318E)) {
				//            		}else {
				//            			m_szDialogMessage = "한글, 영문, 숫자만 입력 가능합니다.";
				//            			showDialog(DIALOG_MEMBER_SEARCH);
				//            			return;
				//            		}            			
				//            	}

				View tempView = null;
				tempView = findViewById(R.id.MSMainList);
				tempView.setVisibility(View.INVISIBLE);
				tempView = findViewById(R.id.MSMainNotice);
				tempView.setVisibility(View.INVISIBLE);

				nCurrentPage = 1;                  	
				if("E".equals(mType)) {
					requestData(MAIL_CONTACTLIST, m_szCurSearch);
				} else {            		
					requestData(PEOPLE_MEMBERSEARCH, m_szCurSearch);
				}
			}
		}); 

		EditText et = (EditText) findViewById(R.id.MSMainSearchInputText);
		et.setOnClickListener(new OnClickListener() {
			public void onClick(View view) {
				getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
			}
		});
		et.setOnKeyListener(new OnKeyListener() {
			public boolean onKey(View a_view, int a_nKeyCode, KeyEvent a_keyEvent) {
				if (a_nKeyCode == KeyEvent.KEYCODE_ENTER && a_keyEvent.getAction() == KeyEvent.ACTION_DOWN) {
					hideSoftInputWindow(a_view);

					EditText et = (EditText) findViewById(R.id.MSMainSearchInputText);
					m_szCurSearch = et.getText().toString();

					char[] digit = m_szCurSearch.toCharArray();
					int len = digit.length;
					if (len<2) {
						m_szTitle = getString(R.string.easyaproval_error_input_search);
						m_szDialogMessage = getString(R.string.easyaproval_editbox_alert);
						showDialog(DIALOG_MEMBER_SEARCH);
						return true;
					}else if (len<3) {
						boolean isAllDigit = true;
						for (int i=0; i<len; i++) {
							if (digit[i]<0x30 || digit[i]>0x39) {
								isAllDigit = false;
								break;
							}
						}
						if (isAllDigit) {
							m_szTitle = getString(R.string.easyaproval_error_input_search);
							m_szDialogMessage = getString(R.string.easyaproval_editbox_alert);
							showDialog(DIALOG_MEMBER_SEARCH);
							return true;
						}
					}
					if (!StringUtil.checkKeyCode(m_szCurSearch)) {
						m_szTitle = getString(R.string.easyaproval_error_input_search);
						m_szDialogMessage = getString(R.string.easyaproval_editbox_alert2);
						showDialog(DIALOG_MEMBER_SEARCH);
						return true;
					}

					View tempView = null;
					tempView = findViewById(R.id.MSMainList);
					tempView.setVisibility(View.INVISIBLE);
					tempView = findViewById(R.id.MSMainNotice);
					tempView.setVisibility(View.INVISIBLE);

					nCurrentPage = 1;
					if("E".equals(mType)) {
						requestData(MAIL_CONTACTLIST, m_szCurSearch);
					} else {
						requestData(PEOPLE_MEMBERSEARCH, m_szCurSearch);
					}

					return true;
				} // end if (a_nKeyCode == KeyEvent.KEYCODE_ENTER)

				return false;
			}
		});

		//		m_frameLayout.findViewById(R.id.MORE_BUTTON).setOnClickListener(new OnClickListener() {
		m_frameLayout.findViewById(R.id.MORE_BUTTON).setOnClickListener(new OnClickListener() {
			public void onClick(View view) {
				//            	View tempView = null;
				//				tempView = findViewById(R.id.MSMainList);
				//				tempView.setVisibility(View.INVISIBLE);
				//				tempView = findViewById(R.id.MSMainNotice);
				//				tempView.setVisibility(View.INVISIBLE);

				nCurrentPage++;
				LinearLayout layout = (LinearLayout)view;
				TextView child = (TextView)layout.findViewById(R.id.txt_footer);
				child.setText(R.string.easyaproval_text_loading);
				child.setCompoundDrawablesWithIntrinsicBounds(R.drawable.easy_member_icon_loading_off, 0, 0, 0);

				if("E".equals(mType)) {
					requestData(MAIL_CONTACTLIST, m_szCurSearch);
				} else {
					requestData(PEOPLE_MEMBERSEARCH, m_szCurSearch);
				}
			}
		});

	}

	protected void onResumeX() {
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
		EditText et = (EditText) findViewById(R.id.MSMainSearchInputText);
		SKTUtil.hideKeyboard(et);
	}		

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{		
		menu.add(0, VIEW_LAST, 0, R.string.easyaproval_viewlast);
		menu.add(0, VIEW_MY_TEAM, 0, R.string.easyaproval_label_view_mygroup);
		menu.add(0, MY_PROFILE, 0, R.string.easyaproval_my_profile);
		//		menu.add(0, SAVE_ADDR, 0, R.string.save_phone_addr);

		menu.add(0, SEND_GROUP_SMS, 0, R.string.easyaproval_send_group_sms);
		menu.add(0, SEND_GROUP_EMAIL, 0, R.string.easyaproval_send_group_email);

		return super.onCreateOptionsMenu(menu);
	}

	/* (non-Javadoc)
	 * @see com.skt.pe.common.activity.SKTActivity#onPrepareOptionsMenu(android.view.Menu)
	 */
	@Override
	public boolean onPrepareOptionsMenu(Menu menu)
	{
		for(int i = 0; i < mListView.getCount(); i++) {
			if(mListView.isGroupExpanded(i)) {
				return false;
			}
		}		

		if(!mType.equals("E"))
		{
			if(currentMenu == VIEW_MY_TEAM)
			{
				menu.findItem(VIEW_MY_TEAM).setVisible(false);
				menu.findItem(VIEW_LAST).setVisible(true);
			}
			else if(currentMenu == VIEW_LAST)
			{
				menu.findItem(VIEW_LAST).setVisible(false);
				menu.findItem(VIEW_MY_TEAM).setVisible(true);
			}

		}
		else
		{
			menu.findItem(VIEW_MY_TEAM).setVisible(false);
			menu.findItem(VIEW_LAST).setVisible(false);

			if ("G".equals(orderType)) {
				menu.findItem(SEND_GROUP_SMS).setVisible(false);
				menu.findItem(SEND_GROUP_EMAIL).setVisible(false);
			} else {
				menu.findItem(SEND_GROUP_SMS).setVisible(true);
				menu.findItem(SEND_GROUP_EMAIL).setVisible(true);
			}
		}

		if(isMultiSelect){
			menu.findItem(SEND_GROUP_SMS).setVisible(false);
			menu.findItem(SEND_GROUP_EMAIL).setVisible(false);
		}


		/*
		if(mType.equals("M")) {
			menu.findItem(VIEW_MY_TEAM).setVisible(true);
			menu.findItem(MY_PROFILE).setVisible(false);
		} else {
			menu.findItem(VIEW_MY_TEAM).setVisible(false);
			menu.findItem(MY_PROFILE).setVisible(true);
		}
		if(mType.equals("E") && !mIsLastView) {
			menu.findItem(SAVE_ADDR).setVisible(true);
		} else {
			menu.findItem(SAVE_ADDR).setVisible(false);
		}
		 */



		return super.onPrepareOptionsMenu(menu);
	}

	/* (non-Javadoc)
	 * 옵션 메뉴 클릭 이벤트 핸들러<br>
	 * - 최근 조회 목록<br>
	 * - 내 소속팀 보기<br>
	 * - 내 프로필<br>
	 * - 주소록에 저장<br>
	 * - 단체 문자 보내기<br>
	 * - 단체 이메일 보내기
	 * @see android.app.Activity#onMenuItemSelected(int, android.view.MenuItem)
	 */
	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		Intent intent = null;

		if(item.getItemId() == VIEW_LAST) {
			currentMenu = VIEW_LAST;

			if(!mIsLastView) {
				getLastSearch();
				if(mListView.getFooterViewsCount() > 0) {
					mListView.removeFooterView(m_frameLayout);
				}
				mIsLastView = true;
				mListView.setAdapter(mAdapter);
			}

			findViewById(R.id.index_list).setVisibility(View.GONE);

			return true;
		} else if(item.getItemId() == VIEW_MY_TEAM) {
			currentMenu = VIEW_MY_TEAM;

			groupData.clear();
			try {
				requestData(PEOPLE_DEPTMEMBER, SKTUtil.getCheckedCompanyCd(this));
			} catch(SKTException e) {
				e.printStackTrace();
			}

			return true;
		} else if(item.getItemId() == MY_PROFILE) {

			intent = new Intent(this, AddressInfoActivity.class);
			intent.putExtra("employee", myId);
			intent.putExtra("companyCode", myCompanyCd);
			startActivityForResult(intent, 1);
			return true;
		} else if(item.getItemId() == SAVE_ADDR) {
			if(groupData.size() == 0 || nTotalIndex == 0) {
				return true;
			}
			mIsDialogExit = false;
			final EmployeeData[] employee = new EmployeeData[nTotalIndex];
			for(int i = 0; i < groupData.size(); i++) {
				Map<String, EmployeeData> map = groupData.get(i);
				EmployeeData data = map.get("data");
				employee[i] = data;
			}
			final Dialog dialog = new Dialog(this);
			dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
			dialog.setContentView(R.layout.easy_member_progress_copy_addr);
			mBar = (ProgressBar)dialog.findViewById(R.id.progress_bar);
			mBarText = (TextView)dialog.findViewById(R.id.progress_text);
			mBarContent = (TextView)dialog.findViewById(R.id.progress_content);
			mBarButton = (Button)dialog.findViewById(R.id.progress_btn);
			mBarButton.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					// TODO Auto-generated method stub
					if(mIsDialogExit) {
						Intent intent = new Intent("com.android.contacts.action.LIST_DEFAULT");
						startActivity(intent);
					}
					mIsDialogExit = true;
					dialog.dismiss();
				}
			});
			dialog.show();
			new Thread(new Runnable() {
				public void run() {
					// TODO Auto-generated method stub
					for(int i = 0; i < employee.length; i++) {
						if(employee[i].m_szName == null || employee[i].m_szName.equals("")) {
							continue;
						}
						if(mIsDialogExit) {
							break;
						}
						mId = AddressInfoActivity.existContact(AddressSearchActivity.this,
								employee[i]);

						if(mId > 0) {
							m_szTitle = getString(R.string.easyaproval_title_save_phone);
							m_szDialogMessage = employee[i].m_szName +
							getString(R.string.easyaproval_text_overwrite);
							tempEmployeeData = employee[i];
							mHandler.sendEmptyMessage(1);
							while(mIsWait) {}
							mIsWait = true;
						} else {
							AddressInfoActivity.createContactEntry(AddressSearchActivity.this,
									employee[i], 0);
						}
						Message msg = mHandler.obtainMessage(0, (i + 1) + ":" + employee.length);
						mHandler.sendMessage(msg);
					}
					mHandler.sendEmptyMessage(2);
					mIsDialogExit = true;
				}
			}).start();
		} else if(item.getItemId() == SEND_GROUP_SMS) {
			Log.d("xxxxxx", "xxxxxxxxxxxxxx  222222222222" );
			if(groupData.size() == 0) {
				return true;
			}
			intent = new Intent(AddressSearchActivity.this, AddressMultiSelectActivity.class);
			String[] names = new String[nTotalIndex];
			String[] codes = new String[nTotalIndex];
			String[] infos = new String[nTotalIndex];
			String[] team = new String[nTotalIndex];
			String[] role = new String[nTotalIndex];
			String[] vvip = new String[nTotalIndex];

			boolean[] isData = new boolean[nTotalIndex];
			int i = 0;

			for(Map<String, EmployeeData> map: groupData) {
				EmployeeData data = map.get("data");
				if (!"E".equals(mType)) {		
					if(data.m_szSerialNo.equals("")) {
						continue;
					}
				} 

				names[i] = data.m_szName;
				codes[i] = data.m_szCellPhoneNo;
				infos[i] = data.m_szCellPhoneNo;
				isData[i] = data.m_szCellPhoneNo.equals("") ? false : true;
				intent.putExtra("type", "sms");
				role[i] = data.m_szRole;
				team[i] = data.m_szTeam;
				vvip[i] = data.m_szVvip;
				i++;
				 
				Log.d("xxxxxx", "xxxxxx  data.m_szCellPhoneNo  : "  +data.m_szCellPhoneNo );
				Log.d("xxxxxx", "xxxxxx  m_szOfficePhoneNo  : "  +data.m_szOfficePhoneNo );
				
			}
			intent.putExtra("names", names);
			intent.putExtra("codes", codes);
			intent.putExtra("infos", infos);
			intent.putExtra("isData", isData);
			intent.putExtra("role", role);
			intent.putExtra("team", team);
			intent.putExtra("vvip", vvip);
			intent.putExtra("isChild", false);
			if(mType.equals("E")) {
				intent.putExtra("useEmail", true);
			}
			startActivityForResult(intent, 1);
			return true;
		} else if(item.getItemId() == SEND_GROUP_EMAIL) {
			if(groupData.size() == 0) {
				return true;
			}
			intent = new Intent(AddressSearchActivity.this, AddressMultiSelectActivity.class);
			//			int cnt = 0;
			int total = nTotalIndex;
			//			if(mType.equals("E")) {
			//				for(Map<String, EmployeeData> map: groupData) {
			//					EmployeeData data = map.get("data");
			//					if(data.m_szInnerMail.equals("")) {
			//						continue;
			//					}
			//					cnt++;
			//				}
			//				total = cnt;
			//			}
			String[] team = new String[total];
			String[] role = new String[total];

			String[] names = new String[total];
			String[] codes = new String[total];
			String[] infos = new String[total];
			boolean[] isData = new boolean[total];
			String[] vvip	 = new String[total];
			int i = 0;
			for(Map<String, EmployeeData> map: groupData) {
				EmployeeData data = map.get("data");
				if(data.m_szMail.equals("")) {
					continue;
				}
				names[i] = data.m_szName;
				SKTUtil.log(Log.DEBUG, "xxxxxxx", "Xxxxxxxxxxxx m_szMail " + data.m_szMail);
				codes[i] = data.m_szMail;
				//				}
				if(mType.equals("E")) {
					team[i] = data.m_szDepartment;
				} else {
					team[i] = data.m_szTeam;
				}
				role[i] = data.m_szRole;
				infos[i] = data.m_szMail;
				SKTUtil.log(Log.DEBUG, "Xxxxxxxxxxx", "xxxxxxxxxx" + infos[i]);
				SKTUtil.log(Log.DEBUG, "Xxxxxxxxxxx", "xxxxxxxxxx" + codes[i]);
				isData[i] = data.m_szMail.equals("") ? false : true;
				//				}
				String tempVvip = null;
				if("Y".equals(data.getVvip())){
					tempVvip = "true";
				}else{
					tempVvip = "false";
				}
				vvip[i] = tempVvip;
				i++;
			}

			intent.putExtra("type", "email");
			intent.putExtra("names", names);
			intent.putExtra("codes", codes);
			intent.putExtra("infos", infos);
			intent.putExtra("isData", isData);
			intent.putExtra("role", role);
			intent.putExtra("team", team);
			intent.putExtra("isChild", false);
			if(mType.equals("E")) {
				intent.putExtra("useEmail", true);
			}
			startActivityForResult(intent, 1);
			return true;
		}

		return super.onMenuItemSelected(featureId, item);
	}

	protected Dialog onCreateDialog(int a_nId) {
		Dialog aDialog = null;

		switch (a_nId) {
		case DIALOG_NO_APP:
		case DIALOG_MEMBER_SEARCH:
			aDialog = createDialog(a_nId, m_szDialogMessage, DIALOG_ONE_BUTTON,
					getString(R.string.easyaproval_ok_button), null, null);
			break;
		case DIALOG_OVERWRITE_CONFIRM:
			aDialog = createDialog(a_nId, m_szDialogMessage, DIALOG_TWO_BUTTON,
					getString(R.string.easyaproval_ok_button), getString(R.string.easyaproval_cancel_button), null);
			aDialog.setCancelable(false);
		} // end switch (a_nId)

		return aDialog;
	}

	/* (non-Javadoc)
	 * 팝업 버튼 클릭 이벤트 핸들러
	 * @see com.sk.pe.group.activity.BaseActivity#onClickDialog(int, java.lang.String, int)
	 */
	public void onClickDialog(int a_nDialogId, String a_szButtonText, int a_nClickedButton) {
		switch (a_nDialogId) {
		case DIALOG_OVERWRITE_CONFIRM:
			if(a_nClickedButton == DIALOG_CLICKED_BUTTON_FIRST) {
				AddressInfoActivity.deleteContact(this, mId);
				AddressInfoActivity.createContactEntry(this, tempEmployeeData, mId);
			}
			mIsWait = false;
			break;
		case DIALOG_NO_APP:
			Intent intent = new Intent(Constants.Action.STORE_DETAIL_VIEW);
			intent.putExtra("APP_ID", "MOGP000001");
			startActivity(intent);
			break;
		} // end switch (a_nDialogId)
	} // end public void onClickDialog(int a_nDialogId, String a_szButtonText, int a_nClickedButton)

	/**
	 * 문자의 초성 리턴
	 * @param str 문자
	 * @return 문자의 초성
	 */
	private char getChosung(String str) {
		char comVal = str.charAt(0);

		if(comVal >= 0xAC00) {
			char uniVal = (char)(comVal - 0xAC00);
			comVal = (char)((((uniVal - (uniVal % 28)) / 28) / 21) + 0x1100);
		}

		return comVal;
	}

	/**
	 * 목록 우측에 초성 리스트 출력
	 */
	private void addChosungView() {
		LinearLayout indexList = (LinearLayout)findViewById(R.id.index_list);
		indexList.removeAllViews();

		View v = new View(this);
		LinearLayout.LayoutParams vparams =
			new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 40);
		v.setOnClickListener(this);
		indexList.addView(v, vparams);
		for(int i = 0; i < chosungs.length; i++) {
			TextView tv = new TextView(this);
			tv.setId(START_INDEX + i);
			tv.setText(chosungs[i]);
			tv.setTextColor(Color.BLACK);
			tv.setBackgroundResource(android.R.drawable.list_selector_background);
			tv.setGravity(Gravity.CENTER);
			tv.setOnClickListener(this);
			LinearLayout.LayoutParams params =
				new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0);
			params.weight = 1.0f;

			indexList.addView(tv, params);
		}
		View v1 = new View(this);
		vparams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 40);
		v1.setOnClickListener(this);
		indexList.addView(v1, vparams);

		indexList.setVisibility(View.VISIBLE);
	}

	private void setIndexList() {
		//		addChosungView();
	}

	/**
	 * 초성 검색
	 * @param index 초성의 인덱스
	 */
	private void searchChosung(int index) {
		for(int i = 0; i < mListView.getCount(); i++) {
			Map<String, EmployeeData> map = groupData.get(i);
			EmployeeData data = map.get("data");
			char cho = getChosung(data.m_szName);

			if(cho == getChosung(chosungs[index]) ||
					(index == chosungs.length - 1 &&
							((cho >= 0x41 && cho <= 0x5A) || (cho >= 61 && cho <= 0x7A)))) {
				mListView.setSelection(i);
				break;
			}
		}
	}

	/* (non-Javadoc)
	 * 액션 처리 후 UI 세팅<br>
	 * - 멤버 검색 요청<br>
	 * - 회사 목록 요청<br>
	 * - 아웃룩 주소록 요청<br>
	 * - 내 소속팀 요청
	 * @see com.sk.pe.group.activity.BaseActivity#onPostThread(com.skt.pe.common.service.XMLData, com.skt.pe.common.exception.SKTException)
	 * onActionPost와 동일한 기능을 수행한다.
	 */
	public void onPostThread(XMLData a_XMLData, SKTException a_exception) {
		// 정상 응답이  발생하였으면 오류 메시지 보여주고 리턴
		int titleNum = 0;
		TextView child;

		if (a_exception != null) {
			m_szTitle = getString(R.string.easyaproval_text_error);
			m_szDialogMessage = a_exception.getMessage();
			showDialog(DIALOG_MEMBER_SEARCH);

			//    		child = (TextView)m_frameLayout.getChildAt(0);
			child = (TextView)m_frameLayout.findViewById(R.id.txt_footer);
			child.setCompoundDrawablesWithIntrinsicBounds(R.drawable.easy_member_btn_footer_selector, 0, 0, 0);
			child.setText(R.string.easyaproval_label_footer);
			return;
		} // end else if (m_szResponseCode != null && m_szResponseCode.equals("1000") == false)

		switch (m_nRequestType){
		case PEOPLE_MEMBERSEARCH:
			try {
				if(nCurrentPage == 1) {
					nTotalIndex = 0;
					groupData.clear();
					for(int i = 0; i < CATEGORY_MAX; i++) {
						nListCount[i] = 0;
					}
				}

				m_szListEnd = a_XMLData.get("end");
				m_szResponseCode = a_XMLData.get("result");
				m_szResponseMessage = a_XMLData.get("resultMessage");

				XMLData tempXml;
				String nullCheck;

				try {
					tempXml = a_XMLData.getChild("nameResult");
				} catch(SKTException e) {
					tempXml = null;
				}
				if(tempXml != null) {
					nullCheck = tempXml.get("listCnt");
					if(nullCheck != null) {
						tempXml.setList("people");
						int len = tempXml.size();
						if(len > 0) {

							for (int i=0; i<len; i++, nTotalIndex++) {
								if(nCurrentPage == 1) {
									addCurTeamData(
											tempXml.get(i, "id"),
											tempXml.get(i, "deptCode"),
											tempXml.get(i, "suffixDept"),
											tempXml.get(i, "name"), "","",
											tempXml.get(i, "phone"), 
											tempXml.get(i, "telNum"),
											tempXml.get(i, "email"),
											tempXml.get(i, "outEmail"),
											tempXml.get(i, "innerTelNum"),
											tempXml.get(i, "work"),
											tempXml.get(i, "companyNm"),
											tempXml.get(i, "companyCd"),
											tempXml.get(i, "role"), 
											"M".equals(mType) ? "" : tempXml.get(i, "role"),
													tempXml.get(i, "empId"),
													tempXml.get(i, "engName"),
													tempXml.get(i, "vvip"),
													tempXml.get(i, "teamManager"));
								} else {
									insertCurTeamData(nListCount[CATEGORY_NAME] + i ,
											tempXml.get(i, "id"),
											tempXml.get(i, "deptCode"),
											tempXml.get(i, "suffixDept"),
											tempXml.get(i, "name"), "","",
											tempXml.get(i, "phone"), 
											tempXml.get(i, "telNum"),
											tempXml.get(i, "email"),
											tempXml.get(i, "outEmail"),
											tempXml.get(i, "innerTelNum"),
											tempXml.get(i, "work"),
											tempXml.get(i, "companyNm"),
											tempXml.get(i, "companyCd"),
											tempXml.get(i, "role"), 
											"M".equals(mType) ? "" : tempXml.get(i, "role"),
													tempXml.get(i, "empId"),
													tempXml.get(i, "engName"),
													tempXml.get(i, "vvip"),
													tempXml.get(i, "teamManager"));
								}
							}
							nListCount[CATEGORY_NAME] += len;
						}
					}
				}
				if(nListCount[CATEGORY_NAME] > 0) {
					titleNum++;
				}

				try {
					tempXml = a_XMLData.getChild("teamResult");
				} catch(SKTException e) {
					tempXml = null;
				}
				if(tempXml != null) {
					nullCheck = tempXml.get("listCnt");
					if(nullCheck != null) {
						tempXml.setList("people");
						int len = tempXml.size();
						if(len > 0) {

							for (int i=0; i<len; i++, nTotalIndex++) {
								if(nCurrentPage == 1) {
									addCurTeamData( 
											tempXml.get(i, "id"),
											tempXml.get(i, "deptCode"),
											tempXml.get(i, "suffixDept"),
											tempXml.get(i, "name"), "","",
											tempXml.get(i, "phone"), 
											tempXml.get(i, "telNum"),
											tempXml.get(i, "email"),
											tempXml.get(i, "outEmail"),
											tempXml.get(i, "innerTelNum"),
											tempXml.get(i, "work"),
											tempXml.get(i, "companyNm"),
											tempXml.get(i, "companyCd"),
											tempXml.get(i, "role"), 
											"M".equals(mType) ? "" : tempXml.get(i, "role"),
													tempXml.get(i, "empId"),
													tempXml.get(i, "engName"),
													tempXml.get(i, "vvip"),
													tempXml.get(i, "teamManager"));
								} else {
									insertCurTeamData(nListCount[CATEGORY_NAME]
									                             + nListCount[CATEGORY_TEAM] + i + titleNum , 
									                             tempXml.get(i, "id"),
									                             tempXml.get(i, "deptCode"),
									                             tempXml.get(i, "suffixDept"),
									                             tempXml.get(i, "name"), "","",
									                             tempXml.get(i, "phone"), 
									                             tempXml.get(i, "telNum"),
									                             tempXml.get(i, "email"),
									                             tempXml.get(i, "outEmail"),
									                             tempXml.get(i, "innerTelNum"),
									                             tempXml.get(i, "work"),
									                             tempXml.get(i, "companyNm"),
									                             tempXml.get(i, "companyCd"),
									                             tempXml.get(i, "role"), 
									                             "M".equals(mType) ? "" : tempXml.get(i, "role"),
									                            		 tempXml.get(i, "empId"),
									                            		 tempXml.get(i, "engName"),
									                            		 tempXml.get(i, "vvip"),
									                            		 tempXml.get(i, "teamManager"));
								}
							}
							nListCount[CATEGORY_TEAM] += len;
						}
					}
				}
				if(nListCount[CATEGORY_TEAM] > 0) {
					titleNum++;
				}

				try {
					tempXml = a_XMLData.getChild("workResult");
				} catch(SKTException e) {
					tempXml = null;
				}
				if(tempXml != null) {
					nullCheck = tempXml.get("listCnt");
					if(nullCheck != null) {
						tempXml.setList("people");
						int len = tempXml.size();
						if(len > 0) {

							for (int i=0; i<len; i++, nTotalIndex++) {
								if(nCurrentPage == 1) {
									addCurTeamData(	
											tempXml.get(i, "id"),
											tempXml.get(i, "deptCode"),
											tempXml.get(i, "suffixDept"),
											tempXml.get(i, "name"), "","",
											tempXml.get(i, "phone"), 
											tempXml.get(i, "telNum"),
											tempXml.get(i, "email"),
											tempXml.get(i, "outEmail"),
											tempXml.get(i, "innerTelNum"),
											tempXml.get(i, "work"),
											tempXml.get(i, "companyNm"),
											tempXml.get(i, "companyCd"),
											tempXml.get(i, "role"), 
											"M".equals(mType) ? "" : tempXml.get(i, "role"),
													tempXml.get(i, "empId"),
													tempXml.get(i, "engName"),
													tempXml.get(i, "vvip"),
													tempXml.get(i, "teamManager"));
								} else {
									insertCurTeamData(nListCount[CATEGORY_NAME]
									                             + nListCount[CATEGORY_TEAM]
									                                          + nListCount[CATEGORY_WORK] + i + titleNum ,	
									                                          tempXml.get(i, "id"),
									                                          tempXml.get(i, "deptCode"),
									                                          tempXml.get(i, "suffixDept"),
									                                          tempXml.get(i, "name"), "","",
									                                          tempXml.get(i, "phone"), 
									                                          tempXml.get(i, "telNum"),
									                                          tempXml.get(i, "email"),
									                                          tempXml.get(i, "outEmail"),
									                                          tempXml.get(i, "innerTelNum"),
									                                          tempXml.get(i, "work"),
									                                          tempXml.get(i, "companyNm"),
									                                          tempXml.get(i, "companyCd"),
									                                          tempXml.get(i, "role"), 
									                                          "M".equals(mType) ? "" : tempXml.get(i, "role"),
									                                        		  tempXml.get(i, "empId"),
									                                        		  tempXml.get(i, "engName"),
									                                        		  tempXml.get(i, "vvip"),
									                                        		  tempXml.get(i, "teamManager"));
								}
							}
							nListCount[CATEGORY_WORK] += len;
						}
					}
				}
				if(nListCount[CATEGORY_WORK] > 0) {
					titleNum++;
				}

				try {
					tempXml = a_XMLData.getChild("emailResult");
				} catch(SKTException e) {
					tempXml = null;
				}
				if(tempXml != null) {
					nullCheck = tempXml.get("listCnt");
					if(nullCheck != null) {
						tempXml.setList("people");
						int len = tempXml.size();
						if (len>0) {

							for (int i=0; i<len; i++, nTotalIndex++) {
								if(nCurrentPage == 1) {
									addCurTeamData(				    					
											tempXml.get(i, "id"),
											tempXml.get(i, "deptCode"),
											tempXml.get(i, "suffixDept"),
											tempXml.get(i, "name"), "","",
											tempXml.get(i, "phone"), 
											tempXml.get(i, "telNum"),
											tempXml.get(i, "email"),
											tempXml.get(i, "outEmail"),
											tempXml.get(i, "innerTelNum"),
											tempXml.get(i, "work"),
											tempXml.get(i, "companyNm"),
											tempXml.get(i, "companyCd"),
											tempXml.get(i, "role"), 
											"M".equals(mType) ? "" : tempXml.get(i, "role"),
													tempXml.get(i, "empId"),
													tempXml.get(i, "engName"),
													tempXml.get(i, "vvip"),
													tempXml.get(i, "teamManager"));
								} else {
									insertCurTeamData(nListCount[CATEGORY_NAME]
									                             + nListCount[CATEGORY_TEAM]
									                                          + nListCount[CATEGORY_WORK]
									                                                       + nListCount[CATEGORY_EMAIL]
									                                                                    + i + titleNum ,
									                                                                    tempXml.get(i, "id"),
									                                                                    tempXml.get(i, "deptCode"),
									                                                                    tempXml.get(i, "suffixDept"),
									                                                                    tempXml.get(i, "name"), "","",
									                                                                    tempXml.get(i, "phone"), 
									                                                                    tempXml.get(i, "telNum"),
									                                                                    tempXml.get(i, "email"),
									                                                                    tempXml.get(i, "outEmail"),
									                                                                    tempXml.get(i, "innerTelNum"),
									                                                                    tempXml.get(i, "work"),
									                                                                    tempXml.get(i, "companyNm"),
									                                                                    tempXml.get(i, "companyCd"),
									                                                                    tempXml.get(i, "role"), 
									                                                                    "M".equals(mType) ? "" : tempXml.get(i, "role"),
									                                                                    		tempXml.get(i, "empId"),
									                                                                    		tempXml.get(i, "engName"),
									                                                                    		tempXml.get(i, "vvip"),
									                                                                    		tempXml.get(i, "teamManager"));
								}
							}
							nListCount[CATEGORY_EMAIL] += len;
						}
					}
				}
				if(nListCount[CATEGORY_EMAIL] > 0) {
					titleNum++;
				}

				try {
					tempXml = a_XMLData.getChild("mobileResult");
				} catch(SKTException e) {
					tempXml = null;
				}
				if(tempXml != null) {
					nullCheck = tempXml.get("listCnt");
					if(nullCheck != null) {
						tempXml.setList("people");
						int len = tempXml.size();
						if (len>0) {							
							for (int i=0; i<len; i++, nTotalIndex++) {
								if(nCurrentPage == 1) {
									addCurTeamData(				    					
											tempXml.get(i, "id"),
											tempXml.get(i, "deptCode"),
											tempXml.get(i, "suffixDept"),
											tempXml.get(i, "name"), 
											"",
											"",
											tempXml.get(i, "phone"), 
											tempXml.get(i, "telNum"),
											tempXml.get(i, "email"),
											tempXml.get(i, "outEmail"),
											tempXml.get(i, "innerTelNum"),
											tempXml.get(i, "work"),
											tempXml.get(i, "companyNm"),
											tempXml.get(i, "companyCd"),
											tempXml.get(i, "role"), 
											"M".equals(mType) ? "" : tempXml.get(i, "role"),
													tempXml.get(i, "empId"),
													tempXml.get(i, "engName"),
													tempXml.get(i, "vvip"),
													tempXml.get(i, "teamManager"));

								} else {
									insertCurTeamData(nListCount[CATEGORY_NAME]
									                             + nListCount[CATEGORY_TEAM]
									                                          + nListCount[CATEGORY_WORK]
									                                                       + nListCount[CATEGORY_EMAIL]
									                                                                    + nListCount[CATEGORY_MOBILE]
									                                                                                 + i + titleNum ,
									                                                                                 tempXml.get(i, "id"),
									                                                                                 tempXml.get(i, "deptCode"),
									                                                                                 tempXml.get(i, "suffixDept"),
									                                                                                 tempXml.get(i, "name"), "","",
									                                                                                 tempXml.get(i, "phone"), 
									                                                                                 tempXml.get(i, "telNum"),
									                                                                                 tempXml.get(i, "email"),
									                                                                                 tempXml.get(i, "outEmail"),
									                                                                                 tempXml.get(i, "innerTelNum"),
									                                                                                 tempXml.get(i, "work"),
									                                                                                 tempXml.get(i, "companyNm"),
									                                                                                 tempXml.get(i, "companyCd"),
									                                                                                 tempXml.get(i, "role"), 
									                                                                                 "M".equals(mType) ? "" : tempXml.get(i, "role"),
									                                                                                		 tempXml.get(i, "empId"),
									                                                                                		 tempXml.get(i, "engName"),
									                                                                                		 tempXml.get(i, "vvip"),
									                                                                                		 tempXml.get(i, "teamManager"));
								}
							}
							nListCount[CATEGORY_MOBILE] += len;
						}
					}
				}
				if(nListCount[CATEGORY_MOBILE] > 0) {
					titleNum++;
				}

				try {
					tempXml = a_XMLData.getChild("phoneResult");
				} catch(SKTException e) {
					tempXml = null;
				}
				if(tempXml != null) {
					nullCheck = tempXml.get("listCnt");
					if(nullCheck != null) {
						tempXml.setList("people");
						int len = tempXml.size();
						if (len>0) {							
							for (int i=0; i<len; i++, nTotalIndex++) {
								if(nCurrentPage == 1) {
									addCurTeamData(				    			
											tempXml.get(i, "id"),
											tempXml.get(i, "deptCode"),
											tempXml.get(i, "suffixDept"),
											tempXml.get(i, "name"), "", "",
											tempXml.get(i, "phone"), 
											tempXml.get(i, "telNum"),
											tempXml.get(i, "email"),
											tempXml.get(i, "outEmail"),
											tempXml.get(i, "innerTelNum"),
											tempXml.get(i, "work"),
											tempXml.get(i, "companyNm"),
											tempXml.get(i, "companyCd"),
											tempXml.get(i, "role"),"",
											tempXml.get(i, "empId"),
											tempXml.get(i, "engName"),
											tempXml.get(i, "vvip"),
											tempXml.get(i, "teamManager"));
								} else {
									insertCurTeamData(nListCount[CATEGORY_NAME]
			                             + nListCount[CATEGORY_TEAM]
                                          + nListCount[CATEGORY_WORK]
									                                                       + nListCount[CATEGORY_EMAIL]
									                                                                    + nListCount[CATEGORY_MOBILE]
									                                                                                 + nListCount[CATEGORY_PHONE]
									                                                                                              + i + titleNum ,
									                                                                                              tempXml.get(i, "id"),
									                                                                                              tempXml.get(i, "deptCode"),
									                                                                                              tempXml.get(i, "suffixDept"),
									                                                                                              tempXml.get(i, "name"), "", "",
									                                                                                              tempXml.get(i, "phone"), 
									                                                                                              tempXml.get(i, "telNum"),
									                                                                                              tempXml.get(i, "email"),
									                                                                                              tempXml.get(i, "outEmail"),
									                                                                                              tempXml.get(i, "innerTelNum"),
									                                                                                              tempXml.get(i, "work"),
									                                                                                              tempXml.get(i, "companyNm"),
									                                                                                              tempXml.get(i, "companyCd"),
									                                                                                              tempXml.get(i, "role"),"",
									                                                                                              tempXml.get(i, "empId"),
									                                                                                              tempXml.get(i, "engName"),
									                                                                                              tempXml.get(i, "vvip"),
									                                                                                              tempXml.get(i, "teamManager"));
								}
							}
							nListCount[CATEGORY_PHONE] += len;
						}
					}
				}
				if(nListCount[CATEGORY_PHONE] > 0) {
					titleNum++;
				}

				try {
					tempXml = a_XMLData.getChild("idResult");
				} catch(SKTException e) {
					tempXml = null;
				}
				if(tempXml != null) {
					nullCheck = tempXml.get("listCnt");
					if(nullCheck != null) {
						tempXml.setList("people");
						int len = tempXml.size();
						if (len>0) 
						{						
							for (int i=0; i<len; i++, nTotalIndex++) {
								if(nCurrentPage == 1) {
									addCurTeamData(				    					
											tempXml.get(i, "id"),
											tempXml.get(i, "deptCode"),
											tempXml.get(i, "suffixDept"),
											tempXml.get(i, "name"), "","",
											tempXml.get(i, "phone"), 
											tempXml.get(i, "telNum"),
											tempXml.get(i, "email"),
											tempXml.get(i, "outEmail"),
											tempXml.get(i, "innerTelNum"),
											tempXml.get(i, "work"),
											tempXml.get(i, "companyNm"),
											tempXml.get(i, "companyCd"),
											tempXml.get(i, "role"),"",
											tempXml.get(i, "empId"),
											tempXml.get(i, "engName"),
											tempXml.get(i, "vvip"),
											tempXml.get(i, "teamManager"));
								} else {
									insertCurTeamData(nListCount[CATEGORY_NAME]
									                             + nListCount[CATEGORY_TEAM]
									                                          + nListCount[CATEGORY_WORK]
									                                                       + nListCount[CATEGORY_EMAIL]
									                                                                    + nListCount[CATEGORY_MOBILE]
									                                                                                 + nListCount[CATEGORY_PHONE]
									                                                                                              + nListCount[CATEGORY_ID]
									                                                                                                           + i + titleNum ,
									                                                                                                           tempXml.get(i, "id"),
									                                                                                                           tempXml.get(i, "deptCode"),
									                                                                                                           tempXml.get(i, "suffixDept"),
									                                                                                                           tempXml.get(i, "name"), "","",
									                                                                                                           tempXml.get(i, "phone"), 
									                                                                                                           tempXml.get(i, "telNum"),
									                                                                                                           tempXml.get(i, "email"),
									                                                                                                           tempXml.get(i, "outEmail"),
									                                                                                                           tempXml.get(i, "innerTelNum"),
									                                                                                                           tempXml.get(i, "work"),
									                                                                                                           tempXml.get(i, "companyNm"),
									                                                                                                           tempXml.get(i, "companyCd"),
									                                                                                                           tempXml.get(i, "role"),"",
									                                                                                                           tempXml.get(i, "empId"),
									                                                                                                           tempXml.get(i, "engName"),
									                                                                                                           tempXml.get(i, "vvip"),
									                                                                                                           tempXml.get(i, "teamManager"));
								}
							}
							nListCount[CATEGORY_ID] += len;
						}
					}
				}
				if(nListCount[CATEGORY_ID] > 0) {
					titleNum++;
				}
			} catch (SKTException e) {
				e.printStackTrace();
			}

			if (nTotalIndex==0) {
				showNoSearch(true);
			} else {
				showNoSearch(false);

				switch (m_nRequestType) {
				case PEOPLE_MEMBERSEARCH:
					if(mListView.getFooterViewsCount() > 0)
						mListView.removeFooterView(m_frameLayout);
					if (m_szListEnd.equals("N"))			    		
						mListView.addFooterView(m_frameLayout, null, false);
					int i = mListView.getFirstVisiblePosition();
					mListView.setAdapter(mAdapter);
					mListView.setSelection(i);
					break;
				}
			}
			child = (TextView)m_frameLayout.findViewById(R.id.txt_footer);
			child.setCompoundDrawablesWithIntrinsicBounds(R.drawable.easy_member_btn_footer_selector, 0, 0, 0);
			child.setText(R.string.easyaproval_label_footer);
			mIsLastView = false;
			break;

		case PEOPLE_COMPANY:
			try {
				a_XMLData.setList("company");
				mCompanyList = new String[a_XMLData.size() + 1];
				mCompanyCode = new String[a_XMLData.size() + 1];
				for(int i = 0; i < a_XMLData.size() + 1; i++) {
					if(i == 0) {
						mCompanyList[i] = getString(R.string.easyaproval_groupName);
						mCompanyCode[i] = "";
					} else {
						mCompanyList[i] = a_XMLData.get(i - 1, "companyNm");
						mCompanyCode[i] = a_XMLData.get(i - 1, "companyCd");
					}
				}
				showCompanyDialog();
			} catch (SKTException e) {
				android.util.Log.e("member", e.getMessage());
			}
			break;

		// 그룹목록
		case MAIL_CONTACTLIST:
			// 2015-01-20 Join 주석처리 시작 -> 착수보고 시연을 위해
			/*int totalPage = 0;
			int curPage = 0;
			String searchType = "";
			try {
				if(nCurrentPage == 1) {
					nTotalIndex = 0;
					groupData.clear();
				}

				m_szResponseCode = a_XMLData.get("result");
				m_szResponseMessage = a_XMLData.get("resultMessage");

				if (!StringUtil.isNull(a_XMLData.get("totalPageCnt"))) {
					totalPage = Integer.parseInt(a_XMLData.get("totalPageCnt"));
				}
				String nullCheck = a_XMLData.get("page");
				if(nullCheck == null || nullCheck.equals("")) {
					nullCheck = "0";
				}
				searchType = a_XMLData.get("searchType");
				if ("M".equals(searchType)) {
					eFolder_view.setVisibility(View.VISIBLE);
				}else{
					eFolder_view.setVisibility(View.GONE);
				}
				curPage = Integer.parseInt(nullCheck);
				XMLData tempXml = a_XMLData.getChild("contactInfoList");

				if(tempXml != null) {
					nullCheck = tempXml.get("listCnt");
					if(nullCheck != null) {
						tempXml.setList("contactInfo");
						int len = tempXml.size();
						if (len > 0)
						{
							//연락처 검색시에 데이터 파싱
							for (int i=0; i<len; i++, nTotalIndex++)
							{
								EmployeeData employeeData = new EmployeeData()
								.setEmpId(tempXml.get(i, "id"))
								.setName(tempXml.get(i, "name"))
								.setMail(tempXml.get(i, "email"))
								.setInnerMail(tempXml.get(i, "outEmail"))
								.setCompany(tempXml.get(i, "companyNm"))
								.setCompanyCD(tempXml.get(i, "companyCd"))
								.setDepartment(tempXml.get(i, "suffixDept"))
								.setLocation(tempXml.get(i, "loc"))
								.setOfficePhoneNo(tempXml.get(i, "phone"))
								.setCellPhoneNo(tempXml.get(i, "telNum"))
								.setRole(tempXml.get(i, "role"))
								.setTwitter(tempXml.get(i, "twitter"))
								.setJobCd(tempXml.get(i, "jobCd"))
								.setJobNm(tempXml.get(i, "jobNm"))
								.setTeam(tempXml.get(i, "suffixDept"));

								Map<String, EmployeeData> map = new HashMap<String, EmployeeData>();
								map.put("data", employeeData);
								groupData.add(map);
							}
						}
					}
				}
			} catch(SKTException e) {
				e.printStackTrace();
			}

			if (nTotalIndex==0) {
				showNoSearch(true);
			} else {
				showNoSearch(false);

				if(mListView.getFooterViewsCount() > 0)
					mListView.removeFooterView(m_frameLayout);


				//		    	if (totalPage != curPage)
				//		    		mListView.addFooterView(m_frameLayout, null, false);
				int i = mListView.getFirstVisiblePosition();
				mListView.setAdapter(mAdapter);
				mListView.setSelection(i);
				//					saveLatestSearch();
			}
			//    		child = (TextView)m_frameLayout.getChildAt(0);
			child = (TextView)m_frameLayout.findViewById(R.id.txt_footer);
			child.setCompoundDrawablesWithIntrinsicBounds(R.drawable.member_btn_footer_selector, 0, 0, 0);
			child.setText(R.string.label_footer);
			mIsLastView = false;

			//			setIndexList();
*/
			// 2015-01-20 Join 주석처리 끝 -> 착수보고 시연을 위해

			// 2015-01-20 Join 추가 시작 -> 착수보고 시연을 위해
			Toast.makeText(getApplicationContext(), "서비스 연계정보 변경 중입니다.", Toast.LENGTH_SHORT).show();
			// 2015-01-20 Join 추가 끝 -> 착수보고 시연을 위해
			break;

		case PEOPLE_MEMBERSEARCH_LOGIN:
			try {
				mMyCompany = a_XMLData.get("company");
				//				((TextView)findViewById(R.id.groupText)).setText(mMyCompany);
				//					requestData(PEOPLE_DEPTMEMBER, "");
			} catch (SKTException e) {
				// TODO Auto-generated catch block
				android.util.Log.e("member", e.getMessage());
			}
			break;

			// 구성원 기본 조회 response
		case PEOPLE_DEPTMEMBER:
			nTotalIndex = 0;
			groupData.clear();
			String deptName	= "";
			String deptCode = "";
			String companyNm = "";
			try 
			{
				deptName = a_XMLData.get("suffixDept");
				deptCode = a_XMLData.get("deptCode");	
				if(StringUtil.isNull(a_XMLData.get("companyNm"))) {
					companyNm = a_XMLData.get("companyNm");
				}else{
					companyNm = "";
				}
				a_XMLData.setList("deptMember");

				for(int i = 0; i < a_XMLData.size(); i++, nTotalIndex++) 
				{					
					/*
					 * 구성원 리스트 기본조회시 데이터 파싱부분.
					 */
					EmployeeData data = new EmployeeData();
					data.setSerialNo(a_XMLData.get(nTotalIndex, "id"))
					.setNfuId(a_XMLData.get(nTotalIndex, "email"))
					.setDepartment(deptCode)
					.setTeam(deptName)
					.setName(a_XMLData.get(nTotalIndex, "name"))
					.setOfficePhoneNo(a_XMLData.get(nTotalIndex, "phone"))   //  회사 전화 번호 내려오지 않음
					.setCellPhoneNo(a_XMLData.get(nTotalIndex, "telNum"))
					.setMail(a_XMLData.get(nTotalIndex, "email"))
					.setInnerMail(a_XMLData.get(nTotalIndex, "outEmail"))
					.setRole(a_XMLData.get(nTotalIndex, "role"))
					.setEmpId(a_XMLData.get(nTotalIndex, "empId"))
					.setEngName(a_XMLData.get(nTotalIndex, "engName"))
					.setCompany(companyNm)
					.setVvip(a_XMLData.get(nTotalIndex, "vvip"))   
					.setCompanyCD(a_XMLData.get(nTotalIndex, "companyCd"))   
					.setTeamLeader(a_XMLData.get(nTotalIndex, "teamManager"));   
					
					//					Log.i(TAG, "--------------"+i+" : email"+data.m_szMail);
					Map<String, EmployeeData> map = new HashMap<String, EmployeeData>();
					map.put("data", data);
					groupData.add(map);
				}
			} catch(SKTException e) {
				e.printStackTrace();
			}

			if(nTotalIndex == 0) {
				showNoSearch(true);
			} else {
				showNoSearch(false);

				if(mListView.getFooterViewsCount() > 0)
					mListView.removeFooterView(m_frameLayout);
				mListView.setAdapter(mAdapter);
			}
			mIsLastView = false;

			((TextView)findViewById(R.id.txt_team_name)).setText(deptName);

			break;
			
		case MAILADV_GETUSERINFO : 
			dupJobUser = new ArrayList<EmployeeData>();
			try{
				Log.i(TAG, "duplicate???"+a_XMLData.get("info"));
				XMLData dupJobList = a_XMLData.getChild("otherinfo");
				dupJobList.setList("userList");
				Log.i(TAG, "otherList > userList"+ dupJobList.get("userList"));
				
				Document doc = convertStringToDocument(dupJobList.get("userList"));
				NodeList nList = doc.getElementsByTagName("user");
				
				for(int i=0; i<nList.getLength(); i++){
					EmployeeData data = new EmployeeData();
					Node node = nList.item(i);
					
					if(node.getNodeType() == Node.ELEMENT_NODE){
						Element element = (Element)node;
						
						NodeList idList = element.getElementsByTagName("id");
						Element idElmnt = (Element) idList.item(0);
						Node id = idElmnt.getFirstChild();
						data.setEmpId(id.getNodeValue());
						
						NodeList nameList = element.getElementsByTagName("name");
						Element nameElmnt = (Element) nameList.item(0);
						Node name = nameElmnt.getFirstChild();
						data.setName(name.getNodeValue());
						
						NodeList rankNameList = element.getElementsByTagName("rankName");
						Element rankElmnt = (Element) rankNameList.item(0);
						Node rank = rankElmnt.getFirstChild();
						data.setDutNm(rank.getNodeValue());
						
						NodeList deptList = element.getElementsByTagName("deptName");
						Element deptElmnt = (Element) deptList.item(0);
						Node dept = deptElmnt.getFirstChild();
						data.setDepartment(dept.getNodeValue());
						
						dupJobUser.add(data);
					}
				}
				showDialogDupJob(dupJobUser);
			}
			catch (Exception e) {
				e.printStackTrace();
			}
			
			break;
		}
	}
	
	public void showDialogDupJob(ArrayList<EmployeeData> dupJobUser){
		selectDialog = new SelectJobDialog(AddressSearchActivity.this, dupJobUser);
		selectDialog.setCancelable(false);
		selectDialog.setOnDismissListener(new OnDismissListener() {
			
			@Override
			public void onDismiss(DialogInterface dialog) {
				Toast.makeText(AddressSearchActivity.this, "userID!!! "+selectDialog.getUserID(), Toast.LENGTH_LONG).show();
				////////data에 선택된 nfuID 추가 후 return
				
				
				//EmployeeData data = sendData(data)
			}
		});
		selectDialog.show();
	}

	/* (non-Javadoc)
	 * 액션 처리 핸들러<br>
	 * - 멤버 검색 요청<br>
	 * - 회사 목록 요청<br>
	 * - 아웃룩 주소록 요청<br>
	 * - 내 소속팀 요청
	 * @see com.sk.pe.group.activity.BaseActivity#onProcessThread()
	 */
	public XMLData onProcessThread() throws SKTException {
		Parameters params = null;
		Controller controller = null;
		XMLData nXMLData = null;
		String keyword = "";

		switch (m_nRequestType) 
		{
		case PEOPLE_MEMBERSEARCH:
			keyword = (String)m_requestObject;
			//			keyword = keyword.replace(" ", "");
			params = new Parameters("COMMON_PEOPLE_MEMBERSEARCH");
			params.put("type", "G");
			//    	    if("G".equals(mType)) {
			//    	    	params.put("infoCompanyCd", mCompanyCode[mCompanyIndex]);
			//    	    } else if("M".equals(mType)) {
			//    	    	params.put("infoCompanyCd", SKTUtil.getCheckedCompanyCd(this));
			//    	    }
			params.put("keyword", keyword);
			params.put("page", "" + nCurrentPage);
			if(mApprovalType != null && !mApprovalType.equals("")) {
				params.put("approvalType", mApprovalType);
			}
			if(mEmpType != null && !mEmpType.equals("")) {
				params.put("empType", mEmpType);
			}
			break;
		case PEOPLE_MEMBERSEARCH_LOGIN:
			params = new Parameters("COMMON_PEOPLE_MEMBERCONTENT");
			params.put("type", "M");
			break;
		case PEOPLE_COMPANY:
			params = new Parameters("COMMON_PEOPLE_COMPANYS");
			break;
		// 그룹목록
		case MAIL_CONTACTLIST:
			// 2015-01-20 Join 주석처리 시작 -> 착수보고 시연을 위해
			/*keyword = (String)m_requestObject;
			//			keyword = keyword.replace(" ", "");
			params = new Parameters("COMMON_MAIL_CONTACTLIST");
			params.put("searchType", orderType);
			params.put("countPerPage", "15");
			params.put("searchWord", "" + keyword);*/
			// 2015-01-20 Join 주석처리 끝 -> 착수보고 시연을 위해
			break;
		case PEOPLE_DEPTMEMBER:
			params = new Parameters("COMMON_PEOPLE_DEPTMEMBER");
			params.put("infoCompanyCd", (String)m_requestObject);
			params.put("deptCode", "");
			if(mApprovalType != null && !mApprovalType.equals("")) {
				params.put("approvalType", mApprovalType);
			}
			if(mEmpType != null && !mEmpType.equals("")) {
				params.put("empType", mEmpType);
			}
			break;
			
		case MAILADV_GETUSERINFO:
			params = new Parameters("COMMON_MAILADV_GETUSERINFO");
			
			params.put("userID", emailID);
			break;
		}

		if(m_nRequestType != MAIL_CONTACTLIST) {
			controller = new Controller(this);
			nXMLData = controller.request(params);
			
			if(m_nRequestType == PEOPLE_DEPTMEMBER) {
				XMLData memberList = nXMLData.getChild("deptMemberList");
				memberList.setList("deptMember");
				if(memberList != null && memberList.size() > 0) {
					for(int i = 0; i < memberList.size(); i++) {
						Log.d(TAG, "XML Data ========== " + i + " email : " + memberList.get(i, "email"));
						Log.d(TAG, "XML Data ========== " + i + " outEmail : " + memberList.get(i, "outEmail"));
					}
				} else {
					Log.d(TAG, "XML Data is Empty");
				}
			}
			
		} else {
			nXMLData = null;
		}

		return nXMLData;
	}

	//	private void saveLatestSearch() {
	//		if(nTotalIndex == 0) {
	//			return;
	//		}
	//
	//		m_dbHelper = new MemberSearchSQLite(this);
	//        m_dbMember = m_dbHelper.getWritableDatabase();
	//        ContentValues cv = new ContentValues();
	//        Cursor cs = null;
	//
	//	    try {
	//	    	m_dbMember.delete(MemberSearchSQLite.DATABASE_TABLE, "type = '" + mType + "'", null);
	//	    	
	//			cs = m_dbMember.query(MemberSearchSQLite.DATABASE_TABLE,
	//					new String[] { "serial", "deptname", "name", "telnum", "role", "work",
	//							"company", "phone", "email", "type", "companycd", "searchdate" },
	//					"type = '" + mType + "'", null, null, null, null);
	//			
	//			if(groupData.size() > 0) {
	//				for(int i = 0; i < groupData.size(); i++) {
	//					Map<String, EmployeeData> map = groupData.get(i);
	//					EmployeeData data = map.get("data");
	//
	//					if(data.m_szSerialNo.equals("")) {
	//						continue;
	//					}
	//					cv.put("serial", data.m_szSerialNo);
	//	            	cv.put("deptname", data.m_szTeam);
	//	            	cv.put("name", data.m_szName);
	//	            	cv.put("telnum", data.m_szCellPhoneNo);
	//	            	cv.put("role", data.m_szRole);
	//	            	cv.put("work", data.m_szWork);
	//	            	cv.put("company", data.m_szCompany);
	//	            	cv.put("phone", data.m_szOfficePhoneNo);
	//	            	cv.put("email", data.m_szInnerMail);
	//	            	cv.put("type", mType);
	//	            	cv.put("companycd", data.m_szCompanyCd);
	//	            	cv.put("searchdate", System.currentTimeMillis());
	//					m_dbMember.insert(MemberSearchSQLite.DATABASE_TABLE, null, cv);
	//					
	//					if(i >= 14) {
	//						break;
	//					}
	//				}
	//			}
	//        } catch(SQLException e) {
	//        	android.util.Log.e("member", e.getMessage());
	//        } finally {
	//        	if(cs != null) {
	//        		cs.close();
	//        	}
	//        }
	//		m_dbMember.close();
	//		m_dbHelper.close();
	//	}

	/**
	 * 검색 결과 없음 처리
	 * @param isShow 검색 결과 여부
	 */
	private void showNoSearch(boolean isShow) {
		View tempView;

		if(isShow) {
			tempView = findViewById(R.id.MSMainList);
			tempView.setVisibility(View.INVISIBLE);
			tempView = findViewById(R.id.MSMainNotice);
			tempView.setVisibility(View.VISIBLE);

			tempView = findViewById(R.id.notice_icon);
			tempView.setVisibility(View.INVISIBLE);
			tempView = findViewById(R.id.notice_text);
			tempView.setVisibility(View.INVISIBLE);
			tempView = findViewById(R.id.notice_text2);
			tempView.setVisibility(View.VISIBLE);
		} else {
			tempView = findViewById(R.id.MSMainList);
			tempView.setVisibility(View.VISIBLE);
			tempView = findViewById(R.id.MSMainNotice);
			tempView.setVisibility(View.INVISIBLE);
		}
	}

	/**
	 * 최근 조회 목록 가져오기
	 * DB에 접근하여 가져온다.
	 */
	private void getLastSearch() {
		groupData.clear();
		nTotalIndex = 0;

		m_dbHelper = new MemberSearchSQLite(this);
		m_dbMember = m_dbHelper.getWritableDatabase();

		Cursor cs = null;
		//		m_dbMember.query(table, columns, selection, selectionArgs, groupBy, having, orderBy)
		try {
			String comCd = SeedCrypter.encrypt(SKTUtil.getCheckedCompanyCd(this));

			//CJ 변경
			//그룹사탭G, 자사탭M 구분이 안되고 있음
			//저장은 무조건 G로 하고 있고 UI에서는 M으로 인식하고 있음
			cs = m_dbMember.query(MemberSearchSQLite.DATABASE_TABLE,
					new String[] { "serial", "deptname", "name", "telnum", "role", "work",
					"company", "phone", "email", "type", "infocompanycd", "empid",
					"searchdate", "deptcode", "currentkey", "landline", "vvip", "teamManager","memo"},
					"type = '" + SeedCrypter.encrypt(mType) + "' AND companycd = '" + comCd + "'", null, null, null,
					null);
			if (cs == null || cs.getCount() == 0) {
				showNoSearch(true);
			} else {
				showNoSearch(false);

				cs.moveToFirst();
				do 
				{	
					EmployeeData employeeData = new EmployeeData()
					.setSerialNo(SeedCrypter.decrypt(cs.getString(0)))
					.setTeam(SeedCrypter.decrypt(cs.getString(1)))
					.setName(SeedCrypter.decrypt(cs.getString(2)))
					.setCellPhoneNo(SeedCrypter.decrypt(cs.getString(3)))
					.setRole(SeedCrypter.decrypt(cs.getString(4)))
					.setTemp("M".equals(mType) || "E".equals(mType) ? "" : SeedCrypter.decrypt(cs.getString(4)))
					.setWork(SeedCrypter.decrypt(cs.getString(5)))
					.setCompany(SeedCrypter.decrypt(cs.getString(6)))
					.setOfficePhoneNo(SeedCrypter.decrypt(cs.getString(7)))
					.setInnerMail(SeedCrypter.decrypt(cs.getString(8)))
					.setMail(SeedCrypter.decrypt(cs.getString(8)))   // 수정 05-24
					.setType(SeedCrypter.decrypt(cs.getString(9)))
					.setCompanyCD(SeedCrypter.decrypt(cs.getString(10)))
					.setEmpId(SeedCrypter.decrypt(cs.getString(11)))
					.setDepartment(SeedCrypter.decrypt(cs.getString(13)))
					.setCurrentKey(SeedCrypter.decrypt(cs.getString(14)))
					.setLandline(SeedCrypter.decrypt(cs.getString(15)))
					.setVvip(SeedCrypter.decrypt(cs.getString(16)))
					.setTeamLeader(SeedCrypter.decrypt(cs.getString(17)))
					.setMemo(SeedCrypter.decrypt(cs.getString(18)));

					Map<String, EmployeeData> map = new HashMap<String, EmployeeData>();
					map.put("data", employeeData);
					groupData.add(map);	   					
					nTotalIndex++;
				} while(cs.moveToNext());
			}
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			if(cs != null) {
				cs.close();
			}
		}

		m_dbMember.close();
		m_dbHelper.close();
	}

	class TeamData
	{
		private EmployeeData data;
		public TeamData() { data = new EmployeeData(); }
		public TeamData(EmployeeData data) { this.data = data; }

		public TeamData setSerialNo(String s) { data.m_szSerialNo = checkTrim(s); return this; }
		public TeamData setNfuId(String s) { data.m_nfuId = checkTrim(s); return this; }
		public TeamData setDepartment(String s) { data.m_szDepartment = checkTrim(s); return this; }
		public TeamData setTeam(String s) { data.m_szTeam = checkTrim(s); return this; }
		public TeamData setName(String s) { data.m_szName = checkTrim(s); return this; }
		public TeamData setPicturePath(String s) { data.m_szPicturePath = checkTrim(s); return this; }
		public TeamData setLocation(String s) { data.m_szLocation = checkTrim(s); return this; }
		public TeamData setOfficePhoneNo(String s) { data.m_szOfficePhoneNo = checkTrim(s); return this; }
		public TeamData setCellPhoneNo(String s) { data.m_szCellPhoneNo = checkTrim(s); return this; }
		public TeamData setMail(String s) { data.m_szMail = checkTrim(s); return this; }
		public TeamData setInnerMail(String s) { data.m_szInnerMail = checkTrim(s); return this; }
		public TeamData setInnerPhoneNo(String s) { data.m_szInnerPhoneNo = checkTrim(s); return this; }		// 2015-03-17 Join 추가 - 기존 내부 메일 항목이 구내전화로 바꼈기 때문에 추가
		public TeamData setWork(String s) { data.m_szWork = checkTrim(s); return this; }
		public TeamData setCompany(String s) { data.m_szCompany = checkTrim(s); return this; }
		public TeamData setWznoe(String s) { data.m_szWzone = checkTrim(s); return this; }
		public TeamData setRole(String s) { data.m_szRole = checkTrim(s); return this; }
		public TeamData setMessenger(String s) { data.m_szMessenger = checkTrim(s); return this; }
		public TeamData setTwitter(String s) { data.m_szTwitter = checkTrim(s); return this; }
		public TeamData setType(String s) { data.m_szType = checkTrim(s); return this; }
		public TeamData setCompanyCD(String s) { data.m_szCompanyCd = checkTrim(s); return this; }
		public TeamData setTemp(String s) { data.m_szTemp = checkTrim(s); return this; }
		public TeamData setEmpId(String s) { data.m_szEmpId = checkTrim(s); return this; }

		public void build() 
		{ 
			Map<String, EmployeeData> map = new HashMap<String, EmployeeData>();
			map.put("data", data);
			groupData.add(map);			
		}
	}

	private void addCurTeamData(String a_ParamA, String a_ParamB, String a_ParamC, String a_ParamD,
                                String a_ParamE, String a_ParamF, String a_ParamG, String a_ParamH, String a_ParamI,
                                String a_ParamJ, String a_ParamJ2, String a_ParamK, String a_ParamL, String a_ParamM, String a_ParamN,
                                String a_ParamO, String a_ParamP, String a_ParamQ, String a_ParamR, String a_ParamS,
                                String a_ParamT, String a_ParamU, String a_ParamX, String a_ParamZ ) {
		EmployeeData data = new EmployeeData();
		data.m_szSerialNo = checkTrim(a_ParamA);
		data.m_szDepartment = checkTrim(a_ParamB);
		data.m_szTeam = checkTrim(a_ParamC);
		data.m_szName = checkTrim(a_ParamD);
		data.m_szPicturePath = checkTrim(a_ParamE);
		data.m_szLocation = checkTrim(a_ParamF);
		data.m_szOfficePhoneNo = checkPhone(a_ParamG);
		data.m_szCellPhoneNo = checkPhone(a_ParamH);
		data.m_szMail = checkTrim(a_ParamI);
		data.m_szInnerMail = checkTrim(a_ParamJ);
		data.m_szInnerPhoneNo = checkTrim(a_ParamJ2);		// 2015-03-17 Join 추가 - 기존 내부 메일 항목이 구내전화로 바꼈기 때문에 추가
		data.m_szWork = checkTrim(a_ParamK);
		data.m_szCompany = checkTrim(a_ParamL);
		data.m_szCompanyCd = checkTrim(a_ParamM);
		data.m_szRole = checkTrim(a_ParamN);
		data.m_szMessenger = checkTrim(a_ParamO);
		data.m_szTwitter = checkTrim(a_ParamP);
		data.m_szType = checkTrim(a_ParamQ);
		data.m_szWzone = checkTrim(a_ParamR);
		data.m_szTemp = checkTrim(a_ParamS);
		data.m_szEmpId = checkTrim(a_ParamT);
		data.setEngName(checkTrim(a_ParamU)) ;
		data.m_szVvip = checkTrim(a_ParamX);
		data.m_szTeamLeader = checkTrim(a_ParamZ);

		
		Log.d("xxxxxxxx", "xxxxxxxxxxx m_szOfficePhoneNo checkPhone(a_ParamG) : "  + checkPhone(a_ParamG) );
		Log.d("xxxxxxxx", "xxxxxxxxxxx m_szCellPhoneNo checkPhone(a_ParamH) : "  + checkPhone(a_ParamH) );
		Map<String, EmployeeData> map = new HashMap<String, EmployeeData>();
		map.put("data", data);
		groupData.add(map);
	}	


	private void addCurTeamData(String a_ParamA, String a_ParamB, String a_ParamC, String a_ParamD,
                                String a_ParamE, String a_ParamF, String a_ParamG, String a_ParamH, String a_ParamI,
                                String a_ParamJ, String a_ParamJ2, String a_ParamK, String a_ParamL, String a_ParamM, String a_ParamN,
                                String a_ParamO, String a_ParamP, String a_ParamQ, String a_ParamX, String a_ParamZ) {
		addCurTeamData(a_ParamA, a_ParamB, a_ParamC, a_ParamD, a_ParamE, a_ParamF, a_ParamG,
				a_ParamH, a_ParamI, a_ParamJ, a_ParamJ2, a_ParamK, a_ParamL, a_ParamM, a_ParamN, "", "", "",
				"", a_ParamO, a_ParamP, a_ParamQ, a_ParamX, a_ParamZ );
	}

	private void insertCurTeamData(int index, String a_ParamA, String a_ParamB, String a_ParamC,
                                   String a_ParamD, String a_ParamE, String a_ParamF, String a_ParamG, String a_ParamH,
                                   String a_ParamI, String a_ParamJ, String a_ParamJ2, String a_ParamK, String a_ParamL, String a_ParamM,
                                   String a_ParamN, String a_ParamO, String a_ParamP, String a_ParamQ, String a_ParamX,
                                   String a_ParamZ ) {
		EmployeeData data = new EmployeeData();
		data.m_szSerialNo = checkTrim(a_ParamA);
		data.m_szDepartment = checkTrim(a_ParamB);
		data.m_szTeam = checkTrim(a_ParamC);
		data.m_szName = checkTrim(a_ParamD);
		data.m_szPicturePath = checkTrim(a_ParamE);
		data.m_szLocation = checkTrim(a_ParamF);
		data.m_szOfficePhoneNo = checkPhone(a_ParamG);
		data.m_szCellPhoneNo = checkPhone(a_ParamH);
		data.m_szMail = checkTrim(a_ParamI);
		data.m_szInnerMail = checkTrim(a_ParamJ);
		data.m_szInnerPhoneNo = checkTrim(a_ParamJ2);		// 2015-03-17 Join 추가 - 기존 내부 메일 항목이 구내전화로 바꼈기 때문에 추가
		data.m_szWork = checkTrim(a_ParamK);
		data.m_szCompany = checkTrim(a_ParamL);
		data.m_szCompanyCd = checkTrim(a_ParamM);
		data.m_szRole = checkTrim(a_ParamN);
		data.m_szTemp = checkTrim(a_ParamO);
		data.m_szEmpId = checkTrim(a_ParamP);
		data.setEngName(checkTrim(a_ParamQ)) ;
		data.m_szVvip = checkTrim(a_ParamX);
		data.m_szTeamLeader = checkTrim(a_ParamZ);

		Map<String, EmployeeData> map = new HashMap<String, EmployeeData>();
		map.put("data", data);
		groupData.add(index, map);
	}


	/* (non-Javadoc)
	 * 공백 처리<br>   
	 */
	private String checkTrim(String a_sParam) {
		if(a_sParam == null) {
			return "";
		}

		return a_sParam.trim();
	}

	/* (non-Javadoc)
	 * 전화번호 - 처리<br>
	 */
	private String checkPhone(String phone) {
		String ret = "";

		if(phone == null) {
			return ret;
		}

		ret = phone.replace("-", "").trim();

		return ret;
	}

	/* (non-Javadoc)
	 * 목록 클릭 이벤트 핸들러
	 * @see android.widget.ExpandableListView.OnGroupClickListener#onGroupClick(android.widget.ExpandableListView, android.view.View, int, long)
	 */
	@SuppressWarnings("unchecked")
	public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id){
		// TODO Auto-generated method stub
		boolean isCollapsed = false;

		for(int i = 0; i < parent.getCount(); i++) {
			if(parent.isGroupExpanded(i)) {
				isCollapsed = true;
				parent.collapseGroup(i);
			}
		}
		Log.i(TAG, "mType!!!!!!!!!!!!!!!"+mType);
		if(!isCollapsed) 
		{
			if(isMultiSelect){
				HashMap<String, EmployeeData> map = (HashMap<String, EmployeeData>)parent.getExpandableListAdapter().getGroup(groupPosition);
				EmployeeData data = map.get("data");
				if (mType.equals("M")) {
					ImageView indicator = (ImageView)v.findViewById(R.id.ROW_EMPLOYEE_PHONE);
					Log.i(TAG, "--------------multiselect--------------");
					Log.i(TAG, "data......szMail : "+data.m_szMail);			//U0013890_G0013230
//					Log.i(TAG, "data......m_szInnerMail : "+data.m_szInnerMail);//kmh@ex.co.kr
//					Log.i(TAG, "data......m_szOutMail : "+data.m_szOutMail);	//공백
					
					if(data.isSelected) {
						data.isSelected = false;
						indicator.setImageResource(R.drawable.easy_member_check_off);
					} else {
						data.isSelected = true;
						indicator.setImageResource(R.drawable.easy_member_check_on);
						if(data.m_szMail.length() > 8){
							dupJobUser = null;
							Log.i(TAG, "겸직자 선택!");
							if(data.m_szMail.startsWith("U")){
								emailID = data.m_szMail.substring(data.m_szMail.indexOf("U"), 8);
							}
							else{
								emailID = data.m_szMail.substring(data.m_szMail.indexOf("U"), 16);
							}
							

							requestData(MAILADV_GETUSERINFO, emailID);	
							
						}
					
					}
				} else {
					if ("G".equals(orderType)) {
						orderType = "M";
						requestData(MAIL_CONTACTLIST, data.m_szEmpId);
						suffix_dept_text.setText(data.m_szName);
					} else {
						ImageView indicator = (ImageView)v.findViewById(R.id.ROW_EMPLOYEE_PHONE);
						if(data.isSelected) {
							data.isSelected = false;
							indicator.setImageResource(R.drawable.easy_member_check_off);
						} else {
							data.isSelected = true;
							indicator.setImageResource(R.drawable.easy_member_check_on);
						}
					}
				}
			} else if(isSingleSelect) {
				HashMap<String, EmployeeData> map = (HashMap<String, EmployeeData>)parent
				.getExpandableListAdapter().getGroup(groupPosition);
				Log.i(TAG, "!!!!!!!!!!!mType!!!!!!!!!!!"+mType);
				
				EmployeeData data = map.get("data");
				if ("G".equals(orderType) && "E".equals(mType)) {
					Log.i(TAG, "!!!!!!!!!!!!!!!!!!!!!!!");
					orderType = "M";
					requestData(MAIL_CONTACTLIST, data.m_szEmpId);
					suffix_dept_text.setText(data.m_szName);
				} else {
					Intent intent = new Intent();
					intent.putExtra("name", data.m_szName);
					intent.putExtra("empId", data.m_szEmpId);
					intent.putExtra("role", data.m_szRole);
					intent.putExtra("suffixDept", data.m_szTeam);
					intent.putExtra("nfuId", data.m_szMail); 
					intent.putExtra("arg1", arg1);
					AddressTabActivity parent1 = (AddressTabActivity)getParent();
					parent1.setResult(RESULT_OK, intent);
					finish();
				}
			} 
			else 
			{
				//**********************
				//구성원에서 상세보기시.!!
				HashMap<String, EmployeeData> map = (HashMap<String, EmployeeData>)parent.getExpandableListAdapter().getGroup(groupPosition);
				EmployeeData data = map.get("data");
				Intent intent = new Intent(AddressSearchActivity.this, AddressInfoActivity.class);
				intent.putExtra("type", mType);
				/*
	        	if(!mIsLastView) {
	        		intent.putExtra("type", mType);
	        		Log.e("mType  == =====", mType);
	        	}
				 */
				intent.putExtra("deptCode", data.m_szDepartment);	        	
				if(mType.equals("M")){
					intent.putExtra("employee", data.m_szSerialNo);
					intent.putExtra("companyCode", data.m_szCompanyCd);
					startActivityForResult(intent, 1);
				}else{
					if ("G".equals(orderType)) {
						orderType = "M";
						requestData(MAIL_CONTACTLIST, data.m_szEmpId);
						suffix_dept_text.setText(data.m_szName);
					} else {
						intent.putExtra("changeKey", data.m_szEmpId);
						startActivityForResult(intent, 1);
					}

				}
			}	

			//				if(!mType.equals("E")){ 
			//					//**********************
			//					//구성원에서 상세보기시.!!
			//					HashMap<String, EmployeeData> map = (HashMap<String, EmployeeData>)parent
			//					.getExpandableListAdapter().getGroup(groupPosition);
			//					EmployeeData data = map.get("data");
			//					Intent intent = new Intent(AddressSearchActivity.this, AddressInfoActivity.class);
			//					if(!mIsLastView) {
			//						intent.putExtra("type", mType);
			//					}
			//					intent.putExtra("deptCode", data.m_szDepartment);	        	
			//					intent.putExtra("employee", data.m_szSerialNo);
			//					intent.putExtra("companyCode", data.m_szCompanyCd);
			//Log.e("aaaaaaaaaaaaaaaaa", "2105");
			//					if(mType.equals("E")) 
			//						intent.putExtra("data", data);	 
			//						intent.putExtra("deptCode", data.m_szDepartment);	        	
			//						intent.putExtra("employee", data.m_szSerialNo);
			//						intent.putExtra("companyCode", data.m_szCompanyCd);
			//						startActivityForResult(intent, 1);
			//				} else if(mIsLastView) {
			//					HashMap<String, EmployeeData> map = (HashMap<String, EmployeeData>)parent
			//					.getExpandableListAdapter().getGroup(groupPosition);
			//					EmployeeData data = map.get("data");
			//					Intent intent = new Intent(AddressSearchActivity.this, AddressInfoActivity.class);
			//					intent.putExtra("type", data.m_szType);
			//					intent.putExtra("isLast", true);
			//					intent.putExtra("companyCode", data.m_szCompanyCd);
			//					intent.putExtra("employee", data.m_szSerialNo);
			//					intent.putExtra("deptCode", data.m_szDepartment);
			//					if(mType.equals("E")) {
			//						intent.putExtra("data", data);
			//					}
			//					startActivityForResult(intent, 1);



		}
		return true;
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

	/* (non-Javadoc)
	 * 컨텍스트 메뉴 클릭 이벤트 핸들러<br>
	 * - 모바일 전화 걸기<br>
	 * - 회사 전화 걸기<br>
	 * - 문자 보내기<br>
	 * - 이메일 보내기<br>
	 * - 상세 보기
	 * @see android.app.Activity#onContextItemSelected(android.view.MenuItem)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public boolean onContextItemSelected(MenuItem item)
	{
		// TODO Auto-generated method stub
		Intent intent = null;
		HashMap<String, EmployeeData> map = (HashMap<String, EmployeeData>)mListView.getExpandableListAdapter().getGroup(mLongIndex);
		EmployeeData data = map.get("data");

		if(item.getItemId() == CALL_MOBILE) {
			if(data.m_szCellPhoneNo == null || "".equals(data.m_szCellPhoneNo) ||
					"HC00001".equals(data.m_szEmpId)) {
				m_szTitle = getString(R.string.easyaproval_text_error);
				m_szDialogMessage = getString(R.string.easyaproval_no_mobile);
				showDialog(DIALOG_MEMBER_SEARCH);
				return true;
			}
			Uri uri = Uri.parse("tel:" + data.m_szCellPhoneNo);
			intent = new Intent(Intent.ACTION_DIAL, uri);
			startActivity(intent);
			return true;
		} else if(item.getItemId() == CALL_OFFICE) {
			if(data.m_szCellPhoneNo == null || "".equals(data.m_szCellPhoneNo) ||
					"HC00001".equals(data.m_szEmpId)) {
				m_szTitle = getString(R.string.easyaproval_text_error);
				m_szDialogMessage = getString(R.string.easyaproval_no_office);
				showDialog(DIALOG_MEMBER_SEARCH);
				return true;
			}
			Uri uri = Uri.parse("tel:" + data.m_szCellPhoneNo);
			intent = new Intent(Intent.ACTION_DIAL, uri);
			startActivity(intent);
			return true;
		} else if(item.getItemId() == SEND_SMS) {
			if(data.m_szCellPhoneNo == null || "".equals(data.m_szCellPhoneNo) ||
					"HC00001".equals(data.m_szEmpId)) {
				m_szTitle = getString(R.string.easyaproval_text_error);
				m_szDialogMessage = getString(R.string.easyaproval_no_mobile);
				showDialog(DIALOG_MEMBER_SEARCH);
				return true;
			}
			Uri uri = Uri.parse("sms:" + data.m_szCellPhoneNo);
			intent = new Intent(Intent.ACTION_SENDTO, uri);
			startActivity(intent);
			return true;
		} else if(item.getItemId() == SEND_EMAIL) {
			if (!"E".equals(mType) ) {
				if(data.m_szMail == null || data.m_szMail.equals("") ) {
					m_szTitle = getString(R.string.easyaproval_text_error);
					m_szDialogMessage = getString(R.string.easyaproval_no_email);
					showDialog(DIALOG_MEMBER_SEARCH);
					return true;
				}
			} else {
				if(	"".equals(data.m_szEmpId)) {
					m_szTitle = getString(R.string.easyaproval_text_error);
					m_szDialogMessage = getString(R.string.easyaproval_no_email);
					showDialog(DIALOG_MEMBER_SEARCH);
					return true;
				}
			}

			try {
				intent = new Intent(ACTION_EMAIL_CLIENT);
				String[] empname = new String[1];
				String[] empEmail = new String[1];
				String[] empVvip = new String[1];

				empname[0] = data.m_szName;
				if (!"E".equals(mType)) {
					empEmail[0] = data.m_szMail;
				} else {
					empEmail[0] = data.m_szEmpId;
				}
				empVvip[0] = data.m_szVvip;
				intent.putExtra("wtype", "urlforward");
				//				if (!"E".equals(mType) ) {
				//					intent.putExtra("toText", data.m_szInnerMail);
				//				}else{
				//					intent.putExtra("toText", data.m_szEmpId);
				//				}
				intent.putExtra("type", "side");
				intent.putExtra("names", empname);
				intent.putExtra("emails", empEmail);				            
				intent.putExtra("vvip", empVvip);	
				startActivity(intent);
			} catch(Exception e) {
				m_szTitle = getString(R.string.easyaproval_title_no_mail);
				m_szDialogMessage = getString(R.string.easyaproval_no_emailclient);
				showDialog(DIALOG_NO_APP);
			}

			return true;


		} else if(item.getItemId() == VIEW_CONTENT) {
			intent = new Intent(AddressSearchActivity.this, AddressInfoActivity.class);
			intent.putExtra("type", mType);
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
			if(mType.equals("E")) {
				intent.putExtra("changeKey", data.m_szEmpId);
			}
			startActivityForResult(intent, 1);
			return true;
		}

		return super.onContextItemSelected(item);
	}

	/* (non-Javadoc)
	 * 롱클릭시 팝업 메뉴
	 * @see android.app.Activity#onCreateContextMenu(android.view.ContextMenu, android.view.View, android.view.ContextMenu.ContextMenuInfo)
	 */
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenuInfo menuInfo) {
		SKTUtil.log(Log.DEBUG, "Xxxxxxxxxxxxx", "xxxxxxxxxxxxxx  onItemLongClick arg2  :  " + mLongIndex);
		HashMap<String, EmployeeData> map = (HashMap<String, EmployeeData>)mListView
		.getExpandableListAdapter().getGroup(mLongIndex);
		EmployeeData data = map.get("data");
		if(isMultiSelect) {
			return;
		}
		if("E".equals(mType)){
			if( "M".equals(orderType)){
				if (!StringUtil.isNull(data.m_szCellPhoneNo)) {
					menu.add(0, CALL_MOBILE, 0, R.string.easyaproval_callMobile);
				}
				//			if (!StringUtil.isNull(data.m_szOfficePhoneNo)) {
				//				menu.add(0, CALL_OFFICE, 0, R.string.callOffice);
				//			}
				if (!StringUtil.isNull(data.m_szCellPhoneNo)) {
					menu.add(0, SEND_SMS, 0, R.string.easyaproval_sendSMS);
				}
				if (!StringUtil.isNull(data.m_szEmpId)) {
					menu.add(0, SEND_EMAIL, 0, R.string.easyaproval_sendEmail);
				}
				menu.add(0, VIEW_CONTENT, 0, R.string.easyaproval_viewContent);
			}
		}else{
			if (!StringUtil.isNull(data.m_szCellPhoneNo)) {
				menu.add(0, CALL_MOBILE, 0, R.string.easyaproval_callMobile);
			}
			//			if (!StringUtil.isNull(data.m_szOfficePhoneNo)) {
			//				menu.add(0, CALL_OFFICE, 0, R.string.callOffice);
			//			}
			if (!StringUtil.isNull(data.m_szCellPhoneNo)) {
				menu.add(0, SEND_SMS, 0, R.string.easyaproval_sendSMS);
			}
			if (!StringUtil.isNull(data.m_szMail)) {
				menu.add(0, SEND_EMAIL, 0, R.string.easyaproval_sendEmail);
			}
			menu.add(0, VIEW_CONTENT, 0, R.string.easyaproval_viewContent);
		}
		super.onCreateContextMenu(menu, v, menuInfo);
	}
	
	public EmployeeData addNfuID(EmployeeData data){
//		data.m_nfuId = ;
		return data;
	}

	/* (non-Javadoc)
	 * 리스트의 목록 처리 어댑터<br>
	 */
	private class MyListAdapter extends SimpleExpandableListAdapter {
		public MyListAdapter(Context context,
                             List<? extends Map<String, ?>> groupData, int expandedGroupLayout, int collapsedGroupLayout, String[] groupFrom, int[] groupTo, List<? extends List<? extends Map<String, ?>>> childData, int childLayout, int lastChildLayout, String[] childFrom, int[] childTo){
			super(context, groupData, expandedGroupLayout, collapsedGroupLayout, groupFrom, groupTo, childData, childLayout, lastChildLayout, childFrom, childTo);
			// TODO Auto-generated constructor stub
		}

		/* (non-Javadoc)
		 * @see android.widget.SimpleExpandableListAdapter#getGroupView(int, boolean, android.view.View, android.view.ViewGroup)
		 */
		@SuppressWarnings("unchecked")
		@Override
		public View getGroupView(final int groupPosition, boolean isExpanded, View convertView, final ViewGroup parent){
			// TODO Auto-generated method stub
			HashMap<String, EmployeeData> map = (HashMap<String, EmployeeData>)getGroup(groupPosition);
			final EmployeeData data = map.get("data");

			convertView = newGroupView(isExpanded, parent);

			TextView name = null;
			ImageView sms = null;

			//			if (data.getEngName() == null || "".equals(data.getEngName())) {

			//			}else{
			//				name.setText(data.m_szName+" ["+data.getEngName()+"]");
			//			}           
			//			if("Y".equals(data.m_szTeamLeader)){
			//				name.setTextColor(Color.rgb(0x1C, 0x8C, 0xC8));
			//			}
			
			if(mType.equals("M"))
			{
				name = (TextView)convertView.findViewById(R.id.ROW_EMPLOYEE_NAME);
				name.setText(data.m_szName);
				
				if(mIsLastView){

					name = (TextView)convertView.findViewById(R.id.ROW_EMPLOYEE_CORP);
					name.setText(data.m_szTeam);
					//회사명
					name = (TextView)convertView.findViewById(R.id.ROW_EMPLOYEE_ID);
					name.setText(data.m_szRole);   
					//name.setVisibility(StringUtil.isNull(data.m_szTeam) ? View.GONE : View.VISIBLE);

					//팀명
//					name = (TextView)convertView.findViewById(R.id.ROW_EMPLOYEE_DEPT);
//					name.setVisibility(View.GONE);
					//					name.setVisibility(StringUtil.isNull(data.m_szRole) ? View.GONE : View.VISIBLE);
				}else{
					//부서명
					name = (TextView)convertView.findViewById(R.id.ROW_EMPLOYEE_CORP);
					name.setText(data.m_szTeam); 
					
					//직급
					name = (TextView)convertView.findViewById(R.id.ROW_EMPLOYEE_ID);
					name.setText(data.m_szRole);   
					//					name.setVisibility(StringUtil.isNull(data.m_szTeam) ? View.GONE : View.VISIBLE);

					//팀명
//					name = (TextView)convertView.findViewById(R.id.ROW_EMPLOYEE_DEPT);
//					name.setVisibility(View.GONE);
					//					name.setVisibility(StringUtil.isNull(data.m_szRole) ? View.GONE : View.VISIBLE);
				}

				if(data.m_szTeam.equals("")) 
				{
					name.setVisibility(View.GONE);
					try
					{
						convertView.findViewById(R.id.ROW_EMPLOYEE_SPLIT).setVisibility(View.GONE);
					} catch(NullPointerException e) { }
				} else {
					name.setVisibility(View.VISIBLE);
					try
					{
						convertView.findViewById(R.id.ROW_EMPLOYEE_SPLIT).setVisibility(View.VISIBLE);
					} catch(NullPointerException e) { }
				}            	

				//				//상세보기 아이콘 - 아웃룩엔 존재하지 않는다.	 
				//				sms = (ImageView)convertView.findViewById(R.id.go_detail);	    
				//				sms.setVisibility(isMultiSelect ? View.GONE : View.VISIBLE);
			}	
			else
			{
				//  메일은 다른 아이디값을 지닌다.
				//  메일에 대한 설정
				LinearLayout li1 = (LinearLayout)convertView.findViewById(R.id.type_folder);
				LinearLayout li2 = (LinearLayout)convertView.findViewById(R.id.type_list);

				if ("G".equals(orderType)) {
					li1.setVisibility(View.VISIBLE);
					li2.setVisibility(View.GONE);
					name = (TextView)convertView.findViewById(R.id.ROW_FOLDER_NAME);
					name.setText(data.m_szName);            	
				} else {
					li1.setVisibility(View.GONE);
					li2.setVisibility(View.VISIBLE);

					name = (TextView)convertView.findViewById(R.id.ROW_EMPLOYEE_NAME);
					name.setText(data.m_szName);

//					name = (TextView)convertView.findViewById(R.id.ROW_EMPLOYEE_DEPT);
//					name.setVisibility(View.GONE);

					//부서명
					name = (TextView)convertView.findViewById(R.id.ROW_EMPLOYEE_CORP);
					name.setText(data.m_szTeam); 
					
					//회사명
					name = (TextView)convertView.findViewById(R.id.ROW_EMPLOYEE_ID);
					name.setText(data.m_szRole);   

					ImageView btn_sms = (ImageView)convertView.findViewById(R.id.ROW_EMPLOYEE_SMS);
					btn_sms.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View arg0) {
							try {
								Intent intent = new Intent(ACTION_EMAIL_CLIENT);

								String[] empname = new String[1];
								String[] empEmail = new String[1];
								String[] empVvip = new String[1];

								empname[0] = data.m_szName;
								empEmail[0] = data.m_szMail;
								empVvip[0] = data.m_szVvip;
								SKTUtil.log(Log.DEBUG, "xxxxxxxxxxx" , "xxxxxxxxxxxxxx " + data.m_szMail);
								intent.putExtra("type", "side");
								intent.putExtra("names", empname);
								intent.putExtra("emails", empEmail);				            
								intent.putExtra("vvip", empVvip);				            
								startActivity(intent);	

							} catch(Exception e) {
								m_szTitle = getString(R.string.easyaproval_title_no_mail);
								m_szDialogMessage = getString(R.string.easyaproval_no_emailclient);
								showDialog(DIALOG_NO_APP);
							}
						}
					});

					final ImageView indicator = (ImageView)convertView.findViewById(R.id.ROW_EMPLOYEE_PHONE);
					ImageView officePhone = (ImageView)convertView.findViewById(R.id.ROW_EMPLOYEE_LANDLINE);
					if (isMultiSelect) {
						if(data.isSelected) {
							indicator.setImageResource(R.drawable.easy_member_check_on);
						} else {
							indicator.setImageResource(R.drawable.easy_member_check_off);
						}
						btn_sms.setVisibility(View.GONE);
						officePhone.setVisibility(View.GONE);
					} else {
						
						if (StringUtil.isNull(data.m_szCellPhoneNo))
							indicator.setVisibility(View.INVISIBLE);
						else
							indicator.setVisibility(View.VISIBLE);
						
						indicator.setOnClickListener(new OnClickListener() {
							
							@Override
							public void onClick(View arg0) {
								Uri uri = Uri.parse("tel:" + data.m_szCellPhoneNo);
								Intent intent = new Intent(Intent.ACTION_DIAL, uri);
								startActivity(intent);
							}
						});
						
						if (StringUtil.isNull(data.m_szOfficePhoneNo))
							officePhone.setVisibility(View.INVISIBLE);
						else
							officePhone.setVisibility(View.VISIBLE);
						
						officePhone.setOnClickListener(new OnClickListener() {
							
							@Override
							public void onClick(View arg0) {
								Uri uri = Uri.parse("tel:" + data.m_szOfficePhoneNo);
								Intent intent = new Intent(Intent.ACTION_DIAL, uri);
								startActivity(intent);
							}
						});
					}
					
					if (isSingleSelect) {
						officePhone.setVisibility(View.GONE);
						indicator.setVisibility(View.GONE);
						btn_sms.setVisibility(View.GONE);
					}
				}
			}
			if(mType.equals("M"))
			{
				sms = (ImageView)convertView.findViewById(R.id.ROW_EMPLOYEE_SMS);
				final ImageView indicator =
					(ImageView)convertView.findViewById(R.id.ROW_EMPLOYEE_PHONE);
				ImageView officePhone = (ImageView)convertView.findViewById(R.id.ROW_EMPLOYEE_LANDLINE);
				sms.setVisibility(View.VISIBLE);
				indicator.setVisibility(View.VISIBLE);
				
				indicator.setOnClickListener(new OnClickListener()
				{
					public void onClick(View v) {
						Log.i(TAG, "indicator clicked!!!!!!!!");

						if(isMultiSelect) {
							if(data.isSelected){
								data.isSelected = false;
								indicator.setImageResource(R.drawable.easy_member_check_off);
							} else {
								dupJobUser = null;
								data.isSelected = true;
								Log.i(TAG, "userdddID, nfuId mail ID  ============="+data.m_szMail);
								if(data.m_szMail.length() > 8){
									emailID = data.m_szMail.split("_")[0];
									requestData(MAILADV_GETUSERINFO, emailID);
//									requestData(MAILADV_GETUSERINFO, data);
//									sendData(data);
								}
								
//								addNfuID(data);
								indicator.setImageResource(R.drawable.easy_member_check_on);
							}
						} else {
							Uri uri = Uri.parse("tel:" + data.m_szCellPhoneNo);
							Intent intent = new Intent(Intent.ACTION_DIAL, uri);
							startActivity(intent);
						}
					}
				});            
				sms.setOnClickListener(new OnClickListener() {
					public void onClick(View v) {
						Uri uri = Uri.parse("sms:" + data.m_szCellPhoneNo);
						Intent intent = new Intent(Intent.ACTION_SENDTO, uri);
						startActivity(intent);
					}
				});

				if (StringUtil.isNull(data.m_szOfficePhoneNo))
					officePhone.setVisibility(View.INVISIBLE);
				else
					officePhone.setVisibility(View.VISIBLE);
				
				officePhone.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View arg0) {
						Uri uri = Uri.parse("tel:" + data.m_szOfficePhoneNo);
						Intent intent = new Intent(Intent.ACTION_DIAL, uri);
						startActivity(intent);
					}
				});
//				else{
//					if (StringUtil.isNull(data.m_szCellPhoneNo)) {
//						sms.setVisibility(View.GONE);
//						indicator.setVisibility(View.GONE);
//						officePhone.setVisibility(View.GONE);
//					}else{
//						sms.setVisibility(View.VISIBLE);
//						indicator.setVisibility(View.VISIBLE);
//						officePhone.setVisibility(View.VISIBLE);
//					}
//				}
				if(isMultiSelect) 
				{
					Log.i(TAG, "isMultiSelect!!!!!!");
					officePhone.setVisibility(View.GONE);
					sms.setVisibility(View.GONE);
					if(data.isSelected) 
						indicator.setImageResource(R.drawable.easy_member_check_on);
					else 
						indicator.setImageResource(R.drawable.easy_member_check_off);
				} 
				else if(isSingleSelect) 
				{
					officePhone.setVisibility(View.GONE);
					indicator.setVisibility(View.GONE);
					sms.setVisibility(View.GONE);
				} 
				else 
				{
					indicator.setImageResource(R.drawable.easy_member_btn_mobile_selector);
					sms.setVisibility(View.VISIBLE);
				}
				
			} else{
				//				sms = (ImageView)convertView.findViewById(R.id.ROW_EMPLOYEE_SMS);        	
				//				final ImageView indicator =
				//					(ImageView)convertView.findViewById(R.id.ROW_EMPLOYEE_PHONE);  
				//				sms.setVisibility(View.GONE);
				//				indicator.setVisibility(View.GONE);
			}

			return convertView;
		}

		/* (non-Javadoc)
		 * @see android.widget.SimpleExpandableListAdapter#getChildView(int, int, boolean, android.view.View, android.view.ViewGroup)
		 */
		@SuppressWarnings("unchecked")
		@Override
		public View getChildView(int groupPosition, int childPosition,
                                 boolean isLastChild, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			if(convertView == null) {
				convertView = newChildView(isLastChild, parent);
			}
			HashMap<String, EmployeeData> map =
				(HashMap<String, EmployeeData>)getGroup(groupPosition);
			final EmployeeData data = map.get("data");
			LinearLayout mobile = (LinearLayout)convertView.findViewById(R.id.callMobile);
			mobile.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					Uri uri = Uri.parse("tel:" + (String)data.m_szCellPhoneNo);
					Intent intent = new Intent(Intent.ACTION_DIAL, uri);
					startActivity(intent);
				}
			});
			LinearLayout office = (LinearLayout)convertView.findViewById(R.id.callOffice);
			office.setOnClickListener(new OnClickListener() {
				/* (non-Javadoc)
				 * @see android.view.View.OnClickListener#onClick(android.view.View)
				 */
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Uri uri = Uri.parse("tel:" + (String)data.m_szOfficePhoneNo);
					Intent intent = new Intent(Intent.ACTION_DIAL, uri);
					startActivity(intent);
				}
			});
			LinearLayout sms = (LinearLayout)convertView.findViewById(R.id.sendSMS);
			sms.setOnClickListener(new OnClickListener() {
				/* (non-Javadoc)
				 * @see android.view.View.OnClickListener#onClick(android.view.View)
				 */
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Uri uri = Uri.parse("sms:" + (String)data.m_szCellPhoneNo);
					Intent intent = new Intent(Intent.ACTION_SENDTO, uri);
					startActivity(intent);
				}
			});
			LinearLayout email = (LinearLayout)convertView.findViewById(R.id.sendEmail);
			email.setOnClickListener(new OnClickListener() {
				/* (non-Javadoc)
				 * @see android.view.View.OnClickListener#onClick(android.view.View)
				 */
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Intent intent = new Intent(AddressSearchActivity.this,
							SendEMAILActivity.class);
					ArrayList<String> mEmployeeName = new ArrayList<String>();
					ArrayList<String> mEmployeePhoneNum = new ArrayList<String>();
					ArrayList<String> mEmployeeVvip = new ArrayList<String>();
					mEmployeePhoneNum.add(data.m_szSerialNo);
					mEmployeeName.add(data.m_szName);
					if("Y".equals(data.getVvip())){
						mEmployeeVvip.add("true");
					}else{
						mEmployeeVvip.add("false");
					}
					intent.putStringArrayListExtra("phonenum", mEmployeePhoneNum);
					intent.putStringArrayListExtra("name", mEmployeeName);
					intent.putStringArrayListExtra("vvip", mEmployeeVvip);
					intent.putExtra("isChild", false);
					startActivityForResult(intent, 1);
				}
			});
			View split1 = convertView.findViewById(R.id.split1);
			View split2 = convertView.findViewById(R.id.split2);
			View split3 = convertView.findViewById(R.id.split3);

			if(data.m_szCellPhoneNo.trim().length() == 0) {
				mobile.setVisibility(View.GONE);
				split1.setVisibility(View.GONE);
				sms.setVisibility(View.GONE);
				split3.setVisibility(View.GONE);
			} else {
				mobile.setVisibility(View.VISIBLE);
				split1.setVisibility(View.VISIBLE);
				sms.setVisibility(View.VISIBLE);
				split3.setVisibility(View.VISIBLE);
			}
			if(data.m_szOfficePhoneNo.trim().length() == 0) {
				office.setVisibility(View.GONE);
				split2.setVisibility(View.GONE);
			} else {
				office.setVisibility(View.VISIBLE);
				split2.setVisibility(View.VISIBLE);
			}
			if(data.m_szMail.trim().length() == 0) {
				email.setVisibility(View.GONE);
			} else {
				email.setVisibility(View.VISIBLE);
			}

			return convertView;
		}

		/* (non-Javadoc)
		 * @see android.widget.SimpleExpandableListAdapter#getChildrenCount(int)
		 */
		@SuppressWarnings("unchecked")
		@Override
		public int getChildrenCount(int groupPosition) {
			// TODO Auto-generated method stub
			HashMap<String, EmployeeData> map =
				(HashMap<String, EmployeeData>)getGroup(groupPosition);
			EmployeeData data = map.get("data");

			if(data.m_szSerialNo.trim().length() == 0
					|| (data.m_szCellPhoneNo.trim().length() == 0
							&& data.m_szOfficePhoneNo.trim().length() == 0
							&& data.m_szMail.trim().length() == 0)) {
				return 0;
			}
			return 1;
		}
	}

	/**
	 * 최근 조회 목록 업데이트 클래스
	 * @author jokim
	 *
	 */
	public class LastSearchUpdateReceiver extends BroadcastReceiver
	{
		@Override
		public void onReceive(Context context, Intent intent)
		{			
			if(intent.getAction().equals(LAST_SEARCH_UPDATE)) 
			{
				if(mIsLastView) {
					getLastSearch();
					mAdapter.notifyDataSetChanged();
				}
			}
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


//	@Override
//	public void onBackPressed() {
//		if (DeleteDB()) {
//			super.onBackPressed();
//		}
//	}

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
//						SKTUtil.runApp(this, "com.ex.group.folder");	
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
	
	private static Document convertStringToDocument(String xmlStr) {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder;
		try{  
			builder = factory.newDocumentBuilder();  
			Document doc = builder.parse( new InputSource( new StringReader( xmlStr ) ) );
			return doc;
		}catch(Exception e) {
			e.printStackTrace();  
		} 
		return null;
	}
		  
	private static String getTagValue(String sTag, Element eElement) {
		 NodeList nlList = ((Document) eElement).getElementsByTagName(sTag).item(0).getChildNodes();
		 
		        Node nValue = (Node) nlList.item(0);
		 
		 return nValue.getNodeValue();
}

}

