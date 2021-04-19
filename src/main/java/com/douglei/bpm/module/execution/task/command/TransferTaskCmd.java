package com.douglei.bpm.module.execution.task.command;

import com.douglei.bpm.module.execution.task.AssignMode;
import com.douglei.bpm.module.execution.task.HandleState;
import com.douglei.bpm.module.execution.task.runtime.TaskEntity;
import com.douglei.bpm.process.api.user.option.OptionTypeConstants;

/**
 * 转办任务
 * @author DougLei
 */
public class TransferTaskCmd extends DelegateTaskCmd {

	public TransferTaskCmd(TaskEntity taskInstance, String userId, String assignedUserId, String remark) {
		super(taskInstance, userId, assignedUserId, remark);
	}

	@Override
	protected String getOptionType() {
		return OptionTypeConstants.TRANSFER;
	}

	@Override
	protected AssignMode getAssignMode() {
		return AssignMode.TRANSFERRED;
	}

	@Override
	protected HandleState targetHandleState() {
		return HandleState.INVALID;
	}
}
