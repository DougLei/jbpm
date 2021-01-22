package com.douglei.bpm.module.runtime.task.command.dispatch;

import java.util.List;

import com.douglei.bpm.ProcessEngineBeans;
import com.douglei.bpm.module.runtime.task.command.CarbonCopyTaskCmd;
import com.douglei.bpm.process.api.user.bean.factory.UserBean;
import com.douglei.bpm.process.api.user.option.impl.carboncopy.CarbonCopyOptionHandler;
import com.douglei.bpm.process.handler.GeneralHandleParameter;
import com.douglei.bpm.process.handler.TaskDispatchException;
import com.douglei.bpm.process.metadata.TaskMetadata;
import com.douglei.bpm.process.metadata.TaskMetadataEntity;
import com.douglei.bpm.process.metadata.TaskNotExistsException;
import com.douglei.bpm.process.metadata.task.user.UserTaskMetadata;
import com.douglei.bpm.process.metadata.task.user.option.Option;
import com.douglei.bpm.process.metadata.task.user.option.carboncopy.CarbonCopyOption;

/**
 * 调度执行器
 * @author DougLei
 */
public abstract class DispatchExecutor {
	protected TaskMetadataEntity<? extends TaskMetadata> currentTaskMetadataEntity;
	protected GeneralHandleParameter handleParameter;
	protected List<String> assignedUserIds; // 实际指派的用户id集合
	protected ProcessEngineBeans processEngineBeans;
	
	/**
	 * 设置属性
	 * @param currentUserTaskMetadataEntity
	 * @param handleParameter
	 * @param assignedUserIds 实际指派的用户id集合
	 * @param processEngineBeans
	 * @return
	 */
	protected final DispatchExecutor setParameters(TaskMetadataEntity<? extends TaskMetadata> currentTaskMetadataEntity, GeneralHandleParameter handleParameter, List<String> assignedUserIds, ProcessEngineBeans processEngineBeans) {
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
		UserTaskMetadata userTaskMetadata = currentTaskMetadataEntity.getTaskMetadata();
		Option option = userTaskMetadata.getOption(CarbonCopyOptionHandler.TYPE);
		if(option == null || ((CarbonCopyOption)option).getCandidate().getAssignPolicy().isDynamic())
			return;
		
		// 获取具体可抄送的所有人员集合, 并进行抄送
		List<UserBean> assignableUsers = processEngineBeans.getTaskHandleUtil().getAssignableUsers(
				((CarbonCopyOption)option).getCandidate().getAssignPolicy(), 
				userTaskMetadata, 
				handleParameter);
		
		if(assignableUsers.isEmpty())
			return;
		
		new CarbonCopyTaskCmd().execute(
				handleParameter.getTaskEntityHandler().getCurrentTaskEntity().getTask().getTaskinstId(), 
				handleParameter.getUserEntity().getCurrentHandleUser().getUserId(), 
				handleParameter.getCurrentDate(), 
				assignableUsers);
	}
	
	/**
	 * 设置实际指派的用户集合
	 * @param assignedUserIds
	 */
	protected void setAssignedUsers(List<String> assignedUserIds) {
		List<UserBean> assignedUsers = processEngineBeans.getUserBeanFactory().create(assignedUserIds);
		if(assignedUsers != null && !assignedUsers.isEmpty())
			handleParameter.getUserEntity().getAssignedUsers().addAll(assignedUsers);
	}
	
	/**
	 * 进行调度
	 * @throws TaskNotExistsException
	 * @throws TaskDispatchException
	 */
	public abstract void execute() throws TaskNotExistsException, TaskDispatchException;
}
