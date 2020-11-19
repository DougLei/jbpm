package com.douglei.bpm.process.node.task.user;

import java.util.List;

import com.douglei.bpm.process.node.task.Task;
import com.douglei.bpm.process.node.task.user.option.Option;

/**
 * 
 * @author DougLei
 */
public class UserTask extends Task {
	private String pageID;
	private String timeLimitExpr;
	
	
	private Candidate candidate;
	private List<Option> options;
	
	
	protected UserTask(String id, String name) {
		super(id, name);
	}
}