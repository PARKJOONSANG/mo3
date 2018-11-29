package com.ex.group.approval.easy.primitive;

import android.util.Log;

import com.ex.group.approval.easy.constant.UserInfo;
import com.ex.group.approval.easy.domain.PrivateLine;
import com.ex.group.approval.easy.domain.PrivateState;
import com.skt.pe.common.activity.SKTActivity.Action;
import com.skt.pe.common.exception.SKTException;
import com.skt.pe.common.primitive.Primitive;
import com.skt.pe.common.service.XMLData;

import org.w3c.dom.Node;

import java.util.ArrayList;
import java.util.List;


@SuppressWarnings("serial")
public class PrivateLineListPrimitive extends Primitive {
	private final String TAG = "PrivateLineListPrimitive";

	// 사용자 저장된 결재경로 리스트
	public final static String name = "COMMON_APPROVALNEW_PRIVATELINE";
	
	
	private final static String[] paramNames = {
		"userID"
	};
	
	private List<PrivateLine> lineList = new ArrayList<PrivateLine>();
	
	public PrivateLineListPrimitive() {
		super(PrivateLineListPrimitive.name, paramNames, Action.SERVICE_RETRIEVING);
		this.init();
	}
	
	private void init() {
//		this.setUserID("19203920");
		UserInfo userInfo = UserInfo.getInstance();
		this.setUserID(userInfo.getUserID());
	}
	
	public void setUserID(String userID) {
		super.addParameter(paramNames[0], userID);
	}
	
	public List<PrivateLine> getLineList() {
		return this.lineList;
	}
	
