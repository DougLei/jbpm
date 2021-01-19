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
	private String target; // 跳转的目标任务id
	private String userId; // 进行跳转的用户id
	private String reason; // 跳转原因
	private List<String> assignedUserIds; // 指派的用户id集合(目标任务)
	
	public JumpTaskCmd(TaskInstance taskInstance, String target, String userId, String reason, List<String> assignedUserIds) {
		this.taskInstance = taskInstance;
		this.target = target;
		this.userId = userId;
		this.reason = reason;
		this.assignedUserIds = assignedUserIds;
	}

	@Override
	public ExecutionResult execute(ProcessEngineBeans processEngineBeans) {
		if(StringUtil.isEmpty(userId))
			throw new TaskHandleException("跳转失败, 跳转["+taskInstance.getName()+"]任务, 需要提供userId");

		TaskMetadataEntity<TaskMetadata> targetTask = taskInstance.getProcessMetadata().getTaskMetadataEntity(target);
		
		ExecutionResult result = completeCurrentTask();
		if(result.isSuccess())
			result = targetTaskStartup(targetTask);
		return result;
	}
	
	/**
	 * 完成当前任务
	 * @return
	 */
	private ExecutionResult completeCurrentTask() {
		// 判断当前任务是否还存在
		
		
		
		
		// 结束当前任务
		
		
		
		
		
		return ExecutionResult.getDefaultSuccessInstance();
	}

	/**
	 * 启动目标任务
	 * @param targetTask
	 * @return
	 */
	private ExecutionResult targetTaskStartup(TaskMetadataEntity<TaskMetadata> targetTask) {
		// TODO Auto-generated method stub
		
		return ExecutionResult.getDefaultSuccessInstance();
	}
}
