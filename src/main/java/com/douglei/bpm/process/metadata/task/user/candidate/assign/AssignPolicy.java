package com.douglei.bpm.process.metadata.task.user.candidate.assign;

import java.io.Serializable;
import java.util.List;

/**
 * 指派策略
 * @author DougLei
 */
public class AssignPolicy implements Serializable{
	private AssignMode mode; // 指派模式
	private AssignNumberExpression assignNumberExpression; // 可指派的人数的表达式
	private List<AssignUserExpression> assignUserExpressions; // 指派用户的表达式集合
	
}
