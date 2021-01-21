package com.douglei.bpm.module.runtime.task.command.dispatch;

import java.util.List;

import com.douglei.bpm.ProcessEngineBeans;
import com.douglei.bpm.module.runtime.task.command.CarbonCopyTaskCmd;
import com.douglei.bpm.process.api.user.bean.factory.UserBean;
import com.douglei.bpm.process.api.user.option.impl.carboncopy.CarbonCopyOptionHandler;
import com.douglei.bpm.process.handler.GeneralHandleParameter;
import com.douglei.bpm.process.handler.TaskDispatchException;
import com.douglei.bpm.process.metadata.TaskMetadataEntity;
import com.douglei.bpm.process.metadata.task.user.UserTaskMetadata;
import com.douglei.bpm.process.metadata.task.user.option.Option;
import com.douglei.bpm.process.metadata.task.user.option.carboncopy.CarbonCopyOption;

/**
 * 调度执行器
 * @author DougLei
 */
public abstract class DispatchExecutor {
	protected TaskMetadataEntity<UserTaskMetadata> currentUserTaskMetadataEntity;
	protected GeneralHandleParameter handleParameter;
	protected ProcessEngineBeans processEngineBeans;
	
	/**
	 * 设置属性
	 * @param currentUserTaskMetadataEntity
	 * @param handleParameter
	 * @param processEngineBeans
	 * @return
	 */
	final DispatchExecutor setParameters(TaskMetadataEntity<UserTaskMetadata> currentUserTaskMetadataEntity, GeneralHandleParameter handleParameter, ProcessEngineBeans processEngineBeans) {
		this.currentUserTaskMetadataEntity = currentUserTaskMetadataEntity;
		this.handleParameter = handleParameter;
		this.processEngineBeans = processEngineBeans;
		return this;
	}
	
	/**
	 * 进行抄送
	 */
	protected final void executeCarbonCopy() {
		UserTaskMetadata userTaskMetadata = currentUserTaskMetadataEntity.getTaskMetadata();
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
	 * 进行调度
	 * @throws TaskDispatchException
	 */
	public abstract void execute() throws TaskDispatchException;
}
