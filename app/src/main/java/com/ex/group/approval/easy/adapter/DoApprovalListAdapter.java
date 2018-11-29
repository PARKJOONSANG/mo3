package com.ex.group.approval.easy.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.ex.group.folder.R;
import com.ex.group.approval.easy.activity.ApprovalCommonActivity;
import com.ex.group.approval.easy.domain.Doc;
import com.ex.group.approval.easy.util.DateUtils;

import java.util.List;

/**
 * 결재하기 목록 Adapter
 * @author pluto248
 *
 */
public class DoApprovalListAdapter extends ArrayAdapter<Doc> {
	private ApprovalCommonActivity activity;
	private int resId;
	
	public DoApprovalListAdapter(Context context, int textViewResourceId) {
		super(context, textViewResourceId);
		this.activity = (ApprovalCommonActivity) context;
		this.resId = textViewResourceId;
	}
	
	public DoApprovalListAdapter(Context context, int textViewResourceId, List<Doc> objects) {
		super(context, textViewResourceId, objects);
	}
	
	public void init(Context context, int textViewResourceId, List<Doc> objects) {
		this.activity = (ApprovalCommonActivity) context;
		this.resId = textViewResourceId;
	}

	/**
	 * View UI 표현
	 * @param position
	 * @param convertView
	 * @param parent
	 */
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		View v = convertView;
		
		if(v == null) {
			LayoutInflater vi = (LayoutInflater)getContext()
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			v = vi.inflate(R.layout.easy_common_listview_approval, null);
		}
		
		fillList(position, v);
		
		return v;
	}
	
	private void fillList(int position, View v) {
		Doc item = getItem(position);
//		ImageView imageView = (ImageView)v.findViewById(R.id.common_approval_listview_imageview_attachment);
		TextView title = (TextView)v.findViewById(R.id.common_approval_listview_textview_title);
		TextView writer = (TextView)v.findViewById(R.id.common_approval_listview_textview_writer);
		TextView deptNm = (TextView)v.findViewById(R.id.common_approval_listview_textview_dept_nm);
		TextView signDate = (TextView)v.findViewById(R.id.common_approval_listview_textview_sign_date);
		
		title.setText(item.getTitle());
		// 2014-07-18 Join 수정 : 간이결재 고도화로 인한 수정
		writer.setText(item.getCreatorPersonInfo());
		deptNm.setText(item.getCreatorDeptInfo());
		signDate.setText(DateUtils.datePatternChange(item.getArrivedTime(), "yyyy.MM.dd HH:mm:ss"));
	}
}
