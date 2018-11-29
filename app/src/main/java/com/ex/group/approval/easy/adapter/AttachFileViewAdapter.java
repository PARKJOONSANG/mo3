package com.ex.group.approval.easy.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;

import com.ex.group.folder.R;
import com.ex.group.approval.easy.domain.AttachFile;
import com.ex.group.approval.easy.domain.SancLine;
import com.skt.pe.common.data.SKTUtil;

import java.util.List;

/**
 * 결재라인 보기 Adapter
 * @author pluto248
 *
 */
public class AttachFileViewAdapter extends ArrayAdapter<AttachFile> {
	private final String TAG = "AttachFileViewAdapter";
	private Context mContext = null;
	
	public AttachFileViewAdapter(Context context, int textViewResourceId) {
		super(context, textViewResourceId);
		mContext = context;
	}
	
	public AttachFileViewAdapter(Context context, int textViewResourceId, List<AttachFile> objects) {
		super(context, textViewResourceId, objects);
		mContext = context;
	}
	
	public void init(Context context, int textViewResourceId, List<SancLine> objects) {
	}

	/**
	 * View UI 표현
	 * @param position
	 * @param convertView
	 * @param parent
	 */
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		AttachFile item = getItem(position);
		
		View v = convertView;
		if(v == null) {
			LayoutInflater vi = (LayoutInflater)getContext()
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			v = vi.inflate(R.layout.easy_common_listview_attachment, null);
		}
		
		fillList(item, position, v);
		
		return v;
	}
	
	private void fillList(final AttachFile item, int position, View v) {
		Button button = (Button) v.findViewById(R.id.common_listview_attachment_button_file);
		button.setText(item.getName());
		button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Log.d(TAG, "item.getIdOrUrl() ==================== " + item.getIdOrUrl());
				SKTUtil.viewSecurityImage(mContext, item.getName(), item.getIdOrUrl());
//				SKTUtil.log("TEST", "File Info: " + item.toString());
			}
		});
	}
}
