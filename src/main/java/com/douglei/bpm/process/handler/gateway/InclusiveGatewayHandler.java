package com.douglei.bpm.process.handler.gateway;

/**
 * 
 * @author DougLei
 */
public class InclusiveGatewayHandler extends ParallelGatewayHandler{

	@Override
	protected boolean ignoreFlowCondition() {
		return false;
	}
}
