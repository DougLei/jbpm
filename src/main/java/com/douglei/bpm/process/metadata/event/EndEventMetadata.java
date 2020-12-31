package com.douglei.bpm.process.metadata.event;

import com.douglei.bpm.process.Type;
import com.douglei.bpm.process.metadata.TaskMetadata;

/**
 * 
 * @author DougLei
 */
public class EndEventMetadata extends TaskMetadata {

	public EndEventMetadata(String id, String name) {
		super(id, name);
	}
	
	@Override
	public boolean isVirtual() {
		return true;
	}
	@Override
	public Type getType() {
		return Type.END_EVENT;
	}
}
