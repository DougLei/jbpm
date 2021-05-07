package com.douglei.bpm.module.execution.task.command.dispatch;

import java.util.HashSet;

import com.douglei.bpm.ProcessEngineBeans;
import com.douglei.bpm.process.handler.AbstractHandleParameter;
import com.douglei.bpm.process.handler.AssignEntity;
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
	protected ProcessEngineBeans processEngineBeans;
	
	/**
	 * 设置调度执行器参数
	 * @param assignedUserIds 实际指派的用户id集合
	 * @param currentTaskMetadataEntity 当前任务的元数据实例
	 * @param handleParameter 办理参数
	 * @param processEngineBeans
	 * @return
	 */
	public final DispatchExecutor setParameters(TaskMetadataEntity<? extends TaskMetadata> currentTaskMetadataEntity, AbstractHandleParameter handleParameter, ProcessEngineBeans processEngineBeans) {
		this.currentTaskMetadataEntity = currentTaskMetadataEntity;
		this.handleParameter = handleParameter;
		this.processEngineBeans = processEngineBeans;
		return this;
	}
	
	/**
	 * 解析调度结果
	 * @param assignedUserIds 实际指派的用户id集合
	 * @return
	 */
	protected abstract DispatchResult parse(HashSet<String> assignedUserIds);
	
	/**
	 * 进行调度
	 * @throws TaskNotExistsException
	 * @throws TaskDispatchException
	 */
	public final void execute() throws TaskNotExistsException, TaskDispatchException {
		AssignEntity assignEntity = handleParameter.getUserEntity().getAssignEntity();
		DispatchResult result = parse(assignEntity.getAssignedUserIds());
		
		// 调整指派的用户id集合
		if(result.assignedUserIds != assignEntity.getAssignedUserIds())
			assignEntity.setAssignedUserIds(result.assignedUserIds);
		
		// 进行任务调度
		handleParameter.getTaskEntityHandler().dispatch();
		processEngineBeans.getTaskHandleUtil().startup(
				handleParameter.getProcessMetadata().getTaskMetadataEntity(result.target), handleParameter);
	}
	
	/**
	 * 
	 * @author DougLei
	 */
	protected final class DispatchResult {
		private String target;
		private HashSet<String> assignedUserIds;
		
		/**
		 * 
		 * @param target 目标任务
		 * @param assignedUserIds 指派的用户id集合
		 */
		public DispatchResult(String target, HashSet<String> assignedUserIds) {
			this.target = target;
			this.assignedUserIds = assignedUserIds;
		}
	}
}
