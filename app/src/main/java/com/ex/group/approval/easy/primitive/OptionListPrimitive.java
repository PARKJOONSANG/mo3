package com.ex.group.approval.easy.primitive;

import com.ex.group.approval.easy.constant.ApprovalConstant;
import com.ex.group.approval.easy.constant.UserInfo;
import com.ex.group.approval.easy.domain.OpinionInfo;
import com.skt.pe.common.activity.SKTActivity.Action;
import com.skt.pe.common.exception.SKTException;
import com.skt.pe.common.primitive.Primitive;
import com.skt.pe.common.service.XMLData;

import java.util.ArrayList;
import java.util.List;


@SuppressWarnings("serial")
public class OptionListPrimitive extends Primitive {
	// 결재 의견리스트 조회
	public final static String name = "COMMON_APPROVALSTAGING_OPINIONLIST";
	
	private final static String[] paramNames = {
		"wdid", "systemType", "docHref", "userID"
	};
	
	private List<OpinionInfo> opinionList = new ArrayList<OpinionInfo>();
	
	public OptionListPrimitive() {
		super(OptionListPrimitive.name, paramNames, Action.SERVICE_RETRIEVING);
		this.init();
	}
	
	private void init() {
		this.setSystemType(ApprovalConstant.SystemType.EASY);
		UserInfo userInfo = UserInfo.getInstance();
		this.setUserID(userInfo.getUserID());
	}
	
	public void setWdid(String wdid) {
		super.addParameter(paramNames[0], wdid);
	}
	
	public void setSystemType(String systemType) {
		super.addParameter(paramNames[1], systemType);
	}
	
	public void setDocHref(String docHref) {
		super.addParameter(paramNames[2], docHref);
	}
	
	public void setUserID(String userID) {
		super.addParameter(paramNames[3], userID);
	}
	
	@Override
	public void convertXML(XMLData xmldata) throws SKTException {
		super.convertXML(xmldata);
		
		int listCnt = parseInt(xmldata.get("listCnt"));
		for (int i=0; i<listCnt; i++) {
			OpinionInfo opinionInfo = new OpinionInfo();
			opinionInfo.setXml(xmldata.getChild("opinionList"), i, "opinionInfo");
			this.opinionList.add(opinionInfo);
		}
	}
	
	@Override
	protected String toPrimitiveString() {
		StringBuffer sb = new StringBuffer();
		
		for (int i=0; i<this.opinionList.size(); i++) {
			sb.append(this.opinionList.get(i).toString());
		}

		return sb.toString();
	}
}
