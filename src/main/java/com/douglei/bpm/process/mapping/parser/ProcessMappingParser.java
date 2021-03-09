package com.douglei.bpm.process.mapping.parser;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.Element;

import com.douglei.bpm.bean.CustomAutowired;
import com.douglei.bpm.bean.annotation.Bean;
import com.douglei.bpm.process.Type;
import com.douglei.bpm.process.mapping.AddOrCoverMappingEntity4Process;
import com.douglei.bpm.process.mapping.ProcessMapping;
import com.douglei.bpm.process.mapping.metadata.ProcessMetadata;
import com.douglei.bpm.process.mapping.metadata.ProcessMetadataAdapter;
import com.douglei.bpm.process.mapping.metadata.TaskMetadata;
import com.douglei.bpm.process.mapping.metadata.event.EndEventMetadata;
import com.douglei.bpm.process.mapping.metadata.event.StartEventMetadata;
import com.douglei.bpm.process.mapping.parser.event.StartEventParser;
import com.douglei.bpm.process.mapping.parser.flow.FlowParser;
import com.douglei.bpm.process.mapping.parser.temporary.data.FlowTemporaryData;
import com.douglei.bpm.process.mapping.parser.temporary.data.TaskTemporaryData;
import com.douglei.orm.mapping.MappingParseToolContext;
import com.douglei.orm.mapping.MappingParser;
import com.douglei.orm.mapping.MappingSubject;
import com.douglei.orm.mapping.handler.entity.AddOrCoverMappingEntity;
import com.douglei.tools.StringUtil;

/**
 * 
 * @author DougLei
 */
@Bean
@SuppressWarnings({"unchecked", "rawtypes"})
public class ProcessMappingParser extends MappingParser implements CustomAutowired {
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
	
	@Override
	public MappingSubject parse(AddOrCoverMappingEntity entity, InputStream input) throws Exception{
		Element rootElement = MappingParseToolContext.getMappingParseTool().getSAXReader().read(input).getRootElement();
		
		// 解析ProcessMetadata
		Element processElement = rootElement.element("process");
		ProcessMetadata processMetadata = new ProcessMetadata(((AddOrCoverMappingEntity4Process)entity).getId(), 
				processElement.attributeValue("code"), processElement.attributeValue("version"), processElement.attributeValue("name"), 
				processElement.attributeValue("title"), processElement.attributeValue("pageID"));
		
		// 构建流程结构
		buildProcessStruct(processMetadata, processElement.elements());
		
		return buildMappingSubjectByDom4j(new ProcessMapping(new ProcessMetadataAdapter(processMetadata)), rootElement);
	}
	
	/**
	 * 构建流程结构
	 * @param processMetadata
	 * @param elements
	 */
	private void buildProcessStruct(ProcessMetadata processMetadata, List<Element> elements) {
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
		processMetadata.setStartEvent(startEventMetadata);
		
		addTaskAndFlow(startEventMetadata, flowTemporaryDatas, taskMap, processMetadata);
		
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
	 *  添加任务和流
	 * @param sourceTask 
	 * @param flowTemporaryDatas
	 * @param taskMap
	 * @param processMetadata
	 */
	private void addTaskAndFlow(TaskMetadata sourceTask, List<FlowTemporaryData> flowTemporaryDatas, Map<String, Object> taskMap, ProcessMetadata processMetadata) {
		boolean taskExistsFlow = false;
		if(!flowTemporaryDatas.isEmpty()) {
			for (int i = 0; i < flowTemporaryDatas.size(); i++) {
				if(sourceTask.getId().equals(flowTemporaryDatas.get(i).getSource())) {
					taskExistsFlow = true;
					FlowTemporaryData flowTemporaryData = flowTemporaryDatas.remove(i--);
					
					Object taskObj = taskMap.get(flowTemporaryData.getTarget());
					if(taskObj == null)
						throw new ProcessParseException("流程中不存在id=["+flowTemporaryData.getTarget()+"]的任务");
					processMetadata.addFlow(flowParser.parse(flowTemporaryData));
					
					TaskMetadata targetTask = null;
					if(taskObj instanceof Element) {
						Element element = (Element) taskObj;
						targetTask = (TaskMetadata) parserMap.get(element.getName()).parse(new TaskTemporaryData(flowTemporaryData.getTarget(), element));
						taskMap.put(targetTask.getId(), targetTask);
					}else {
						targetTask = (TaskMetadata) taskObj;
					}
					if(targetTask != taskObj) { // 证明targetTask是第一次解析
						processMetadata.addTask(targetTask);
						if(!(targetTask instanceof EndEventMetadata)) {
							addTaskAndFlow(targetTask, flowTemporaryDatas, taskMap, processMetadata);
							i = -1;
						}
					}
				}
			}
		}
		
		if(!taskExistsFlow)
			throw new ProcessParseException("流程中id=[" + sourceTask.getId() + "]的任务, 不是结束事件, 必须配置相应的Flow");
	}
}
