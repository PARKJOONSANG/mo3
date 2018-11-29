package com.ex.group.approval.easy.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ex.group.folder.R;
import com.ex.group.approval.easy.activity.ApplyApprovalLineActivity;
import com.ex.group.approval.easy.domain.Member;
import com.ex.group.approval.easy.primitive.DraftPrimitive;
import com.ex.group.approval.easy.widget.CodeSpinner;

import java.util.List;

public class ApprovalLineAdaptor extends ArrayAdapter<Member> {
	private int resource;
	private ApplyApprovalLineActivity activity = null;
	
	public ApprovalLineAdaptor(Context context, int textViewResourceId, List<Member> objects) {
		super(context, textViewResourceId, objects);
		this.activity = (ApplyApprovalLineActivity) context;
		this.resource = textViewResourceId;
	}
	
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		LinearLayout lineView = null;
		
		if (convertView == null) {
			lineView = new LinearLayout(getContext());
			String inflater = Context.LAYOUT_INFLATER_SERVICE;
			LayoutInflater vi = (LayoutInflater) getContext().getSystemService(inflater);
			vi.inflate(this.resource, lineView, true);
		} else {
			lineView = (LinearLayout) convertView;
		}

		fillList(position, lineView);
		
		// 2015-08-06 Join 수정 - 결재선 지정 시 첫 번째 줄은 기안자 자신으로 설정되게 하게 되어 수정
		Button buttonMemberSearch = (Button) lineView.findViewById(R.id.apply_line_button_membersearch);
		Button buttonMemberDelete = (Button) lineView.findViewById(R.id.apply_line_button_delete);
		
