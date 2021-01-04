package com.douglei.bpm.module.runtime.task.command;

import java.util.Arrays;
import java.util.List;

import com.douglei.bpm.bean.BeanInstances;
import com.douglei.bpm.module.Command;
import com.douglei.bpm.module.ExecutionResult;
import com.douglei.bpm.module.runtime.task.Assignee;
import com.douglei.bpm.module.runtime.task.TaskInstance;
import com.douglei.bpm.module.runtime.task.TaskHandleParameter;
import com.douglei.bpm.process.handler.GeneralHandleParameter;
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
	public ExecutionResult execute(BeanInstances beanInstances) {
		if(taskInstance.requiredUserHandle()) {
			if(StringUtil.isEmpty(parameter.getUserId()))
				return new ExecutionResult("办理失败, 办理["+taskInstance.getName()+"]任务, 需要提供具体的userId");
			
			// 查询指定userId, 判断其是否满足办理条件
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
		}
		return beanInstances.getTaskHandlerUtil().handle(taskInstance.getTaskMetadataEntity(), new GeneralHandleParameter(
				taskInstance,
				beanInstances.getUserFactory().create(parameter.getUserId()), 
				parameter.getSuggest(), 
				parameter.getAttitude(), 
				beanInstances.getUserFactory().create(parameter.getAssignUserIds())));
	}
}
