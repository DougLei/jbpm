package com.douglei.bpm.process.executor;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import com.douglei.bpm.module.components.ExecutionResult;
import com.douglei.bpm.module.runtime.instance.StartParameter;
import com.douglei.bpm.module.runtime.instance.entity.ProcessRuntimeInstance;
import com.douglei.bpm.process.executor.task.Task;
import com.douglei.bpm.process.executor.task.event.StartEvent;
import com.douglei.tools.utils.StringUtil;

/**
 * 
 * @author DougLei
 */
public class Process implements Serializable {
	private int id; // 即流程定义id
	private String code;
	private String version;
	private String name;
	private String title;
	private String pageID;
	
	private StartEvent startEvent;
	private Map<String, Task> taskMap = new HashMap<String, Task>();
	
	public Process(int processDefinitionId, String code, String version, String name, String title, String pageID) {
		this.id = processDefinitionId;
		this.code = code;
		this.version = version;
		this.name = StringUtil.isEmpty(name)?(code+":"+version):name;
		this.title = StringUtil.isEmpty(title)?this.name:title;
		this.pageID = pageID;
	}
	
	public void setStartEvent(StartEvent startEvent) {
		this.startEvent = startEvent;
	}
	public void addTask(Task task) {
		this.taskMap.put(task.getId(), task);
	}
	
	public String getId() {
		// TODO
		return null;
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

	/**
	 * 启动流程
	 * @param starter
	 * @return
	 */
	public ExecutionResult<ProcessRuntimeInstance> start(StartParameter starter) {
		// TODO Auto-generated method stub
		
		
		startEvent.execute();
		
		
		return null;
	}
}
