package com.douglei.bpm.module.execution.task.command.dispatch;

import java.util.HashSet;

import com.douglei.bpm.ProcessEngineBeans;
import com.douglei.bpm.process.handler.GeneralTaskHandleParameter;
import com.douglei.bpm.process.handler.TaskDispatchException;
import com.douglei.bpm.process.mapping.metadata.TaskMetadata;
import com.douglei.bpm.process.mapping.metadata.TaskMetadataEntity;
import com.douglei.bpm.process.mapping.metadata.TaskNotExistsException;

/**
 * 调度执行器
 * @author DougLei
 */
public abstract class DispatchExecutor {
	protected TaskMetadataEntity<? extends TaskMetadata> currentTaskMetadataEntity; // 当前任务的元数据实例
	protected GeneralTaskHandleParameter handleParameter; // 办理参数
	protected HashSet<String> assignedUserIds; // 实际指派的用户id集合
	protected ProcessEngineBeans processEngineBeans;
	
	/**
	 * 初始化调度执行器
	 * @param currentTaskMetadataEntity
	 * @param handleParameter
	 * @param assignedUserIds 实际指派的用户id集合
	 * @param processEngineBeans
	 * @return
	 */
	public final DispatchExecutor initParameters(TaskMetadataEntity<? extends TaskMetadata> currentTaskMetadataEntity, GeneralTaskHandleParameter handleParameter, HashSet<String> assignedUserIds, ProcessEngineBeans processEngineBeans) {
		this.currentTaskMetadataEntity = currentTaskMetadataEntity;
		this.handleParameter = handleParameter;
		this.assignedUserIds = assignedUserIds;
		this.processEngineBeans = processEngineBeans;
		return this;
	}
	
	/**
	 * 设置实际指派的用户集合
	 * @param assignedUserIds
	 */
	protected final void setAssignedUsers(HashSet<String> assignedUserIds) {
		if(assignedUserIds != null && !assignedUserIds.isEmpty())
			handleParameter.getUserEntity().appendAssignedUserIds(assignedUserIds);
	}
	
	/**
	 * 进行调度
	 * @throws TaskNotExistsException
	 * @throws TaskDispatchException
	 */
	public abstract void execute() throws TaskNotExistsException, TaskDispatchException;
}
