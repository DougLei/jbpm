package com.douglei.bpm.module.repository.delegation;

/**
 * 流程委托明细
 * @author DougLei
 */
public class ProcessDelegationDetail {
	private Integer id;
	private Integer delegationId;
	private String procdefCode;
	private String procdefVersion;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getDelegationId() {
		return delegationId;
	}
	public void setDelegationId(Integer delegationId) {
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
