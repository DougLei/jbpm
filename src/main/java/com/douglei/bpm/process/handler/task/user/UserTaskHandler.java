package com.douglei.bpm.process.handler.task.user;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.douglei.bpm.module.ExecutionResult;
import com.douglei.bpm.module.runtime.task.HandleState;
import com.douglei.bpm.module.runtime.task.Task;
import com.douglei.bpm.process.api.user.task.handle.policy.DispatchPolicy;
import com.douglei.bpm.process.handler.GeneralHandleParameter;
import com.douglei.bpm.process.handler.TaskHandler;
import com.douglei.bpm.process.handler.task.user.assignee.handle.AssigneeDispatcher;
import com.douglei.bpm.process.handler.task.user.assignee.startup.AssigneeHandler;
import com.douglei.bpm.process.mapping.metadata.task.user.UserTaskMetadata;
import com.douglei.orm.context.SessionContext;

/**
 * 
 * @author DougLei
 */
public class UserTaskHandler extends TaskHandler<UserTaskMetadata, GeneralHandleParameter> {
	private final static ExecutionResult CAN_DISPATCH = new ExecutionResult(true);

	@Override
	public ExecutionResult startup() {
		// 创建当前用户任务
		Task task = createTask(false);
		if(currentTaskMetadataEntity.getTaskMetadata().getTimeLimit() != null)
			task.setExpiryTime(new TimeLimitParser(task.getStartTime(), currentTaskMetadataEntity.getTaskMetadata().getTimeLimit()).getExpiryTime());
		
		// 处理指派的用户
		AssigneeHandler assigneeHandler = new AssigneeHandler();
		assigneeHandler.execute(currentTaskMetadataEntity.getTaskMetadata(), task, handleParameter, processEngineBeans);
		
		// 保存当前用户任务
		SessionContext.getTableSession().save(task);
		
		return ExecutionResult.getDefaultSuccessInstance();
	}
	
	@Override
	public ExecutionResult handle() {
		// 更新businessId
		Task currentTask = handleParameter.getTaskEntityHandler().getCurrentTaskEntity().getTask();
		currentTask.addBusinessId(handleParameter.getBusinessId());
		
		// 进行指派信息的调度
		AssigneeDispatcher assigneeDispatcher = new AssigneeDispatcher(currentTask.getTaskinstId(), handleParameter.getUserEntity(), handleParameter.getCurrentDate());
		assigneeDispatcher.dispatch();
		
		// 判断任务办理是否结束
		if(isFinished()) {
			// TODO 可优化: 判断是否是已经到达认领上限
			
			// 获取办理过当前任务的用户id集合
			List<Object[]> list = SessionContext.getSqlSession().query_("select distinct user_id from bpm_hi_assignee where taskinst_id=? and handle_state = 'FINISHED'", Arrays.asList(currentTask.getTaskinstId()));
			List<String> handledUserIds = new ArrayList<String>(list.size());
			list.forEach(array -> handledUserIds.add(array[0].toString()));
			
			// 根据调度策略, 获取可进行任务调度的userId
			DispatchPolicy dispatchPolicy = processEngineBeans.getTaskHandlePolicyContainer().getDispatchPolicy(currentTaskMetadataEntity.getTaskMetadata().getCandidate().getHandlePolicy().getDispatchPolicyEntity().getName());
			currentTask.setDispatchRight(dispatchPolicy.getUserId(handleParameter.getUserEntity().getCurrentHandleUserId(), handledUserIds));
			return CAN_DISPATCH;
		}
		return CANNOT_DISPATCH;
	}
	
	// 判断任务是否结束
	private boolean isFinished() {
		Task currentTask = handleParameter.getTaskEntityHandler().getCurrentTaskEntity().getTask();
		if(!currentTask.isAllClaimed())
			return false;
		
		if(currentTask.getAssignCount() == 1)
			return true;
		
		// 查询当前任务, 已经认领的指派信息数量
		int claimedAssigneeCount = Integer.parseInt(SessionContext.getSqlSession().uniqueQuery_(
				"select count(id) from bpm_ru_assignee where taskinst_id=? and handle_state=?", 
				Arrays.asList(currentTask.getTaskinstId(), HandleState.CLAIMED.name()))[0].toString());
		return claimedAssigneeCount == 0;
	}
}
