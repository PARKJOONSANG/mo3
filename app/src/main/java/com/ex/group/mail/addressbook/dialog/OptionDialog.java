package com.ex.group.mail.addressbook.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.ex.group.mail.addressbook.widget.OptionAdapter;

import com.ex.group.folder.R;

/**
 * 옵션 다이얼로그 클래스
 * @author jokim
 *
 */
public class OptionDialog extends Dialog implements OnClickListener, OnItemClickListener {
	private String mTitle;
	private String[] mList;
	private OnOptionClickListener mListener;
	private int mIndex;
	private OptionAdapter mAdapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		WindowManager.LayoutParams lpWindow = new WindowManager.LayoutParams();
		lpWindow.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
		lpWindow.dimAmount = 0.8f;
		getWindow().setAttributes(lpWindow);

		//setCancelable(false);
		setContentView(R.layout.mail_member_option_dialog);
		
		View btnOK = findViewById(R.id.btnOK);
		View btnCancel = findViewById(R.id.btnCancel);
		ListView list = (ListView)findViewById(R.id.optionList);
		TextView title = (TextView)findViewById(R.id.title);
		title.setText(mTitle);
		
		btnOK.setOnClickListener(this);
		btnCancel.setOnClickListener(this);

		mAdapter = new OptionAdapter(getContext(), R.layout.mail_member_option_item, mList, mIndex);
		list.setAdapter(mAdapter);
		list.setSelection(mIndex);
		list.setOnItemClickListener(this);
	}

	public OptionDialog(Context context, String title, String[] list,
                        OnOptionClickListener listener, int index) {
		super(context, android.R.style.Theme_Translucent_NoTitleBar);
		this.mTitle = title;
		this.mList = list;
		this.mListener = listener;
		if(index < 0 || index >= list.length) {
			index = 0;
		}
		this.mIndex = index;
	}

	/* (non-Javadoc)
	 * 클릭 이벤트 핸들러<br>
	 * - 확인 버튼<br>
	 * - 취소 버튼
	 * @see android.view.View.OnClickListener#onClick(android.view.View)
	 */
	public void onClick(View v) {
		switch(v.getId()) {
		case R.id.btnOK:
			mListener.onOptionClick(mIndex);
			dismiss();
			break;
		case R.id.btnCancel:
			mListener.onOptionClick(-1);
			dismiss();
			break;
		}
	}
	
	public interface OnOptionClickListener {
		public void onOptionClick(int index);
	}

	/* (non-Javadoc)
	 * 리스트 아이템 클릭 이벤트 핸들러
	 * @see android.widget.AdapterView.OnItemClickListener#onItemClick(android.widget.AdapterView, android.view.View, int, long)
	 */
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub
		mIndex = arg2;
		for(int i = 0; i < mAdapter.getCount(); i++) {
			if(i == arg2) {
				mAdapter.setChecked(i, true);
			} else {
				mAdapter.setChecked(i, false);
			}
		}
		mAdapter.notifyDataSetChanged();
	}
}