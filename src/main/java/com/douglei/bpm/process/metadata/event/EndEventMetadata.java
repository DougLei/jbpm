package com.douglei.bpm.process.metadata.event;

import java.util.List;

import com.douglei.bpm.ProcessEngineException;
import com.douglei.bpm.process.Type;
import com.douglei.bpm.process.metadata.TaskMetadata;
import com.douglei.bpm.process.metadata.flow.FlowMetadata;

/**
 * 
 * @author DougLei
 */
public class EndEventMetadata extends TaskMetadata {

	public EndEventMetadata(String id, String name) {
		super(id, name);
	}
	
	@Override
	public boolean supportMultiFlow() {
		return false;
	}
	
	@Override
	public void addFlow(FlowMetadata flow) {
		throw new ProcessEngineException("EndEvent不能使用Flow");
	}

	@Override
	public List<FlowMetadata> getFlows() {
		throw new ProcessEngineException("EndEvent不能使用Flow");
	}
	
	@Override
	public FlowMetadata getDefaultFlow() {
		throw new ProcessEngineException("EndEvent不能使用Flow");
	}
	
	@Override
	public boolean supportFlowConditionExpr() {
		throw new ProcessEngineException("EndEvent不能使用Flow");
	}
	
	@Override
	public Type getType() {
		return Type.END_EVENT;
	}

}
