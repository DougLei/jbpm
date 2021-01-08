package com.douglei.bpm.process.metadata.task.user.candidate.handle;

import com.douglei.bpm.process.metadata.task.user.candidate.assign.AssignNumber;

/**
 * 可办理的人数的表达式
 * @author DougLei
 */
public class HandleNumber extends AssignNumber{

	public HandleNumber(int number, boolean percent, boolean ceiling) {
		super(number, percent, ceiling);
	}
	
	@Override
	public String toString() {
		return "HandleNumber [number=" + number + ", percent=" + percent + ", ceiling=" + ceiling + "]";
	}
}
