package com.douglei.bpm.process.metadata;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import com.douglei.bpm.process.metadata.node.TaskMetadata;
import com.douglei.bpm.process.metadata.node.event.StartEventMetadata;
import com.douglei.tools.utils.StringUtil;

/**
 * 
 * @author DougLei
 */
public class ProcessMetadata implements Serializable {
	private int id; 
	private String code;
	private String version;
	private String name;
	private String title;
	private String pageID;
	
	private StartEventMetadata startEvent;
	private Map<String, TaskMetadata> taskMap = new HashMap<String, TaskMetadata>();
	
	public ProcessMetadata(int processDefinitionId, String code, String version, String name, String title, String pageID) {
		this.id = processDefinitionId;
		this.code = code;
		this.version = version;
		this.name = StringUtil.isEmpty(name)?(code+":"+version):name;
		this.title = StringUtil.isEmpty(title)?this.name:title;
		this.pageID = pageID;
	}
	public void setStartEvent(StartEventMetadata startEvent) {
		this.startEvent = startEvent;
	}
	public void addTask(TaskMetadata task) {
		this.taskMap.put(task.getId(), task);
	}
	
	public int getId() {
		return id;
	}
	public String getCode() {
		return code;
	}
	public String getVersion() {
		return version;
	}
	public String getName() {
		return name;
	}
	public String getTitle() {
		return title;
	}
	public String getPageID() {
		return pageID;
	}
	public StartEventMetadata getStartEvent() {
		return startEvent;
	}
	public TaskMetadata getTask(String taskId) {
		TaskMetadata task = taskMap.get(taskId);
		if(task == null)
			throw new NullPointerException("不存在id为["+taskId+"]的任务");
		return task;
	}
}
