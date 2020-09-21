package com.douglei.bpm.core.process.parser.impl;

import java.io.ByteArrayInputStream;
import java.io.File;
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
import com.douglei.bpm.core.process.executer.Process;
import com.douglei.bpm.core.process.executer.flow.Flow;
import com.douglei.bpm.core.process.executer.task.Task;
import com.douglei.bpm.core.process.executer.task.event.EndEvent;
import com.douglei.bpm.core.process.executer.task.event.StartEvent;
import com.douglei.bpm.core.process.parser.Parser;
import com.douglei.bpm.core.process.parser.ParserContainer;
import com.douglei.bpm.core.process.parser.ProcessParseException;
import com.douglei.bpm.core.process.parser.element.FlowElement;
import com.douglei.bpm.core.process.parser.element.TaskElement;
import com.douglei.bpm.core.process.parser.impl.flow.FlowParser;
import com.douglei.bpm.core.process.parser.impl.task.event.StartEventParser;
import com.douglei.tools.utils.StringUtil;

/**
 * process解析器
 * @author DougLei
 */
@Bean(transaction = false)
public class ProcessParser implements Parser<String, Process> {
	
	@Attribute
	private StartEventParser startEventParser;
	
	@Attribute
	private FlowParser flowParser;
	
	@Attribute
	private ParserContainer parserContainer;
	
	@Override
	public String elementName() {
		return "process";
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public Process parse(String content) throws ProcessParseException {
		Document document;
		try {
			document = new SAXReader().read(new ByteArrayInputStream(content.getBytes()));
		} catch (DocumentException e) {
			throw new ProcessParseException("读取工作流配置内容时出现异常", e);
		}
		
		Element processElement = document.getRootElement().element(elementName());
		
		String code = processElement.attributeValue("code");
		if(StringUtil.isEmpty(code))
			throw new ProcessParseException("工作流中的编码值不能为空");
		
		String version = processElement.attributeValue("version");
		if(StringUtil.isEmpty(version))
			throw new ProcessParseException("工作流中的版本值不能为空");
		
		Process process = new Process(processElement.attributeValue("name"), code, version, processElement.attributeValue("titleExpr"), processElement.attributeValue("pageID"));
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
		
		String id;
		for (Element element : elements) {
			id = element.attributeValue("id");
			if(StringUtil.isEmpty(id))
				throw new ProcessParseException("工作流中, 任务/网关/事件/连线的id值不能为空");
			
			if(element.getName().equals(startEventParser.elementName())) {
				if(startEvent != null)
					throw new ProcessParseException("工作流中只能配置一个起始事件");
				startEvent = startEventParser.parse(new TaskElement(id, element));
			}else if(element.getName().equals(flowParser.elementName())) {
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
		if(!flowElements.isEmpty())
			flowElements.clear();
	}

	/**
	 *  将任务和流进行连接
	 * @param task
	 * @param flowElements
	 * @param taskMap
	 * @param process
	 */
	private void linkTaskAndFlow(Task task, List<FlowElement> flowElements, Map<String, Object> taskMap, Process process) {
		boolean taskExistsFlow = false;
		for (int i = 0; i < flowElements.size(); i++) {
			if(task.getId().equals(flowElements.get(i).getSource())) {
				taskExistsFlow = true;
				FlowElement flowElement = flowElements.remove(i);
			
				Object taskObj = taskMap.get(flowElement.getTarget());
				if(taskObj == null)
					throw new ProcessParseException("工作流中不存在id="+flowElement.getTarget()+"的任务/网关/事件");
				
				Flow flow = flowParser.parse(flowElement);
				task.addFlow(flow);
				
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
					if(!(targetTask instanceof EndEvent || flowElements.isEmpty())) {
						linkTaskAndFlow(targetTask, flowElements, taskMap, process);
						i = -1;
					}
				}
			}
		}
		
		if(!taskExistsFlow)
			throw new ProcessParseException("工作流中, " + task.getName() + "不是结束事件, 必须配置相应的连线");
	}
	
	
	public static void main(String[] args) throws DocumentException {
		Document document = new SAXReader().read(new File("D:\\workspace4\\jbpm\\src\\test\\resources\\流程配置结构设计.bpm.xml"));
		Element processElement = document.getRootElement().element("process");
		
//		List<Element> elements = processElement.elements();
//		for (Element element : elements) {
//			System.out.println(element.getName());
//		}
		
		System.out.println(processElement.attributeValue("x") == null);
		
	}
}
