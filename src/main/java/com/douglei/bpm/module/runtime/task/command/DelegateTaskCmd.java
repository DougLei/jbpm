package com.douglei.bpm.module.runtime.task.command;

import com.douglei.bpm.ProcessEngineBeans;
import com.douglei.bpm.module.Command;
import com.douglei.bpm.module.ExecutionResult;
import com.douglei.bpm.module.runtime.task.TaskInstance;

/**
 * 委托任务
 * @author DougLei
 */
public class DelegateTaskCmd implements Command {
	private TaskInstance taskInstance;
	private String userId; // 发起委托的用户id
	private String assignedUserId; // 接受委托的用户id
	
	public DelegateTaskCmd(TaskInstance taskInstance, String userId, String assignedUserId) {
		this.taskInstance = taskInstance;
		this.userId = userId;
		this.assignedUserId = assignedUserId;
	}

	@Override
	public ExecutionResult execute(ProcessEngineBeans processEngineBeans) {
		
		// 有没有认领任务
		// 被委托的人在不在
		
		
		
		
		
		
		
		
		return null;
	}
}
