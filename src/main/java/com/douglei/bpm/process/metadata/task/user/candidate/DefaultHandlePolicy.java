package com.douglei.bpm.process.metadata.task.user.candidate;

import com.douglei.bpm.process.metadata.task.user.candidate.handle.HandlePolicy;

/**
 * 
 * @author DougLei
 */
public class DefaultHandlePolicy extends HandlePolicy{
	private static final DefaultHandlePolicy singleton = new DefaultHandlePolicy();
	public static DefaultHandlePolicy getSingleton() {
		return singleton;
	}
	public Object readResolve() {
		return singleton;
	}
	
	private DefaultHandlePolicy() {
		super(false, false, null);
	}
}
