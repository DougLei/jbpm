package com.douglei.bpm.process.handler.gateway;

import com.douglei.bpm.process.mapping.metadata.flow.FlowMetadata;

/**
 * 
 * @author DougLei
 */
public class InclusiveGatewayHandler extends ParallelGatewayHandler{

	@Override
	protected boolean flowMatching(FlowMetadata flow) {
		return processEngineBeans.getTaskHandleUtil().flowMatching(flow, handleParameter);
	}
}
