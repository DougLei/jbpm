package com.douglei.bpm.module.execution.task.command;

import java.util.HashSet;

import com.douglei.bpm.ProcessEngineBeans;
import com.douglei.bpm.module.Command;
import com.douglei.bpm.module.Result;
import com.douglei.bpm.module.execution.task.command.dispatch.impl.SettargetDispatchExecutor;
import com.douglei.bpm.module.execution.task.command.parameter.KillAndJumpTaskParameter;
import com.douglei.bpm.module.execution.task.runtime.Task;
import com.douglei.bpm.module.execution.task.runtime.TaskEntity;
import com.douglei.bpm.process.handler.AbstractTaskHandler;
import com.douglei.bpm.process.handler.GeneralTaskHandleParameter;
import com.douglei.bpm.process.handler.TaskHandleException;
import com.douglei.bpm.process.handler.task.user.assignee.startup.AssigneeHandler;
import com.douglei.bpm.process.mapping.metadata.task.user.candidate.assign.AssignPolicy;
import com.douglei.tools.StringUtil;

/**
 * 
 * @author DougLei
 */
public class KillAndJumpTaskCmd extends AbstractTaskHandler implements Command{
	private TaskEntity entity;
	private KillAndJumpTaskParameter parameter;
	
	public KillAndJumpTaskCmd(TaskEntity entity, KillAndJumpTaskParameter parameter) {
		this.entity = entity;
		this.parameter = parameter;
	}

	@Override
	public Result execute(ProcessEngineBeans processEngineBeans) {
		if(StringUtil.isEmpty(parameter.getUserId()))
			throw new TaskHandleException("KillAndJump["+entity.getName()+"]任务失败,  需要提供userId");

		// 如果是用户任务, 要判断是否存在已经认领的情况, 并根据strict参数值, 决定是返回执行失败的信息, 或进行删除
		Task currentTask = entity.getTask();
		if(entity.isUserTask()) {
			currentTask.deleteAllCC();
			currentTask.deleteAllAssignee();
			currentTask.deleteAllDispatch();
		}
		
		// 创建KillAndJump用办理参数实例
		parameter.getAssignEntity().setAssigneeHandler(new AssigneeHandler() {
			@Override
			protected HashSet<String> validate(AssignPolicy assignPolicy) {
				return handleParameter.getUserEntity().getAssignEntity().getAssignedUserIds();
			}
		});
		GeneralTaskHandleParameter handleParameter = new GeneralTaskHandleParameter(entity, parameter.getUserId(), null, null, null, null, parameter.getAssignEntity());
		
		// kill并完成当前任务
		currentTask.setUserId(parameter.getUserId());
		currentTask.setReason(parameter.getReason());
		completeTask(currentTask, handleParameter.getVariableEntities());
		
		// 进行跳转调度
		new SettargetDispatchExecutor(parameter.getTarget())
			.setParameters(entity.getTaskMetadataEntity(), handleParameter, processEngineBeans)
			.execute();
		return Result.getDefaultSuccessInstance();
	}
}
