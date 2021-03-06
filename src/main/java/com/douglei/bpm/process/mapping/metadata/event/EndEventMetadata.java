package com.douglei.bpm.process.mapping.metadata.event;

import com.douglei.bpm.process.Type;
import com.douglei.bpm.process.mapping.metadata.TaskMetadata;

/**
 * 
 * @author DougLei
 */
public class EndEventMetadata extends TaskMetadata {

	public EndEventMetadata(String id, String name) {
		super(id, name, null);
	}
	
	@Override
	public boolean supportOutFlows() {
		return false;
	}
	@Override
	public Type getType() {
		return Type.END_EVENT;
	}
}
