package com.douglei.bpm.module.execution.instance.command.parameter;

import com.douglei.bpm.process.handler.AssignEntity;
import com.douglei.bpm.process.handler.TaskHandleException;
import com.douglei.bpm.process.handler.VariableEntities;

/**
 * 流程的启动参数
 * @author DougLei
 */
public class StartParameter {
	public static final byte BY_ID= 1; // 使用流程定义的id启动流程
	public static final byte BY_CODE= 2; // 使用流程定义的code启动主版本的流程
	public static final byte BY_CODE_VERSION = 3; // 使用流程定义的code和version启动主要子版本的流程
	
	private Integer id; // 流程定义id
	private String code; // 流程定义code
	private String version; // 流程定义version
	private String tenantId; // 租户id
	private String userId; // 发起人id
	private String businessId; // 关联的业务id
	private AssignEntity assignEntity = new AssignEntity(); // 指派实体 
	private VariableEntities variableEntities = new VariableEntities(); // 流程变量
	
	public StartParameter(Integer id) {
		this(id, null, null, null);
	}
	public StartParameter(String code) {
		this(null, code, null, null);
	}
	public StartParameter(String code, String version) {
		this(null, code, version, null);
	}
	public StartParameter(String code, String version, String tenantId) {
		this(null, code, version, tenantId);
	}
	private StartParameter(Integer id, String code, String version, String tenantId) {
		if(id == null && code == null)
			throw new TaskHandleException("启动流程时, 流程定义的id或code, 至少有一个不能为空");
		
		this.id = id;
		this.code = code;
		this.version = version;
		this.tenantId = tenantId;
	}
	/**
	 * 设置启动人id
	 * @param userId
	 * @return
	 */
	public StartParameter setUserId(String userId) {
		this.userId = userId;
		return this;
	}
	/**
	 * 设置关联的业务id
	 * @param businessId
	 * @return
	 */
	public StartParameter setBusinessId(String businessId) {
		this.businessId = businessId;
		return this;
	}
	
	/**
	 * 获取启动模式
	 * @return
	 */
	public byte getMode() {
		if(id != null)
			return BY_ID;
		if(version != null)
			return BY_CODE_VERSION;
		return BY_CODE;
	}
	/**
	 * 获取流程定义id
	 * @return
	 */
	public Integer getId() {
		return id;
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
	 * 获取启动人id
	 * @return
	 */
	public String getUserId() {
		return userId;
	}
	/**
	 * 获取关联的业务id
	 * @return
	 */
	public String getBusinessId() {
		return businessId;
	}
	/**
	 * 获取指派实体
	 * @return
	 */
	public AssignEntity getAssignEntity() {
		return assignEntity;
	}
	/**
	 * 获取VariableEntities实例
	 * @return
	 */
	public VariableEntities getVariableEntities() {
		return variableEntities;
	}
}