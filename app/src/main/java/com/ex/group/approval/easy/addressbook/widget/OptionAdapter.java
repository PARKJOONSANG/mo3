package com.ex.group.approval.easy.addressbook.widget;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ex.group.folder.R;

public class OptionAdapter extends ArrayAdapter<String> {
	private boolean[] isChecked;

	public OptionAdapter(Context context, int textViewResourceId, String[] objects, int initIndex) {
		super(context, textViewResourceId, objects);
		isChecked = new boolean[objects.length];
		isChecked[initIndex] = true;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		String data = getItem(position);

		View v = convertView;
		if(v == null) {
			LayoutInflater vi = (LayoutInflater)getContext()
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			v = vi.inflate(R.layout.easy_member_option_item, null);
		}

		if(data != null) {
			ImageView checked = (ImageView)v.findViewById(R.id.checked);
			if(isChecked[position]) {
				checked.setImageResource(R.drawable.easy_member_btn_radioon);
			} else {
				checked.setImageResource(R.drawable.easy_member_btn_radio_selector);
			}
			
			TextView companyNm = (TextView)v.findViewById(R.id.companyNm);
			companyNm.setText(data);
		}

		return v;
	}
	
	public void setChecked(int index, boolean check) {
		isChecked[index] = check;
	}
}