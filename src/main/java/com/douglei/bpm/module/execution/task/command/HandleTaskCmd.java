package com.douglei.bpm.module.execution.task.command;

import java.util.Arrays;
import java.util.List;

import com.douglei.bpm.ProcessEngineBeans;
import com.douglei.bpm.module.Command;
import com.douglei.bpm.module.Result;
import com.douglei.bpm.module.execution.task.HandleState;
import com.douglei.bpm.module.execution.task.command.parameter.HandleTaskParameter;
import com.douglei.bpm.module.execution.task.runtime.Assignee;
import com.douglei.bpm.module.execution.task.runtime.TaskEntity;
import com.douglei.bpm.process.api.user.task.handle.policy.ClaimPolicy;
import com.douglei.bpm.process.handler.GeneralTaskHandleParameter;
import com.douglei.bpm.process.handler.TaskHandleException;
import com.douglei.bpm.process.mapping.metadata.task.user.UserTaskMetadata;
import com.douglei.bpm.process.mapping.metadata.task.user.candidate.handle.HandlePolicy;
import com.douglei.orm.context.SessionContext;
import com.douglei.tools.StringUtil;

/**
 * 办理任务
 * @author DougLei
 */
public class HandleTaskCmd extends AbstractTaskCmd implements Command {
	private HandleTaskParameter parameter;
	public HandleTaskCmd(TaskEntity taskInstance, HandleTaskParameter parameter) {
		super(taskInstance);
		this.parameter = parameter;
	}

	@Override
	public Result execute(ProcessEngineBeans processEngineBeans) {
		if(StringUtil.isEmpty(parameter.getUserId()))
			throw new TaskHandleException("办理失败, 办理["+entity.getName()+"]任务, 需要提供userId");

		// 用户任务时, 判断指定的userId能否办理当前任务
		if(entity.isUserTask()) {
			HandlePolicy handlePolicy = ((UserTaskMetadata) entity.getTaskMetadataEntity().getTaskMetadata()).getCandidate().getHandlePolicy();
			if(handlePolicy.suggestIsRequired() && StringUtil.isEmpty(parameter.getSuggest()))
				return new Result("办理失败, 办理[%s]任务, 需要输入办理意见", "jbpm.handle.fail.no.suggest", entity.getName());
			if(handlePolicy.attitudeIsRequired() && parameter.getAttitude() == null)
				return new Result("办理失败, 办理[%s]任务, 需要进行表态", "jbpm.handle.fail.no.attitude", entity.getName());
			
			// 查询指定userId, 判断其是否可以办理任务
			if(!isClaimed(parameter.getUserId()))
				throw new TaskHandleException("办理失败, 指定的userId无法办理["+entity.getName()+"]任务");
			
			// 根据办理策略, 判断当前的userId能否办理
			int waitForPersonNumber = canHandle(handlePolicy, processEngineBeans);
			if(waitForPersonNumber > 0)
				return new Result("办理失败, 指定的用户暂时不能办理[%s]任务, 需要等待前面%d人完成办理", "jbpm.handle.fail.wait.sequence", entity.getName(), waitForPersonNumber);
		}
		
		return processEngineBeans.getTaskHandleUtil().handle(
				entity.getTaskMetadataEntity(), new GeneralTaskHandleParameter(
						entity, 
						parameter.getUserId(), 
						parameter.getSuggest(), 
						parameter.getAttitude(), 
						parameter.getReason(),
						parameter.getBusinessId(),
						null));
	}

	/**
	 * 是否能办理
	 * @param handlePolicy
	 * @param processEngineBeans
	 * @return 需要等待的人数, 返回0表示可以进行办理
	 */
	private int canHandle(HandlePolicy handlePolicy, ProcessEngineBeans processEngineBeans) {
		// 没有配置串行办理策略(即并行办理)
		if(handlePolicy.getSerialHandlePolicyEntity() == null)
			return 0;
		
		// 判断任务的认领人数上限是否为1
		ClaimPolicy claimPolicy = processEngineBeans.getAPIContainer().getClaimPolicy(handlePolicy.getClaimPolicyEntity().getName());
		if(claimPolicy.calcUpperLimit(handlePolicy.getClaimPolicyEntity().getValue(), entity.getTask().getAssignCount()) == 1)
			return 0;
		
		// 查询当前任务, 所有认领状态的指派信息
		List<Assignee> claimedAssigneeList = SessionContext.getSqlSession()
				.query(Assignee.class, 
						"select user_id, claim_time from bpm_ru_assignee where taskinst_id=? and handle_state=?", 
						Arrays.asList(entity.getTask().getTaskinstId(), HandleState.CLAIMED.getValue()));
		
		int count = 0; // 记录是否全部是当前用户的指派信息
		for (Assignee assignee : claimedAssigneeList) {
			if(!assignee.getUserId().equals(parameter.getUserId()))
				break;
			count++;
		}
		
		if(count == claimedAssigneeList.size())
			return 0;
		return processEngineBeans.getAPIContainer().getSerialHandlePolicy(handlePolicy.getSerialHandlePolicyEntity().getName()).canHandle(parameter.getUserId(), claimedAssigneeList);
	}
}
