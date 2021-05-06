package com.douglei.bpm.module.execution.task.command.dispatch;

import java.util.HashSet;

import com.douglei.bpm.ProcessEngineBeans;
import com.douglei.bpm.process.handler.AbstractHandleParameter;
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
	protected AbstractHandleParameter handleParameter; // 办理参数
	protected HashSet<String> assignedUserIds; // 实际指派的用户id集合
	protected ProcessEngineBeans processEngineBeans;
	
	/**
	 * 设置调度执行器参数
	 * @param currentTaskMetadataEntity
	 * @param handleParameter
	 * @param assignedUserIds 实际指派的用户id集合
	 * @param processEngineBeans
	 * @return
	 */
	public final DispatchExecutor setParameters(TaskMetadataEntity<? extends TaskMetadata> currentTaskMetadataEntity, AbstractHandleParameter handleParameter, HashSet<String> assignedUserIds, ProcessEngineBeans processEngineBeans) {
		this.currentTaskMetadataEntity = currentTaskMetadataEntity;
		this.handleParameter = handleParameter;
		this.assignedUserIds = assignedUserIds;
		this.processEngineBeans = processEngineBeans;
		return this;
	}
	
	/**
	 * 解析调度结果
	 * @return
	 */
	protected abstract DispatchResult parse();
	
	/**
	 * 进行调度
	 * @throws TaskNotExistsException
	 * @throws TaskDispatchException
	 */
	public final void execute() throws TaskNotExistsException, TaskDispatchException {
		DispatchResult result = parse();
		
		// 设置指派的用户id集合
		if(result.assignedUserIds != null && !result.assignedUserIds.isEmpty())
			handleParameter.getUserEntity().appendAssignedUserIds(result.assignedUserIds);
		
		// 进行任务调度
		handleParameter.getTaskEntityHandler().dispatch();
		processEngineBeans.getTaskHandleUtil().startup(
				handleParameter.getProcessMetadata().getTaskMetadataEntity(result.target), handleParameter);
	}
	
	/**
	 * 调度结果
	 * @author DougLei
	 */
	protected class DispatchResult {
		private String target; // 目标任务
		private HashSet<String> assignedUserIds; // 实际指派的用户id集合
		
		public DispatchResult(String target, HashSet<String> assignedUserIds) {
			this.target = target;
			this.assignedUserIds = assignedUserIds;
		}
	}
}
