package com.ex.group.approval.easy.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.ex.group.folder.R;
import com.ex.group.approval.easy.domain.SancLine;
import com.ex.group.approval.easy.util.DateUtils;

import java.util.List;

/**
 * 결재라인 보기 Adapter
 * @author pluto248
 *
 */
public class ApprovalLineViewAdapter extends ArrayAdapter<SancLine> {
	private final String TAG = "ApprovalLineViewAdapter";
	
	public ApprovalLineViewAdapter(Context context, int textViewResourceId) {
		super(context, textViewResourceId);
	}
	
	public ApprovalLineViewAdapter(Context context, int textViewResourceId, List<SancLine> objects) {
		super(context, textViewResourceId, objects);
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
		SancLine item = getItem(position);
		
		View v = convertView;
		if(v == null) {
			LayoutInflater vi = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			if ("기안".equals(item.getSancStatus()) || "승인".equals(item.getSancStatus())) {
				v = vi.inflate(R.layout.easy_apply_line_listview_signed, null);
			}
			else {
				v = vi.inflate(R.layout.easy_apply_line_listview_unsigned, null);
			}
		}
		
		Log.d(TAG, position + "번째 SancStatus ============= " + item.getSancStatus());
		fillList(item, position, v);
		
		return v;
	}
	
	private void fillList(SancLine item, int position, View v) {
		TextView no = (TextView)v.findViewById(R.id.apply_line_textview_no);
		TextView bossPosition = (TextView)v.findViewById(R.id.apply_line_textview_boss_position);
		TextView bossName = (TextView)v.findViewById(R.id.apply_line_textview_boss_name);
		TextView deptNm = (TextView)v.findViewById(R.id.apply_line_textview_dept_nm);
		TextView taskName = (TextView)v.findViewById(R.id.apply_line_textview_task_name);
		
		// 아래 두 항목은 null일 수도 있으니 null이 아닐때만 데이터를 채워 넣는다.
//		TextView signYn = (TextView)v.findViewById(R.id.apply_line_textview_sign_yn);
		TextView dateCompleted = (TextView)v.findViewById(R.id.apply_line_textview_date_completed);
		///////
		
		no.setText(String.valueOf(position+1) + ".");
		bossPosition.setText("(" + item.getBossPosition() + ")");
		bossName.setText(item.getBossName());
		deptNm.setText(item.getDeptNm());
//		taskName.setText(item.getTaskName());
		taskName.setText("간이결재");
		
		if (dateCompleted != null) {
			dateCompleted.setText(DateUtils.datePatternChange(item.getProcessedTime(), "yyyy.MM.dd HH:mm:ss"));
		}
	}
}
