package com.douglei.bpm.module.runtime.task.command;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import com.douglei.bpm.ProcessEngineBeans;
import com.douglei.bpm.module.Command;
import com.douglei.bpm.module.ExecutionResult;
import com.douglei.bpm.module.runtime.task.AssignMode;
import com.douglei.bpm.module.runtime.task.Assignee;
import com.douglei.bpm.module.runtime.task.HandleState;
import com.douglei.bpm.module.runtime.task.TaskInstance;
import com.douglei.bpm.process.api.user.bean.factory.UserBean;
import com.douglei.bpm.process.api.user.option.impl.delegate.DelegateOptionHandler;
import com.douglei.bpm.process.handler.GeneralHandleParameter;
import com.douglei.bpm.process.handler.task.user.assignee.startup.DelegationHandler;
import com.douglei.bpm.process.handler.task.user.assignee.startup.SqlCondition;
import com.douglei.bpm.process.metadata.TaskMetadata;
import com.douglei.bpm.process.metadata.task.user.UserTaskMetadata;
import com.douglei.bpm.process.metadata.task.user.candidate.Candidate;
import com.douglei.bpm.process.metadata.task.user.option.delegate.DelegateOption;
import com.douglei.orm.context.SessionContext;
import com.douglei.tools.utils.StringUtil;

/**
 * 委托任务
 * @author DougLei
 */
public class DelegateTaskCmd implements Command {
	private TaskInstance taskInstance;
	private String userId; // 发起委托的用户id
	private String assignedUserId; // 接受委托的用户id
	private String reason; // 委托原因
	
	public DelegateTaskCmd(TaskInstance taskInstance, String userId, String assignedUserId, String reason) {
		this.taskInstance = taskInstance;
		this.userId = userId;
		this.assignedUserId = assignedUserId;
		this.reason = reason;
	}

	@Override
	public ExecutionResult execute(ProcessEngineBeans processEngineBeans) {
		TaskMetadata taskMetadata = taskInstance.getTaskMetadataEntity().getTaskMetadata();
		if(!taskMetadata.requiredUserHandle())
			return new ExecutionResult(getOptionName()+"失败, ["+taskInstance.getName()+"]任务不支持用户进行"+getOptionName()+"操作");
		
		DelegateOption option = (DelegateOption) ((UserTaskMetadata)taskMetadata).getOption(getOptionType());
		if(option == null)
			return new ExecutionResult(getOptionName()+"失败, ["+taskInstance.getName()+"]任务不支持用户进行"+getOptionName()+"操作");
		if(option.reasonIsRequired() && StringUtil.isEmpty(reason))
			return new ExecutionResult(getOptionName()+"失败, 请输入"+getOptionName()+"的具体原因");
		
		// 查询指定userId, 判断其是否可以委托
		List<Assignee> assigneeList = SessionContext.getSqlSession()
				.query(Assignee.class, 
						"select id, group_id, chain_id from bpm_ru_assignee where taskinst_id=? and user_id=? and handle_state=?", 
						Arrays.asList(taskInstance.getTask().getTaskinstId(), userId, HandleState.CLAIMED.name()));
		if(assigneeList.isEmpty())
			return new ExecutionResult(getOptionName()+"失败, 指定的userId没有["+taskInstance.getName()+"]任务的"+getOptionName()+"操作权限");
		
		
		// 获取具体可委托的所有人员集合, 并对本次委托的人员进行验证
		Candidate candidate = option.getCandidate();
		if(candidate == null)
			candidate = ((UserTaskMetadata)taskMetadata).getCandidate();
		
		List<UserBean> assignableUsers = processEngineBeans.getTaskHandleUtil().getAssignableUsers(
				candidate.getAssignPolicy(), 
				(UserTaskMetadata)taskMetadata, 
				new GeneralHandleParameter(taskInstance, processEngineBeans.getUserBeanFactory().create(userId), null, null, null));
		if(assignableUsers.isEmpty())
			return new ExecutionResult("["+taskMetadata.getName()+"]任务不存在可"+getOptionName()+"的人员");
		if(!assignableUsers.contains(new UserBean(assignedUserId)))
			return new ExecutionResult("不能指派配置范围外的人员"); 
		
		
		// 进行委托操作
		// 1. 发起委托的用户, 修改必要的列值
		Map<String, Object> parameterMap = new HashMap<String, Object>(4);
		parameterMap.put("handleState", targetHandleState().name());
		parameterMap.put("assigneeList", assigneeList);
		SessionContext.getSQLSession().executeUpdate("Assignee", "giveupTask", assigneeList);
			
		// 2. 查询委托人的委托信息, 组装成指派信息集合, 保存到指派表
		List<String> assignedUserIds = new ArrayList<String>(1);
		assignedUserIds.add(assignedUserId);
		
		DelegationHandler delegationHandler = new DelegationHandler(
				taskInstance.getProcessMetadata().getCode(), 
				taskInstance.getProcessMetadata().getVersion(), 
				new SqlCondition(assignedUserIds),
				taskInstance.getTask().getTaskinstId(),
				userId,
				new HashSet<String>());
		
		List<Assignee> delegateAssigneeList = new ArrayList<Assignee>(assigneeList.size() * 3);
		Date claimTime = new Date();
		int delegateDeep = -1; // 委托人的委托深度
		for(Assignee assignee : assigneeList) {
			delegationHandler.addAssignee(
					taskInstance.getTask().getTaskinstId(), 
					assignee.getGroupId(), 
					assignee.getChainId()+1, 
					assignedUserId, 
					reason, 
					false, 
					delegateAssigneeList);

			if(delegateDeep == -1)
				delegateDeep = delegateAssigneeList.size()-1;
			
			// 设置委托人的指派模式
			delegateAssigneeList.get(delegateAssigneeList.size()-delegateDeep-1).setModeInstance(getAssignMode());
			// 将最后一个指派信息进行认领
			delegateAssigneeList.get(delegateAssigneeList.size()-1).claim(claimTime);
		}
		SessionContext.getTableSession().save(delegateAssigneeList);
		return ExecutionResult.getDefaultSuccessInstance();
	}
	
	/**
	 * 获取当前option的类型
	 * @return
	 */
	protected String getOptionType() {
		return DelegateOptionHandler.TYPE;
	}
	
	/**
	 * 获取当前option的名称
	 * <p>
	 * 属于引擎内置的名称
	 * @return
	 */
	protected String getOptionName() {
		return "委托";
	}

	/**
	 * 获取指派模式
	 * @return
	 */
	protected AssignMode getAssignMode() {
		return AssignMode.DELEGATED;
	}
	
	/**
	 * 当前用户操作成功后的HandleState
	 * @return
	 */
	protected HandleState targetHandleState() {
		return HandleState.COMPETITIVE_UNCLAIM;
	}
}
