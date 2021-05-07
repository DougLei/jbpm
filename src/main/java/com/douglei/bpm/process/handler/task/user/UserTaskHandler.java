package com.douglei.bpm.process.handler.task.user;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.douglei.bpm.module.Result;
import com.douglei.bpm.module.execution.task.HandleState;
import com.douglei.bpm.module.execution.task.runtime.Task;
import com.douglei.bpm.process.api.user.task.handle.policy.ClaimPolicy;
import com.douglei.bpm.process.api.user.task.handle.policy.DispatchPolicy;
import com.douglei.bpm.process.handler.AbstractHandleParameter;
import com.douglei.bpm.process.handler.TaskHandler;
import com.douglei.bpm.process.handler.task.user.assignee.handle.AssigneeDispatcher;
import com.douglei.bpm.process.handler.task.user.assignee.startup.AssigneeResult;
import com.douglei.bpm.process.handler.task.user.timelimit.TimeLimitCalculator;
import com.douglei.bpm.process.handler.task.user.timelimit.TimeLimitCalculatorFactory;
import com.douglei.bpm.process.mapping.metadata.task.user.TimeLimit;
import com.douglei.bpm.process.mapping.metadata.task.user.UserTaskMetadata;
import com.douglei.bpm.process.mapping.metadata.task.user.candidate.handle.ClaimPolicyEntity;
import com.douglei.orm.context.SessionContext;

/**
 * 
 * @author DougLei
 */
public class UserTaskHandler extends TaskHandler<UserTaskMetadata, AbstractHandleParameter> {
	private final static Result CAN_DISPATCH = new Result(true);

	@Override
	public Result startup() {
		// 创建当前用户任务
		Task task = createTask(false);
		if(currentTaskMetadataEntity.getTaskMetadata().getTimeLimit() != null) {
			TimeLimit timeLimit = currentTaskMetadataEntity.getTaskMetadata().getTimeLimit();
			TimeLimitCalculator calc = TimeLimitCalculatorFactory.build(timeLimit.getType());
			task.setExpiryTime(calc.getExpiryTime(task.getStartTime(), timeLimit.getTimes()));
		}
		
		// 进行指派信息处理
		AssigneeResult result = handleParameter.getUserEntity().getAssignEntity().getAssigneeHandler()
				.setParameters(currentTaskMetadataEntity.getTaskMetadata(), handleParameter, processEngineBeans)
				.execute();
		
		// 保存当前用户任务
		task.setAssignCount(result.getAssignCount());
		task.setIsAllClaimed(result.isAllClaimed()?1:0);
		SessionContext.getTableSession().save(task);
		
		return Result.getDefaultSuccessInstance();
	}
	
	@Override
	public Result handle() {
		// 更新businessId
		Task currentTask = handleParameter.getTaskEntityHandler().getCurrentTaskEntity().getTask();
		currentTask.addBusinessId(handleParameter.getBusinessId());
		
		// 进行指派信息的调度
		AssigneeDispatcher assigneeDispatcher = new AssigneeDispatcher(currentTask.getTaskinstId(), handleParameter.getUserEntity(), handleParameter.getCurrentDate());
		assigneeDispatcher.dispatch();
		
		// 判断任务办理是否结束
		FinishedFlag flag = isFinished();
		if(flag.isFinished) {
			String dispatchUserId = handleParameter.getUserEntity().getUserId(); // 可进行调度的用户id
			if(flag.supportMultipleClaim) {
				// 获取办理过当前任务的用户id集合
				List<Object[]> list = SessionContext.getSqlSession().query_("select distinct user_id from bpm_hi_assignee where taskinst_id=? and handle_state = 6", Arrays.asList(currentTask.getTaskinstId()));
				List<String> handledUserIds = new ArrayList<String>(list.size());
				list.forEach(array -> handledUserIds.add(array[0].toString()));
				
				// 根据调度策略, 获取可进行任务调度的userId
				DispatchPolicy dispatchPolicy = processEngineBeans.getAPIContainer().getDispatchPolicy(currentTaskMetadataEntity.getTaskMetadata().getCandidate().getHandlePolicy().getDispatchPolicyEntity().getName());
				dispatchUserId = dispatchPolicy.getUserId(handleParameter.getUserEntity().getUserId(), handledUserIds);
			}
			currentTask.setDispatchRight(dispatchUserId);
			if(dispatchUserId.equals(handleParameter.getUserEntity().getUserId()))
				return CAN_DISPATCH;
		}
		return CANNOT_DISPATCH;
	}
	
	// 判断任务是否结束
	private FinishedFlag isFinished() {
		Task currentTask = handleParameter.getTaskEntityHandler().getCurrentTaskEntity().getTask();
		if(!currentTask.isAllClaimed())
			return new FinishedFlag(true, false);
		
		ClaimPolicyEntity entity = currentTaskMetadataEntity.getTaskMetadata().getCandidate().getHandlePolicy().getClaimPolicyEntity();
		ClaimPolicy claimPolicy = processEngineBeans.getAPIContainer().getClaimPolicy(entity.getName());
		if(claimPolicy.calcUpperLimit(entity.getValue(), currentTask.getAssignCount()) == 1)
			return new FinishedFlag(false, true);
		
		// 查询当前任务, 剩余已经认领的指派信息数量
		int leftClaimedAssigneeCount = Integer.parseInt(SessionContext.getSqlSession().uniqueQuery_(
				"select count(id) from bpm_ru_assignee where taskinst_id=? and handle_state=?", 
				Arrays.asList(currentTask.getTaskinstId(), HandleState.CLAIMED.getValue()))[0].toString());
		return new FinishedFlag(true, leftClaimedAssigneeCount == 0);
	}
	
	/**
	 * 
	 * @author DougLei
	 */
	class FinishedFlag {
		boolean supportMultipleClaim; // 任务是否支持多人认领
		boolean isFinished; // 任务是否完成
		
		public FinishedFlag(boolean supportMultipleClaim, boolean isFinished) {
			this.supportMultipleClaim = supportMultipleClaim;
			this.isFinished = isFinished;
		}
	}
}
