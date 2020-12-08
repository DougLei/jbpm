package com.douglei.bpm.module.runtime.instance;

import java.util.Map;

import com.douglei.bpm.module.runtime.variable.ProcessVariableMapHandler;
import com.douglei.bpm.module.runtime.variable.ProcessVariableMapHolder;
import com.douglei.bpm.module.runtime.variable.Scope;
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
	private ProcessVariableMapHandler processVariableMapHandler = new ProcessVariableMapHandler(); // 流程变量
	
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
		return addVariable(name, Scope.GLOBAL, value);
	}
	public StartParameter addVariable(String name, Scope scope, Object value) {
		processVariableMapHandler.addVariable(name, scope, value);
		return this;
	}
	public StartParameter addVariables(Map<String, Object> variables) {
		return addVariables(Scope.GLOBAL, variables);
	}
	public StartParameter addVariables(Scope scope, Map<String, Object> variables) {
		processVariableMapHandler.addVariables(scope, variables);
		return this;
	}
	public StartParameter addVariables(Object object) {
		processVariableMapHandler.addVariables(object);
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
	public ProcessVariableMapHolder getProcessVariableMapHolder() {
		return processVariableMapHandler;
	}
}