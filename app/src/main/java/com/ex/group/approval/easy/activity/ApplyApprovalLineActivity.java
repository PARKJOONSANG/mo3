package com.ex.group.approval.easy.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.ex.group.folder.R;
import com.ex.group.approval.easy.adapter.ApprovalLineAdaptor;
import com.ex.group.approval.easy.constant.ApprovalConstant;
import com.ex.group.approval.easy.constant.UserInfo;
import com.ex.group.approval.easy.domain.Member;
import com.ex.group.approval.easy.domain.PrivateState;
import com.ex.group.approval.easy.primitive.DraftPrimitive;
import com.ex.group.approval.easy.primitive.PrivateLineListPrimitive;
import com.ex.group.approval.easy.util.ActivityLauncher;
import com.skt.pe.common.activity.ifaces.CommonUI;
import com.skt.pe.common.dialog.DialogButton;
import com.skt.pe.common.exception.SKTException;
import com.skt.pe.common.primitive.Primitive;
import com.skt.pe.common.primitive.util.PrimitiveList;
import com.skt.pe.util.StringUtil;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ApplyApprovalLineActivity extends ApprovalCommonActivity implements OnClickListener, CommonUI {
	private Handler handler = new Handler(new android.os.Handler.Callback() {
		public boolean handleMessage(final Message msg) {
			boolean ret = false;

			final int position = msg.arg1;
			switch (msg.what) {
				/*
				 * 구성원 검색 호출
				 */
				case ApprovalLineAdaptor.What.ADD_ITEM:
					ret = true;
					selectMemberSearch(new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							ActivityLauncher.launchMemberSearch(ApplyApprovalLineActivity.this, getSelectMemberSearchType(), ApprovalConstant.RequestCode.SELECT_LINE, ActivityLauncher.MemberSelectType.SINGLE, String.valueOf(position));
						}
					}, "approval");
					break;
				case ApprovalLineAdaptor.What.DELETE_ITEM:
					ret = true;

					memberList.remove(position);
					approvalLineAdapter.notifyDataSetChanged();
					break;
				case ApprovalLineAdaptor.What.REFRESH:
					approvalLineAdapter.notifyDataSetChanged();
					break;
			}
			return ret;
		}
	});

	private DraftPrimitive draftPrim = null;
	private PrivateLineListPrimitive privateLinePrim = null;
	private ApprovalLineAdaptor approvalLineAdapter = null;
	private List<Member> memberList = new ArrayList<Member>();

	@Override
	protected int assignLayout() {
		return R.layout.easy_apply_line;
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	@Override
    public void onCreateX(Bundle savedInstanceState) {
		Intent intent = getIntent();
		draftPrim = (DraftPrimitive) intent.getSerializableExtra(ApprovalConstant.IntentArg.PRIMITIVE);
		privateLinePrim = new PrivateLineListPrimitive();
		setSubTitle("결재경로 지정");
		initUI();
    }

	public boolean onCreateOptionsMenu(Menu menu) {
		return false;
	}

	public Handler getHandler() {
		return this.handler;
	}

	@Override
	protected void onReceive(Primitive primitive, SKTException e) {
		super.onReceive(primitive, e);
		if (e == null) {
			if (primitive instanceof DraftPrimitive) {
				alert("결재 신청이 완료되었습니다.", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						setResult(ApprovalConstant.ResponseCode.OK);
						finish();
					}
				});
			} else if (primitive instanceof PrivateLineListPrimitive) {
				// 개인결제라인 DialogBox 창을 띄운다.
				privateLinePrim = (PrivateLineListPrimitive) primitive;
				if (privateLinePrim.getLineList().size() == 0) {
					alert("저장되어 있는 결재 경로가 없습니다.");
				} else {
					selectPrivateLine(privateLinePrim.getLineList(), new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							int selectPrivateLine = getSelectPrivateLine();

							List<PrivateState> stateList = privateLinePrim.getLineList().get(selectPrivateLine).getStateList();
							PrivateState pState = null;
							Member member = null;
							boolean isNewApprovalNone = false;
							if (stateList.size() > 0) {
								approvalLineAdapter.clear();
								for (int i=0; i<stateList.size(); i++) {
									member = new Member();
									pState = stateList.get(i);
									member.setEmpId(pState.getId());
									member.setName(pState.getName());
									member.setRole(pState.getRole());
									member.setSuffixDept(pState.getDeptName());
									member.setApprovalKind(pState.getKind());
									approvalLineAdapter.add(member);
									if (pState.getKind().equals(DraftPrimitive.ApprovalKind.AUTHORIZED) || pState.getKind().equals(DraftPrimitive.ApprovalKind.SUBSTITUTED)) {
										isNewApprovalNone = true;
									}
								}
								Member blank = new Member();
								if (isNewApprovalNone == true) {
									blank.setApprovalKind(DraftPrimitive.ApprovalKind.NONE);
								}
								approvalLineAdapter.add(blank);
								approvalLineAdapter.notifyDataSetChanged();
							} else {
								alert("저장된 결재라인에 지정된 사용자가 없습니다.");
							}
						}
					});
				}
			}
		} else {
			e.alert(this, new DialogButton(0) {
				public void onClick(DialogInterface dialog, int which) {
				}
			});
		}
	}

	@Override
	protected void onActivityResultX(int requestCode, int resultCode, Intent intent) {
		/*
		 * 동행인 구성원 검색 선택 시 결과 처리...
		 */
		if (intent != null) {
			if (requestCode == ApprovalConstant.RequestCode.SELECT_LINE) {
				Member selectedMember = new Member(intent);
				int position = StringUtil.intValue(intent.getStringExtra("arg1"), -1);
				if (position != -1) {
					Member member = approvalLineAdapter.getItem(position);
					member.setMember(selectedMember);
					if (approvalLineAdapter.getCount() == 1 || position == approvalLineAdapter.getCount() - 1)
						approvalLineAdapter.add(new Member());
					approvalLineAdapter.notifyDataSetChanged();
				}
			}
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.apply_line_button_ok:
				String validationMessage = validationUI();

				if (validationMessage == null) {
					alertYesNo("결재 신청 확인", "결재를 신청합니다.",  new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							executePrimitive(draftPrim);
						}
					}, null);

				} else {
					alert(validationMessage);
				}
				break;
			case R.id.apply_line_button_cancel:
				finish();
				break;
			case R.id.apply_line_button_privateline:
				executePrimitive(privateLinePrim);
				break;
		}
	}

	/*
	 * (non-Javadoc)
	 * @see android.app.Activity#onRetainNonConfigurationInstance()
	 * 화면 로테이션 시 List 데이터가 사라지는 현상을 방지...
	 */
