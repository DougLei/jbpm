package com.douglei.bpm.process.handler;

import com.douglei.bpm.process.handler.components.assignee.Assigners;
import com.douglei.bpm.process.metadata.ProcessMetadata;

/**
 * 
 * @author DougLei
 */
public abstract class ExecuteParameter {
	protected String userId; // 操作的用户id
	protected ProcessMetadata processMetadata;
	protected Assigners assigners;
	
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
