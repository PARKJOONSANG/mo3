package com.ex.group.approval.easy.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.ex.group.folder.R;
import com.ex.group.approval.easy.activity.helper.ApprovalTypeHelper;
import com.ex.group.approval.easy.adapter.DoApprovalListAdapter;
import com.ex.group.approval.easy.constant.ApprovalConstant;
import com.ex.group.approval.easy.domain.Doc;
import com.ex.group.approval.easy.primitive.DetailPrimitive;
import com.ex.group.approval.easy.primitive.ListPrimitive;
import com.skt.pe.common.activity.helper.PageMoreHelper;
import com.skt.pe.common.activity.ifaces.CommonUI;
import com.skt.pe.common.dialog.DialogButton;
import com.skt.pe.common.exception.SKTException;
import com.skt.pe.common.primitive.Primitive;

import java.util.List;

public class ApprovalListActivity extends ApprovalCommonActivity implements AdapterView.OnItemClickListener, CommonUI {
	private final String TAG = "ApprovalListActivity";
	private DoApprovalListAdapter doApprovalListAdapter = null;
	private ListPrimitive lPrim = null;
	private PageMoreHelper pageMoreHelper = null;
	
	@Override
	protected int assignLayout() {
		return R.layout.easy_approval_list;
	}
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}
	
	@Override
    public void onCreateX(Bundle savedInstanceState) {
		Intent intent = getIntent();
		
		String subTitle = intent.getStringExtra(IntentArg.TITLE_NAME);
		setSubTitle(subTitle);
		
		this.lPrim = (ListPrimitive) intent.getSerializableExtra(IntentArg.PRIMITIVE);
		
		initUI();
    }
	
	@Override
	protected void onRefresh() {
		resetUI();
		doApprovalListAdapter.clear();
		lPrim.setPage(1);
		executePrimitive(lPrim);
	}
	
	@Override
	protected void onReceive(Primitive primitive, SKTException e) {
		super.onReceive(primitive, e);
		if (e == null) {
			if (primitive instanceof ListPrimitive) {
				ListPrimitive lPrim = (ListPrimitive) primitive;
				selectListViewOrNone();
				addList(lPrim.getDocList(), true);
			} else if (primitive instanceof DetailPrimitive) {
				DetailPrimitive dPrim = (DetailPrimitive) primitive;
				
				Intent intent = new Intent(ApprovalListActivity.this, ApprovalDetailActivity.class);
				intent.putExtra(ApprovalDetailActivity.IntentArg.PRIMITIVE, dPrim);
				intent.putExtra(ApprovalDetailActivity.IntentArg.APPROVAL_TYPE, (ApprovalTypeHelper)lPrim.getTag(ListPrimitive.Extras.APPROVAL_TYPE));
				
				startActivityForResult(intent, ApprovalConstant.RequestCode.REFRESH);
			}
		} else {
			e.alert(this, new DialogButton(0) {
				public void onClick(DialogInterface dialog, int which) {
				}
			});
		}
	}

	@Override
	public void onItemClick(AdapterView<?> adapter, View view, int position, long id) {
		/*
		 * 더보기 버튼 클릭 시
		 */
		if (pageMoreHelper.isMorePageView(view) == true) {
			lPrim.setPage(pageMoreHelper.nextPage());
			executePrimitive(lPrim);
		} else {
			Doc doc = (Doc) adapter.getItemAtPosition(position);
			// 2014-07-18 Join 수정 : 간이결재 고도화로 인한 주석처리 -> 2015-03-23 Join 수정 - 주석 해제
			if (doc.isSecret() == true) {
				DetailPrimitive dp = new DetailPrimitive();
				dp.setDocID(doc.getDocID());
				
				executePrimitive(dp);
			}
			else {
//				super.alert("국정원 보안지침에 의거 현재 문서는 PC에서만 보실 수 있습니다.");
				super.alert("선택한 문서는 PC에서만 보실 수 있습니다.");
			}
			
			// 2014-09-29 Join 수정 : 간이결재 고도화로 인한 재수정
			/*if("보안승인".equals(doc.getTitle())) {
				super.alert("국정원 보안지침에 의거 현재 문서는 PC에서만 보실 수 있습니다.");
			} else {
				if (doc.isSecret() == true) {
					DetailPrimitive dp = new DetailPrimitive();
					dp.setDocID(doc.getDocID());
					
					executePrimitive(dp);
				}
				else {
					super.alert("국정원 보안지침에 의거 현재 문서는 PC에서만 보실 수 있습니다.");
				}
			}*/
			// 2014-09-29 Join 수정 : 간이결재 고도화로 인한 재수정
			
			// 2014-10-29 Join 수정 : 문서제목에 출장, 외출, 근태, 휴가 문구가 있을 경우만 보여주는 방법으로 재수정
			// 2015-03-23 Join 수정 시작 - 감독관 요청 : 문서제목에 외출, 근태, 휴가 문구가 있을 경우만 리스트에 표출했기 때문에 주석처리
			/*String title = doc.getTitle();
			if(title != null) {
				if(title.indexOf("외출") > -1 || title.indexOf("근태") > -1 || title.indexOf("휴가") > -1) {
					if (doc.isSecret() == true) {
						DetailPrimitive dp = new DetailPrimitive();
						dp.setDocID(doc.getDocID());
						
						executePrimitive(dp);
					}
					else {
						super.alert("국정원 보안지침에 의거 현재 문서는 PC에서만 보실 수 있습니다.");
					}
				}
				else if(title.indexOf("출장") > -1) {
					super.alert("서비스 연계정보 변경 중입니다.");
				}
				else {
					super.alert("국정원 보안지침에 의거 현재 문서는 PC에서만 보실 수 있습니다.");
				}
			}*/
			// 2015-03-23 Join 수정 끝
			// 2014-10-29 Join 수정 : 문서제목에 외출, 근태, 휴가 문구가 있을 경우만 보여주는 방법으로 재수정
			
			
//			ActivityLauncher.launchApprovalDetail(DoApprovalActivity.this);
		}
	}
	
	private void selectListViewOrNone() {
		if (lPrim.getDocList().size() == 0) {
			ListView approvalList = (ListView) findViewById(R.id.do_approval_listview_approval);
			approvalList.setVisibility(View.GONE);
			TextView tv = (TextView) findViewById(R.id.do_approval_textview_nodata);
			tv.setVisibility(View.VISIBLE);
		}
	}
	
	private void addList(List<Doc> docList, boolean refresh) {
		// 2014-07-18 Join 수정 : 간이결재 고도화로 인한 수정
		Log.d(TAG, "lPrim.getPage() ================= " + lPrim.getPage());
		Log.d(TAG, "lPrim.getEnd() ================= " + lPrim.getEnd());
		pageMoreHelper.setPageInfo(lPrim.getPage(), lPrim.getEnd());
		
		// 2015-03-23 Join 수정 시작 - 감독관 요청 : 문서제목에 외출, 근태, 휴가 문구가 있을 경우만 리스트에 표출
		String title = "";
		for (int i=0; i<docList.size(); i++) {
			title = docList.get(i).getTitle();
			if(title != null) {
//				if(title.indexOf("외출") > -1 || title.indexOf("근태") > -1 || title.indexOf("휴가") > -1) {
				if(title.indexOf("외출") > -1 || title.indexOf("근태") > -1) {
					this.doApprovalListAdapter.add(docList.get(i));
				}
				else {
					continue;
				}
			}
		}
		// 2015-03-23 Join 수정 끝
		
		pageMoreHelper.putMoreButton();
		
		if (refresh == true)
			this.doApprovalListAdapter.notifyDataSetChanged();
	}

	@Override
	public void initUI() {
		ListView approvalList = (ListView) findViewById(R.id.do_approval_listview_approval);
		
		selectListViewOrNone();
		
		doApprovalListAdapter = new DoApprovalListAdapter(this, R.layout.easy_common_listview_approval);
		// 2014-07-18 Join 수정 : 간이결재 고도화로 인한 수정
		pageMoreHelper = new PageMoreHelper(ApprovalListActivity.this, approvalList, R.layout.easy_common_more, R.id.common_more_button, lPrim.getPage(), lPrim.getEnd());
		
		addList(lPrim.getDocList(), false);
		approvalList.setAdapter(doApprovalListAdapter);
		approvalList.setOnItemClickListener(this);
	}

	@Override
	public void resetUI() {
		ListView approvalList = (ListView) findViewById(R.id.do_approval_listview_approval);
		approvalList.setVisibility(View.VISIBLE);
		TextView tv = (TextView) findViewById(R.id.do_approval_textview_nodata);
		tv.setVisibility(View.GONE);
		pageMoreHelper.setMoreButton(false);
	}

	@Override
	public String validationUI() {
		return null;
	}
	
	public static class IntentArg {
		final static String TITLE_NAME = "INTENT_TITLE_NAME";
		final static String SUB_TITLE_NAME = "INTENT_SUB_TITLE_NAME";
		final static String DOC_TYPE = "DOC_TYPE";
		final static String PRIMITIVE = "INTENT_PRIMITIVE";
	}
}