//	@Override
//	public Object onRetainNonConfigurationInstance() {
//		Map<String, Object> map = new HashMap<String, Object>();
//
//		map.put("approvalLineAdapter", approvalLineAdapter);
//
//		return map;
//	}

	@Override
	public void initUI() {
		ListView lineList = (ListView) findViewById(R.id.apply_line_listview_line);

//		Map<String, Object> o = (Map<String, Object>) getLastNonConfigurationInstance();
//		if (o != null) {
//			approvalLineAdapter = (ApprovalLineAdaptor) o.get("approvalLineAdapter");
//		} else {
			// 2015-06-26 Join 추가 - 결재선 지정 시 첫 번째 줄은 기안자 자신으로 설정되게 하기 위해 추가
			memberList.clear();
			UserInfo userInfo = UserInfo.getInstance();
			Intent intent = new Intent();
			intent.putExtra("name", userInfo.getUserName());
			intent.putExtra("empId", userInfo.getUserID());
			intent.putExtra("role", userInfo.getUserRole());
			intent.putExtra("suffixDept", userInfo.getUserSuffixDept());
			Member drafter = new Member(intent);
			drafter.setApprovalKind(DraftPrimitive.ApprovalKind.DRAFTER);
			Member blank = new Member();
			memberList.add(drafter);
			memberList.add(blank);
			// 2015-06-26 Join 추가 끝
			approvalLineAdapter = new ApprovalLineAdaptor(this, R.layout.easy_apply_line_listview_line, memberList);
//			approvalLineAdapter.add(new Member());
			approvalLineAdapter.notifyDataSetChanged();
//		}
		lineList.setAdapter(approvalLineAdapter);

		findViewById(R.id.apply_line_button_ok).setOnClickListener(this);
		findViewById(R.id.apply_line_button_cancel).setOnClickListener(this);
		findViewById(R.id.apply_line_button_privateline).setOnClickListener(this);

		switch (getIntent().getIntExtra(ApplyApprovalLineActivity.IntentArg.APPROVAL_TYPE, ApprovalConstant.EasyApprovalType.OUTSIDE)) {
			case ApprovalConstant.EasyApprovalType.OUTSIDE:
				showOutsideSummary();
				break;
			case ApprovalConstant.EasyApprovalType.VACATION:
				showVacationSummary();
				break;
		}
	}
	
	@Override
	public void resetUI() {
		
	}

	@Override
	public String validationUI() {
		// 2015-08-06 Join 수정 - 결재선 지정 시 첫 번째 줄은 기안자 자신으로 설정되게 하게 되어 if문의 2 -> 3 으로수정
		if (memberList.size() < 3) 
			return "결재경로는 최소 한명이상 지정이 되어야 합니다.";
		
		if (isDuplicateMember() == true) {
			return "결재경로에 중복된 인원이 있습니다.";
		}
		
		PrimitiveList approvalStep = new PrimitiveList();
		PrimitiveList approvalKind = new PrimitiveList();
		Member member;
		for (int i=0; i<memberList.size(); i++) {
			member = memberList.get(i);
			if (member.isNull() == false) {
				approvalStep.add(member.getEmpId());
				approvalKind.add(member.getApprovalKind());
			}
		}
		
		draftPrim.setApprovalStep(approvalStep.toString());
		draftPrim.setApprovalKind(approvalKind.toString());
		
		return null;
	}
	
	private boolean isDuplicateMember() {
		String empId = null;
		for (int i=0; i<memberList.size(); i++) {
			empId = memberList.get(i).getEmpId();
			for (int j=i; j<memberList.size(); j++) {
				if (j == i) continue;
				if (empId.equals(memberList.get(j).getEmpId()))
					return true;
			}
		}
		return false;
	}
	
	private void showOutsideSummary() {
		TextView summary = (TextView) findViewById(R.id.apply_line_textview_summary);

		// 타이틀 세팅
		SpannableStringBuilder sp = new SpannableStringBuilder(getIntent().getStringExtra(IntentArg.SUMMARY_TITLE));
		sp.setSpan(new ForegroundColorSpan(Color.rgb(0x16, 0xAD, 0xFE)), 0, sp.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//		sp.setSpan(new AbsoluteSizeSpan(24), 0, sp.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		summary.setText(sp);
		
		StringBuffer sb = new StringBuffer();
		
		sb.append("(");
		sb.append((String) draftPrim.getTag(DraftPrimitive.Tags.FORM_CODE_NAME));
		sb.append(", ");
		sb.append(SummaryTargetDateFormat(draftPrim.getTargetDate()));
		sb.append(" ");
		sb.append(DraftPrimitive.formatTime(draftPrim.getFromTime()));
		sb.append("~");
		sb.append(DraftPrimitive.formatTime(draftPrim.getUntilTime()));
		sb.append(")");

		summary.append(sb.toString());
		
		TextView currentDate = (TextView) findViewById(R.id.apply_line_textview_currentdate);
		currentDate.setText(SummaryCurrentDateFormat(new Date(System.currentTimeMillis())));
	}
	
	private void showVacationSummary() {
		TextView summary = (TextView) findViewById(R.id.apply_line_textview_summary);

		// 타이틀 세팅
		SpannableStringBuilder sp = new SpannableStringBuilder(getIntent().getStringExtra(IntentArg.SUMMARY_TITLE));
		sp.setSpan(new ForegroundColorSpan(Color.rgb(0x16, 0xAD, 0xFE)), 0, sp.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//		sp.setSpan(new AbsoluteSizeSpan(24), 0, sp.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		summary.setText(sp);
		
		StringBuffer sb = new StringBuffer();
		
		sb.append("\n");
		sb.append((String) draftPrim.getTag(DraftPrimitive.Tags.FORM_CODE_NAME));
		sb.append(" ");
		sb.append(SummaryTargetDateFormat(draftPrim.getFromDate()));
		sb.append("~");
		sb.append(SummaryTargetDateFormat(draftPrim.getUntilDate()));
		// 2015-03-05 Join 추가 - 휴가일수에 대한 상세정보 제공을 위해 추가
//		sb.append("(*총 " + draftPrim.getTotalVacation() + "일 중 " + draftPrim.getExceptCount() + "일 제외))");
		int realVactionTerm = 0;		// 실제 휴가일수 = 총 휴가일수 - 제외일수
		realVactionTerm = Integer.parseInt(draftPrim.getTotalVacation()) - Integer.parseInt(draftPrim.getExceptCount());
		if("0".equals(draftPrim.getExceptCount())) {
			sb.append("(총 " + realVactionTerm + "일)");
		} else {
			sb.append("(총 " + realVactionTerm + "일 - " + draftPrim.getTotalVacation() + "일 중 " + draftPrim.getExceptCount() + "일 제외)");
		}
		
		summary.append(sb.toString());

//		
////		TextView targetDate = (TextView) findViewById(R.id.apply_line_textview_targetdate);
////		targetDate.setText(SummaryTargetDateFormat(DraftPrimitive.stringToDate(draftPrim.getTargetDate())));
//		
//		TextView fromTime = (TextView) findViewById(R.id.apply_line_textview_fromtime);
//		fromTime.setText(SummaryTargetDateFormat(draftPrim.getFromDate()));
//		
//		TextView untilTime = (TextView) findViewById(R.id.apply_line_textview_untiltime);
//		untilTime.setText(SummaryTargetDateFormat(draftPrim.getUntilDate()));
//		
		TextView currentDate = (TextView) findViewById(R.id.apply_line_textview_currentdate);
		currentDate.setText(SummaryCurrentDateFormat(new Date(System.currentTimeMillis())));
	}
	
	private String SummaryTargetDateFormat(Date inputDate) {
		SimpleDateFormat format = new SimpleDateFormat("MM월 dd일");
		return format.format(inputDate).toString();
	}
	
	private String SummaryCurrentDateFormat(Date inputDate) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy.MM.dd");
		return format.format(inputDate).toString();
	}
	
	public static class IntentArg {
		public final static String APPROVAL_TYPE = "APPROVAL_TYPE";
		public final static String SUMMARY_TITLE = "SUMMARY_TITLE";
	}
}