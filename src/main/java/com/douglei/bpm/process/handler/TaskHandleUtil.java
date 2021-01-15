package com.douglei.bpm.process.handler;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import com.douglei.bpm.ProcessEngineBeans;
import com.douglei.bpm.bean.annotation.Autowired;
import com.douglei.bpm.bean.annotation.Bean;
import com.douglei.bpm.module.ExecutionResult;
import com.douglei.bpm.process.api.user.assignable.expression.AssignableUserExpressionParameter;
import com.douglei.bpm.process.api.user.bean.factory.UserBean;
import com.douglei.bpm.process.handler.event.end.EndEventHandler;
import com.douglei.bpm.process.handler.event.start.StartEventHandler;
import com.douglei.bpm.process.handler.gateway.ExclusiveGatewayHandler;
import com.douglei.bpm.process.handler.gateway.InclusiveGatewayHandler;
import com.douglei.bpm.process.handler.gateway.ParallelGatewayHandler;
import com.douglei.bpm.process.handler.task.user.UserTaskHandler;
import com.douglei.bpm.process.metadata.TaskMetadata;
import com.douglei.bpm.process.metadata.TaskMetadataEntity;
import com.douglei.bpm.process.metadata.flow.FlowMetadata;
import com.douglei.bpm.process.metadata.task.user.UserTaskMetadata;
import com.douglei.bpm.process.metadata.task.user.candidate.assign.AssignNumber;
import com.douglei.bpm.process.metadata.task.user.candidate.assign.AssignPolicy;
import com.douglei.bpm.process.metadata.task.user.candidate.assign.AssignableUserExpressionEntity;
import com.douglei.tools.instances.ognl.OgnlHandler;

/**
 * 任务办理工具
 * @author DougLei
 */
@Bean
@SuppressWarnings({"rawtypes", "unchecked"})
public class TaskHandleUtil {
	
	@Autowired
	private ProcessEngineBeans processEngineBeans;
	
	// 创建任务办理器实例
	private TaskHandler createTaskHandleInstance(TaskMetadataEntity<? extends TaskMetadata> taskMetadataEntity, HandleParameter handleParameter) {
		TaskHandler taskHandler = null;
		switch(taskMetadataEntity.getTaskMetadata().getType()) {
			// 事件类型
			case START_EVENT:
				taskHandler = new StartEventHandler();
				break;
			case END_EVENT:
				taskHandler = new EndEventHandler();
				break;
			
			// 任务类型
			case USER_TASK:
				taskHandler = new UserTaskHandler();
				break;
			
			// 网关类型
			case EXCLUSIVE_GATEWAY:
				taskHandler = new ExclusiveGatewayHandler();
				break;
			case PARALLEL_GATEWAY:
				taskHandler = new ParallelGatewayHandler();
				break;
			case INCLUSIVE_GATEWAY:
				taskHandler = new InclusiveGatewayHandler();
				break;
			default:
				throw new TaskHandleException("目前还未实现["+taskMetadataEntity.getTaskMetadata().getType().getName()+"]类型的任务办理器");
		}
		taskHandler.setParameters(processEngineBeans, taskMetadataEntity, handleParameter);
		return taskHandler;
	}
	
	/**
	 * 启动任务
	 * @param taskMetadataEntity
	 * @param parameter
	 * @return
	 */
	public ExecutionResult startup(TaskMetadataEntity<? extends TaskMetadata> taskMetadataEntity, HandleParameter parameter) {
		return createTaskHandleInstance(taskMetadataEntity, parameter).startup();
	}
	
	/**
	 * 办理任务
	 * @param taskMetadataEntity
	 * @param parameter
	 * @return
	 */
	public ExecutionResult handle(TaskMetadataEntity<? extends TaskMetadata> taskMetadataEntity, HandleParameter parameter) {
		return createTaskHandleInstance(taskMetadataEntity, parameter).handle();
	}
	
