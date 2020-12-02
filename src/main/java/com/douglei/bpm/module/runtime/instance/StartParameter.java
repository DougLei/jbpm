package com.douglei.bpm.module.runtime.instance;

import java.util.HashMap;
import java.util.Map;

/**
 * 流程的启动参数
 * @author DougLei
 */
public class StartParameter {
	private StartingMode mode;
	private int processDefinitionId; // 流程定义的id
	
	private String code; // 流程code
	private String version; // 流程版本
	private String tenantId; // 租户
	
	private String businessId; // (主)业务标识
	private String startUserId; // 启动人
	private Map<String, Object> variables; // 流程变量map集合
	
	public StartParameter(int processDefinitionId) {
		this.processDefinitionId = processDefinitionId;
		this.mode = StartingMode.BY_PROCESS_DEFINITION_ID;
	}
	
	public StartParameter(String code, String version) {
		this(code, version, null);
	}
	public StartParameter(String code, String version, String tenantId) {
		this.code = code;
		this.version = version;
		this.tenantId = tenantId;
		this.mode = StartingMode.BY_PROCESS_DEFINITION_CODE_VERSION;
	}
	
	
	public StartParameter setBusinessId(String businessId) {
		this.businessId = businessId;
		return this;
	}
	public StartParameter setStartUserId(String startUserId) {
		this.startUserId = startUserId;
		return this;
	}
	
	public StartParameter addVariable(String name, Object value) {
		if(this.variables == null)
			this.variables = new HashMap<String, Object>();
		this.variables.put(name, value);
		return this;
	}
	public StartParameter addVariables(Map<String, Object> variables) {
		if(this.variables == null)
			this.variables = new HashMap<String, Object>();
		this.variables.putAll(variables);
		return this;
	}
	
	public StartingMode getMode() {
		return mode;
	}
	public int getProcessDefinitionId() {
		return processDefinitionId;
	}
	public String getCode() {
		return code;
	}
	public String getVersion() {
		return version;
	}
	public String getTenantId() {
		return tenantId;
	}
	public String getBusinessId() {
		return businessId;
	}
	public String getStartUserId() {
		return startUserId;
	}
	public Map<String, Object> getVariables() {
		return variables;
	}
}