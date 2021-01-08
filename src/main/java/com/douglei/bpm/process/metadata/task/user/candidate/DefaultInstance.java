package com.douglei.bpm.process.metadata.task.user.candidate;

import com.douglei.bpm.process.metadata.task.user.candidate.assign.AssignNumber;
import com.douglei.bpm.process.metadata.task.user.candidate.handle.HandleNumber;
import com.douglei.bpm.process.metadata.task.user.candidate.handle.HandlePolicy;

/**
 * 默认的实例
 * @author DougLei
 */
public class DefaultInstance {
	
	/**
	 * 默认的最多可指派的人数表达式
	 */
	public static final AssignNumber DEFAULT_ASSIGN_NUMBER = new AssignNumber(1, false, false);
	
	/**
	 * 默认的办理策略
	 */
	public static final HandlePolicy DEFAULT_HANDLE_POLICY = new HandlePolicy(false, false, null);
	
	/**
	 * 默认的可办理的人数表达式
	 */
	public static final HandleNumber DEFAULT_HANDLE_NUMBER = new HandleNumber(100, true, false);
}
