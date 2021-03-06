package com.douglei.bpm.process.mapping.metadata;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.douglei.bpm.process.mapping.metadata.event.StartEventMetadata;
import com.douglei.bpm.process.mapping.metadata.flow.FlowMetadata;
import com.douglei.orm.mapping.metadata.Metadata;
import com.douglei.tools.StringUtil;

/**
 * 
 * @author DougLei
 */
public class ProcessMetadata implements Metadata {
	private int id; 
	private String code;
	private String version;
	private String name;
	private String title;
	private String pageID;
	
	private StartEventMetadata startEvent;
	private List<FlowMetadata> flows = new ArrayList<FlowMetadata>();
	private Map<String, TaskMetadata> taskMap = new HashMap<String, TaskMetadata>();
	
	public ProcessMetadata(int id, String code, String version, String name, String title, String pageID) {
		this.id = id;
		this.code = code;
		this.version = version;
		this.name = StringUtil.isEmpty(name)?(code+":"+version):name;
		this.title = StringUtil.isEmpty(title)?this.name:title;
		this.pageID = StringUtil.isEmpty(pageID)?null:pageID;
	}
	public void setStartEvent(StartEventMetadata startEvent) {
		this.startEvent = startEvent;
	}
	public void addFlow(FlowMetadata flow) {
		this.flows.add(flow);
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
	
	/**
	 * 获取StartEvent元数据实例
	 * @return
	 */
	public StartEventMetadata getStartEventMetadata() {
		return startEvent;
	}
	/**
	 * 获取StartEvent元数据实体实例
	 * @return
	 */
	public TaskMetadataEntity<StartEventMetadata> getStartEventMetadataEntity() {
		return new TaskMetadataEntity<StartEventMetadata>(this, startEvent, flows);
	}
	/**
	 * 获取任务元数据实体实例
	 * @param taskId 任务id
	 * @return
	 * @throws TaskNotExistsException
	 */
	public TaskMetadataEntity<TaskMetadata> getTaskMetadataEntity(String taskId) throws TaskNotExistsException{
		TaskMetadata task = taskMap.get(taskId);
		if(task == null)
			throw new TaskNotExistsException(taskId);
		return new TaskMetadataEntity<TaskMetadata>(this, task, flows);
	}
}
