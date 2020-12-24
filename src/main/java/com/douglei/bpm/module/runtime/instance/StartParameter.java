package com.douglei.bpm.module.runtime.instance;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.douglei.bpm.module.runtime.variable.Scope;
import com.douglei.bpm.process.handler.VariableEntities;
import com.douglei.tools.utils.StringUtil;

/**
 * 流程的启动参数
 * @author DougLei
 */
public class StartParameter {
	public static final byte BY_PROCESS_DEFINITION_ID= 1; // 使用流程定义的id启动流程
	public static final byte BY_PROCESS_DEFINITION_CODE= 2; // 使用流程定义的code启动主版本的流程
	public static final byte BY_PROCESS_DEFINITION_CODE_VERSION = 3; // 使用流程定义的code和version启动主要子版本的流程
	
	private byte mode; // 启动模式
	private int processDefinitionId; // 流程定义id
	private String code; // 流程code
	private String version; // 流程版本
	private String tenantId; // 租户
	
	private String businessId; // (主)业务标识
	private String startUserId; // 启动人
	private List<String> assignedUserIds; // 指派下一环节办理人
	private VariableEntities variableEntities = new VariableEntities(); // 流程变量
	
	public StartParameter(int processDefinitionId) {
		this.processDefinitionId = processDefinitionId;
		this.mode = BY_PROCESS_DEFINITION_ID;
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
		this.mode = StringUtil.isEmpty(version)?BY_PROCESS_DEFINITION_CODE:BY_PROCESS_DEFINITION_CODE_VERSION;
	}
	
	/**
	 * 设置(主)业务标识
	 * @param businessId
	 * @return
	 */
	public StartParameter setBusinessId(String businessId) {
		this.businessId = businessId;
		return this;
	}
	
	/**
	 * 设置启动人标识
	 * @param startUserId
	 * @return
	 */
	public StartParameter setStartUserId(String startUserId) {
		this.startUserId = startUserId;
		return this;
	}
	
	/**
	 * 添加(指派)下一环节办理人id
	 * @param assignedUserId
	 * @return
	 */
	public StartParameter addAssignedUserId(String assignedUserId) {
		if(assignedUserIds == null)
			assignedUserIds = new ArrayList<String>();
		if(assignedUserIds.isEmpty() || !assignedUserIds.contains(assignedUserId))
			assignedUserIds.add(assignedUserId);
		return this;
	}
	
	/**
	 * 添加变量, 默认是global范围
	 * @param name
	 * @param value
	 * @return
	 */
	public StartParameter addVariable(String name, Object value) {
		return addVariable(name, Scope.GLOBAL, value);
	}
	/**
	 * 添加变量
	 * @param name
	 * @param scope
	 * @param value
	 * @return
	 */
	public StartParameter addVariable(String name, Scope scope, Object value) {
		variableEntities.addVariable(name, scope, value);
		return this;
	}
	/**
	 * 添加变量, 默认是global范围
	 * @param variables
	 * @return
	 */
	public StartParameter addVariables(Map<String, Object> variables) {
		return addVariables(Scope.GLOBAL, variables);
	}
	/**
	 * 添加变量
	 * @param scope
	 * @param variables
	 * @return
	 */
	public StartParameter addVariables(Scope scope, Map<String, Object> variables) {
		variableEntities.addVariables(scope, variables);
		return this;
	}
	/**
	 * 添加变量
	 * @param object
	 * @return
	 */
	public StartParameter addVariables(Object object) {
		variableEntities.addVariables(object);
		return this;
	}
	
	
	public byte getMode() {
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
	public List<String> getAssignedUserIds() {
		return assignedUserIds;
	}
	public VariableEntities getVariableEntities() {
		return variableEntities;
	}
}