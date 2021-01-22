package com.douglei.bpm.module.runtime.task.command;

import java.util.List;

import com.douglei.bpm.ProcessEngineBeans;
import com.douglei.bpm.module.Command;
import com.douglei.bpm.module.ExecutionResult;
import com.douglei.bpm.module.runtime.task.Task;
import com.douglei.bpm.module.runtime.task.TaskInstance;
import com.douglei.bpm.module.runtime.task.command.dispatch.impl.SettargetDispatchExecutor;
import com.douglei.bpm.process.handler.GeneralHandleParameter;
import com.douglei.bpm.process.handler.GeneralTaskHandler;
import com.douglei.bpm.process.handler.TaskHandleException;
import com.douglei.bpm.process.metadata.TaskMetadata;
import com.douglei.bpm.process.metadata.TaskMetadataEntity;
import com.douglei.tools.utils.StringUtil;

/**
 * 跳转
 * @author DougLei
 */
public class JumpTaskCmd extends GeneralTaskHandler implements Command{
	private TaskInstance taskInstance;
	private String targetTask; // 跳转的目标任务id
	private String userId; // 进行跳转的用户id
	private String reason; // 跳转原因
	private boolean executeCC;
	private boolean activateLastAssigneeList;
	private List<String> assignedUserIds; // 指派的用户id集合
	private GeneralHandleParameter handleParameter;
	
	public JumpTaskCmd(TaskInstance taskInstance, String targetTask, String userId, String reason, boolean executeCC, boolean activateLastAssigneeList, List<String> assignedUserIds) {
		this.taskInstance = taskInstance;
		this.targetTask = targetTask;
		this.userId = userId;
		this.reason = reason;
		this.executeCC = executeCC;
		this.activateLastAssigneeList = activateLastAssigneeList;
		this.assignedUserIds = assignedUserIds;
	}

	@Override
	public ExecutionResult execute(ProcessEngineBeans processEngineBeans) {
		if(StringUtil.isEmpty(userId))
			throw new TaskHandleException("跳转失败, 跳转["+taskInstance.getName()+"]任务, 需要提供userId");

		// TODO 判断能不能跳转
		
		
		// 创建跳转用办理参数实例
		this.handleParameter = new GeneralHandleParameter(
				taskInstance, 
				processEngineBeans.getUserBeanFactory().create(userId), 
				null, null, null, null, null);
		
		// 完成当前任务
		Task task = taskInstance.getTask();
		task.setUserId(userId);
		task.setReason(reason);
		completeTask(task, handleParameter.getCurrentDate(), handleParameter.getVariableEntities());
		
		task.deleteAllAssignee();
		task.deleteAllDispatch();
		
		
		// 进行调度
		new JumpDispatchExecutor(targetTask, activateLastAssigneeList, executeCC, 
				taskInstance.getTaskMetadataEntity(), handleParameter, assignedUserIds, processEngineBeans).execute();
		return ExecutionResult.getDefaultSuccessInstance();
	}
	
	// 跳转调度
	class JumpDispatchExecutor extends SettargetDispatchExecutor{
		public JumpDispatchExecutor(String targetTask, boolean activateLastAssigneeList, boolean executeCC, 
				TaskMetadataEntity<? extends TaskMetadata> currentUserTaskMetadataEntity, GeneralHandleParameter handleParameter, List<String> assignedUserIds, ProcessEngineBeans processEngineBeans) {
			super(targetTask, activateLastAssigneeList, executeCC);
			setParameters(currentUserTaskMetadataEntity, handleParameter, assignedUserIds, processEngineBeans);
		}
	}
}
