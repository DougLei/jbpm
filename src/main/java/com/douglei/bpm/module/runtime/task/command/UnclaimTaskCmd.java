package com.douglei.bpm.module.runtime.task.command;

import java.util.Arrays;
import java.util.List;

import com.douglei.bpm.ProcessEngineBeans;
import com.douglei.bpm.module.Command;
import com.douglei.bpm.module.ExecutionResult;
import com.douglei.bpm.module.runtime.task.Assignee;
import com.douglei.bpm.module.runtime.task.HandleState;
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
	public ExecutionResult execute(ProcessEngineBeans processEngineBeans) {
		if(!taskInstance.requiredUserHandle())
			return new ExecutionResult("取消认领失败, ["+taskInstance.getName()+"]任务不支持用户取消认领");
		
		// 查询指定userId, 判断其是否满足取消认领条件
		List<Assignee> assigneeList = SessionContext.getSqlSession()
				.query(Assignee.class, 
						"select id, handle_state from bpm_ru_assignee where taskinst_id=? and user_id=? and handle_state<>?", 
						Arrays.asList(taskInstance.getTask().getTaskinstId(), userId, HandleState.INVALID.name()));
		if(assigneeList.isEmpty())
			return new ExecutionResult("取消认领失败, 指定的userId没有["+taskInstance.getName()+"]任务的办理权限");
		
		for(int i=0;i<assigneeList.size();i++) {
			if(assigneeList.get(i).getHandleStateInstance().unClaim())
				assigneeList.remove(i--);
		}
		if(assigneeList.isEmpty())
			return new ExecutionResult("取消认领失败, 指定的userId未认领["+taskInstance.getName()+"]任务");
		
		
		
		
		
		// TODO 协办, 抄送的相关处理
		
		
		
		
		
		
		
		
		
		
		// 取消认领
		SessionContext.getSQLSession().executeUpdate("Assignee", "unclaimTask", assigneeList);
		
		// 处理task的isAllClaimed字段值, 改为没有全部认领
		if(taskInstance.getTask().isAllClaimed())
			SessionContext.getSqlSession().executeUpdate("update bpm_ru_task set is_all_claimed=0 where taskinst_id=?", Arrays.asList(taskInstance.getTask().getTaskinstId()));
		return null;
	}
}
