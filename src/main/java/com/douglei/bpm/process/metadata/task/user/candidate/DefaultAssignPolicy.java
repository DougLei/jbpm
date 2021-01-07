package com.douglei.bpm.process.metadata.task.user.candidate;

import com.douglei.bpm.process.metadata.task.user.candidate.assign.AssignMode;
import com.douglei.bpm.process.metadata.task.user.candidate.assign.AssignPolicy;

/**
 * 
 * @author DougLei
 */
public class DefaultAssignPolicy extends AssignPolicy{
	private static final DefaultAssignPolicy singleton = new DefaultAssignPolicy();
	public static DefaultAssignPolicy getSingleton() {
		return singleton;
	}
	public Object readResolve() {
		return singleton;
	}
	
	private DefaultAssignPolicy() {
		super(AssignMode.ASSIGNED, null);
	}
}
