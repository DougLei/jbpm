package com.douglei.bpm.module.runtime.task.command;

import java.util.List;

import com.douglei.bpm.ProcessEngineBeans;
import com.douglei.bpm.module.Command;
import com.douglei.bpm.module.ExecutionResult;
import com.douglei.bpm.module.runtime.task.TaskInstance;
import com.douglei.bpm.process.api.user.option.impl.carboncopy.CarbonCopyOptionHandler;
import com.douglei.bpm.process.handler.GeneralHandleParameter;
import com.douglei.bpm.process.handler.HandleParameter;
import com.douglei.bpm.process.metadata.TaskMetadata;
import com.douglei.bpm.process.metadata.task.user.UserTaskMetadata;
import com.douglei.bpm.process.metadata.task.user.candidate.assign.AssignPolicy;
import com.douglei.bpm.process.metadata.task.user.option.Option;
import com.douglei.bpm.process.metadata.task.user.option.carboncopy.CarbonCopyOption;

/**
 * 抄送
 * @author DougLei
 */
public class CarbonCopyTaskCmd extends GeneralTaskCmd implements Command {
	private String userId; // 发起抄送的人id
	private List<String> assignedUserIds; // 接受抄送的人id集合
	
	public CarbonCopyTaskCmd(TaskInstance taskInstance, String userId, List<String> assignedUserIds) {
		super(taskInstance);
		this.userId = userId;
		this.assignedUserIds = assignedUserIds;
	}

	@Override
	public ExecutionResult execute(ProcessEngineBeans processEngineBeans) {
		TaskMetadata taskMetadata = taskInstance.getTaskMetadataEntity().getTaskMetadata();
		if(!taskMetadata.requiredUserHandle())
			return new ExecutionResult("抄送失败, ["+taskInstance.getName()+"]任务不支持用户进行抄送操作");
		
		Option option = ((UserTaskMetadata)taskMetadata).getOption(CarbonCopyOptionHandler.TYPE);
		if(option == null || !((CarbonCopyOption)option).getCandidate().getAssignPolicy().isDynamic())
			return new ExecutionResult("抄送失败, ["+taskInstance.getName()+"]任务不支持用户进行抄送操作");
		
		// 查询指定userId, 判断其是否可以抄送
		if(!isClaimed(userId))
			return new ExecutionResult("抄送失败, 指定的userId没有"+taskInstance.getName()+"[]任务的抄送操作权限");
		
		
		AssignPolicy assignPolicy = ((CarbonCopyOption)option).getCandidate().getAssignPolicy();
		processEngineBeans.getTaskHandleUtil().getAssignableUsers(assignPolicy, (UserTaskMetadata)taskMetadata, new GeneralHandleParameter());
		
		
		
		
		
		
		
		
		return ExecutionResult.getDefaultSuccessInstance();
	}
}
