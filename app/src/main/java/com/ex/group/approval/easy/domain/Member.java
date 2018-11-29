package com.ex.group.approval.easy.domain;

import android.content.Intent;

import com.ex.group.approval.easy.primitive.DraftPrimitive;

import java.io.Serializable;


@SuppressWarnings("serial")
public class Member implements Serializable {
	private String name;
	private String empId;
	private String role;
	private String suffixDept;
	private String approvalKind;
	
	public Member() {
		init();
	}
	
	public Member(Intent intent) {
		if (intent != null) {
			this.name = intent.getStringExtra("name");
			this.empId = intent.getStringExtra("empId");
			this.role = intent.getStringExtra("role");
			this.suffixDept = intent.getStringExtra("suffixDept");
		}
		init();
	}
	
	private void init() {
		this.approvalKind = DraftPrimitive.ApprovalKind.NORMAL;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getEmpId() {
		return empId;
	}
	public void setEmpId(String empId) {
		this.empId = empId;
	}
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}
	public String getSuffixDept() {
		return suffixDept;
	}
	public void setSuffixDept(String suffixDept) {
		this.suffixDept = suffixDept;
	}
	public String getApprovalKind() {
		return approvalKind;
	}
	public void setApprovalKind(String approvalKind) {
		this.approvalKind = approvalKind;
	}
	public boolean isNull() {
		return empId == null || empId.length() == 0;
	}

	public void setMember(Member member) {
		this.setName(member.getName());
		this.setEmpId(member.getEmpId());
		this.setRole(member.getRole());
		this.setSuffixDept(member.getSuffixDept());
//		this.setApprovalKind(member.getApprovalKind());
	}
}