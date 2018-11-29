package com.ex.group.mail.widget;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ex.group.folder.R;
import com.ex.group.mail.data.EmailMainListData;
import com.skt.pe.util.StringUtil;



/**
 * 메일함 Adapter
 * @author sjsun5318
 *
 */
public class MainListAdapter extends ArrayAdapter<EmailMainListData> {
	String TAG = "adapter";
	public MainListAdapter(Context a_context, int a_nTextViewResourceId) {
		super(a_context, a_nTextViewResourceId);
	}
	
	public MainListAdapter(Context a_context, int a_nTextViewResourceId, EmailMainListData[] a_object) {
		super(a_context, a_nTextViewResourceId, a_object);
	}

	/**
	 * View UI 표현
	 * @param a_nPosition
	 * @param a_convertView
	 * @param a_parent
	 */
	@Override
	public View getView(int a_nPosition, View a_convertView, ViewGroup a_parent) {
		// TODO Auto-generated method stub
		View v = a_convertView;
		
		if(v == null) {
			LayoutInflater vi = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			v = vi.inflate(R.layout.mail_box_item, null);
		}
		
		EmailMainListData item = getItem(a_nPosition);
		
		if(item != null ) {
			
			int level = 0 ;
			try {
				level = Integer.parseInt(item.getBoxLevel());
			} catch (Exception e) {
				level = 0;
			}
			
			ImageView imageView = (ImageView)v.findViewById(R.id.MAIN_ICON);
			View mainlist = v.findViewById(R.id.MainList);
			if(item.getBoxType().equals("I1")) {
				imageView.setImageResource(R.drawable.mail_icon_1_selector);
				
			} else if(item.getBoxType().equals("I2")) {
				imageView.setImageResource(R.drawable.mail_icon_2_selector);
				
			} else if(item.getBoxType().equals("S")) {
				imageView.setImageResource(R.drawable.mail_icon_3_selector);
				
			} else if(item.getBoxType().equals("T")) {
				imageView.setImageResource(R.drawable.mail_icon_5_selector);
				
			} else if(item.getBoxType().equals("D")) {
				imageView.setImageResource(R.drawable.mail_icon_4_selector);
			
			} else {
				imageView.setImageResource(R.drawable.mail_icon_6_selector);
			}
			mainlist.setPadding(level * 50,0,0,0);
			
			
			TextView view1 = (TextView)v.findViewById(R.id.MAINTEXT);
			if(view1 != null) {
				view1.setText(item.getBoxName());
			}
			
			
			TextView view3 = (TextView)v.findViewById(R.id.MAILCNT);
			if(view3 != null && "I1".equals(item.getParentBoxType())) {
				if(StringUtil.isNull(item.getUnreadCnt())) {
					view3.setText("0건");
				} else {
					view3.setText(item.getUnreadCnt() );
					// "건" 삭제
				}
				view3.setVisibility(View.VISIBLE);
			} else {
				view3.setVisibility(View.GONE);
			}
		}
		return v;
	}
}
