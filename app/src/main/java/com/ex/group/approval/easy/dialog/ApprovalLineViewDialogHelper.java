package com.ex.group.approval.easy.dialog;

import android.widget.ListView;

import com.ex.group.folder.R;
import com.ex.group.approval.easy.adapter.ApprovalLineViewAdapter;
import com.ex.group.approval.easy.domain.SancLine;

import java.util.List;

public class ApprovalLineViewDialogHelper {
	private ApprovalLineViewAdapter adaptor = null;
	
	public ApprovalLineViewDialogHelper(ListView listView, List<SancLine> bossList) {
		adaptor = new ApprovalLineViewAdapter(listView.getContext(), R.layout.easy_apply_line_listview_signed, bossList);
		listView.setAdapter(adaptor);
	}
}
