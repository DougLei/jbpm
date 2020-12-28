package com.douglei.bpm.process.handler.gateway;

import com.douglei.bpm.process.handler.HandleParameter;
import com.douglei.bpm.process.handler.TaskHandler;
import com.douglei.bpm.process.metadata.gateway.AbstractGatewayMetadata;

/**
 * 
 * @author DougLei
 */
abstract class AbstractGatewayHandler extends TaskHandler<AbstractGatewayMetadata, HandleParameter>{
	
	/**
	 * 移除不继承的流程变量
	 */
	protected final void removeVariables() {
		// 5, 6, 8, 9  global
		// 3, 4, 8, 9  local
		// 1, 4, 6, 9  transient
		switch(taskMetadata.getUnextendScopeWeight()) {
			case 1:
				handleParameter.getVariableEntities().removeAllTransientVariable();
				break;
			case 3:
				handleParameter.getVariableEntities().removeAllLocalVariable();
				break;
			case 5:
				handleParameter.getVariableEntities().removeAllGlobalVariable();
				break;
			case 6:
				handleParameter.getVariableEntities().removeAllGlobalVariable();
				handleParameter.getVariableEntities().removeAllTransientVariable();
			case 4:
				handleParameter.getVariableEntities().removeAllLocalVariable();
				handleParameter.getVariableEntities().removeAllTransientVariable();
				break;
			case 8:
				handleParameter.getVariableEntities().removeAllGlobalVariable();
				handleParameter.getVariableEntities().removeAllLocalVariable();
				break;
			case 9:
				handleParameter.getVariableEntities().removeAllGlobalVariable();
				handleParameter.getVariableEntities().removeAllLocalVariable();
				handleParameter.getVariableEntities().removeAllTransientVariable();
				break;
			default:
				break;
		}
	}
}
