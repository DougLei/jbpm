package com.douglei.bpm.process.handler;

import java.util.HashSet;
import java.util.List;

import com.douglei.bpm.ProcessEngineBeans;
import com.douglei.bpm.bean.annotation.Autowired;
import com.douglei.bpm.bean.annotation.Bean;
import com.douglei.bpm.module.Result;
import com.douglei.bpm.process.api.user.assignable.expression.AssignableUserExpressionParameter;
import com.douglei.bpm.process.handler.event.end.EndEventHandler;
import com.douglei.bpm.process.handler.event.start.StartEventHandler;
import com.douglei.bpm.process.handler.gateway.ExclusiveGatewayHandler;
import com.douglei.bpm.process.handler.gateway.InclusiveGatewayHandler;
import com.douglei.bpm.process.handler.gateway.ParallelGatewayHandler;
import com.douglei.bpm.process.handler.task.user.UserTaskHandler;
import com.douglei.bpm.process.mapping.metadata.ProcessNodeMetadata;
import com.douglei.bpm.process.mapping.metadata.TaskMetadata;
import com.douglei.bpm.process.mapping.metadata.TaskMetadataEntity;
import com.douglei.bpm.process.mapping.metadata.flow.FlowMetadata;
import com.douglei.bpm.process.mapping.metadata.listener.ActiveTime;
import com.douglei.bpm.process.mapping.metadata.listener.Listener;
import com.douglei.bpm.process.mapping.metadata.task.user.candidate.assign.AssignNumber;
import com.douglei.bpm.process.mapping.metadata.task.user.candidate.assign.AssignPolicy;
import com.douglei.bpm.process.mapping.metadata.task.user.candidate.assign.AssignableUserExpressionEntity;
import com.douglei.tools.OgnlUtil;

/**
 * 任务办理工具
 * @author DougLei
 */
@Bean
@SuppressWarnings({"rawtypes", "unchecked"})
public class TaskHandleUtil {
	
	@Autowired
	private ProcessEngineBeans processEngineBeans;
	
	
	//---------------------------------------------------------------------------------------------
	// 关于任务流转的api
	//---------------------------------------------------------------------------------------------
	// 创建任务办理器实例
	private TaskHandler createTaskHandleInstance(TaskMetadataEntity<? extends TaskMetadata> taskMetadataEntity, AbstractHandleParameter handleParameter) {
		TaskHandler taskHandler = null;
		switch(taskMetadataEntity.getTaskMetadata().getType()) {
			// 事件
			case START_EVENT:
				taskHandler = new StartEventHandler();
				break;
			case END_EVENT:
				taskHandler = new EndEventHandler();
				break;
			
			// 任务
			case USER_TASK:
				taskHandler = new UserTaskHandler();
				break;
			
			// 网关
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
		taskHandler.setParameters(taskMetadataEntity, handleParameter, processEngineBeans);
		return taskHandler;
	}
	
	/**
	 * 启动任务
	 * @param taskMetadataEntity
	 * @param parameter
	 * @return
	 */
	public Result startup(TaskMetadataEntity<? extends TaskMetadata> taskMetadataEntity, AbstractHandleParameter parameter) {
		return createTaskHandleInstance(taskMetadataEntity, parameter).startup();
	}
	
	/**
	 * 办理任务
	 * @param taskMetadataEntity
	 * @param parameter
	 * @return 返回对象的success=true时, 其Object属性为boolean类型, 标识是否可以进行调度
	 */
	public Result handle(TaskMetadataEntity<? extends TaskMetadata> taskMetadataEntity, AbstractHandleParameter parameter) {
		return createTaskHandleInstance(taskMetadataEntity, parameter).handle();
	}
	
	/**
	 * 匹配flow; 判断指定的flow是否满足流转条件
	 * @param flowMetadata
	 * @param parameter
	 * @return 
	 */
	public boolean flowMatching(FlowMetadata flowMetadata, AbstractHandleParameter parameter) {
		String conditionExpression = flowMetadata.getConditionExpression();
		if(conditionExpression == null)
			return true;
		return OgnlUtil.getBooleanValue(conditionExpression, parameter.getVariableEntities().getVariableMap());
	}
	
	/**
	 * 执行监听程序
	 * @param metadata
	 * @param target
	 */
	public void notifyListners(ProcessNodeMetadata metadata, AbstractHandleParameter handleParameter, ActiveTime target) {
		List<Listener> listeners = metadata.getListeners();
		if(listeners == null)
			return;
		
		listeners.stream().filter(listener -> listener.getActiveTime() == target).forEach(listener -> listener.notify(handleParameter));
	}
	
	//---------------------------------------------------------------------------------------------
	// 关于用户指派的api
	//---------------------------------------------------------------------------------------------
	/**
	 * 获取指定的指派策略下, 可指派的用户id集合
	 * @param procinstId 当前操作的流程实例id
	 * @param taskinstId 当前操作的任务实例id
	 * @param currentHandleUserId 当前办理的用户id
	 * @param assignPolicy
	 * @return
	 */
	public HashSet<String> getAssignableUserIds(String procinstId, String taskinstId, String currentHandleUserId, AssignPolicy assignPolicy) {
		// 获取可指派的用户id集合
		HashSet<String> assignableUserIds = new HashSet<String>();
		AssignableUserExpressionParameter parameter = new AssignableUserExpressionParameter(procinstId, taskinstId, currentHandleUserId);
		for(AssignableUserExpressionEntity entity : assignPolicy.getAssignableUserExpressionEntities()) {
			List<String> userIds = processEngineBeans.getAPIContainer().getAssignableUserExpression(entity.getName()).getUserIds(entity.getValue(), parameter);
			if(userIds != null && !userIds.isEmpty())
				assignableUserIds.addAll(userIds);
		}
		return assignableUserIds;
	}

	/**
	 * 验证指派的用户
	 * @param assignedUserIds 实际指派的用户id集合
	 * @param assignableUserIds 可指派的用户id集合
	 * @param assignNumber
	 * @throws TaskHandleException
	 */
	public void validateAssignedUsers(HashSet<String> assignedUserIds, HashSet<String> assignableUserIds, AssignNumber assignNumber) throws TaskHandleException{
		// 判断实际指派的人, 是否都存在于可指派的用户集合中
		for (String assignedUserId : assignedUserIds) {
			if(!assignableUserIds.contains(assignedUserId))
				throw new TaskHandleException("不能指派配置范围外的人员"); 
		}
		
		// 判断实际指派的人数, 是否超过最多可指派的人数
		int upperLimit = assignNumber.calcUpperLimit(assignableUserIds.size());
		if(assignedUserIds.size() > upperLimit)
			throw new TaskHandleException("实际指派的人数["+assignedUserIds.size()+"]超过配置的上限["+upperLimit+"]");
	}
}
