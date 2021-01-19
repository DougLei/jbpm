package com.douglei.bpm.module.runtime.task.command;

import java.util.List;

import com.douglei.bpm.ProcessEngineBeans;
import com.douglei.bpm.module.Command;
import com.douglei.bpm.module.ExecutionResult;
import com.douglei.bpm.module.runtime.task.TaskInstance;

/**
 * 跳转
 * @author DougLei
 */
public class JumpTaskCmd extends AbstractTaskCmd implements Command{
	private String target; // 跳转的目标任务id
	private String userId; // 进行跳转的用户id
	private String reason; // 跳转原因
	private List<String> assignedUserIds; // 指派的用户id集合(目标任务)
	
	public JumpTaskCmd(TaskInstance taskInstance, String target, String userId, String reason, List<String> assignedUserIds) {
		super(taskInstance);
		this.target = target;
		this.userId = userId;
		this.reason = reason;
		this.assignedUserIds = assignedUserIds;
	}

	@Override
	public ExecutionResult execute(ProcessEngineBeans processEngineBeans) {
		// TODO Auto-generated method stub
		
		
		
		
		
		
		
		
		
		
		
		return null;
	}
}
