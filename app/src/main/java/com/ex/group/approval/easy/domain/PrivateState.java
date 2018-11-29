package com.ex.group.approval.easy.domain;

import java.io.Serializable;

@SuppressWarnings("serial")
public class PrivateState implements Serializable {
	private String level;						// ??
	private String parentKind;				// ??
	private String parentSeqNum;		// ??
	private String seqNum;					// ??
	private String actorType;				// ??
	/*
	 * 결재종류 - 110 : 결재, 111 : 협조, 112 : 공람, 113 : 전결, 114 : 대결, 115 : 담당, 116 : 결재안함, 117 : 대결(사후보고), 
	 *                 118 : 감사(부서), 119 : 검토, 120 : 상임감사(개인), 121 : 부서 협조, 122 : 부서결재 접수, 123 : 부서결재 기안, 
	 *                 124 : 부서결재 반송, 125 : 기안, 126 : 대결(한문 대), 127 : 대결(권한대행), 128 : 사후보고(한문대), 130 : 기안전결, 
	 *                 131 : 협조(병렬), 132 : 협조안함, 133 : 협조안함(병렬), 134 : 부서 협조(병렬), 135 : 참조, 136 : 기업후결, 137 : 기업후열, 
	 *                 138 : 상임감사 후결, 139 : 협조후결(일반), 140 : 협조후열(일반), 141 : 통제
	 */
	private String kind;
	
	private String id;							// 결재자ID (기존 : userId)
	private String nfuserid;					// ??
	private String kid;						// ??
	private String name;						// 결재자명 (기존과 동일)
	private String deptID;					// 결재자 부서코드
	private String deptName;				// 결재자 부서명 (기존 : deptNm)
	private String parentOrgID;			// 결재자 최상위 조직코드??
	private String parentOrgName;		// 결재자 최상위 조직명??
	private String rank;						// 결재자 직위
	private String role;						// 결재자 직책 (기존 : position)
	private String caste;						// 결재자 직급
	private String extRole;					// 결재자 직책??
	private String telePhone;				// 결재자 연락처??
	private String employeeNumber;	// 결재자 사번??
	private String userType;				// ??
	private String insaCode;				// 인사코드??
	
	private String notProcessReason;	// ??
	private String isFormConnected;	// ??
	private String postProcess;			// ??
	private String smsUse;					// ??
	private String permID;					// ??
	private String canDelete;				// ??
	
	public String getLevel() {
		return level;
	}
	public void setLevel(String level) {
		this.level = level;
	}
	public String getParentKind() {
		return parentKind;
	}
	public void setParentKind(String parentKind) {
		this.parentKind = parentKind;
	}
	public String getParentSeqNum() {
		return parentSeqNum;
	}
	public void setParentSeqNum(String parentSeqNum) {
		this.parentSeqNum = parentSeqNum;
	}
	public String getSeqNum() {
		return seqNum;
	}
	public void setSeqNum(String seqNum) {
		this.seqNum = seqNum;
	}
	public String getActorType() {
		return actorType;
	}
	public void setActorType(String actorType) {
		this.actorType = actorType;
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
	public String getNfuserid() {
		return nfuserid;
	}
	public void setNfuserid(String nfuserid) {
		this.nfuserid = nfuserid;
	}
	public String getKid() {
		return kid;
	}
	public void setKid(String kid) {
		this.kid = kid;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDeptID() {
		return deptID;
	}
	public void setDeptID(String deptID) {
		this.deptID = deptID;
	}
	public String getDeptName() {
		return deptName;
	}
	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}
	public String getParentOrgID() {
		return parentOrgID;
	}
	public void setParentOrgID(String parentOrgID) {
		this.parentOrgID = parentOrgID;
	}
	public String getParentOrgName() {
		return parentOrgName;
	}
	public void setParentOrgName(String parentOrgName) {
		this.parentOrgName = parentOrgName;
	}
	public String getRank() {
		return rank;
	}
	public void setRank(String rank) {
		this.rank = rank;
	}
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}
	public String getCaste() {
		return caste;
	}
	public void setCaste(String caste) {
		this.caste = caste;
	}
	public String getExtRole() {
		return extRole;
	}
	public void setExtRole(String extRole) {
		this.extRole = extRole;
	}
	public String getTelePhone() {
		return telePhone;
	}
	public void setTelePhone(String telePhone) {
		this.telePhone = telePhone;
	}
	public String getEmployeeNumber() {
		return employeeNumber;
	}
	public void setEmployeeNumber(String employeeNumber) {
		this.employeeNumber = employeeNumber;
	}
	public String getUserType() {
		return userType;
	}
	public void setUserType(String userType) {
		this.userType = userType;
	}
	public String getInsaCode() {
		return insaCode;
	}
	public void setInsaCode(String insaCode) {
		this.insaCode = insaCode;
	}
	public String getNotProcessReason() {
		return notProcessReason;
	}
	public void setNotProcessReason(String notProcessReason) {
		this.notProcessReason = notProcessReason;
	}
	public String getIsFormConnected() {
		return isFormConnected;
	}
	public void setIsFormConnected(String isFormConnected) {
		this.isFormConnected = isFormConnected;
	}
	public String getPostProcess() {
		return postProcess;
	}
	public void setPostProcess(String postProcess) {
		this.postProcess = postProcess;
	}
	public String getSmsUse() {
		return smsUse;
	}
	public void setSmsUse(String smsUse) {
		this.smsUse = smsUse;
	}
	public String getPermID() {
		return permID;
	}
	public void setPermID(String permID) {
		this.permID = permID;
	}
	public String getCanDelete() {
		return canDelete;
	}
	public void setCanDelete(String canDelete) {
		this.canDelete = canDelete;
	}
	
	
	
}