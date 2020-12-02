package com.douglei.bpm.module.runtime.instance;

import java.util.HashMap;
import java.util.Map;

import com.douglei.tools.utils.StringUtil;

/**
 * 流程的启动参数
 * @author DougLei
 */
public class StartParameter {
	static final byte BY_PROCESS_DEFINITION_ID = 1; // 使用流程定义的id启动流程
	static final byte BY_PROCESS_DEFINITION_CODE= 2; // 使用流程定义的code启动最新版本的流程
	static final byte BY_PROCESS_DEFINITION_CODE_VERSION = 3; // 使用流程定义的code和version启动最新版本的流程
	
	private byte startMode;
	private int processDefinitionId; // 流程定义的id
	
	private String code; // 流程code
	private String version; // 流程版本
	private String tenantId; // 租户
	
	private String businessId; // (主)业务标识
	private String startUserId; // 启动人
	private Map<String, Object> variables; // 流程变量map集合
	
	public StartParameter(int processDefinitionId) {
		this.processDefinitionId = processDefinitionId;
		this.startMode = BY_PROCESS_DEFINITION_ID;
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
		this.startMode = StringUtil.isEmpty(version)?BY_PROCESS_DEFINITION_CODE:BY_PROCESS_DEFINITION_CODE_VERSION;
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
	
	public byte getStartMode() {
		return startMode;
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