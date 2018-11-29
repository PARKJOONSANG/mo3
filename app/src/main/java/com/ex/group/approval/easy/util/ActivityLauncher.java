package com.ex.group.approval.easy.util;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

import com.ex.group.folder.R;
import com.ex.group.approval.easy.activity.ApplyApprovalLineActivity;
import com.ex.group.approval.easy.activity.ApprovalDetailActivity;
import com.ex.group.approval.easy.addressbook.activity.AddressTabActivity;
import com.ex.group.approval.easy.constant.ApprovalConstant;
import com.ex.group.approval.easy.primitive.DraftPrimitive;
import com.sk.pe.group.imageviewer.activity.ImageViewerMainActivity;
import com.skt.pe.common.conf.Constants;
import com.skt.pe.common.data.SKTUtil;
import com.skt.pe.common.dialog.DialogButton;
import com.skt.pe.common.dialog.SKTDialog;
import com.skt.pe.common.service.Parameters;
import com.skt.pe.util.StringUtil;

public class ActivityLauncher {
	public static void launchApprovalLine(Activity activity, DraftPrimitive draftPrim, int approvalType, String title) {
		Intent intent = new Intent(activity, ApplyApprovalLineActivity.class);
		intent.putExtra(ApprovalConstant.IntentArg.PRIMITIVE, draftPrim);
		intent.putExtra(ApplyApprovalLineActivity.IntentArg.APPROVAL_TYPE, approvalType);
		intent.putExtra(ApplyApprovalLineActivity.IntentArg.SUMMARY_TITLE, title);
		activity.startActivityForResult(intent, ApprovalConstant.RequestCode.FINISH);
	}
	
	public static void launchApprovalDetail(Context context) {
		context.startActivity(new Intent(context, ApprovalDetailActivity.class));
	}
	
	public static void launchMemberSearch(Activity activity, int which, int requestCode) {
		launchMemberSearch(activity, which, requestCode, MemberSelectType.SINGLE, null);
	}
	
	public static void launchMemberSearch(Activity activity, int which, int type, int requestCode) {
		launchMemberSearch(activity, which, requestCode, type, null);
	}
	
	public static void launchMemberSearch(final Activity activity, int which, int requestCode, int type, String arg1) {
		String MEMBERSEARCH = Constants.Action.MEMBER_SEARCH;
		String MEMBERSEARCH_MAIN = Constants.Action.MEMBER_SEARCH_MAIN;
		
		try {
			Intent approvalIntent = new Intent(activity, AddressTabActivity.class);
			
			switch (type) {
				case MemberSelectType.SINGLE:
					approvalIntent.putExtra("isSingleSelect",true);
					break;
				case MemberSelectType.MULTI:
					approvalIntent.putExtra("isMultiSelect",true);
					break;
			}
			approvalIntent.putExtra("tab", which);
			if (arg1 != null) {
				approvalIntent.putExtra("arg1", arg1);
			}
			
			activity.startActivityForResult(approvalIntent, requestCode);
		} catch (Exception e) {
			SKTDialog dlg = new SKTDialog(activity, SKTDialog.DLG_TYPE_2);
			String msg = StringUtil.format(activity.getResources().getString(R.string.easyaproval_MemberSearch), Constants.CoreComponent.APP_NM_MEMBER);
			dlg.getDialog(activity.getResources().getString(R.string.easyaproval_dialog_ok), msg, new DialogButton(0) {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					try {
						SKTUtil.goMobileOffice(activity, Constants.CoreComponent.APP_ID_MEMBER);
					} catch (Exception e) {
					}
				}
			}, new DialogButton(0) {
				@Override
				public void onClick(DialogInterface dialog, int which) {
				}
			}).show();
		}
	}
	
	public static Intent makeImageViewIntent(Context context, String fileName, String docId, float zoom) {
		final String PARAM_TYPE     = "type";
		final String PARAM_FILENAME = "fileName";
		final String PARAM_PARAMS   = "params";
		final String PARAM_DOCID    = "docId";
		final String PARAM_ZOOM     = "zoom";

		Parameters params = new Parameters(Constants.PRIMITIVE_ATTACH_VIEW);
		params.put(PARAM_DOCID, docId);

		Intent intent = new Intent(context, ImageViewerMainActivity.class);
		
		intent.putExtra(PARAM_TYPE    , 1);
		intent.putExtra(PARAM_FILENAME, fileName);
		intent.putExtra(PARAM_PARAMS  , params);
		if(zoom != -1)
			intent.putExtra(PARAM_ZOOM, zoom);
		
		return intent;
	}
	
	public static class MemberSelectType {
		public final static int SINGLE = 1;
		public final static int MULTI = 2;
	}
}
