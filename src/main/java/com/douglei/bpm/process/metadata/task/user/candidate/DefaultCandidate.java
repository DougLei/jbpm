package com.douglei.bpm.process.metadata.task.user.candidate;

/**
 * 
 * @author DougLei
 */
public class DefaultCandidate extends Candidate{
	private static final DefaultCandidate singleton = new DefaultCandidate();
	public static DefaultCandidate getSingleton() {
		return singleton;
	}
	public Object readResolve() {
		return singleton;
	}
	
	private DefaultCandidate() {
		super(DefaultAssignPolicy.getSingleton(), DefaultHandlePolicy.getSingleton());
	}
}
