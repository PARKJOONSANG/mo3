package com.sk.pe.group.imageviewer.manager;

public class QueueItem {
	public int status;
	public int type;
	public String url;
	public String primitive;
	public String docId;
	public String pageNo;
	public Exception exp;
	private String uid;
	
	public QueueItem(String url, String primitive, String docId, String pageNo, int type) {
		this.url = url;
		this.primitive = primitive;
		this.docId = docId;
		this.pageNo = pageNo;
		this.type = type;
	}
	
	public QueueItem(QueueItem value) {
		this.status = value.status;
		this.type = value.type;
		this.url = value.url;
		this.primitive = value.primitive;
		this.docId = value.docId;
		this.pageNo = value.pageNo;
		this.exp = value.exp;
		this.uid = value.getUid();
	}
	
	public void setUid() {
		this.uid = this.generateUid();
	}
	
	public String getUid() {
		return this.uid;
	}
	
	private synchronized String generateUid() {
		// 현재 편의상 고유값을 시간 기반으로 만들기 때문에 고유한 값을 만들기 위해 일부로 Sleep를 한다.
		// by pluto248
		try {
			Thread.sleep(10);
		} catch (InterruptedException e) {
			for (int tmp=0; tmp<10000; tmp++);
		}
		return String.valueOf(System.currentTimeMillis());
//		return this.primitive + "_" + this.docId + "_" + this.pageNo;
	}
	
	public boolean equalFile(QueueItem value) {
		return this.primitive.equals(value.primitive) && this.url.equals(value.url) && this.docId.equals(value.docId) && this.pageNo.equals(value.pageNo);
	}
	
	public String toString() {
		return "status="+status+"&url="+url+"&primitive="+primitive+"&docId="+docId+"&pageNo="+pageNo+"&type="+type+"&exp="+(exp==null?null:exp.getMessage())+"&uid="+getUid();
	}
}
