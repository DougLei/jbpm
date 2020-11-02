package com.douglei.bpm.process.parser;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.douglei.bpm.bean.annotation.Attribute;
import com.douglei.bpm.bean.annotation.Bean;
import com.douglei.bpm.process.executer.Process;
import com.douglei.bpm.process.executer.flow.Flow;
import com.douglei.bpm.process.executer.task.Task;
import com.douglei.bpm.process.executer.task.event.EndEvent;
import com.douglei.bpm.process.executer.task.event.StartEvent;
import com.douglei.bpm.process.parser.element.FlowElement;
import com.douglei.bpm.process.parser.element.TaskElement;
import com.douglei.bpm.process.parser.impl.flow.FlowParser;
import com.douglei.bpm.process.parser.impl.task.event.StartEventParser;
import com.douglei.tools.utils.StringUtil;

/**
 * process解析器
 * @author DougLei
 */
@Bean(transaction = false)
public class ProcessParser {
	
	@Attribute
	private StartEventParser startEventParser;
	
	@Attribute
	private FlowParser flowParser;
	
	@Attribute
	private ParserContainer parserContainer;
	
	@SuppressWarnings("unchecked")
	public Process parse(int id, String content) throws ProcessParseException {
		Document document;
		try {
			document = new SAXReader().read(new ByteArrayInputStream(content.getBytes()));
		} catch (DocumentException e) {
			throw new ProcessParseException("读取工作流配置内容时出现异常", e);
		}
		
		Element processElement = document.getRootElement().element("process");
		String code = processElement.attributeValue("code");
		if(StringUtil.isEmpty(code))
			throw new ProcessParseException("工作流中的编码值不能为空");
		
		String version = processElement.attributeValue("version");
		if(StringUtil.isEmpty(version))
			throw new ProcessParseException("工作流中的版本值不能为空");
		
		Process process = new Process(id, code, version, processElement.attributeValue("name"), processElement.attributeValue("titleExpr"), processElement.attributeValue("pageID"));
		buildProcessStruct(process, processElement.elements());
		return process;
	}
	
	/**
	 * 构建流程结构
	 * @param process
	 * @param elements
	 */
	private void buildProcessStruct(Process process, List<Element> elements) {
		StartEvent startEvent = null;
		List<FlowElement> flowElements = new ArrayList<FlowElement>(elements.size());
		Map<String, Object> taskMap = new HashMap<String, Object>();
		
		String id, elementName;
		for (Element element : elements) {
			id = element.attributeValue("id");
			if(StringUtil.isEmpty(id))
				throw new ProcessParseException("工作流中, 连线/任务/网关/事件的id值不能为空");
			
			elementName = element.getName();
			if(elementName.equals(startEventParser.elementName())) {
				if(startEvent != null)
					throw new ProcessParseException("工作流中只能配置一个起始事件");
				startEvent = startEventParser.parse(new TaskElement(id, element));
			}else if(elementName.equals(flowParser.elementName())) {
				flowElements.add(new FlowElement(id, element));
			}else { 
				if(taskMap.containsKey(id))
					throw new ProcessParseException("工作流中的任务/网关/事件, 出现重复的id值: " + id);
				taskMap.put(id, element);
			}
		}
		
		if(startEvent == null)
			throw new ProcessParseException("工作流中必须配置起始事件");
		process.setStartEvent(startEvent);
		
		linkTaskAndFlow(startEvent, flowElements, taskMap, process);
		
		taskMap.clear();
		flowElements.clear();
	}

	/**
	 *  将任务和流进行连接
	 * @param sourceTask
	 * @param flowElements
	 * @param taskMap
	 * @param process
	 */
	private void linkTaskAndFlow(Task sourceTask, List<FlowElement> flowElements, Map<String, Object> taskMap, Process process) {
		boolean taskExistsFlow = false;
		if(!flowElements.isEmpty()) {
			for (int i = 0; i < flowElements.size(); i++) {
				if(sourceTask.getId().equals(flowElements.get(i).getSource())) {
					taskExistsFlow = true;
					FlowElement flowElement = flowElements.remove(i--);
					
					Object taskObj = taskMap.get(flowElement.getTarget());
					if(taskObj == null)
						throw new ProcessParseException("工作流中不存在id="+flowElement.getTarget()+"的任务/网关/事件");
					
					Flow flow = flowParser.parse(flowElement);
					sourceTask.addFlow(flow);
					
					Task targetTask = null;
					if(taskObj instanceof Element) {
						targetTask = parserContainer.parse(new TaskElement(flowElement.getTarget(), (Element)taskObj));
						taskMap.put(targetTask.getId(), targetTask);
					}else {
						targetTask = (Task) taskObj;
					}
					
					flow.setTargetTask(targetTask);
					if(targetTask != taskObj) { // 证明targetTask是第一次解析
						process.addTask(targetTask);
						if(!(targetTask instanceof EndEvent)) {
							linkTaskAndFlow(targetTask, flowElements, taskMap, process);
							i = -1;
						}
					}
				}
			}
		}
		
		if(!taskExistsFlow)
			throw new ProcessParseException("工作流中, " + sourceTask.getName() + "不是结束事件, 必须配置相应的连线");
	}
}
