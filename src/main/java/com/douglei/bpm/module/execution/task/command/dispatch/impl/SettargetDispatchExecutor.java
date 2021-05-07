package com.douglei.bpm.module.execution.task.command.dispatch.impl;

import java.util.HashSet;

import com.douglei.bpm.module.execution.task.command.dispatch.DispatchExecutor;

/**
 * 设置目标调度
 * @author DougLei
 */
public class SettargetDispatchExecutor extends DispatchExecutor {
	private String target; // 目标任务
	
	public SettargetDispatchExecutor(String target) {
		this.target = target;
	}
	
	@Override
	protected DispatchResult parse(HashSet<String> assignedUserIds) {
		return new DispatchResult(target, assignedUserIds);
	}
}
