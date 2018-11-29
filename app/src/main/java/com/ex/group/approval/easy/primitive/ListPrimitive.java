package com.ex.group.approval.easy.primitive;

import android.util.Log;

import com.ex.group.approval.easy.constant.UserInfo;
import com.ex.group.approval.easy.domain.Doc;
import com.skt.pe.common.activity.SKTActivity.Action;
import com.skt.pe.common.exception.SKTException;
import com.skt.pe.common.primitive.Primitive;
import com.skt.pe.common.service.XMLData;

import java.util.ArrayList;
import java.util.List;


@SuppressWarnings("serial")
public class ListPrimitive extends Primitive {
	// 2014-07-18 Join 수정 : 간이결재 고도화로 인한 수정
	// 결재하기(=결재 할 문서), 결재 상황보기(=결재진행 문서), 결재완료함, 결재 반려함 리스트 조회
	public final static String name = "COMMON_APPROVALNEW_LIST";
	private final String TAG = "ListPrimitive";
	
	private final static String[] paramNames = {
		"docType", "userID", "paging"
	};
	
	private List<Doc> docList = new ArrayList<Doc>();
	private String page = null;
	private String totalPage = null;
	private String end = null;
	
	public ListPrimitive() {
		super(ListPrimitive.name, paramNames, Action.SERVICE_RETRIEVING);
		this.init();
	}
	
	private void init() {
//		this.setUserID("19203920");
//		this.setUserID("19516018");
		UserInfo userInfo = UserInfo.getInstance();
		this.setUserID(userInfo.getUserID());
	}
	
	public void setDocType(String docType) {
		super.addParameter(paramNames[0], docType);
	}
	
	public void setUserID(String userID) {
		super.addParameter(paramNames[1], userID);
	}
	
	public void setPage(int page) {
		this.page = Integer.toString(page);
		super.addParameter(paramNames[2], page);
	}
	
	public int getPage() {
		return parseInt(page);
	}
	
	public List<Doc> getDocList() {
		return this.docList;
	}
	
	public String getEnd() {
		return this.end;
	}
	
	@Override
	public void convertXML(XMLData xmldata) throws SKTException {
		super.convertXML(xmldata);

		docList.clear();
		totalPage = xmldata.get("PAGE");
		
		if(page != null && totalPage != null && totalPage.equals(page)) {
			end = "Y";
		} else {
			end = "N";
		}
		
		Log.d(TAG, "page =========== " + page);
		Log.d(TAG, "totalPage =========== " + totalPage);
		Log.d(TAG, "end =========== " + end);
		
		int listCnt = parseInt(xmldata.get("DocumentListCnt"));
		Log.d(TAG, "convertXML() - listCnt :: "+listCnt);
		for (int i = 0; i < listCnt; i++) {
			Doc doc = new Doc();
			doc.setXml(xmldata.getChild("DocumentList"), i, "Document");
			docList.add(doc);
			Log.d(TAG, "convertXML() - docList ["+i+"] :: "+docList.get(i).getTitle());
		}
	}
	
	@Override
	protected String toPrimitiveString() {
		StringBuffer sb = new StringBuffer();
		
		sb.append("end=");
		sb.append(this.getEnd());
		sb.append("&");
		sb.append("listCnt=");
		sb.append(this.docList.size());
		sb.append("&");
		sb.append("docList=");
		
		for (int i=0; i<this.docList.size(); i++) {
			sb.append(this.docList.get(i).toString());
			Log.d(TAG, "toPrimitiveString() - docList ["+i+"] :: "+this.docList.get(i).toString());
		}

		return sb.toString();
	}
	
	public static class Extras {
		public final static String APPROVAL_TITLE = "APPROVAL_TITLE";
		public final static String APPROVAL_TYPE = "APPROVAL_TYPE";
	}
}
