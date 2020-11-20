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

import com.douglei.bpm.bean.Autowire;
import com.douglei.bpm.bean.Bean;
import com.douglei.bpm.bean.BeanFactory;
import com.douglei.bpm.process.node.Process;
import com.douglei.bpm.process.node.flow.Flow;
import com.douglei.bpm.process.node.task.Task;
import com.douglei.bpm.process.node.task.event.EndEvent;
import com.douglei.bpm.process.node.task.event.StartEvent;
import com.douglei.bpm.process.parser.flow.FlowParser;
import com.douglei.bpm.process.parser.flow.FlowTemporaryData;
import com.douglei.bpm.process.parser.task.TaskTemporaryData;
import com.douglei.bpm.process.parser.task.event.StartEventParser;
import com.douglei.tools.utils.StringUtil;

/**
 * process解析器
 * @author DougLei
 */
@SuppressWarnings({"unchecked", "rawtypes"})
@Bean(isTransaction = false)
public class ProcessParser {
	private Map<String, Parser> parserMap = new HashMap<String, Parser>();
	private StartEventParser startEventParser; // 冗余
	private FlowParser flowParser;
	
	@Autowire
	private BeanFactory beanFactory;
	
	public ProcessParser() {
		beanFactory.getInstances(Parser.class).forEach(parser -> {
			if(parser.getClass() == StartEventParser.class) {
				startEventParser = (StartEventParser) parser;
			}else if(parser.getClass() == FlowParser.class) {
				flowParser = (FlowParser) parser;
			}
			parserMap.put(parser.elementName(), parser);
		});
	}

	/**
	 * 解析流程
	 * @param processDefinitionId
	 * @param content
	 * @return
	 * @throws ProcessParseException
	 */
	public Process parse(int processDefinitionId, String content) throws ProcessParseException {
		try {
			Document document = new SAXReader().read(new ByteArrayInputStream(content.getBytes(StandardCharsets.UTF_8)));
			Element processElement = document.getRootElement().element("process");
			Process process = new Process(processDefinitionId, 
					processElement.attributeValue("code"), processElement.attributeValue("version"), processElement.attributeValue("name"), 
					processElement.attributeValue("title"), processElement.attributeValue("pageID"));
			buildProcessStruct(process, processElement.elements());
			return process;
		} catch (DocumentException e) {
			throw new ProcessParseException("读取流程配置内容时出现异常", e);
		}
	}
	
	/**
	 * 构建流程结构
	 * @param process
	 * @param elements
	 */
	private void buildProcessStruct(Process process, List<Element> elements) {
		StartEvent startEvent = null;
		List<FlowTemporaryData> flowTemporaryDatas = new ArrayList<FlowTemporaryData>(elements.size());
		Map<String, Object> taskMap = new HashMap<String, Object>();
		
		String elementName, id;
		for (Element element : elements) {
			elementName = element.getName();
			if(!parserMap.containsKey(elementName))
				throw new ProcessParseException("流程引擎不支持解析<"+elementName+">标签");
			
			id = element.attributeValue("id");
			if(StringUtil.isEmpty(id))
				throw new ProcessParseException("流程中连线/任务的id值不能为空");
			if(idExists4flowTemporaryDatas(id, flowTemporaryDatas) || (!taskMap.isEmpty() && taskMap.containsKey(id)) || (startEvent != null && startEvent.getId().equals(id)))
				throw new ProcessParseException("流程中连线/任务的id值出现重复: " + id);
			
			if(startEventParser.elementName().equals(elementName)) {
				if(startEvent != null)
					throw new ProcessParseException("流程中只能配置一个起始事件");
				startEvent = startEventParser.parse(new TaskTemporaryData(id, element));
			}else if(flowParser.elementName().equals(elementName)) {
				flowTemporaryDatas.add(new FlowTemporaryData(id, element));
			}else {
				taskMap.put(id, element);
			}
		}
		
		if(startEvent == null)
			throw new ProcessParseException("流程中必须配置起始事件");
		process.setStartEvent(startEvent);
		
		linkTaskAndFlow(startEvent, flowTemporaryDatas, taskMap, process);
		
		if(!flowTemporaryDatas.isEmpty())
			flowTemporaryDatas.clear();
		taskMap.clear();
	}
	
	/**
	 * 判断id是否已经存在于flowTemporaryDatas集合中
	 * @param id
	 * @param flowTemporaryDatas
	 * @return
	 */
	private boolean idExists4flowTemporaryDatas(String id, List<FlowTemporaryData> flowTemporaryDatas) {
		if(!flowTemporaryDatas.isEmpty()) {
			for (FlowTemporaryData td : flowTemporaryDatas) {
				if(td.getId().equals(id)) 
					return true;
			}
		}
		return false;
	}

	/**
	 *  将任务和流进行连接
	 * @param sourceTask 
	 * @param flowTemporaryDatas
	 * @param taskMap
	 * @param process
	 */
	private void linkTaskAndFlow(Task sourceTask, List<FlowTemporaryData> flowTemporaryDatas, Map<String, Object> taskMap, Process process) {
		boolean taskExistsFlow = false;
		if(!flowTemporaryDatas.isEmpty()) {
			for (int i = 0; i < flowTemporaryDatas.size(); i++) {
				if(sourceTask.getId().equals(flowTemporaryDatas.get(i).getSource())) {
					taskExistsFlow = true;
					FlowTemporaryData flowTemporaryData = flowTemporaryDatas.remove(i--);
					
					Object taskObj = taskMap.get(flowTemporaryData.getTarget());
					if(taskObj == null)
						throw new ProcessParseException("流程中不存在id=["+flowTemporaryData.getTarget()+"]的任务");
					
					Flow flow = flowParser.parse(flowTemporaryData);
					sourceTask.addFlow(flow);
					
					Task targetTask = null;
					if(taskObj instanceof Element) {
						Element element = (Element) taskObj;
						targetTask = (Task) parserMap.get(element.getName()).parse(new TaskTemporaryData(flowTemporaryData.getTarget(), element));
						taskMap.put(targetTask.getId(), targetTask);
					}else {
						targetTask = (Task) taskObj;
					}
					
					flow.setTargetTask(targetTask);
					if(targetTask != taskObj) { // 证明targetTask是第一次解析
						process.addTask(targetTask);
						if(!(targetTask instanceof EndEvent)) {
							linkTaskAndFlow(targetTask, flowTemporaryDatas, taskMap, process);
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
