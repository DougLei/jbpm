package com.douglei.bpm.module.runtime.task.command;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import com.douglei.bpm.ProcessEngineBeans;
import com.douglei.bpm.ProcessEngineException;
import com.douglei.bpm.module.Command;
import com.douglei.bpm.module.ExecutionResult;
import com.douglei.bpm.module.runtime.task.Assignee;
import com.douglei.bpm.module.runtime.task.HandleState;
import com.douglei.bpm.module.runtime.task.TaskInstance;
import com.douglei.bpm.process.api.user.task.handle.policy.ClaimResult;
import com.douglei.bpm.process.handler.task.user.assignee.handle.AssigneeQueryByGroupIdSqlCondition;
import com.douglei.bpm.process.metadata.task.user.UserTaskMetadata;
import com.douglei.bpm.process.metadata.task.user.candidate.handle.ClaimPolicyEntity;
import com.douglei.orm.context.SessionContext;

/**
 * 认领任务
 * @author DougLei
 */
public class ClaimTaskCmd implements Command{
	private TaskInstance taskInstance;
	private String currentClaimUserId; // 要认领的用户id
	public ClaimTaskCmd(TaskInstance taskInstance, String currentClaimUserId) {
		this.taskInstance = taskInstance;
		this.currentClaimUserId = currentClaimUserId;
	}
	
	@Override
	public ExecutionResult execute(ProcessEngineBeans processEngineBeans) {
		if(!taskInstance.requiredUserHandle())
			return new ExecutionResult("认领失败, ["+taskInstance.getName()+"]任务不支持用户认领");
		if(taskInstance.getTask().isAllClaimed())
			return new ExecutionResult("认领失败, ["+taskInstance.getName()+"]任务的办理权限已被认领完");
		
		// 查询指定userId, 判断其是否可以认领
		List<Assignee> assigneeList = SessionContext.getSqlSession()
				.query(Assignee.class, 
						SQL_QUERY_CAN_CLAIM_ASSIGNEE_LIST, 
						Arrays.asList(taskInstance.getTask().getTaskinstId(), currentClaimUserId, HandleState.COMPETITIVE_UNCLAIM.name(), HandleState.UNCLAIM.name()));
		if(assigneeList.isEmpty())
			return new ExecutionResult("认领失败, 指定的userId无法认领["+taskInstance.getName()+"]任务");
		return claim(assigneeList, processEngineBeans);
	}

	// 任务认领
	private synchronized ExecutionResult claim(List<Assignee> assigneeList, ProcessEngineBeans processEngineBeans) {
		// 查询同组内有没有人已经认领
		List<Object[]> claimedGroupIdList = SessionContext.getSQLSession().query_("Assignee", "querySameGroupClaimed", new AssigneeQueryByGroupIdSqlCondition(taskInstance.getTask().getTaskinstId(), assigneeList));
		if(claimedGroupIdList.size() > 0) {
			if(claimedGroupIdList.size() == assigneeList.size())
				return new ExecutionResult("认领失败, ["+taskInstance.getName()+"]任务已被认领");
			
			int claimedGroupId;
			for (Object[] claimedGroupIdArr : claimedGroupIdList) {
				claimedGroupId = Integer.parseInt(claimedGroupIdArr[0].toString());
				for(int i=0;i<assigneeList.size();i++) {
					if(assigneeList.get(i).getGroupId() == claimedGroupId) {
						assigneeList.remove(i--);
						break;
					}
				}
			}
		}
		
		// 判断当前用户能否认领
		if(!canClaim(assigneeList, processEngineBeans))
			return new ExecutionResult("认领失败, ["+taskInstance.getName()+"]任务的办理权限已被认领完");
		
		// 进行认领
		Date claimTime = new Date();
		for (Assignee assignee : assigneeList) {
			if(!assignee.isChainLast()) // 不是链的最后, 要将比自己大的都删除掉
				SessionContext.getSqlSession().executeUpdate(
						"delete bpm_ru_assignee where taskinst_id=? and group_id=? and chain_id >?", 
						Arrays.asList(taskInstance.getTask().getTaskinstId(), assignee.getGroupId(), assignee.getChainId()));
			assignee.claim(claimTime);
		}
		
		SessionContext.getTableSession().update(assigneeList);
		return ExecutionResult.getDefaultSuccessInstance();
	}

	/**
	 * 是否可以认领
	 * @param currentAssigneeList 当前进行认领的用户的指派信息集合
	 * @param processEngineBeans
	 * @return
	 */
	private boolean canClaim(List<Assignee> currentAssigneeList, ProcessEngineBeans processEngineBeans) {
		List<Assignee> unclaimAssigneeList = SessionContext.getSQLSession().query(Assignee.class, "Assignee", "queryAssigneeClaimSituation", taskInstance.getTask().getTaskinstId());
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
				case INVALID:
				case COMPETITIVE_UNCLAIM:
					throw new ProcessEngineException("BUG");
			}
		}
		
		ClaimPolicyEntity claimPolicyEntity = ((UserTaskMetadata) taskInstance.getTaskMetadataEntity().getTaskMetadata()).getCandidate().getHandlePolicy().getClaimPolicyEntity();
		ClaimResult result = processEngineBeans
				.getTaskHandlePolicyContainer()
				.getClaimPolicy(claimPolicyEntity.getName())
				.claimValidate(claimPolicyEntity.getValue(), currentClaimUserId, currentAssigneeList, unclaimAssigneeList, claimedAssigneeList, finishedAssigneeList);
		
		// 处理task的isAllClaimed字段值, 改为全部认领
		if(result.canClaim() && result.getLeftCount() == 0)
			SessionContext.getSqlSession().executeUpdate("update bpm_ru_task set is_all_claimed=1 where taskinst_id=?", Arrays.asList(taskInstance.getTask().getTaskinstId()));
		return result.canClaim();
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
