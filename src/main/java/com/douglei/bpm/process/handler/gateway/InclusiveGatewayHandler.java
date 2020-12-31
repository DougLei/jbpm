package com.douglei.bpm.process.handler.gateway;

import com.douglei.bpm.module.ExecutionResult;

/**
 * 
 * @author DougLei
 */
public class InclusiveGatewayHandler extends AbstractGatewayHandler{

	@Override
	public ExecutionResult startup() {
		removeVariables();
		
		
		
		return ExecutionResult.getDefaultSuccessInstance();
	}
	
	@Override
	public ExecutionResult handle() {
		
		
		
		return ExecutionResult.getDefaultSuccessInstance();
	}
}
