package com.douglei.bpm.module.runtime.task.command;

import java.util.Arrays;

import com.douglei.bpm.bean.annotation.Autowired;
import com.douglei.bpm.module.Command;
import com.douglei.bpm.module.ExecutionResult;
import com.douglei.bpm.module.runtime.task.Assignee;
import com.douglei.bpm.module.runtime.task.HandleState;
import com.douglei.bpm.module.runtime.task.TaskEntity;
import com.douglei.bpm.process.handler.GeneralExecuteParameter;
import com.douglei.bpm.process.handler.ProcessHandlers;
import com.douglei.bpm.process.handler.components.assignee.AssignerFactory;
import com.douglei.bpm.process.handler.components.assignee.Assigners;
import com.douglei.orm.context.SessionContext;
import com.douglei.tools.utils.StringUtil;

/**
 * 
 * @author DougLei
 */
public class CompleteTaskCmd implements Command {
	private TaskEntity taskEntity;
	private String userId; // 办理人id
	private String[] assigneeUserIds; // 指派的用户id数组
	
	public CompleteTaskCmd(TaskEntity taskEntity, String userId, String... assigneeUserIds) {
		this.taskEntity = taskEntity;
		this.userId = userId;
		this.assigneeUserIds = assigneeUserIds;
	}

	@Autowired
	private ProcessHandlers processHandlers;
	
	@Autowired
	private AssignerFactory assignerFactory;


	@Override
	public ExecutionResult execute() {
		if(taskEntity.supportUserHandling()) {
			if(StringUtil.isEmpty(userId))
				return new ExecutionResult("办理失败, 办理["+taskEntity.getName()+"]任务需要提供userId");
			
			Assignee assignee = SessionContext.getSqlSession().uniqueQuery(Assignee.class, "select id, group_id, handle_state from bpm_ru_assignee where taskinst_id=? and user_id=?", Arrays.asList(taskEntity.getTask().getTaskinstId(), userId));
			if(assignee == null || assignee.getHandleStateInstance() == HandleState.UNCLAIM || assignee.getHandleStateInstance() == HandleState.INVALID)
				return new ExecutionResult("办理失败, 指定的userId未认领["+taskEntity.getName()+"]任务");
			if(assignee.getHandleStateInstance() == HandleState.FINISHED)
				return new ExecutionResult("办理失败, 指定的userId已办理过["+taskEntity.getName()+"]任务");
		}
		return processHandlers.complete(taskEntity.getTaskMetadata(), new GeneralExecuteParameter(taskEntity, new Assigners(assignerFactory.create(assigneeUserIds))));
	}
}
