package com.douglei.bpm.module.execution.task.command;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

import com.douglei.bpm.ProcessEngineBeans;
import com.douglei.bpm.module.Command;
import com.douglei.bpm.module.Result;
import com.douglei.bpm.module.execution.task.AssignMode;
import com.douglei.bpm.module.execution.task.HandleState;
import com.douglei.bpm.module.execution.task.runtime.Assignee;
import com.douglei.bpm.module.execution.task.runtime.TaskEntity;
import com.douglei.bpm.process.api.user.option.OptionTypeConstants;
import com.douglei.bpm.process.handler.TaskHandleException;
import com.douglei.bpm.process.handler.task.user.assignee.startup.DelegateHandler;
import com.douglei.bpm.process.handler.task.user.assignee.startup.DelegationSqlCondition;
import com.douglei.bpm.process.mapping.metadata.TaskMetadata;
import com.douglei.bpm.process.mapping.metadata.task.user.UserTaskMetadata;
import com.douglei.bpm.process.mapping.metadata.task.user.candidate.Candidate;
import com.douglei.bpm.process.mapping.metadata.task.user.option.delegate.DelegateOption;
import com.douglei.orm.context.SessionContext;
import com.douglei.tools.StringUtil;

/**
 * 委托任务
 * @author DougLei
 */
public class DelegateTaskCmd implements Command {
	private TaskEntity entity;
	private String userId; // 发起委托的用户id
	private String assignedUserId; // 接受委托的用户id
	private String reason; // 委托原因
	
	public DelegateTaskCmd(TaskEntity entity, String userId, String assignedUserId, String reason) {
		this.entity = entity;
		this.userId = userId;
		this.assignedUserId = assignedUserId;
		this.reason = reason;
	}

	@Override
	public Result execute(ProcessEngineBeans processEngineBeans) {
		TaskMetadata taskMetadata = entity.getTaskMetadataEntity().getTaskMetadata();
		if(!entity.isUserTask())
			throw new TaskHandleException(getAssignMode()+"失败, ["+entity.getName()+"]任务不支持用户进行"+getAssignMode()+"操作");
		
		if(userId.equals(assignedUserId))
			return new Result("不能%s给自己", "jbpm.delegate.fail.to.self", getAssignMode());
		
		DelegateOption option = (DelegateOption) ((UserTaskMetadata)taskMetadata).getOption(getOptionType());
		if(option == null)
			throw new TaskHandleException(getAssignMode()+"失败, ["+entity.getName()+"]任务不支持用户进行"+getAssignMode()+"操作");
		if(option.reasonIsRequired() && StringUtil.isEmpty(reason))
			return new Result("请输入%s的具体原因", "jbpm.delegate.fail.no.reason", getAssignMode());
		
		// 查询指定userId, 判断其是否可以委托
		List<Assignee> assigneeList = SessionContext.getSqlSession()
				.query(Assignee.class, 
						"select id, taskinst_id, group_id, chain_id from bpm_ru_assignee where taskinst_id=? and user_id=? and handle_state=?", 
						Arrays.asList(entity.getTask().getTaskinstId(), userId, HandleState.CLAIMED.getValue()));
		if(assigneeList.isEmpty())
			throw new TaskHandleException(getAssignMode()+"失败, 指定的userId没有["+entity.getName()+"]任务的"+getAssignMode()+"操作权限");
		
	
		// 获取具体可委托的所有人员集合, 并对本次委托的人员进行验证
		Candidate candidate = option.getCandidate();
		if(candidate == null)
			candidate = ((UserTaskMetadata)taskMetadata).getCandidate();
		
		HashSet<String> assignableUserIds = processEngineBeans.getTaskHandleUtil().getAssignableUserIds(
				entity.getTask().getProcinstId(), entity.getTask().getTaskinstId(), userId, candidate.getAssignPolicy());
		if(assignableUserIds.isEmpty())
			throw new TaskHandleException("["+taskMetadata.getName()+"]任务不存在可"+getAssignMode()+"的人员");
		if(!assignableUserIds.contains(assignedUserId))
			throw new TaskHandleException("不能指派配置范围外的人员"); 
		
		
		// 进行委托操作
		// 1. 发起委托的用户, 修改必要的列值, 放弃任务的办理权
		giveupTask(assigneeList);
			
		// 2. 查询委托人的委托信息, 组装成指派信息集合, 保存到指派表
		HashSet<String> assignedUserIds = new HashSet<String>(2);
		assignedUserIds.add(assignedUserId);
		
		DelegateHandler delegateHandler = new DelegateHandler(
				entity.getTask().getTaskinstId(), userId, 
				new DelegationSqlCondition(entity.getProcessMetadata().getCode(), entity.getProcessMetadata().getVersion(), assignedUserIds));
		
		List<Assignee> delegateAssigneeList = new ArrayList<Assignee>(assigneeList.size() * 3);
		Date claimTime = new Date();
		int delegateDeep = -1; // 委托人的委托深度
		for(Assignee assignee : assigneeList) {
			delegateHandler.addAssignee(
					entity.getTask().getTaskinstId(), 
					assignee.getGroupId(), 
					assignee.getChainId()+1, 
					assignedUserId, 
					reason, 
					false, 
					delegateAssigneeList);

			// 计算出委托人的委托深度
			if(delegateDeep == -1) 
				delegateDeep = delegateAssigneeList.size()-1;
			
			// 设置第一个指派信息的指派模式
			int firstIndex = delegateAssigneeList.size()-delegateDeep-1;
			delegateAssigneeList.get(firstIndex).setModeInstance(getAssignMode());
			
			// 对最后一个指派信息进行认领操作
			int lastIndex = delegateAssigneeList.size()-1;
			delegateAssigneeList.get(lastIndex).claim(claimTime);
			
			// 将前面(二次委托的)指派信息的HandleState设置为INVALID_UNCLAIM
			if(firstIndex< lastIndex) {
				for(int i=firstIndex; i< lastIndex; i++) 
					delegateAssigneeList.get(i).setHandleStateInstance(HandleState.INVALID_UNCLAIM);
			}
		}
		SessionContext.getTableSession().save(delegateAssigneeList);
		return Result.getDefaultSuccessInstance();
	}
	
	/**
	 * 获取当前option的类型
	 * @return
	 */
	protected String getOptionType() {
		return OptionTypeConstants.DELEGATE;
	}
	
	/**
	 * 获取指派模式
	 * @return
	 */
	protected AssignMode getAssignMode() {
		return AssignMode.DELEGATED;
	}
	
	/**
	 * 放弃任务办理权
	 * @param assigneeList
	 */
	protected void giveupTask(List<Assignee> assigneeList) {
		SessionContext.getSQLSession().executeUpdate("Assignee", "giveupTask4Delegate", assigneeList);
	}
}
