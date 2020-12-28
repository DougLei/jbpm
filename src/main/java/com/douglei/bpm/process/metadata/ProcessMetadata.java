package com.douglei.bpm.process.metadata;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.douglei.bpm.process.metadata.entity.StartEventMetadataEntity;
import com.douglei.bpm.process.metadata.entity.TaskMetadataEntity;
import com.douglei.bpm.process.metadata.event.StartEventMetadata;
import com.douglei.bpm.process.metadata.flow.FlowMetadata;
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
	private List<FlowMetadata> flows = new ArrayList<FlowMetadata>();
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
	 * 获取StartEvent元数据
	 * @return
	 */
	public StartEventMetadataEntity getStartEvent() {
		return new StartEventMetadataEntity(startEvent, flows);
	}
	/**
	 * 获取任务元数据实体实例
	 * @param taskId 任务id
	 * @param inputFlows 是否获取输入flow集合
	 * @param outputFlows 是否获取输出flow集合
	 * @param defaultFlow 是否获取默认flow
	 * @return
	 */
	public TaskMetadataEntity getTask(String taskId, boolean inputFlows, boolean outputFlows, boolean defaultFlow) {
		TaskMetadata task = taskMap.get(taskId);
		if(task == null)
			throw new NullPointerException("不存在id为["+taskId+"]的任务");
		return new TaskMetadataEntity(task, flows, inputFlows, outputFlows, task.getDefaultFlowId()==null?false:defaultFlow);
	}
}
