package com.douglei.bpm.module.runtime.instance;

import com.douglei.bpm.module.runtime.task.command.HandleTaskParameter;
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
	private String code; // 流程code
	private String version; // 流程版本
	private String tenantId; // 租户
	
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
}