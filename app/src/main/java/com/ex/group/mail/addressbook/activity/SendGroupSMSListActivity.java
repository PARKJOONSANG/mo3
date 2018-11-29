package com.ex.group.mail.addressbook.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.ex.group.mail.addressbook.data.SmsData;
import com.skt.pe.common.activity.SKTActivity;
import com.skt.pe.common.exception.SKTException;
import com.skt.pe.common.service.XMLData;

import java.util.ArrayList;

import com.ex.group.folder.R;

/**
 * 선택된 멤버 목록 보여주는 화면
 * @author jokim
 *
 */
public class SendGroupSMSListActivity extends SKTActivity implements OnClickListener {
	private ArrayList<SmsData> mSmsList;
	
	private TextView countView;
	private ListView listView;
	private Button sendView;
	private Button cancelView;
	
	@Override
	protected int assignLayout() {
		// TODO Auto-generated method stub
		return R.layout.mail_member_receivelist;
	}

	@Override
	protected void onCreateX(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		mSmsList = SendGroupSMSActivity.sSmsList;
		
		countView = (TextView)findViewById(R.id.TEXT_COUNT);
		listView = (ListView)findViewById(R.id.MMSelectList);
		sendView = (Button)findViewById(R.id.multiselect_confirm);
		cancelView = (Button)findViewById(R.id.multiselect_cancel);
		
		sendView.setOnClickListener(this);
		cancelView.setOnClickListener(this);
		
		updateCount();
		
		ListAdapter adapter = new ListAdapter(this, mSmsList);
		listView.setAdapter(adapter);
	}

	@Override
	protected int onActionPre(String primitive) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	protected XMLData onAction(String primitive, String... args)
			throws SKTException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void onActionPost(String primitive, XMLData result, SKTException e)
			throws SKTException {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * 클릭 이벤트 핸들러<br>
	 * - 확인 버튼<br>
	 * - 취소 버튼
	 * @see android.view.View.OnClickListener#onClick(android.view.View)
	 */
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if(v.getId() == R.id.multiselect_confirm) {
			sendSms();
			onBackPressed();
		} else if(v.getId() == R.id.multiselect_cancel) {
			onBackPressed();
		}
	}
	
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		setResult(RESULT_OK);
		finish();
	}
	
	/**
	 * 문자 메세지 보내기
	 */
	private void sendSms() {
    	String target = "";
    	
    	for(SmsData sms: mSmsList) {
    		String phone = sms.getPhone();
    		
    		target += phone + ";";
    	}
        Uri uri = Uri.parse("sms:" + (String)target);
        Intent intent = new Intent(Intent.ACTION_SENDTO, uri);
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        startActivity(intent);
	}

	/**
	 * 선택된 멤버 목록 카운트 업데이트
	 */
	private void updateCount() {
		countView.setText("(" + mSmsList.size() + "/50)");
	}
	
	private class ListAdapter extends ArrayAdapter<SmsData> {
		public ListAdapter(Context context, ArrayList<SmsData> data) {
			super(context, 0, data);
			// TODO Auto-generated constructor stub
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			if(convertView == null) {
				convertView = LayoutInflater.from(getContext()).inflate(R.layout.mail_member_receivelistitem,
						null);
			}
			
			final SmsData data = getItem(position);
			
			if(data != null) {
				TextView name = (TextView)convertView.findViewById(R.id.employee_name);
				name.setText(data.getName());
				
				TextView phone = (TextView)convertView.findViewById(R.id.employee_data);
				phone.setText(data.getPhone());
				
				Button delete = (Button)convertView.findViewById(R.id.employee_delete);
				delete.setOnClickListener(new OnClickListener() {
					public void onClick(View v) {
						// TODO Auto-generated method stub
						remove(data);
						notifyDataSetChanged();
						updateCount();
					}
				});
			}
			
			return convertView;
		}
	}
}