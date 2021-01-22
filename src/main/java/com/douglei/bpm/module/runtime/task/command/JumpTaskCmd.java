package com.douglei.bpm.module.runtime.task.command;

import java.util.List;

import com.douglei.bpm.ProcessEngineBeans;
import com.douglei.bpm.module.Command;
import com.douglei.bpm.module.ExecutionResult;
import com.douglei.bpm.module.runtime.task.TaskInstance;
import com.douglei.bpm.process.handler.TaskHandleException;
import com.douglei.bpm.process.metadata.TaskMetadata;
import com.douglei.bpm.process.metadata.TaskMetadataEntity;
import com.douglei.tools.utils.StringUtil;

/**
 * 跳转
 * @author DougLei
 */
public class JumpTaskCmd implements Command{
	private TaskInstance taskInstance;
	private String targetTask; // 跳转的目标任务id
	private String userId; // 进行跳转的用户id
	private String reason; // 跳转原因
	private List<String> assignedUserIds; // 指派的用户id集合(目标任务)
	
	public JumpTaskCmd(TaskInstance taskInstance, String targetTask, String userId, String reason, List<String> assignedUserIds) {
		this.taskInstance = taskInstance;
		this.targetTask = targetTask;
		this.userId = userId;
		this.reason = reason;
		this.assignedUserIds = assignedUserIds;
	}

	@Override
	public ExecutionResult execute(ProcessEngineBeans processEngineBeans) {
		if(StringUtil.isEmpty(userId))
			throw new TaskHandleException("跳转失败, 跳转["+taskInstance.getName()+"]任务, 需要提供userId");

		TaskMetadataEntity<TaskMetadata> targetTaskMetadataEntity = taskInstance.getProcessMetadata().getTaskMetadataEntity(targetTask);
		
		ExecutionResult result = completeCurrentTask();
		if(result.isSuccess())
			result = dispatchTargetTask(targetTaskMetadataEntity);
		return result;
	}
	
	/**
	 * 完成当前任务
	 * @return
	 */
	private ExecutionResult completeCurrentTask() {
		// 有没有人认领了还未办理完成的: 如果删除了, 在办理那里尝试抛出异常
		
		
		
		// 结束当前任务
		
		
		
		
		
		return ExecutionResult.getDefaultSuccessInstance();
	}

	/**
	 * 调度目标任务
	 * @param targetTaskMetadataEntity
	 * @return
	 */
	private ExecutionResult dispatchTargetTask(TaskMetadataEntity<TaskMetadata> targetTaskMetadataEntity) {
		// TODO Auto-generated method stub
		
		return ExecutionResult.getDefaultSuccessInstance();
	}
}
