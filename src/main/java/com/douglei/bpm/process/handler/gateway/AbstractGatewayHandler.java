package com.douglei.bpm.process.handler.gateway;

import com.douglei.bpm.process.handler.AbstractHandleParameter;
import com.douglei.bpm.process.handler.TaskHandler;
import com.douglei.bpm.process.mapping.metadata.gateway.AbstractGatewayMetadata;

/**
 * 
 * @author DougLei
 */
abstract class AbstractGatewayHandler extends TaskHandler<AbstractGatewayMetadata, AbstractHandleParameter>{
	
	/**
	 * 移除不继承的流程变量
	 */
	protected final void removeVariables() {
		boolean[] variableExtend = currentTaskMetadataEntity.getTaskMetadata().getVariableExtend();
		if(!variableExtend[0])
			handleParameter.getVariableEntities().removeAllGlobalVariable();
		if(!variableExtend[1])
			handleParameter.getVariableEntities().removeAllLocalVariable();
		if(!variableExtend[2])
			handleParameter.getVariableEntities().removeAllTransientVariable();
	}
}
