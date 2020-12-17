package com.douglei.bpm.process.handler;

import java.util.List;

import com.douglei.bpm.process.handler.components.assignee.Assigner;
import com.douglei.bpm.process.handler.components.assignee.Assigners;
import com.douglei.bpm.process.metadata.ProcessMetadata;

/**
 * 
 * @author DougLei
 */
public abstract class ExecuteParameter {
	private ProcessMetadata processMetadata;
	private Assigners assigners;
	
	protected ExecuteParameter(ProcessMetadata processMetadata) {
		this.processMetadata = processMetadata;
	}
	protected ExecuteParameter(ProcessMetadata processMetadata, List<Assigner> assigners) {
		this.processMetadata = processMetadata;
		this.assigners = new Assigners(assigners);
	}
	protected ExecuteParameter(ProcessMetadata processMetadata, Assigners assigners) {
		this.processMetadata = processMetadata;
		this.assigners = assigners;
	}
	
	public final ProcessMetadata getProcessMetadata() {
		return processMetadata;
	}
	public final Assigners getAssigners() {
		return assigners;
	}
	public final int getProcdefId() {
		return processMetadata.getId();
	}
	public abstract int getProcinstId();
}
