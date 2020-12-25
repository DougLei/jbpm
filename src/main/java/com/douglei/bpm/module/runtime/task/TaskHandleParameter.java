package com.douglei.bpm.module.runtime.task;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.douglei.bpm.module.runtime.variable.Scope;
import com.douglei.bpm.process.handler.VariableEntities;

/**
 * 
 * @author DougLei
 */
public class TaskHandleParameter {
	private String businessId; // 业务id
	private String userId; // 办理人id
	private String suggest; // 办理人意见
	private Attitude attitude; // 办理人态度
	private List<String> assignedUserIds; // 下一环节的办理人id集合
	private VariableEntities variableEntities = new VariableEntities(); // 流程变量
	
	/**
	 * 设置业务id
	 * @param businessId
	 * @return
	 */
	public TaskHandleParameter setBusinessId(String businessId) {
		this.businessId = businessId;
		return this;
	}
	
	/**
	 * 设置办理人id
	 * @param userId
	 * @return
	 */
	public TaskHandleParameter setUserId(String userId) {
		this.userId = userId;
		return this;
	}
	
	/**
	 * 设置办理人意见
	 * @param suggest
	 * @return
	 */
	public TaskHandleParameter setSuggest(String suggest) {
		this.suggest = suggest;
		return this;
	}

	/**
	 * 设置办理人态度
	 * @param attitude
	 * @return
	 */
	public TaskHandleParameter setAttitude(Attitude attitude) {
		this.attitude = attitude;
		return this;
	}

	/**
	 * 添加下一环节的办理人id
	 * @param assignedUserId
	 * @return
	 */
	public TaskHandleParameter addAssignedUserId(String assignedUserId) {
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
	public TaskHandleParameter addVariable(String name, Object value) {
		return addVariable(name, Scope.GLOBAL, value);
	}
	/**
	 * 添加变量
	 * @param name
	 * @param scope
	 * @param value
	 * @return
	 */
	public TaskHandleParameter addVariable(String name, Scope scope, Object value) {
		variableEntities.addVariable(name, scope, value);
		return this;
	}
	/**
	 * 添加变量, 默认是global范围
	 * @param variables
	 * @return
	 */
	public TaskHandleParameter addVariables(Map<String, Object> variables) {
		return addVariables(Scope.GLOBAL, variables);
	}
	/**
	 * 添加变量
	 * @param scope
	 * @param variables
	 * @return
	 */
	public TaskHandleParameter addVariables(Scope scope, Map<String, Object> variables) {
		variableEntities.addVariables(scope, variables);
		return this;
	}
	/**
	 * 添加变量
	 * @param object
	 * @return
	 */
	public TaskHandleParameter addVariables(Object object) {
		variableEntities.addVariables(object);
		return this;
	}
	
	/**
	 * 获取业务id
	 * @return
	 */
	public String getBusinessId() {
		return businessId;
	}
	/**
	 * 获取办理人id
	 * @return
	 */
	public String getUserId() {
		return userId;
	}
	/**
	 * 获取办理人意见
	 * @return
	 */
	public String getSuggest() {
		return suggest;
	}
	/**
	 * 获取办理人态度
	 * @return
	 */
	public Attitude getAttitude() {
		return attitude;
	}
	/**
	 * 获取下一环节的办理人id集合
	 * @return
	 */
	public List<String> getAssignedUserIds() {
		return assignedUserIds;
	}
	/**
	 * 获取VariableEntities实例
	 * @return
	 */
	public VariableEntities getVariableEntities() {
		return variableEntities;
	}
}
