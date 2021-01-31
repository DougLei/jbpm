package com.douglei.bpm.module.runtime.task.command;

import java.util.Arrays;
import java.util.List;

import com.douglei.bpm.ProcessEngineBeans;
import com.douglei.bpm.module.Command;
import com.douglei.bpm.module.ExecutionResult;
import com.douglei.bpm.module.runtime.task.HandleState;
import com.douglei.bpm.module.runtime.task.Task;
import com.douglei.bpm.module.runtime.task.TaskInstance;
import com.douglei.bpm.module.runtime.task.command.dispatch.impl.SettargetDispatchExecutor;
import com.douglei.bpm.process.handler.GeneralHandleParameter;
import com.douglei.bpm.process.handler.GeneralTaskHandler;
import com.douglei.bpm.process.handler.TaskHandleException;
import com.douglei.orm.context.SessionContext;
import com.douglei.tools.StringUtil;

/**
 * 跳转
 * @author DougLei
 */
public class JumpTaskCmd extends GeneralTaskHandler implements Command{
	private TaskInstance taskInstance;
	private String targetTask; // 跳转的目标任务id
	private String userId; // 进行跳转的用户id
	private String reason; // 跳转原因
	private List<String> assignedUserIds; // 指派的用户id集合
	private boolean strict; // 如果存在已经认领的指派信息, 跳转时是否强制删除相关数据; 建议传入false
	private GeneralHandleParameter handleParameter;
	
	public JumpTaskCmd(TaskInstance taskInstance, String targetTask, String userId, String reason, List<String> assignedUserIds, boolean strict) {
		this.taskInstance = taskInstance;
		this.targetTask = targetTask;
		this.userId = userId;
		this.reason = reason;
		this.assignedUserIds = assignedUserIds;
		this.strict = strict;
	}

	@Override
	public ExecutionResult execute(ProcessEngineBeans processEngineBeans) {
		if(StringUtil.isEmpty(userId))
			throw new TaskHandleException("跳转失败, 跳转["+taskInstance.getName()+"]任务, 需要提供userId");

		// 如果是用户任务, 要判断是否存在已经认领的情况, 并根据strict参数值, 决定是返回执行失败的信息, 或进行删除
		Task currentTask = taskInstance.getTask();
		if(taskInstance.isUserTask()) {
			int claimedCount = Integer.parseInt(SessionContext.getSqlSession().uniqueQuery_(
					"select count(id) from bpm_ru_assignee where taskinst_id=? and handle_state=?", 
					Arrays.asList(currentTask.getTaskinstId(), HandleState.CLAIMED.name()))[0].toString());
			if(claimedCount > 0 && !strict)
				return new ExecutionResult("跳转失败, [%s]任务存在%d条已认领的指派信息, 如确实需要跳转, 请先处理相关数据", "jbpm.jump.fail.claimed.assignee.exists", taskInstance.getName(), claimedCount);
			
			currentTask.deleteAllAssignee();
			currentTask.deleteAllDispatch();
		}
		
		// 创建跳转用办理参数实例
		this.handleParameter = new GeneralHandleParameter(
				taskInstance, 
				processEngineBeans.getUserBeanFactory().create(userId), 
				null, null, null, null, null);
		
		// 完成当前任务
		currentTask.setUserId(userId);
		currentTask.setReason(reason);
		completeTask(currentTask, handleParameter.getCurrentDate(), handleParameter.getVariableEntities());
		
		// 进行跳转调度
		new JumpDispatchExecutor(targetTask)
			.setParameters(taskInstance.getTaskMetadataEntity(), handleParameter, assignedUserIds, processEngineBeans)
			.execute();
		return ExecutionResult.getDefaultSuccessInstance();
	}
	
	// 跳转调度
	class JumpDispatchExecutor extends SettargetDispatchExecutor{
		public JumpDispatchExecutor(String targetTask) { 
			super(targetTask, false, false);
		}
	}
}
