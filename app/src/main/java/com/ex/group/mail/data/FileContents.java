package com.ex.group.mail.data;

import android.os.Parcel;
import android.os.Parcelable;

public class FileContents implements Parcelable {
	
	private String oper;
	private String kind;
	private String id;
	private String seq;
	private String name;
	private String desc;
	private String len;
	private String path;
	private String url;
	
	public FileContents(){}
	
	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel p, int A) {
		p.writeString(oper);
		p.writeString(kind);
		p.writeString(id);
		p.writeString(seq);
		p.writeString(name);
		p.writeString(desc);
		p.writeString(len);
		p.writeString(path);
		p.writeString(url);		
	}

	public static final Parcelable.Creator<FileContents> CREATOR = new Creator<FileContents>(){

		@Override
		public FileContents createFromParcel(Parcel parcel) {
			FileContents filecontents = new FileContents();
			filecontents.setOper(parcel.readString());
			filecontents.setKind(parcel.readString());
			filecontents.setId(parcel.readString());
			filecontents.setSeq(parcel.readString());
			filecontents.setName(parcel.readString());
			filecontents.setDesc(parcel.readString());
			filecontents.setLen(parcel.readString());
			filecontents.setPath(parcel.readString());
			filecontents.setUrl(parcel.readString());			
			return filecontents;
		}

		@Override
		public FileContents[] newArray(int size) {
			// TODO Auto-generated method stub
			return new FileContents[size];
		}
		
	};

	public String getOper() {
		return oper;
	}

	public void setOper(String oper) {
		this.oper = oper;
	}

	public String getKind() {
		return kind;
	}

	public void setKind(String kind) {
		this.kind = kind;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getSeq() {
		return seq;
	}

	public void setSeq(String seq) {
		this.seq = seq;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public String getLen() {
		return len;
	}

	public void setLen(String len) {
		this.len = len;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
	
	
}
