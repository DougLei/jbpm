package com.douglei.bpm.module.runtime.task.command.dispatch.impl;

import com.douglei.bpm.process.handler.TaskDispatchException;

/**
 * 回退步数调度
 * @author DougLei
 */
public class BackstepsDispatchExecutor extends SettargetDispatchExecutor {
	private int steps; // 回退的步数, 默认值为1; 超过最大步数时修正为最大步数

	public BackstepsDispatchExecutor(boolean executeCC) {
		super(executeCC);
		this.steps = 1;
	}
	public BackstepsDispatchExecutor(int steps, boolean executeCC) {
		super(executeCC);
		if(steps < 1)
			throw new TaskDispatchException("回退步数调度时, 设置的步数值不能小于1");
		this.steps = steps;
	}

	@Override
	public void execute() throws TaskDispatchException{
		// TODO Auto-generated method stub
		
		
		
	}
}
