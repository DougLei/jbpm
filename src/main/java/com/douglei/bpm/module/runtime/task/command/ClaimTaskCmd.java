package com.douglei.bpm.module.runtime.task.command;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import com.douglei.bpm.ProcessEngineBeans;
import com.douglei.bpm.ProcessEngineException;
import com.douglei.bpm.module.Command;
import com.douglei.bpm.module.ExecutionResult;
import com.douglei.bpm.module.runtime.task.AssignMode;
import com.douglei.bpm.module.runtime.task.Assignee;
import com.douglei.bpm.module.runtime.task.HandleState;
import com.douglei.bpm.module.runtime.task.TaskInstance;
import com.douglei.bpm.process.api.user.task.handle.policy.ClaimPolicy;
import com.douglei.bpm.process.api.user.task.handle.policy.ClaimResult;
import com.douglei.bpm.process.metadata.task.user.UserTaskMetadata;
import com.douglei.bpm.process.metadata.task.user.candidate.handle.ClaimPolicyEntity;
import com.douglei.orm.context.SessionContext;

/**
 * 
 * @author DougLei
 */
public class ClaimTaskCmd implements Command{
	private TaskInstance taskInstance;
	private String currentClaimUserId; // 要认领的用户id
	private Date claimTime;
	public ClaimTaskCmd(TaskInstance taskInstance, String currentClaimUserId) {
		this.taskInstance = taskInstance;
		this.currentClaimUserId = currentClaimUserId;
	}
	
	// 获取认领时间
	private Date getClaimTime() {
		if(claimTime == null)
			claimTime = new Date();
		return claimTime;
	}
	
	@Override
	public ExecutionResult execute(ProcessEngineBeans processEngineBeans) {
		if(!taskInstance.requiredUserHandle())
			return new ExecutionResult("认领失败, ["+taskInstance.getName()+"]任务不支持用户认领");
		if(taskInstance.getTask().isAllClaimed())
			return new ExecutionResult("认领失败, ["+taskInstance.getName()+"]任务的办理权限已被认领完");
		
		// 查询指定userId, 判断其是否满足认领条件
		List<Assignee> assigneeList = SessionContext.getSqlSession()
				.query(Assignee.class, 
						"select id, taskinst_id, group_id, chain_id, mode, handle_state from bpm_ru_assignee where taskinst_id=? and user_id=? and handle_state<>?", 
						Arrays.asList(taskInstance.getTask().getTaskinstId(), currentClaimUserId, HandleState.INVALID.name()));
		if(assigneeList.isEmpty())
			return new ExecutionResult("认领失败, 指定的userId没有["+taskInstance.getName()+"]任务的办理权限");
		
		for (Assignee assignee : assigneeList) {
			if(assignee.getHandleStateInstance().isClaimed())
				return new ExecutionResult("认领失败, 指定的userId已认领["+taskInstance.getName()+"]任务");
		}
		
		// 筛选出协办的指派信息, 并认领
		List<Assignee> assistedAssigneeList = null;
		for(int i=0;i<assigneeList.size();i++) {
			if(assigneeList.get(i).getModeInstance() == AssignMode.ASSISTED) {
				if(assistedAssigneeList == null)
					assistedAssigneeList = new ArrayList<Assignee>(assigneeList.size());
				assistedAssigneeList.add(assigneeList.remove(i--).setHandleStateInstance(HandleState.CLAIMED).setClaimTime(getClaimTime()));
			}
		}
		
		boolean assistedClaimResult = directClaim(assistedAssigneeList);
		if(assigneeList.isEmpty())
			return ExecutionResult.getDefaultSuccessInstance();
		
		// 进行主办的指派信息认领
		ExecutionResult result = claim(assigneeList, processEngineBeans);
		if(assistedClaimResult)
			return ExecutionResult.getDefaultSuccessInstance();
		return result;
	}

	// 直接进行认领, 返回是否认领成功
	private boolean directClaim(List<Assignee> assigneeList) {
		if(assigneeList == null)
			return false;
		
		SessionContext.getTableSession().update(assigneeList);
		return true;
	}
	
	// 任务认领
	private synchronized ExecutionResult claim(List<Assignee> assigneeList, ProcessEngineBeans processEngineBeans) {
		// 查询同组内有没有人已经认领
		List<Object[]> claimedGroupIdList = SessionContext.getSQLSession().query_("Assignee", "querySameGroupClaimed", assigneeList);
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
		if(!canClaim(processEngineBeans))
			return new ExecutionResult("认领失败, ["+taskInstance.getName()+"]任务的办理权限已被认领完");
		
		// 进行认领
		for (Assignee assignee : assigneeList) {
			if(!assignee.isChainLast()) // 不是链的最后, 要将比自己大的都删除掉
				SessionContext.getSqlSession().executeUpdate(
						"delete bpm_ru_assignee where taskinst_id=? and group_id=? and chain_id >?", 
						Arrays.asList(taskInstance.getTask().getTaskinstId(), assignee.getGroupId(), assignee.getChainId()));
			assignee.setHandleStateInstance(HandleState.CLAIMED).setClaimTime(getClaimTime()).setIsChainLast(1);
		}
		
		directClaim(assigneeList);
		return ExecutionResult.getDefaultSuccessInstance();
	}

	/**
	 * 是否可以认领
	 * @param processEngineBeans
	 * @return
	 */
	private boolean canClaim(ProcessEngineBeans processEngineBeans) {
		ClaimPolicyEntity claimPolicyEntity = ((UserTaskMetadata) taskInstance.getTaskMetadataEntity().getTaskMetadata()).getCandidate().getHandlePolicy().getClaimPolicyEntity();
		ClaimPolicy policy = processEngineBeans.getTaskHandlePolicyContainer().getClaimPolicy(claimPolicyEntity.getName());
			
		List<Assignee> assigneeList = SessionContext.getSQLSession().query(Assignee.class, "Assignee", "queryAssigneeClaimSituation", taskInstance.getTask().getTaskinstId());
		List<Assignee> claimedAssigneeList= null; 
		List<Assignee> finishedAssigneeList = null;
		
		for(int i=0;i<assigneeList.size();i++) {
			switch(assigneeList.get(i).getHandleStateInstance()) {
				case UNCLAIM:
					break;
				case CLAIMED:
					if(claimedAssigneeList == null)
						claimedAssigneeList = new ArrayList<Assignee>();
					claimedAssigneeList.add(assigneeList.remove(i--));
					break;
				case FINISHED:
					if(finishedAssigneeList == null)
						finishedAssigneeList = new ArrayList<Assignee>();
					finishedAssigneeList.add(assigneeList.remove(i--));
					break;
				case INVALID:
				case COMPETITIVE_UNCLAIM:
					throw new ProcessEngineException("BUG");
			}
		}
		ClaimResult result = policy.claimValidate(claimPolicyEntity.getValue(), currentClaimUserId, assigneeList, claimedAssigneeList, finishedAssigneeList);
		
		// 处理task的isAllClaimed字段值, 改为全部认领
		if(result.canClaim() && result.getLeftCount() == 0)
			SessionContext.getSqlSession().executeUpdate("update bpm_ru_task set is_all_claimed=1 where taskinst_id=?", Arrays.asList(taskInstance.getTask().getTaskinstId()));
		return result.canClaim();
	}
}
