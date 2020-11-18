package com.douglei.bpm.process.parser;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.douglei.bpm.bean.Bean;
import com.douglei.bpm.module.repository.definition.entity.ProcessDefinition;
import com.douglei.bpm.process.executor.Process;
import com.douglei.bpm.process.executor.flow.Flow;
import com.douglei.bpm.process.executor.task.Task;
import com.douglei.bpm.process.executor.task.event.EndEvent;
import com.douglei.bpm.process.executor.task.event.StartEvent;
import com.douglei.bpm.process.parser.flow.FlowMetadata;
import com.douglei.bpm.process.parser.flow.FlowParser;
import com.douglei.bpm.process.parser.task.TaskMetadata;
import com.douglei.bpm.process.parser.task.event.StartEventParser;
import com.douglei.tools.instances.resource.scanner.impl.ClassScanner;
import com.douglei.tools.utils.StringUtil;
import com.douglei.tools.utils.reflect.ClassLoadUtil;
import com.douglei.tools.utils.reflect.ConstructorUtil;

/**
 * process解析器
 * @author DougLei
 */
@Bean(isTransaction = false)
@SuppressWarnings({ "rawtypes", "unchecked" })
public class ProcessParser {
	private static Map<String, Parser<TaskMetadata, ? extends Task>> parserMap = new HashMap<String, Parser<TaskMetadata,? extends Task>>();
	private static StartEventParser startEventParser;
	private static FlowParser flowParser;
	static {
		new ClassScanner().scan(ProcessParser.class.getPackage().getName()).forEach(classpath -> {
			Class<?> clazz = ClassLoadUtil.loadClass(classpath);
			if(clazz.getAnnotation(ParserBean.class) != null) {
				Parser parser = (Parser) ConstructorUtil.newInstance(clazz);
				if("startEvent".equals(parser.elementName())) {
					startEventParser = (StartEventParser)parser;
				}else if("flow".equals(parser.elementName())) {
					flowParser = (FlowParser)parser;
				}
				parserMap.put(parser.elementName(), parser);
			}
		});
	}
	
	@SuppressWarnings("unchecked")
	public Process parse(ProcessDefinition processDefinition) throws ProcessParseException {
		Document document;
		try {
			document = new SAXReader().read(new ByteArrayInputStream(processDefinition.getContent().getBytes(StandardCharsets.UTF_8)));
		} catch (DocumentException e) {
			throw new ProcessParseException("读取流程配置内容时出现异常", e);
		}
		
		Element processElement = document.getRootElement().element("process");
		String code = processElement.attributeValue("code");
		if(StringUtil.isEmpty(code))
			throw new ProcessParseException("流程的编码值不能为空");
		
		String version = processElement.attributeValue("version");
		if(StringUtil.isEmpty(version))
			throw new ProcessParseException("流程的版本值不能为空");
		
		Process process = new Process(processDefinitionId, code, version, processElement.attributeValue("name"), processElement.attributeValue("title"), processElement.attributeValue("pageID"));
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
		List<FlowMetadata> flowMetadatas = new ArrayList<FlowMetadata>(elements.size());
		Map<String, Object> taskMap = new HashMap<String, Object>();
		
		String elementName, id;
		for (Element element : elements) {
			elementName = element.getName();
			if(!parserMap.containsKey(elementName))
				throw new ProcessParseException("流程引擎不支持解析<"+elementName+">标签");
			
			id = element.attributeValue("id");
			if(StringUtil.isEmpty(id))
				throw new ProcessParseException("流程中连线/任务的id值不能为空");
			if(idExists4flowMetadatas(id, flowMetadatas) || (!taskMap.isEmpty() && taskMap.containsKey(id)) || (startEvent != null && startEvent.getId().equals(id)))
				throw new ProcessParseException("流程中连线/任务的id值出现重复: " + id);
			
			if(startEventParser.elementName().equals(elementName)) {
				if(startEvent != null)
					throw new ProcessParseException("流程中只能配置一个起始事件");
				startEvent = startEventParser.parse(new TaskMetadata(id, element));
			}else if(flowParser.elementName().equals(elementName)) {
				flowMetadatas.add(new FlowMetadata(id, element));
			}else {
				taskMap.put(id, element);
			}
		}
		
		if(startEvent == null)
			throw new ProcessParseException("流程中必须配置起始事件");
		process.setStartEvent(startEvent);
		
		linkTaskAndFlow(startEvent, flowMetadatas, taskMap, process);
		
		if(!flowMetadatas.isEmpty())
			flowMetadatas.clear();
		taskMap.clear();
	}
	
	/**
	 * 判断id是否已经存在于flowMetadatas集合中
	 * @param id
	 * @param flowMetadatas
	 * @return
	 */
	private boolean idExists4flowMetadatas(String id, List<FlowMetadata> flowMetadatas) {
		if(!flowMetadatas.isEmpty()) {
			for (FlowMetadata fm : flowMetadatas) {
				if(fm.getId().equals(id)) 
					return true;
			}
		}
		return false;
	}

	/**
	 *  将任务和流进行连接
	 * @param sourceTask 
	 * @param flowMetadatas
	 * @param taskMap
	 * @param process
	 */
	private void linkTaskAndFlow(Task sourceTask, List<FlowMetadata> flowMetadatas, Map<String, Object> taskMap, Process process) {
		boolean taskExistsFlow = false;
		if(!flowMetadatas.isEmpty()) {
			for (int i = 0; i < flowMetadatas.size(); i++) {
				if(sourceTask.getId().equals(flowMetadatas.get(i).getSource())) {
					taskExistsFlow = true;
					FlowMetadata flowMetadata = flowMetadatas.remove(i--);
					
					Object taskObj = taskMap.get(flowMetadata.getTarget());
					if(taskObj == null)
						throw new ProcessParseException("流程中不存在id=["+flowMetadata.getTarget()+"]的任务");
					
					Flow flow = flowParser.parse(flowMetadata);
					sourceTask.addFlow(flow);
					
					Task targetTask = null;
					if(taskObj instanceof Element) {
						targetTask = parserMap.get(((Element)taskObj).getName()).parse(new TaskMetadata(flowMetadata.getTarget(), (Element)taskObj));
						taskMap.put(targetTask.getId(), targetTask);
					}else {
						targetTask = (Task) taskObj;
					}
					
					flow.setTargetTask(targetTask);
					if(targetTask != taskObj) { // 证明targetTask是第一次解析
						process.addTask(targetTask);
						if(!(targetTask instanceof EndEvent)) {
							linkTaskAndFlow(targetTask, flowMetadatas, taskMap, process);
							i = -1;
						}
					}
				}
			}
		}
		
		if(!taskExistsFlow)
			throw new ProcessParseException("流程中id=[" + sourceTask.getId() + "]的任务, 不是结束事件, 必须配置相应的连线");
	}
}
