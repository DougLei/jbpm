package com.douglei.bpm.module.execution.task.command;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import com.douglei.bpm.ProcessEngineBeans;
import com.douglei.bpm.ProcessEngineBugException;
import com.douglei.bpm.module.Command;
import com.douglei.bpm.module.Result;
import com.douglei.bpm.module.execution.task.HandleState;
import com.douglei.bpm.module.execution.task.runtime.Assignee;
import com.douglei.bpm.module.execution.task.runtime.TaskEntity;
import com.douglei.bpm.process.api.user.task.handle.policy.ClaimResult;
import com.douglei.bpm.process.handler.TaskHandleException;
import com.douglei.bpm.process.mapping.metadata.task.user.UserTaskMetadata;
import com.douglei.bpm.process.mapping.metadata.task.user.candidate.handle.ClaimPolicyEntity;
import com.douglei.orm.context.SessionContext;

/**
 * 认领任务
 * @author DougLei
 */
public class ClaimTaskCmd implements Command{
	private TaskEntity entity;
	private String currentClaimUserId; // 要认领的用户id
	public ClaimTaskCmd(TaskEntity entity, String currentClaimUserId) {
		this.entity = entity;
		this.currentClaimUserId = currentClaimUserId;
	}
	
	@Override
	public Result execute(ProcessEngineBeans processEngineBeans) {
		if(!entity.isUserTask())
			throw new TaskHandleException("认领失败, ["+entity.getName()+"]任务不支持用户认领");
		if(entity.getTask().isAllClaimed())
			return new Result("认领失败, [%s]任务的办理权限已被认领完", "jbpm.claim.fail.task.all.claimed", entity.getName());
		
		return claim(processEngineBeans);
	}

	// 任务认领
	private synchronized Result claim(ProcessEngineBeans processEngineBeans) {
		// 查询指定userId, 判断其是否可以认领
		List<Assignee> assigneeList = SessionContext.getSqlSession().query(
				Assignee.class, 
				SQL_QUERY_CAN_CLAIM_ASSIGNEE_LIST, 
				Arrays.asList(entity.getTask().getTaskinstId(), currentClaimUserId, HandleState.COMPETITIVE_UNCLAIM.getValue(), HandleState.UNCLAIM.getValue()));
		if(assigneeList.isEmpty())
			return new Result("认领失败, [%s]任务已被认领", "jbpm.claim.fail.claimed.by.other", entity.getName());
		
		// 判断当前用户能否认领
		ClaimResult result = canClaim(assigneeList, processEngineBeans);
		if(!result.canClaim())
			return new Result("认领失败, [%s]任务的办理权限已被认领完", "jbpm.claim.fail.task.all.claimed", entity.getName());
		
		// 进行认领
		Date claimTime = new Date();
		for (Assignee assignee : assigneeList) {
			// 不是链的最后, 要将比自己大的都删除掉
			if(!assignee.isChainLast()) 
				SessionContext.getSqlSession().executeUpdate(
						"delete bpm_ru_assignee where taskinst_id=? and group_id=? and chain_id >?", 
						Arrays.asList(entity.getTask().getTaskinstId(), assignee.getGroupId(), assignee.getChainId()));
			
			// 不是链的开始, 要将比自己小的HandleState值从COMPETITIVE_UNCLAIM改为INVALID_UNCLAIM
			if(!assignee.isChainFirst()) 
				SessionContext.getSqlSession().executeUpdate(
						"update bpm_ru_assignee set handle_state=? where taskinst_id=? and group_id=? and chain_id <? and handle_state=?", 
						Arrays.asList(HandleState.INVALID_UNCLAIM.getValue(), entity.getTask().getTaskinstId(), assignee.getGroupId(), assignee.getChainId(), HandleState.COMPETITIVE_UNCLAIM.getValue()));
			
			// 进行认领
			assignee.claim(claimTime);
		}
		SessionContext.getTableSession().update(assigneeList);
		
		// 处理task的isAllClaimed字段值, 改为全部认领
		if(result.getLeftCount() == 0)
			entity.getTask().setAllClaimed();
		return Result.getDefaultSuccessInstance();
	}

	/**
	 * 是否可以认领
	 * @param currentAssigneeList 当前进行认领的用户的指派信息集合
	 * @param processEngineBeans
	 * @return
	 */
	private ClaimResult canClaim(List<Assignee> currentAssigneeList, ProcessEngineBeans processEngineBeans) {
		List<Assignee> unclaimAssigneeList = SessionContext.getSQLSession().query(Assignee.class, "Assignee", "queryAssigneeClaimSituation", entity.getTask().getTaskinstId());
		List<Assignee> claimedAssigneeList= null; 
		List<Assignee> finishedAssigneeList = null;
		
		for(int i=0;i<unclaimAssigneeList.size();i++) {
			switch(unclaimAssigneeList.get(i).getHandleStateInstance()) {
				case UNCLAIM:
					break;
				case CLAIMED:
					if(claimedAssigneeList == null)
						claimedAssigneeList = new ArrayList<Assignee>();
					claimedAssigneeList.add(unclaimAssigneeList.remove(i--));
					break;
				case FINISHED:
					if(finishedAssigneeList == null)
						finishedAssigneeList = new ArrayList<Assignee>();
					finishedAssigneeList.add(unclaimAssigneeList.remove(i--));
					break;
				default:
					throw new ProcessEngineBugException("查询当前认领状态的指派信息集合时, 出现了错误的办理状态["+unclaimAssigneeList.get(i).getHandleStateInstance()+"]");
			}
		}
		
		ClaimPolicyEntity claimPolicyEntity = ((UserTaskMetadata) entity.getTaskMetadataEntity().getTaskMetadata()).getCandidate().getHandlePolicy().getClaimPolicyEntity();
		return processEngineBeans
				.getAPIContainer()
				.getClaimPolicy(claimPolicyEntity.getName())
				.claimValidate(claimPolicyEntity.getValue(), currentClaimUserId, currentAssigneeList, unclaimAssigneeList, claimedAssigneeList, finishedAssigneeList);
	}
	
	//------------------------------------------------------------------------------------------------------------------
	// SQL
	//------------------------------------------------------------------------------------------------------------------
	/*
	 * 查询用户可进行认领操作的指派信息集合: 
	 * 
	 * select a.id, a.group_id, a.chain_id, a.mode_ from (
	 * 	select taskinst_id, group_id, max(chain_id) chain_id from bpm_ru_assignee 
	 *		where taskinst_id=? and user_id=? and handle_state in (?,?) 
	 * 			group by taskinst_id, group_id
	 * ) r
	 * left join bpm_ru_assignee a on (a.taskinst_id = r.taskinst_id and a.group_id = r.group_id and a.chain_id = r.chain_id)
	 */
	private static final String SQL_QUERY_CAN_CLAIM_ASSIGNEE_LIST = "select a.id, a.group_id, a.chain_id, a.mode_ from (select taskinst_id, group_id, max(chain_id) chain_id from bpm_ru_assignee where taskinst_id=? and user_id=? and handle_state in (?,?) group by taskinst_id, group_id) r left join bpm_ru_assignee a on (a.taskinst_id = r.taskinst_id and a.group_id = r.group_id and a.chain_id = r.chain_id)";
}