		if(position == 0) {
			buttonMemberSearch.setEnabled(false);
			buttonMemberSearch.setVisibility(View.INVISIBLE);
			buttonMemberDelete.setEnabled(false);
			buttonMemberDelete.setVisibility(View.INVISIBLE);
		} else {
			buttonMemberSearch.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					sendMessage(What.ADD_ITEM, position);
				}
			});
			
			buttonMemberDelete.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					if (getItem(position).isNull() == false) {
						if (DraftPrimitive.ApprovalKind.AUTHORIZED.equals(getItem(position).getApprovalKind()) || DraftPrimitive.ApprovalKind.SUBSTITUTED.equals(getItem(position).getApprovalKind())) {
							setFillKindNormal(position);
						}
						sendMessage(What.DELETE_ITEM, position);
					}
				}
			});
		}
		// 2015-08-06 Join 수정
		
		final CodeSpinner approvalKind = (CodeSpinner) lineView.findViewById(R.id.apply_line_spinner_approval_kind);
		approvalKind.setOnClickListener(new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				String strApprovalKind = approvalKind.getValue();
				if (DraftPrimitive.ApprovalKind.AUTHORIZED.equals(getItem(position).getApprovalKind()) || DraftPrimitive.ApprovalKind.SUBSTITUTED.equals(getItem(position).getApprovalKind())) {
					if (DraftPrimitive.ApprovalKind.NORMAL.equals(strApprovalKind)) {
						setFillKindNormal(position);
					}
				}
				
				if (DraftPrimitive.ApprovalKind.AUTHORIZED.equals(strApprovalKind) || DraftPrimitive.ApprovalKind.SUBSTITUTED.equals(strApprovalKind)) {
					setFillKindNone(position);
				}
				getItem(position).setApprovalKind(strApprovalKind);
				
			}
		});
		
		if (position == getCount()-1 && DraftPrimitive.ApprovalKind.NONE.equals(approvalKind.getValue())) {
			buttonMemberSearch.setEnabled(false);
		} else {
			buttonMemberSearch.setEnabled(true);
		}
		
		return lineView;
	}
	
	private void fillList(int position, View v) {
		Member item = getItem(position);
		
		TextView no = (TextView) v.findViewById(R.id.apply_line_textview_no);
		no.setText((position+1) + ".");
		
		TextView name = (TextView) v.findViewById(R.id.apply_line_textview_name);
		name.setText(item.getName());
		
		TextView grade = (TextView) v.findViewById(R.id.apply_line_textview_grade);
		if (item.getRole() != null)
			grade.setText("(" + item.getRole() + ")");
		else
			grade.setText("");
		
		TextView dept = (TextView) v.findViewById(R.id.apply_line_textview_dept);
		dept.setText(item.getSuffixDept());
		
		setApprovalKind(position, v);
	}
	
	private void setApprovalKind(int position, View v) {
		CodeSpinner approvalKind = (CodeSpinner) v.findViewById(R.id.apply_line_spinner_approval_kind);
		Member item = getItem(position);
		String[] values = null;
		String[] codes = null;

		int totalCount = this.getCount() - 1;
		int realPosition = position + 1;
		/* 
		 * 규칙
		 * 0. 현재 item이 결재안함이면 결재안함 항목으로 바꾼다.
		 * 1. 첫 번째 항목은 '기안'만 들어간다 -- 2015-08-06 Join 수정
		 * 2. 새로 추가되는 항목과 마지막 항목에는 '결재' 만 들어간다.
		 * 3. 마지막 라인 바로 전의 항목은 '결재/전결/대결' 이 다 들어간다. 단 2명일때는 제외이다.
		 * 4. 나머지 항목은 '결재/전결' 만 들어간다.
		 */
		
		if (DraftPrimitive.ApprovalKind.NONE.equals(item.getApprovalKind())) {
			values = v.getResources().getStringArray(R.array.easyaproval_apply_line_spinner_approval_kind_name_none);
			codes = v.getResources().getStringArray(R.array.easyaproval_apply_line_spinner_approval_kind_code_none);
		} else {
			if (position == 0) {
				values = v.getResources().getStringArray(R.array.easyaproval_apply_line_spinner_approval_kind_name_type0);
				codes = v.getResources().getStringArray(R.array.easyaproval_apply_line_spinner_approval_kind_code_type0);
			} else if (totalCount == position) {
				values = v.getResources().getStringArray(R.array.easyaproval_apply_line_spinner_approval_kind_name_type1);
				codes = v.getResources().getStringArray(R.array.easyaproval_apply_line_spinner_approval_kind_code_type1);
			} else if (totalCount == realPosition) {
				values = v.getResources().getStringArray(R.array.easyaproval_apply_line_spinner_approval_kind_name_type1);
				codes = v.getResources().getStringArray(R.array.easyaproval_apply_line_spinner_approval_kind_code_type1);
			} else if (totalCount > 2 && realPosition == totalCount - 1) {
				values = v.getResources().getStringArray(R.array.easyaproval_apply_line_spinner_approval_kind_name_type3);
				codes = v.getResources().getStringArray(R.array.easyaproval_apply_line_spinner_approval_kind_code_type3);
			} else {
				values = v.getResources().getStringArray(R.array.easyaproval_apply_line_spinner_approval_kind_name_type2);
				codes = v.getResources().getStringArray(R.array.easyaproval_apply_line_spinner_approval_kind_code_type2);
			}
		}
		approvalKind.setValues(values, codes);
		
		if (item.getApprovalKind() == null) {
			item.setApprovalKind(approvalKind.getValue());
		} else {
			approvalKind.setCode(item.getApprovalKind());
		}
	}
	
//	private void setFillKindOthers(int position, String approvalKind) {
//		for (int i=position+1; i<getCount(); i++) {
//			getItem(i).setApprovalKind(approvalKind);
//		}
//		sendMessage(What.REFRESH);
//	}
	
	private void setFillKindNone(int position) {
		for (int i=position+1; i<getCount(); i++) {
			getItem(i).setApprovalKind(DraftPrimitive.ApprovalKind.NONE);
		}
		sendMessage(What.REFRESH);
	}
	
	private void setFillKindNormal(int position) {
		for (int i=position+1; i<getCount(); i++) {
			getItem(i).setApprovalKind(DraftPrimitive.ApprovalKind.NORMAL);
		}
		sendMessage(What.REFRESH);
	}
	
	private void sendMessage(int what) {
		Message msg = Message.obtain();
		msg.what = what;
		activity.getHandler().sendMessage(msg);
	}
	
	private void sendMessage(int what, int arg1) {
		Message msg = Message.obtain();
		msg.what = what;
		msg.arg1 = arg1;
		activity.getHandler().sendMessage(msg);
	}
	
	public static class What {
		final public static int ADD_ITEM = 0;
		final public static int DELETE_ITEM = 1;
		final public static int REFRESH = 2;
	}
}
