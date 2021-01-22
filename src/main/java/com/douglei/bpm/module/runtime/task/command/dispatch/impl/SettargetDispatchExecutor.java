package com.douglei.bpm.module.runtime.task.command.dispatch.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.douglei.bpm.module.runtime.task.HandleState;
import com.douglei.bpm.module.runtime.task.command.dispatch.DispatchExecutor;
import com.douglei.bpm.process.handler.TaskDispatchException;
import com.douglei.bpm.process.metadata.TaskMetadata;
import com.douglei.bpm.process.metadata.TaskMetadataEntity;
import com.douglei.bpm.process.metadata.TaskNotExistsException;
import com.douglei.orm.context.SessionContext;

/**
 * 设置目标调度
 * @author DougLei
 */
public class SettargetDispatchExecutor extends DispatchExecutor {
	private String targetTask; // 目标任务
	private boolean activateLastAssigneeList; // 如果之前流过目标任务, 是否激活其最后一次的指派信息
	private boolean executeCC; // 是否进行抄送
	
	public SettargetDispatchExecutor(String targetTask, boolean executeCC) {
		this(targetTask, true, executeCC);
	}
	public SettargetDispatchExecutor(String targetTask, boolean activateLastAssigneeList, boolean executeCC) {
		this.targetTask = targetTask;
		this.activateLastAssigneeList = activateLastAssigneeList;
		this.executeCC = executeCC;
	}

	@Override
	public void execute() throws TaskNotExistsException, TaskDispatchException {
		String taskinstId= null;
		if(activateLastAssigneeList) {
			List<Object[]> list = SessionContext.getSqlSession().queryLimit_("select taskinst_id from bpm_hi_task where procinst_id=? and key_=? order by end_time desc", 1, 1, Arrays.asList(handleParameter.getProcessInstanceId(), targetTask));
			if(list.size() == 1)
				taskinstId = list.get(0)[0].toString();
		}
		execute(taskinstId, targetTask);
	}
	
	/**
	 * 进行调度
	 * @param taskinstId
	 * @param targetTaskKey 
	 */
	protected final void execute(String taskinstId, String targetTaskKey) {
		TaskMetadataEntity<TaskMetadata> targetTaskMetadataEntity = currentTaskMetadataEntity.getProcessMetadata().getTaskMetadataEntity(targetTaskKey);
		
		if(executeCC)
			executeCarbonCopy();
		
		// 设置实际指派的用户集合
		if(taskinstId != null) {
			List<Object[]> list = SessionContext.getSqlSession().query_("select user_id from bpm_hi_assignee where taskinst_id=? and handle_state=?", Arrays.asList(taskinstId, HandleState.FINISHED.name()));
			if(list.size() > 0) {
				if(assignedUserIds==null) {
					assignedUserIds = new ArrayList<String>(list.size());
				}else if(assignedUserIds.size() > 0) {
					assignedUserIds.clear();
				}
				list.forEach(object -> {
					assignedUserIds.add(object[0].toString());
				});
			}
		}
		setAssignedUsers(assignedUserIds);
		
		processEngineBeans.getTaskHandleUtil().dispatchByTask(targetTaskMetadataEntity, handleParameter);
	}
}
