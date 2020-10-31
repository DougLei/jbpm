package com.douglei.bpm.process.executer;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import com.douglei.bpm.process.executer.task.Task;
import com.douglei.bpm.process.executer.task.event.StartEvent;
import com.douglei.tools.utils.StringUtil;

/**
 * 
 * @author DougLei
 */
public class Process implements Serializable {
	private String name;
	private String code;
	private String version;
	private String titleExpr;
	private String pageID;
	
	private StartEvent startEvent;
	private Map<String, Task> taskMap = new HashMap<String, Task>();
	
	public Process(String name, String code, String version, String titleExpr, String pageID) {
		this.name = StringUtil.isEmpty(name)?(code+":"+version):name;
		this.code = code;
		this.version = version;
		this.titleExpr = StringUtil.isEmpty(titleExpr)?this.name:titleExpr;
		this.pageID = pageID;
	}
	
	public void setStartEvent(StartEvent startEvent) {
		this.startEvent = startEvent;
	}
	public void addTask(Task task) {
		taskMap.put(task.getId(), task);
	}
	
	public String getTitle(Object parameter) {
		return titleExpr;
	}
	
	public String getName() {
		return name;
	}
	public String getCode() {
		return code;
	}
	public String getVersion() {
		return version;
	}
	public String getPageID() {
		return pageID;
	}

	public int getId() {
		// TODO Auto-generated method stub
		return 0;
	}
}
