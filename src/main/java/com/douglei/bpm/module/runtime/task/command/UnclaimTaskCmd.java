package com.douglei.bpm.module.runtime.task.command;

import java.util.Arrays;
import java.util.List;

import com.douglei.bpm.bean.BeanInstances;
import com.douglei.bpm.module.Command;
import com.douglei.bpm.module.ExecutionResult;
import com.douglei.bpm.module.runtime.task.Assignee;
import com.douglei.bpm.module.runtime.task.TaskInstance;
import com.douglei.orm.context.SessionContext;

/**
 * 
 * @author DougLei
 */
public class UnclaimTaskCmd implements Command {
	private TaskInstance taskInstance;
	private String userId; // 要取消认领的用户id
	public UnclaimTaskCmd(TaskInstance taskInstance, String userId) {
		this.taskInstance = taskInstance;
		this.userId = userId;
	}

	@Override
	public ExecutionResult execute(BeanInstances beanInstances) {
		if(!taskInstance.requiredUserHandle())
			return new ExecutionResult("取消认领失败, ["+taskInstance.getName()+"]任务不支持用户处理");
		
		// 查询指定userId, 判断其是否满足取消认领条件
		List<Assignee> assigneeList = SessionContext.getSqlSession()
				.query(Assignee.class, 
						"select id, handle_state from bpm_ru_assignee where taskinst_id=? and user_id=?", 
						Arrays.asList(taskInstance.getTask().getTaskinstId(), userId));
		if(assigneeList.isEmpty())
			return new ExecutionResult("取消认领失败, 指定的userId没有["+taskInstance.getName()+"]任务的办理权限");
		
		for(int i=0;i<assigneeList.size();i++) {
			switch(assigneeList.get(i).getHandleStateInstance()) {
				case CLAIMED:
					continue;
				case UNCLAIM:
				case INVALID:
					assigneeList.remove(i--);
					continue;
				case FINISHED:
					return new ExecutionResult("取消认领失败, 指定的userId已完成["+taskInstance.getName()+"]任务的办理");
			}
		}
		if(assigneeList.isEmpty())
			return new ExecutionResult("取消认领失败, 指定的userId未认领["+taskInstance.getName()+"]任务");
		
		// 取消认领
		SessionContext.getSQLSession().executeUpdate("Assignee", "unclaimTask", assigneeList);
		return null;
	}
}
