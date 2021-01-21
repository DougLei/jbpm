package com.douglei.bpm.process.handler;

import java.util.Date;

import com.douglei.bpm.process.metadata.ProcessMetadata;

/**
 * 办理参数抽象类
 * @author DougLei
 */
public abstract class AbstractHandleParameter implements HandleParameter {
	private Date currentDate = new Date(); // 当前时间
	protected String processInstanceId; // 流程实例id
	protected String businessId; // 本次办理关联的业务id
	protected ProcessMetadata processMetadata; // 流程元数据实例
	
	protected TaskEntityHandler taskEntityHandler = new TaskEntityHandler(); // 任务实体处理器
	protected UserEntity userEntity; // 办理的用户实体
	protected VariableEntities variableEntities; // 办理中的流程变量
	
	@Override
	public final Date getCurrentDate() {
		return currentDate;
	}
	@Override
	public final String getProcessInstanceId() {
		return processInstanceId;
	}
	@Override
	public final String getBusinessId() {
		return businessId;
	}
	@Override
	public final ProcessMetadata getProcessMetadata() {
		return processMetadata;
	}
	
	@Override
	public final TaskEntityHandler getTaskEntityHandler() {
		return taskEntityHandler;
	}
	@Override
	public final UserEntity getUserEntity() {
		return userEntity;
	}
	@Override
	public final VariableEntities getVariableEntities() {
		return variableEntities;
	}
}
