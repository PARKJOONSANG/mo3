package com.ex.group.approval.easy.activity.helper;

import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.widget.Button;

import com.ex.group.approval.easy.activity.ApprovalCommonActivity;
import com.ex.group.approval.easy.constant.ApprovalConstant;
import com.ex.group.approval.easy.primitive.ApprovePrimitive;
import com.ex.group.approval.easy.primitive.DetailPrimitive;

import java.io.Serializable;

@SuppressWarnings("serial")
public class ApprovalTypeHelper implements Serializable {
	private String button1Name;
	private String button2Name;
	private DoApproval doApproval = null;
	
	protected ApprovalTypeHelper(DoApproval doApproval) {
		this.doApproval = doApproval;
		this.button1Name = doApproval.getButtonMessage(0);
		this.button2Name = doApproval.getButtonMessage(1);
	}
	
	public String getButton1Name() {
		return button1Name;
	}
	
	public String getButton2Name() {
		return button2Name;
	}
	
	public String getDialogMessage(String approveAction) {
		return doApproval.getDialogMessage(approveAction);
	}
	
	public void initUI(ApprovalCommonActivity activity, int buttonId, DetailPrimitive dPrim) {
		doApproval.initUI(activity, buttonId, dPrim);
	}
	
	public void startButton1Action(ApprovalCommonActivity activity, DetailPrimitive dPrim, ApprovePrimitive aPrim) {
		doApproval.doButton1(activity, dPrim, aPrim);
	}
	
	public void startButton2Action(ApprovalCommonActivity activity, DetailPrimitive dPrim, ApprovePrimitive aPrim) {
		doApproval.doButton2(activity, dPrim, aPrim);
	}
	
	public static class Factory {
		public static ApprovalTypeHelper getInstance(String type) {
			ApprovalTypeHelper approvalType = null;
			
			if (ApprovalConstant.DocType.APPROVAL_LIST.equals(type) == true) {
				approvalType = new ApprovalTypeHelper(new DoApprovalList());
			} else if (ApprovalConstant.DocType.APPROVAL_ING.equals(type) == true) {
				approvalType = new ApprovalTypeHelper(new DoApprovalIng());
			} else if (ApprovalConstant.DocType.APPROVAL_DONE.equals(type) == true) {
				approvalType = new ApprovalTypeHelper(new DoApprovalDoneAndReturn());
			} else if (ApprovalConstant.DocType.APPROVAL_RETURN.equals(type) == true) {
				approvalType = new ApprovalTypeHelper(new DoApprovalDoneAndReturn());
			}
			
			return approvalType;
		}
	}
	
	/*
	 * 결재하기
	 */
	public static class DoApprovalList implements DoApproval {
		String title[] = {
			"결재 승인", "결재 반려",
			"결재가 승인되었습니다.", "결재가 반려되었습니다."
		};
		/*
		 * 결재 승인
		 */
		@Override
		public void doButton1(final ApprovalCommonActivity activity, DetailPrimitive dPrim, final ApprovePrimitive aPrim) {
			activity.alertYesNo("결재 승인 확인", "결재를 승인합니다.",  new OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					aPrim.setAction(ApprovePrimitive.ApproveAction.APPROVE);
					activity.executePrimitive(aPrim);
				}
			}, null);
		}
		/*
		 * 결재 반려
		 */
		@Override
		public void doButton2(final ApprovalCommonActivity activity, DetailPrimitive dPrim, final ApprovePrimitive aPrim) {
			activity.alertYesNo("결재 반려 확인", "결재를 반려합니다.",  new OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					aPrim.setAction(ApprovePrimitive.ApproveAction.REJECT);
					activity.executePrimitive(aPrim);
				}
			}, null);
		}
		@Override
		public void initUI(ApprovalCommonActivity activity, int buttonId, DetailPrimitive dPrim) {
		}
		@Override
		public String getButtonMessage(int index) {
			return title[index];
		}
		@Override
		public String getDialogMessage(String approvalAction) {
			if (ApprovePrimitive.ApproveAction.APPROVE.equals(approvalAction)) 
				return title[2];
			if (ApprovePrimitive.ApproveAction.REJECT.equals(approvalAction)) 
				return title[3];
			return null;
		}
	}
	
	/*
	 * 결재상황보기
	 */
	public static class DoApprovalIng implements DoApproval {
		String title[] = {
				"상황 보기", "결재 회수",
				"", "결재가 회수 되었습니다"
			};
		/*
		 * 상황 보기
		 */
		@Override
		public void doButton1(ApprovalCommonActivity activity, DetailPrimitive dPrim, ApprovePrimitive aPrim) {
			activity.viewApprovalLine(dPrim.getSancLineList());
		}
		/*
		 * 결재 회수
		 */
		@Override
		public void doButton2(ApprovalCommonActivity activity, DetailPrimitive dPrim, ApprovePrimitive aPrim) {
			aPrim.setAction(ApprovePrimitive.ApproveAction.RECALL);
			activity.executePrimitive(aPrim);
		}
		@Override
		public void initUI(ApprovalCommonActivity activity, int buttonId, DetailPrimitive dPrim) {
			Button button2 = (Button) activity.findViewById(buttonId);
//			button2.setEnabled(dPrim.isMobile());
		}
		@Override
		public String getButtonMessage(int index) {
			return title[index];
		}
		@Override
		public String getDialogMessage(String approvalAction) {
			if (ApprovePrimitive.ApproveAction.RECALL.equals(approvalAction)) 
				return title[3];
			
			return null;
		}
	}
	
	/*
	 * 결재완료함 및 반려함
	 */
	public static class DoApprovalDoneAndReturn implements DoApproval {
		String title[] = {
				"상황 보기", "목록",
				"", ""
			};
		/*
		 * 상황 보기
		 */
		@Override
		public void doButton1(ApprovalCommonActivity activity, DetailPrimitive dPrim, ApprovePrimitive aPrim) {
			activity.viewApprovalLine(dPrim.getSancLineList());
		}
		/*
		 * 목록
		 */
		@Override
		public void doButton2(ApprovalCommonActivity activity, DetailPrimitive dPrim, ApprovePrimitive aPrim) {
			activity.finish();
		}
		@Override
		public void initUI(ApprovalCommonActivity activity, int buttonId, DetailPrimitive dPrim) {
		}
		@Override
		public String getButtonMessage(int index) {
			return title[index];
		}
		@Override
		public String getDialogMessage(String approvalAction) {
			return null;
		}
	}
	
	protected interface DoApproval extends Serializable {
		public String getButtonMessage(int index);
		public String getDialogMessage(String approvalAction);
		public void initUI(ApprovalCommonActivity activity, int buttonId, DetailPrimitive dPrim);
		public void doButton1(ApprovalCommonActivity activity, DetailPrimitive dPrim, ApprovePrimitive aPrim);
		public void doButton2(ApprovalCommonActivity activity, DetailPrimitive dPrim, ApprovePrimitive aPrim);
	}
}
