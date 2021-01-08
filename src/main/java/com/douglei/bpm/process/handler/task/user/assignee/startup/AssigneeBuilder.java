package com.douglei.bpm.process.handler.task.user.assignee.startup;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

import com.douglei.bpm.ProcessEngineBeans;
import com.douglei.bpm.module.runtime.task.Assignee;
import com.douglei.bpm.process.api.user.assignable.expression.AssignableUserExpressionParameter;
import com.douglei.bpm.process.api.user.bean.factory.UserBean;
import com.douglei.bpm.process.handler.HandleParameter;
import com.douglei.bpm.process.handler.TaskHandleException;
import com.douglei.bpm.process.metadata.task.user.UserTaskMetadata;
import com.douglei.bpm.process.metadata.task.user.candidate.assign.AssignPolicy;
import com.douglei.orm.context.SessionContext;

/**
 * 启动任务时, 指派信息构建器
 * @author DougLei
 */
public class AssigneeBuilder {
	private String code;
	private String version;
	private List<UserBean> assignedUsers; 
	
	/**
	 * 
	 * @param code 任务所属流程定义的code
	 * @param version 任务所属流程定义的version
	 * @param assignedUsers 上一环节实际指派的用户集合
	 */
	public AssigneeBuilder(String code, String version, List<UserBean> assignedUsers) {
		this.code = code;
		this.version = version;
		this.assignedUsers = assignedUsers;
	}
	
	/**
	 * 对指派的用户集合进行预处理
	 * <p>
	 * 主要包括指派用户的验证, 以及静态指派用户的获取
	 * @param metadata 当前用户任务的元数据实例
	 * @param handleParameter
	 * @param processEngineBeans
	 * @throws AssignedUserValidateException
	 */
	public void pretreatment(UserTaskMetadata metadata, HandleParameter handleParameter, ProcessEngineBeans processEngineBeans) throws AssignedUserValidateException{
		// 根据指派策略, 进行基础验证和处理
		AssignPolicy assignPolicy = metadata.getCandidate().getAssignPolicy();
		if(assignPolicy.isDynamic()) {
			if(assignedUsers.isEmpty())
				throw new TaskHandleException("[id="+metadata.getId()+", name="+metadata.getName()+", isDynamicAssign="+assignPolicy.isDynamic()+"]的UserTask未指派办理人员");
			if(!assignPolicy.getAssignNumber().isPercent() && assignPolicy.getAssignNumber().getNumber() < assignedUsers.size())
				throw new TaskHandleException("[id="+metadata.getId()+", name="+metadata.getName()+", isDynamicAssign="+assignPolicy.isDynamic()+"]的UserTask, 实际指派的人数["+assignedUsers.size()+"]大于配置的上限["+assignPolicy.getAssignNumber().getNumber()+"]");
		}
		
		// 获取具体可指派的用户集合
		List<UserBean> assignableUsers = new ArrayList<UserBean>();
		AssignableUserExpressionParameter parameter = new AssignableUserExpressionParameter(metadata, handleParameter, processEngineBeans);
		assignPolicy.getAssignableUserExpressionEntities().forEach(entity -> {
			assignableUsers.addAll(processEngineBeans.getAssignableUserExpressionContainer().get(entity.getName()).getAssignUserList(entity.getValue(), entity.getExtendValue(), parameter));
		});
		if(assignableUsers.isEmpty())
			throw new TaskHandleException("[id="+metadata.getId()+", name="+metadata.getName()+", isDynamicAssign="+assignPolicy.isDynamic()+"]的UserTask, 不存在可指派的办理人员");
		
		// 根据指派策略, 可指派的用户集合, 和实际指派的用户集合, 进行验证
		if(assignPolicy.isDynamic()) {
			
			// 判断实际指派的人, 是否都存在于可指派的用户集合中
			HashSet<UserBean> temp = new HashSet<UserBean>(assignableUsers);
			for (UserBean assignedUser : assignedUsers) {
				if(!temp.contains(assignedUser))
					throw new TaskHandleException("[id="+metadata.getId()+", name="+metadata.getName()+", isDynamicAssign="+assignPolicy.isDynamic()+"]的UserTask, 不能指派配置范围外的 "+assignedUser+" 做为办理人员");
			}
			
			// 判断实际指派的人数, 是否超过最多可指派的人数百分比
			if(assignPolicy.getAssignNumber().isPercent()) {
				int max = assignableUsers.size()*assignPolicy.getAssignNumber().getNumber(); 
				if(max%100 > 0 && assignPolicy.getAssignNumber().isCeiling()) {
					max = max/100 + 1;
				}else {
					max = max/100;
				}
				if(max == 0)
					max = 1;
				
				if(max < assignedUsers.size())
					throw new TaskHandleException("[id="+metadata.getId()+", name="+metadata.getName()+", isDynamicAssign="+assignPolicy.isDynamic()+"]的UserTask, 实际指派的人数["+assignedUsers.size()+"]大于配置的上限["+max+"][total="+assignableUsers.size()+", value="+assignPolicy.getAssignNumber().getNumber()+"%, ceiling="+assignPolicy.getAssignNumber().isCeiling()+"]");
			}
		}else {
			// 静态指派, 忽略动态指派的用户
			if(assignedUsers.size() > 0)
				assignedUsers.clear();
			assignedUsers.addAll(assignableUsers);
		}
		
		if(assignedUsers.isEmpty())
			throw new TaskHandleException("[id="+metadata.getId()+", name="+metadata.getName()+", isDynamicAssign="+assignPolicy.isDynamic()+"]的UserTask未指派办理人员");
	}
	
	/**
	 * 保存指派信息
	 * @param taskinstId 当前的任务实例id
	 * @throws TaskHandleException
	 */
	public void save(String taskinstId) throws TaskHandleException{
		if(assignedUsers.size() > 1) // 去重
			assignedUsers = assignedUsers.parallelStream().distinct().collect(Collectors.toList());
		
		// 查询指派用户的委托数据, 将处理后的指派数据保存到运行表
		SqlCondition queryCondition = new SqlCondition(assignedUsers);
		DelegationHandler delegationHandler = new DelegationHandler(
				SessionContext.getSQLSession().query(DelegationInfo.class, "Assignee", "queryDelegations", queryCondition), 
				queryCondition, code, version);
		
		List<Assignee> assigneeList = new ArrayList<Assignee>(assignedUsers.size() + 5); // +5是备用的长度
		int groupId = 1;
		for (UserBean assignedUser : assignedUsers) 
			delegationHandler.addAssignee(taskinstId, groupId++, null, assignedUser.getUserId(), null, assigneeList);
		
		SessionContext.getTableSession().save(assigneeList);
	}
}
