package com.douglei.bpm.module.runtime.instance.start;

import java.util.HashMap;
import java.util.Map;

import com.douglei.bpm.module.components.variable.Variable;

/**
 * 流程的启动参数
 * @author DougLei
 */
public class StartParameter {
	private StartingMode startingMode;
	private int processDefinitionId; // 流程定义的id
	
	private String code; // 流程code
	private String version; // 流程版本
	private String tenantId; // 租户
	
	private String businessId; // (主)业务标识
	private String startUser; // 启动人
	private Map<String, Variable> variables; // 流程变量map集合
	
	public StartParameter(int processDefinitionId) {
		this.processDefinitionId = processDefinitionId;
		this.startingMode = StartingMode.BY_PROCESS_DEFINITION_ID;
	}
	
	public StartParameter(String code) {
		this(code, null, null);
	}
	public StartParameter(String code, String version) {
		this(code, version, null);
	}
	public StartParameter(String code, String version, String tenantId) {
		this.code = code;
		this.version = version;
		this.tenantId = tenantId;
		this.startingMode = StartingMode.BY_PROCESS_DEFINITION_CODE_VERSION;
	}
	
	public StartParameter setBusinessId(String businessId) {
		this.businessId = businessId;
		return this;
	}
	public StartParameter setStartUser(String startUser) {
		this.startUser = startUser;
		return this;
	}
	
	public StartParameter addVariable(String name, Object value) {
		if(this.variables == null)
			this.variables = new HashMap<String, Variable>();
		this.variables.put(name, new Variable(name, value));
		return this;
	}
	public StartParameter addVariables(Map<String, Object> variables) {
		if(this.variables == null)
			this.variables = new HashMap<String, Variable>();
		
		variables.entrySet().forEach(entry -> this.variables.put(entry.getKey(), new Variable(entry.getKey(), entry.getValue())));
		return this;
	}
	
	public StartingMode getStartingMode() {
		return startingMode;
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
	public String getStartUser() {
		return startUser;
	}
	public Map<String, Variable> getVariables() {
		return variables;
	}
}