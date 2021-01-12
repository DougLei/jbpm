package com.douglei.bpm.process.api.user.assignable.expression;

import com.douglei.bpm.ProcessEngineBeans;
import com.douglei.bpm.process.handler.HandleParameter;
import com.douglei.bpm.process.handler.VariableEntities;
import com.douglei.bpm.process.metadata.task.user.UserTaskMetadata;

/**
 * 表达式在获取具体可指派的用户集合时, 需要的参数
 * @author DougLei
 */
public class AssignableUserExpressionParameter {
	private HandleParameter handleParameter;
	
	/**
	 * 
	 * @param metadata
	 * @param handleParameter
	 * @param processEngineBeans
	 */
	public AssignableUserExpressionParameter(UserTaskMetadata metadata, HandleParameter handleParameter, ProcessEngineBeans processEngineBeans) {
		// TODO 持续补充
		
		
		this.handleParameter = handleParameter;
	}

	/**
	 * 获取当前处理的任务的流程变量
	 * @return
	 */
	public VariableEntities getCurrentTaskVariables() {
		return handleParameter.getVariableEntities();
	}
}
