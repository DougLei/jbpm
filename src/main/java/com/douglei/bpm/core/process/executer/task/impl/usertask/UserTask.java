package com.douglei.bpm.core.process.executer.task.impl.usertask;

import java.util.List;

import com.douglei.bpm.core.process.executer.task.Task;
import com.douglei.bpm.core.process.executer.task.impl.usertask.option.Option;

/**
 * 
 * @author DougLei
 */
public class UserTask extends Task {
	private String pageID;
	private String timeLimitExpr;
	
	
	
	private boolean allowActivation = true;
	
	
	private Candidate candidate;
	private List<Option> options;
	
	
	protected UserTask(String id, String name) {
		super(id, name);
	}
}