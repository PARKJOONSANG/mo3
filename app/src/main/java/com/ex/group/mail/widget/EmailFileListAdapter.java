package com.ex.group.mail.widget;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.ex.group.mail.activity.EmailDetailActivity;
import com.ex.group.mail.data.EmailFileListData;

import com.ex.group.folder.R;


/**
 * 파일 Adapter
 * @author sjsun5318
 *
 */
public class EmailFileListAdapter extends ArrayAdapter<EmailFileListData> {

	protected static final int COLOR_READ = Color.rgb(207, 213, 223);
	protected static final int COLOR_READ_TEXT = Color.rgb(97, 98, 99);


	public EmailFileListAdapter(Context a_context, int a_nTextViewResourceId, EmailFileListData[] a_object) {
		super(a_context, a_nTextViewResourceId, a_object);
	}

	public EmailFileListAdapter(
            EmailDetailActivity emailDetailActivity, int filelist,
            EmailFileListData[] mReceiveDatas, int del) {
		// TODO Auto-generated constructor stub
		super(emailDetailActivity, filelist, mReceiveDatas);
	}

	/**
	 * View UI 표현
	 * @param a_nPosition
	 * @param a_convertView
	 * @param a_parent
	 */
	public View getView(int a_nPosition, View a_convertView, ViewGroup a_parent) {
		// TODO Auto-generated method stub
		View v = a_convertView;

		if(v == null) {
			LayoutInflater vi = (LayoutInflater)getContext()
			.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			v = vi.inflate(R.layout.mail_filelist, null);
		}
		
		EmailFileListData item = getItem(a_nPosition);

		if(item != null) {
			TextView m_TextView = (TextView)v.findViewById(R.id.filebtn);

			m_TextView.setText(item.getM_szName());

//			LinearLayout fileBtn = (LinearLayout)v.findViewById(R.id.filebtn);
//			fileBtn.setClickable(true);
//
//
//			fileBtn.setOnClickListener(new EventHandler(item,
//					(EmailDetailActivity)getContext()));
		}

		return v;
	}

	/**
	 * 파일 OnclickListener 이벤트 핸들러
	 * @author sjsun5318
	 *
	 */
	public class EventHandler implements OnClickListener {
		private EmailFileListData m_data;
		private EmailDetailActivity m_activity;

		public EventHandler(EmailFileListData a_data, EmailDetailActivity a_activity) {
			m_data = a_data;
			m_activity = a_activity;
		}

		/* (non-Javadoc)
		 * @see android.view.View.OnClickListener#onClick(android.view.View)
		 */
		public void onClick(View view) {
			m_activity.newAction("MAIL_ATTACHCHECK").execute(m_data.getM_szId(), m_data.getM_szName());

		} // end public void onClick(View view)
	}	


}
