package com.douglei.bpm.module.runtime.task.command;

import java.util.List;

import com.douglei.bpm.ProcessEngineBeans;
import com.douglei.bpm.module.Command;
import com.douglei.bpm.module.ExecutionResult;
import com.douglei.bpm.module.history.task.Attitude;
import com.douglei.bpm.module.runtime.task.TaskInstance;

/**
 * 跳转办理
 * @author DougLei
 */
public class JumpHandleTaskCmd extends AbstractTaskCmd implements Command{
	private String userId; // 进行跳转的用户id
	private String suggest; // 跳转办理意见
	private Attitude attitude; // 跳转办理态度
	private List<String> assignedUserIds; // 指派的用户id集合(目标任务)
	
	public JumpHandleTaskCmd(TaskInstance taskInstance, String userId, String suggest, Attitude attitude, List<String> assignedUserIds) {
		super(taskInstance);
		this.userId = userId;
		this.suggest = suggest;
		this.attitude = attitude;
		this.assignedUserIds = assignedUserIds;
	}

	@Override
	public ExecutionResult execute(ProcessEngineBeans processEngineBeans) {
		// TODO Auto-generated method stub
		
		
		
		
		
		
		
		
		
		
		
		return null;
	}
}
