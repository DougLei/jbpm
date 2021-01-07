package com.douglei.bpm.process.metadata.task.user.candidate.assign;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 指派策略
 * @author DougLei
 */
public class AssignPolicy implements Serializable{
	private AssignMode mode; // 指派模式
	private AssignNumber assignNumber; // 可指派的人数的表达式
	private List<AssignableUserExpressionEntity> assignableUserExpressionEntities; // 可指派的用户表达式实体集合
	
	public AssignPolicy(AssignMode mode, AssignNumber assignNumber) {
		this.mode = mode;
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
}