	/**
	 * 任务调度
	 * <p>
	 *  对task中的flows进行调度, 选择第一条匹配的flow执行
	 * @param taskMetadataEntity
	 * @param parameter
	 * @throws TaskDispatchException
	 */
	public void dispatch(TaskMetadataEntity<? extends TaskMetadata> taskMetadataEntity, HandleParameter parameter) throws TaskDispatchException{
		for(FlowMetadata flow : taskMetadataEntity.getOutputFlows()) {
			if(flowMatching(flow, parameter.getVariableEntities().getVariableMap())) {
				dispatch(flow, parameter);
				return;
			}
		}
		
		FlowMetadata defaultOutputFlow = taskMetadataEntity.getDefaultOutputFlow();
		if(defaultOutputFlow == null)
			throw new TaskDispatchException("执行"+taskMetadataEntity.getTaskMetadata()+"任务时, 未能匹配到满足条件的OutputFlow");
		dispatch(defaultOutputFlow, parameter);
	}
	
	/**
	 * flow匹配
	 * <p>
	 * 判断指定的flowMetadata是否满足流转条件
	 * @param flowMetadata
	 * @param variableMap
	 * @return 
	 */
	public boolean flowMatching(FlowMetadata flowMetadata, Map<String, Object> variableMap) {
		String conditionExpression = flowMetadata.getConditionExpression();
		if(conditionExpression == null)
			return true;
		if(variableMap == null) 
			return false;
		return OgnlHandler.getSingleton().getBooleanValue(conditionExpression, variableMap);
	}
	
	/**
	 * flow调度, 属于直接调度
	 * @param flowMetadata
	 * @param parameter
	 */
	public void dispatch(FlowMetadata flowMetadata, HandleParameter parameter) {
		parameter.getTaskEntityHandler().dispatch();
		startup(parameter.getProcessMetadata().getTaskMetadataEntity(flowMetadata.getTarget()), parameter);
	}
	
	
	/**
	 * 获取指定的指派策略下, 具体可指派的所有用户集合
	 * @param assignPolicy 任务的指派策略; 抄送的指派策略; 委托的指派策略; 转办的指派策略
	 * @param currentUserTaskMetadata 当前用户任务的元数据实例
	 * @param handleParameter
	 * @return
	 */
	public List<UserBean> getAssignableUsers(AssignPolicy assignPolicy, UserTaskMetadata currentUserTaskMetadata, HandleParameter handleParameter) {
		List<UserBean> assignableUsers = new ArrayList<UserBean>();
		
		AssignableUserExpressionParameter parameter = new AssignableUserExpressionParameter(currentUserTaskMetadata, handleParameter, processEngineBeans);
		List<UserBean> tempList = null;
		for(AssignableUserExpressionEntity entity : assignPolicy.getAssignableUserExpressionEntities()) {
			tempList = processEngineBeans.getAssignableUserExpressionContainer().get(entity.getName()).getAssignUserList(entity.getValue(), parameter);
			if(tempList != null && !tempList.isEmpty())
				assignableUsers.addAll(tempList);
		}
		
		// 去重
		if(assignableUsers.size() < 2)
			return assignableUsers;
		
		HashSet<UserBean> hashset = new HashSet<UserBean>(assignableUsers);
		if(hashset.size() == assignableUsers.size())
			return assignableUsers;
		
		assignableUsers.clear();
		assignableUsers.addAll(hashset);
		return assignableUsers;
	}

	/**
	 * 验证指派的用户
	 * @param assignedUsers 实际指派的用户集合
	 * @param assignableUsers 具体可指派的所有用户集合
	 * @param assignNumber
	 * @return 0表示验证通过; 1表示实际指派的用户超出可指派用户的范围; 2表示实际指派人数超过上限
	 */
	public int validateAssignedUsers(List<UserBean> assignedUsers, List<UserBean> assignableUsers, AssignNumber assignNumber) {
		// 判断实际指派的人, 是否都存在于可指派的用户集合中
		for (UserBean assignedUser : assignedUsers) {
			if(!assignableUsers.contains(assignedUser))
				return 1;
		}
		
		// 判断实际指派的人数, 是否超过最多可指派的人数
		if(assignNumber.isPercent()) {
			if(assignedUsers.size() > assignNumber.calcUpperLimit(assignableUsers.size()))
				return 2;
			return 0;
		}
		
		if(assignedUsers.size() > assignNumber.getNumber())
			return 2;
		return 0;
	}
}
