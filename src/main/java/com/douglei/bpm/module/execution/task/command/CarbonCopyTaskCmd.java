package com.douglei.bpm.module.execution.task.command;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.douglei.bpm.ProcessEngineBeans;
import com.douglei.bpm.module.Command;
import com.douglei.bpm.module.Result;
import com.douglei.bpm.module.execution.task.runtime.CarbonCopy;
import com.douglei.bpm.module.execution.task.runtime.TaskEntity;
import com.douglei.bpm.process.api.user.option.OptionTypeConstants;
import com.douglei.bpm.process.handler.TaskHandleException;
import com.douglei.bpm.process.mapping.metadata.TaskMetadata;
import com.douglei.bpm.process.mapping.metadata.task.user.UserTaskMetadata;
import com.douglei.bpm.process.mapping.metadata.task.user.candidate.assign.AssignPolicy;
import com.douglei.bpm.process.mapping.metadata.task.user.option.Option;
import com.douglei.bpm.process.mapping.metadata.task.user.option.carboncopy.CarbonCopyOption;
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
	public CarbonCopyTaskCmd(TaskEntity taskInstance, String userId, List<String> assignedUserIds) {
		super(taskInstance);
		this.userId = userId;
		this.assignedUserIds = assignedUserIds;
	}

	@Override
	public Result execute(ProcessEngineBeans processEngineBeans) {
		TaskMetadata taskMetadata = entity.getTaskMetadataEntity().getTaskMetadata();
		if(!entity.isUserTask())
			throw new TaskHandleException("抄送失败, ["+entity.getName()+"]任务不支持用户进行抄送操作");
		
		Option option = ((UserTaskMetadata)taskMetadata).getOption(OptionTypeConstants.CARBON_COPY);
		if(option == null)
			throw new TaskHandleException("抄送失败, ["+entity.getName()+"]任务不支持用户进行抄送操作");
		
		// 查询指定userId, 判断其是否可以抄送
		if(!isClaimed(userId))
			throw new TaskHandleException("抄送失败, 指定的userId没有["+entity.getName()+"]任务的抄送操作权限");
		
		// 获取具体可抄送的所有人员集合
		AssignPolicy assignPolicy = ((CarbonCopyOption)option).getCandidate().getAssignPolicy();
		List<String> assignableUserIds = processEngineBeans.getTaskHandleUtil().getAssignableUserIds(
				entity.getTask().getProcinstId(), entity.getTask().getTaskinstId(), userId, assignPolicy);
		if(assignableUserIds.isEmpty())
			throw new TaskHandleException("["+taskMetadata.getName()+"]任务不存在可抄送的人员");
		
		// 构建实际抄送的用户集合并进行验证
		processEngineBeans.getTaskHandleUtil().validateAssignedUsers(assignedUserIds, assignableUserIds, assignPolicy.getAssignNumber());
		
		// 进行抄送
		execute(entity.getTask().getTaskinstId(), userId, new Date(), assignedUserIds);
		return Result.getDefaultSuccessInstance();
	}
	
	/**
	 * 进行抄送
	 * @param taskinstId 任务实例id
	 * @param ccUserId 抄送人id
	 * @param ccTime 抄送时间
	 * @param assignedUsers 接受的用户id集合
	 */
	public void execute(String taskinstId, String ccUserId, Date ccTime, List<String> assignedUserIds) {
		// 判断接受的用户集合中是否包含抄送人, 如果包含则移除
		for(int i=0; i< assignedUserIds.size(); i++) {
			if(assignedUserIds.get(i).equals(ccUserId))
				assignedUserIds.remove(i--);
		}
		
		List<CarbonCopy> carbonCopies = new ArrayList<CarbonCopy>(assignedUserIds.size());
		assignedUserIds.forEach(userId -> {
			carbonCopies.add(new CarbonCopy(taskinstId, ccUserId, ccTime, userId));
		});
		SessionContext.getTableSession().save(carbonCopies);
	}
}
