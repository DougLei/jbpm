package com.douglei.bpm.module.runtime.instance;

import java.util.Map;

import com.douglei.bpm.module.runtime.task.command.parameter.HandleTaskParameter;
import com.douglei.bpm.module.runtime.variable.Scope;
import com.douglei.bpm.process.handler.VariableEntities;
import com.douglei.tools.utils.StringUtil;

/**
 * 流程的启动参数
 * @author DougLei
 */
public class StartParameter extends HandleTaskParameter{
	public static final byte BY_PROCESS_DEFINITION_ID= 1; // 使用流程定义的id启动流程
	public static final byte BY_PROCESS_DEFINITION_CODE= 2; // 使用流程定义的code启动主版本的流程
	public static final byte BY_PROCESS_DEFINITION_CODE_VERSION = 3; // 使用流程定义的code和version启动主要子版本的流程
	
	private byte mode; // 启动模式
	private int processDefinitionId; // 流程定义id
	private String code; // 流程定义code
	private String version; // 流程定义version
	private String tenantId; // 租户id
	
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
	 * 添加变量, 默认是global范围
	 * @param name
	 * @param value
	 * @return
	 */
	public HandleTaskParameter addVariable(String name, Object value) {
		return addVariable(name, Scope.GLOBAL, value);
	}
	/**
	 * 添加变量
	 * @param name
	 * @param scope
	 * @param value
	 * @return
	 */
	public HandleTaskParameter addVariable(String name, Scope scope, Object value) {
		variableEntities.addVariable(name, scope, value);
		return this;
	}
	/**
	 * 添加变量, 默认是global范围
	 * @param variables
	 * @return
	 */
	public HandleTaskParameter addVariables(Map<String, Object> variables) {
		return addVariables(Scope.GLOBAL, variables);
	}
	/**
	 * 添加变量
	 * @param scope
	 * @param variables
	 * @return
	 */
	public HandleTaskParameter addVariables(Scope scope, Map<String, Object> variables) {
		variableEntities.addVariables(scope, variables);
		return this;
	}
	/**
	 * 添加变量
	 * @param object
	 * @return
	 */
	public HandleTaskParameter addVariables(Object object) {
		variableEntities.addVariables(object);
		return this;
	}
	
	/**
	 * 获取启动模式
	 * @return
	 */
	public byte getMode() {
		return mode;
	}
	/**
	 * 获取流程定义id
	 * @return
	 */
	public int getProcessDefinitionId() {
		return processDefinitionId;
	}
	/**
	 * 获取流程定义code
	 * @return
	 */
	public String getCode() {
		return code;
	}
	/**
	 * 获取流程定义version
	 * @return
	 */
	public String getVersion() {
		return version;
	}
	/**
	 * 获取租户id
	 * @return
	 */
	public String getTenantId() {
		return tenantId;
	}
	/**
	 * 获取VariableEntities实例
	 * @return
	 */
	public VariableEntities getVariableEntities() {
		return variableEntities;
	}
}