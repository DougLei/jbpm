package com.douglei.bpm.module.runtime.task.command;

import java.util.List;

import com.douglei.bpm.ProcessEngineBeans;
import com.douglei.bpm.module.Command;
import com.douglei.bpm.module.ExecutionResult;
import com.douglei.bpm.module.runtime.task.TaskInstance;

/**
 * 抄送
 * @author DougLei
 */
public class CarbonCopyTaskCmd implements Command {
	private TaskInstance taskInstance;
	private String userId; // 发起抄送的人id
	private List<String> assignedUserIds; // 接受抄送的人id集合
	
	public CarbonCopyTaskCmd(TaskInstance taskInstance, String userId, List<String> assignedUserIds) {
		this.taskInstance = taskInstance;
		this.userId = userId;
		this.assignedUserIds = assignedUserIds;
	}

	@Override
	public ExecutionResult execute(ProcessEngineBeans processEngineBeans) {
		
		
		
		
		
		
		
		
		
		
		return null;
	}
}
