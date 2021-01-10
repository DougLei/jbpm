package com.douglei.bpm.module.runtime.task.command;

import java.util.Arrays;
import java.util.List;

import com.douglei.bpm.ProcessEngineBeans;
import com.douglei.bpm.module.Command;
import com.douglei.bpm.module.ExecutionResult;
import com.douglei.bpm.module.runtime.task.Assignee;
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
			return new ExecutionResult("办理失败, 办理["+taskInstance.getName()+"]任务, 需要提供办理人id");
		
		UserBean currentHandleUser = processEngineBeans.getUserBeanFactory().create(parameter.getUserId());
		if(taskInstance.requiredUserHandle()) {
			HandlePolicy handlePolicy = taskInstance.getHandlePolicy();
			if(handlePolicy.isSuggest() && StringUtil.isEmpty(parameter.getSuggest()))
				return new ExecutionResult("办理失败, 办理["+taskInstance.getName()+"]任务, 需要输入办理意见");
			if(handlePolicy.isAttitude() && parameter.getAttitude() == null)
				return new ExecutionResult("办理失败, 办理["+taskInstance.getName()+"]任务, 需要进行表态");
			
			// 查询指定userId, 判断其是否有权限办理任务, 以及是否认领了任务
			List<Assignee> assigneeList = SessionContext.getSqlSession()
					.query(Assignee.class, 
							"select id, handle_state from bpm_ru_assignee where taskinst_id=? and user_id=?", 
							Arrays.asList(taskInstance.getTask().getTaskinstId(), parameter.getUserId()));
			if(assigneeList.isEmpty())
				return new ExecutionResult("办理失败, 指定的userId没有["+taskInstance.getName()+"]任务的办理权限");
			
			int unClaimNum = 0;
			for (Assignee assignee : assigneeList) {
				switch(assignee.getHandleStateInstance()) {
					case CLAIMED:
						break;
					case UNCLAIM:
					case INVALID:
						unClaimNum++;
						continue;
					case FINISHED:
						return new ExecutionResult("办理失败, 指定的userId已完成["+taskInstance.getName()+"]任务的办理");
				}
			}
			if(unClaimNum == assigneeList.size())
				return new ExecutionResult("办理失败, 指定的userId未认领["+taskInstance.getName()+"]任务");
			
			// 如果是串行办理时, 要判断是否轮到当前userId进行办理
			if(handlePolicy.isMultiHandle() && handlePolicy.getMultiHandlePolicy().isSerialHandle()) {
				SerialHandleSequencePolicy policy = processEngineBeans.getTaskHandlePolicyContainer().getSerialHandleSequencePolicy(handlePolicy.getMultiHandlePolicy().getSerialHandleSequencePolicyName());
				policy.
				
				
				
			}
		}
		return processEngineBeans.getTaskHandleUtil().handle(taskInstance.getTaskMetadataEntity(), new GeneralHandleParameter(
				taskInstance,
				currentHandleUser, 
				parameter.getSuggest(), 
				parameter.getAttitude(), 
				processEngineBeans.getUserBeanFactory().create(parameter.getAssignUserIds())));
	}
}
