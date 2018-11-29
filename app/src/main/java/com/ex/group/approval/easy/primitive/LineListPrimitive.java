package com.ex.group.approval.easy.primitive;

import com.ex.group.approval.easy.constant.ApprovalConstant;
import com.ex.group.approval.easy.constant.UserInfo;
import com.ex.group.approval.easy.domain.State;
import com.skt.pe.common.activity.SKTActivity.Action;
import com.skt.pe.common.exception.SKTException;
import com.skt.pe.common.primitive.Primitive;
import com.skt.pe.common.service.XMLData;

import java.util.ArrayList;
import java.util.List;


@SuppressWarnings("serial")
public class LineListPrimitive extends Primitive {
	// 결재선/현황 리스트
	public final static String name = "COMMON_APPROVALSTAGING_LINELIST";
	
	private final static String[] paramNames = {
		"systemType", "wdid", "docHref", "userID"
	};
	
	private String docKey = null;
	private String systemType = null;
	private List<State> stateList = new ArrayList<State>();
	
	public LineListPrimitive() {
		super(LineListPrimitive.name, paramNames, Action.SERVICE_RETRIEVING);
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
	
	public void setWdid(String wdid) {
		super.addParameter(paramNames[1], wdid);
	}
	
	public void setDocHref(String docHref) {
		super.addParameter(paramNames[2], docHref);
	}
	
	public void setUserID(String userID) {
		super.addParameter(paramNames[3], userID);
	}
	
	public String getDocKey() {
		return this.docKey;
	}
	
	public String getSystemType() {
		return this.systemType;
	}
	
	public List<State> getStateList() {
		return this.stateList;
	}
	
	@Override
	public void convertXML(XMLData xmldata) throws SKTException {
		super.convertXML(xmldata);
		
		this.docKey = xmldata.get("docKey");
		this.systemType = xmldata.get("systemType");
		int listCnt = parseInt(xmldata.get("listCnt"));
		for (int i=0; i<listCnt; i++) {
			State state = new State();
			state.setXml(xmldata.getChild("stateList"), i, "state");
			this.stateList.add(state);
		}
	}
	
	@Override
	protected String toPrimitiveString() {
		StringBuffer sb = new StringBuffer();
		
		sb.append("docKey=");
		sb.append(this.getDocKey());
		sb.append("&");
		sb.append("systemType=");
		sb.append(this.getSystemType());
		sb.append("&");
		sb.append("listCnt=");
		sb.append(this.stateList.size());
		sb.append("&");
		sb.append("stateList=");
		
		for (int i=0; i<this.stateList.size(); i++) {
			sb.append(this.stateList.get(i).toString());
		}

		return sb.toString();
	}
}
