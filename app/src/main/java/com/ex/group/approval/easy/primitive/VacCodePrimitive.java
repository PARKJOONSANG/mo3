package com.ex.group.approval.easy.primitive;

import android.content.Context;

import com.ex.group.approval.easy.constant.ApprovalConstant;
import com.ex.group.approval.easy.constant.UserInfo;
import com.ex.group.approval.easy.domain.VocCode;
import com.ex.group.approval.easy.domain.VocCodeTree;
import com.skt.pe.common.activity.SKTActivity.Action;
import com.skt.pe.common.data.AuthData;
import com.skt.pe.common.data.SKTUtil;
import com.skt.pe.common.ds.Tree;
import com.skt.pe.common.exception.SKTException;
import com.skt.pe.common.primitive.Primitive;
import com.skt.pe.common.service.XMLData;

import java.util.Map;


@SuppressWarnings("serial")
public class VacCodePrimitive extends Primitive {
	// 휴가종류코드 리스트 조회
	public final static String name = "COMMON_APPROVALSTAGING_VACCODE";
	
	private final static String[] paramNames = {
		"systemType", "hiddenWorkGuBun", "hiddenSWBeonHo", "userID"
	};
	
	private VocCodeTree vocCodeTree = new VocCodeTree();
	
	public VacCodePrimitive() {
		super(VacCodePrimitive.name, paramNames, Action.SERVICE_RETRIEVING);
		this.init(null);
	}
	
	public VacCodePrimitive(Context ctx) {
		super(VacCodePrimitive.name, paramNames, Action.SERVICE_RETRIEVING);
		this.init(ctx);
	}	
	
	private void init(Context ctx) {
		this.setSystemType(ApprovalConstant.SystemType.EASY, ctx);
	}
	
	public void setSystemType(String systemType, Context ctx) {
		super.addParameter(paramNames[0], systemType);
		
		if(ctx != null){
			super.addParameter(paramNames[1], "select");
			Map<String, String> map;
			try {
				map = SKTUtil.getGMPAuth(ctx);
				
				if(!map.get(AuthData.ID_ID).startsWith("010")){
					super.addParameter(paramNames[2], map.get(AuthData.ID_ID));
				}
				
			} catch (SKTException e) {
			
			}	
		}
		
		UserInfo userInfo = UserInfo.getInstance();
		super.addParameter(paramNames[3], userInfo.getUserID());
	}
	
	public VocCodeTree getVocCodeTree() {
		return this.vocCodeTree;
	}
	
	@Override
	public void convertXML(XMLData xmldata) throws SKTException {
		super.convertXML(xmldata);
		
		// 첫번째 항목이 내려옴
		XMLData vacCodeList = xmldata.getChild("vacCodeList");
		int listCnt = parseInt(vacCodeList.get("listCnt"));

		vocCodeTree.initTree(listCnt);
		for (int i=0; i<listCnt; i++) {
			Tree<VocCode> leaf = (Tree<VocCode>) vocCodeTree.getTree(i);
			VocCode vc = new VocCode();
			vc.setXml(vacCodeList, i, "vocCode");
			leaf.setItem(vc);
			XMLData c1 = vacCodeList.getChild(i);
			XMLData children = c1.getChild("children1st");
			
			insertTree1st(children, leaf);
		}
	}
	
	private void insertTree1st(XMLData xmldata, Tree<VocCode> tree) throws SKTException {
		int listCnt = parseInt(xmldata.get("listCnt"));
		if (listCnt == 0) return;
		tree.initTree(listCnt);

		xmldata.setList("child1st");
		
		for (int i=0; i<listCnt; i++) {
			Tree<VocCode> leaf = (Tree<VocCode>) tree.getTree(i);
			VocCode vc = new VocCode();
			vc.setXml(xmldata, i);
			leaf.setItem(vc);
			XMLData c1 = xmldata.getChild(i);
			XMLData children = c1.getChild("children2nd");
			
			insertTree2nd(children, leaf);
		}
	}
	
	private void insertTree2nd(XMLData xmldata, Tree<VocCode> tree) throws SKTException {
		int listCnt = parseInt(xmldata.get("listCnt"));
		if (listCnt == 0) return;
		tree.initTree(listCnt);

		xmldata.setList("child2nd");
		
		for (int i=0; i<listCnt; i++) {
			Tree<VocCode> leaf = (Tree<VocCode>) tree.getTree(i);
			VocCode vc = new VocCode();
			vc.setXml(xmldata, i);
			leaf.setItem(vc);
		}
	}
	
	@Override
	protected String toPrimitiveString() {
		return this.getVocCodeTree().toString();
	}
}