	@Override
	public void convertXML(XMLData xmldata) throws SKTException {
		super.convertXML(xmldata);
		
		this.lineList.clear();
		int listCnt = parseInt(xmldata.get("LaliasListCnt"));
		Log.d(TAG, "LaliasListCnt ================ " + listCnt);
		
		for (int i = 0; i < listCnt; i++) {
			PrivateLine line = new PrivateLine();
			XMLData child = xmldata.getChild("LaliasList");
			line.setXml(child, i, "WFSancAlias");
			
			// 2014.08.04 Join -> 서버로부터 넘겨받은 XML의 Depth가 깊어 아래와 같이 복잡해짐...
			Node node = child.getNode("WFSancAlias");
			for(int k = 0; k < node.getChildNodes().getLength(); k++) {
				Node cn1 = node.getChildNodes().item(k);
//				Log.d(TAG, k + "번째 cn1 ============ " + cn1.getNodeName() + " : " + cn1.getTextContent());
				
				if("sancs".equals(cn1.getNodeName())) {
					PrivateState pState = new PrivateState();
					XMLData child2 = new XMLData(cn1);
					Node cn2 = child2.getNode("/WFSancLineAlias");
					
					for(int l = 0; l < cn2.getChildNodes().getLength(); l++) {
						Node cn3 = cn2.getChildNodes().item(l);
//						Log.d(TAG, l + "번째 cn3 ============ " + cn3.getNodeName() + " : " + cn3.getTextContent());
						
						if("level".equals(cn3.getNodeName()))			pState.setLevel(cn3.getTextContent());
						if("parentKind".equals(cn3.getNodeName()))			pState.setParentKind(cn3.getTextContent());
						if("parentSeqNum".equals(cn3.getNodeName()))			pState.setParentSeqNum(cn3.getTextContent());
						if("seqNum".equals(cn3.getNodeName()))			pState.setSeqNum(cn3.getTextContent());
						if("actorType".equals(cn3.getNodeName()))			pState.setActorType(cn3.getTextContent());
						if("kind".equals(cn3.getNodeName()))			pState.setKind(cn3.getTextContent());
						if("notProcessReason".equals(cn3.getNodeName()))			pState.setNotProcessReason(cn3.getTextContent());
						if("isFormConnected".equals(cn3.getNodeName()))			pState.setIsFormConnected(cn3.getTextContent());
						if("postProcess".equals(cn3.getNodeName()))			pState.setPostProcess(cn3.getTextContent());
						if("smsUse".equals(cn3.getNodeName()))			pState.setSmsUse(cn3.getTextContent());
						if("permID".equals(cn3.getNodeName()))			pState.setPermID(cn3.getTextContent());
						if("canDelete".equals(cn3.getNodeName()))			pState.setCanDelete(cn3.getTextContent());
					}
					
					Node cn4 = child2.getNode("/WFSancLineAlias/person/WFUserInfo");
					
					for(int m = 0; m < cn4.getChildNodes().getLength(); m++) {
						Node cn5 = cn4.getChildNodes().item(m);
//						Log.d(TAG, m + "번째 cn5 ============ " + cn5.getNodeName() + " : " + cn5.getTextContent());
						
						if("id".equals(cn5.getNodeName()))			pState.setId(cn5.getTextContent());
						if("nfuserid".equals(cn5.getNodeName()))			pState.setNfuserid(cn5.getTextContent());
						if("kid".equals(cn5.getNodeName()))			pState.setKid(cn5.getTextContent());
						if("name".equals(cn5.getNodeName()))			pState.setName(cn5.getTextContent());
						if("deptID".equals(cn5.getNodeName()))			pState.setDeptID(cn5.getTextContent());
						if("deptName".equals(cn5.getNodeName()))			pState.setDeptName(cn5.getTextContent());
						if("parentOrgID".equals(cn5.getNodeName()))			pState.setParentOrgID(cn5.getTextContent());
						if("parentOrgName".equals(cn5.getNodeName()))			pState.setParentOrgName(cn5.getTextContent());
						if("rank".equals(cn5.getNodeName()))			pState.setRank(cn5.getTextContent());
						if("role".equals(cn5.getNodeName()))			pState.setRole(cn5.getTextContent());
						if("caste".equals(cn5.getNodeName()))			pState.setCaste(cn5.getTextContent());
						if("extRole".equals(cn5.getNodeName()))			pState.setExtRole(cn5.getTextContent());
						if("telePhone".equals(cn5.getNodeName()))			pState.setTelePhone(cn5.getTextContent());
						if("employeeNumber".equals(cn5.getNodeName()))			pState.setEmployeeNumber(cn5.getTextContent());
						if("userType".equals(cn5.getNodeName()))			pState.setUserType(cn5.getTextContent());
						if("insaCode".equals(cn5.getNodeName()))			pState.setInsaCode(cn5.getTextContent());
					}
					
					line.getStateList().add(pState);
				}
			}
			
			/*for (int j = 0; j < stateListCnt; j++) {
				PrivateState pState = new PrivateState();
				line.getStateList().add(pState);
				PrivateState pState = new PrivateState();
				pState.setXml(child.getChild("WFSancAlias"), j, "sancs");
				line.getStateList().add(pState);
			}*/
			
			this.lineList.add(line);
		}
		
		Log.d(TAG, "List<PrivateLine> size ================ " + this.lineList.size());
		for(int i = 0; i < lineList.size(); i++) {
			Log.d(TAG, i + "번째 List<PrivateState> size ================ " + this.lineList.get(i).getStateList().size());
			for(int j = 0; j < this.lineList.get(i).getStateList().size(); j++) {
				Log.d(TAG, "List<PrivateState> " + j + "번째 kind ============== " + this.lineList.get(i).getStateList().get(j).getKind());
				Log.d(TAG, "List<PrivateState> " + j + "번째 id ============== " + this.lineList.get(i).getStateList().get(j).getId());
				Log.d(TAG, "List<PrivateState> " + j + "번째 name ============== " + this.lineList.get(i).getStateList().get(j).getName());
			}
		}
		// 2014.08.04 Join -> 서버로부터 넘겨받은 XML의 Depth가 깊어 아래와 같이 복잡해짐...
	}
	
	@Override
	protected String toPrimitiveString() {
		StringBuffer sb = new StringBuffer();
		
		for (int i=0; i<this.lineList.size(); i++) {
			sb.append(this.lineList.get(i).toString());
			List<PrivateState> stateList = this.lineList.get(i).getStateList();
			sb.append("&stateList=");
			for (int j=0; j<stateList.size(); j++) {
				sb.append(stateList.get(j).toString());
			}
		}

		return sb.toString();
	}
}
