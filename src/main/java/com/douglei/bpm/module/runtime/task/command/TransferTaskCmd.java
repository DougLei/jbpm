package com.douglei.bpm.module.runtime.task.command;

import com.douglei.bpm.module.runtime.task.AssignMode;
import com.douglei.bpm.module.runtime.task.HandleState;
import com.douglei.bpm.module.runtime.task.TaskInstance;
import com.douglei.bpm.process.api.user.option.OptionTypeConstants;

/**
 * 转办任务
 * @author DougLei
 */
public class TransferTaskCmd extends DelegateTaskCmd {

	public TransferTaskCmd(TaskInstance taskInstance, String userId, String assignedUserId, String remark) {
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
	protected String getAssignModeName() {
		return "转办";
	}

	@Override
	protected HandleState targetHandleState() {
		return HandleState.INVALID;
	}
}
