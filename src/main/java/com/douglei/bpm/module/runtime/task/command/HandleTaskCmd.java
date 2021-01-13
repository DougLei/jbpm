package com.douglei.bpm.module.runtime.task.command;

import java.util.Arrays;
import java.util.List;

import com.douglei.bpm.ProcessEngineBeans;
import com.douglei.bpm.module.Command;
import com.douglei.bpm.module.ExecutionResult;
import com.douglei.bpm.module.runtime.task.AssignMode;
import com.douglei.bpm.module.runtime.task.Assignee;
import com.douglei.bpm.module.runtime.task.HandleState;
import com.douglei.bpm.module.runtime.task.TaskHandleParameter;
import com.douglei.bpm.module.runtime.task.TaskInstance;
import com.douglei.bpm.process.api.user.bean.factory.UserBean;
import com.douglei.bpm.process.api.user.task.handle.policy.SerialHandleSequencePolicy;
import com.douglei.bpm.process.handler.GeneralHandleParameter;
import com.douglei.bpm.process.metadata.task.user.UserTaskMetadata;
import com.douglei.bpm.process.metadata.task.user.candidate.handle.HandlePolicy;
import com.douglei.orm.context.SessionContext;
import com.douglei.tools.utils.StringUtil;

/**
 * 
 * @author DougLei
 */
public class HandleTaskCmd implements Command {
	private TaskInstance taskInstance;
	private TaskHandleParameter parameter;
	public HandleTaskCmd(TaskInstance taskInstance, TaskHandleParameter parameter) {
		this.taskInstance = taskInstance;
		this.parameter = parameter;
	}

	@Override
	public ExecutionResult execute(ProcessEngineBeans processEngineBeans) {
		if(StringUtil.isEmpty(parameter.getUserId()))
			return new ExecutionResult("办理失败, 办理["+taskInstance.getName()+"]任务, 需要提供userId");

		UserBean currentHandleUser = processEngineBeans.getUserBeanFactory().create(parameter.getUserId());
		
		if(taskInstance.requiredUserHandle()) {
			HandlePolicy handlePolicy = ((UserTaskMetadata) taskInstance.getTaskMetadataEntity().getTaskMetadata()).getCandidate().getHandlePolicy();
			if(handlePolicy.isSuggest() && StringUtil.isEmpty(parameter.getSuggest()))
				return new ExecutionResult("办理失败, 办理["+taskInstance.getName()+"]任务, 需要输入办理意见");
			if(handlePolicy.isAttitude() && parameter.getAttitude() == null)
				return new ExecutionResult("办理失败, 办理["+taskInstance.getName()+"]任务, 需要进行表态");
			
			// 查询指定userId, 判断其是否有权限办理任务, 以及是否认领了任务
			List<Assignee> assigneeList = SessionContext.getSqlSession()
					.query(Assignee.class, 
							"select id, handle_state from bpm_ru_assignee where taskinst_id=? and user_id=? and handle_state<>?", 
							Arrays.asList(taskInstance.getTask().getTaskinstId(), parameter.getUserId(), HandleState.INVALID.name()));
			if(assigneeList.isEmpty())
				return new ExecutionResult("办理失败, 指定的userId没有["+taskInstance.getName()+"]任务的办理权限");
			
			int unClaimNum = 0;
			for (Assignee assignee : assigneeList) {
				if(assignee.getHandleStateInstance().isClaimed())
					break;
				else
					unClaimNum++;
			}
			if(unClaimNum == assigneeList.size())
				return new ExecutionResult("办理失败, 指定的userId未认领["+taskInstance.getName()+"]任务");
			
			// 根据办理策略, 判断当前的userId能否办理
			int waitForPersonNumber = canHandle(handlePolicy, currentHandleUser, processEngineBeans);
			if(waitForPersonNumber > 0)
				return new ExecutionResult("办理失败, 指定的userId暂时不能办理["+taskInstance.getName()+"]任务, 需要等待前面"+waitForPersonNumber+"人完成办理");
		}
		return processEngineBeans.getTaskHandleUtil().handle(taskInstance.getTaskMetadataEntity(), new GeneralHandleParameter(
				taskInstance,
				currentHandleUser, 
				parameter.getSuggest(), 
				parameter.getAttitude(), 
				processEngineBeans.getUserBeanFactory().create(parameter.getAssignUserIds())));
	}

	/**
	 * 是否能办理
	 * @param handlePolicy
	 * @param currentHandleUser
	 * @param processEngineBeans
	 * @return 需要等待的人数, 返回0表示可以进行办理
	 */
	private int canHandle(HandlePolicy handlePolicy, UserBean currentHandleUser, ProcessEngineBeans processEngineBeans) {
		if(handlePolicy.isMultiHandle() && handlePolicy.getMultiHandlePolicyEntity().isSerialHandle()) {
			SerialHandleSequencePolicy policy = processEngineBeans.getTaskHandlePolicyContainer().getSerialHandleSequencePolicy(
					handlePolicy.getMultiHandlePolicyEntity().getSerialHandleSequencePolicyName());
			
			// 查询当前任务, 所有认领状态的指派信息
			List<Assignee> claimedAssigneeList = SessionContext.getSqlSession()
					.query(Assignee.class, 
							"select * from bpm_ru_assignee where taskinst_id=? and handle_state=? and mode <> ?", 
							Arrays.asList(taskInstance.getTask().getTaskinstId(), HandleState.CLAIMED.name(), AssignMode.ASSISTED.name()));
			
			int count = 0; // 记录是否全部是当前用户的指派信息
			for (Assignee assignee : claimedAssigneeList) {
				if(assignee.getUserId().equals(currentHandleUser.getUserId()))
					count++;
				else
					break;
			}
			
			if(count == claimedAssigneeList.size())
				return 0;
			return policy.canHandle(currentHandleUser, claimedAssigneeList);
		}
		return 0;
	}
}
