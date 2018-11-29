package com.ex.group.board.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import com.ex.group.folder.R;
import com.ex.group.board.constants.Constants;
import com.ex.group.board.constants.Constants.RequestCode;
import com.ex.group.board.constants.Constants.RequestFeild;
import com.ex.group.board.custom.CustomLog;
import com.ex.group.board.custom.SchUtils;
import com.ex.group.board.data.BoardListDAO;
import com.ex.group.board.data.BoardListInfo;
import com.ex.group.board.data.UserInfo;
import com.ex.group.board.receiver.C2DMHelper;
import com.ex.group.board.service.BoardUserInfoRunnable;
import com.ex.group.board.widget.BoardListAdapter;
import com.skt.pe.common.activity.SKTActivity;
import com.skt.pe.common.conf.EnvironManager;
import com.skt.pe.common.data.AuthData;
import com.skt.pe.common.data.SKTUtil;
import com.skt.pe.common.dialog.SKTDialog;
import com.skt.pe.common.exception.SKTException;
import com.skt.pe.common.service.Controller;
import com.skt.pe.common.service.Parameters;
import com.skt.pe.common.service.XMLData;
import com.skt.pe.util.StringUtil;

import java.util.Map;

public class BoardListActivity_bak extends BoardActivity implements OnClickListener {

	private String tag = "BoardListActivity";
	private final String SEARCH_OP_SUBJECT = "2";
	private final String SEARCH_OP_AUTHOR = "1";
	private final int MENU_UPDATE = 10;
	private String _selectedType;
	private String _currentPage;
	
	private View footerView;
	
	private BoardListDAO boardListDAO;
	
	private int [] res_listView = {R.id.board_list_01, R.id.board_list_02, 
			R.id.board_list_03, R.id.board_list_04,
			R.id.board_list_05, R.id.board_list_06};
	private int [] res_tab = {R.id.board_list_tab01, R.id.board_list_tab02, 
			R.id.board_list_tab03, R.id.board_list_tab04,
			R.id.board_list_tab05, R.id.board_list_tab06};
	private int [] res_background = {R.drawable.board_tab01_selector_,  R.drawable.board_tab02_selector_,
			R.drawable.board_tab03_selector_, R.drawable.board_tab04_selector_,
			R.drawable.board_tab05_selector_, R.drawable.board_tab06_selector_};
	private ListView[] listViews;
	private Button[] buttons;
	
	private BoardListAdapter boardListAdapter;
	
	private EditText search_edit;
	private TextView board_detail_nodata;
	
	private String searchOp;
	private String searchKeyword;
	private int indexBtn;
	
	private boolean footerFlag;
	
	private int initTabIndex;
	
	@Override
	protected void onCreateX(Bundle savedInstanceState) {
		overridePendingTransition(0, 0);
		C2DMHelper.registC2DM(this);
		
		Intent intent = getIntent();
		
		if(intent != null && intent.getStringExtra("bType")!= null && !"".equals(intent.getStringExtra("bType"))){
			CustomLog.L(tag, intent.getStringExtra("bType"));
			CustomLog.L(tag, "c2dmData.getbType() " + intent.getStringExtra("bType"));
			
			//String num = intent.getStringExtra("bType");
			String num = ((BoardApplication)getApplicationContext()).getC2dmData().getbType();
			CustomLog.L(tag, "c2dmData.getbType() num2 " +num);
			for(int i=0;i<getResources().getStringArray(R.array.board_tab_num).length;i++){
				if(num.equals(getResources().getStringArray(R.array.board_tab_num)[i])) initTabIndex = i;
			}
			CustomLog.L(tag, "initTabIndex " + initTabIndex);
		}else{
			initTabIndex = 0;
		}
		
		Log.d(tag, "onCreateX() - res_tab : "+res_tab.length);
	}

