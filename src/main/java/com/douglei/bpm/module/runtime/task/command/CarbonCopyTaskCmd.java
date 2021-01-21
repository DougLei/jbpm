package com.douglei.bpm.module.runtime.task.command;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.douglei.bpm.ProcessEngineBeans;
import com.douglei.bpm.module.Command;
import com.douglei.bpm.module.ExecutionResult;
import com.douglei.bpm.module.runtime.task.CarbonCopy;
import com.douglei.bpm.module.runtime.task.TaskInstance;
import com.douglei.bpm.process.api.user.bean.factory.UserBean;
import com.douglei.bpm.process.api.user.option.impl.carboncopy.CarbonCopyOptionHandler;
import com.douglei.bpm.process.handler.GeneralHandleParameter;
import com.douglei.bpm.process.handler.TaskHandleException;
import com.douglei.bpm.process.metadata.TaskMetadata;
import com.douglei.bpm.process.metadata.task.user.UserTaskMetadata;
import com.douglei.bpm.process.metadata.task.user.candidate.assign.AssignPolicy;
import com.douglei.bpm.process.metadata.task.user.option.Option;
import com.douglei.bpm.process.metadata.task.user.option.carboncopy.CarbonCopyOption;
import com.douglei.orm.context.SessionContext;

/**
 * 抄送
 * @author DougLei
 */
public class CarbonCopyTaskCmd extends AbstractTaskCmd implements Command {
	private String userId; // 发起抄送的用户id
	private List<String> assignedUserIds; // 接受抄送的用户id集合
	
	public CarbonCopyTaskCmd() {
		super(null);
	}
	public CarbonCopyTaskCmd(TaskInstance taskInstance, String userId, List<String> assignedUserIds) {
		super(taskInstance);
		this.userId = userId;
		this.assignedUserIds = assignedUserIds;
	}

	@Override
	public ExecutionResult execute(ProcessEngineBeans processEngineBeans) {
		TaskMetadata taskMetadata = taskInstance.getTaskMetadataEntity().getTaskMetadata();
		if(taskMetadata.isAuto())
			throw new TaskHandleException("抄送失败, ["+taskInstance.getName()+"]任务不支持用户进行抄送操作");
		
		Option option = ((UserTaskMetadata)taskMetadata).getOption(CarbonCopyOptionHandler.TYPE);
		if(option == null || !((CarbonCopyOption)option).getCandidate().getAssignPolicy().isDynamic())
			throw new TaskHandleException("抄送失败, ["+taskInstance.getName()+"]任务不支持用户进行抄送操作");
		
		// 查询指定userId, 判断其是否可以抄送
		if(!isClaimed(userId))
			throw new TaskHandleException("抄送失败, 指定的userId没有["+taskInstance.getName()+"]任务的抄送操作权限");
		
		// 获取具体可抄送的所有人员集合
		AssignPolicy assignPolicy = ((CarbonCopyOption)option).getCandidate().getAssignPolicy();
		List<UserBean> assignableUsers = processEngineBeans.getTaskHandleUtil().getAssignableUsers(
				assignPolicy, 
				(UserTaskMetadata)taskMetadata, 
				new GeneralHandleParameter(taskInstance, processEngineBeans.getUserBeanFactory().create(userId), null, null, null, null, null));
		if(assignableUsers.isEmpty())
			throw new TaskHandleException("["+taskMetadata.getName()+"]任务不存在可抄送的人员");
		
		// 构建实际抄送的用户集合并进行验证
		List<UserBean> assignedUsers = new ArrayList<UserBean>(assignedUserIds.size());
		assignedUserIds.forEach(userId -> assignedUsers.add(new UserBean(userId)));
		processEngineBeans.getTaskHandleUtil().validateAssignedUsers(assignedUsers, assignableUsers, assignPolicy.getAssignNumber());
		
		// 进行抄送
		execute(taskInstance.getTask().getTaskinstId(), userId, new Date(), assignedUsers);
		return ExecutionResult.getDefaultSuccessInstance();
	}
	
	/**
	 * 进行抄送
	 * @param taskinstId 任务实例id
	 * @param ccUserId 抄送人id
	 * @param ccTime 抄送时间
	 * @param assignedUsers 接受的用户集合
	 */
	public void execute(String taskinstId, String ccUserId, Date ccTime, List<UserBean> assignedUsers) {
		// 判断接受的用户集合中是否包含抄送人, 如果包含则移除
		for(int i=0; i< assignedUsers.size(); i++) {
			if(assignedUsers.get(i).getUserId().equals(ccUserId))
				assignedUsers.remove(i--);
		}
		
		List<CarbonCopy> carbonCopies = new ArrayList<CarbonCopy>(assignedUsers.size());
		assignedUsers.forEach(user -> {
			carbonCopies.add(new CarbonCopy(taskinstId, ccUserId, ccTime, user.getUserId()));
		});
		SessionContext.getTableSession().save(carbonCopies);
	}
}
