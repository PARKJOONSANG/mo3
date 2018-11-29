package com.ex.group.approval.easy.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ex.group.folder.R;
import com.ex.group.approval.easy.domain.MainListItem;

/**
 * 간이결재 메인 Adapter
 * @author pluto248
 *
 */
public class MainListAdapter extends ArrayAdapter<MainListItem> {
	
	public MainListAdapter(Context context, int textViewResourceId) {
		super(context, textViewResourceId);
	}
	
	public MainListAdapter(Context context, int textViewResourceId, MainListItem[] objects) {
		super(context, textViewResourceId, objects);
	}
	
//	public void init(Context context, int textViewResourceId, MainListItem[] objects) {
//	}

	/**
	 * View UI 표현
	 * @param position
	 * @param convertView
	 * @param parent
	 */
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View v = convertView;
		
		if(v == null) {
			LayoutInflater vi = (LayoutInflater)getContext()
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//			v = vi.inflate(R.layout.main_listview_item, null);
			v = vi.inflate(R.layout.easy_main_listview_item, parent, false);
		}
		
		fillList(position, v);
		
		return v;
	}
	
	private void fillList(int position, View v) {
		TextView view1 = (TextView)v.findViewById(R.id.main_textview_title);
		ImageView imageView = (ImageView)v.findViewById(R.id.main_imageview_icon);
		TextView view3 = (TextView)v.findViewById(R.id.main_textview_count);
		View mainLayoutCount = v.findViewById(R.id.main_layout_count);
		
		MainListItem item = getItem(position);

		imageView.setImageResource(item.imageId);
		view1.setText(item.title);
		view1.setTextColor(Color.rgb(62, 62, 62));
		
		if(view3 != null && item.newItemCount >= 0) {
			view3.setText(String.valueOf(item.newItemCount));
			view3.setVisibility(View.VISIBLE);
			mainLayoutCount.setVisibility(View.VISIBLE);
		} else {
			view3.setVisibility(View.GONE);
			mainLayoutCount.setVisibility(View.GONE);
		}
	}
}
