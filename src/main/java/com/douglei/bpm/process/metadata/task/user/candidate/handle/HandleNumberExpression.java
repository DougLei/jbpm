package com.douglei.bpm.process.metadata.task.user.candidate.handle;

import com.douglei.bpm.process.metadata.task.user.candidate.assign.AssignNumberExpression;

/**
 * 可办理的人数的表达式
 * @author DougLei
 */
public class HandleNumberExpression extends AssignNumberExpression{

	public HandleNumberExpression(int number, boolean percent, boolean ceiling) {
		super(number, percent, ceiling);
	}
}
