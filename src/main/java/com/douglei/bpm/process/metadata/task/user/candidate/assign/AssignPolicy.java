package com.douglei.bpm.process.metadata.task.user.candidate.assign;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.douglei.bpm.process.metadata.task.user.DefaultCandidateInstance;

/**
 * 指派策略
 * @author DougLei
 */
public class AssignPolicy implements Serializable{
	private boolean isDynamic; // 是否动态指派
	protected AssignNumber assignNumber; // 最多可指派的人数的表达式
	private List<AssignableUserExpressionEntity> assignableUserExpressionEntities; // 可指派的用户表达式实体集合
	
	public AssignPolicy(boolean isDynamic, AssignNumber assignNumber) {
		this.isDynamic = isDynamic;
		this.assignNumber = assignNumber;
	}
	
	/**
	 * 添加可指派的用户表达式实体实例
	 * @param entity
	 */
	public void addAssignableUserExpressionEntity(AssignableUserExpressionEntity entity) {
		if(assignableUserExpressionEntities == null)
			assignableUserExpressionEntities = new ArrayList<AssignableUserExpressionEntity>();
		assignableUserExpressionEntities.add(entity);
	}

	/**
	 * 是否动态指派
	 * @return
	 */
	public boolean isDynamic() {
		return isDynamic;
	}
	/**
	 * 获取最多可指派的人数的表达式
	 * @return
	 */
	public AssignNumber getAssignNumber() {
		if(assignNumber == null)
			return DefaultCandidateInstance.DEFAULT_ASSIGN_NUMBER_1;
		return assignNumber;
	}
	/**
	 * 获取可指派的用户表达式实体集合
	 * @return
	 */
	public List<AssignableUserExpressionEntity> getAssignableUserExpressionEntities() {
		return assignableUserExpressionEntities;
	}
}
