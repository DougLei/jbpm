package com.douglei.bpm.module.execution.task.command;

import java.util.List;

import com.douglei.bpm.module.execution.task.AssignMode;
import com.douglei.bpm.module.execution.task.runtime.Assignee;
import com.douglei.bpm.module.execution.task.runtime.TaskEntity;
import com.douglei.bpm.process.api.user.option.OptionTypeConstants;
import com.douglei.orm.context.SessionContext;

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
	protected void giveupTask(List<Assignee> assigneeList) {
		for (Assignee assignee : assigneeList) {
			SessionContext.getSQLSession().executeUpdate("Assignee", "giveupTask4Transfer", assignee);
		}
	}
}
