package com.douglei.bpm.process.handler.components.assignee;

/**
 * 
 * @author DougLei
 */
class DelegationProcess {
	private String code;
	private String version;
	
	public DelegationProcess(String code, String version) {
		this.code = code;
		this.version = version;
	}
	public boolean matching(String processCode, String processVersion) {
		if(code.equals(processCode)) {
			if(version == null || version.equals(processVersion))
				return true;
		}
		return false;
	}
}
