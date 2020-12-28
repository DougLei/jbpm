package com.douglei.bpm.process.metadata;

/**
 * 
 * @author DougLei
 */
public abstract class MultiFlowTaskMetadata extends TaskMetadata{
	private String defaultFlowId;
	
	protected MultiFlowTaskMetadata(String id, String name, String defaultFlowId) {
		super(id, name);
		this.defaultFlowId = defaultFlowId;
	}

	public String getDefaultFlowId() {
		return defaultFlowId;
	}
}
