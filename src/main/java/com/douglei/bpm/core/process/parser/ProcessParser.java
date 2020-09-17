package com.douglei.bpm.core.process.parser;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.douglei.bpm.bean.annotation.Attribute;
import com.douglei.bpm.bean.annotation.Bean;
import com.douglei.bpm.core.process.executer.Process;
import com.douglei.bpm.core.process.executer.task.event.StartEvent;
import com.douglei.bpm.core.process.parser.impl.flow.FlowElement;
import com.douglei.bpm.core.process.parser.impl.flow.FlowParser;
import com.douglei.bpm.core.process.parser.impl.task.event.StartEventParser;

/**
 * process解析器
 * @author DougLei
 */
@Bean(transaction = false)
public class ProcessParser implements Parser<String, Process>{
	
	@Attribute
	private FlowParser flowParser;
	@Attribute
	private StartEventParser startEventParser;
	
	@Override
	public String elementName() {
		return "process";
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public Process parse(String content) throws ProcessParseException {
		Document document;
		try {
			document = new SAXReader().read(new ByteArrayInputStream(content.getBytes()));
		} catch (DocumentException e) {
			throw new ProcessParseException(e);
		}
		
		Element processElement = document.getRootElement().element(elementName());
		Process process = new Process(processElement.attributeValue("name"), 
				processElement.attributeValue("code"), 
				processElement.attributeValue("version"),
				processElement.attributeValue("titleExpr"));
		
		List<FlowElement> flowElements = getFlowElements(processElement.elements(flowParser.elementName()));
		StartEvent startEvent = getStartEvent(processElement.elements(startEventParser.elementName()));
		
		
		
		return process;
	}
	
	// 获取流的元素集合
	private List<FlowElement> getFlowElements(List<Element> elements) {
		List<FlowElement> flowElements = new ArrayList<FlowElement>(elements.size());
		elements.forEach(element -> {
			flowElements.add(new FlowElement(element.attributeValue("source"), element));
		});
		return flowElements;
	}
	
	// 获取起始事件
	private StartEvent getStartEvent(List<Element> elements) {
		// TODO Auto-generated method stub
		return null;
	}


	public static void main(String[] args) throws DocumentException {
		Document document = new SAXReader().read(new File("D:\\workspace4\\jbpm\\src\\test\\resources\\流程配置结构设计.bpm.xml"));
		Element processElement = document.getRootElement().element("process");
		
		List<Element> elements = processElement.elements();
		for (Element element : elements) {
			System.out.println(element.getName());
		}
		
	}
}
