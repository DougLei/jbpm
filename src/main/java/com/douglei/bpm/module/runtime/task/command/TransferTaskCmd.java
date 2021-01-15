package com.douglei.bpm.module.runtime.task.command;

import com.douglei.bpm.module.runtime.task.TaskInstance;

/**
 * 转办任务
 * @author DougLei
 */
public class TransferTaskCmd extends DelegateTaskCmd {

	public TransferTaskCmd(TaskInstance taskInstance, String userId, String assignedUserId) {
		super(taskInstance, userId, assignedUserId);
	}
}
