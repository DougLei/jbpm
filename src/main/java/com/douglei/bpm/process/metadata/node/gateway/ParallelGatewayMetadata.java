package com.douglei.bpm.process.metadata.node.gateway;

import com.douglei.bpm.process.Type;
import com.douglei.bpm.process.metadata.node.TaskMetadata;

/**
 * 
 * @author DougLei
 */
public class ParallelGatewayMetadata extends TaskMetadata {

	public ParallelGatewayMetadata(String id, String name) {
		super(id, name);
	}
	
	@Override
	public Type getType() {
		return Type.PARALLEL_GATEWAY;
	}
}