	@Override
	protected void onResumeX() {
		// TODO Auto-generated method stub
		super.onResumeX();
		Log.i(tag, "onResume");
	}
	@Override
	protected int assignLayout() {
		// TODO Auto-generated method stub
		return R.layout.board_list_activity_bak;
	}




	@Override
	protected void onStartX() {
		// TODO Auto-generated method stub
		super.onStartX();
		if(boardListDAO == null) setInit();
		
		//2014-03-04 JSJ 사원정보.
		try {
			UserInfo.companyCd 	= StringUtil.isNull(SKTUtil.getCheckedCompanyCd(this)) ? EnvironManager.getTestCompanyCd() : SKTUtil.getCheckedCompanyCd(this);
			UserInfo.mdn			= StringUtil.isNull(SKTUtil.getMdn(this)) ? "mdn" : SKTUtil.getMdn(this);
			Map<String,String> map = SKTUtil.getGMPAuth(this);
			UserInfo.id			= map.get(AuthData.ID_ID);
		} catch (Exception e) {
		}
		BoardUserInfoRunnable userInfoRunnable = new BoardUserInfoRunnable(this, null, UserInfo.mdn, UserInfo.companyCd);
		new Thread(userInfoRunnable).start();
		
	}



	@Override
	public void setInit() {
		Log.d(tag, "setInit()~!");
		// TODO Auto-generated method stub
		
		listViews = new ListView[res_listView.length];
		buttons = new Button[res_tab.length];

		footerFlag = false;
		
		searchOp = SEARCH_OP_SUBJECT;
		searchKeyword = "";
		super.setInit();		
	}

	@Override
	public void setLayout() {
		Log.d(tag, "setLayout()~!");
		// TODO Auto-generated method stub		
		super.setLayout();	
		
		search_edit = (EditText)findViewById(R.id.board_list_edit_view);
		board_detail_nodata = (TextView) (findViewById(R.id.board_detail_nodata));
		
		setInitTabType(initTabIndex);	
		for(int i=0;i<res_listView.length;i++){
			listViews[i] = (ListView)findViewById(res_listView[i]);
			buttons[i] = (Button) findViewById(res_tab[i]);
			buttons[i].setTag(i);
			buttons[i].setOnClickListener(this);
		}
		setTabLayout(indexBtn);
		
		footerView = (LinearLayout) getLayoutInflater().inflate(R.layout.board_more,null,false);
		
		search_edit.setOnEditorActionListener(new OnEditorActionListener() {
			
			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				// TODO Auto-generated method stub
				if(actionId == EditorInfo.IME_ACTION_SEARCH){
					onSearchActiocn();
				}
				return false;
			}
		});
		
