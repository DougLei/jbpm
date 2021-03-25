package com.douglei.bpm.module.runtime.task.command.dispatch;

import java.util.List;

import com.douglei.bpm.ProcessEngineBeans;
import com.douglei.bpm.module.runtime.task.command.CarbonCopyTaskCmd;
import com.douglei.bpm.process.api.user.option.OptionTypeConstants;
import com.douglei.bpm.process.handler.GeneralHandleParameter;
import com.douglei.bpm.process.handler.TaskDispatchException;
import com.douglei.bpm.process.mapping.metadata.TaskMetadata;
import com.douglei.bpm.process.mapping.metadata.TaskMetadataEntity;
import com.douglei.bpm.process.mapping.metadata.TaskNotExistsException;
import com.douglei.bpm.process.mapping.metadata.task.user.UserTaskMetadata;
import com.douglei.bpm.process.mapping.metadata.task.user.option.Option;
import com.douglei.bpm.process.mapping.metadata.task.user.option.carboncopy.CarbonCopyOption;

/**
 * 调度执行器
 * @author DougLei
 */
public abstract class DispatchExecutor {
	protected TaskMetadataEntity<? extends TaskMetadata> currentTaskMetadataEntity; // 当前任务的元数据实例
	protected GeneralHandleParameter handleParameter; // 办理参数
	protected List<String> assignedUserIds; // 实际指派的用户id集合
	protected ProcessEngineBeans processEngineBeans;
	
	/**
	 * 初始化调度执行器的必要参数
	 * @param currentTaskMetadataEntity
	 * @param handleParameter
	 * @param assignedUserIds 实际指派的用户id集合
	 * @param processEngineBeans
	 * @return
	 */
	public final DispatchExecutor initParameters(TaskMetadataEntity<? extends TaskMetadata> currentTaskMetadataEntity, GeneralHandleParameter handleParameter, List<String> assignedUserIds, ProcessEngineBeans processEngineBeans) {
		this.currentTaskMetadataEntity = currentTaskMetadataEntity;
		this.handleParameter = handleParameter;
		this.assignedUserIds = assignedUserIds;
		this.processEngineBeans = processEngineBeans;
		return this;
	}
	
	/**
	 * 进行抄送
	 */
	protected final void executeCarbonCopy() {
		if(!currentTaskMetadataEntity.getTaskMetadata().isUserTask())
			return;
		
		UserTaskMetadata userTaskMetadata = (UserTaskMetadata) currentTaskMetadataEntity.getTaskMetadata();
		Option option = userTaskMetadata.getOption(OptionTypeConstants.CARBON_COPY);
		if(option == null || ((CarbonCopyOption)option).getCandidate().getAssignPolicy().isDynamic())
			return;
		
		// 获取具体可抄送的所有人员集合, 并进行抄送
		List<String> assignableUserIds = processEngineBeans.getTaskHandleUtil().getAssignableUserIds(
				handleParameter.getTaskEntityHandler().getCurrentTaskEntity().getTask().getProcinstId(), 
				handleParameter.getTaskEntityHandler().getCurrentTaskEntity().getTask().getTaskinstId(), 
				handleParameter.getUserEntity().getCurrentHandleUserId(), 
				((CarbonCopyOption)option).getCandidate().getAssignPolicy());
		
		if(assignableUserIds.isEmpty())
			return;
		
		new CarbonCopyTaskCmd().execute(
				handleParameter.getTaskEntityHandler().getCurrentTaskEntity().getTask().getTaskinstId(), 
				handleParameter.getUserEntity().getCurrentHandleUserId(), 
				handleParameter.getCurrentDate(), 
				assignableUserIds);
	}
	
	/**
	 * 设置实际指派的用户集合
	 * @param assignedUserIds
	 */
	protected void setAssignedUsers(List<String> assignedUserIds) {
		if(assignedUserIds != null && !assignedUserIds.isEmpty())
			handleParameter.getUserEntity().getAssignedUserIds().addAll(assignedUserIds);
	}
	
	/**
	 * 进行调度
	 * @throws TaskNotExistsException
	 * @throws TaskDispatchException
	 */
	public abstract void execute() throws TaskNotExistsException, TaskDispatchException;
}
