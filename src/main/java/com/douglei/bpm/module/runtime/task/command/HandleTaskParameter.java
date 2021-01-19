package com.douglei.bpm.module.runtime.task.command;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.douglei.bpm.module.history.task.Attitude;
import com.douglei.bpm.module.runtime.variable.Scope;
import com.douglei.bpm.process.handler.VariableEntities;

/**
 * 
 * @author DougLei
 */
public class HandleTaskParameter {
	private String businessId; // 业务id
	private String userId; // 办理人id
	
	private String suggest; // 办理人意见
	private Attitude attitude; // 办理人态度
	
	private String reason; // 办理人的办理原因, 即为什么办理; 该值会存储在task表的reason列中; 其和 suggest和attitude是互斥的; 用来表示特殊的任务办理, 例如网关的办理, 结束事件的办理, 流程跳转等
	
	private List<String> assignUserIds; // 下一环节的办理人id集合
	private VariableEntities variableEntities = new VariableEntities(); // 流程变量
	
	/**
	 * 设置业务id
	 * @param businessId
	 * @return
	 */
	public HandleTaskParameter setBusinessId(String businessId) {
		this.businessId = businessId;
		return this;
	}
	
	/**
	 * 设置办理人id
	 * @param userId
	 * @return
	 */
	public HandleTaskParameter setUserId(String userId) {
		this.userId = userId;
		return this;
	}
	
	/**
	 * 设置办理人意见
	 * @param suggest
	 * @return
	 */
	public HandleTaskParameter setSuggest(String suggest) {
		this.suggest = suggest;
		return this;
	}

	/**
	 * 设置办理人态度
	 * @param attitude
	 * @return
	 */
	public HandleTaskParameter setAttitude(Attitude attitude) {
		this.attitude = attitude;
		return this;
	}
	
	/**
	 * 设置办理人的办理原因
	 * @param reason
	 * @return
	 */
	public HandleTaskParameter setReason(String reason) {
		this.reason = reason;
		return this;
	}
	
	/**
	 * 添加下一环节的办理人id
	 * @param assignUserId
	 * @return
	 */
	public HandleTaskParameter addAssignUserId(String assignUserId) {
		if(assignUserIds == null)
			assignUserIds = new ArrayList<String>();
		if(assignUserIds.isEmpty() || !assignUserIds.contains(assignUserId))
			assignUserIds.add(assignUserId);
		return this;
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
	 * 获取办理人的办理原因
	 * @return
	 */
	public String getReason() {
		return reason;
	}
	/**
	 * 获取下一环节的办理人id集合
	 * @return
	 */
	public List<String> getAssignUserIds() {
		return assignUserIds;
	}
	/**
	 * 获取VariableEntities实例
	 * @return
	 */
	public VariableEntities getVariableEntities() {
		return variableEntities;
	}
}