		findViewById(R.id.board_list_search_type).setOnClickListener(this);
		findViewById(R.id.board_list_search_btn).setOnClickListener(this);
	}
	
	
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		menu.add(0, MENU_UPDATE, Menu.NONE, getString(R.string.board_menu_update))
		.setIcon(R.drawable.board_option_refresh_selector);
		return super.onCreateOptionsMenu(menu);
	}

	
	

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Log.d(tag, "onOptionsItemSelected()~!");
		// TODO Auto-generated method stub
		int nId = item.getItemId();
    	
    	switch (nId) {
			case MENU_UPDATE:		
			try {
				getReqList(searchKeyword, boardListDAO == null ? "1" : (boardListDAO.getCurrentPage())+"", true);
			} catch (SKTException e) {
				// TODO Auto-generated catch block
				searchKeyword = "";
				e.alert(this);
			}
				break;
		}
		return super.onOptionsItemSelected(item);
	}


	private void setInitTabType(int index){
		Log.d(tag, "etInitTabType()~!");
		Log.d(tag, "etInitTabType() - index : "+index);
		Log.d(tag, "etInitTabType() - _selectedType : "+_selectedType);
		_selectedType = getResources().getStringArray(R.array.board_tab_num)[index];
		_currentPage = "1";
		search_edit.setText("");
		searchKeyword = "";
		indexBtn = index;
		board_detail_nodata.setVisibility(View.GONE);
		// 검색 옵션 초기화
		/*searchOp = SEARCH_OP_SUBJECT;
		((TextView)findViewById(R.id.board_list_search_type)).setText(getResources().getString(R.string.board_select_type1));*/
		
		SKTUtil.hideKeyboard(search_edit);
	}
	
	/**
	 * 
	 * @param type type : -1 초기화
	 */
	private void setTabLayout(int index){
		Log.d(tag, "setTabLayout()~!");
		Log.d(tag, "setTabLayout() - index : "+index);
		for(int i=0;i<res_listView.length;i++){
			listViews[i].setVisibility(View.GONE);
			listViews[i].setOnItemClickListener(null);
//			buttons[i].setBackgroundResource(R.drawable.btn_tab);
			buttons[i].setBackgroundResource(res_background[i]);
			
			if(i == index) {
				buttons[i].setPressed(true);
				buttons[i].setSelected(true);
			}
			else {
				buttons[i].setPressed(false);
				buttons[i].setSelected(false);
			}
		}
		
		setInitTabType(index);
		try {
			getReqList("", _currentPage, true);
		} catch (SKTException e) {
			// TODO Auto-generated catch block
			e.alert(this);
		}
		
//		buttons[index].setBackgroundResource(R.drawable.tab_borard_f);
		
	
	}

	
	
	
	
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub		
		/*if(boardListDAO != null && boardListDAO.getCurrentPage() > 1 && false){
			getReqList(searchKeyword, (boardListDAO.getCurrentPage()-1)+"", true);
		}else{*/
			super.onBackPressed();
		//}
	}



	private void getReqList(String kwd, String page, boolean pendding) throws SKTException {
		Log.d(tag, "getReqList()~!");
		if(kwd == null) kwd = "";
		String searchType = "".equals(kwd) ? "0" : searchOp;
		searchKeyword = kwd;
		new SKTActivity.Action(Constants.BOARD_LIST_PRIMITIVE, pendding).execute(_selectedType, page, kwd, searchType);
		
	}
	
	
	private void setResList(XMLData result) throws SKTException {
		CustomLog.L(tag, "-------------------------------------------------------==================================");
		//ArrayList<BoardListInfo> arrayList = new ArrayList<BoardListInfo>();
		
		//if(footerFlag)		arrayList.addAll(boardListDAO.getListInfo());
		
		boardListDAO = new BoardListDAO();
		boardListDAO.initListInfo();
		XMLData tempXML = result.getChild("mo");
		boardListDAO.setCurrentTotalPage(Integer.parseInt(tempXML.get("totalPageCnt")));
		boardListDAO.setCurrentPage(Integer.parseInt(tempXML.get("page")));
		
		tempXML.setList("boardList");
		boardListDAO.setListCnt(Integer.parseInt(tempXML.get("listCnt")));
		boardListDAO.setEndPage("Y".equals(tempXML.get("end")));
		
		tempXML.setList("board");
		
		boardListDAO.getListInfo().clear();
		
		//if(footerFlag)		boardListDAO.setListInfo(arrayList);
		
		for(int i=0;i<boardListDAO.getListCnt();i++){
			BoardListInfo boardListInfo = new BoardListInfo();
			boardListInfo.setAttachment(SchUtils.ChkNullStr(tempXML.get(i, "attachment") , ""));
			boardListInfo.setAuthor(SchUtils.ChkNullStr(tempXML.get(i, "author") , ""));
			boardListInfo.setChildrenCnt(SchUtils.ChkNullStr(tempXML.get(i, "childrenCnt") , ""));
			boardListInfo.setId(SchUtils.ChkNullStr(tempXML.get(i, "id") , ""));
			boardListInfo.setLevel(SchUtils.ChkNullStr(tempXML.get(i, "level") , "0"));
			boardListInfo.setParentId(SchUtils.ChkNullStr(tempXML.get(i, "parentId") , ""));
			boardListInfo.setRead(SchUtils.ChkNullStr(tempXML.get(i, "read") , ""));
			boardListInfo.setTeam(SchUtils.ChkNullStr(tempXML.get(i, "team") , ""));
			boardListInfo.setTitle(SchUtils.ChkNullStr(tempXML.get(i, "title") , ""));
			String date = SchUtils.ChkNullStr(tempXML.get(i, "writeDate") , "");
			boardListInfo.setWriteDate(date);
			
			Log.d("", "title:"+boardListInfo.getTitle());
			
			boardListDAO.getListInfo().add(boardListInfo);
		}
		
		CustomLog.L(tag, boardListDAO.toString());
		CustomLog.L(tag, "-------------------------------------------------------==================================");
		setListView(indexBtn, boardListDAO);
	}
	
	private void setListView(int type, BoardListDAO listDAO){
		CustomLog.L(tag, "-----type ======================== " + type);

		footerFlag = false;
		listViews[type].setVisibility(View.VISIBLE);
		
		if(listViews[type].getFooterViewsCount()>0) 	listViews[type].removeFooterView(footerView);
		
		if(boardListDAO.getCurrentPage() >= boardListDAO.getCurrentTotalPage() || boardListDAO.isEndPage()){
			
		}else{
			footerView.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					try {
						getReqList(searchKeyword, boardListDAO == null ? "1" :(boardListDAO.getCurrentPage()+1)+"", true);
					} catch (SKTException e) {
						// TODO Auto-generated catch block
						e.alert(BoardListActivity_bak.this);
					}
					footerFlag = true;
				}
			});
			listViews[type].addFooterView(footerView);
		}
		
		if(boardListDAO.getCurrentPage() == 1){
			boardListAdapter = new BoardListAdapter(this, R.layout.board_list, boardListDAO.getListInfo());
			listViews[type].setAdapter(boardListAdapter);
			listViews[type].setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
					// TODO Auto-generated method stub
					CustomLog.L(tag, "indexBtn " + indexBtn);
					BoardListInfo boardListInfo = (BoardListInfo)parent.getAdapter().getItem(position);
					CustomLog.L(tag, boardListInfo.toString());
					Intent de = new Intent(BoardListActivity_bak.this, BoardDetailActivity.class);
					Log.d(tag, "######## indexBtn : "+indexBtn);
					Log.d(tag, "######## _selectedType : "+_selectedType);
					Log.d(tag, "######## boardListInfo.getId() : "+boardListInfo.getId());
					Log.d(tag, "######## boardListInfo.getTeam() : "+boardListInfo.getTeam());
					de.putExtra("indexBtn", indexBtn+"");
					de.putExtra("type", _selectedType);
					de.putExtra("id", boardListInfo.getId());
					de.putExtra("yearmon", boardListInfo.getTeam());
					startActivityForResult(de, RequestCode.DetailActivity);
				}
				
			});
		}else{
			for(int i=0;i<boardListDAO.getListInfo().size();i++){
				boardListAdapter.add(boardListDAO.getListInfo().get(i));
			}
		}
		
		if(boardListDAO.getListInfo().size() == 0){
			board_detail_nodata.setVisibility(View.VISIBLE);
			listViews[type].setVisibility(View.GONE);
		}else{
			if(boardListDAO.getListInfo().size() == 1 && "등록된 게시물이 없습니다.".equals(boardListDAO.getListInfo().get(0).getId())) {
				board_detail_nodata.setVisibility(View.VISIBLE);
				listViews[type].setVisibility(View.GONE);
			} else {
				listViews[type].setVisibility(View.VISIBLE);
				board_detail_nodata.setVisibility(View.GONE);
			}
		}
		
	}

	
	@Override
	protected void onActivityResultX(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResultX(requestCode, resultCode, data);
		
		switch (requestCode) {
		case RequestCode.DetailActivity:
			CustomLog.L(tag, "onActivityResultX " + resultCode);
			if(resultCode == RESULT_OK){
				
			}else if(resultCode == RESULT_CANCELED){
				
			}
			break;

		default:
			break;
		}
	}



	@Override
	protected XMLData onAction(String primitive, String... args)throws SKTException {
		CustomLog.L(tag, args);
		
		Parameters param  = new Parameters(primitive);
		
		param.put(RequestFeild.LIST_TYPE, args[0]);
		param.put(RequestFeild.LIST_PG, args[1]);
		param.put(RequestFeild.LIST_KWD, args[2]);
		param.put(RequestFeild.LIST_SEARCHTYPE, args[3]);
		Log.d(tag, "onAction() - primitive : "+primitive);
		Log.d(tag, "onAction() - args : "+args);
		Log.d("","onAction value 1= " + args[0]);
		Log.d("","onAction value 2= " + args[1]);
		Log.d("","onAction value 3= " + args[2]);
		Log.d("","onAction value 4= " + args[3]);
		Controller controller = new Controller(this);
		return controller.request(param/*,true*/);
	}

	
	@Override
	protected void onActionPost(String primitive, XMLData result, SKTException e) throws SKTException {
		// TODO Auto-generated method stub
		if(result != null && e == null )	{			
			setResList(result);		
		}else{
			e.alert(this);
		}
	}


	private void onSearchActiocn(){
		if(search_edit.getText().toString().length() >= 2){
			try {
				getReqList(search_edit.getText().toString(), "1", true);
			} catch (SKTException e) {
				// TODO Auto-generated catch block
				e.alert(this);
			}
			SKTUtil.hideKeyboard(search_edit);
		}else{
			if(search_edit.getText().toString().length() == 0){
				try {
					getReqList("", "1", true);
				} catch (SKTException e) {
					// TODO Auto-generated catch block
					e.alert(this);
				}
				SKTUtil.hideKeyboard(search_edit);
			}else{
				SKTDialog dl = new SKTDialog(BoardListActivity_bak.this, SKTDialog.DLG_TYPE_1);
				dl.getDialog("검색어 입력 오류", "검색어는 2자 이상 입력하셔야 됩니다. ").show();
			}
		}
		
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		
		case R.id.board_list_tab01:
		case R.id.board_list_tab02:
		case R.id.board_list_tab03:
		case R.id.board_list_tab04:
		case R.id.board_list_tab05:			
		case R.id.board_list_tab06:
			setTabLayout(Integer.parseInt(v.getTag().toString()));
			break;
			
		case R.id.board_list_search_btn:
			onSearchActiocn();
			break;
			
		case R.id.board_list_search_type:
			TextView searchType = ((TextView)findViewById(R.id.board_list_search_type));
			if(searchOp.equals(SEARCH_OP_SUBJECT)){
				searchType.setText(getResources().getString(R.string.board_select_type2));
				searchOp = SEARCH_OP_AUTHOR;
				searchType.setTag(searchOp);
			}else if(searchOp.equals(SEARCH_OP_AUTHOR)){
				searchType.setText(getResources().getString(R.string.board_select_type1));
				searchOp = SEARCH_OP_SUBJECT;
				searchType.setTag(searchOp);
			}
			break;
		}
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
	}		
	
	// Handler 를 이용하여 처리하기
	/*private boolean m_Flag = false;
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
					// Handler 호출 (0.5초 이후 back_Check 값 false)
					back_Handler.sendEmptyMessageDelayed(0, 1000);
					return false;
				} else {
					
					try{
						SKTUtil.runApp(this, "com.ex.group.folder");	
					}catch(Exception e){
						e.printStackTrace();
					}						
					
					moveTaskToBack(true);
					finish();
					return true;
				}
			}
			return false;
		}		
		return false;
	}	*/	
		

}
