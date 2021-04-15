package com.douglei.bpm.module.runtime.task.command;

import java.util.Arrays;

import com.douglei.bpm.ProcessEngineBeans;
import com.douglei.bpm.module.Command;
import com.douglei.bpm.module.ExecutionResult;
import com.douglei.bpm.module.runtime.task.HandleState;
import com.douglei.bpm.module.runtime.task.Task;
import com.douglei.bpm.module.runtime.task.TaskEntity;
import com.douglei.bpm.module.runtime.task.command.dispatch.impl.SettargetDispatchExecutor;
import com.douglei.bpm.module.runtime.task.command.parameter.KillAndJumpTaskParameter;
import com.douglei.bpm.process.handler.GeneralTaskHandleParameter;
import com.douglei.bpm.process.handler.GeneralTaskHandler;
import com.douglei.bpm.process.handler.TaskHandleException;
import com.douglei.orm.context.SessionContext;
import com.douglei.tools.StringUtil;

/**
 * 
 * @author DougLei
 */
public class KillAndJumpTaskCmd extends GeneralTaskHandler implements Command{
	private TaskEntity entity;
	private KillAndJumpTaskParameter parameter;
	
	public KillAndJumpTaskCmd(TaskEntity entity, KillAndJumpTaskParameter parameter) {
		this.entity = entity;
		this.parameter = parameter;
	}

	@Override
	public ExecutionResult execute(ProcessEngineBeans processEngineBeans) {
		if(StringUtil.isEmpty(parameter.getUserId()))
			throw new TaskHandleException("KillAndJump["+entity.getName()+"]任务失败,  需要提供userId");

		// 如果是用户任务, 要判断是否存在已经认领的情况, 并根据strict参数值, 决定是返回执行失败的信息, 或进行删除
		Task currentTask = entity.getTask();
		if(entity.isUserTask()) {
			int claimedCount = Integer.parseInt(SessionContext.getSqlSession().uniqueQuery_(
					"select count(id) from bpm_ru_assignee where taskinst_id=? and handle_state=?", 
					Arrays.asList(currentTask.getTaskinstId(), HandleState.CLAIMED.name()))[0].toString());
			if(claimedCount > 0 && !parameter.isStrict())
				return new ExecutionResult("KillAndJump失败, [%s]任务存在%d条已认领的指派信息, 如确实需要, 请先处理相关数据", "jbpm.kill.jump.fail.claimed.assignee.exists", entity.getName(), claimedCount);
			
			currentTask.deleteAllAssignee();
			currentTask.deleteAllDispatch();
		}
		
		// 创建KillAndJump用办理参数实例
		GeneralTaskHandleParameter handleParameter = new GeneralTaskHandleParameter(entity, parameter.getUserId(), null, null, null, null, null);
		
		// kill并完成当前任务
		currentTask.setUserId(parameter.getUserId());
		currentTask.setReason(parameter.getReason());
		completeTask(currentTask, handleParameter.getVariableEntities());
		
		// 进行跳转调度
		new SettargetDispatchExecutor(parameter.getTargetTask())
			.initParameters(entity.getTaskMetadataEntity(), handleParameter, parameter.getAssignedUserIds(), processEngineBeans)
			.execute();
		return ExecutionResult.getDefaultSuccessInstance();
	}
}
