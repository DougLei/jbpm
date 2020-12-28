package com.douglei.bpm.process.parser;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.douglei.bpm.bean.CustomAutowired;
import com.douglei.bpm.bean.annotation.Bean;
import com.douglei.bpm.process.Type;
import com.douglei.bpm.process.metadata.ProcessMetadata;
import com.douglei.bpm.process.metadata.TaskMetadata;
import com.douglei.bpm.process.metadata.event.EndEventMetadata;
import com.douglei.bpm.process.metadata.event.StartEventMetadata;
import com.douglei.bpm.process.metadata.flow.FlowMetadata;
import com.douglei.bpm.process.parser.event.StartEventParser;
import com.douglei.bpm.process.parser.flow.FlowParser;
import com.douglei.bpm.process.parser.tmp.data.FlowTemporaryData;
import com.douglei.bpm.process.parser.tmp.data.TaskTemporaryData;
import com.douglei.tools.utils.StringUtil;

/**
 * process解析器
 * @author DougLei
 */
@Bean
@SuppressWarnings({"unchecked", "rawtypes"})
public class ProcessParser implements CustomAutowired{
	private static final Logger logger = LoggerFactory.getLogger(ProcessParser.class);
	private Map<String, Parser> parserMap = new HashMap<String, Parser>();
	private StartEventParser startEventParser;
	private FlowParser flowParser;
	
	@Override
	public void setFields(Map<Class<?>, Object> beanContainer) {
		((List<Parser>)beanContainer.get(Parser.class)).forEach(parser -> {
			if(parser.getType() == Type.START_EVENT) {
				startEventParser = (StartEventParser) parser;
			}else if(parser.getType() == Type.FLOW) {
				flowParser = (FlowParser) parser;
			}
			parserMap.put(parser.getType().getName(), parser);
		});
	}
	
	/**
	 * 解析流程
	 * @param processDefinitionId
	 * @param content
	 * @return
	 */
	public ProcessMetadata parse(int processDefinitionId, String content) {
		try {
			logger.debug("解析的流程配置内容为: {}", content);
			Document document = new SAXReader().read(new ByteArrayInputStream(content.getBytes(StandardCharsets.UTF_8)));
			Element processElement = document.getRootElement().element("process");
			ProcessMetadata process = new ProcessMetadata(processDefinitionId, 
					processElement.attributeValue("code"), processElement.attributeValue("version"), processElement.attributeValue("name"), 
					processElement.attributeValue("title"), processElement.attributeValue("pageID"));
			buildProcessStruct(process, processElement.elements());
			return process;
		} catch (Exception e) {
			throw new ProcessParseException("读取流程配置内容时出现异常", e);
		}
	}
	
	/**
	 * 构建流程结构
	 * @param process
	 * @param elements
	 */
	private void buildProcessStruct(ProcessMetadata process, List<Element> elements) {
		StartEventMetadata startEventMetadata = null;
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
			if(id.length() > 50)
				throw new ProcessParseException("流程中连线/任务的id值长度不能超过50");
			if(idExists4flowTemporaryDatas(id, flowTemporaryDatas) || (!taskMap.isEmpty() && taskMap.containsKey(id)) || (startEventMetadata != null && startEventMetadata.getId().equals(id)))
				throw new ProcessParseException("流程中连线/任务的id值出现重复: " + id);
			
			if(startEventParser.getType().getName().equals(elementName)) {
				if(startEventMetadata != null)
					throw new ProcessParseException("流程中只能配置一个起始事件");
				startEventMetadata = startEventParser.parse(new TaskTemporaryData(id, element));
			}else if(flowParser.getType().getName().equals(elementName)) {
				flowTemporaryDatas.add(new FlowTemporaryData(id, element));
			}else {
				taskMap.put(id, element);
			}
		}
		
		if(startEventMetadata == null)
			throw new ProcessParseException("流程中必须配置起始事件");
		process.setStartEvent(startEventMetadata);
		
		linkTaskAndFlow(startEventMetadata, flowTemporaryDatas, taskMap, process);
		
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
	private void linkTaskAndFlow(TaskMetadata sourceTask, List<FlowTemporaryData> flowTemporaryDatas, Map<String, Object> taskMap, ProcessMetadata process) {
		boolean taskExistsFlow = false;
		if(!flowTemporaryDatas.isEmpty()) {
			for (int i = 0; i < flowTemporaryDatas.size(); i++) {
				if(sourceTask.getId().equals(flowTemporaryDatas.get(i).getSource())) {
					taskExistsFlow = true;
					FlowTemporaryData flowTemporaryData = flowTemporaryDatas.remove(i--);
					
					Object taskObj = taskMap.get(flowTemporaryData.getTarget());
					if(taskObj == null)
						throw new ProcessParseException("流程中不存在id=["+flowTemporaryData.getTarget()+"]的任务");
					process.addFlow(flowParser.parse(flowTemporaryData));
					
					TaskMetadata targetTask = null;
					if(taskObj instanceof Element) {
						Element element = (Element) taskObj;
						targetTask = (TaskMetadata) parserMap.get(element.getName()).parse(new TaskTemporaryData(flowTemporaryData.getTarget(), element));
						taskMap.put(targetTask.getId(), targetTask);
					}else {
						targetTask = (TaskMetadata) taskObj;
					}
					if(targetTask != taskObj) { // 证明targetTask是第一次解析
						process.addTask(targetTask);
						if(!(targetTask instanceof EndEventMetadata)) {
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
