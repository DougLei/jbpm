package com.douglei.bpm.module.repository.delegation;

/**
 * 流程委托明细
 * @author DougLei
 */
public class DelegationDetail {
	private int id;
	private int delegationId;
	private String procdefCode;
	private String procdefVersion;
	
	public DelegationDetail() {}
	public DelegationDetail(String procdefCode, String procdefVersion) {
		this.procdefCode = procdefCode;
		this.procdefVersion = procdefVersion;
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getDelegationId() {
		return delegationId;
	}
	public void setDelegationId(int delegationId) {
		this.delegationId = delegationId;
	}
	public String getProcdefCode() {
		return procdefCode;
	}
	public void setProcdefCode(String procdefCode) {
		this.procdefCode = procdefCode;
	}
	public String getProcdefVersion() {
		return procdefVersion;
	}
	public void setProcdefVersion(String procdefVersion) {
		this.procdefVersion = procdefVersion;
	}
}
