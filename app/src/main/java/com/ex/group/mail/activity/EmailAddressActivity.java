package com.ex.group.mail.activity;/*package com.ex.group.mail.activity;


import java.util.ArrayList;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.ContactsContract.CommonDataKinds.Email;
import android.provider.ContactsContract.Data;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.ex.group.mail.R;
import com.ex.group.mail.R.drawable;
import com.ex.group.mail.util.EmailClientUtil;
import com.skt.pe.common.activity.SKTActivity;
import com.skt.pe.common.dialog.DialogButton;
import com.skt.pe.common.dialog.SKTDialog;
import com.skt.pe.common.exception.SKTException;
import com.skt.pe.common.service.Controller;
import com.skt.pe.common.service.Parameters;
import com.skt.pe.common.service.XMLData;
*//**
 * 단말기에 등록돼있는 주소록 가져오기
 * @author sjsun5318
 *
 *//*

public class EmailAddressActivity extends SKTActivity implements OnItemClickListener, OnClickListener {
	
	private EmailList[] data = null;
	private ListView list = null;
	private ArrayList<String> mailListName = new ArrayList<String>();
	private ArrayList<String> mailListEmail = new ArrayList<String>();
	private EditText addrEditText = null;
	private LinearLayout addr_Search_Layout = null;
	private int type;
	private int pageNum;
	private LinearLayout moreBtn;
	private EmailAdapter adapter;
	private String searchText;
	private TextView settitle;
	
	*//**
	 * onCreate 메소드
	 * @param savedInstanceState
	 *//*
	@Override
	protected void onCreateX(Bundle savedInstanceState) {
		Intent intent = getIntent();
		type = intent.getIntExtra("type", 0);
		list = (ListView)findViewById(R.id.ADDRLIST);
		settitle = (TextView)findViewById(R.id.settitle);
		list.setOnItemClickListener(this);
		addrEditText = (EditText)findViewById(R.id.Addrss_SearchInputText);
		addr_Search_Layout = (LinearLayout)findViewById(R.id.Search_Layout);
		findViewById(R.id.ALLDel).setOnClickListener(this);
		findViewById(R.id.Del).setOnClickListener(this);
		findViewById(R.id.Addrss_SearchButton).setOnClickListener(this);
		moreBtn = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.listfooterview, null);
		adapter = new EmailAdapter(this, R.layout.emailaddress_list);
		list.setAdapter(adapter); 
		if(type == 1) {
			settitle.setText("주소록검색");
			addr_Search_Layout.setVisibility(View.GONE);
			Cursor c = EmailAddressActivity.this.managedQuery(Data.CONTENT_URI,
					new String[] { Email.DATA,Data.DISPLAY_NAME },
					Data.MIMETYPE + " = '" + Email.CONTENT_ITEM_TYPE + "'",
					null, null);
			 
			
			if(c != null){
				data = new EmailList[c.getCount()];
				int a = 0 ;
				while(c.moveToNext()) {
//					mailListName.add(c.getString(c.getColumnIndex(Data.DISPLAY_NAME)));
//					mailListName.add(c.getString(c.getColumnIndex(Email.DATA)));
					
					data[a] = new EmailList(c.getString(c.getColumnIndex(Data.DISPLAY_NAME)),c.getString(c.getColumnIndex(Email.DATA)),false);
					a++;
				}
			}
			TextView text = (TextView)findViewById(R.id.textnolist);
			if(data.length <= 0){
				text.setVisibility(View.VISIBLE);
			}else{
				text.setVisibility(View.GONE);
			}
			
			addAdapter(data);
			
		} else if(type == 2) {
			settitle.setText("DL 검색");
			addr_Search_Layout.setVisibility(View.VISIBLE);
			pageNum = 1;
			new Action(EmailClientUtil.COMMON_MAIL_CONTACTLIST).execute("");
			
		}
		
	}
	
	private void addAdapter(EmailList[] mailListData) {
		for(EmailList list : mailListData) {
			adapter.add(list);
		}
		adapter.notifyDataSetChanged();
	}
	
	

	*//**
	 * onClick 이벤트 핸들러<br>
	 * - 완료 버튼
	 * @param v
	 *//*
	@Override
	public void onClick(View v) {
		if(v.getId() == R.id.ALLDel) {
			if(data != null) {
				for(int a = 0 ; a < data.length ; a++ ){
					if(data[a].isChecked()){
						mailListName.add(data[a].getName());
						mailListEmail.add(data[a].getEmail());
					}
				}
				
				String[] names = new String[mailListName.size()];
				String[] emails = new String[mailListEmail.size()];
				names = mailListName.toArray(names);
				emails = mailListEmail.toArray(emails);
				
				
				Intent intent = new Intent();
				intent.putExtra("names", names);
				intent.putExtra("emails", emails);
				setResult(RESULT_OK,intent);
				finish();
			} else {
				finish();
			}
			
			
		} else if(v.getId() == R.id.Addrss_SearchButton) {
			pageNum = 1;
			if(adapter != null && adapter.getCount() > 0) {
				adapter.clear();
			}
			EmailClientUtil.hideSoftInputWindow(v);
			searchText = addrEditText.getText().toString();
			char[] digit = searchText.toCharArray();
			int len = digit.length;
			
			if (len < 2) {
				SKTDialog dlg = new SKTDialog(this, SKTDialog.DLG_TYPE_1);
				dlg.getDialog(getResources().getString(R.string.SEARCH_ERROR_TITLE_TEXT), 
						getResources().getString(R.string.SEARCH_ERROR_TEXT), new DialogButton(0) {
					public void onClick(DialogInterface dialog, int which) {
					}
				}).show();
				return;
			} else {
				new Action(EmailClientUtil.COMMON_MAIL_DL).execute(searchText,Integer.toString(pageNum));
			}
		} else if(v.getId() ==R.id.moreButton) {
			new Action(EmailClientUtil.COMMON_MAIL_DL).execute(searchText,Integer.toString(++pageNum));
		} else {
			finish();
		}
		
	}

	*//**
	 * 선택한 주소록 체크
	 * @param a_position
	 * @param a_checked
	 *//*
	public void checkItem(int a_position, boolean a_checked){
		//data[a_position].setChecked(a_checked);
		adapter.getItem(a_position).checked = a_checked;
	}
	
	 

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
			long arg3) {
		// TODO Auto-generated method stub

	}

	@Override
	protected int assignLayout() {
		// TODO Auto-generated method stub
		return R.layout.emailaddrss;
	}

	@Override
	protected int onActionPre(String primitive) {
		// TODO Auto-generated method stub
		return Action.SERVICE_RETRIEVING;
	}

	@Override
	protected XMLData onAction(String primitive, String... args)
	throws SKTException {
		Parameters params = new Parameters(primitive);
		Controller controller = new Controller(this);
		
		//2014-02-06 JSJ 사원 아이디,이름 추가
		params.put("empId", EmailClientUtil.id);
		params.put("empName", EmailClientUtil.empNm);
		params.put("keyword", args[0]);
		params.put("page", args[1]);
		return controller.request(params);
	}

	@Override
	protected void onActionPost(String primitive, XMLData result,
			SKTException e) throws SKTException {
		if(e != null) {				
			e.alert(getResources().getString(R.string.error) , this, new DialogButton(0) {
				public void onClick(DialogInterface arg0 , int arg1) {
					finish();
				}
			});
		} else {
			String end = result.get("end");
			result.setList("people");
			data = new EmailList[result.size()];
			for(int a = 0 ; a < result.size() ; a ++) {
				data[a] = new EmailList(result.get(a, "name"), result.get(a, "email"), false);
			}

			TextView text = (TextView)findViewById(R.id.textnolist);
			if(data.length <= 0){
				text.setVisibility(View.VISIBLE);
			}else{
				text.setVisibility(View.GONE);
			}
			addAdapter(data);
			if("N".equals(end)) {
				if(list.getFooterViewsCount() == 0) {
					list.addFooterView(moreBtn);
					TextView moreBtn2 = (TextView)moreBtn.findViewById(R.id.moreButton);
					moreBtn2.setOnClickListener(this);
					list.setAdapter(adapter);
				}
			} else {
				if(list.getFooterViewsCount() > 0) {
					list.removeFooterView(moreBtn);
				}
			}
		}

	}
		*//**
		 * 리스트에 들어갈 데이터 Class
		 * @author sjsun5318
		 *
		 *//*
	class EmailList { 

		private String name; 
		private String email; 
		private boolean checked;

		public EmailList(String name, String email,boolean checked){ 
			this.name = name; 
			this.email = email; 
		}


		public boolean isChecked() {
			return checked;
		}

		public void setChecked(boolean checked) {
			this.checked = checked;
		}


		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getEmail() {
			return email;
		}

		public void setEmail(String email) {
			this.email = email;
		} 
	}
		
	*//**
	 * 리스트 선택시  EventHandler
	 * @author sjsun5318
	 *
	 *//*
	public class EventHandler implements OnClickListener {
		private EmailList m_data;
		private EmailAddressActivity m_activity;
		private int m_position;

		public EventHandler(EmailList a_data, EmailAddressActivity a_activity,
				int a_position) {
			m_data = a_data;
			m_activity = a_activity;
			m_position = a_position;
		}

		@Override
		public void onClick(View view) {

			if(m_data.checked)
			{
				view.setBackgroundResource(drawable.btn_uncheck);
				m_activity.checkItem(m_position, false);
			}else{
				view.setBackgroundResource(drawable.btn_check);
				m_activity.checkItem(m_position, true);
			}


		} // end public void onClick(View view)
	}	
		
	*//**
	 * 리스트에 들어갈  Adapter 
	 * @author sjsun5318
	 *
	 *//*
	class EmailAdapter extends ArrayAdapter<EmailList> { 


		protected final int COLOR_MAIN_BG = Color.parseColor("#F7F7F7");//rgb(247,247,247);
		private Context context;



		public Context getContext() {
			return context;
		}

		public void setContext(Context context) {
			this.context = context;
		}
		
		public EmailAdapter(Context context, int textViewResourceId) {
			super(context, textViewResourceId);
			this.context = context;
		}

		public EmailAdapter(Context context, int textViewResourceId, EmailList[] items) {
			super(context, textViewResourceId, items);
			this.context = context;
		}


		@Override
		public View getView(final int a_nPosition, View a_convertView, ViewGroup a_parent) {
			// TODO Auto-generated method stub
			View v = a_convertView;

			if(v == null) {

				v = (LinearLayout) LayoutInflater.from(this.context).inflate(R.layout.emailaddress_list, null);
			}
			final EmailList item = getItem(a_nPosition);

			LinearLayout m_mainLayout = (LinearLayout)v.findViewById(R.id.MAIN_LAYOUT);



			if(a_nPosition%2==0){
				m_mainLayout.setBackgroundDrawable(getContext().getResources().getDrawable(R.drawable.list_view_selector1));
			}else{
				m_mainLayout.setBackgroundDrawable(getContext().getResources().getDrawable(R.drawable.list_view_selector));
			}
			if(item != null) {
				TextView nameTextView = (TextView)v.findViewById(R.id.address_name);
				nameTextView.setText(item.getName());

				TextView emailTextView = (TextView)v.findViewById(R.id.address_email);
				if(item.getEmail().split("@").length > 1) {
					emailTextView.setText(item.getEmail());
				} else {
					emailTextView.setText("");
				}	

				final Button fileBtn = (Button)v.findViewById(R.id.address_checkbox);
				if(emailTextView.getText().toString().trim().equals("")){
					fileBtn.setClickable(false);
				} else {
					fileBtn.setOnClickListener(new EventHandler(item,(EmailAddressActivity)getContext(),a_nPosition));
					v.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							if(item.checked)
							{
								fileBtn.setBackgroundResource(drawable.btn_uncheck);
								((EmailAddressActivity)getContext()).checkItem(a_nPosition, false);
								//v.setBackgroundColor(android.R.drawable.list_selector_background);


							}else{
								fileBtn.setBackgroundResource(drawable.btn_check);
								((EmailAddressActivity) getContext()).checkItem(a_nPosition, true);
								//v.setBackgroundColor(android.R.drawable.list_selector_background);

							}

						}
					});
				}

				if(item.checked) {
					fileBtn.setBackgroundResource(drawable.btn_check);
				} else {
					fileBtn.setBackgroundResource(drawable.btn_uncheck);
				}

			}
			return v;
		}
	}
}
	*/