package com.douglei.bpm.module.runtime.task.command;

import java.util.ArrayList;
import java.util.Arrays;
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
	private ClaimTaskParameter claimTaskParameter;
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
		
		// 查询指定userId, 判断其是否满足认领条件
		List<Assignee> assigneeList = SessionContext.getSqlSession()
				.query(Assignee.class, 
						"select id, group_id, mode, handle_state from bpm_ru_assignee where taskinst_id=? and user_id=?", 
						Arrays.asList(taskInstance.getTask().getTaskinstId(), currentClaimUserId));
		if(assigneeList.isEmpty())
			return new ExecutionResult("认领失败, 指定的userId没有["+taskInstance.getName()+"]任务的办理权限");
		
		for (Assignee assignee : assigneeList) {
			if(assignee.getHandleStateInstance().isClaimed())
				return new ExecutionResult("认领失败, 指定的userId已认领["+taskInstance.getName()+"]任务");
		}
		
		claimTaskParameter = new ClaimTaskParameter(assigneeList.size(), taskInstance.getTask().getTaskinstId());
		for(int i=0;i<assigneeList.size();i++) {
			if(assigneeList.get(i).getModeInstance() == AssignMode.ASSISTED) 
				claimTaskParameter.addAssigneeId(assigneeList.remove(i--).getId());
		}
		directClaim();
		
		if(!assigneeList.isEmpty())
			return claim(assigneeList, processEngineBeans);
		return ExecutionResult.getDefaultSuccessInstance();
	}

	// 直接认领
	private void directClaim() {
		if(claimTaskParameter.getAssigneeIds().isEmpty())
			return;
		SessionContext.getSQLSession().executeUpdate("Assignee", "claimTask", claimTaskParameter);
		claimTaskParameter.getAssigneeIds().clear();
	}
	
	// 任务认领
	private synchronized ExecutionResult claim(List<Assignee> assigneeList, ProcessEngineBeans processEngineBeans) {
		// 查询同组内有没有人已经认领
		claimTaskParameter.setAssigneeList(assigneeList);
		
		List<Object[]> claimedGroupIdList = SessionContext.getSQLSession().query_("Assignee", "querySameGroupClaimed", claimTaskParameter);
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
			if(assignee.getHandleStateInstance() == HandleState.INVALID) // 将同组中非INVALID的改为INVALID状态
				claimTaskParameter.addGroupId(assignee.getGroupId());
			claimTaskParameter.addAssigneeId(assignee.getId());
		}
		
		if(claimTaskParameter.getGroupIds() != null)
			SessionContext.getSQLSession().executeUpdate("Assignee", "handleState2Invalid", claimTaskParameter);
		directClaim();
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
					throw new ProcessEngineException("BUG");
				case UNCLAIM:
					break;
			}
		}
		ClaimResult result = policy.claimValidate(claimPolicyEntity.getValue(), currentClaimUserId, assigneeList, claimedAssigneeList, finishedAssigneeList);
		
		// 处理task的isAllClaimed字段值, 改为全部认领
		if(result.getLeftCount() == 0)
			SessionContext.getSqlSession().executeUpdate("update bpm_ru_task set is_all_claimed=1 where taskinst_id=?", Arrays.asList(taskInstance.getTask().getTaskinstId()));
		return result.canClaim();
	}
}
