package com.douglei.bpm.process.metadata;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import com.douglei.bpm.module.components.expression.Expression;
import com.douglei.bpm.module.components.expression.ExpressionConstants;
import com.douglei.bpm.process.metadata.node.task.TaskMetadata;
import com.douglei.bpm.process.metadata.node.task.events.StartEventMetadata;
import com.douglei.tools.utils.StringUtil;

/**
 * 
 * @author DougLei
 */
public class ProcessMetadata implements Serializable {
	private int id; 
	private String name;
	private Expression titleExpression;
	private String pageID;
	
	private StartEventMetadata startEvent;
	private Map<String, TaskMetadata> taskMap = new HashMap<String, TaskMetadata>();
	
	public ProcessMetadata(int processDefinitionId, String code, String version, String name, String title, String pageID) {
		this.id = processDefinitionId;
		this.name = StringUtil.isEmpty(name)?(code+":"+version):name;
		this.titleExpression = new Expression(StringUtil.isEmpty(title)?this.name:title);
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
	public String getName() {
		return name;
	}
	public String getTitle(Map<String, Object> variables) {
		String ts = titleExpression.getSource();
		if(variables == null || !titleExpression.exists())
			return ts;
		
		Object value = null;
		do {
			value = titleExpression.getValue(variables);
			if(value != null)
				ts = ts.replaceAll(ExpressionConstants.PREFIX_4_REGEX + titleExpression.getExpression() + ExpressionConstants.SUFFIX, value.toString());
		}while(titleExpression.next());
		return ts;
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
