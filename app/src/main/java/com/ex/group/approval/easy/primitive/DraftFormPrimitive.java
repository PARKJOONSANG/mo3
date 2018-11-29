package com.ex.group.approval.easy.primitive;

import com.ex.group.approval.easy.constant.ApprovalConstant;
import com.ex.group.approval.easy.constant.UserInfo;
import com.ex.group.approval.easy.domain.FormInfo;
import com.skt.pe.common.activity.SKTActivity.Action;
import com.skt.pe.common.exception.SKTException;
import com.skt.pe.common.primitive.Primitive;
import com.skt.pe.common.service.XMLData;


@SuppressWarnings("serial")
public class DraftFormPrimitive extends Primitive {
	// 외출신청 시 입력폼 정보 조회
	public final static String name = "COMMON_APPROVALSTAGING_DRAFTFORM";
//	public final static String name = "COMMON_APPROVAL_DRAFTFORM";
	
	private final static String[] paramNames = {
		"systemType", "userID"
	};

	private FormInfo formInfo = new FormInfo();
	
	public DraftFormPrimitive() {
		super(DraftFormPrimitive.name, paramNames, Action.SERVICE_RETRIEVING);
		this.init();
	}
	
	private void init() {
		this.setSystemType(ApprovalConstant.SystemType.EASY);
		UserInfo userInfo = UserInfo.getInstance();
		this.setUserID(userInfo.getUserID());
	}
	
	public void setSystemType(String systemType) {
		super.addParameter(paramNames[0], systemType);
	}
	
	public void setUserID(String userID) {
		super.addParameter(paramNames[1], userID);
	}
	
	public FormInfo getFormInfo() {
		return this.formInfo;
	}
	
	@Override
	public void convertXML(XMLData xmldata) throws SKTException {
		super.convertXML(xmldata);
		formInfo.setXml(xmldata, "formInfo");
	}

	@Override
	protected String toPrimitiveString() {
		StringBuffer sb = new StringBuffer();
		
		sb.append("formInfo=");
		sb.append(this.formInfo.toString());

		return sb.toString();
	}
}
