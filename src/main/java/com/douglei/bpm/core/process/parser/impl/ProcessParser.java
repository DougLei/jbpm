package com.douglei.bpm.core.process.parser.impl;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.douglei.bpm.bean.annotation.Bean;
import com.douglei.bpm.core.process.executer.Process;
import com.douglei.bpm.core.process.parser.Parser;
import com.douglei.bpm.core.process.parser.ProcessParseException;

/**
 * process解析器
 * @author DougLei
 */
@Bean(transaction = false)
public class ProcessParser implements Parser<String, Process>{
	
	
	@Override
	public Process parse(String content) throws ProcessParseException {
		Document document;
		try {
			document = new SAXReader().read(new ByteArrayInputStream(content.getBytes()));
		} catch (DocumentException e) {
			throw new ProcessParseException(e);
		}
		
		Element processElement = document.getRootElement().element("process");
		Process process = new Process(processElement.attributeValue("name"), 
				processElement.attributeValue("code"), 
				processElement.attributeValue("version"),
				processElement.attributeValue("titleExpr"));
		
		
		
		
		
		
		return process;
	}
	
	
	
	public static void main(String[] args) throws DocumentException {
		Document document = new SAXReader().read(new File("D:\\workspace4\\jbpm\\src\\test\\resources\\流程配置结构设计.bpm.xml"));
		Element processElement = document.getRootElement().element("process");
		
		List<Element> elements = processElement.selectNodes("");
		for (Element element : elements) {
			System.out.println(element.getName());
		}
		
		
	}
}
